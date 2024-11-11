package com.cardinal.assessment.service;

import java.util.HashMap;
import java.util.Map;

import com.cardinal.assessment.model.Tool;
import com.cardinal.assessment.model.constants.ToolType;

/**
 * Service to manage and retrieve the rental tools
 */
public class ToolService {

	/**
	 * Assuming that the tools are cached in the service. In real life, this could
	 * be fetched from database and cached with a TTL to reduce network round trip
	 * and latency
	 */
	private Map<String, Tool> tools;

	/**
	 * This init method populates the tools in the service cache. In practice, there
	 * will a database call to fetch these details and kept in cache with a TTL
	 */
	{
		populateTools();
	}

	private void populateTools() {
		Tool toolStihlChainsaw = new Tool("CHNS", ToolType.Chainsaw, "Stihl");
		Tool toolWernerLadder = new Tool("LADW", ToolType.Ladder, "Werner");
		Tool toolDewaltJackhammer = new Tool("JAKD", ToolType.Jackhammer, "DeWalt");
		Tool toolRidgidJackhammer = new Tool("JAKR", ToolType.Jackhammer, "DeWalt");

		Map<String, Tool> toolsWithTheirCode = new HashMap<String, Tool>();
		toolsWithTheirCode.put(toolStihlChainsaw.toolCode(), toolStihlChainsaw);
		toolsWithTheirCode.put(toolWernerLadder.toolCode(), toolWernerLadder);
		toolsWithTheirCode.put(toolDewaltJackhammer.toolCode(), toolDewaltJackhammer);
		toolsWithTheirCode.put(toolRidgidJackhammer.toolCode(), toolRidgidJackhammer);

		this.tools = toolsWithTheirCode;
	}

	/**
	 * Returns tool for the given tool code
	 * 
	 * @param toolCode
	 * @return
	 */
	public Tool getToolForCode(String toolCode) {
		return tools.get(toolCode);
	}
}
