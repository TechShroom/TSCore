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
package com.techshroom.tscore.regex;

import java.lang.reflect.Method;
import java.util.regex.*;

public class ExtendedMatcher implements IExtendedMatcher {
    @SuppressWarnings("unused")
    private static final Method Matcher_getTextLength;

    static {
        Method dummy = null;
        try {
            dummy = Matcher.class.getDeclaredMethod("getTextLength");
            dummy.setAccessible(true);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "matcher has no getTextLength or it cannot be accessed", e);
        }
        Matcher_getTextLength = dummy;
    }

    public static final ExtendedMatcher get(Matcher m) {
        return new ExtendedMatcher(m);
    }

    public static final ExtendedMatcher get(Pattern p, CharSequence seq) {
        return get(p.matcher(seq));
    }

    public static final ExtendedMatcher get(String regex, CharSequence seq) {
        return get(regex, seq, 0);
    }

    public static final ExtendedMatcher get(String regex, CharSequence seq,
            int flags) {
        return get(Pattern.compile(regex, flags), seq);
    }

    private final Matcher java;

    private ExtendedMatcher(Matcher wrap) {
        java = wrap;
    }

    @Override
    public int start() {
        return java.start();
    }

    @Override
    public int start(int group) {
        return java.start(group);
    }

    @Override
    public int end() {
        return java.end();
    }

    @Override
    public int end(int group) {
        return java.end(group);
    }

    @Override
    public String group() {
        return java.group();
    }

    @Override
    public String group(int group) {
        return java.group(group);
    }

    @Override
    public int groupCount() {
        return java.groupCount();
    }

    public Matcher javaMatcher() {
        return java;
    }

    @Override
    public MatchInfo lookingAt(int start) {
        int[] stateSave = { java.regionStart(), java.regionEnd() };
        boolean res = java.region(start, getTextLength()).lookingAt();
        MatchInfo resmi = MatchInfo.saveState(java, res);
        java.region(stateSave[0], stateSave[1]);
        return resmi;
    }

    @Override
    public MatchInfo matches(int start) {
        int[] stateSave = { java.regionStart(), java.regionEnd() };
        boolean res = java.region(start, getTextLength()).matches();
        MatchInfo resmi = MatchInfo.saveState(java, res);
        java.region(stateSave[0], stateSave[1]);
        return resmi;
    }

    private int getTextLength() {
        return java.regionEnd();
        /*
         * Hope the end is the text length
         * 
         * try { return ((Integer) Matcher_getTextLength
         * .invoke(java)).intValue(); } catch (Exception e) { return 0; }
         */
    }
}
