package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expected = "11 days 10:40:31.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String expected = "P11D10H40M31.67S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567;
 String format = "d' days 'H' hours 'm' minutes 's' seconds.SSS";
 String expected = "11 days 10:40:31.670";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithPadding() {
 long durationMillis = 1234567;
 String format = "d'  days  'H' hours  'm' minutes  's' seconds.SSS";
 String expected = "11    days 10       hours 58 minutes  31.670 seconds.";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithUnmatchedQuote() {
 String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 try {
 DurationFormatUtils.formatDuration(0L, format);
 } catch (IllegalArgumentException e) {
 assertEquals("Unmatched quote in format: " + format, e.getMessage());
 }
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 123456789;
 long endMillis = 123456798;
 String expected = "P1DPT1H";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 123456789;
 long endMillis = 123456798;
 String format = "Pd PT h m s";
 String expected = "P1D PT1H";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithPadding() {
 long startMillis = 123456789;
 long endMillis = 123456798;
 String format = "Pd PT h m s";
 String expected = "P1 day PT1 hour";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
 assertEquals(expected, result);
 }
}