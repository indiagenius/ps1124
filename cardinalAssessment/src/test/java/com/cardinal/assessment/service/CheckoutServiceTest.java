package com.cardinal.assessment.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cardinal.assessment.exception.CheckoutException;
import com.cardinal.assessment.model.RentalAgreement;
import com.cardinal.assessment.service.holiday.HolidayService;

public class CheckoutServiceTest {

	private static CheckoutService checkoutService;
	private static Calendar july1stCalendar2024;
	private static Calendar september1stCalendar2024;

	@BeforeClass
	public static void setupBeforeAllTests() {
		HolidayService holidayService = new HolidayService();
		ToolService toolService = new ToolService();
		ToolChargeService toolChargeService = new ToolChargeService();

		checkoutService = new CheckoutService(toolService, toolChargeService, holidayService);

		july1stCalendar2024 = Calendar.getInstance();
		july1stCalendar2024.set(Calendar.YEAR, 2024);
		july1stCalendar2024.set(Calendar.MONTH, Calendar.JULY);
		july1stCalendar2024.set(Calendar.DAY_OF_MONTH, 1);

		september1stCalendar2024 = Calendar.getInstance();
		september1stCalendar2024.set(Calendar.YEAR, 2024);
		september1stCalendar2024.set(Calendar.MONTH, Calendar.SEPTEMBER);
		september1stCalendar2024.set(Calendar.DAY_OF_MONTH, 1);
	}

	@AfterClass
	public static void cleanupAfterAlltests() {
		checkoutService = null;
		july1stCalendar2024 = null;
		september1stCalendar2024 = null;
	}

	@Test(expected = CheckoutException.class)
	public void testIfExceptionIsReturnedWhenNumberofRentalsIsLessThanZero() throws CheckoutException {
		checkoutService.checkout("CHNS", 0, 10, new Date());
	}

	@Test(expected = CheckoutException.class)
	public void testIfExceptionIsReturnedWhenPercentageDiscountIsLessThanZero() throws CheckoutException {
		checkoutService.checkout("CHNS", 10, -1, new Date());
	}

	@Test(expected = CheckoutException.class)
	public void testIfExceptionIsReturnedWhenPercentageDiscountIsGreaterThanOneHundred() throws CheckoutException {
		checkoutService.checkout("CHNS", 10, 101, new Date());
	}

	@Test
	public void testIfExceptionIsReturnedWithNonEmptyMessageWhenNumberofRentalsIsLessThanZero()
			throws CheckoutException {
		Exception exception = assertThrows(CheckoutException.class, () -> {
			checkoutService.checkout("CHNS", 0, 10, new Date());
		});

		assertTrue(exception.getMessage().length() > 0);
	}

	@Test
	public void testIfExceptionIsReturnedWithNonEmptyMessageWhenPercentageDiscountIsLessThanZero()
			throws CheckoutException {
		Exception exception = assertThrows(CheckoutException.class, () -> {
			checkoutService.checkout("CHNS", 10, -1, new Date());
		});

		assertTrue(exception.getMessage().length() > 0);
	}

	@Test
	public void testIfExceptionIsReturnedWithNonEmptyMessageWhenPercentageDiscountIsGreaterThanOneHundred()
			throws CheckoutException {
		Exception exception = assertThrows(CheckoutException.class, () -> {
			checkoutService.checkout("CHNS", 10, 101, new Date());
		});

		assertTrue(exception.getMessage().length() > 0);
	}

	@Test
	public void testIfCorrectNumberofChargeDaysIsCalculatedWhenHolidayChargeIsApplicableAndWeekendChargeIsNotApplicable()
			throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 0, july1stCalendar2024.getTime());
		assertEquals(8, rentalAgreement.chargeDays());
	}
	
	@Test
	public void testIfCorrectNumberofChargeDaysIsCalculatedWhenHolidayChargeIsNotApplicableAndWeekendChargeIsApplicable()
			throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("LADW", 10, 0, july1stCalendar2024.getTime());
		assertEquals(9, rentalAgreement.chargeDays());
	}
	
	@Test
	public void testIfCorrectNumberofChargeDaysIsCalculatedWhenHolidayChargeAndWeekendChargeAreBothNotApplicable()
			throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("JAKR", 10, 0, july1stCalendar2024.getTime());
		assertEquals(7, rentalAgreement.chargeDays());
	}

	@Test
	public void testIfCorrectDiscountIsCalculatedWhenADiscountIsApplied() throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 12, new Date());
		assertEquals(1.43, rentalAgreement.discountAmount(),0.0);
	}
	
	@Test
	public void testIfNoDiscountIsCalculatedWhenADiscountIsZeroPercent() throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 0, new Date());
		assertEquals(0.0, rentalAgreement.discountAmount(),0.0);
	}
	
	@Test
	public void testIfCorrectFinalChargeisCalculatedWhenNoDiscountIsApplied() throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 0, new Date());
		assertEquals(11.92, rentalAgreement.finalCharge(),0.0);
	}
	
	@Test
	public void testIfRentalIsFreeWhenDiscountIsHundredPercent() throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 100, new Date());
		assertEquals(11.92, rentalAgreement.discountAmount(),0.0);
		assertEquals(0, rentalAgreement.finalCharge(),0.0);
	}

	@Test
	public void testIfIndependenceDayHolidayisCalculatedCorrectly() throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 0, july1stCalendar2024.getTime());
		/*
		 * We have the following numbers for total 10 rental days - 
		 * Number of weekdays - 7
		 * Number of weekends - 2
		 * Number of holidays - 1
		 * 
		 * For CHNS, we are charging for weekdays and holidays only, so total number of charge days is 8
		 */
		assertEquals(8, rentalAgreement.chargeDays());
	}

	@Test
	public void testIfLaborDayHolidayisCalculatedCorrectly() throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 0, september1stCalendar2024.getTime());
		/*
		 * We have the following numbers for total 10 rental days - 
		 * Number of weekdays - 7
		 * Number of weekends - 2
		 * Number of holidays - 1
		 * 
		 * For CHNS, we are charging for weekdays and holidays only, so total number of charge days is 8
		 */
		assertEquals(8, rentalAgreement.chargeDays());
	}

	@Test
	public void testIfWeekendsAreCalculatedCorrectly() throws CheckoutException {
		RentalAgreement rentalAgreement = checkoutService.checkout("LADW", 10, 0, september1stCalendar2024.getTime());
		/*
		 * We have the following numbers for total 10 rental days - 
		 * Number of weekdays - 7
		 * Number of weekends - 2
		 * Number of holidays - 1
		 * 
		 * For CHNS, we are charging for weekdays and weekends only, so total number of charge days is 9
		 */
		assertEquals(9, rentalAgreement.chargeDays());
	}
}