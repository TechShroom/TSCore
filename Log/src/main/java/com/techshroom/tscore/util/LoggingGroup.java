package com.techshroom.tscore.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

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
	INFO(Level.INFO.intValue()),
	/**
	 * Non-fatal errors or suggestions for performance
	 */
	WARNING(Level.WARNING.intValue()),
	/**
	 * Fatal errors
	 */
	ERROR(Level.SEVERE.intValue()),
	/**
	 * Debug output for developing
	 */
	DEBUG(Level.CONFIG.intValue()),
	/**
	 * Dump group for unloading tons of data
	 */
	JUNK(Level.FINEST.intValue());

	public static final Set<LoggingGroup> ALL = EnumSet
			.allOf(LoggingGroup.class);

	private static final Map<Integer, LoggingGroup> valueToLevel = new HashMap<Integer, LoggingGroup>();

	static {
		for (LoggingGroup lg : values()) {
			Level al = lg.LEVEL;
			valueToLevel.put(al.intValue(), lg);
		}
	}

	public static final LoggingGroup lookupLevel(int value) {
		return valueToLevel.get(value);
	}

	private static Level getNextLevel(LoggingGroup assoc, int target) {
		Level alreadyWorks = getMatchingValue(target);
		if (alreadyWorks != null) {
			if (alreadyWorks instanceof AssocLevel) {
				// don't overtake one of our levels
				target++;
			} else {
				// there's already another level for this level
				// probably best to reuse that value
				return new AssocLevel(alreadyWorks);
			}
		}
		return new AssocLevel(assoc.name(), findNextValue(target));
	}

	private static List<Level> _known;

	private static List<Level> known() {
		if (_known == null) {
			_known = new ArrayList<Level>();
			Field[] fields = Level.class.getDeclaredFields();
			for (Field f : fields) {
				if (f.getType() == Level.class && f.isAccessible()) {
					try {
						_known.add((Level) f.get(null));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return _known;
	}

	private static Level getMatchingValue(int target) {
		List<Level> k = known();
		for (Level l : k) {
			if (l.intValue() == target) {
				return l;
			}
		}
		return null;
	}

	private static int findNextValue(int target) {
		int i = target;
		List<Level> k = known();
		for (Level l : k) {
			if (i == l.intValue()) {
				i += 1;
			}
		}
		return i;
	}

	public final Level LEVEL;

	private LoggingGroup(int level) {
		LEVEL = getNextLevel(this, level);
	}

	private static final class AssocLevel extends Level {
		private static final long serialVersionUID = -2098180269622683720L;

		AssocLevel(String name, int value) {
			super(name, value);
		}

		AssocLevel(Level match) {
			this(match.getName(), match.intValue());
		}

		private Object readResolve() {
			synchronized (Level.class) {
				List<Level> known = known();
				for (int i = 0; i < known.size(); i++) {
					Level other = known.get(i);
					if (this.getName().equals(other.getName())
							&& this.intValue() == other.intValue()
							&& (this.getResourceBundleName() == other
									.getResourceBundleName() || (this
									.getResourceBundleName() != null && this
									.getResourceBundleName().equals(
											other.getResourceBundleName())))) {
						return other;
					}
				}
				// Woops. Whoever sent us this object knows
				// about a new log level. Add it to our list.
				known.add(this);
				return this;
			}
		}
	}
}