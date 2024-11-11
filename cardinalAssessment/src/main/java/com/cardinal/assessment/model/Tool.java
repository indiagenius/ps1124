package com.cardinal.assessment.model;

import com.cardinal.assessment.model.constants.ToolType;

/**
 * Immutable tool
 */
public record Tool(String toolCode, ToolType toolType, String brand) {
}