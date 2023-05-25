package com.bioinformatics;

public class SequenceAnalyzer implements SequenceAnalyzerActions {

	private SequenceAnalyzerContext context;


	public SequenceAnalyzer() {
	    this.context = new SequenceAnalyzerContext(this);
	}


	public void found() {

		throw new UnsupportedOperationException("Action found is not implemented");

	}

}

