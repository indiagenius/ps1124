package com.cardinal.assessment.service.holiday.utils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class HolidayComputationUtilsTest {

	@Test
	public void testIfIndependenceDayIsAlwaysComputedOnTheNearestWeekday() {

		Calendar july4thOnSatCalendar = Calendar.getInstance();
		july4thOnSatCalendar.set(Calendar.YEAR, 2020);

		Calendar july4thOnSunCalendar = Calendar.getInstance();
		july4thOnSunCalendar.set(Calendar.YEAR, 2021);

		Calendar independenceDayHoliday = HolidayComputationUtils
				.computeIndependenceDayHolidayDate(july4thOnSatCalendar);
		assertEquals(Calendar.FRIDAY, independenceDayHoliday.get(Calendar.DAY_OF_WEEK));

		independenceDayHoliday = HolidayComputationUtils.computeIndependenceDayHolidayDate(july4thOnSunCalendar);
		assertEquals(Calendar.MONDAY, independenceDayHoliday.get(Calendar.DAY_OF_WEEK));
	}

	@Test
	public void testIfLaborDayIsAlwaysTheFirstMondayOfSeptember() {
		Calendar laborDayCalendar = Calendar.getInstance();
		laborDayCalendar.set(Calendar.YEAR, 2024);

		Calendar laborDayHoliday = HolidayComputationUtils.computeLaborDayHolidayDate(laborDayCalendar);
		assertEquals(Calendar.MONDAY, laborDayHoliday.get(Calendar.DAY_OF_WEEK));
		assertEquals(1, laborDayHoliday.get(Calendar.WEEK_OF_MONTH));
		assertEquals(2, laborDayHoliday.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.SEPTEMBER, laborDayHoliday.get(Calendar.MONTH));

	}
}
