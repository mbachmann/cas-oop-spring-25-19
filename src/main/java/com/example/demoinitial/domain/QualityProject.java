package com.example.demoinitial.domain;

import com.example.demoinitial.domain.enums.ProjectType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("QualityProject")
public class QualityProject extends Project {

    public QualityProject() {}

    public QualityProject(String name) {
        super(name);
        super.type = ProjectType.QualityProjectType.getValue();
    }
}
