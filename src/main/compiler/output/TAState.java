package com.Bio;

public class TAState extends Base {

	public TAState(BioContext context) {
	    super(context, 4, "TAState");
	}


	protected void __exit__() {

		

	}

	protected void __enter__() {

		

	}

	public void match(char n) {

		Bio ctx = context.getOwner();

		if(n=='A') {

			context.getState().__exit__();

			try {

				

			} finally {
			    context.setState(new TAGState(this));
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

