package com.aim.project.obr.solution;

import com.aim.project.obr.interfaces.OBRSolutionInterface;
import com.aim.project.obr.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class OBRSolution implements OBRSolutionInterface {

	private SolutionRepresentationInterface m_oRepresentation;
    private int m_iObjectiveFunctionValue;

    public OBRSolution(SolutionRepresentationInterface oRepresentation, int iObjectiveFunctionValue) {

        this.m_oRepresentation = oRepresentation;
        this.m_iObjectiveFunctionValue = iObjectiveFunctionValue;
    }

	@Override
	public int getObjectiveFunctionValue() {

        // TODO
        return m_iObjectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(int iObjectiveFunctionValue) {

        // TODO
		this.m_iObjectiveFunctionValue = iObjectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {

        // TODO
        return m_oRepresentation;
	}
	
	@Override
	public OBRSolutionInterface clone() {

		// Deep copy: clone the representation so changes to the clone
        // don't affect the original solution.
        SolutionRepresentationInterface oClonedRepresentation = m_oRepresentation.clone();
        return new OBRSolution(oClonedRepresentation, m_iObjectiveFunctionValue);
    }


	@Override
	public int getNumberOfLocations() {

        // TODO
        return m_oRepresentation.getTotalNumberOfLocations();
	}
}
