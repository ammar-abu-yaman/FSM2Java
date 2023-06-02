package com.sample;

public class NextState extends Base {

	public NextState(SyntaxContext context) {
	    super(context, "2", "NextState");
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void normalTransition(Type param) {

		Syntax ctx = context.getOwner();

		super.normalTransition(param);

	}

	public void innerLoopbackTransition() {

		Syntax ctx = context.getOwner();

		super.innerLoopbackTransition();

	}

}

