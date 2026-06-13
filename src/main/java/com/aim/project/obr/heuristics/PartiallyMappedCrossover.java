package com.aim.project.obr.heuristics;

import java.util.Random;

import com.aim.project.obr.interfaces.ObjectiveFunctionInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;
import com.aim.project.obr.interfaces.CrossoverHeuristicInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class PartiallyMappedCrossover implements CrossoverHeuristicInterface {

	private final Random m_oRandom;

    private ObjectiveFunctionInterface m_oObjectiveFunction;

	public PartiallyMappedCrossover(Random oRandom) {
		
		this.m_oRandom = oRandom;
	}

	@Override
	public int apply(OBRSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

        // TODO
        // Crossover heuristics usually don't use the single-parent apply.
        return oSolution.getObjectiveFunctionValue();
    }

	@Override
	public int apply(OBRSolutionInterface oParent1, OBRSolutionInterface oParent2, OBRSolutionInterface oChild, double dDepthOfSearch, double dIntensityOfMutation) {

        // TODO
        int[] p1 = oParent1.getSolutionRepresentation().getSolutionRepresentation();
        int[] p2 = oParent2.getSolutionRepresentation().getSolutionRepresentation();
        int n = p1.length;
        int[] childRoute = new int[n];
        java.util.Arrays.fill(childRoute, -1);

        // two random crossover points
        int cut1 = m_oRandom.nextInt(n);
        int cut2 = m_oRandom.nextInt(n);
        int start = Math.min(cut1, cut2);
        int end = Math.max(cut1, cut2);

        // copy segment from Parent 1 to Child
        for (int i = start; i <= end; i++) {
            childRoute[i] = p1[i];
        }

        //fill remaining slots from Parent 2
        for (int i = 0; i < n; i++) {
            if (i >= start && i <= end) continue;

            int candidate = p2[i];
            // If candidate is already in the child  , frm p1 s segment
            // we must find its "mapped" replacement to avoid duplicates
            while (contains(childRoute, candidate, start, end)) {
                candidate = findReplacement(candidate, p1, p2, start, end);
            }
            childRoute[i] = candidate;
        }

        // update the child solution object
        oChild.getSolutionRepresentation().setSolutionRepresentation(childRoute);

        // calculate and return new cost
        int iNewCost = m_oObjectiveFunction.getObjectiveFunctionValue(oChild.getSolutionRepresentation());
        oChild.setObjectiveFunctionValue(iNewCost);

        return iNewCost;
    }

	//check if value exists in the segment
    private boolean contains(int[] route, int value, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (route[i] == value) return true;
        }
        return false;
    }
	// find the mapping for PMX
    private int findReplacement(int value, int[] p1, int[] p2, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (p1[i] == value) return p2[i];
        }
        return value;
    }
	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {

        this.m_oObjectiveFunction = oObjectiveFunction;
	}

	@Override
	public boolean isCrossover() {

		// TODO
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return false;
	}
}
