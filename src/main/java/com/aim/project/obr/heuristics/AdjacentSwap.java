package com.aim.project.obr.heuristics;

import java.util.Random;

import com.aim.project.obr.interfaces.HeuristicInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	public AdjacentSwap(Random oRandom) {

		super(oRandom);
	}

    /**
     * TODO - delta evaluation
     *
     * @param oSolution The solution to which the heuristic is applied. This solution may be modified during the method execution.
     * @param dDepthOfSearch The parameter controlling the extent or depth of exploration performed during the heuristic's application.
     * @param dIntensityOfMutation The parameter controlling the extent or intensity of changes introduced by the heuristic.
     * @return
     */
	@Override
public int apply(OBRSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

    int[] route = oSolution.getSolutionRepresentation().getSolutionRepresentation();

    int numberOfSwaps;
    if (dIntensityOfMutation < 0.2) numberOfSwaps = 1;
    else if (dIntensityOfMutation < 0.4) numberOfSwaps = 2;
    else if (dIntensityOfMutation < 0.6) numberOfSwaps = 4;
    else if (dIntensityOfMutation < 0.8) numberOfSwaps = 8;
    else if (dIntensityOfMutation < 1.0) numberOfSwaps = 16;
    else numberOfSwaps = 32;


    for (int i = 0; i < numberOfSwaps; i++) {
        int index = m_oRandom.nextInt(route.length);
        int nextIndex = (index + 1) % route.length;

        int temp = route[index];
        route[index] = route[nextIndex];
        route[nextIndex] = temp;
    }

    // Save modified route back to solution object
    oSolution.getSolutionRepresentation().setSolutionRepresentation(route);

    // Recalculate the cost using the objective function via HeuristicOperators
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
