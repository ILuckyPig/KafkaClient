package com.lu.application.controller;

import com.lu.application.Context;

public class RootController {
    public RootController() {
        Context.getControllers().put(this.getClass().getSimpleName(), this);
    }
}
