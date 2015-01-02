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
package com.techshroom.tscore.log;

import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LogProvider {

    private LogProvider() {
    }

    private static final Logger LOG = LogManager.getLogger();

    /**
     * Original streams from start of program
     */
    public static final PrintStream STDOUT = System.out, STDERR = System.err;

    public static final PrintStream METHODIZEDOUT, METHODIZEDERR;

    static {
        STDOUT.print("Methodizing STD streams...");
        METHODIZEDOUT = new MethodizedSTDStream(STDOUT).asPrintStream();
        METHODIZEDERR = new MethodizedSTDStream(STDERR).asPrintStream();
        STDOUT.println("complete.");
    }

    public static void init() {
        // does nothing
    }

    public static void overrideSTDStreams() {
        METHODIZEDOUT.print("Setting System.out...");
        System.setOut(METHODIZEDOUT);
        METHODIZEDOUT.println("complete.");
        METHODIZEDERR.print("Setting System.err...");
        System.setErr(METHODIZEDERR);
        METHODIZEDERR.println("complete.");
    }

    static void print(String message) {
        LOG.info(message);
    }
}
