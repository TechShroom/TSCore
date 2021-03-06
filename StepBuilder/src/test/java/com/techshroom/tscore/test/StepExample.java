/**
 * Copyright (c) 2014 TechShroom
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.techshroom.tscore.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.techshroom.tscore.util.stepbuilder.*;

/**
 * A class example for the <code>util.stepbuilder</code> package.
 * 
 * @author Kenzie Togami
 */
public class StepExample {

    private static final int A_VALUE = 0;
    private static final int B_VALUE = 1;
    private static final int C_VALUE = 2;
    private static final int D_VALUE = 3;
    private static final String MESSAGE_VALUE = "0123!";

    /**
     * StepBuilder interfaces can be implemented in a concise way and work.
     */
    @SuppressWarnings({ "static-method", "boxing" })
    @Test
    public void buildersImplementAndWork() {
        Build build = new Build.Builder().start().a(A_VALUE).b(B_VALUE)
                .c(C_VALUE).d(D_VALUE).message(MESSAGE_VALUE).build();
        assertEquals(build.getA(), A_VALUE);
        assertEquals(build.getB(), B_VALUE);
        assertEquals(build.getC(), C_VALUE);
        assertEquals(build.getD(), D_VALUE);
        assertEquals(build.getMessage(), MESSAGE_VALUE);
    }

    @SuppressWarnings("hiding")
    public static final class Build {
        private final int a, b, c, d;
        private final String message;

        private Build(int a, int b, int c, int d, String msg) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            message = msg;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getC() {
            return c;
        }

        public int getD() {
            return d;
        }

        public String getMessage() {
            return message;
        }

        /**
         * The builder
         * 
         * @author Kenzie Togami
         */
        public static final class Builder implements
                StepBuilder<Build, FirstStep> {
            int a, b, c, d;
            String message;

            @Override
            public FirstStep start() {
                return new FirstStep(this);
            }
        }

        /*
         * Some steps implemented, notice that these could technically be
         * private.
         * 
         * Also, note how the overridden method must be exposed but the better
         * method that names the variable is also there. This is a side effect
         * of using interfaces.
         */

        public static class FirstStep implements
                OneArgStep<Build, Integer, SecondStep> {
            private final Builder b;

            private FirstStep(Builder b) {
                this.b = b;
            }

            @SuppressWarnings("boxing")
            @Override
            public SecondStep step(Integer arg1) {
                b.a = arg1;
                return new SecondStep(b);
            }

            public SecondStep a(Integer a) {
                return step(a);
            }
        }

        public static class SecondStep implements
                OneArgStep<Build, Integer, ThirdStep> {
            private final Builder b;

            private SecondStep(Builder b) {
                this.b = b;
            }

            @SuppressWarnings("boxing")
            @Override
            public ThirdStep step(Integer arg1) {
                b.b = arg1;
                return new ThirdStep(b);
            }

            public ThirdStep b(Integer b_) {
                return step(b_);
            }
        }

        public static class ThirdStep implements
                OneArgStep<Build, Integer, FourthStep> {
            private final Builder b;

            private ThirdStep(Builder b) {
                this.b = b;
            }

            @SuppressWarnings("boxing")
            @Override
            public FourthStep step(Integer arg1) {
                b.c = arg1;
                return new FourthStep(b);
            }

            public FourthStep c(Integer c) {
                return step(c);
            }
        }

        public static class FourthStep implements
                OneArgStep<Build, Integer, FifthStep> {
            private final Builder b;

            private FourthStep(Builder b) {
                this.b = b;
            }

            @SuppressWarnings("boxing")
            @Override
            public FifthStep step(Integer arg1) {
                b.d = arg1;
                return new FifthStep(b);
            }

            public FifthStep d(Integer d) {
                return step(d);
            }
        }

        public static class FifthStep implements
                OneArgStep<Build, String, FinalStep<Build>> {
            private final Builder b;

            private FifthStep(Builder b) {
                this.b = b;
            }

            @Override
            public FinalStep<Build> step(String arg1) {
                b.message = arg1;
                return new FinalStep<Build>() {

                    @Override
                    public Build build() {
                        Build build = new Build(b.a, b.b, b.c, b.d, b.message);

                        // normally you would check things here

                        return build;
                    }
                };
            }

            public FinalStep<Build> message(String message) {
                return step(message);
            }
        }
    }
}
