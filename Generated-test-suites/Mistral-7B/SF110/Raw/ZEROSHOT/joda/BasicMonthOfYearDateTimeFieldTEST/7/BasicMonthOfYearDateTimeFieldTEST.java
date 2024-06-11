import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.MonthOfYearField;
import org.joda.time.DurationFieldType;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_CHRONOLOGY = ISOChronology.getInstanceUTCOffset();

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 BaseDateTime date = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 long month = field.get(date.getMillis());
 assertEquals(1, month);
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 BaseDateTime date = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 long newMillis = field.add(date.getMillis(), 3);
 BaseDateTime newDate = new BaseDateTime(newMillis, ISO_CHRONOLOGY);
 assertEquals(DateTimeConstants.APRIL, newDate.getMonthOfYear());
 }

 @Test
 public void testAddWithNegativeValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 BaseDateTime date = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 long newMillis = field.add(date.getMillis(), -3);
 BaseDateTime newDate = new BaseDateTime(newMillis, ISO_CHRONOLOGY);
 assertEquals(DateTimeConstants.NOVEMBER, newDate.getMonthOfYear());
 }

 @Test
 public void testAddWithBigValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 BaseDateTime date = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 long newMillis = field.add(date.getMillis(), 123456789);
 BaseDateTime newDate = new BaseDateTime(newMillis, ISO_CHRONOLOGY);
 assertEquals(DateTimeConstants.JANUARY, newDate.getMonthOfYear()); // The value should wrap around
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 long millis = 1420070400000L; // January 1, 2015
 long wrappedValue = 13;
 long newMillis = field.addWrapField(millis, wrappedValue);
 BaseDateTime newDate = new BaseDateTime(newMillis, ISO_CHRONOLOGY);
 assertEquals(DateTimeConstants.DECEMBER, newDate.getMonthOfYear());
 }

 @Test
 public void testGetDifferenceAsLong() {
 BaseDateTime date1 = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 BaseDateTime date2 = new BaseDateTime(1420135200000L, ISO_CHRONOLOGY);
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 long difference = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
 assertEquals(2, difference);
 }

 @Test
 public void testSet() {
 BaseDateTime date = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 long newMillis = field.set(date.getMillis(), 4);
 BaseDateTime newDate = new BaseDateTime(newMillis, ISO_CHRONOLOGY);
 assertEquals(DateTimeConstants.APRIL, newDate.getMonthOfYear());
 }

 @Test
 public void testIsLeap() {
 BaseDateTime date = new BaseDateTime(1423548800000L, ISO_CHRONOLOGY); // February 29, 2012
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 assertTrue(field.isLeap(date.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BaseDateTime date = new BaseDateTime(1423548800000L, ISO_CHRONOLOGY); // February 29, 2012
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 assertEquals(1, field.getLeapAmount(date.getMillis()));
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 BaseDateTime date = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 long roundedMillis = field.roundFloor(date.getMillis());
 BaseDateTime roundedDate = new BaseDateTime(roundedMillis, ISO_CHRONOLOGY);
 assertEquals(DateTimeConstants.JANUARY, roundedDate.getMonthOfYear());
 }

 @Test
 public void testRemainder() {
 BaseDateTime date = new BaseDateTime(1420070400000L, ISO_CHRONOLOGY);
 BaseDateTime roundedDate = new BaseDateTime(field.roundFloor(date.getMillis()), ISO_CHRONOLOGY);
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 long remainder = field.remainder(date.getMillis());
 assertEquals(DateTimeFieldType.months().getMillisPerDay() * (DateTimeConstants.APRIL - DateTimeConstants.JANUARY), remainder);
 }

}