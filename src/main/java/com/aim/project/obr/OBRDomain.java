package com.aim.project.obr;

import com.aim.project.obr.instance.Location;
import com.aim.project.obr.interfaces.*;
import com.aim.project.obr.interfaces.CrossoverHeuristicInterface;

import AbstractClasses.ProblemDomain;
import com.aim.project.obr.heuristics.*;
import com.aim.project.obr.interfaces.HeuristicInterface;
import com.aim.project.obr.interfaces.OBRInstanceInterface;
import com.aim.project.obr.interfaces.OBRSolutionInterface;
import AbstractClasses.ProblemDomain.HeuristicType;
import com.aim.project.obr.solution.OBRSolution;
import com.aim.project.obr.solution.SolutionRepresentation;
import com.aim.project.obr.instance.OpenTopBusRoutingInstance;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class OBRDomain extends ProblemDomain implements Visualisable, InLabPracticalExamInterface {

	private Object[] m_aoHeuristics;
	private CrossoverHeuristicInterface m_oCrossoverHeuristic;

	private OBRSolutionInterface[] m_aoSolutionMemory;
	private OBRSolutionInterface m_oBestSolution;
	private OBRInstanceInterface m_oInstance;
    private int[] m_aiHeuristicCallCounts;
    private long m_lTimeOfBestSolution;
    private long m_lStartTime;

    public OBRDomain(long lSeed) {

		super(lSeed);

        // TODO
        //  recommend to create the heuristics here



        m_aoHeuristics = new Object[7];
    // Mutation
    m_aoHeuristics[0] = new AdjacentSwap(rng);
    m_aoHeuristics[1] = new Reinsertion(rng);
    m_aoHeuristics[6] = new Inversion(rng);

    // Local Search
    m_aoHeuristics[2] = new NextDescent(rng);
    m_aoHeuristics[3] = new DavissHillClimbing(rng);

    // Crossover
    m_aoHeuristics[4] = new PartiallyMappedCrossover(rng);
    m_aoHeuristics[5] = new OrderCrossover(rng);
        // Initialize counters
        m_aiHeuristicCallCounts = new int[m_aoHeuristics.length];
        m_lStartTime = System.currentTimeMillis();
}



@Override
	public double applyHeuristic(int iHeuristicIndex, int iCurrentSolutionIndex, int iCandidateSolutionIndex) {


        m_aiHeuristicCallCounts[iHeuristicIndex]++;

    OBRSolutionInterface oCurrentSolution = m_aoSolutionMemory[iCurrentSolutionIndex];
    OBRSolutionInterface oCandidateSolution = oCurrentSolution.clone();

    // The heuristic modifies the route and returns the cost
    int iNewCost = ((HeuristicInterface) m_aoHeuristics[iHeuristicIndex]).apply(
        oCandidateSolution,
        depthOfSearch,
        intensityOfMutation
    );
    oCandidateSolution.setObjectiveFunctionValue(iNewCost);
    m_aoSolutionMemory[iCandidateSolutionIndex] = oCandidateSolution; // Fixed variable name to  iCandidateSolutionIndex
    updateBestSolution(iCandidateSolutionIndex);

    return (double) iNewCost;
}




@Override
	public double applyHeuristic(int iHeuristicIndex, int iParent1Index, int iParent2Index, int iCandidateIndex) {


        m_aiHeuristicCallCounts[iHeuristicIndex]++;
        // TODO
     // get the two parent solutions
    OBRSolutionInterface oParent1 = m_aoSolutionMemory[iParent1Index];
    OBRSolutionInterface oParent2 = m_aoSolutionMemory[iParent2Index];

    //  Prepare the candidate , ie the child
    if (m_aoSolutionMemory[iCandidateIndex] == null) {
        m_aoSolutionMemory[iCandidateIndex] = oParent1.clone();
    }
    OBRSolutionInterface oChild = m_aoSolutionMemory[iCandidateIndex];

    // index 4 is our Crossover
    int iNewCost = ((CrossoverHeuristicInterface) m_aoHeuristics[iHeuristicIndex]).apply(
        oParent1,
        oParent2,
        oChild,
        depthOfSearch,
        intensityOfMutation
    );

    oChild.setObjectiveFunctionValue(iNewCost);
    updateBestSolution(iCandidateIndex);

    return (double) iNewCost;
}
	@Override
	public String bestSolutionToString() {

        // TODO
        if (m_oBestSolution == null) return "";

    int[] route = m_oBestSolution.getSolutionRepresentation().getSolutionRepresentation();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < route.length; i++) {
        sb.append(route[i]);
        if (i < route.length - 1) {
            sb.append(",");
        }
    }
    return sb.toString();
	}


	@Override
	public boolean compareSolutions(int iSolutionIndexA, int iSolutionIndexB) {

        // TODO
        int valA = m_aoSolutionMemory[iSolutionIndexA].getObjectiveFunctionValue();
    int valB = m_aoSolutionMemory[iSolutionIndexB].getObjectiveFunctionValue();
    return valA == valB;
}

	@Override
	public void copySolution(int iSolutionIndexFrom, int iSolutionIndexTo) {

        // TODO
        //  this should create a deep copy of the solution
		if (m_aoSolutionMemory[iSolutionIndexFrom] != null) {
        m_aoSolutionMemory[iSolutionIndexTo] = m_aoSolutionMemory[iSolutionIndexFrom].clone();
    }
	}

	@Override
	public double getBestSolutionValue() {

		// TODO
        return (m_oBestSolution == null) ? -1.0 : (double) m_oBestSolution.getObjectiveFunctionValue();
}
	
	@Override
	public double getFunctionValue(int iSolutionIndex) {

        // TODO
        return m_aoSolutionMemory[iSolutionIndex].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType oHeuristicType) {

        return switch (oHeuristicType) {
        case MUTATION -> new int[]{0, 1, 6};     // IDs for Mutation
        case LOCAL_SEARCH -> new int[]{2, 3}; // IDs for Local Search
        case CROSSOVER -> new int[]{4, 5};       // ID for Crossover
        default -> new int[0];
    };
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		
		return new int[]{2, 3}; // NextDescent and DavissHillClimbing
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {

        return new int[]{0, 1, 6}; // AdjacentSwap and Reinsertion and Inversion
	}

	@Override
	public int getNumberOfHeuristics() {

        // TODO


    return 7;
	}

	@Override
	public int getNumberOfInstances() {

        // TODO
		return asFileNames.length;
	}

	@Override
	public void initialiseSolution(int iSolutionIndex) {

// Get the number of POIs
    int numberOfPoIs = m_oInstance.getNumberOfLocations();

    //  Create the route [0,1..., n-1]
    int[] route = new int[numberOfPoIs];
    for (int i = 0; i < numberOfPoIs; i++) {
        route[i] = i;
    }

    // Random shuffle using the HyFlex 'rng'
    for (int i = route.length - 1; i > 0; i--) {
        int index = rng.nextInt(i + 1);
        int a = route[index];
        route[index] = route[i];
        route[i] = a;
    }

    // Create the solution object
    SolutionRepresentation oRepresentation = new SolutionRepresentation(route);
    int initialCost = (int) m_oInstance.getOBRObjectiveFunction().getObjectiveFunctionValue(oRepresentation);

    m_aoSolutionMemory[iSolutionIndex] = new OBRSolution(oRepresentation, initialCost);

    // Update the best sol tracker
    updateBestSolution(iSolutionIndex);
    }



    private final String[] asFileNames = {
    "square.obr",           // 0
    "libraries-15.obr",     // 1
    "carparks-40.obr",      // 2
    "tramstops-85.obr",     // 3
    "grid.obr",             // 4
    "clustered-pois.obr",   // 5
    "chatgpt-instance-100-PoIs.obr", // 6
    "unseen-instance.obr",           // 7 (EXAM instance)
    "initialisation-instance.obr",           // 8 (Safety backup)
    "hidden.obr"            // 9 (Safety backup)
    };

	@Override
public void loadInstance(int iInstanceId) {
    String fileName = asFileNames[iInstanceId];
    try {
        java.nio.file.Path path = findInstanceFile(fileName);

        if (path == null) {
            System.err.println("CRITICAL ERROR: Could not find " + fileName);
            System.err.println("Working Directory was: " + System.getProperty("user.dir"));
            return;
        }

        com.aim.project.obr.instance.reader.OBRInstanceReader oReader =
            new com.aim.project.obr.instance.reader.OBRInstanceReader();
        this.m_oInstance = oReader.readOBRInstanceFile(path, rng);

        // Link heuristics to objective function
        if (m_aoHeuristics != null) {
            for (Object h : m_aoHeuristics) {
                if (h instanceof HeuristicInterface) {
                    ((HeuristicInterface) h).setObjectiveFunction(m_oInstance.getOBRObjectiveFunction());
                } else if (h instanceof CrossoverHeuristicInterface) {
                    ((CrossoverHeuristicInterface) h).setObjectiveFunction(m_oInstance.getOBRObjectiveFunction());
                }
            }
        }
        System.out.println("Successfully loaded: " + fileName);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

private java.nio.file.Path findInstanceFile(String fileName) {

    String[] candidatePaths = {
        "COMP2001CW/src/main/resources/instances/obr/" + fileName,
        "src/main/resources/instances/obr/" + fileName,
        "resources/instances/obr/" + fileName,
        "instances/obr/" + fileName
    };

    for (String p : candidatePaths) {
        java.io.File f = new java.io.File(p);
        if (f.exists()) {
            return f.toPath();
        }
    }
    return null;
}

	@Override
	public void setMemorySize(int iNewMemorySize) {
	this.m_aoSolutionMemory = new OBRSolutionInterface[iNewMemorySize];
}

	@Override
	public String solutionToString(int iSolutionIndex) {

        // TODO
        if (m_aoSolutionMemory[iSolutionIndex] == null) return "";

    int[] route = m_aoSolutionMemory[iSolutionIndex].getSolutionRepresentation().getSolutionRepresentation();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < route.length; i++) {
        sb.append(route[i]);
        if (i < route.length - 1) {
            sb.append(",");
        }
    }
    return sb.toString();
	}

	@Override
	public String toString() {

		return "Open-top Bus Routing";
	}
	
	private void updateBestSolution(int iSolutionIndex) {

        // TODO

		OBRSolutionInterface candidate = m_aoSolutionMemory[iSolutionIndex];
    	if (m_oBestSolution == null || candidate.getObjectiveFunctionValue() < m_oBestSolution.getObjectiveFunctionValue()) {
        	m_oBestSolution = candidate.clone();
            // Record how many milliseconds since the program started
            m_lTimeOfBestSolution = System.currentTimeMillis() - m_lStartTime;
    }


	}
    /*
    public void printHeuristicStatistics() {
    System.out.println("\n--- Heuristic Usage Stats ---");
    String[] names = {"AdjSwap", "Reinsert", "NextDesc", "DavisHC", "PMX", "OrderX", "Inversion"};
    for (int i = 0; i < m_aiHeuristicCallCounts.length; i++) {
        System.out.println(names[i] + " (ID " + i + "): " + m_aiHeuristicCallCounts[i] + " calls");
    }}
    */
	
	@Override
	public OBRInstanceInterface getLoadedInstance() {

		// TODO
		return this.m_oInstance;
	}

	/**
	 * @return The integer array representing the ordering of the best solution.
	 */
	@Override
	public int[] getBestSolutionRepresentation() {

		// TODO
		if(m_oBestSolution == null) return null;
    return m_oBestSolution.getSolutionRepresentation().getSolutionRepresentation();
}

	@Override
	public Location[] getRouteOrderedByPoIs() {

		// TODO
		// Get the list from the instance
    ArrayList<Location> routeList = m_oInstance.getSolutionAsListOfLocations(m_oBestSolution);

    //  if the list is null, return an empty array so the visualizer doesn't crash
    if (routeList == null) {
        return new Location[0];
    }

    // convert the ArrayList to a standard Array
    return routeList.toArray(new Location[0]);
}

	public OBRSolutionInterface getBestSolution() {

        // TODO
        return m_oBestSolution;
	}

    /**
     * Should print the best solution found in the form:
     * (d_x,d_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (d_x,d_y)
     * where:
     * `d` is the (bus) depot
     * `l_xi` is the x-coordinate of the location in the i^th index in the solution.
     * `l_yi` is the y-coordinate of the location in the i^th index in the solution.
     * <p>
     * For example:
     * (0,0) - (1,1) - (2,2) - (3,3) - (4,4) - (0,0)
     */
    @Override
public void printBestSolutionFound() {
    Location depot = m_oInstance.getLocationOfBusDepot();
    StringBuilder sb = new StringBuilder();
    sb.append("(").append(depot.x()).append(",").append(depot.y()).append(")");

    int[] route = getBestSolutionRepresentation();
    for (int id : route) {
        Location l = m_oInstance.getLocationForPoI(id);
        sb.append(" - (").append(l.x()).append(",").append(l.y()).append(")");
    }

    sb.append(" - (").append(depot.x()).append(",").append(depot.y()).append(")");
    System.out.println(sb.toString());
}

    /**
     * Prints the objective value of the best solution found.
     */
    @Override
    public void printObjectiveValueOfTheBestSolutionFound() {

        // TODO
        System.out.println( getBestSolutionValue());
    }


    /**
     * Should print the initial solution:
     * (d_x,d_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (d_x,d_y)
     * where:
     * `d` is the (bus) depot
     * `l_xi` is the x-coordinate of the location in the i^th index in the solution.
     * `l_yi` is the y-coordinate of the location in the i^th index in the solution.
     * <p>
     * For example:
     * (0,0) - (2,2) - (1,1) - (3,3) - (4,4) - (0,0)
     */
    @Override
    public void printInitialSolution() {

        // TODO
        if (m_aoSolutionMemory[0] != null) {
        Location depot = m_oInstance.getLocationOfBusDepot();
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(depot.x()).append(",").append(depot.y()).append(")");

        int[] route = m_aoSolutionMemory[0].getSolutionRepresentation().getSolutionRepresentation();
        for (int id : route) {
            Location l = m_oInstance.getLocationForPoI(id);
            sb.append(" - (").append(l.x()).append(",").append(l.y()).append(")");
        }
        sb.append(" - (").append(depot.x()).append(",").append(depot.y()).append(")");
        System.out.println(sb.toString());
    }
    }


}
