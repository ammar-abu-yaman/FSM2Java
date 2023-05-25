package com.turnstile;

import com.fsm4j.AbstractState;

public abstract class TurnstileState extends AbstractState {

	protected TurnstileContext context;


	protected TurnstileState(TurnstileContext context, String _id, String _name) {
	    super(_id, _name);
	    this.context = context;
	}


	protected void coin() {

		Base();

	}

	protected void pass() {

		Base();

	}

	protected void Base() {
	    throw new RuntimeException("Undefined transtion");
	}

}

