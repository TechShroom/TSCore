package com.techshroom.tscore.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public final class LogProvider {

	private LogProvider() {
	}

	private static final Logger bkupLog = Logger.getLogger("TSCore-backuplog");

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
		METHODIZEDOUT.print("Setting System.out...");
		System.setOut(METHODIZEDOUT);
		METHODIZEDOUT.println("complete.");
		METHODIZEDERR.print("Setting System.err...");
		System.setErr(METHODIZEDERR);
		METHODIZEDERR.println("complete.");
	}

	public static Logger LOG = bkupLog;

	/**
	 * Sets the {@link #LOG} variable.
	 * 
	 * @param l
	 *            - the new logger that should be used
	 */
	public static void setLog(Logger l) {
		LOG = l;
		LOG.log(Level.INFO, "Using log " + l.getName());
	}

	/**
	 * Initializes a logger instance for the given resource. Prints to STDOUT.
	 * 
	 * @param resource
	 *            - the name for the logger
	 * 
	 * @return a new Logger dumping to STDOUT.
	 */
	public static Logger init(String resource) {
		return init(resource, true);
	}

	/**
	 * Initializes a logger instance for the given resource, printing to the
	 * specified standard stream.
	 * 
	 * @param resource
	 *            - the name for the logger
	 * @param stdout
	 *            - true for STDOUT, false for STDERR
	 * 
	 * @return a new Logger dumping to the specified standard stream.
	 */
	public static Logger init(String resource, boolean stdout) {
		return init(resource, stdout ? STDOUT : STDERR);
	}

	/**
	 * Initializes a logger instance for the given resource, printing to the
	 * specified stream.
	 * 
	 * @param resource
	 *            - the name for the logger
	 * @param stream
	 *            - the output stream
	 * 
	 * @return a new Logger dumping to the specified stream.
	 */
	public static Logger init(String resource, OutputStream stream) {
		Logger l = Logger.getLogger(resource);
		l.setUseParentHandlers(false);
		l.addHandler(new StreamHandler(stream, new SimpleFormatter()));
		return l;
	}

	/**
	 * Activate the filter for logging groups. Leave the level of the logger
	 * alone after this method.
	 * 
	 * @param l
	 *            - the logger to activate
	 * @return the logger
	 */
	public static Logger activateLoggingGroups(Logger l) {
		l.setFilter(new Filter() {

			@Override
			public boolean isLoggable(LogRecord record) {
				return false;
			}
		});
		l.setLevel(Level.ALL);
		return l;
	}

	/**
	 * Deactivates the logging groups, but leaves settings intact. A call to
	 * {@link #activateLoggingGroups(Logger)} will re-enable with the same
	 * settings.
	 * 
	 * @param l
	 *            - the logger to deactivate
	 * @return the logger
	 */
	public static Logger deactivateLoggingGroups(Logger l) {
		LGFilter filter = safeGetFilter(l);
		return l;
	}

	private static LGFilter safeGetFilter(Logger l) {
		Filter f = l.getFilter();
		if (f instanceof LGFilter) {
			return (LGFilter) f;
		}
		return null;
	}
}
