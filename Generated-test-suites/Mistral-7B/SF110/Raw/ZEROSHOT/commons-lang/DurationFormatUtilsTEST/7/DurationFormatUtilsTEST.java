package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import java.util.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 long durationMillis;
 String expectedResult;

 @BeforeMethod
 public void setup() {
 durationMillis = 1234567;
 }

 @Test
 public void testFormatDurationHMS() {
 expectedResult = "02:04:23.670";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationISO() {
 expectedResult = "P1Y2M3DT4H6M23.670S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDuration() {
 expectedResult = "P1Y2M3DT4H6M23.67s";
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithPadZeros() {
 expectedResult = "P1Y2M3DT4H6M23.670S";
 String format = "P'y'Y'M'M'd'DT'H'H'm'M's.S'S'";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWords() {
 expectedResult = "1 year, 2 months, 3 days, 4 hours, 6 minutes, 23 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodISO() {
 Calendar start = Calendar.getInstance();
 Calendar end = Calendar.getInstance();
 start.setTime(new Date(System.currentTimeMillis() - (durationMillis * 2)));
 end.setTime(new Date(System.currentTimeMillis()));
 expectedResult = "P1Y2M3DT4H6M23.67S";
 String result = DurationFormatUtils.formatPeriodISO(start.getTimeInMillis(), end.getTimeInMillis());
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriod() {
 Calendar start = Calendar.getInstance();
 Calendar end = Calendar.getInstance();
 start.setTime(new Date(System.currentTimeMillis() - (durationMillis * 2)));
 end.setTime(new Date(System.currentTimeMillis()));
 expectedResult = "P1Y2M3DT4H6M23.670S";
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatPeriod(start.getTimeInMillis(), end.getTimeInMillis(), format);
 assertEquals(expectedResult, result);
 }

}