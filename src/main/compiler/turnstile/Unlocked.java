package com.turnstile;

public class Unlocked extends Base {

	public Unlocked(TurnstileContext context) {
	    super(context, "2", "Unlocked");
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

				ctx.thankyou();

			} finally {
			    context.setState(new nil(context));
			    context.getState().__enter__();
			}

			return;

		}

	}

	public void pass() {

		Turnstile ctx = context.getOwner();

		{

			context.getState().__exit__();

			try {

				ctx.lock();

			} finally {
			    context.setState(new Locked(context));
			    context.getState().__enter__();
			}

			return;

		}

	}

}

