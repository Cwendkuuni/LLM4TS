import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final int DAY_IN_MILLIS = 86400000;
 private static final int MIN_VALUE = 1;
 private static final int MAX_VALUE = 12;
 private static final int LEAP_MONTH = 2;
 private static final int LEAP_YEAR = 2000;
 private static final Chronology CHRONOLOGY = new JulianChronology();
 private static final BasicMonthOfYearDateTimeField TESTEE = new BasicMonthOfYearDateTimeField(CHRONOLOGY, LEAP_MONTH);

 @Test
 public void testIsLenient() {
 assertFalse(TESTEE.isLenient());
 }

 @Test
 public void testGet() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 assertEquals(3, TESTEE.get(date.getMillis()));
 }

 @Test
 public void testAdd() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 long result = TESTEE.add(date.getMillis(), 1);
 MutableDateTime resultDateTime = new MutableDateTime(result);
 assertEquals(4, resultDateTime.getMonthOfYear());

 result = TESTEE.add(date.getMillis(), -1);
 resultDateTime = new MutableDateTime(result);
 assertEquals(3, resultDateTime.getMonthOfYear());
 }

 @Test
 public void testAddLong() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 long result = TESTEE.add(date.getMillis(), 2);
 MutableDateTime resultDateTime = new MutableDateTime(result);
 assertEquals(5, resultDateTime.getMonthOfYear());

 result = TESTEE.add(date.getMillis(), -2);
 resultDateTime = new MutableDateTime(result);
 assertEquals(3, resultDateTime.getMonthOfYear());
 }

 @Test
 public void testAddReadablePartial() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 MutableDateTime addDate = new MutableDateTime();
 addDate.setYear(2022);
 addDate.setMonthOfYear(4, 30);
 addDate.setDayOfMonth(30);

 Period period = new Period(2, 0, 0);
 ReadablePartial readablePartial = new PeriodFieldSet(DateTimeFieldType.months(), DateTimeFieldType.days(), period);

 long[] result = TESTEE.add(readablePartial, 1, new long[3], 0);
 MutableDateTime resultDateTime = new MutableDateTime(result[0]);
 assertEquals(5, resultDateTime.getMonthOfYear());
 assertEquals(1, resultDateTime.getDayOfMonth());
 assertEquals(0, resultDateTime.getYear());
 }

 @Test
 public void testAddWrapField() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 long result = TESTEE.addWrapField(date.getMillis(), 13);
 MutableDateTime resultDateTime = new MutableDateTime(result);
 assertEquals(5, resultDateTime.getMonthOfYear());
 }

 @Test
 public void testGetDifferenceAsLong() {
 MutableDateTime date1 = new MutableDateTime();
 date1.setYear(2022);
 date1.setMonthOfYear(3, 31);
 date1.setDayOfMonth(31);
 MutableDateTime date2 = new MutableDateTime();
 date2.setYear(2023);
 date2.setMonthOfYear(1, 31);
 date2.setDayOfMonth(31);

 long result = TESTEE.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
 assertEquals(365, result);
 }

 @Test
 public void testSet() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 long result = TESTEE.set(date.getMillis(), 5);
 MutableDateTime resultDateTime = new MutableDateTime(result);
 assertEquals(5, resultDateTime.getMonthOfYear());
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField result = TESTEE.getRangeDurationField();
 assertEquals(DurationFieldType.YEARS, result.getType());
 }

 @Test
 public void testIsLeap() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(LEAP_YEAR);
 date.setMonthOfYear(LEAP_MONTH, 29);
 date.setDayOfMonth(29);

 assertTrue(TESTEE.isLeap(date.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(LEAP_YEAR);
 date.setMonthOfYear(LEAP_MONTH, 29);
 date.setDayOfMonth(29);

 assertEquals(1, TESTEE.getLeapAmount(date.getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField result = TESTEE.getLeapDurationField();
 assertEquals(DurationFieldType.DAYS, result.getType());
 }

 @Test
 public void testGetMinimumValue() {
 assertEquals(MIN_VALUE, TESTEE.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 assertEquals(MAX_VALUE, TESTEE.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 long result = TESTEE.roundFloor(date.getMillis());
 assertEquals(TESTEE.iChronology.getYearMonthMillis(2022, 3), result);
 }

 @Test
 public void testRemainder() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 long result = TESTEE.remainder(date.getMillis());
 assertEquals(0, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidArgument() {
 MutableDateTime date = new MutableDateTime();
 date.setYear(2022);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(31);

 TESTEE.add(date.getMillis(), Long.MAX_VALUE);
 }
}