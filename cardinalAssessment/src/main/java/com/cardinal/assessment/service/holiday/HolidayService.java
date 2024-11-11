package com.cardinal.assessment.service.holiday;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.TimeZone;

import com.cardinal.assessment.model.Holiday;
import com.cardinal.assessment.model.constants.HolidayName;
import com.cardinal.assessment.service.holiday.utils.HolidayComputationUtils;

/**
 * Service to manage and retrieve the holidays
 */
public class HolidayService {

	/**
	 * Cache to store the holidays, preferably with a TTL
	 */
	private EnumMap<HolidayName, Holiday> holidays;

	/**
	 * Returns whether a provided date falls on a holiday.
	 * 
	 * @param date
	 * @return
	 */
	public boolean isDateAHoliday(Date date) {

		Calendar dateCalendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		dateCalendar.setTime(date);

		for (Holiday holiday : holidays.values()) {
			Calendar holidayCalendar = holiday.holidayDate();
			if (holidayCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
					&& holidayCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
					&& holidayCalendar.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This init method populates the holidays in the holiday service cache. In
	 * practice, there will a database call to fetch these details and kept in cache
	 * with a TTL
	 */
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		populateHolidays(calendar);
	}

	private void populateHolidays(Calendar calendar) {
		Holiday independenceDayHoliday = new Holiday(HolidayName.INDEPENDENCE_DAY,
				HolidayComputationUtils.computeIndependenceDayHolidayDate(calendar));
		Holiday laborDayHoliday = new Holiday(HolidayName.LABOR_DAY,
				HolidayComputationUtils.computeLaborDayHolidayDate(calendar));

		holidays = new EnumMap<HolidayName, Holiday>(HolidayName.class);
		holidays.put(HolidayName.INDEPENDENCE_DAY, independenceDayHoliday);
		holidays.put(HolidayName.LABOR_DAY, laborDayHoliday);
	}
}
