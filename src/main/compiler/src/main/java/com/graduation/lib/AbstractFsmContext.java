package com.graduation.lib;

public class AbstractFsmContext {

    private AbstractState _currentState;

    public AbstractFsmContext() {
    }

    public void setState(AbstractState state) {
        this._currentState = state;
    }

    public AbstractState getState() {
        return _currentState;
    }

}
