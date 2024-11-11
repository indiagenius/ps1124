package com.cardinal.assessment.service;

import java.util.EnumMap;

import com.cardinal.assessment.model.ToolCharge;
import com.cardinal.assessment.model.constants.ToolType;

/**
 * Service to manage and retrieve the tool rental charges
 */
public class ToolChargeService {

	/**
	 * Assuming that the tool charges are cached in the service. In real life, this
	 * could be fetched from database and cached with a TTL to reduce network round
	 * trip
	 */
	private EnumMap<ToolType, ToolCharge> toolCharges;

	/**
	 * This init method populates the tool charges in the service cache. In
	 * practice, there will a database call to fetch these details and kept in cache
	 * with a TTL
	 */
	{
		populateToolCharges();
	}

	private void populateToolCharges() {
		ToolCharge toolChargeLadder = new ToolCharge(ToolType.Ladder, 1.99, true, true, false);
		ToolCharge toolChargeChainsaw = new ToolCharge(ToolType.Chainsaw, 1.49, true, false, true);
		ToolCharge toolChargeJackhammer = new ToolCharge(ToolType.Jackhammer, 2.99, true, false, false);

		EnumMap<ToolType, ToolCharge> toolTypeCharges = new EnumMap<ToolType, ToolCharge>(ToolType.class);
		toolTypeCharges.put(ToolType.Ladder, toolChargeLadder);
		toolTypeCharges.put(ToolType.Chainsaw, toolChargeChainsaw);
		toolTypeCharges.put(ToolType.Jackhammer, toolChargeJackhammer);

		this.toolCharges = toolTypeCharges;
	}

	/**
	 * Returns tool charges for the given tool type
	 * 
	 * @param toolType
	 * @return
	 */
	public ToolCharge getToolChargeForToolType(ToolType toolType) {
		return toolCharges.get(toolType);
	}
}