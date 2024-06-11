import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthField;
import org.joda.time.field.WeekFields;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology IS0 = new ISOChronology();

 @Test
 public void testBasicInstances() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12); // December
 assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
 assertEquals(IS0, field.getChronology());
 assertEquals(12, field.getMaximumValue());
 assertFalse(field.isLenient());
 }

 @Test
 public void testMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 LocalDateTime dateTime = new LocalDateTime(2012, 11, 30, 12, 0, 0);
 assertEquals(11, field.get(dateTime.getMillis()));
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 LocalDateTime dateTime = new LocalDateTime(2012, 11, 30, 12, 0, 0);
 long newMillis = field.add(dateTime.getMillis(), 1);
 LocalDateTime newDateTime = new LocalDateTime(newMillis);
 assertEquals(12, newDateTime.getMonthOfYear());

 LocalDateTime dateTime2 = new LocalDateTime(2012, 11, 31, 12, 0, 0);
 long newMillis2 = field.add(dateTime2.getMillis(), 2);
 LocalDateTime newDateTime2 = new LocalDateTime(newMillis2);
 assertEquals(1, newDateTime2.getMonthOfYear());
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 long newMillis = field.addWrapField(Long.MIN_VALUE, 100);
 LocalDateTime newDateTime = new LocalDateTime(newMillis);
 assertEquals(13, newDateTime.getMonthOfYear());
 }

 @Test
 public void testAddNotContiguous() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 LocalDateTime dateTime = new LocalDateTime(2012, 11, 30, 12, 0, 0);
 LocalDateTime dateTime2 = new LocalDateTime(2013, 1, 1, 12, 0, 0);
 long[] newMillis = field.add(IS0.getBaseInstance(), 0, new long[] { dateTime.getMillis(), dateTime2.getMillis() }, 2);
 LocalDateTime newDateTime = new LocalDateTime(newMillis[0]);
 LocalDateTime newDateTime2 = new LocalDateTime(newMillis[1]);
 assertEquals(13, newDateTime.getMonthOfYear());
 assertEquals(1, newDateTime2.getMonthOfYear());
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 LocalDateTime dateTime1 = new LocalDateTime(2012, 11, 30, 12, 0, 0);
 LocalDateTime dateTime2 = new LocalDateTime(2013, 1, 1, 12, 0, 0);
 long difference = field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis());
 assertEquals(1, difference);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 long newMillis = field.set(Long.MIN_VALUE, 3);
 LocalDateTime newDateTime = new LocalDateTime(newMillis);
 assertEquals(3, newDateTime.getMonthOfYear());
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 2); // February
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 29, 12, 0, 0);
 assertTrue(field.isLeap(dateTime.getMillis()));
 LocalDateTime dateTime2 = new LocalDateTime(2012, 2, 28, 12, 0, 0);
 assertFalse(field.isLeap(dateTime2.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 2); // February
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 29, 12, 0, 0);
 assertEquals(1, field.getLeapAmount(dateTime.getMillis()));
 LocalDateTime dateTime2 = new LocalDateTime(2012, 2, 28, 12, 0, 0);
 assertEquals(0, field.getLeapAmount(dateTime2.getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 2); // February
 assertEquals(WeekFields.ISO.weeks(), field.getLeapDurationField());
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 LocalDateTime dateTime = new LocalDateTime(2012, 11, 30, 12, 0, 0);
 long roundedMillis = field.roundFloor(dateTime.getMillis());
 LocalDateTime roundedDateTime = new LocalDateTime(roundedMillis);
 assertEquals(11, roundedDateTime.getMonthOfYear());
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 LocalDateTime dateTime = new LocalDateTime(2012, 12, 31, 12, 0, 0);
 long remainderMillis = field.remainder(dateTime.getMillis());
 LocalDateTime remainderDateTime = new LocalDateTime(remainderMillis);
 assertEquals(0, remainderDateTime.getMonthOfYear());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNegativeMagnitude() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(IS0, 12);
 LocalDateTime dateTime = new LocalDateTime(2012, 11, 30, 12, 0, 0);
 field.add(dateTime.getMillis(), -2);
 fail("Expected IllegalArgumentException");
 }
}