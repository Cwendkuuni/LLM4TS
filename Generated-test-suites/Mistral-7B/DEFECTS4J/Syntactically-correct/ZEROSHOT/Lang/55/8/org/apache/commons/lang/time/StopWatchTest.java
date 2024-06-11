package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Date;

import org.junit.Test;

public class StopWatchTest {

	@Test
	public void testStartAndStop() {
		StopWatch sw = new StopWatch();
		sw.start();
		sw.stop();

		assertNotNull(sw.getTime());
		assertEquals(0, sw.getSplitTime());
		assertEquals("0.000:00.000.001", sw.toString());
	}

	@Test
	public void testStartSplitAndStop() {
		StopWatch sw = new StopWatch();
		sw.start();
		sw.split();
		sw.stop();

		assertNotNull(sw.getTime());
		assertNotNull(sw.getSplitTime());
		assertEquals("0.000:00.000.050", sw.toString());
		assertEquals("0.000:00.000.001", sw.toSplitString());
	}

	@Test
	public void testStartSplitStopSplitAndStop() {
		StopWatch sw = new StopWatch();
		sw.start();
		sw.split();
		sw.split();
		sw.stop();

		assertNotNull(sw.getTime());
		assertNotNull(sw.getSplitTime());
		assertNotNull(sw.getSplitTime(1));
		assertEquals("0.000:00.000.100", sw.toString());
		assertEquals("0.000:00.000.001", sw.toSplitString());
		assertEquals("0.000:00.000.050", sw.toSplitString(1));
	}

	@Test
	public void testSuspendResume() {
		StopWatch sw = new StopWatch();
		sw.start();
		sw.suspend();
		sw.resume();
		sw.stop();

		assertNotNull(sw.getTime());
		assertEquals(0, sw.getSplitTime());
		assertEquals("0.000:00.000.300", sw.toString());
	}

	@Test
	public void testSuspendResumeSuspendResumeAndStop() {
		StopWatch sw = new StopWatch();
		sw.start();
		sw.suspend();
		sw.resume();
		sw.suspend();
		sw.resume();
		sw.stop();

		assertNotNull(sw.getTime());
		assertEquals(0, sw.getSplitTime());
		assertEquals("0.000:00.000.700", sw.toString());
	}

	@Test
	public void testIllegalMethodsCalls() {
		StopWatch sw = new StopWatch();

		assertThrows(IllegalStateException.class, sw::stop);
		assertThrows(IllegalStateException.class, sw::split);
		assertThrows(IllegalStateException.class, sw::unsplit);
		assertThrows(IllegalStateException.class, sw::suspend);
		assertThrows(IllegalStateException.class, sw::resume);

		sw.start();

		assertThrows(IllegalStateException.class, () -> sw.stop());
		assertThrows(IllegalStateException.class, () -> sw.split());
		assertThrows(IllegalStateException.class, () -> sw.unsplit());
		assertThrows(IllegalStateException.class, () -> sw.suspend());
		assertThrows(IllegalStateException.class, () -> sw.resume());
	}

	@Test
	public void testReset() {
		StopWatch sw = new StopWatch();
		sw.start();
		sw.split();
		sw.stop();
		sw.reset();

		assertNull(sw.getTime());
		assertNull(sw.getSplitTime());
		assertEquals(0, sw.getSplitTime(1));
		assertEquals("", sw.toString());
	}

	@Test
	public void testFormatDurationHMS() {
		long duration = 1234567;
		String format = DurationFormatUtils.formatDurationHMS(duration);
		Date date = new Date(duration);
		String format2 = new StringBuilder().append(date.getHours()).append(":")
				.append(String.format("%02d", date.getMinutes()))
				.append(":").append(String.format("%02d", date.getSeconds()))
				.append(".").append(String.format("%03d", date.getMilliseconds()))
				.toString();
		assertEquals(format, format2);
	}
}