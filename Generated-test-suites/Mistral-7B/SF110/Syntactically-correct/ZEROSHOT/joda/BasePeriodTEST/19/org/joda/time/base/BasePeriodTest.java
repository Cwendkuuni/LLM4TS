import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.field.DateTimeFields;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodWithValidArguments() {
 BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithChronology() {
 BasePeriod basePeriod = new BasePeriod(1234567890L, 1234567891L, PeriodType.standard(), DateTimeUtils.UTC);
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{7, 1, 0, 0, 0, 1, 1, 1}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithReadableInstant() {
 BasePeriod basePeriod = new BasePeriod(DateTimeConstants.MINUTE_OF_JANUARY_1_2000, DateTimeConstants.MINUTE_OF_JANUARY_2_2000, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{0, 1, 0, 0, 0, 1, 0, 0}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithReadablePartial() {
 BaseLocal localStart = new LocalDateTime(2000, 1, 1, 0, 0, 0).toLocalBase();
 BaseLocal localEnd = new LocalDateTime(2000, 1, 2, 0, 0, 0).toLocalBase();
 BasePeriod basePeriod = new BasePeriod(localStart, localEnd, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{DateTimeFields.YEARS, 1, DateTimeFields.MONTHS, 0, DateTimeFields.DAYS, 1, DateTimeFields.HOURS, 0, DateTimeFields.MINUTES, 0, DateTimeFields.SECONDS, 0, DateTimeFields.MILLIS, 0}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithReadableInstantAndReadableDuration() {
 BasePeriod basePeriod = new BasePeriod(DateTimeConstants.MINUTE_OF_JANUARY_1_2000, new Duration(2, DateTimeConstants.HOURS), PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{0, 2, 0, 0, 0, 0, 0, 0}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithReadableDurationAndReadableInstant() {
 BasePeriod basePeriod = new BasePeriod(new Duration(2, DateTimeConstants.HOURS), DateTimeConstants.MINUTE_OF_JANUARY_1_2000, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{0, 0, 0, 0, 2, 0, 0, 0}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithZeroArguments() {
 BasePeriod basePeriod = new BasePeriod(0);
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.time(), periodType);
 assertArrayEquals(new int[8], basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithObject() {
 BasePeriod basePeriod = new BasePeriod(new Duration(2, DateTimeConstants.HOURS), PeriodType.standard(), DateTimeUtils.UTC);
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{0, 0, 0, 0, 2, 0, 0, 0}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithObjectAndChronology() {
 BasePeriod basePeriod = new BasePeriod(new org.joda.time.Duration(2, DateTimeConstants.HOURS), PeriodType.standard(), DateTimeUtils.UTC);
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{0, 0, 0, 0, 2, 0, 0, 0}, basePeriod.iValues);
 }

 @Test
 public void testBasePeriodWithValidArgumentsWithIntArrayAndPeriodType() {
 BasePeriod basePeriod = new BasePeriod(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 assertArrayEquals(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, basePeriod.iValues);
 }

 @Test
 public void testGetPeriodType() {
 BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testGetValue() {
 BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 int value = basePeriod.getValue(DateTimeFields.YEARS);
 assertEquals(2, value);
 }

 @Test
 public void testToDurationFrom() {
 BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 ReadableInstant readableInstant = new DateTime(2000, 1, 1, 0, 0, 0).toInstant();
 Duration duration = basePeriod.toDurationFrom(readableInstant);
 int years = duration.getStandardDays() / 365;
 int months = duration.getStandardDays() % 365;
 int days = duration.getStandardHours() / 24;
 int hours = duration.getStandardHours() % 24;
 int minutes = duration.getStandardMinutes();
 int seconds = duration.getStandardSeconds();
 int millis = duration.getMillis();
 assertEquals(2, years);
 assertEquals(3, months);
 assertEquals(4, days);
 assertEquals(5, hours);
 assertEquals(6, minutes);
 assertEquals(7, seconds);
 assertEquals(9, millis);
 }

 @Test
 public void testToDurationTo() {
 BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 ReadableInstant readableInstant = new DateTime(2000, 1, 4, 12, 30, 5).toInstant();
 Duration duration = basePeriod.toDurationTo(readableInstant);
 int years = duration.getStandardDays() / -365;
 int months = duration.getStandardDays() % -365;
 int days = -duration.getStandardHours() / 24;
 int hours = -duration.getStandardHours() % 24;
 int minutes = -duration.getStandardMinutes();
 int seconds = -duration.getStandardSeconds();
 int millis = -duration.getMillis();
 assertEquals(-2, years);
 assertEquals(-3, months);
 assertEquals(-4, days);
 assertEquals(-5, hours);
 assertEquals(-6, minutes);
 assertEquals(-7, seconds);
 assertEquals(-9, millis);
 }
}