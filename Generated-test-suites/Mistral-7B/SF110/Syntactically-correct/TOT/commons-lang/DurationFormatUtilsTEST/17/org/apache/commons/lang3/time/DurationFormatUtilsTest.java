package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "02:07:13";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationHMS_Negative() {
 long durationMillis = -1234567L;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "-02:07:13";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "P2DT2H7M13S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO_Negative() {
 long durationMillis = -1234567L;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "P-2DT-2H-7M-13S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567L;
 String format = "dd:hh:mm:ss";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 String expected = "02:07:01:13";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDuration_Negative() {
 long durationMillis = -1234567L;
 String format = "dd:hh:mm:ss";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 String expected = "-02:07:01:13";
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDuration_InvalidFormat() {
 long durationMillis = 1234567L;
 String format = "xx:hh:mm:ss";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDuration_InvalidFormat_Negative() {
 long durationMillis = -1234567L;
 String format = "xx:hh:mm:ss";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = false;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 days, 7 hours, 1 minute, 13 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords_SuppressLeadingZeroElements() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = false;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 days, 7 hours, 1 minute, 13 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords_SuppressTrailingZeroElements() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = true;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 days, 7 hours, 1 minute, 13 second";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords_Negative() {
 long durationMillis = -1234567L;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = false;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "-2 days, -7 hours, -1 minute, -13 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords_Negative_SuppressLeadingZeroElements() {
 long durationMillis = -1234567L;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = false;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "-2 days, -7 hours, -1 minute, -13 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords_Negative_SuppressTrailingZeroElements() {
 long durationMillis = -1234567L;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = true;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "-2 days, -7 hours, -1 minute, -13 second";
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWords_InvalidFormat() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = false;
 String format = "dd:hh:mm:ss:sss";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements, format);
 fail("Expected IllegalArgumentException");
 }
}