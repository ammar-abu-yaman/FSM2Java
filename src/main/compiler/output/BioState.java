package com.Bio;

import com.fsm4j.AbstractState;

public abstract class BioState extends AbstractState {

	protected BioContext context;


	protected BioState(BioContext context, String _id, String _name) {
	    super(_id, _name);
	    this.context = context;
	}


	protected void match(char n) {

		Base();

	}

	protected void Base() {
	    throw new RuntimeException("Undefined transtion");
	}

}

