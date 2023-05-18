package com.Bio;

public class TState extends Base {

	public TState(BioContext context) {
	    super(context, 2, "TState");
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
			    context.setState(new TAState(this));
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

