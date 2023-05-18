package com.Bio;

public class Base extends BioState {

	public Base(BioContext context) {
	    super(context, 0, "Base");
	}


	protected void __exit__() {

		

	}

	protected void __enter__() {

		

	}

	public void match(char n) {

		Bio ctx = context.getOwner();

		super.match(n);

	}

}

