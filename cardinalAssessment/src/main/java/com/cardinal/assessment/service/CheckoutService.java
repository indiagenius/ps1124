package com.cardinal.assessment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.cardinal.assessment.exception.CheckoutException;
import com.cardinal.assessment.model.RentalAgreement;
import com.cardinal.assessment.model.Tool;
import com.cardinal.assessment.model.ToolCharge;
import com.cardinal.assessment.service.holiday.HolidayService;

/**
 * Service to complete the tool rental checkout process.
 */
public class CheckoutService {

	/**
	 * Tool service. In practice, this will be populated by dependency injection.
	 */
	private ToolService toolService;

	/**
	 * Tool charge service. In practice, this will be populated by dependency
	 * injection.
	 */
	private ToolChargeService toolChargeService;

	/**
	 * Holiday service. In practice, this will be populated by dependency injection.
	 */
	private HolidayService holidayService;

	/**
	 * Constructor to instantiate the checkout service
	 * 
	 * @param toolService
	 * @param toolChargeService
	 * @param holidayService
	 */
	public CheckoutService(ToolService toolService, ToolChargeService toolChargeService,
			HolidayService holidayService) {
		this.toolService = toolService;
		this.toolChargeService = toolChargeService;
		this.holidayService = holidayService;
	}

	/**
	 * Completes the checkout process for a customer.
	 * 
	 * @param toolCode
	 * @param rentalDayCount
	 * @param discountPercent
	 * @param checkoutDate
	 * @return RentalAgreement - containing the unique rental agreement for a given
	 *         tool rental.
	 * @throws CheckoutException
	 */
	public RentalAgreement checkout(String toolCode, int rentalDayCount, int discountPercent, Date checkoutDate)
			throws CheckoutException {

		if (discountPercent < 0 || discountPercent > 100) {
			throw new CheckoutException("Discount Percent should only be between 0% and 100%");
		}

		if (rentalDayCount < 1) {
			throw new CheckoutException("Number of rental days should be greater than 1");
		}

		Tool tool = toolService.getToolForCode(toolCode);
		ToolCharge toolCharge = toolChargeService.getToolChargeForToolType(tool.toolType());

		Date dueDate = computeDueDate(checkoutDate, rentalDayCount);
		int chargeDays = computeChargeableDays(checkoutDate, dueDate, toolCharge);
		double preDiscountCharge = computePreDiscountCharge(chargeDays, toolCharge);
		double discountAmount = computeDiscountAmount(discountPercent, preDiscountCharge);
		double finalCharge = computeFinalCharge(preDiscountCharge, discountAmount);

		RentalAgreement rentalAgreement = new RentalAgreement(1, toolCode, tool.toolType(), tool.brand(),
				rentalDayCount, checkoutDate, dueDate, toolCharge.dailyCharge(), chargeDays, preDiscountCharge,
				discountPercent, discountAmount, finalCharge);

		return rentalAgreement;
	}

	private double computeFinalCharge(double preDiscountCharge, double discountAmount) {
		return preDiscountCharge - discountAmount;
	}

	private double computeDiscountAmount(int discountPercent, double preDiscountCharge) {
		double discountAmount = (discountPercent / 100.0) * preDiscountCharge;
		BigDecimal roundedDiscount = new BigDecimal(discountAmount).setScale(2, RoundingMode.HALF_UP);

		return roundedDiscount.doubleValue();
	}

	private double computePreDiscountCharge(int chargeDays, ToolCharge toolCharge) {
		double preDiscountCharge = Math.round(chargeDays * toolCharge.dailyCharge() * 100) / 100.0;
		BigDecimal roundedPreDiscountCharge = new BigDecimal(preDiscountCharge).setScale(2, RoundingMode.HALF_UP);

		return roundedPreDiscountCharge.doubleValue();
	}

	private int computeChargeableDays(Date checkoutDate, Date dueDate, ToolCharge toolCharge) {
		Calendar start = Calendar.getInstance(TimeZone.getTimeZone("EST"));

		start.setTime(checkoutDate);
		/*
		 * Adding one day since the checkout date is not included in the charge days
		 */
		start.add(Calendar.DAY_OF_MONTH, 1);

		Calendar end = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		end.setTime(dueDate);

		int chargeableDays = 0;

		int numberOfHolidaysInDateRange = 0;
		int numberOfWeekdaysInDateRange = 0;
		int numberOfWeekendsInDateRange = 0;

		while (start.before(end) || start.equals(end)) {
			if (isDateAWeekend(start)) {
				numberOfWeekendsInDateRange++;
			} else if (isDateAHoliday(start)) {
				numberOfHolidaysInDateRange++;
			} else {
				numberOfWeekdaysInDateRange++;
			}

			/*
			 * Increment the start date by one day to continue checking for the next date
			 */
			start.add(Calendar.DATE, 1);
		}

		if (numberOfHolidaysInDateRange > 0 && toolCharge.isHolidayCharge()) {
			chargeableDays = numberOfHolidaysInDateRange;
		}

		if (numberOfWeekendsInDateRange > 0 && toolCharge.isWeekendCharge()) {
			chargeableDays += numberOfWeekendsInDateRange;
		}

		if (numberOfWeekdaysInDateRange > 0 && toolCharge.isWeekdayCharge()) {
			chargeableDays += numberOfWeekdaysInDateRange;
		}

		return chargeableDays;
	}

	private Date computeDueDate(Date checkoutDate, int rentalDayCount) {
		/*
		 * Assuming the time zone to be fixed as EST.
		 */
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		calendar.setTime(checkoutDate);
		calendar.add(Calendar.DAY_OF_MONTH, rentalDayCount);

		return calendar.getTime();
	}

	private boolean isDateAHoliday(Calendar start) {
		return holidayService.isDateAHoliday(start.getTime());
	}

	private boolean isDateAWeekend(Calendar calendar) {
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		}
		return false;
	}
}
