package com.lu.controller;

import com.lu.Context;

public class RootController {
    public RootController() {
        Context.getControllers().put(this.getClass().getSimpleName(), this);
    }
}
