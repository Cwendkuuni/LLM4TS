import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.TestingChronology;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology TEST_CHRONOLOGY = new TestingChronology();;

 @Test
 public void testGetAndSetMonthOfYear() {
     // Create a test DateTime using the TestingChronology
     BaseDateTime testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2020, 1, 1, 0, 0, 0);

     // Test getting the month of the year
     int initialMonth = testDateTime.getMonthOfYear();
     assertEquals(1, initialMonth);

     // Test setting the month of the year
     testDateTime = testDateTime.withFieldAdded(DateTimeFieldType.monthOfYear(), 2);
     int setMonth = testDateTime.getMonthOfYear();
     assertEquals(2, setMonth);

     // Test setting the month of the year out of bounds
     testDateTime = testDateTime.withFieldAdded(DateTimeFieldType.monthOfYear(), 13);
     setMonth = testDateTime.getMonthOfYear();
     assertEquals(1, setMonth); // Should wrap around to January
 }

 @Test
 public void testAddMonthOfYear() {
     // Create a test DateTime using the TestingChronology
     BaseDateTime testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2020, 1, 1, 0, 0, 0);

     // Test adding one month
     testDateTime = testDateTime.plusMonths(1);
     int resultMonth = testDateTime.getMonthOfYear();
     assertEquals(2, resultMonth);

     // Test adding a negative number of months
     testDateTime = testDateTime.minusMonths(1);
     resultMonth = testDateTime.getMonthOfYear();
     assertEquals(1, resultMonth);

     // Test adding a large number of months
     testDateTime = testDateTime.plusMonths(12);
     resultMonth = testDateTime.getMonthOfYear();
     assertEquals(13, resultMonth); // Should wrap around to March
 }

 @Test
 public void testAddMonthsToDateTimeWithLeapYear() {
     // Create a test DateTime using the TestingChronology
     BaseDateTime testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2020, 2, 1, 0, 0, 0); // February 1, 2020

     // Test adding 2 months, which will result in a leap year
     testDateTime = testDateTime.plusMonths(2);
     int resultMonth = testDateTime.getMonthOfYear();
     assertEquals(4, resultMonth); // March 1, 2020

     // Test adding an additional month, which will also result in a leap year
     testDateTime = testDateTime.plusMonths(1);
     resultMonth = testDateTime.getMonthOfYear();
     assertEquals(5, resultMonth); // April 1, 2020
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMonthsOutOfBounds() {
     // Create a test DateTime using the TestingChronology
     BaseDateTime testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2020, 1, 1, 0, 0, 0);

     // Test adding an invalid number of months
     testDateTime = testDateTime.plusMonths(365 * 2 + 1); // Adding 2 years + 1 day
     try {
         testDateTime = testDateTime.plusMonths(1);
     } catch (IllegalArgumentException e) {
         Assert.assertEquals("Magnitude of add amount is too large: 1", e.getMessage());
     }
 }

 @Test
 public void testGetLeapAmount() {
     // Create a test DateTime using the TestingChronology
     BaseDateTime testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2020, 2, 1, 0, 0, 0); // February 1, 2020

     // Test getting the leap amount
     int leapAmount = testDateTime.getLeapAmount();
     assertEquals(0, leapAmount);

     // Test getting the leap amount for a leap year
     testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2021, 2, 1, 0, 0, 0); // February 1, 2021
     leapAmount = testDateTime.getLeapAmount();
     assertEquals(1, leapAmount);
 }

 @Test
 public void testGetLeapDurationField() {
     // Create a test DateTime using the TestingChronology
     BaseDateTime testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2020, 2, 1, 0, 0, 0); // February 1, 2020

     // Test getting the leap duration field
     DurationField leapDurationField = testDateTime.getLeapDurationField();
     assertEquals(org.joda.time.DurationFieldType.day(), leapDurationField.getFieldType());
     assertEquals(1, leapDurationField.get(testDateTime));
 }

 @Test
 public void testIsLeap() {
     // Create a test DateTime using the TestingChronology
     BaseDateTime testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2020, 2, 1, 0, 0, 0); // February 1, 2020

     // Test checking for a leap year
     boolean isLeap = testDateTime.isLeap(testDateTime);
     assertTrue(isLeap);

     // Test checking for a non-leap year
     testDateTime = new BaseDateTime(TEST_CHRONOLOGY, 2021, 2, 1, 0, 0, 0); // February 1, 2021
     isLeap = testDateTime.isLeap(testDateTime);
     assertFalse(isLeap);
 }
}