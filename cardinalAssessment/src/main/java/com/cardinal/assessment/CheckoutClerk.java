package com.cardinal.assessment;

import java.util.Date;

import com.cardinal.assessment.exception.CheckoutException;
import com.cardinal.assessment.model.RentalAgreement;
import com.cardinal.assessment.service.CheckoutService;
import com.cardinal.assessment.service.ToolChargeService;
import com.cardinal.assessment.service.ToolService;
import com.cardinal.assessment.service.holiday.HolidayService;

public class CheckoutClerk {

	public static void main(String[] args) {
		HolidayService holidayService = new HolidayService();
		ToolService toolService = new ToolService();
		ToolChargeService toolChargeService = new ToolChargeService();
		CheckoutService checkoutService = new CheckoutService(toolService, toolChargeService, holidayService);
		try {
			RentalAgreement rentalAgreement = checkoutService.checkout("CHNS", 10, 12, new Date());
			rentalAgreement.print();
			
			/*
			 * Output is:
			 * Tool code: CHNS
			 * Tool type: Chainsaw
			 * Tool brand: Stihl
			 * Rental days: 10
			 * Checkout date: 11/10/24
			 * Due date: 11/20/24
			 * Daily rental charge: $1.49
			 * Charge days: 8
			 * Pre-discount charge: $11.92
			 * Discount %: 12%
			 * Discount amount: $1.43
			 * Final charge: $10.49
			 */
			
		} catch (CheckoutException e) {
			e.printStackTrace();
		}
		
	}
}