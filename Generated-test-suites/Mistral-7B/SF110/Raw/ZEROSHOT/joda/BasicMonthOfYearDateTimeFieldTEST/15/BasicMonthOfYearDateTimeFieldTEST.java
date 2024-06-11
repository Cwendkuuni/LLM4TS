import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private BaseChronology isoChronology;
 private BasicMonthOfYearDateTimeField monthOfYearField;

 @Before
 public void setup() {
 isoChronology = new ISOChronology();
 monthOfYearField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 }

 @Test
 public void testGet() {
 long testValue = LocalDate.parse("2015-02-28".split("-")[0], isoChronology.getLocale()).getMillis();
 int expected = 2;
 int actual = monthOfYearField.get(testValue);
 assertEquals("Expected month to be 2 for given date, but was " + actual, expected, actual);
 }

 @Test
 public void testAdd() {
 long testValue = LocalDate.parse("2015-02-28".split("-")[0], isoChronology.getLocale()).getMillis();
 long expected = LocalDate.parse("2015-03-31".split("-")[0], isoChronology.getLocale()).getMillis();
 long actual = monthOfYearField.add(testValue, 1);
 assertEquals("Expected result of adding one month to be " + expected + " but was " + actual, expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddTooBig() {
 long testValue = LocalDate.parse("2015-02-28".split("-")[0], isoChronology.getLocale()).getMillis();
 monthOfYearField.add(testValue, 12 * 12 + 1);
 }

 @Test
 public void testAddNegative() {
 long testValue = LocalDate.parse("2015-03-31".split("-")[0], isoChronology.getLocale()).getMillis();
 long expected = LocalDate.parse("2015-02-28".split("-")[0], isoChronology.getLocale()).getMillis();
 long actual = monthOfYearField.add(testValue, -1);
 assertEquals("Expected result of subtracting one month to be " + expected + " but was " + actual, expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNegativeTooBig() {
 long testValue = LocalDate.parse("2015-03-31".split("-")[0], isoChronology.getLocale()).getMillis();
 monthOfYearField.add(testValue, -(12 * 12 + 1));
 }

 @Test
 public void testIsLeap() {
 long testValue = LocalDate.parse("2016-02-29".split("-")[0], isoChronology.getLocale()).getMillis();
 assertTrue("Expected February 29, 2016 to be leap year", monthOfYearField.isLeap(testValue));
 }

 @Test
 public void testIsNotLeap() {
 long testValue = LocalDate.parse("2015-02-28".split("-")[0], isoChronology.getLocale()).getMillis();
 assertFalse("Expected February 28, 2015 to not be leap year", monthOfYearField.isLeap(testValue));
 }
}