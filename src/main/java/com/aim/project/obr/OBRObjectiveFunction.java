package com.aim.project.obr;

import com.aim.project.obr.interfaces.ObjectiveFunctionInterface;
import com.aim.project.obr.interfaces.OBRInstanceInterface;
import com.aim.project.obr.interfaces.SolutionRepresentationInterface;
import com.aim.project.obr.instance.Location;
/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class OBRObjectiveFunction implements ObjectiveFunctionInterface {

    private final OBRInstanceInterface m_oInstance;

	public OBRObjectiveFunction(OBRInstanceInterface oInstance) {

        this.m_oInstance = oInstance;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {

    int[] route = oSolution.getSolutionRepresentation();

    // Safety check: if route is empty, cost is 0
    if (route.length == 0) return 0;

    int totalDistance = 0;

    // Distance from Depot to the first poi in the route
    totalDistance += getDistanceBetweenBusDepotAndPoI(route[0]);

    // Distance between each consecutive poi
    for (int i = 0; i < route.length - 1; i++) {
        totalDistance += getCost(route[i], route[i+1]);
    }

    // Distance from the last poi in the route back to the Depot
    totalDistance += getDistanceBetweenBusDepotAndPoI(route[route.length - 1]);

    return   totalDistance;
}

	@Override
	public int getCost(int iLocationA, int iLocationB) {

    //  Get Location from the instance using the IDs
    Location locA = m_oInstance.getLocationForPoI(iLocationA);
    Location locB = m_oInstance.getLocationForPoI(iLocationB);

    // use .x() and .y() to get coordinates from the Record
    double xDiff = locA.x() - locB.x();
    double yDiff = locA.y() - locB.y();

    // Euclidean distance: sqrt( a^2 + b^2 )
    double distance = Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));

    // Apply ceiling and cast to int
    return (int) Math.ceil(distance);


	}

	@Override
	public int getDistanceBetweenBusDepotAndPoI(int iLocation) {

        Location depot = m_oInstance.getLocationOfBusDepot();
    Location poi = m_oInstance.getLocationForPoI(iLocation);

    double xDiff = depot.x() - poi.x();
    double yDiff = depot.y() - poi.y();

    double distance = Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));

    return (int) Math.ceil(distance);
	}
}
