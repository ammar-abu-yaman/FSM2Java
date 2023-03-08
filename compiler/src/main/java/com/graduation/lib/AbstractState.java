package com.graduation.lib;

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

    protected void __enter__() {

    }

    protected void __exit__() {

    }

}
