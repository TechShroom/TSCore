package com.techshroom.tscore.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.junit.Test;

import com.techshroom.tscore.util.LogProvider;
import com.techshroom.tscore.util.LoggingGroup;

public class LogProviderTest {
	volatile boolean flag = false;

	@Test
	public void loggingTest() {
		ByteArrayOutputStream outputGrabber = new ByteArrayOutputStream() {
			public void write(int b) {
				super.write(b);
				System.err.println(toString() + ": " + count + "("
						+ Arrays.toString(buf) + ")");
				flag = true;
			}

			public void write(byte b[], int off, int len) {
				super.write(b, off, len);
				System.err.println(toString() + "(" + new String(buf) + ")"
						+ ": " + count + "(" + Arrays.toString(buf) + ")");
				flag = true;
				new Throwable().printStackTrace();
			}
		};
		Logger log = LogProvider.activateLoggingGroups(LogProvider.init("test",
				outputGrabber));

		Formatter using = new Formatter() {

			@Override
			public String format(LogRecord record) {
				System.err.println("formatted");
				return record.getLevel().getLocalizedName() + ": "
						+ record.getMessage() + "\n";
			}
		};

		// let people see what's logging
		log.addHandler(new StreamHandler(LogProvider.STDOUT, using));

		for (Handler h : log.getHandlers()) {
			// catch the errors and print them
			h.setErrorManager(new ErrorManager());
			// attach formatter
			if (h.getFormatter() != using) {
				h.setFormatter(using);
			}
		}

		for (int i = 0; i < factorial(LoggingGroup.ALL.size()).intValue(); i++) {
			LogProvider.deactivateLoggingGroups(log);
			List<LoggingGroup> activate = new ArrayList<LoggingGroup>();
			for (int x = 0; x < LoggingGroup.ALL.size(); x++) {
				if (bitSet(i, x)) {
					activate.add(LoggingGroup.values()[x]);
					log.info("Activating " + LoggingGroup.values()[x]);
				}
			}
			outputGrabber.reset();
			LogProvider.setLoggingGroups(log, activate);
			LogProvider.activateLoggingGroups(log);
			for (LoggingGroup lg : LoggingGroup.ALL) {
				log.log(lg.LEVEL, lg.name() + " logging");
			}
			
			while (flag) {
				try {
					//Thread.sleep(10);
				} catch (Exception e) {
				}
			}
			flag = false;

			String parse = outputGrabber.toString();

			System.err.println(Arrays.toString(parse.getBytes()));

			StringBuilder matchBuilder = new StringBuilder();

			for (LoggingGroup lg : activate) {
				matchBuilder.append(lg.name()).append(": ").append(lg.name())
						.append(" logging").append('\n');
			}

			String matching = matchBuilder.toString();
			assertEquals(matching, parse);
		}
	}

	// math stuff

	static BigInteger recfact(long start, long n) {
		long i;
		if (n <= 16) {
			BigInteger r = BigInteger.valueOf(start);
			for (i = start + 1; i < start + n; i++)
				r = r.multiply(BigInteger.valueOf(i));
			return r;
		}
		i = n / 2;
		return recfact(start, i).multiply(recfact(start + i, n - i));
	}

	static BigInteger factorial(long n) {
		return recfact(1, n);
	}

	static boolean bitSet(int i, int bit) {
		return BigInteger.valueOf(i).testBit(bit);
	}
}
