package com.cardinal.assessment.model;

import com.cardinal.assessment.model.constants.ToolType;

/**
 * Immutable tool charge
 */
public record ToolCharge(ToolType toolType, double dailyCharge, boolean isWeekdayCharge, boolean isWeekendCharge,
		boolean isHolidayCharge) {
}
