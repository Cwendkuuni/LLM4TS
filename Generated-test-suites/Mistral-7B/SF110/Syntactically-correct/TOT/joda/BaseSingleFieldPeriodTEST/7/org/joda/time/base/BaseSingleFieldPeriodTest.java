package org.joda.time.base;

 import static org.junit.Assert.*;
 import org.joda.time.*;
 import org.joda.time.chrono.*;
 import org.joda.time.field.*;
 import org.junit.*;

 public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod period1;
 private BaseSingleFieldPeriod period2;

 @Before
 public void setUp() {
 period1 = new BaseSingleFieldPeriod(5);
 period2 = new BaseSingleFieldPeriod(10);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType durationFieldType = period1.getFieldType();
 assertEquals(DurationFieldType.DAYS, durationFieldType);
 }

 @Test
 public void testGetPeriodType() {
 PeriodType periodType = period1.getPeriodType();
 assertEquals(PeriodType.DAYS, periodType);
 }

 @Test
 public void testSize() {
 int size = period1.size();
 assertEquals(1, size);
 }

 @Test
 public void testGetValue() {
 int value1 = period1.getValue();
 int value2 = period2.getValue();
 assertEquals(5, value1);
 assertEquals(10, value2);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);
 assertTrue(period1.equals(period1));
 assertTrue(period1.equals(period3));
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = period1.hashCode();
 int hashCode2 = period2.hashCode();
 BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(5);
 assertEquals(hashCode1, hashCode1);
 assertEquals(hashCode2, hashCode2);
 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode4);
 }

 @Test
 public void testCompareTo() {
 int result1 = period1.compareTo(period1);
 int result2 = period1.compareTo(period2);
 int result3 = period2.compareTo(period1);
 int result4 = period2.compareTo(period2);
 assertEquals(0, result1);
 assertEquals(1, result2);
 assertEquals(-1, result3);
 assertEquals(0, result4);
 }

 @Test
 public void testStandardPeriodIn() {
 long n = 7;
 int expected = 35;
 int actual = BaseSingleFieldPeriod.standardPeriodIn(period1, n);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWithNullPeriod() {
 BaseSingleFieldPeriod nullPeriod = null;
 int n = 7;
 BaseSingleFieldPeriod.standardPeriodIn(nullPeriod, n);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant() {
 ReadableInstant nullReadableInstant = null;
 ReadableInstant readableInstant2 = DateTime.now();
 DurationFieldType durationFieldType = DurationFieldType.DAYS;
 int between = BaseSingleFieldPeriod.between(nullReadableInstant, readableInstant2, durationFieldType);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant2() {
 ReadableInstant readableInstant = DateTime.now();
 ReadableInstant nullReadableInstant2 = null;
 DurationFieldType durationFieldType = DurationFieldType.DAYS;
 int between = BaseSingleFieldPeriod.between(readableInstant, nullReadableInstant2, durationFieldType);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithDifferentFieldTypes() {
 ReadableInstant readableInstant = DateTime.now();
 ReadableInstant readableInstant2 = DateTime.now();
 DurationFieldType durationFieldType = DurationFieldType.HOURS;
 DurationFieldType differentDurationFieldType = DurationFieldType.DAYS;
 int between = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
 int between2 = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, differentDurationFieldType);
 assertNotSame(between, between2);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNonContiguousReadablePartial() {
 ReadablePartial readablePartial = new GeneralMillis(DateTime.now().getMillis() - 1000, DateTime.now().getChronology());
 ReadablePartial readablePartial2 = new GeneralMillis(DateTime.now().getMillis() + 1000, DateTime.now().getChronology());
 ReadablePeriod readablePeriod = Period.ONE_HOUR;
 int between = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 fail("IllegalArgumentException expected but not thrown");
 }
 }