package org.sergfedrv.specifications;

public enum ApiAction {
    BARN_UNLOCK("barn-unlock"),
    TOILETSEAT_DOWN("toiletseat-down"),
    CHICKENS_FEED("chickens-feed"),
    EGGS_COLLECT("eggs-collect"),
    EGGS_COUNT("eggs-count");
    public final String value;

    ApiAction(String value) {
        this.value = value;
    }
}
