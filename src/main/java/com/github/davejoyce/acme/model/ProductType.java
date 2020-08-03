package com.github.davejoyce.acme.model;

import java.util.Arrays;

/**
 * Enumeration of Acme product types.
 */
public enum ProductType {

    DOMAIN("domain"),
    P_DOMAIN("protected domain"),
    E_DOMAIN("educational domain"),
    HOSTING("hosting"),
    EMAIL("email");

    private final String label;

    ProductType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ProductType fromLabel(String label) {
        // @formatter:off
        return Arrays.stream(values())
                     .filter(value -> value.getLabel().equals(label))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("No such value: " + label));
        // @formatter:on
    }

}
