package com.aim.project.obr.heuristics;

import com.aim.project.obr.interfaces.ObjectiveFunctionInterface;

import java.util.Random;

/**
 * <p>
 * This class is included (and all non-crossover heuristics subclass this class) to simplify your implementation, and it
 * is intended that you include any common operations in this class to simplify your implementation of the other heuristics.
 * Furthermore, if you implement and test common functionality here, it is less likely that you introduce a bug elsewhere!
 * <p>
 * For example, think about common neighbourhood operators and any other incremental changes that you might perform
 * while applying low-level heuristics.
 *
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class HeuristicOperators {

	protected final Random m_oRandom;

    protected ObjectiveFunctionInterface m_oObjectiveFunction;

	public HeuristicOperators(Random oRandom) {

		this.m_oRandom = oRandom;
	}

    public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {

        this.m_oObjectiveFunction = oObjectiveFunction;
    }

    protected void swap(int[] route, int i, int j) {
    int temp = route[i];
    route[i] = route[j];
    route[j] = temp;
}

/**
 * Calculates the change in cost when swapping two indices in the route.
 */
protected int getDelta(int i, int j, int[] route) {
    int n = route.length;

    // Get the neighbors of i and j
    int iPrev = (i == 0) ? -1 : route[i - 1]; // -1 represents the Depot
    int iNext = (i == n - 1) ? -1 : route[i + 1];

    int jPrev = (j == 0) ? -1 : route[j - 1];
    int jNext = (j == n - 1) ? -1 : route[j + 1];

    int oldCost = 0;
    int newCost = 0;


    return newCost - oldCost;
}
    // TODO include any common operations here, for example setObjectiveFunction(...) as implemented above.
}
