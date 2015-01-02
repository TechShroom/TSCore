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

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Matcher;

public class MethodizedSTDStream extends ByteArrayOutputStream {
    private static final Field ps_autoFlush;
    static {
        try {
            ps_autoFlush = PrintStream.class.getDeclaredField("autoFlush");
            ps_autoFlush.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("No autoFlush in PrintStream");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    PrintStream orig = null;
    String data = "";
    boolean lastNewline = true, autoFlush = false;

    public MethodizedSTDStream(PrintStream out) {
        orig = out;
        try {
            autoFlush = ps_autoFlush.getBoolean(out);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        try {
            String str = new String(Arrays.copyOfRange(b, off, len));
            str = replaceAllButLast(str, "\\r?\\n",
                    "$0" + Matcher.quoteReplacement(getMethod()));
            if (lastNewline) {
                data += getMethod() + str;
            } else {
                data += str;
            }
            lastNewline = str.endsWith("\n");
            if (autoFlush) {
                flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String replaceAllButLast(String str, String check,
            String replace) {
        String out = "";
        String sub = str.replaceAll(check + "$", "");
        out = str.replace(sub, sub.replaceAll(check, replace));
        return out;
    }

    private static String getMethod() {

        StackTraceElement[] ste = new Throwable().getStackTrace();

        int i = 2; // StackTraceInfo.DUAL_INVOKING_METHOD_ZERO
        StackTraceElement s = null;
        for (; i < ste.length; i++) {
            s = ste[i];
            // skip LUtils.print() because we want the method that called that
            // one.
            if (!s.getClassName().matches("^(java|sun)(.+?)") && !(s
                    .getClassName().equals(LogProvider.class.getName()) && s
                    .getMethodName().equals("print"))) {
                break;
            }
        }
        if (s == null) {
            // there is no stack!
            throw new IllegalStateException("No stack!");
        }
        String[] classsplit = s.getClassName().split("\\.");
        return "[" + classsplit[classsplit.length - 1]
                + "."
                + s.getMethodName()
                + "("
                + s.getFileName()
                + ":"
                + s.getLineNumber()
                + ")@"
                + Thread.currentThread().getName()
                + "] ";
    }

    @Override
    public void flush() throws IOException {
        orig.write(data.getBytes());
        orig.flush();
        data = "";
    }

    public PrintStream asPrintStream() {
        return new PrintStream(this);
    }
}
