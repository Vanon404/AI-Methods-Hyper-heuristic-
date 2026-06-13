package com.aim.project.obr.heuristics;


import java.util.Random;

import com.aim.project.obr.interfaces.HeuristicInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {

	public NextDescent(Random oRandom) {
	
		super(oRandom);
	}

	@Override
    public int apply(OBRSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
        int[] route = oSolution.getSolutionRepresentation().getSolutionRepresentation();
        int n = route.length;
        int currentCost = oSolution.getObjectiveFunctionValue();

        // Map DoS to total passes
        int maxPasses;
        if (dDepthOfSearch < 0.2) maxPasses = 1;
        else if (dDepthOfSearch < 0.4) maxPasses = 2;
        else if (dDepthOfSearch < 0.6) maxPasses = 3;
        else if (dDepthOfSearch < 0.8) maxPasses = 4;
        else maxPasses = 5;

        for (int count = 0; count < maxPasses; count++) {
            boolean improvementInThisRound = false;

            // Randomized starting points for the scan
            int startI = m_oRandom.nextInt(n);

            for (int i = 0; i < n; i++) {
                int u = (startI + i) % n;

                for (int j = i + 1; j < n; j++) {
                    int v = (startI + j) % n;

                    int start = Math.min(u, v);
                    int end = Math.max(u, v);

                    // Apply Inversion
                    reverse(route, start, end);

                    int newCost = m_oObjectiveFunction.getObjectiveFunctionValue(oSolution.getSolutionRepresentation());

                    if (newCost < currentCost) {
                        currentCost = newCost;
                        improvementInThisRound = true;
                        // Found better route, accept and restart scan from here
                        break;
                    } else {
                        // if not better, Undo the inversion immediately
                        reverse(route, start, end);
                    }
                }
                if (improvementInThisRound) break;
            }

            //  we're at a local optimum if we've checked all pairs and found nothing
            if (!improvementInThisRound) break;
        }

        oSolution.getSolutionRepresentation().setSolutionRepresentation(route);
        oSolution.setObjectiveFunctionValue(currentCost);
        return currentCost;
    }

    // Helper to keep code clean
    private void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    @Override
    public boolean isCrossover() { return false; }

    @Override
    public boolean usesIntensityOfMutation() { return false; }

    @Override
    public boolean usesDepthOfSearch() { return true; }
}