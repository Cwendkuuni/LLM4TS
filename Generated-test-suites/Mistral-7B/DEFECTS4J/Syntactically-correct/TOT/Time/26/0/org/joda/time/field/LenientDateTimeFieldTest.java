import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Period;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.field.ZonedDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology DATE_TIME_UTC = DateTimeUtils.UTC;
 private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();
 private static final DateTimeFieldType MINUTE_OF_HOUR = DateTimeFieldType.minuteOfHour();
 private static final DateTimeFieldType SECOND_OF_MINUTE = DateTimeFieldType.secondOfMinute();
 private static final DateTimeFieldType MILLISECOND_OF_SECOND = DateTimeFieldType.millisecondOfSecond();

 @Test
 public void testLenientDateTimeField_getSetValue_withinBounds() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(HOUR_OF_DAY, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 2023, 1, 1, 12, 0, 0, 0, DATE_TIME_UTC);
 long currentValue = dateTime.get(HOUR_OF_DAY);
 long newValue = 13;
 long expectedValue = newValue;

 // set value within bounds
 long setValue = field.set(dateTime.getLong(), newValue);
 assertEquals(expectedValue, setValue);
 // get value after setting
 long getValue = dateTime.get(HOUR_OF_DAY);
 assertEquals(expectedValue, getValue);
 }

 @Test
 public void testLenientDateTimeField_getSetValue_outOfBounds_positive() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(HOUR_OF_DAY, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 2023, 1, 1, 12, 0, 0, 0, DATE_TIME_UTC);
 long currentValue = dateTime.get(HOUR_OF_DAY);
 long newValue = 25;
 long expectedValue = newValue % 24; // 25 hours is equivalent to 1 hour in next day

 // set value out of bounds
 long setValue = field.set(dateTime.getLong(), newValue);
 assertEquals(expectedValue, setValue);
 // get value after setting
 long getValue = dateTime.get(HOUR_OF_DAY);
 assertEquals(expectedValue, getValue);
 }

 @Test
 public void testLenientDateTimeField_getSetValue_outOfBounds_negative() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(HOUR_OF_DAY, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 2023, 1, 1, 12, 0, 0, 0, DATE_TIME_UTC);
 long currentValue = dateTime.get(HOUR_OF_DAY);
 long newValue = -3;
 long expectedValue = (23 - newValue + 24) % 24; // -3 hours is equivalent to 21 hours in previous day

 // set value out of bounds
 long setValue = field.set(dateTime.getLong(), newValue);
 assertEquals(expectedValue, setValue);
 // get value after setting
 long getValue = dateTime.get(HOUR_OF_DAY);
 assertEquals(expectedValue, getValue);
 }

 @Test
 public void testLenientDateTimeField_setInvalidValue() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(HOUR_OF_DAY, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 2023, 1, 1, 12, 0, 0, 0, DATE_TIME_UTC);
 long invalidValue = 25 * 24 + 1; // 601 hours, which is more than a year

 // try to set an invalid value
 long setValue = field.set(dateTime.getLong(), invalidValue);
 assertNotEquals(invalidValue, setValue);
 // get value after setting, it should be unchanged
 long getValue = dateTime.get(HOUR_OF_DAY);
 assertEquals(12, getValue);
 }

 @Test
 public void testLenientDateTimeField_setInvalidValue_negative() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(HOUR_OF_DAY, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 2023, 1, 1, 12, 0, 0, 0, DATE_TIME_UTC);
 long invalidValue = -601; // -601 hours, which is more than a year in the past

 // try to set an invalid value
 long setValue = field.set(dateTime.getLong(), invalidValue);
 assertNotEquals(invalidValue, setValue);
 // get value after setting, it should be unchanged
 long getValue = dateTime.get(HOUR_OF_DAY);
 assertEquals(12, getValue);
 }

 @Test
 public void testLenientDateTimeField_getSetValue_withChronology() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(DAY_OF_MONTH, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 2023, 1, 1, 0, 0, 0, 0, DATE_TIME_UTC);
 LocalDate date = new LocalDate(DATE_TIME_UTC);
 long currentValue = dateTime.get(DAY_OF_MONTH);
 long newValue = 32;
 long expectedValue = newValue;

 // set value with chronology
 dateTime.setChronology(DateTimeUtils.forDate(date.plusWeeks(2).toDate()));
 long setValue = field.set(dateTime.getLong(), newValue);
 assertEquals(expectedValue, setValue);
 // get value after setting
 long getValue = dateTime.get(DAY_OF_MONTH);
 assertEquals(expectedValue, getValue);
 }

 @Test
 public void testLenientDateTimeField_getSetValue_withPeriod() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(MINUTE_OF_HOUR, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 2023, 1, 1, 0, 0, 0, 0, DATE_TIME_UTC);
 Period period = new Period(3, 30);
 long currentValue = dateTime.get(MINUTE_OF_HOUR);
 long newValue = currentValue + period.getMinutes();
 long expectedValue = newValue;

 // set value with period
 dateTime.add(period);
 long setValue = field.set(dateTime.getLong(), newValue);
 assertEquals(expectedValue, setValue);
 // get value after setting
 long getValue = dateTime.get(MINUTE_OF_HOUR);
 assertEquals(expectedValue, getValue);
 }

 @Test
 public void testLenientDateTimeField_getSetValue_withInstant() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(SECOND_OF_MINUTE, DATE_TIME_UTC), DATE_TIME_UTC);
 Instant instant = new Instant(0L);
 MutableDateTime dateTime = new MutableDateTime(instant.getMillis(), DATE_TIME_UTC);
 long currentValue = dateTime.get(SECOND_OF_MINUTE);
 long newValue = currentValue + 30;
 long expectedValue = newValue;

 // set value with Instant
 long setValue = field.set(dateTime.getLong(), newValue);
 assertEquals(expectedValue, setValue);
 // get value after setting
 long getValue = dateTime.get(SECOND_OF_MINUTE);
 assertEquals(expectedValue, getValue);
 }

 @Test
 public void testLenientDateTimeField_getSetValue_withLocalDateTime() {
 DateTimeField field = LenientDateTimeField.getInstance(new ZonedDateTimeField(SECOND_OF_MINUTE, DATE_TIME_UTC), DATE_TIME_UTC);
 MutableLocalDateTime localDateTime = new MutableLocalDateTime();
 LocalDateTime dateTime = new LocalDateTime(DATE_TIME_UTC);
 long currentValue = localDateTime.get(SECOND_OF_MINUTE);
 long newValue = currentValue + 30;
 long expectedValue = newValue;

 // set value with LocalDateTime
 localDateTime.set(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute(), 0, DATE_TIME_UTC);
 long setValue = field.set(localDateTime.getLong(), newValue);
 assertEquals(expectedValue, setValue);
 // get value after setting
 long getValue = localDateTime.getSecondOfMinute();
 assertEquals(expectedValue, getValue);
 }
}