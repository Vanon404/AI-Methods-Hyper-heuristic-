package com.aim.project.obr.heuristics;

import java.util.Random;

import com.aim.project.obr.interfaces.HeuristicInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

	public DavissHillClimbing(Random oRandom) {
	
		super(oRandom);
	}

	@Override
public int apply(OBRSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

    int[] route = oSolution.getSolutionRepresentation().getSolutionRepresentation();
    int n = route.length;

    // DoS to check how many full passes to attempt
    int maxImprovements = (int) (dDepthOfSearch * 10) + 1;
    int totalImprovements = 0;

    int currentCost = oSolution.getObjectiveFunctionValue();

    // created a list of indices and shuffle them
    Integer[] indices = new Integer[n];
    for (int i = 0; i < n; i++) indices[i] = i;

    // while we haven't hit our improvement, cap
    while (totalImprovements < maxImprovements) {

        boolean foundImprovementInThisPass = false;
        java.util.Collections.shuffle(java.util.Arrays.asList(indices), m_oRandom);

        //  Iterate through shuffled indices
        for (int i : indices) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                // swap elements at i and j
                int temp = route[i];
                route[i] = route[j];
                route[j] = temp;

                int newCost = m_oObjectiveFunction.getObjectiveFunctionValue(oSolution.getSolutionRepresentation());

                if (newCost < currentCost) {
                    currentCost = newCost;
                    totalImprovements++;
                    foundImprovementInThisPass = true;
                    // we move to the next 'i', once we improve 'i'
                    break;
                } else {
                    // Undo if no improvement
                    route[j] = route[i];
                    route[i] = temp;
                }
            }
        }

        // If after full pass of all indices and found 0 improvements,
        // we are at a local optimum and should stop.
        if (!foundImprovementInThisPass) {
            break;
        }
    }

    // Save final state back to the solution object
    oSolution.getSolutionRepresentation().setSolutionRepresentation(route);
    oSolution.setObjectiveFunctionValue(currentCost);

    return currentCost;
}

	@Override
	public boolean isCrossover() {

		// TODO
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return true;
	}
}
