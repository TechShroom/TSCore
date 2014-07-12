package com.techshroom.tscore.util;


import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;


public final class LogProvider {

	private LogProvider() {
	}

	/**
	 * A dummy method to load this class. Does nothing.
	 */
	public static void init() {

	}

	/**
	 * <p>
	 * These logging groups are used to filter certain information. The default
	 * logging combo is INFO + WARNING + ERROR.
	 * </p>
	 * 
	 * Logging groups from lowest to highest: INFO, WARNING, DEBUG, JUNK.<br>
	 * <br>
	 * 
	 * Recommended usages:
	 * <dl>
	 * <dt>INFO</dt>
	 * <dd>- STDOUT</dd>
	 * <dt>WARNING</dt>
	 * <dd>- warnings like non-fatal OpenGL errors</dd>
	 * <dt>ERROR</dt>
	 * <dd>- STDERR</dd>
	 * <dt>DEBUG</dt>
	 * <dd>- debug info for developing</dd>
	 * <dt>JUNK</dt>
	 * <dd>- for batch-dumping information</dd>
	 * </dl>
	 */
	public static enum LoggingGroup {
		/**
		 * Standard output for users; etc.
		 */
		INFO,
		/**
		 * Non-fatal errors or suggestions for performance
		 */
		WARNING,
		/**
		 * Fatal errors
		 */
		ERROR,
		/**
		 * Debug output for developing
		 */
		DEBUG,
		/**
		 * Dump group for unloading tons of data
		 */
		JUNK;

		public static final EnumSet<LoggingGroup> ALL = EnumSet
				.allOf(LoggingGroup.class);
	}

	private static Set<LoggingGroup> logGroups = EnumSet.of(LoggingGroup.INFO,
			LoggingGroup.WARNING, LoggingGroup.ERROR);

	public static Set<LoggingGroup> getValidGroups() {
		return EnumSet.copyOf(logGroups);
	}

	public static Set<LoggingGroup> setValidGroups(Set<LoggingGroup> groups) {
		logGroups = EnumSet.copyOf(groups);
		return getValidGroups();
	}

	public static Set<LoggingGroup> setValidGroups(LoggingGroup... groups) {
		return setValidGroups(EnumSet.copyOf(Arrays.asList(groups)));
	}

	public static boolean isValidGroup(LoggingGroup g) {
		return getValidGroups().contains(g);
	}

	private static final Logger bkupLog = Logger.getLogger(
			 "TSCore-backuplog");
	
	public static Logger LOG = bkupLog;
}
