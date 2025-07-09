//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nextlevel.platform.edi.enums;

public enum MeterPointType implements IEdiEnum {
    Z30("Marktlokation"),
    Z31("Messlokation"),
    Z32("Lieferantensummenzeitreihen"),
    Z70("Tranche"),
    Z71("Markt- und Messlokation"),
    Z67("Marktlokation und bisheriger Identifikator"),
    Z68("bisher Identifikator zukünfig nicht genutzt"),
    Z69("Marktlokation und zukünftiger Identifikator"),
    Z82("prozessual behandelte Messlokation");

    private final String description;

    private MeterPointType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getValue() {
        return this.name();
    }
}
