package com.techshroom.tscore.util.stepbuilder;

public interface SkipStep<BUILDTYPE, NEXT extends Step<BUILDTYPE, ?>> extends
        Step<BUILDTYPE, NEXT> {
    NEXT skip();
}
