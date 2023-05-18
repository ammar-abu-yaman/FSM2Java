package com.Bio;

public class Start extends Base {

	public Start(BioContext context) {
	    super(context, 3, "Start");
	}


	protected void __exit__() {

		

	}

	protected void __enter__() {

		

	}

	public void match(char n) {

		Bio ctx = context.getOwner();

		if(n=='T') {

			context.getState().__exit__();

			try {

				

			} finally {
			    context.setState(new TState(this));
			    context.getState().__enter__();
			}

			return;

		}

		super.match(n);

	}

}

