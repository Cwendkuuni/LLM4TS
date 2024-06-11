import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class MutablePeriodTest {

 @Test
 public void testParseString() {
 MutablePeriod mutablePeriod = MutablePeriod.parse("P1D");
 assertEquals(1, mutablePeriod.getDays());
 }

 @Test
 public void testParseStringWithFormatter() {
 PeriodFormatter formatter = ISOPeriodFormat.standard();
 MutablePeriod mutablePeriod = MutablePeriod.parse("P1D", formatter);
 assertEquals(1, mutablePeriod.getDays());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStringWithInvalidFormat() {
 assertThrows(IllegalArgumentException.class, () -> MutablePeriod.parse("INVALID_FORMAT"));
 }

 @Test
 public void testConstructorWithValues() {
 MutablePeriod mutablePeriod = new MutablePeriod(5, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(5, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(3, mutablePeriod.getWeeks());
 assertEquals(4, mutablePeriod.getDays());
 assertEquals(5, mutablePeriod.getHours());
 assertEquals(6, mutablePeriod.getMinutes());
 assertEquals(7, mutablePeriod.getSeconds());
 assertEquals(8, mutablePeriod.getMillis());
 }

 @Test
 public void testConstructorWithValuesAndPeriodType() {
 PeriodType periodType = PeriodType.standard();
 MutablePeriod mutablePeriod = new MutablePeriod(5, 2, 3, 4, 5, 6, 7, 8, periodType);
 assertEquals(5, mutablePeriod.getYears(PeriodType.YEAR_INDEX));
 assertEquals(2, mutablePeriod.getMonths(PeriodType.MONTH_INDEX));
 assertEquals(3, mutablePeriod.getWeeks(PeriodType.WEEK_INDEX));
 assertEquals(4, mutablePeriod.getDays(PeriodType.DAY_INDEX));
 assertEquals(5, mutablePeriod.getHours(PeriodType.HOUR_INDEX));
 assertEquals(6, mutablePeriod.getMinutes(PeriodType.MINUTE_INDEX));
 assertEquals(7, mutablePeriod.getSeconds(PeriodType.SECOND_INDEX));
 assertEquals(8, mutablePeriod.getMillis(PeriodType.MILLI_INDEX));
 }

 @Test
 public void testConstructorWithPeriod() {
 MutablePeriod sourcePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod mutablePeriod = new MutablePeriod(sourcePeriod);
 assertEquals(sourcePeriod.getYears(), mutablePeriod.getYears());
 assertEquals(sourcePeriod.getMonths(), mutablePeriod.getMonths());
 assertEquals(sourcePeriod.getWeeks(), mutablePeriod.getWeeks());
 assertEquals(sourcePeriod.getDays(), mutablePeriod.getDays());
 assertEquals(sourcePeriod.getHours(), mutablePeriod.getHours());
 assertEquals(sourcePeriod.getMinutes(), mutablePeriod.getMinutes());
 assertEquals(sourcePeriod.getSeconds(), mutablePeriod.getSeconds());
 assertEquals(sourcePeriod.getMillis(), mutablePeriod.getMillis());
 }

 @Test
 public void testClone() {
 MutablePeriod sourcePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod mutablePeriod = sourcePeriod.clone();
 assertEquals(sourcePeriod.getYears(), mutablePeriod.getYears());
 assertEquals(sourcePeriod.getMonths(), mutablePeriod.getMonths());
 assertEquals(sourcePeriod.getWeeks(), mutablePeriod.getWeeks());
 assertEquals(sourcePeriod.getDays(), mutablePeriod.getDays());
 assertEquals(sourcePeriod.getHours(), mutablePeriod.getHours());
 assertEquals(sourcePeriod.getMinutes(), mutablePeriod.getMinutes());
 assertEquals(sourcePeriod.getSeconds(), mutablePeriod.getSeconds());
 assertEquals(sourcePeriod.getMillis(), mutablePeriod.getMillis());
 }

 @Test
 public void testClear() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.clear();
 assertNull(mutablePeriod.getYears());
 assertNull(mutablePeriod.getMonths());
 assertNull(mutablePeriod.getWeeks());
 assertNull(mutablePeriod.getDays());
 assertNull(mutablePeriod.getHours());
 assertNull(mutablePeriod.getMinutes());
 assertNull(mutablePeriod.getSeconds());
 assertNull(mutablePeriod.getMillis());
 }

 @Test
 public void testSetValue() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.setValue(0, 1);
 assertEquals(1, mutablePeriod.getValues()[0]);
 }

 @Test
 public void testSetField() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.setField(DurationFieldType.years(), 1);
 assertEquals(1, mutablePeriod.getYears());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod sourcePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.setPeriod(sourcePeriod);
 assertEquals(sourcePeriod.getYears(), mutablePeriod.getYears());
 assertEquals(sourcePeriod.getMonths(), mutablePeriod.getMonths());
 assertEquals(sourcePeriod.getWeeks(), mutablePeriod.getWeeks());
 assertEquals(sourcePeriod.getDays(), mutablePeriod.getDays());
 assertEquals(sourcePeriod.getHours(), mutablePeriod.getHours());
 assertEquals(sourcePeriod.getMinutes(), mutablePeriod.getMinutes());
 assertEquals(sourcePeriod.getSeconds(), mutablePeriod.getSeconds());
 assertEquals(sourcePeriod.getMillis(), mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartAndEndMillis() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 long startMillis = 1611155200000L; // 2021-02-01 00:00:00.000Z
 long endMillis = 1611168800000L; // 2021-02-01 05:00:00.000Z
 mutablePeriod.setPeriod(startMillis, endMillis);
 assertEquals(1, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(0, mutablePeriod.getDays());
 assertEquals(5, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithChronology() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 long startMillis = 1611155200000L; // 2021-02-01 00:00:00.000Z
 Chronology chronology = DateTimeUtils.forUTC();
 mutablePeriod.setPeriod(startMillis, endMillis, chronology);
 assertEquals(1, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(0, mutablePeriod.getDays());
 assertEquals(5, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithDurationMillis() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 long durationMillis = 3600000; // 1 hour
 mutablePeriod.setPeriod(durationMillis);
 assertEquals(0, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(0, mutablePeriod.getDays());
 assertEquals(1, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithChronologyAndDurationMillis() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 long durationMillis = 3600000; // 1 hour
 Chronology chronology = DateTimeUtils.forUTC();
 mutablePeriod.setPeriod(durationMillis, chronology);
 assertEquals(0, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(0, mutablePeriod.getDays());
 assertEquals(1, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithLong() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.setPeriod(1L);
 assertEquals(0, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(0, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithLongAndChronology() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 Chronology chronology = DateTimeUtils.forUTC();
 mutablePeriod.setPeriod(1L, chronology);
 assertEquals(0, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(0, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testAddField() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 mutablePeriod.addField(DurationFieldType.days(), 10);
 assertEquals(10, mutablePeriod.getDays());
 }

 @Test
 public void testAddPeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 MutablePeriod addend = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);
 mutablePeriod.addPeriod(addend);
 assertEquals(2, mutablePeriod.getYears());
 assertEquals(3, mutablePeriod.getMonths());
 assertEquals(4, mutablePeriod.getWeeks());
 assertEquals(5, mutablePeriod.getDays());
 assertEquals(6, mutablePeriod.getHours());
 assertEquals(7, mutablePeriod.getMinutes());
 assertEquals(8, mutablePeriod.getSeconds());
 assertEquals(9, mutablePeriod.getMillis());
 }

 @Test
 public void testAddPeriodWithChronology() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 MutablePeriod addend = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);
 mutablePeriod.addPeriod(addend, DateTimeUtils.forUTC());
 assertEquals(2, mutablePeriod.getYears());
 assertEquals(3, mutablePeriod.getMonths());
 assertEquals(4, mutablePeriod.getWeeks());
 assertEquals(5, mutablePeriod.getDays());
 assertEquals(6, mutablePeriod.getHours());
 assertEquals(7, mutablePeriod.getMinutes());
 assertEquals(8, mutablePeriod.getSeconds());
 assertEquals(9, mutablePeriod.getMillis());
 }

 @Test
 public void testAddPeriodWithReadablePeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 ReadablePeriod addend = new Period(2, 3, 4, 5, 6, 7, 8, 9);
 mutablePeriod.addPeriod(addend);
 assertEquals(2, mutablePeriod.getYears());
 assertEquals(3, mutablePeriod.getMonths());
 assertEquals(4, mutablePeriod.getWeeks());
 assertEquals(5, mutablePeriod.getDays());
 assertEquals(6, mutablePeriod.getHours());
 assertEquals(7, mutablePeriod.getMinutes());
 assertEquals(8, mutablePeriod.getSeconds());
 assertEquals(9, mutablePeriod.getMillis());
 }

 @Test
 public void testAddPeriodWithLong() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 mutablePeriod.addPeriod(1L);
 assertEquals(1, mutablePeriod.getDays());
 }

 @Test
 public void testAddPeriodWithLongAndChronology() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 Chronology chronology = DateTimeUtils.forUTC();
 mutablePeriod.addPeriod(1L, chronology);
 assertEquals(1, mutablePeriod.getDays());
 }

 @Test
 public void testMergePeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod addend = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.mergePeriod(addend);
 assertEquals(8, mutablePeriod.getYears());
 assertEquals(4, mutablePeriod.getMonths());
 assertEquals(7, mutablePeriod.getWeeks());
 assertEquals(8, mutablePeriod.getDays());
 assertEquals(11, mutablePeriod.getHours());
 assertEquals(6, mutablePeriod.getMinutes());
 assertEquals(6, mutablePeriod.getSeconds());
 assertEquals(8, mutablePeriod.getMillis());
 }

 @Test(expected = ArithmeticException.class)
 public void testMergePeriodWithNegativePeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod subtrahend = new MutablePeriod(-1, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.mergePeriod(subtrahend);
 }

 @Test
 public void testGetValue() {
 MutablePeriod mutablePeriod = new MutablePeriod(7, 2, 3, 4, 5, 6, 7, 8);
 int expectedValue = mutablePeriod.getValues()[0];
 int actualValue = mutablePeriod.getValue(0);
 assertEquals(expectedValue, actualValue);
 }
}