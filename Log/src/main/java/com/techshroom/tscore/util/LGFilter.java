package com.techshroom.tscore.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class LGFilter implements Filter {
	private boolean disabled = false;
	private Set<LoggingGroup> logGroups = EnumSet.of(LoggingGroup.INFO,
			LoggingGroup.WARNING, LoggingGroup.ERROR);

	Set<LoggingGroup> getValidGroups() {
		return EnumSet.copyOf(logGroups);
	}

	void setValidGroups(Collection<LoggingGroup> groups) {
		if (groups.isEmpty()) {
			logGroups = EnumSet.noneOf(LoggingGroup.class);
			return;
		}
		logGroups = EnumSet.copyOf(groups);
	}

	void setValidGroups(LoggingGroup... groups) {
		setValidGroups(Arrays.asList(groups));
	}

	boolean isValidGroup(LoggingGroup g) {
		return getValidGroups().contains(g);
	}

	void disable() {
		disabled = true;
	}

	void enable() {
		disabled = false;
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		if (disabled) {
			return true;
		}
		Level match = record.getLevel();
		LoggingGroup matched = LoggingGroup.lookupLevel(match.intValue());
		if (matched == null) {
			// not a known handler level
			// since we don't go by Level's values + structure this is an
			// invalid value
			return false;
		}
		return isValidGroup(matched);
	}
}
