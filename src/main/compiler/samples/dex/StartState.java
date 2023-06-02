package com.sample;

public class StartState extends Base {

	public StartState(SyntaxContext context) {
	    super(context, "1", "StartState");
	}


	public void __exit__() {

		

	}

	public void __enter__() {

		

	}

	public void normalTransition(Type param) {

		Syntax ctx = context.getOwner();

		if(param.condition()) {

			context.getState().__exit__();

			try {

				ctx.action1(); // executes action1 in transition code

			} finally {
			    context.setState(new NextState(context));
			    context.getState().__enter__();
			}

			return;

		}

		super.normalTransition(param);

	}

	public void innerLoopbackTransition() {

		Syntax ctx = context.getOwner();

		{

			try {

				

			} finally {
			    context.setState(new StartState(context));
			
			}

			return;

		}

	}

}

