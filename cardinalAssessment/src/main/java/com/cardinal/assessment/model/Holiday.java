package com.cardinal.assessment.model;

import java.util.Calendar;

import com.cardinal.assessment.model.constants.HolidayName;

/**
 * Immutable holiday
 */
public record Holiday(HolidayName holidayName, Calendar holidayDate) {
}
