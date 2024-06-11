import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalDate;
import org.joda.time.MutableLocalTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.Months;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_8601_DATE = DateTimeConstants.ISO_8601_DATE;

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_DATE, DateTimeConstants.FEBRUARY);
 assertEquals(1, field.getLeapAmount(new BaseDateTime(2024, 2, DateTimeConstants.FEBRUARY_29).getMillis()));
 assertEquals(0, field.getLeapAmount(new BaseDateTime(2024, 2, DateTimeConstants.FEBRUARY_28).getMillis()));
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_DATE, DateTimeConstants.FEBRUARY);
 assertTrue(field.isLeap(new BaseDateTime(2024, 2, DateTimeConstants.FEBRUARY_29).getMillis()));
 assertFalse(field.isLeap(new BaseDateTime(2024, 2, DateTimeConstants.FEBRUARY_28).getMillis()));
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_DATE, DateTimeConstants.JANUARY);
 assertEquals(1, field.get(new BaseDateTime(2021, 1, 1).getMillis()));
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_DATE, DateTimeConstants.JANUARY);
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDate(2021, 1, 1);
 MutableLocalDateTime localDateTime = new MutableLocalDateTime(dateTime);
 localDateTime.setField(DateTimeFieldType.hourOfDay(), 13);
 localDateTime.setField(DateTimeFieldType.minuteOfHour(), 30);
 MutableLocalDate localDate = new MutableLocalDate(dateTime);
 localDate.setDayOfMonth(31);
 long initialMilliseconds = localDateTime.toMillis();

 long newMilliseconds = field.add(initialMilliseconds, 2);
 LocalDateTime newLocalDateTime = new LocalDateTime(newMilliseconds);
 LocalDate newLocalDate = newLocalDate(newMilliseconds);
 LocalTime newLocalTime = newLocalTime(newLocalDateTime);

 assertEquals(2, field.get(newLocalDateTime.toDate().getTime()));
 assertEquals(3, field.get(newLocalDate.toDate().getTime()));
 assertEquals(1, field.get(newLocalTime.toDate().getTime()));

 assertTrue(newLocalDateTime.getDayOfMonth() > 31);
 assertEquals(1, newLocalDateTime.getDayOfMonth());
 assertEquals(2, newLocalDate.getMonthOfYear());
 assertEquals(1, newLocalTime.getHourOfDay());
 assertEquals(30, newLocalTime.getMinuteOfHour());

 long addOneYear = field.getMillisPerDay() * 365 + (ISO_8601_DATE.isLeapYear(2022) ? 1 : 0);
 long newMilliseconds2 = field.add(newMilliseconds, addOneYear);
 localDateTime = new LocalDateTime(newMilliseconds2);
 localDate = new LocalDate(newMilliseconds2);
 localTime = new LocalTime(localDateTime.toDate());

 assertEquals(3, field.get(newLocalDateTime.toDate().getTime()));
 assertEquals(2, field.get(newLocalDate.toDate().getTime()));
 assertEquals(1, field.get(newLocalTime.toDate().getTime()));
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_DATE, DateTimeConstants.JANUARY);
 long initialMilliseconds = new DateTime(2021, 0, 1, 0, 0).getMillis();
 long newMilliseconds = field.addWrapField(initialMilliseconds, 13 * 12 + 1);
 assertEquals(13, field.get(newMilliseconds));
 }

 @Test
 public void testAddWithNegativeValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_DATE, DateTimeConstants.JANUARY);
 long initialMilliseconds = new DateTime(2021, 0, 1, 0, 0).getMillis();
 long newMilliseconds = field.add(initialMilliseconds, -13);
 assertEquals(11, field.get(newMilliseconds));
 }

 @Test
 public void testAddWithInvalidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601_DATE, DateTimeConstants.JANUARY);
 long initialMilliseconds = new DateTime(2021, 0, 1, 0, 0).getMillis();
 try {
 field.add(initialMilliseconds, 32);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 field.add(new Object(), 3, new int[0], 2);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetDifferenceAsLong() {
 MutableDateTime dateTime1 = new MutableDateTime();
 MutableDateTime dateTime2 = new MutableDateTime();
 dateTime1.setDate(2021, 1, 1);
 dateTime2.setDate(2022, 1, 31);
 long differenceAsLong = field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis());
 assertEquals(31 * field.getMillisPerDay(), differenceAsLong);
 }

 @Test
 public void testSet() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDate(2021, 1, 1);
 assertEquals(1, field.get(dateTime.getMillis()));

 MutableLocalDateTime localDateTime = new MutableLocalDateTime(dateTime);
 localDateTime.setField(DateTimeFieldType.monthOfYear(), 2);
 dateTime.setLocalDateTime(localDateTime);
 assertEquals(2, field.get(dateTime.getMillis()));
 }

 @Test
 public void testSetWithInvalidValue() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDate(2021, 1, 1);
 try {
 field.set(dateTime.getMillis(), 13 * 12 + 13);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}