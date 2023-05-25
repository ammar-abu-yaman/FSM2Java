package com.bioinformatics;

public class TAState extends Base {

	public TAState(SequenceAnalyzerContext context) {
	    super(context, "3", "TAState");
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void match(char nucleotide) {

		SequenceAnalyzer ctx = context.getOwner();

		if(nucleotide == 'G') {

			context.getState().__exit__();

			try {

				

			} finally {
			    context.setState(new TAGState(context));
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

