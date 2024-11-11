package com.cardinal.assessment.model;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import com.cardinal.assessment.model.constants.ToolType;

/**
 * Immutable rental agreement
 */
public record RentalAgreement(int rentalAgreementId, String toolCode, ToolType toolType, String toolBrand,
		int rentalDays, Date checkoutDate, Date dueDate, double dailyRentalCharge, int chargeDays,
		double preDiscountCharge, int discountPercent, double discountAmount, double finalCharge) {

	public void print() {

		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		currencyFormatter.setCurrency(Currency.getInstance("USD"));

		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");

		StringBuffer printableRentalAgreement = new StringBuffer();
		printableRentalAgreement.append("Tool code: ").append(toolCode).append("\n").append("Tool type: ")
				.append(toolType).append("\n").append("Tool brand: ").append(toolBrand).append("\n")
				.append("Rental days: ").append(rentalDays).append("\n").append("Checkout date: ")
				.append(dateFormatter.format(checkoutDate)).append("\n").append("Due date: ")
				.append(dateFormatter.format(dueDate)).append("\n").append("Daily rental charge: ")
				.append(currencyFormatter.format(dailyRentalCharge)).append("\n").append("Charge days: ")
				.append(chargeDays).append("\n").append("Pre-discount charge: ")
				.append(currencyFormatter.format(preDiscountCharge)).append("\n").append("Discount %: ")
				.append(discountPercent).append("%").append("\n").append("Discount amount: ")
				.append(currencyFormatter.format(discountAmount)).append("\n").append("Final charge: ")
				.append(currencyFormatter.format(finalCharge)).append("\n");

		System.out.println(printableRentalAgreement);
	}
}