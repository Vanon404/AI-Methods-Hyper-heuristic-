package com.aim.project.obr.solution;

import com.aim.project.obr.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] m_aiRepresentation;
	public SolutionRepresentation(int[] aiRepresentation) {

        // TODO
		this.m_aiRepresentation = aiRepresentation.clone();
	}

	@Override
	public int[] getSolutionRepresentation() {

        // TODO
        return m_aiRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {

        // TODO
		this.m_aiRepresentation = aiSolutionRepresentation.clone();
	}

	@Override
	public int getTotalNumberOfLocations() {

        // TODO
        return m_aiRepresentation.length;
	}

	@Override
	public SolutionRepresentationInterface clone() {

        // TODO
        return new SolutionRepresentation(this.m_aiRepresentation.clone());
	}
}
