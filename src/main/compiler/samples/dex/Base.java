package com.sample;

public class Base extends SyntaxState {

	public Base(SyntaxContext context) {
	    super(context, "0", "Base");
	}


	public Base(SyntaxContext context, String _id, String _name) {
		super(context, _id, _name);
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

