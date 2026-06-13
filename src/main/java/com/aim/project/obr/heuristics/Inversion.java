package com.aim.project.obr.heuristics;
import com.aim.project.obr.interfaces.HeuristicInterface;
import com.aim.project.obr.interfaces.ObjectiveFunctionInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;

import java.util.Random;


public class Inversion implements HeuristicInterface {

    private final Random rng;
    private ObjectiveFunctionInterface objectiveFunction;

    public Inversion(Random rng) {
        this.rng = rng;
    }

    @Override
    public int apply(OBRSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        int[] route = solution.getSolutionRepresentation().getSolutionRepresentation();
        int n = route.length;

        // Perform more inversions if intensity is high
        int moves = (int) (intensityOfMutation * 10) + 1;
        for(int i = 0; i < moves; i++) {
        //  two random points
        int cut1 = rng.nextInt(n);
        int cut2 = rng.nextInt(n);

        // make sure cut1 is the smaller index
        int start = Math.min(cut1, cut2);
        int end = Math.max(cut1, cut2);

        // Reverse the segment
        while (start < end) {
            int temp = route[start];
            route[start] = route[end];
            route[end] = temp;
            start++;
            end--;
        }
    }
        //  Update and calculate new cost
        solution.getSolutionRepresentation().setSolutionRepresentation(route);
        int newCost = (int) objectiveFunction.getObjectiveFunctionValue(solution.getSolutionRepresentation());

        return newCost;
    }

    @Override
    public void setObjectiveFunction(ObjectiveFunctionInterface objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    @Override
    public boolean isCrossover() {

        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {

        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {

        return false;
    }
}
