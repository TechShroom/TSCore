package com.techshroom.tscore.log;

import java.util.*;

import org.apache.logging.log4j.Level;

/**
 * <p>
 * These logging groups are used to filter certain information. The default
 * logging combo is INFO + WARNING + ERROR.
 * </p>
 * 
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
public enum LoggingGroup {
	/**
	 * Standard output for users; etc.
	 */
	INFO(Level.INFO),
	/**
	 * Suggestions for performance
	 */
	SUGGESTIONS(Level.WARN),
	/**
	 * Non-fatal errors
	 */
	WARNING(Level.ERROR),
	/**
	 * Fatal errors
	 */
	ERROR(Level.FATAL),
	/**
	 * Debug output for developing
	 */
	DEBUG(Level.DEBUG),
	/**
	 * Dump group for unloading tons of data
	 */
	JUNK(Level.TRACE);

	public static final Set<LoggingGroup> ALL = EnumSet
			.allOf(LoggingGroup.class);

	private static final Map<Level, LoggingGroup> valueToLevel = new HashMap<Level, LoggingGroup>();

	static {
		for (LoggingGroup lg : values()) {
			Level al = lg.LEVEL;
			valueToLevel.put(al, lg);
		}
	}

	public static final LoggingGroup lookupLevel(Level value) {
		return valueToLevel.get(value);
	}

	public final Level LEVEL;

	private LoggingGroup(Level level) {
		LEVEL = level;
	}
}