import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParse() {
 MutablePeriod mutablePeriod = MutablePeriod.parse("P1Y2M3D");
 assertEquals(1, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(3, mutablePeriod.getDays());
 }

 @Test
 public void testParseWithCustomFormatter() {
 PeriodFormatter formatter = ISOPeriodFormat.standard().withParseAnchors(ParseAnchors.ALL);
 MutablePeriod mutablePeriod = MutablePeriod.parse("PT1H30M", formatter);
 assertEquals(1, mutablePeriod.getHours());
 assertEquals(30, mutablePeriod.getMinutes());
 }

 @Test
 public void testConstructorWithDefaults() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 assertEquals(0, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(0, mutablePeriod.getDays());
 assertEquals(0, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testConstructorWithPeriodType() {
 MutablePeriod mutablePeriod = new MutablePeriod(PeriodType.standard());
 assertEquals(0, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(0, mutablePeriod.getDays());
 assertEquals(0, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testConstructorWithInts() {
 MutablePeriod mutablePeriod = new MutablePeriod(3, 2, 1, 4, 5, 6, 7, 8);
 assertEquals(3, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(1, mutablePeriod.getWeeks());
 assertEquals(4, mutablePeriod.getDays());
 assertEquals(5, mutablePeriod.getHours());
 assertEquals(6, mutablePeriod.getMinutes());
 assertEquals(7, mutablePeriod.getSeconds());
 assertEquals(8, mutablePeriod.getMillis());
 }

 @Test
 public void testConstructorWithIntsAndPeriodType() {
 PeriodType periodType = PeriodType.standard();
 MutablePeriod mutablePeriod = new MutablePeriod(3, 2, 1, 4, 5, 6, 7, 8, periodType);
 assertEquals(3, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(1, mutablePeriod.getWeeks());
 assertEquals(4, mutablePeriod.getDays());
 assertEquals(5, mutablePeriod.getHours());
 assertEquals(6, mutablePeriod.getMinutes());
 assertEquals(7, mutablePeriod.getSeconds());
 assertEquals(8, mutablePeriod.getMillis());
 assertEquals(periodType, mutablePeriod.getPeriodType());
 }

 @Test
 public void testConstructorWithLong() {
 MutablePeriod mutablePeriod = new MutablePeriod(123456789L);
 assertEquals(1, mutablePeriod.getYears());
 assertEquals(123, mutablePeriod.getMonths());
 assertEquals(7, mutablePeriod.getDays());
 assertEquals(23, mutablePeriod.getHours());
 assertEquals(45, mutablePeriod.getMinutes());
 assertEquals(67, mutablePeriod.getSeconds());
 assertEquals(89, mutablePeriod.getMillis());
 }

 @Test
 public void testConstructorWithLongAndPeriodType() {
 MutablePeriod mutablePeriod = new MutablePeriod(123456789L, PeriodType.standard());
 assertEquals(1, mutablePeriod.getYears());
 assertEquals(123, mutablePeriod.getMonths());
 assertEquals(7, mutablePeriod.getDays());
 assertEquals(23, mutablePeriod.getHours());
 assertEquals(45, mutablePeriod.getMinutes());
 assertEquals(67, mutablePeriod.getSeconds());
 assertEquals(89, mutablePeriod.getMillis());
 assertEquals(PeriodType.standard(), mutablePeriod.getPeriodType());
 }

 @Test
 public void testConstructorWithReadableInstants() {
 MutablePeriod mutablePeriod = new MutablePeriod(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z"), DateTimeUtils.parseLocalDateTime("2022-01-02T01:01:01.010Z"));
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86401001L, mutablePeriod.getMillis());
 }

 @Test
 public void testConstructorWithReadableDurations() {
 MutablePeriod mutablePeriod = new MutablePeriod(Duration.standardHours(1), Duration.standardHours(2));
 assertEquals(1, mutablePeriod.getHours());
 assertEquals(2, mutablePeriod.getHours());
 assertEquals(60 * 60 * 1000L, mutablePeriod.getMillis());
 }

 @Test
 public void testClear() {
 MutablePeriod mutablePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.clear();
 assertEquals(0, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(0, mutablePeriod.getDays());
 assertEquals(0, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testSetValue() {
 MutablePeriod mutablePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.setValue(0, 123);
 assertEquals(123, mutablePeriod.getValues()[0]);
 }

 @Test
 public void testSetField() {
 MutablePeriod mutablePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 mutablePeriod.setField(DurationFieldType.years(), 123);
 assertEquals(123, mutablePeriod.getYears());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 ReadablePeriod readablePeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 mutablePeriod.setPeriod(readablePeriod);
 assertEquals(1, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(3, mutablePeriod.getWeeks());
 assertEquals(4, mutablePeriod.getDays());
 assertEquals(5, mutablePeriod.getHours());
 assertEquals(6, mutablePeriod.getMinutes());
 assertEquals(7, mutablePeriod.getSeconds());
 assertEquals(8, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithValues() {
 MutablePeriod mutablePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
 mutablePeriod.setPeriod(values);
 assertEquals(1, mutablePeriod.getYears());
 assertEquals(2, mutablePeriod.getMonths());
 assertEquals(3, mutablePeriod.getWeeks());
 assertEquals(4, mutablePeriod.getDays());
 assertEquals(5, mutablePeriod.getHours());
 assertEquals(6, mutablePeriod.getMinutes());
 assertEquals(7, mutablePeriod.getSeconds());
 assertEquals(8, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartAndEndInstant() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 ReadableInstant start = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z");
 ReadableInstant end = DateTimeUtils.parseLocalDateTime("2022-01-02T01:01:01.010Z");
 mutablePeriod.setPeriod(start, end);
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86401001L, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartAndEndMillis() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 long endMillis = DateTimeUtils.parseLocalDateTime("2022-01-02T01:01:01.010Z").getMillis();
 mutablePeriod.setPeriod(startMillis, endMillis);
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86401001L, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartInstantAndDuration() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 ReadableInstant start = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z");
 ReadableDuration duration = Duration.standardDays(1);
 mutablePeriod.setPeriod(start, duration);
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86400000L, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartMillisAndDuration() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 ReadableDuration duration = Duration.standardDays(1);
 mutablePeriod.setPeriod(startMillis, duration);
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86400000L, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartInstantAndMillis() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 ReadableInstant start = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z");
 long endMillis = DateTimeUtils.parseLocalDateTime("2022-01-02T01:01:01.010Z").getMillis();
 mutablePeriod.setPeriod(start, endMillis);
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86401001L, mutablePeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartMillisAndPeriodType() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 PeriodType periodType = PeriodType.standard();
 mutablePeriod.setPeriod(startMillis, periodType);
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86400000L, mutablePeriod.getMillis());
 assertEquals(periodType, mutablePeriod.getPeriodType());
 }

 @Test
 public void testSetPeriodWithStartMillisAndCustomChronology() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 ISOChronology customChronology = ISOChronology.getInstance();
 mutablePeriod.setPeriod(startMillis, customChronology);
 assertEquals(1, mutablePeriod.getDays());
 assertEquals(86400000L, mutablePeriod.getMillis());
 assertEquals(customChronology, mutablePeriod.getChronology());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithNull() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 mutablePeriod.setPeriod(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithStartAndNullDuration() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 ReadableInstant start = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z");
 mutablePeriod.setPeriod(start, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithStartAndNegativeDuration() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 ReadableInstant start = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z");
 ReadableDuration duration = Duration.standardDays(-1);
 mutablePeriod.setPeriod(start, duration);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithStartMillisAndNegativeDuration() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 ReadableDuration duration = Duration.standardDays(-1);
 mutablePeriod.setPeriod(startMillis, duration);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithStartMillisAndNullPeriodType() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 mutablePeriod.setPeriod(startMillis, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithStartMillisAndInvalidPeriodType() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 PeriodType invalidPeriodType = new PeriodType() {
 @Override
 public int getPrecision() {
 return Integer.MIN_VALUE;
 }
 };
 mutablePeriod.setPeriod(startMillis, invalidPeriodType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithStartMillisAndNullChronology() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 mutablePeriod.setPeriod(startMillis, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithStartMillisAndInvalidChronology() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 long startMillis = DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.000Z").getMillis();
 Object invalidChronology = new Object();
 mutablePeriod.setPeriod(startMillis, (Chronology)invalidChronology);
 }
}