import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.base.Defaults;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParseString() {
 String input = "P1Y2M3D4H5M6S750MS";
 MutablePeriod period = MutablePeriod.parse(input);
 assertEquals("Year: 1, Month: 2, Week: 0, DayOfMonth: 3, Hour: 4, Minute: 5, Second: 6, Millis: 750", period.toString());
 }

 @Test
 public void testParseStringWithCustomFormatter() {
 String input = "PT1H2M3S";
 PeriodFormatter formatter = ISOPeriodFormat.standard().withParsedMillisOffset(60 * 60 * 1000);
 MutablePeriod period = MutablePeriod.parse(input, formatter);
 assertEquals("Hour: 1, Minute: 2, Second: 3", period.toString());
 }

 @Test
 public void testEmptyConstructor() {
 MutablePeriod period = new MutablePeriod();
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
 public void testConstructorWithPeriodType() {
 MutablePeriod period = new MutablePeriod(PeriodType.dayTime());
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
 public void testConstructorWithValues() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
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
 public void testConstructorWithChronology() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUtils.UTC);
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
 public void testConstructorWithValuesAndChronology() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUtils.UTC, PeriodType.dayTime());
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
 public void testSetValues() {
 MutablePeriod period = new MutablePeriod();
 period.setValues(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
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
 public void testSetValue() {
 MutablePeriod period = new MutablePeriod();
 period.setValue(PeriodFieldType.years(), 1);
 assertEquals(1, period.getYears());
 }

 @Test
 public void testSetField() {
 MutablePeriod period = new MutablePeriod();
 period.setField(DurationFieldType.years(), 1);
 assertEquals(1, period.getYears());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 MutablePeriod period2 = new MutablePeriod();
 period2.setPeriod(period1);
 assertEquals(1, period2.getYears());
 assertEquals(2, period2.getMonths());
 assertEquals(3, period2.getWeeks());
 assertEquals(4, period2.getDays());
 assertEquals(5, period2.getHours());
 assertEquals(6, period2.getMinutes());
 assertEquals(7, period2.getSeconds());
 assertEquals(8, period2.getMillis());
 }

 @Test
 public void testSetPeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(null);
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
 public void testAddFields() {
 MutablePeriod period = new MutablePeriod();
 period.addField(DurationFieldType.years(), 1);
 period.addField(DurationFieldType.months(), 2);
 period.addField(DurationFieldType.days(), 3);
 period.addField(DurationFieldType.hours(), 4);
 period.addField(DurationFieldType.minutes(), 5);
 period.addField(DurationFieldType.seconds(), 6);
 period.addField(DurationFieldType.millis(), 750);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getDays());
 assertEquals(4, period.getHours());
 assertEquals(5, period.getMinutes());
 assertEquals(6, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testAddPeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 MutablePeriod period2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 MutablePeriod period3 = new MutablePeriod();
 period3.addPeriod(period1);
 period3.addPeriod(period2);
 assertEquals(3, period3.getYears());
 assertEquals(5, period3.getMonths());
 assertEquals(7, period3.getWeeks());
 assertEquals(11, period3.getDays());
 assertEquals(11, period3.getHours());
 assertEquals(12, period3.getMinutes());
 assertEquals(13, period3.getSeconds());
 assertEquals(17, period3.getMillis());
 }

 @Test
 public void testAddPeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 MutablePeriod period2 = null;
 period.addPeriod(period2);
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
 public void testAddPeriodWithSameInstance() {
 MutablePeriod period = new MutablePeriod();
 period.addPeriod(period);
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
 public void testClear() {
 MutablePeriod period = new MutablePeriod();
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
 public void testClone() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 MutablePeriod clonedPeriod = period.copy();
 assertEquals(1, clonedPeriod.getYears());
 assertEquals(2, clonedPeriod.getMonths());
 assertEquals(3, clonedPeriod.getWeeks());
 assertEquals(4, clonedPeriod.getDays());
 assertEquals(5, clonedPeriod.getHours());
 assertEquals(6, clonedPeriod.getMinutes());
 assertEquals(7, clonedPeriod.getSeconds());
 assertEquals(8, clonedPeriod.getMillis());
 }
}