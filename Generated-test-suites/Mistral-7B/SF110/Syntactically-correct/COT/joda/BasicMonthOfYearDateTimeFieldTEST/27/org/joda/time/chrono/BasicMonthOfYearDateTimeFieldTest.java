import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import static org.junit.Assert.*;

import java.util.Arrays;

public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField field;

 @Before
 public void setUp() {
 GregorianChronology chronology = new GregorianChronology();
 field = new BasicMonthOfYearDateTimeField(chronology, 2); // February is the second leap month
 }

 @Test
 public void testIsLenient() {
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 long date = new GregorianDate(2022, 1, 1).getMillis();
 int expected = 1;
 int actual = field.get(date);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWithValidValues() {
 long date = new GregorianDate(2022, 1, 1).getMillis();
 long expected = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 long actual = field.add(date, 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWithNegativeValues() {
 long date = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 long expected = new GregorianDate(2022, 1, 1).getMillis();
 long actual = field.add(date, -1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWithZero() {
 long date = new GregorianDate(2022, 1, 1).getMillis();
 long expected = date;
 long actual = field.add(date, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWithLargeValues() {
 long date = new GregorianDate(2022, 1, 1).getMillis();
 long expected = new GregorianDate(2100, 1, 1).getMillis();
 long actual = field.add(date, Integer.MAX_VALUE);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNegativeLargeValues() {
 long date = new GregorianDate(2100, 1, 1).getMillis();
 long actual = field.add(date, Integer.MIN_VALUE);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testAddWrapField() {
 long date = new GregorianDate(2022, 1, 31).getMillis(); // 31st of January is not a valid date
 long expected = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 long actual = field.addWrapField(date, 12);
 assertEquals(expected, actual);
 }

 @Test
 public void testSet() {
 long date = new GregorianDate(2022, 1, 1).getMillis();
 int month = 2;
 long expected = new GregorianDate(2022, month, 28).getMillis(); // 28th of February in a leap year
 long actual = field.set(date, month);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithInvalidValue() {
 long date = new GregorianDate(2022, 1, 1).getMillis();
 int month = 13;
 field.set(date, month);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField expected = DurationFields.years();
 DurationField actual = field.getRangeDurationField();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLeap() {
 long date = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 assertTrue(field.isLeap(date));
 }

 @Test
 public void testGetLeapAmount() {
 long date = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 int expected = 1;
 int actual = field.getLeapAmount(date);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField expected = DurationFields.days();
 DurationField actual = field.getLeapDurationField();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMinimumValue() {
 int expected = 1;
 int actual = field.getMinimumValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaximumValue() {
 int expected = 12;
 int actual = field.getMaximumValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testRoundFloor() {
 long date = new GregorianDate(2022, 1, 31).getMillis(); // 31st of January is not a valid date
 long expected = new GregorianDate(2022, 1, 1).getMillis();
 long actual = field.roundFloor(date);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemainder() {
 long date = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 long expected = 0;
 long actual = field.remainder(date);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDifferenceAsLongWithValidValues() {
 long date1 = new GregorianDate(2022, 1, 1).getMillis();
 long date2 = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 long expected = 59;
 long actual = field.getDifferenceAsLong(date1, date2);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDifferenceAsLongWithInvalidOrder() {
 long date1 = new GregorianDate(2022, 2, 28).getMillis(); // 28th of February in a leap year
 long date2 = new GregorianDate(2022, 1, 1).getMillis();
 long expected = -59;
 long actual = field.getDifferenceAsLong(date2, date1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWithReadablePartial() {
 ReadablePartial readablePartial = new DateTimeFieldsMutable().parse("P1Y1M1D-1"); // One year, one month, one day and one second before the current date
 long expected = new DateTime(System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 365L * 1L + 1000L * 60L * 60L * 24L * 30L * 12L + 1000L * 60L * 60L * 24L * 28L + 1000L * 60L * 60L * 24L * 27L * 1L));
 long actual = field.add(new DateTime(System.currentTimeMillis()), readablePartial, null, 1).getMillis();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWithReadablePartialWithNegativeValues() {
 ReadablePartial readablePartial = new DateTimeFieldsMutable().parse("P1Y1M1D-1"); // One year, one month, one day and one second before the current date
 long expected = new DateTime(System.currentTimeMillis() + (1000L * 60L * 60L * 24L * 365L * 1L + 1000L * 60L * 60L * 24L * 30L * 12L + 1000L * 60L * 60L * 24L * 28L + 1000L * 60L * 60L * 24L * 27L * 1L));
 long actual = field.add(new DateTime(System.currentTimeMillis()), readablePartial, null, -1).getMillis();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWithReadablePartialWithInvalidFieldType() {
 ReadablePartial readablePartial = new DateTimeFieldsMutable().appendFields(new DateTimeFields("P1Y1M1D"), new DateTimeFieldType("InvalidFieldType"));
 long expected = new DateTime(System.currentTimeMillis()).getMillis();
 long actual = field.add(new DateTime(System.currentTimeMillis()), readablePartial, null, 1).getMillis();
 assertEquals(expected, actual);
 }

}