import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.base.Defaults;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParsingString() {
 MutablePeriod period = MutablePeriod.parse("P3Y2M1D");
 assertEquals(3, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(1, period.getDays());
 }

 @Test
 public void testParsingStringWithFormatter() {
 PeriodFormatter formatter = ISOPeriodFormat.standard();
 MutablePeriod period = MutablePeriod.parse("P3Y2M1D", formatter);
 assertEquals(3, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(1, period.getDays());
 }

 @Test
 public void testCreatingMutablePeriodWithDefaults() {
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
 public void testCreatingMutablePeriodWithValues() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
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
 public void testCreatingMutablePeriodWithValuesAndPeriodType() {
 PeriodType periodType = PeriodType.micro();
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, periodType);
 assertEquals(1, period.getYears(), periodType.getYears());
 assertEquals(2, period.getMonths(), periodType.getMonths());
 assertEquals(3, period.getWeeks(), periodType.getWeeks());
 assertEquals(4, period.getDays(), periodType.getDays());
 assertEquals(5, period.getHours(), periodType.getHours());
 assertEquals(6, period.getMinutes(), periodType.getMinutes());
 assertEquals(7, period.getSeconds(), periodType.getSeconds());
 assertEquals(8, period.getMillis(), periodType.getMillis());
 }

 @Test
 public void testCreatingMutablePeriodWithLong() {
 MutablePeriod period = new MutablePeriod(123456789L);
 assertEquals(1, period.getYears(), Defaults.YEARS_PER_MILLIS);
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(123456789L, period.getMillis());
 }

 @Test
 public void testSettingValues() {
 MutablePeriod period = new MutablePeriod();
 period.setValue(DurationFieldType.years().getIndex(), 1);
 period.setValue(DurationFieldType.months().getIndex(), 2);
 period.setValue(DurationFieldType.weeks().getIndex(), 3);
 period.setValue(DurationFieldType.days().getIndex(), 4);
 period.setValue(DurationFieldType.hours().getIndex(), 5);
 period.setValue(DurationFieldType.minutes().getIndex(), 6);
 period.setValue(DurationFieldType.seconds().getIndex(), 7);
 period.setValue(DurationFieldType.millis().getIndex(), 8);
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
 public void testSettingFields() {
 MutablePeriod period = new MutablePeriod();
 period.setField(DurationFieldType.years(), 1);
 period.setField(DurationFieldType.months(), 2);
 period.setField(DurationFieldType.weeks(), 3);
 period.setField(DurationFieldType.days(), 4);
 period.setField(DurationFieldType.hours(), 5);
 period.setField(DurationFieldType.minutes(), 6);
 period.setField(DurationFieldType.seconds(), 7);
 period.setField(DurationFieldType.millis(), 8);
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
 public void testSettingPeriod() {
 MutablePeriod period = new MutablePeriod();
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

 @Test
 public void testSettingPeriodWithValues() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(1, 2, 3, 4, 5, 6, 7, 8);
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
 public void testSettingPeriodWithStartMillisAndEndMillis() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(123456789L, 234567890L);
 assertEquals(1, period.getYears(), Defaults.YEARS_PER_MILLIS);
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(123456789L, period.getMillis());
 }

 @Test
 public void testSettingPeriodWithStartMillisAndEndMillisAndChronology() {
 MutablePeriod period = new MutablePeriod();
 Chronology chronology = DateTimeUtils.forDate(new Date());
 period.setPeriod(123456789L, 234567890L, chronology);
 assertEquals(1, period.getYears(), Defaults.YEARS_PER_MILLIS);
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(123456789L, period.getMillis());
 }

 @Test
 public void testSettingPeriodWithStartInstantAndEndInstant() {
 MutablePeriod period = new MutablePeriod();
 ReadableInstant startInstant = new DateTime(123456789000L);
 ReadableInstant endInstant = new DateTime(123456789001L);
 period.setPeriod(startInstant, endInstant);
 assertEquals(0, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(1L, period.getMillis());
 }

 @Test
 public void testSettingPeriodWithStartInstantAndEndInstantAndPeriodType() {
 MutablePeriod period = new MutablePeriod();
 ReadableInstant startInstant = new DateTime(123456789000L);
 ReadableInstant endInstant = new DateTime(123456789001L);
 PeriodType periodType = PeriodType.micro();
 period.setPeriod(startInstant, endInstant, periodType);
 assertEquals(0, period.getYears(), periodType.getYears());
 assertEquals(0, period.getMonths(), periodType.getMonths());
 assertEquals(0, period.getWeeks(), periodType.getWeeks());
 assertEquals(0, period.getDays(), periodType.getDays());
 assertEquals(0, period.getHours(), periodType.getHours());
 assertEquals(0, period.getMinutes(), periodType.getMinutes());
 assertEquals(0, period.getSeconds(), periodType.getSeconds());
 assertEquals(1L, period.getMillis(), periodType.getMillis());
 }

 @Test
 public void testSettingPeriodWithReadableDuration() {
 MutablePeriod period = new MutablePeriod();
 ReadableDuration readableDuration = new Duration(1, 2, 3, 4, 5, 6, 7, 8);
 period.setPeriod(readableDuration);
 assertEquals(0, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(1, period.getHours());
 assertEquals(2, period.getMinutes());
 assertEquals(3, period.getSeconds());
 assertEquals(4, period.getMillis());
 }

 @Test
 public void testSettingPeriodWithReadableDurationAndPeriodType() {
 MutablePeriod period = new MutablePeriod();
 ReadableDuration readableDuration = new Duration(1, 2, 3, 4, 5, 6, 7, 8);
 PeriodType periodType = PeriodType.micro();
 period.setPeriod(readableDuration, periodType);
 assertEquals(0, period.getYears(), periodType.getYears());
 assertEquals(0, period.getMonths(), periodType.getMonths());
 assertEquals(0, period.getWeeks(), periodType.getWeeks());
 assertEquals(0, period.getDays(), periodType.getDays());
 assertEquals(1, period.getHours(), periodType.getHours());
 assertEquals(2, period.getMinutes(), periodType.getMinutes());
 assertEquals(3, period.getSeconds(), periodType.getSeconds());
 assertEquals(4, period.getMillis(), periodType.getMillis());
 }

 @Test
 public void testSettingPeriodWithLongAndPeriodType() {
 MutablePeriod period = new MutablePeriod();
 long durationMillis = 123456789;
 PeriodType periodType = PeriodType.micro();
 period.setPeriod(durationMillis, periodType);
 assertEquals(0, period.getYears(), periodType.getYears());
 assertEquals(0, period.getMonths(), periodType.getMonths());
 assertEquals(0, period.getWeeks(), periodType.getWeeks());
 assertEquals(0, period.getDays(), periodType.getDays());
 assertEquals(1, period.getHours(), periodType.getHours());
 assertEquals(2, period.getMinutes(), periodType.getMinutes());
 assertEquals(3, period.getSeconds(), periodType.getSeconds());
 assertEquals(4, period.getMillis(), periodType.getMillis());
 assertEquals(durationMillis, period.getMillis(), periodType.getMillis());
 }

 @Test
 public void testSettingPeriodWithLongAndChronology() {
 MutablePeriod period = new MutablePeriod();
 long durationMillis = 123456789;
 Chronology chronology = DateTimeUtils.forDate(new Date());
 period.setPeriod(durationMillis, chronology);
 assertEquals(1, period.getYears(), Defaults.YEARS_PER_MILLIS);
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(durationMillis, period.getMillis());
 }

 @Test
 public void testAddingPeriod() {
 MutablePeriod period1 = new MutablePeriod();
 MutablePeriod period2 = new MutablePeriod();
 period1.addPeriod(new Period(1, 2, 3, 4, 5, 6, 7, 8));
 period2.addPeriod(new Period(9, 10, 11, 12, 13, 14, 15, 16));
 period1.addPeriod(period2);
 assertEquals(1, period1.getYears());
 assertEquals(2, period1.getMonths());
 assertEquals(3, period1.getWeeks());
 assertEquals(4, period1.getDays());
 assertEquals(14, period1.getHours());
 assertEquals(14, period1.getMinutes());
 assertEquals(15, period1.getSeconds());
 assertEquals(16, period1.getMillis());
 }

 @Test
 public void testAddingLong() {
 MutablePeriod period = new MutablePeriod();
 period.add(new Period(1, 2, 3, 4, 5, 6, 7, 8));
 period.add(123456789);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(14, period.getHours());
 assertEquals(14, period.getMinutes());
 assertEquals(15, period.getSeconds());
 assertEquals(160457891L, period.getMillis());
 }

 @Test
 public void testMergingPeriod() {
 MutablePeriod period1 = new MutablePeriod();
 MutablePeriod period2 = new MutablePeriod();
 period1.addPeriod(new Period(1, 2, 3, 4, 5, 6, 7, 8));
 period2.addPeriod(new Period(9, 10, 11, 12, 13, 14, 15, 16));
 period1.mergePeriod(period2);
 assertEquals(2, period1.getYears());
 assertEquals(12, period1.getMonths());
 assertEquals(3, period1.getWeeks());
 assertEquals(14, period1.getDays());
 assertEquals(14, period1.getHours());
 assertEquals(14, period1.getMinutes());
 assertEquals(15, period1.getSeconds());
 assertEquals(16, period1.getMillis());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingPeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingPeriodWithInvalidFormat() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod("invalid format");
 }
}