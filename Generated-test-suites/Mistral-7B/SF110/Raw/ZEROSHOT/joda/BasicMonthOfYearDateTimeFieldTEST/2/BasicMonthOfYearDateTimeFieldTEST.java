import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 @Test
 public void testBasicMonthOfYearDateTimeFieldConstruction() {
 BasicChronology gregorianChronology = new GregorianChronology();
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);
 assertEquals("Expected chronology to match", gregorianChronology, field.getChronology());
 assertEquals("Expected leap month to match", 2, field.getLeapMonth());
 }

 @Test
 public void testGetIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 assertFalse("Expected lenient to be false", field.isLenient());
 }

 @Test
 public void testGetIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 DateTime base = new DateTime(2016, 3, 1, 0, 0);
 DateTime test = new DateTime(2016, 2, 29, 0, 0);
 assertTrue("Expected February 29, 2016 to be a leap year", field.isLeap(test.getMillis()));
 test = new DateTime(2016, 3, 1, 0, 0);
 assertFalse("Expected March 1, 2016 to not be a leap year", field.isLeap(test.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 DateTime base = new DateTime(2016, 3, 1, 0, 0);
 DateTime test = new DateTime(2016, 2, 29, 0, 0);
 assertEquals("Expected leap amount to be 1 for February 29, 2016", 1, field.getLeapAmount(test.getMillis()));
 test = new DateTime(2016, 3, 1, 0, 0);
 assertEquals("Expected leap amount to be 0 for March 1, 2016", 0, field.getLeapAmount(test.getMillis()));
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 assertEquals("Expected minimum value to be 1", 1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 assertEquals("Expected maximum value to be 12", 12, field.getMaximumValue());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 DateTime test = new DateTime(2016, 2, 1, 0, 0);
 assertEquals("Expected month of year to match for February 1, 2016", 2, field.get(test.getMillis()));
 test = new DateTime(2016, 3, 1, 0, 0);
 assertEquals("Expected month of year to match for March 1, 2016", 3, field.get(test.getMillis()));
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 DateTime base = new DateTime(2016, 3, 1, 0, 0);
 DateTime test;
 long expected;

 // Add 1 month
 test = field.add(base.getMillis(), 1);
 expected = new DateTime(2016, 4, 1, 0, 0).getMillis();
 assertEquals("Expected result of adding 1 month to March 1, 2016 to be April 1, 2016", expected, test.getMillis());

 // Add 12 months
 expected = new DateTime(2017, 3, 1, 0, 0).getMillis();
 test = field.add(base.getMillis(), 12);
 assertEquals("Expected result of adding 12 months to March 1, 2016 to be March 1, 2017", expected, test.getMillis());

 // Add negative number of months
 expected = new DateTime(2016, 1, 1, 0, 0).getMillis();
 test = field.add(base.getMillis(), -1);
 assertEquals("Expected result of adding -1 month to March 1, 2016 to be February 1, 2016", expected, test.getMillis());

 // Add 13 months
 expected = new DateTime(2017, 4, 1, 0, 0).getMillis();
 test = field.add(base.getMillis(), 13);
 assertEquals("Expected result of adding 13 months to March 1, 2016 to be April 1, 2017", expected, test.getMillis());
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 DateTime test = new DateTime(2020, 12, 31, 0, 0); // December 31, 2020
 long expected = new DateTime(2021, 1, 1, 0, 0).getMillis(); // January 1, 2021
 long testMillis = field.addWrapField(test.getMillis(), 1);
 assertEquals("Expected result of adding 1 month to December 31, 2020 to be January 1, 2021", expected, testMillis);
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 DurationField durationField = field.getRangeDurationField();
 assertEquals("Expected range duration field to be years", DurationFieldType.years(), durationField.getType());
 }

 @Test
 public void testAddReadablePartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
 ReadablePartial readablePartial = new DateTime(2016, 2, 1, 0, 0).toLocalDate().toDateTimeAtStartOfDay().getLocalDate().toReadablePartial();
 long[] expected = {3, 1, 0, 0, 0, 0, 0};
 int[] actual = field.add(readablePartial, 1, new long[7], 0).getValues();
 assertArrayEquals("Expected result of adding 1 month to February 1, 2016 to be March 1, 2016", expected, actual);
 }
}