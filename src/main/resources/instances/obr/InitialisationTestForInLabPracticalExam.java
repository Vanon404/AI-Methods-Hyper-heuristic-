package com.aim.project.obr.runners;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.obr.OBRDomain;

import java.util.Arrays;

/**
 * @author Warren G Jackson
 */
public class InitialisationTestForInLabPracticalExam extends HyperHeuristic {

    public InitialisationTestForInLabPracticalExam(long lSeed) {

        super(lSeed);
    }

	protected void solve(ProblemDomain problem) {

		problem.initialiseSolution(0);
		hasTimeExpired();
	}

	@Override
	public String toString() {
		return "In-lab practical exam initialisation test HH";
	}

	public static void main(String[] args) {

		// TODO update to point to the instance in the question
		final int INSTANCE_ID = -1;

        InitialisationTestForInLabPracticalExam hh = new InitialisationTestForInLabPracticalExam(20260508L);
		hh.setTimeLimit(Long.MAX_VALUE);
		OBRDomain oDomain = new OBRDomain(20260508L);
		oDomain.loadInstance(INSTANCE_ID);
		oDomain.initialiseSolution(0);
		hh.loadProblemDomain(oDomain);
		hh.run();

        System.out.println("Outputs from `InitialisationTestForInLabPracticalExam` are printed below based ono your own implementations of the interface methods:");

        System.out.println("\nObjective value of the best solution found (initial solution): ");
        oDomain.printObjectiveValueOfTheBestSolutionFound();

        System.out.println("\nRepresentation of the best solution found (initial solution): ");
        System.out.println(Arrays.toString(oDomain.getBestSolutionRepresentation()));

		System.out.println("\nCoordinates of the initial solution: ");
        oDomain.printInitialSolution();
	}

}
