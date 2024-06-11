import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 @Test
 public void testBetween_Instant() {
 ReadableInstant instant = new Instant(System.currentTimeMillis());
 ReadableInstant instant2 = new Instant(System.currentTimeMillis() + 1000);
 DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
 int result = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
 assertEquals(1000, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_InstantNull() {
 ReadableInstant instant = null;
 ReadableInstant instant2 = new Instant(System.currentTimeMillis());
 DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
 BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_Instant2Null() {
 ReadableInstant instant = new Instant(System.currentTimeMillis());
 ReadableInstant instant2 = null;
 DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
 BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
 }

 @Test
 public void testBetween_Partial() {
 ReadablePartial readablePartial = new LocalDate();
 ReadablePartial readablePartial2 = new LocalDate().plusDays(1);
 ReadablePeriod readablePeriod = Period.ONE_DAY;
 int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 assertEquals(86400000, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_PartialNull() {
 ReadablePartial readablePartial = null;
 ReadablePartial readablePartial2 = new LocalDate();
 ReadablePeriod readablePeriod = Period.ONE_DAY;
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_PartialSizeMismatch() {
 ReadablePartial readablePartial = new LocalDate();
 ReadablePartial readablePartial2 = new LocalDateTime();
 ReadablePeriod readablePeriod = Period.ONE_DAY;
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_PartialNonContiguous() {
 ReadablePartial readablePartial = new LocalDate().withDayOfMonth(1).withDayOfWeek(DateTimeConstants.FRIDAY);
 ReadablePartial readablePartial2 = new LocalDate().withDayOfMonth(1).withDayOfWeek(DateTimeConstants.SATURDAY);
 ReadablePeriod readablePeriod = Period.ONE_DAY;
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test
 public void testStandardPeriodIn() {
 ReadablePeriod readablePeriod = Period.ONE_DAY;
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(172800000, result * n);
 }

 @Test
 public void testStandardPeriodIn_ZeroPeriod() {
 ReadablePeriod readablePeriod = Period.ZERO;
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(0, result);
 }

 @Test
 public void testConstructors() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 assertEquals(1, period.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_NegativeValue() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 assertEquals(1, period.getValue());
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 period.setValue(2);
 assertEquals(2, period.getValue());
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 DurationFieldType fieldType = period.getFieldType();
 assertEquals(DurationFieldType.MILLISECONDS, fieldType);
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 PeriodType periodType = period.getPeriodType();
 assertEquals(PeriodType.MILLISECONDS, periodType);
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 assertEquals(1, period.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldType_WrongIndex() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 period.getFieldType(2);
 }

 @Test
 public void testgetValue_WrongIndex() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 assertEquals(1, period.getValue());
 assertEquals(1, period.getValue(0));
 }

 @Test
 public void testGet_FieldTypeMatches() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 DurationFieldType fieldType = new DurationFieldType() {
 @Override
 public DurationField getField(Chronology chronology) {
 return new DaysField(chronology, true);
 }

 @Override
 public String getName() {
 return "days";
 }

 @Override
 public PeriodType getPeriodType() {
 return PeriodType.DAYS;
 }
 };
 assertEquals(fieldType, period.getFieldType());
 }

 @Test
 public void testGet_FieldTypeDoesntMatch() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 DurationFieldType fieldType = new DurationFieldType() {
 @Override
 public DurationField getField(Chronology chronology) {
 return new MinutesField(chronology, true);
 }

 @Override
 public String getName() {
 return "minutes";
 }

 @Override
 public PeriodType getPeriodType() {
 return PeriodType.MINUTES;
 }
 };
 assertNotEquals(fieldType, period.getFieldType());
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 Period result = period.toPeriod();
 assertEquals(Period.ONE_DAY, result);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 MutablePeriod mutablePeriod = period.toMutablePeriod();
 mutablePeriod.add(Period.ONE_DAY);
 assertEquals(2, mutablePeriod.getValues()[0]);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
 assertTrue(period1.equals(period2));
 }

 @Test
 public void testEquals_DifferentClass() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 Object o = new Object();
 assertFalse(period1.equals(o));
 }

 @Test
 public void testEquals_DifferentValue() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
 assertEquals(period1.hashCode(), period2.hashCode());
 }

 @Test
 public void testCompareTo_SameValue() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
 assertEquals(0, period1.compareTo(period2));
 }

 @Test
 public void testCompareTo_DifferentValues() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
 assertEquals(-1, period1.compareTo(period2));
 }

 @Test
 public void testCompareTo_SameClass() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
 assertEquals(0, period1.compareTo(period2));
 }

 @Test
 public void testCompareTo_DifferentClass_SameValue() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 Object o = new Object();
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
 assertEquals(0, period1.compareTo(period2));
 }

 @Test
 public void testCompareTo_DifferentClass_DifferentValue() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 Object o = new Object();
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
 assertEquals(-1, period1.compareTo(period2));
 }
}