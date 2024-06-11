import org.joda.time.*;
import org.joda.time.base.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParseString() {
 MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getDays());
 }

 @Test
 public void testParseStringWithFormat() {
 PeriodFormatter format = ISOPeriodFormat.standard();
 MutablePeriod period = MutablePeriod.parse("P1Y2M3D", format);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getDays());
 }

 @Test
 public void testConstructors() {
 // Default constructor
 MutablePeriod defaultPeriod = new MutablePeriod();
 assertEquals(0, defaultPeriod.getYears());
 assertEquals(0, defaultPeriod.getMonths());
 assertEquals(0, defaultPeriod.getWeeks());
 assertEquals(0, defaultPeriod.getDays());
 assertEquals(0, defaultPeriod.getHours());
 assertEquals(0, defaultPeriod.getMinutes());
 assertEquals(0, defaultPeriod.getSeconds());
 assertEquals(0, defaultPeriod.getMillis());

 // Construct with PeriodType
 MutablePeriod periodTypeConstruct = new MutablePeriod(PeriodType.standard());
 assertEquals(0, periodTypeConstruct.getYears());
 assertEquals(0, periodTypeConstruct.getMonths());
 assertEquals(0, periodTypeConstruct.getWeeks());
 assertEquals(0, periodTypeConstruct.getDays());
 assertEquals(0, periodTypeConstruct.getHours());
 assertEquals(0, periodTypeConstruct.getMinutes());
 assertEquals(0, periodTypeConstruct.getSeconds());
 assertEquals(0, periodTypeConstruct.getMillis());

 // Construct with ints
 MutablePeriod intConstruct = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(1, intConstruct.getYears());
 assertEquals(2, intConstruct.getMonths());
 assertEquals(3, intConstruct.getWeeks());
 assertEquals(4, intConstruct.getDays());
 assertEquals(5, intConstruct.getHours());
 assertEquals(6, intConstruct.getMinutes());
 assertEquals(7, intConstruct.getSeconds());
 assertEquals(8, intConstruct.getMillis());
 }

 @Test
 public void testParseLong() {
 MutablePeriod period = new MutablePeriod(123456789L);
 assertEquals(1, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(351, period.getHours());
 assertEquals(18, period.getMinutes());
 assertEquals(53, period.getSeconds());
 assertEquals(699000, period.getMillis());
 }

 @Test
 public void testClear() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.clear();
 assertEquals(0, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testSetValue() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setValue(0, 1);
 assertEquals(1, period.getMonths());
 assertEquals(0, period.getYears());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetValueNegative() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setValue(-1, 1);
 }

 @Test (expected = ArrayIndexOutOfBoundsException.class)
 public void testSetValueOutOfBounds() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setValue(13, 1);
 }

 @Test
 public void testSetField() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setField(DurationFieldType.years(), 1);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetFieldNegative() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setField(DurationFieldType.years(), -1);
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 ReadablePeriod readablePeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8);
 period.setPeriod(readablePeriod);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test (expected = NullPointerException.class)
 public void testSetPeriodNull() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setPeriod(null);
 }

 @Test
 public void testSetPeriodWithInvalidChronology() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 ReadablePeriod readablePeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUtils.getCzechDate().getCalendar().getTimeZone());
 period.setPeriod(readablePeriod);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithSameChronology() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 ReadablePeriod readablePeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUtils.UTC);
 period.setPeriod(readablePeriod);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithDifferentChronology() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 ReadablePeriod readablePeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUtils.getCzechDate().getCalendar().getTimeZone());
 period.setPeriod(readablePeriod, DateTimeUtils.UTC);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithNegativeMilliseconds() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 ReadablePeriod readablePeriod = new Period(-1, 0, 0, 0, 0, 0, 0, 0, DateTimeUtils.UTC);
 period.setPeriod(readablePeriod);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(-8, period.getMillis());
 }

 @Test
 public void testAddPeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period1.add(period2);
 assertEquals(2, period1.getYears());
 assertEquals(4, period1.getMonths());
 assertEquals(6, period1.getWeeks());
 assertEquals(8, period1.getDays());
 assertEquals(11, period1.getHours());
 assertEquals(12, period1.getMinutes());
 assertEquals(12, period1.getSeconds());
 assertEquals(12, period1.getMillis());
 }

 @Test
 public void testAddPeriodWithNegativeMillis() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period2.setMillis(-12);
 period1.add(period2);
 assertEquals(1, period1.getYears());
 assertEquals(2, period1.getMonths());
 assertEquals(3, period1.getWeeks());
 assertEquals(4, period1.getDays());
 assertEquals(11, period1.getHours());
 assertEquals(12, period1.getMinutes());
 assertEquals(11, period1.getSeconds());
 assertEquals(111800000, period1.getMillis());
 }

 @Test
 public void testAddPeriodWithDifferentChronology() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUtils.UTC);
 MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUtils.getCzechDate().getCalendar().getTimeZone());
 period1.add(period2);
 assertEquals(2, period1.getYears());
 assertEquals(4, period1.getMonths());
 assertEquals(6, period1.getWeeks());
 assertEquals(8, period1.getDays());
 assertEquals(11, period1.getHours());
 assertEquals(12, period1.getMinutes());
 assertEquals(12, period1.getSeconds());
 assertEquals(12, period1.getMillis());
 }

 @Test
 public void testMergePeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period1.mergePeriod(period2);
 assertEquals(2, period1.getYears());
 assertEquals(4, period1.getMonths());
 assertEquals(6, period1.getWeeks());
 assertEquals(8, period1.getDays());
 assertEquals(11, period1.getHours());
 assertEquals(12, period1.getMinutes());
 assertEquals(12, period1.getSeconds());
 assertEquals(12, period1.getMillis());
 }

 @Test
 public void testEquals() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertTrue(period1.equals(period2));
 }

 @Test
 public void testNotEquals() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 9);
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testHashCode() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(period1.hashCode(), period2.hashCode());
 }

 @Test
 public void testToString() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals("P1Y2M3D", period.toString());
 }

}