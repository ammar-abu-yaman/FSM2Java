package com.Bio;

public class TAGState extends Base {

	public TAGState(BioContext context) {
	    super(context, 1, "TAGState");
	}


	protected void __exit__() {

		

	}

	protected void __enter__() {

		

	}

	public void match(char n) {

		Bio ctx = context.getOwner();

		if(n=='C') {

			context.getState().__exit__();

			try {

				found()

			} finally {
			    context.setState(new Start(this));
			    context.getState().__enter__();
			}

			return;

		}

		else {

			context.getState().__exit__();

			try {

				

			} finally {
			    context.setState(new Start(this));
			    context.getState().__enter__();
			}

			return;

		}

	}

}

