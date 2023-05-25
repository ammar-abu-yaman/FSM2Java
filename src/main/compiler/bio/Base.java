package com.bioinformatics;

public class Base extends SequenceAnalyzerState {

	public Base(SequenceAnalyzerContext context) {
	    super(context, "0", "Base");
	}


	public Base(SequenceAnalyzerContext context, String _id, String _name) {
		super(context, _id, _name);
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void match(char nucleotide) {

		SequenceAnalyzer ctx = context.getOwner();

		super.match(nucleotide);

	}

}

