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