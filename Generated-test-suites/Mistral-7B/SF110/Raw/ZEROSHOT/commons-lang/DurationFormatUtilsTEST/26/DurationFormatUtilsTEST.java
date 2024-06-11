package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class DurationFormatUtilsTest {

 @Test
 void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expected = "10:04:37.67";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatDurationISO() {
 long durationMillis = 1234567;
 String expected = "P1w3d10h4m37.67s";
 String actual = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatDuration() {
 long durationMillis = 1234567;
 String format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
 String expected = "3d10h4m37s";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatDurationWithPadWithZeros() {
 long durationMillis = 1234567;
 String format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
 String expected = "03d010h04m37s";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatDurationWords() {
 long durationMillis = 1234567;
 String expected = "3 days 10 hours 4 minutes 37 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatDurationWordsSuppressLeadingZeroElements() {
 long durationMillis = 1234567;
 String expected = "3 days, 10 hours, 4 minutes, 37 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatDurationWordsSuppressTrailingZeroElements() {
 long durationMillis = 1234567;
 String expected = "3 days, 10 hours, 4 minutes, 37 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatPeriodISO() {
 Calendar startCalendar = new GregorianCalendar(2019, 1, 1, 0, 0, 0, 0, TimeZone.getDefault());
 long startMillis = startCalendar.getTimeInMillis();
 Calendar endCalendar = new GregorianCalendar(2019, 1, 1, 1, 1, 1, 0, TimeZone.getDefault());
 long endMillis = endCalendar.getTimeInMillis();
 String expected = "P1D";
 String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatPeriod() {
 Calendar startCalendar = new GregorianCalendar(2019, 1, 1, 0, 0, 0, 0, TimeZone.getDefault());
 long startMillis = startCalendar.getTimeInMillis();
 Calendar endCalendar = new GregorianCalendar(2019, 1, 1, 1, 1, 1, 0, TimeZone.getDefault());
 long endMillis = endCalendar.getTimeInMillis();
 String format = "d 'days'";
 String expected = "1d";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatPeriodWithPadWithZeros() {
 Calendar startCalendar = new GregorianCalendar(2019, 1, 1, 0, 0, 0, 0, TimeZone.getDefault());
 long startMillis = startCalendar.getTimeInMillis();
 Calendar endCalendar = new GregorianCalendar(2019, 1, 1, 1, 1, 1, 0, TimeZone.getDefault());
 long endMillis = endCalendar.getTimeInMillis();
 String format = "d 'days'";
 String expected = "01d";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
 assertEquals(expected, actual);
 }

 @Test
 void testLexxFormat() {
 String format = "d 'days' H 'hours' m 'minutes' s 'seconds'S";
 Token[] expectedTokens = new Token[]{
 new Token("d"), new Token("days"),
 new Token("H"), new Token("hours"),
 new Token("m"), new Token("minutes"),
 new Token("s"), new Token("seconds"),
 new Token("S")
 };
 Token[] actualTokens = DurationFormatUtils.lexx(format);
 assertArrayEquals(expectedTokens, actualTokens);
 }

 @Test
 void testFormat() {
 long years = 20;
 long months = 11;
 long days = 30;
 long hours = 23;
 long minutes = 59;
 long seconds = 59;
 long milliseconds = 999;
 String format = "y 'years' M 'months' d 'days' H 'hours' m 'minutes' s 'seconds'S";
 String expected = "20y11M30d23H59m59s999";
 String actual = DurationFormatUtils.format(new Object[]{years, months, days, hours, minutes, seconds, milliseconds}, new Object[]{"y", "years", "M", "months", "d", "days", "H", "hours", "m", "minutes", "s", "seconds", "S"}, false, (Object[])null);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatWithTimeZone() {
 Calendar startCalendar = new GregorianCalendar(2019, 1, 1, 0, 0, 0, 0, TimeZone.getTimeZone("UTC"));
 long startMillis = startCalendar.getTimeInMillis();
 Calendar endCalendar = new GregorianCalendar(2019, 1, 1, 1, 1, 1, 0, TimeZone.getTimeZone("UTC+1:00"));
 long endMillis = endCalendar.getTimeInMillis();
 String format = "d 'days'";
 String expected = "1d";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getTimeZone("UTC+1:00"));
 assertEquals(expected, actual);
 }

 @Test
 void testFormatWithInvalidFormat() {
 String format = "H:mi:ss.SSS";
 assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatDuration(1234567, format));
 }

 @Test
 void testFormatWithNegativeDuration() {
 long durationMillis = -1234567;
 String format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
 String expected = "-1d-10h-4m-37s";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatWithNegativeDurationAndPadWithZeros() {
 long durationMillis = -1234567;
 String format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
 String expected = "-01d-10h-04m-37s";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatWithInvalidFormatAndPadWithZeros() {
 long durationMillis = 1234567;
 String format = "d 'days' H:mi:ss.SSS";
 assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatDuration(durationMillis, format, true));
 }

 @Test
 void testFormatWithInvalidFormatAndNegativeDuration() {
 long durationMillis = -1234567;
 String format = "d 'days' H:mi:ss.SSS";
 assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatDuration(durationMillis, format, true));
 }

 @Test
 void testFormatWithInvalidFormatAndNegativeDurationAndPadWithZeros() {
 long durationMillis = -1234567;
 String format = "d 'days' H:mi:ss.SSS";
 assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatDuration(durationMillis, format, true));
 }

 @Test
 void testFormatWithInvalidFormatAndNegativeDurationAndPadWithZerosAndTimeZone() {
 long durationMillis = -1234567;
 String format = "d 'days' H:mi:ss.SSS";
 assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatPeriod(durationMillis, 0, format, true, TimeZone.getDefault()));
 }

 @Test
 void testFormatWithInvalidFormatAndNegativeDurationAndPadWithZerosAndTimeZoneUTC() {
 long durationMillis = -1234567;
 String format = "d 'days' H:mi:ss.SSS";
 assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatPeriod(durationMillis, 0, format, true, TimeZone.getTimeZone("UTC")));
 }

 @Test
 void testFormatWithInvalidFormatAndNegativeDurationAndPadWithZerosAndTimeZoneUTCPlusOne() {
 long durationMillis = -1234567;
 String format = "d 'days' H:mi:ss.SSS";
 assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatPeriod(durationMillis, 0, format, true, TimeZone.getTimeZone("UTC+1:00")));
 }
}

class Token {
 String value;
 int count;

 Token(String value) {
 this.value = value;
 this.count = 1;
}

 Token(String value, int count) {
 this.value = value;
 this.count = count;
}

 boolean equals(Object obj) {
 if (obj instanceof Token) {
 Token tok = (Token)obj;
 if (this.value.equals(tok.value) && this.count == tok.count) {
 return true;
 }
 }
 return false;
}

 @Override
 public int hashCode() {
 return this.value.hashCode();
}

 @Override
 public String toString() {
 return StringUtils.repeat(this.value, this.count);
}
}

class TokenTest {
 @Test
 void testEquals() {
 Token tok1 = new Token("d");
 Token tok2 = new Token("d");
 assertTrue(tok1.equals(tok2));
 Token tok3 = new Token("days");
 assertFalse(tok1.equals(tok3));
 Token tok4 = new Token("d", 2);
 Token tok5 = new Token("d", 2);
 assertTrue(tok4.equals(tok5));
 Token tok6 = new Token("d", 3);
 assertFalse(tok4.equals(tok6));
}

 @Test
 void testHashCode() {
 Token tok1 = new Token("d");
 Token tok2 = new Token("d");
 assertEquals(tok1.hashCode(), tok2.hashCode());
 Token tok3 = new Token("days");
 assertNotEquals(tok1.hashCode(), tok3.hashCode());
 Token tok4 = new Token("d", 2);
 Token tok5 = new Token("d", 2);
 assertEquals(tok4.hashCode(), tok5.hashCode());
 Token tok6 = new Token("d", 3);
 assertNotEquals(tok4.hashCode(), tok6.hashCode());
}

 @Test
 void testToString() {
 Token tok1 = new Token("d");
 assertEquals("d", tok1.toString());
 Token tok2 = new Token("days");
 assertEquals("days", tok2.toString());
 Token tok3 = new Token("d", 2);
 assertEquals("dd", tok3.toString());
}
}