package com.techshroom.tscore.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

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
public enum LoggingGroup {
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

	public static final Set<LoggingGroup> ALL = EnumSet
			.allOf(LoggingGroup.class);
	
	private static Level getNextLevel(LoggingGroup assoc) {
		return new AssocLevel(assoc.name(), findNextValue());
	}
	
	private static final List<Level> Level_known_backup = new ArrayList<Level>();
	private static final Field Level_known;
	static {
		Field lvlknown = null;
		try {
			lvlknown = Level.class.getDeclaredField("known");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				lvlknown = LoggingGroup.class.getDeclaredField("Level_known_backup");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		lvlknown.setAccessible(true);
		Level_known = lvlknown;
	}
	
	private static int findNextValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public final Level LEVEL = getNextLevel(this);

	private static final class AssocLevel extends Level {
		protected AssocLevel(String name, int value) {
			super(name, value);
		}
		
	}
}