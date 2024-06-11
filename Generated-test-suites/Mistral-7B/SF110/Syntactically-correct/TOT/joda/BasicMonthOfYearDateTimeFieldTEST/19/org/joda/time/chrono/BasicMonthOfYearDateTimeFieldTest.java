import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_DATE = DateTimeUtils.getISOCalendar();

 @Test
 public void testGetMethod() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_DATE, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime(2022, 1, 31);

 assertEquals("Expected the month of the test date to be 1.", 1, testField.get(testDate.getMillis()));
 }

 @Test
 public void testAddMethodWithValidArguments() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_DATE, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime(2022, 1, 31);
 MutableDateTime newTestDate = new MutableDateTime(testDate.getMillis());

 newTestDate.add(DateTimeFieldType.monthOfYear(), 2);

 assertEquals("Expected the month of the new test date to be 3.", 3, testField.get(newTestDate.getMillis()));
 }

 @Test
 public void testAddMethodWithNegativeArguments() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_DATE, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime(2022, 1, 31);
 MutableDateTime newTestDate = new MutableDateTime(testDate.getMillis());

 newTestDate.add(DateTimeFieldType.monthOfYear(), -2);

 assertEquals("Expected the month of the new test date to be 11.", 11, testField.get(newTestDate.getMillis()));
 }

 @Test
 public void testAddMethodWithInvalidArguments() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_DATE, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime(2022, 1, 31);
 MutableDateTime newTestDate = new MutableDateTime(testDate.getMillis());

 try {
 newTestDate.add(DateTimeFieldType.monthOfYear(), Integer.MAX_VALUE);
 fail("Expected IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 newTestDate.add(DateTimeFieldType.monthOfYear(), Integer.MIN_VALUE);
 fail("Expected IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddWrapFieldMethod() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_DATE, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime(2022, 1, 31);
 MutableDateTime newTestDate = new MutableDateTime(testDate.getMillis());

 newTestDate.addWrapField(Integer.MAX_VALUE);

 assertEquals("Expected the month of the new test date to be 1.", 1, testField.get(newTestDate.getMillis()));
 }

 @Test
 public void testGetDifferenceAsLongMethod() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_DATE, DateTimeConstants.JANUARY);
 MutableDateTime testDate1 = new MutableDateTime(2022, 1, 31);
 MutableDateTime testDate2 = new MutableDateTime(2022, 2, 1);

 assertEquals("Expected the difference between the two test dates to be 1.", 1, testField.getDifferenceAsLong(testDate1.getMillis(), testDate2.getMillis()));
 }

 @Test
 public void testSetMethod() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_DATE, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime(2022, 1, 31); MutableDateTime newTestDate = new MutableDateTime(testDate.getMillis());

 newTestDate.set(DateTimeFieldType.monthOfYear(), 3);

 assertEquals("Expected the month of the new test date to be 3.", 3, testField.get(newTestDate.getMillis()));
 }

 @Test
 public void testIsLeapMethod() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, DateTimeConstants.MARCH);
 Instant testDate = new Instant(2024, 3, 1, 0, 0, 0, 0);
 Instant newTestDate = new Instant(2024, 3, 1, 0, 0, 0, 1);

 assertTrue("Expected the new test date to be a leap date.", testField.isLeap(testDate.getMillis()));
 assertFalse("Expected the new test date to not be a leap date.", testField.isLeap(newTestDate.getMillis()));
 }
}