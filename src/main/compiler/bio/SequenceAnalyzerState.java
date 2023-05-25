package com.bioinformatics;

import com.fsm4j.AbstractState;

public abstract class SequenceAnalyzerState extends AbstractState {

	protected SequenceAnalyzerContext context;


	protected SequenceAnalyzerState(SequenceAnalyzerContext context, String _id, String _name) {
	    super(_id, _name);
	    this.context = context;
	}


	protected void match(char nucleotide) {

		Base();

	}

	protected void Base() {
	    throw new RuntimeException("Undefined transtion");
	}

}

