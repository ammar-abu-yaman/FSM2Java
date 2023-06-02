package com.sample;

public class Syntax implements SyntaxActions {

	private SyntaxContext context;


	public Syntax() {
	    this.context = new SyntaxContext(this);
	}


	public void action1() {

		throw new UnsupportedOperationException("Action action1 is not implemented");

	}

	public void action2() {

		throw new UnsupportedOperationException("Action action2 is not implemented");

	}

}

