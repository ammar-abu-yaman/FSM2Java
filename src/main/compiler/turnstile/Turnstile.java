package com.turnstile;

public class Turnstile implements TurnstileActions {

	private TurnstileContext context;


	public Turnstile() {
	    this.context = new TurnstileContext(this);
	}


	public void lock() {

		throw new UnsupportedOperationException("Action lock is not implemented");

	}

	public void unlock() {

		throw new UnsupportedOperationException("Action unlock is not implemented");

	}

	public void alarm() {

		throw new UnsupportedOperationException("Action alarm is not implemented");

	}

	public void thankyou() {

		throw new UnsupportedOperationException("Action thankyou is not implemented");

	}

}

