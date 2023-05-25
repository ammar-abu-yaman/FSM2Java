package com.bioinformatics;

public class Start extends Base {

	public Start(SequenceAnalyzerContext context) {
	    super(context, "1", "Start");
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void match(char nucleotide) {

		SequenceAnalyzer ctx = context.getOwner();

		if(nucleotide == 'T') {

			context.getState().__exit__();

			try {

				

			} finally {
			    context.setState(new TState(context));
			    context.getState().__enter__();
			}

			return;

		}

		else {

			try {

				

			} finally {
			    context.setState(new Start(context));
			
			}

			return;

		}

	}

}

