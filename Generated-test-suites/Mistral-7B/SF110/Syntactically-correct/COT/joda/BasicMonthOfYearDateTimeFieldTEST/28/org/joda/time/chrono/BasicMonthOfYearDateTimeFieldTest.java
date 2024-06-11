import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.JulianChronology;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField testField;
 private Chronology julianChronology;

 @Before
 public void setUp() {
 testField = new BasicMonthOfYearDateTimeField(new JulianChronology(), DateTimeConstants.JANUARY);
 julianChronology = new JulianChronology();
 }

 @Test
 public void testIsLenient() {
 assertFalse(testField.isLenient());
 }

 @Test
 public void testGet() {
 MutableDateTime date = new MutableDateTime();
 date.setChronology(julianChronology);
 date.setYear(2022);
 date.setMonthOfYear(1);
 date.setDayOfMonth(31);
 long input = date.getMillis();
 int expected = DateTimeConstants.JANUARY;
 int actual = testField.get(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd() {
 MutableDateTime date = new MutableDateTime();
 date.setChronology(julianChronology);
 date.setYear(2022);
 date.setMonthOfYear(1);
 date.setDayOfMonth(31);
 long input = date.getMillis();
 long expected = 32145600000L; // January 31, 2100
 long actual = testField.add(input, 11);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithLargeMagnitude() {
 MutableDateTime date = new MutableDateTime();
 date.setChronology(julianChronology);
 date.setYear(2022);
 date.setMonthOfYear(1);
 date.setDayOfMonth(31);
 long input = date.getMillis();
 long actual = testField.add(input, Integer.MAX_VALUE + 1);
 }

 @Test
 public void testAddWrapField() {
 MutableDateTime date = new MutableDateTime();
 date.setChronology(julianChronology);
 date.setYear(2022);
 date.setMonthOfYear(13);
 long input = date.getMillis();
 long expected = 131232000000L; // March 1, 1900
 long actual = testField.addWrapField(input, 2);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDifferenceAsLong() {
 MutableDateTime date1 = new MutableDateTime();
 date1.setChronology(julianChronology);
 date1.setYear(2022);
 date1.setMonthOfYear(1);
 date1.setDayOfMonth(31);
 long input1 = date1.getMillis();
 MutableDateTime date2 = new MutableDateTime();
 date2.setChronology(julianChronology);
 date2.setYear(2023);
 date2.setMonthOfYear(2);
 date2.setDayOfMonth(28);
 long input2 = date2.getMillis();
 long expected = 51536000000L; // February 28, 2023
 long actual = testField.getDifferenceAsLong(input1, input2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSet() {
 MutableDateTime date = new MutableDateTime();
 date.setChronology(julianChronology);
 date.setYear(2022);
 date.setMonthOfYear(1);
 date.setDayOfMonth(31);
 long input = date.getMillis();
 int expected = DateTimeConstants.MARCH;
 int actual = testField.set(input, 2);
 MutableDateTime result = new MutableDateTime(testField.getMillis(input));
 date.setYear(expected);
 date.setMonthOfYear(1);
 long expectedMillis = date.getMillis();
 assertEquals(expectedMillis, testField.getMillis(actual));
 }

 @Test
 public void testAddWithReadablePartial() {
 MutableDateTime date1 = new MutableDateTime();
 date1.setChronology(julianChronology);
 date1.setYear(2022);
 date1.setMonthOfYear(1);
 date1.setDayOfMonth(31);
 long input1 = date1.getMillis();
 MutableDateTime date2 = new MutableDateTime();
 date2.setChronology(julianChronology);
 date2.setYear(2023);
 date2.setMonthOfYear(DateTimeConstants.APRIL);
 date2.setDayOfMonth(1);
 long input2 = date2.getMillis();
 int[] expected = { DateTimeConstants.MAY, 1 };
 int[] actual = testField.add(new BaseDateTime(input1), 1, null, 1);
 assertEquals(expected[0], actual[0]);
 assertEquals(expected[1], actual[1]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithInvalidReadablePartial() {
 MutableDateTime date1 = new MutableDateTime();
 date1.setChronology(julianChronology);
 date1.setYear(2022);
 date1.setMonthOfYear(1);
 date1.setDayOfMonth(31);
 long input1 = date1.getMillis();
 MutableDateTime date2 = new MutableDateTime();
 date2.setChronology(julianChronology);
 date2.setYear(2023);
 date2.setMonthOfYear(DateTimeConstants.FEBRUARY);
 date2.setDayOfMonth(31); // invalid value for this chronology
 long input2 = date2.getMillis();
 int[] array = new int[2];
 testField.add(new BaseDateTime(input1), 1, array, 1);
 }
}