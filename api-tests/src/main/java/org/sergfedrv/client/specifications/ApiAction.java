package org.sergfedrv.client.specifications;

public enum ApiAction {
    BARN_UNLOCK("barn-unlock"),
    TOILETSEAT_DOWN("toiletseat-down"),
    CHICKENS_FEED("chickens-feed"),
    EGGS_COLLECT("eggs-collect"),
    EGGS_COUNT("eggs-count");
    private final String value;

    ApiAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
