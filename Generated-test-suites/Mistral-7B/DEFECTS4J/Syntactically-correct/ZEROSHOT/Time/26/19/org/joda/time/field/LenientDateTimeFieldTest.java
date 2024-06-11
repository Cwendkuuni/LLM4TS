package org.joda.time.field.test;

 import org.joda.time.BaseDateTime;
 import org.joda.time.DateTimeField;
 import org.joda.time.DateTimeFieldType;
 import org.joda.time.Chronology;
 import org.joda.time.LocalDateTime;
 import org.joda.time.LocalDate;
 import org.joda.time.LocalTime;
 import org.joda.time.format.DateTimeFormat;
 import org.joda.time.field.LenientDateTimeField;
 import org.joda.time.field.StrictDateTimeField;
 import org.junit.Test;
 import static org.junit.Assert.*;

 /**
 * Test for LenientDateTimeField class.
 */
 public class LenientDateTimeFieldTest {

 private static final DateTimeField DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private static final DateTimeField YEAR = DateTimeFieldType.year();
 private static final DateTimeField MONTH_OF_YEAR = DateTimeFieldType.monthOfYear();
 private static final DateTimeField HOUR_OF_DAY = DateTimeFieldType.hourOfDay();
 private static final DateTimeField MINUTE_OF_HOUR = DateTimeFieldType.minuteOfHour();
 private static final DateTimeField SECOND_OF_MINUTE = DateTimeFieldType.secondOfMinute();

 @Test
 public void testLenientDateTimeField() {
 // Test constructor
 DateTimeField field = DAY_OF_MONTH;
 Chronology base = BaseDateTime.UTC;
 LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
 assertEquals(LenientDateTimeField.class, lenientField.getClass());
 assertEquals(field, lenientField.getDelegate());
 assertEquals(base, lenientField.getChronology());

 // Test isLenient method
 assertTrue(lenientField.isLenient());

 // Test set method with valid value
 BaseDateTime testDateTime = new BaseDateTime(1530120000000L, base);
 long newValue = lenientField.get(testDateTime.toLocalDateTime().toDate());
 lenientField.set(testDateTime.toLocalDateTime().toDate(), newValue);
 assertEquals(newValue, lenientField.get(testDateTime.toLocalDateTime().toDate()));

 // Test set method with valid value out of bounds
 BaseDateTime testDateTimeOutOfBounds = new BaseDateTime(1530120000000L + 1000L * 60 * 60 * 24 * 30 * 12, base);
 long newValueOutOfBounds = lenientField.get(testDateTimeOutOfBounds.toLocalDateTime().toDate()) - 30;
 lenientField.set(testDateTimeOutOfBounds.toLocalDateTime().toDate(), newValueOutOfBounds);
 assertEquals(newValueOutOfBounds, lenientField.get(testDateTimeOutOfBounds.toLocalDateTime().toDate()));

 // Test set method with invalid value
 BaseDateTime testDateTimeInvalidValue = new BaseDateTime(1530120000000L - 1000L * 60 * 60 * 24 * 30 * 12, base);
 long newValueInvalidValue = lenientField.get(testDateTimeInvalidValue.toLocalDateTime().toDate()) + 31;
 lenientField.set(testDateTimeInvalidValue.toLocalDateTime().toDate(), newValueInvalidValue);
 assertEquals(lenientField.get(testDateTimeInvalidValue.toLocalDateTime().toDate()), newValueInvalidValue);

 // Test set method with negative value
 BaseDateTime testDateTimeNegativeValue = new BaseDateTime(1530120000000L - 1000L * 60 * 60 * 24 * 30 * 12, base);
 long newValueNegativeValue = lenientField.get(testDateTimeNegativeValue.toLocalDateTime().toDate()) - 1;
 lenientField.set(testDateTimeNegativeValue.toLocalDateTime().toDate(), newValueNegativeValue);
 assertEquals(newValueNegativeValue, lenientField.get(testDateTimeNegativeValue.toLocalDateTime().toDate()));

 // Test set method with value greater than max value
 BaseDateTime testDateTimeMaxValue = new BaseDateTime(Long.MAX_VALUE, base);
 long newValueMaxValue = lenientField.get(testDateTimeMaxValue.toLocalDateTime().toDate()) + Long.MAX_VALUE / 2;
 lenientField.set(testDateTimeMaxValue.toLocalDateTime().toDate(), newValueMaxValue);
 assertEquals(newValueMaxValue, lenientField.get(testDateTimeMaxValue.toLocalDateTime().toDate()));

 // Test set method with long value
 BaseDateTime testDateTimeLongValue = new BaseDateTime(Long.MAX_VALUE + 1, base);
 long newValueLongValue = lenientField.get(testDateTimeLongValue.toLocalDateTime().toDate()) + Long.MAX_VALUE / 2 + 1;
 lenientField.set(testDateTimeLongValue.toLocalDateTime().toDate(), newValueLongValue);
 assertEquals(newValueLongValue, lenientField.get(testDateTimeLongValue.toLocalDateTime().toDate()));

 // Test set method with LocalDateTime
 LocalDateTime localDateTime = new LocalDateTime(2012, 12, 31, 23, 59, 59, 0, base);
 long newValueLocalDateTime = lenientField.get(localDateTime.toDate()) - 1;
 lenientField.set(localDateTime, newValueLocalDateTime);
 assertEquals(newValueLocalDateTime, lenientField.get(localDateTime));

 // Test set method with LocalDate
 LocalDate localDate = new LocalDate(2012, 12, 31, base);
 long newValueLocalDate = lenientField.get(localDate.toDate()) - 1;
 lenientField.set(localDate, newValueLocalDate);
 assertEquals(newValueLocalDate, lenientField.get(localDate));

 // Test set method with LocalTime
 LocalTime localTime = new LocalTime(23, 59, 59, base);
 long newValueLocalTime = lenientField.get(localTime.toDate()) - 1;
 lenientField.set(localTime, newValueLocalTime);
 assertEquals(newValueLocalTime, lenientField.get(localTime));
 }
}