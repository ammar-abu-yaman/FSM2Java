package com.turnstile;

public class Locked extends Base {

	public Locked(TurnstileContext context) {
	    super(context, "1", "Locked");
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void coin() {

		Turnstile ctx = context.getOwner();

		{

			context.getState().__exit__();

			try {

				ctx.unlock();

			} finally {
			    context.setState(new Unlocked(context));
			    context.getState().__enter__();
			}

			return;

		}

	}

	public void pass() {

		Turnstile ctx = context.getOwner();

		{

			try {

				ctx.alarm();

			} finally {
			    context.setState(new Locked(context));
			
			}

			return;

		}

	}

}

