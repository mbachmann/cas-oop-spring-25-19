package com.example.demoinitial.domain.enums;

public enum ProjectType {

    DesignProjectType("DesignProjectType"),
    QualityProjectType("DesignProjectType");

    private final String value;

    ProjectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
