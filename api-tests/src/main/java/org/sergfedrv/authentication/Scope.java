package org.sergfedrv.authentication;

public enum Scope {
    FULL("fullScopeApp"),
    EMPTY("emptyScopeApp");
    private final String value;
    Scope(String scope) {
        value = scope;
    }

    public String getValue() {
        return value;
    }
}
