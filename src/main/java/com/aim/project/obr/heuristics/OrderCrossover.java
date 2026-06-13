package com.aim.project.obr.heuristics;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.aim.project.obr.interfaces.CrossoverHeuristicInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;
import com.aim.project.obr.interfaces.ObjectiveFunctionInterface;
import com.aim.project.obr.solution.OBRSolution;
import java.util.Random;
import com.aim.project.obr.interfaces.CrossoverHeuristicInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;
import com.aim.project.obr.interfaces.ObjectiveFunctionInterface;
/**
 * This crossover type heuristic should take two parent solutions and create a single child
 * (chosen randomly) according to the explanation of OX from the lectures (see lecture 6).
 * When selecting the two cut points, you should ensure that it is not possible that all
 * sightseeing locations are within the segment to be copied. There is no requirement to
 * use either of the intensity of mutation or depth of search settings for this
 * implementation.
 *
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class OrderCrossover implements CrossoverHeuristicInterface {
	
	private final Random m_oRandom;
	
	private ObjectiveFunctionInterface m_oObjectiveFunction;

	public OrderCrossover(Random oRandom) {
		
		this.m_oRandom = oRandom;
	}

@Override
    public int apply(OBRSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
        // Not used for crossover
        return oSolution.getObjectiveFunctionValue();
    }

	@Override
    public int apply(OBRSolutionInterface oParent1, OBRSolutionInterface oParent2,
                        OBRSolutionInterface oChild, double dDepthOfSearch, double dIntensityOfMutation) {

        int[] p1 = oParent1.getSolutionRepresentation().getSolutionRepresentation();
        int[] p2 = oParent2.getSolutionRepresentation().getSolutionRepresentation();
        int n = p1.length;
        int[] childRoute = new int[n];
        java.util.Arrays.fill(childRoute, -1);

        // pick two random cut points
        int cut1 = m_oRandom.nextInt(n);
        int cut2 = m_oRandom.nextInt(n);
        int start = Math.min(cut1, cut2);
        int end = Math.max(cut1, cut2);

        // copy the segment from Parent 1
        for (int i = start; i <= end; i++) {
            childRoute[i] = p1[i];
        }

        // fill the rest from Parent 2 in the order they appear
        int currentChildPos = (end + 1) % n;
        int currentParent2Pos = (end + 1) % n;

        // Loop through Parent 2 starting from the same relative position
        int filledCount = (end - start) + 1;
        while (filledCount < n) {
            int candidate = p2[currentParent2Pos];

            if (!arrayContains(childRoute, candidate)) {
                childRoute[currentChildPos] = candidate;
                currentChildPos = (currentChildPos + 1) % n;
                filledCount++;
            }
            currentParent2Pos = (currentParent2Pos + 1) % n;
        }

        oChild.getSolutionRepresentation().setSolutionRepresentation(childRoute);
        int iNewCost = m_oObjectiveFunction.getObjectiveFunctionValue(oChild.getSolutionRepresentation());
        oChild.setObjectiveFunctionValue(iNewCost);

        return iNewCost;
    }

    private boolean arrayContains(int[] arr, int val) {
        for (int x : arr) {
            if (x == val) return true;
        }
        return false;
    }
	/*
	 * TODO update the methods below to return the correct boolean value.
	 */

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

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
		
		this.m_oObjectiveFunction = oObjectiveFunction;
	}
}
