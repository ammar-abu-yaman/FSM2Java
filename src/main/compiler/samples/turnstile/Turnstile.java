package com.turnstile;

public class Turnstile implements TurnstileActions {

	private TurnstileContext context;

	public Turnstile() { this.context = new TurnstileContext(this); }

	public void coin() {context.coin(); }

	public void pass() { context.pass(); }

	@Override
	public void lock() {
		System.out.println("The gate is locked");
	}

	@Override
	public void unlock() {
		System.out.println("The gate is unlocked, you may pass");
	}

	@Override
	public void alarm() {
		System.out.println("Alarm! entered without paying");
	}

	@Override
	public void thankyou() {
		System.out.println("Thanks for the extra coin");
	}

}

