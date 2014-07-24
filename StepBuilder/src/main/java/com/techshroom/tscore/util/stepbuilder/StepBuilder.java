package com.techshroom.tscore.util.stepbuilder;

public interface StepBuilder<BUILDTYPE, NEXT extends Step<BUILDTYPE, ?>> {
    NEXT start();
}
