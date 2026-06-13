package com.aim.project.obr.heuristics;

import java.util.Random;

import com.aim.project.obr.interfaces.HeuristicInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {


	public Reinsertion(Random oRandom) {

		super(oRandom);
	}

	@Override
	public int apply(OBRSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

    int[] route = oSolution.getSolutionRepresentation().getSolutionRepresentation();
    int n = route.length;

    int numberOfMoves;
    if (dIntensityOfMutation < 0.2) numberOfMoves = 1;
    else if (dIntensityOfMutation < 0.4) numberOfMoves = 2;
    else if (dIntensityOfMutation < 0.6) numberOfMoves = 3;
    else if (dIntensityOfMutation < 0.8) numberOfMoves = 4;
    else numberOfMoves = 5;

    for (int m = 0; m < numberOfMoves; m++) {
        // pck a random node to move and a random destination
        int fromIndex = m_oRandom.nextInt(n);
        int toIndex = m_oRandom.nextInt(n);

        if (fromIndex == toIndex) continue;

        int cityToMove = route[fromIndex];

        // Shift elements to fill the gap and make room for reinsertion
        if (fromIndex < toIndex) {
            // Moving forward: shift everything between back by one
            System.arraycopy(route, fromIndex + 1, route, fromIndex, toIndex - fromIndex);
        } else {
            // Moving backward: shift everything between forward by one
            System.arraycopy(route, toIndex, route, toIndex + 1, fromIndex - toIndex);
        }
        route[toIndex] = cityToMove;
    }

    // save and return cost
    oSolution.getSolutionRepresentation().setSolutionRepresentation(route);
    int iNewCost = m_oObjectiveFunction.getObjectiveFunctionValue(oSolution.getSolutionRepresentation());
	oSolution.setObjectiveFunctionValue(iNewCost);
	return iNewCost;
	}

    @Override
	public boolean isCrossover() {

		// TODO
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return false;
	}

}
