package com.turnstile;

public class Base extends TurnstileState {

	public Base(TurnstileContext context) {
	    super(context, "0", "Base");
	}


	public Base(TurnstileContext context, String _id, String _name) {
		super(context, _id, _name);
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void coin() {

		Turnstile ctx = context.getOwner();

		super.coin();

	}

	public void pass() {

		Turnstile ctx = context.getOwner();

		super.pass();

	}

}

