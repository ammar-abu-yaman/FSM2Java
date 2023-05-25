package com.turnstile;

import com.fsm4j.AbstractFsmContext;

public class TurnstileContext extends AbstractFsmContext {
	protected Turnstile owner;


	public TurnstileContext(Turnstile owner) {
	    this.owner = owner;
	    setState(new Locked(this));
	}

	public void coin() {

		((TurnstileState) getState()).coin();

	}

	public void pass() {

		((TurnstileState) getState()).pass();

	}


	public Turnstile getOwner() {
	    return this.owner;
	}

}

