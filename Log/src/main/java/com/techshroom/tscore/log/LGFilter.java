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

import java.util.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "LGFilter", category = "Core", elementType = "filter",
        printObject = true)
public class LGFilter extends AbstractFilter {
    private Set<LoggingGroup> logGroups = EnumSet.of(LoggingGroup.INFO,
            LoggingGroup.WARNING, LoggingGroup.ERROR);

    public LGFilter() {
        super(Result.ACCEPT, Result.DENY);
    }

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

    private State state = State.INITIALIZED;

    @Override
    public void start() {
        state = State.STARTING;
        state = State.STARTED;
    }

    @Override
    public void stop() {
        state = State.STOPPING;
        state = State.STOPPED;
    }

    @Override
    public boolean isStarted() {
        return state == State.STARTED;
    }

    @Override
    public boolean isStopped() {
        return state == State.STOPPED;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg,
            Object... params) {
        return filterBase(level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg,
            Throwable t) {
        return filterBase(level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker,
            Message msg, Throwable t) {
        return filterBase(level);
    }

    @Override
    public Result filter(LogEvent event) {
        return filterBase(event.getLevel());
    }

    private Result filterBase(Level l) {
        if (state != State.STARTED) {
            return getOnMatch();
        }
        LoggingGroup lg = LoggingGroup.lookupLevel(l);
        return isValidGroup(lg) ? getOnMatch() : getOnMismatch();
    }

    /**
     * Create a LGFilter.
     * 
     * @return The created LGFilter.
     */
    @PluginFactory
    public static LGFilter createFilter() {
        return new LGFilter();
    }
}
