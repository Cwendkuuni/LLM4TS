package org.apache.commons.lang3.test;

import org.apache.commons.lang3.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 private static final long DURATION_MILLIS = 1234567890L;
 private static final String HMS_FORMAT = "H:mm:ss.SSS";
 private static final String ISO_FORMAT = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 private static final String WORDS_FORMAT = "d' days 'H' hours 'm' minutes 's' seconds'";
 private static final String PERIOD_ISO_FORMAT = "P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";

 @BeforeClass
 public static void setUpClass() {
 // Set up any static resources or initialize any shared fixtures here
 }

 @AfterClass
 public static void tearDownClass() {
 // Perform any necessary cleanup here
 }

 @Before
 public void setUp() {
 // Initialize any test-specific state here
 }

 @After
 public void tearDown() {
 // Perform any necessary cleanup here
 }

 @Test
 public void testFormatDurationHMS() {
 String expected = "01:46:34.789";
 String actual = DurationFormatUtils.formatDurationHMS(DURATION_MILLIS);
 assertTrue("Unexpected format for HMS duration", expected.equals(actual));
 }

 @Test
 public void testFormatDurationISO() {
 String expected = "P3Y1M10D1H46M34.789S";
 String actual = DurationFormatUtils.formatDurationISO(DURATION_MILLIS);
 assertTrue("Unexpected format for ISO duration", expected.equals(actual));
 }

 @Test
 public void testFormatDurationWithInvalidFormat() {
 try {
 DurationFormatUtils.formatDuration(DURATION_MILLIS, "InvalidFormat");
 fail("Expected IllegalArgumentException with message 'Invalid format string'");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long negativeDurationMillis = -DURATION_MILLIS;
 String hmsFormat = "H:mm:ss.SSS";
 String isoFormat = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 String hmsExpected = "-01:46:34.789";
 String isoExpected = "P-3Y-1M-10D-1H-46M-34.789S";
 String hmsActual = DurationFormatUtils.formatDurationHMS(negativeDurationMillis);
 String isoActual = DurationFormatUtils.formatDurationISO(negativeDurationMillis);
 assertEquals(hmsExpected, hmsActual);
 assertEquals(isoExpected, isoActual);
 }

 @Test
 public void testFormatDurationWords() {
 String expected = "3 years, 1 month and 10 days, 1 hour, 46 minutes and 34 seconds";
 String actual = DurationFormatUtils.formatDurationWords(DURATION_MILLIS, false, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWordsWithSuppressedLeadingZeroElements() {
 String expected = "3 years, 1 month, 10 days, 1 hour, 46 minutes and 34 seconds";
 String actual = DurationFormatUtils.formatDurationWords(DURATION_MILLIS, true, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWordsWithSuppressedTrailingZeroElements() {
 String expected = "3 years, 1 month, 10 days, 1 hour, 46 minutes and 34 seconds";
 String actual = DurationFormatUtils.formatDurationWords(DURATION_MILLIS, false, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodISO() {
 Date startDate = new Date(0L);
 Date endDate = new Date(DURATION_MILLIS);
 String expected = "P3Y1M10DT1H46M34.789S";
 String actual = DurationFormatUtils.formatPeriodISO(startDate.getTime(), endDate.getTime());
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 try {
 DurationFormatUtils.formatPeriod(0L, 0L, "InvalidFormat");
 fail("Expected IllegalArgumentException with message 'Invalid format string'");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }
}