package com.cardinal.assessment.service.holiday.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Utility methods to compute the holidays
 */
public class HolidayComputationUtils {

	/**
	 * Computes date for the independence day holiday
	 * 
	 * @param calendar
	 * @return
	 */
	public static Calendar computeIndependenceDayHolidayDate(Calendar calendar) {
		/*
		 * Safely assuming that Independence day always falls on July 4th
		 */
		Calendar july4thCalendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		july4thCalendar.setTime(calendar.getTime());
		july4thCalendar.set(Calendar.MONTH, Calendar.JULY);
		july4thCalendar.set(Calendar.DAY_OF_MONTH, 4);

		if (july4thCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			july4thCalendar.add(Calendar.DAY_OF_MONTH, -1);
		} else if (july4thCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			july4thCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		return july4thCalendar;
	}

	/**
	 * Computes date for the labor day holiday
	 * 
	 * @param calendar
	 * @return
	 */
	public static Calendar computeLaborDayHolidayDate(Calendar calendar) {
		/*
		 * Safely assuming that Labor day always falls on the first Monday of September
		 */
		Calendar laborDayCalendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		laborDayCalendar.setTime(calendar.getTime());
		laborDayCalendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
		laborDayCalendar.set(Calendar.WEEK_OF_MONTH, 1);
		laborDayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return laborDayCalendar;
	}
}
