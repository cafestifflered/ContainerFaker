package com.stifflered.containerfaker.pool.fill;

import com.stifflered.containerfaker.pool.fill.impl.RandomFillStrategy;
import com.stifflered.containerfaker.pool.fill.impl.RandomSlottedFillStrategy;

public enum FillType {
    FILL_RANDOM(new RandomFillStrategy()),
    SLOTTED_RANDOM(new RandomSlottedFillStrategy());

    private final FillStrategy strategy;

    FillType(FillStrategy fillStrategy) {
        this.strategy = fillStrategy;
    }

    public FillStrategy getStrategy() {
        return this.strategy;
    }
}
