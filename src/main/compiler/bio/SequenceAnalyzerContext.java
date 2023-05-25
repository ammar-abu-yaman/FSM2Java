package com.bioinformatics;

import com.fsm4j.AbstractFsmContext;

public class SequenceAnalyzerContext extends AbstractFsmContext {
	protected SequenceAnalyzer owner;


	public SequenceAnalyzerContext(SequenceAnalyzer owner) {
	    this.owner = owner;
	    setState(new Start(this));
	}

	public void match(char nucleotide) {

		((SequenceAnalyzerState) getState()).match(nucleotide);

	}


	public SequenceAnalyzer getOwner() {
	    return this.owner;
	}

}

