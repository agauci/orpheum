package com.orpheum.benchmark.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public abstract class AbstractEntity<ID, T extends AbstractEntity<ID, T>> implements Persistable<ID> {

    @Transient
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public T markAsNew() {
        this.isNew = true;

        return (T) this;
    }

}
