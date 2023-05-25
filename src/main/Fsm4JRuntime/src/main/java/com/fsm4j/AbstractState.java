package com.fsm4j;

import java.io.Serializable;

public abstract class AbstractState implements Serializable {

    protected final String _id;
    protected final String _name;

    public AbstractState(String _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public String getStateId() {
        return _id;
    }

    public String getStateName() {
        return _name;
    }

    public void __enter__() {

    }

    public void __exit__() {

    }

}
