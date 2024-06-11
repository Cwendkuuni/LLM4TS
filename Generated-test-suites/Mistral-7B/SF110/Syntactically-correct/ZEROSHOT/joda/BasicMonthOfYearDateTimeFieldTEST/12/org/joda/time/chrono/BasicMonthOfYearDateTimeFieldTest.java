import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalDate;
import org.joda.time.YearMonth;
import org.joda.time.base.BasicChronology;
import org.joda.time.base.JulianDays;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_8601_CHRONOLOGY = new BasicGregorianChronology();

 @Test
 public void testBasicMonthOfYearDateTimeFieldCreation() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(ISO_8601_CHRONOLOGY, field.getChronology());
 assertEquals(false, field.isLenient());
 assertEquals(1, field.getMinimumValue());
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testGetMethod() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 1, 1), new LocalTime());
 int month = dateTime.getMonthOfYear();
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 int fieldValue = field.get(dateTime.getMillis());
 assertEquals(month, fieldValue);
 }

 @Test
 public void testAddMethod() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 1, 1), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 long newMillis = field.add(dateTime.getMillis(), 2);
 LocalDateTime newDateTime = new LocalDateTime(new Instant(newMillis));
 assertEquals(newDateTime.getMonthOfYear(), 3);
 }

 @Test
 public void testAddWrapFieldMethod() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 12, 1), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.DECEMBER);
 long newMillis = field.addWrapField(dateTime.getMillis(), 12);
 LocalDateTime newDateTime = new LocalDateTime(new Instant(newMillis));
 assertEquals(newDateTime.getMonthOfYear(), 1);
 }

 @Test
 public void testAddWithReadablePartial() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 1, 1), new LocalTime());
 IntField yearField = FieldUtils.wrap(DateTimeFieldType.year(), 2023);
 IntField monthField = FieldUtils.wrap(DateTimeFieldType.monthOfYear(), 3);
 ReadablePartial partial = new GeneralBaseDateTimeFieldSet(yearField, monthField);
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 long newMillis = field.add(dateTime.getMillis(), partial);
 LocalDateTime newDateTime = new LocalDateTime(new Instant(newMillis));
 assertEquals(newDateTime.getMonthOfYear(), 3);
 }

 @Test
 public void testAddWithLongValue() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 1, 1), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 long newMillis = field.add(dateTime.getMillis(), 12);
 LocalDateTime newDateTime = new LocalDateTime(new Instant(newMillis));
 assertEquals(newDateTime.getMonthOfYear(), 3);
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 DurationField durationField = field.getRangeDurationField();
 assertEquals(DurationFieldType.years(), durationField.getFieldType());
 }

 @Test
 public void testIsLeap() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2023, 2, 1), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 boolean isLeap = field.isLeap(dateTime.getMillis());
 assertTrue(isLeap);
 }

 @Test
 public void testGetLeapAmount() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 2, 1), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 int leapAmount = field.getLeapAmount(dateTime.getMillis());
 assertEquals(leapAmount, 0);
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 DurationField durationField = field.getLeapDurationField();
 assertEquals(DurationFieldType.days(), durationField.getFieldType());
 }

 @Test
 public void testGetDifferenceAsLong() {
 MutableLocalDateTime dateTime1 = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 1, 1), new LocalTime());
 MutableLocalDateTime dateTime2 = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 1, 15), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 long difference = field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis());
 assertEquals(13L, difference);
 }

 @Test
 public void testSetMethod() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 1, 1), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.JANUARY);
 long newMillis = field.set(dateTime.getMillis(), 3);
 LocalDateTime newDateTime = new LocalDateTime(new Instant(newMillis));
 assertEquals(newDateTime.getMonthOfYear(), 3);
 }

 @Test
 public void testAddWrapFieldWithNegativeValue() {
 MutableLocalDateTime dateTime = new MutableLocalDateTime(ISO_8601_CHRONOLOGY, new LocalDate(2022, 12, 1), new LocalTime());
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_CHRONOLOGY, DateTimeConstants.DECEMBER);
 long newMillis = field.addWrapField(dateTime.getMillis(), -12);
 LocalDateTime newDateTime = new LocalDateTime(new Instant(newMillis));
 assertEquals(newDateTime.getMonthOfYear(), 11);
 }
}