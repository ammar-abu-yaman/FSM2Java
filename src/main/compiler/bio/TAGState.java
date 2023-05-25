package com.bioinformatics;

public class TAGState extends Base {

	public TAGState(SequenceAnalyzerContext context) {
	    super(context, "4", "TAGState");
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void match(char nucleotide) {

		SequenceAnalyzer ctx = context.getOwner();

		if(nucleotide == 'C') {

			context.getState().__exit__();

			try {

				ctx.found();

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

