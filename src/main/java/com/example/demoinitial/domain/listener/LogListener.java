package com.example.demoinitial.domain.listener;

import com.example.demoinitial.domain.Employee;

import java.util.logging.Logger;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;


public class LogListener {

    private static final Logger logger = Logger.getLogger(LogListener.class.toString());

    @PostLoad
    private void postLoad(Object obj) {
        if (obj instanceof Employee) {
            logger.info("postLoad: " + obj);
        }
    }

    @PostPersist
    private void postPersist(Object obj) {
        logger.info("postPersist: " + obj);
    }

    @PostRemove
    private void postRemove(Object obj) {
        logger.info("postRemove: " + obj);
    }

    @PostUpdate
    private void postUpdate(Object obj) {
        logger.info("postUpdate: " + obj);
    }

    @PrePersist
    private void prePersist(Object obj) {
        logger.info("prePersist: " + obj);
    }

    @PreUpdate
    private void preUpdate(Object obj) {
        logger.info("preUpdate: " + obj);
    }
}
