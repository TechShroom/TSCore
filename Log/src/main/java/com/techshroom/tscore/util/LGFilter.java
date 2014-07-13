package com.techshroom.tscore.util;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

class LGFilter implements Filter {
	private boolean disabled = false;
	private Set<LoggingGroup> logGroups = EnumSet.of(LoggingGroup.INFO,
			LoggingGroup.WARNING, LoggingGroup.ERROR);

	Set<LoggingGroup> getValidGroups() {
		return EnumSet.copyOf(logGroups);
	}

	Set<LoggingGroup> setValidGroups(Set<LoggingGroup> groups) {
		logGroups = EnumSet.copyOf(groups);
		return getValidGroups();
	}

	Set<LoggingGroup> setValidGroups(LoggingGroup... groups) {
		return setValidGroups(EnumSet.copyOf(Arrays.asList(groups)));
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
		return disabled || ;
	}
}
