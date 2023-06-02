package com.sample;

import com.fsm4j.AbstractState;

public abstract class SyntaxState extends AbstractState {

	protected SyntaxContext context;


	protected SyntaxState(SyntaxContext context, String _id, String _name) {
	    super(_id, _name);
	    this.context = context;
	}


	protected void normalTransition(Type param) {

		Base();

	}

	protected void innerLoopbackTransition() {

		Base();

	}

	protected void Base() {
	    throw new RuntimeException("Undefined transtion");
	}

}

