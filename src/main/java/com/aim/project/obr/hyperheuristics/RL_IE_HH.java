package com.aim.project.obr.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import java.util.Arrays;

public class RL_IE_HH extends HyperHeuristic {

    public RL_IE_HH(long lSeed) {
        super(lSeed);
    }

    @Override
    protected void solve(ProblemDomain oProblem) {
        int iNumHeuristics = oProblem.getNumberOfHeuristics();

        //  Initialize Memory
        int[] aiScores = new int[iNumHeuristics];
        int[] aiTabuList = new int[iNumHeuristics]; //  Tabu memory
        Arrays.fill(aiScores, 10);
        Arrays.fill(aiTabuList, -1); // No one is banned at start

        oProblem.initialiseSolution(0);
        double dCurrentCost = oProblem.getFunctionValue(0);
        double dBestCost = dCurrentCost;

        int iIteration = 0; // To track tabu duration
        long startTime = System.currentTimeMillis();
        long lastPrintTime = startTime;

        System.out.println("Starting Search");

        // cast oProblem to the specific OBRDomain to access its instance-specific methods
    int numLocations = ((com.aim.project.obr.OBRDomain) oProblem).getLoadedInstance().getNumberOfLocations();

    // set tenure based on that size
    int tenureValue = (numLocations > 50) ? (numLocations / 2) : 15;

        while (!hasTimeExpired()) {
            iIteration++;
            long currentTime = System.currentTimeMillis();

            /*if (currentTime - lastPrintTime > 10000) {
                System.out.println("Running... [" + (currentTime - startTime) / 1000 + "s elapsed]");
                lastPrintTime = currentTime;
            }*/

            // Select heuristic
            int iHeuristicId = selectHeuristic(aiScores, aiTabuList, iIteration);

            // Apply
            double dCandidateCost = oProblem.applyHeuristic(iHeuristicId, 0, 1);

            // Learning + Tabu
            if (dCandidateCost < dCurrentCost) {
                // IMPROVEMENT
                aiScores[iHeuristicId] += 5;
                oProblem.copySolution(1, 0);
                dCurrentCost = dCandidateCost;

                // Clear tabu if successful
                aiTabuList[iHeuristicId] = -1;

            } else if (dCandidateCost == dCurrentCost) {
                // EQUALITY
                aiScores[iHeuristicId] += 1;
            } else {
                // WORSE = Punishment + Tabu Ban
                aiScores[iHeuristicId] = Math.max(1, aiScores[iHeuristicId] - 2);
                aiTabuList[iHeuristicId] = iIteration + tenureValue;
            }
        }
        System.out.println("Search Complete. Finalizing results...");
    }

    private int selectHeuristic(int[] scores, int[] tabuList, int iteration) {
        //  filter Tabu heuristics
        int totalScore = 0;
        boolean[] isAvailable = new boolean[scores.length];
        int availableCount = 0;

        for (int i = 0; i < scores.length; i++) {
            if (tabuList[i] < iteration) {
                totalScore += scores[i];
                isAvailable[i] = true;
                availableCount++;
            }
        }

        // If everyone is Tabu, pick a random one
        if (availableCount == 0) {
            return rng.nextInt(scores.length);
        }

        //Roulette Wheel on available heuristics
        int rand = rng.nextInt(totalScore);
        int runningSum = 0;
        for (int i = 0; i < scores.length; i++) {
            if (isAvailable[i]) {
                runningSum += scores[i];
                if (rand < runningSum) return i;
            }
        }
        return rng.nextInt(scores.length);
    }

    @Override
    public String toString() {
        return "RL with Tabu Search HH";
    }
}