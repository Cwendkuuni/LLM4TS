package org.apache.commons.lang3.test;

import org.apache.commons.lang3.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @BeforeClass
 static void setup() {
 // Initialize any static variables or resources here
 }

 @AfterClass
 static void teardown() {
 // Release any static resources or variables here
 }

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expected = "1:46:11.700";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567890123L;
 String expected = "P3Y2M1D1H4M11S.701M";
 String actual = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567L;
 String expected = "-1:46:11.700";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormat() {
 long durationMillis = 1234567890123L;
 String format = "H:mm:ss.SS";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 String expected = "1 day 1 hour 46 minutes 11 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890123L;
 long endMillis = 1434567890123L;
 String expected = "P3Y2M1D1H4M11S.701M";
 String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodWithNegativeDuration() {
 long startMillis = 1434567890123L;
 long endMillis = 1234567890123L;
 String expected = "P-3Y2M1D1H4M11S.701M";
 String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 long startMillis = 1234567890123L;
 long endMillis = 1434567890123L;
 String format = "PddMMyyyyHHmmssSS";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail("Expected IllegalArgumentException but got: " + actual);
 }
}