package com.sample;

import com.fsm4j.AbstractFsmContext;

public class SyntaxContext extends AbstractFsmContext {
	protected Syntax owner;


	public SyntaxContext(Syntax owner) {
	    this.owner = owner;
	    setState(new StartState(this));
	}

	public void normalTransition(Type param) {

		((SyntaxState) getState()).normalTransition(param);

	}

	public void innerLoopbackTransition() {

		((SyntaxState) getState()).innerLoopbackTransition();

	}


	public Syntax getOwner() {
	    return this.owner;
	}

}

