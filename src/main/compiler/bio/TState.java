package com.bioinformatics;

public class TState extends Base {

	public TState(SequenceAnalyzerContext context) {
	    super(context, "2", "TState");
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void match(char nucleotide) {

		SequenceAnalyzer ctx = context.getOwner();

		if(nucleotide == 'A') {

			context.getState().__exit__();

			try {

				

			} finally {
			    context.setState(new TAState(context));
			    context.getState().__enter__();
			}

			return;

		}

		else {

			context.getState().__exit__();

			try {

				

			} finally {
			    context.setState(new Start(context));
			    context.getState().__enter__();
			}

			return;

		}

	}

}

