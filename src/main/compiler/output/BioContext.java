package com.Bio;

import com.fsm4j.AbstractFsmContext;

public class BioContext extends AbstractFsmContext {
	public BioContext(Bio owner) {
	    this.owner = owner;
	    setState(new Start(this));
	}

	public void match(char n) {

		getState().match(n);

	}

}

