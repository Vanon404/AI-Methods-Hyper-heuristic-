package com.aim.project.obr.instance;


import java.util.ArrayList;
import java.util.Random;

import com.aim.project.obr.OBRObjectiveFunction;
import com.aim.project.obr.interfaces.ObjectiveFunctionInterface;
import com.aim.project.obr.interfaces.OBRInstanceInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;
import com.aim.project.obr.solution.OBRSolution;

import com.aim.project.obr.solution.SolutionRepresentation;
import com.aim.project.obr.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class OpenTopBusRoutingInstance implements OBRInstanceInterface {

    private final int m_iNumberOfLocations;
    private final Location[] m_aoLocations;
    private final Location m_oBusDepotLocation;
    private final Random m_oRandom;
    private final ObjectiveFunctionInterface m_oObjectiveFunction;

    public OpenTopBusRoutingInstance(int iNumberOfLocations, Location[] aoLocations, Location oBusDepotLocation, Random oRandom) {

        this.m_iNumberOfLocations = iNumberOfLocations;
        this.m_aoLocations = aoLocations;
        this.m_oBusDepotLocation = oBusDepotLocation;
        this.m_oRandom = oRandom;
        this.m_oObjectiveFunction = new OBRObjectiveFunction(this);
    }

	@Override
public OBRSolution createSolution(InitialisationMode oMode) {
    int[] aiRoute = new int[m_iNumberOfLocations];
    for (int i = 0; i < m_iNumberOfLocations; i++) {
        aiRoute[i] = i;
    }

    if (oMode == InitialisationMode.RANDOM) {
        // Shuffle the array
        for (int i = 0; i < aiRoute.length; i++) {
            int randomIndexToSwap = m_oRandom.nextInt(aiRoute.length);
            int temp = aiRoute[randomIndexToSwap];
            aiRoute[randomIndexToSwap] = aiRoute[i];
            aiRoute[i] = temp;
        }
    }

    SolutionRepresentation oRep = new SolutionRepresentation(aiRoute);
    int iCost = m_oObjectiveFunction.getObjectiveFunctionValue(oRep);
    return new OBRSolution(oRep, iCost);
}

    @Override
	public ObjectiveFunctionInterface getOBRObjectiveFunction() {

        // TODO
		return m_oObjectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {

        // TODO
        return m_iNumberOfLocations;
	}

	@Override
	public Location getLocationForPoI(int iPointOfInterestId) {

        // TODO
        // Return the specific location from your array
        return m_aoLocations[iPointOfInterestId];
	}

	@Override
	public Location getLocationOfBusDepot() {

        // TODO
        return m_oBusDepotLocation;
	}

	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(OBRSolutionInterface oSolution) {
    ArrayList<Location> loLocations = new ArrayList<>();

    // If the solution is null, return just the depot so it doesn't crash
    if (oSolution == null || oSolution.getSolutionRepresentation() == null) {
        loLocations.add(this.m_oBusDepotLocation);
        return loLocations;
    }

    loLocations.add(this.m_oBusDepotLocation);

    int[] aiRepresentation = oSolution.getSolutionRepresentation().getSolutionRepresentation();

    for (int iPOIId : aiRepresentation) {
        loLocations.add(getLocationForPoI(iPOIId));
    }

    loLocations.add(this.m_oBusDepotLocation);

    return loLocations;
}
}
