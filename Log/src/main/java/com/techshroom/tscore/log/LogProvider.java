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
