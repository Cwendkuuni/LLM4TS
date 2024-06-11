package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.junit.BaseConversionTest;

public class BaseSingleFieldPeriodTest extends BaseConversionTest {

 private BaseSingleFieldPeriod period;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 period = new BaseSingleFieldPeriod(3600000) {
 @Override
 public DurationFieldType getFieldType() {
 return new Hours(60);
 }
 };
 }

 // Test for protected static int between(ReadableInstant, ReadableInstant, DurationFieldType)
 @Test
 public void testBetween_ReadableInstants() {
 Instant start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
 Instant end = new DateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC).toInstant();
 int result = BaseSingleFieldPeriod.between(start, end, new Hours(1));
 assertEquals(3600000, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_NullReadableInstants() {
 Instant start = null;
 Instant end = new DateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC).toInstant();
 try {
 BaseSingleFieldPeriod.between(start, end, new Hours(1));
 } catch (IllegalArgumentException e) {
 assertEquals("ReadableInstant objects must not be null", e.getMessage());
 }
 }

 // Test for protected static int between(ReadablePartial, ReadablePartial, ReadablePeriod)
 @Test
 public void testBetween_ReadablePartials() {
 LocalDateTime start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 LocalDateTime end = new DateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC);
 Period period = new Period(3600000, new Hours(1), new Minutes(0), newSeconds(0));
 int result = BaseSingleFieldPeriod.between(new LocalDateTimePeriod(start), new LocalDateTimePeriod(end), period);
 assertEquals(3600000, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_NullReadablePartial() {
 LocalDateTime start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 LocalDateTime end = new DateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC);
 Period period = new Period(3600000, new Hours(1), new Minutes(0), newSeconds(0));
 try {
 BaseSingleFieldPeriod.between(null, new LocalDateTimePeriod(end), period);
 } catch (IllegalArgumentException e) {
 assertEquals("ReadablePartial objects must not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_DifferentFieldTypes() {
 LocalDateTime start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 LocalDateTime end = new DateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC);
 Period period = new Period(3600000, new Hours(1), new Minutes(0), newSeconds(0));
 try {
 BaseSingleFieldPeriod.between(new LocalDateTimePeriod(start), new LocalDateTimePeriod(end), period);
 BaseSingleFieldPeriod.between(new LocalDateTimePeriod(start), new LocalDateTimePeriod(end), new Days(1));
 } catch (IllegalArgumentException e) {
 assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_NonContiguousReadablePartial() {
 LocalDateTime start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 LocalDateTime end = new DateTime(2022, 1, 1, 0, 1, 0, 0, DateTimeZone.UTC);
 Period period = new Period(3600000, new Hours(1), new Minutes(1), newSeconds(0));
 try {
 BaseSingleFieldPeriod.between(new LocalDateTimePeriod(start), new LocalDateTimePeriod(end), period);
 } catch (IllegalArgumentException e) {
 assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
 }
 }

 // Test for protected static int standardPeriodIn(ReadablePeriod, long)
 @Test
 public void testStandardPeriodIn() {
 Period period = new Period(3600000, new Hours(1), new Minutes(0), newSeconds(0));
 long divisor = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, divisor);
 assertEquals(1800000, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodIn_NullReadablePeriod() {
 Period period = null;
 long divisor = 2;
 try {
 BaseSingleFieldPeriod.standardPeriodIn(period, divisor);
 } catch (IllegalArgumentException e) {
 assertEquals("ReadablePeriod objects must not be null", e.getMessage());
 }
 }

 // Test for BaseSingleFieldPeriod(int) constructor
 @Test
 public void testConstructor() {
 int iPeriod = 3600000;
 BaseSingleFieldPeriod actual = new BaseSingleFieldPeriod(iPeriod);
 BaseSingleFieldPeriod expected = new BaseSingleFieldPeriod(iPeriod) {
 @Override
 public DurationFieldType getFieldType() {
 return new Hours(60);
 }
 };
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(expected.getValue(), actual.getValue());
 }

 // Test for getValue()
 @Test
 public void testGetValue() {
 assertEquals(3600000, period.getValue());
 }

 // Test for setValue(int)
 @Test
 public void testSetValue() {
 period.setValue(7200000);
 assertEquals(7200000, period.getValue());
 }

 // Test for abstract DurationFieldType getFieldType()
 @Test
 public void testGetFieldType() {
 assertEquals(Hours.class, period.getFieldType().getClass());
 }

 // Test for abstract PeriodType getPeriodType()
 @Test
 public void testGetPeriodType() {
 fail("Method not implemented.");
 }

 // Test for int size()
 @Test
 public void testSize() {
 assertEquals(1, period.size());
 }

 // Test for DurationFieldType getFieldType(int)
 @Test
 public void testGetFieldType() {
 assertEquals(Hours.class, period.getFieldType(0));
 }

 // Test for int getValue(int)
 @Test
 public void testGetValue() {
 assertEquals(3600000, period.getValue(0));
 }

 // Test for int get(DurationFieldType)
 @Test
 public void testGet() {
 assertEquals(3600000, period.get(new Hours(1)));
 }

 // Test for boolean isSupported(DurationFieldType)
 @Test
 public void testIsSupported() {
 assertTrue(period.isSupported(new Hours(1)));
 }

 // Test for Period toPeriod()
 @Test
 public void testToPeriod() {
 Period expected = new Period(3600000, new Hours(1), new Minutes(0), newSeconds(0));
 Period actual = period.toPeriod();
 assertEquals(expected, actual);
 }

 // Test for MutablePeriod toMutablePeriod()
 @Test
 public void testToMutablePeriod() {
 MutablePeriod expected = new MutablePeriod();
 expected.add(new Period(3600000, new Hours(1), new Minutes(0), newSeconds(0)));
 MutablePeriod actual = period.toMutablePeriod();
 assertEquals(expected, actual);
 }

 // Test for boolean equals(Object)
 @Test
 public void testEquals() {
 Period other = new Period(3600000, new Hours(1), new Minutes(0), newSeconds(0));
 assertTrue(period.equals(other));
 }

 // Test for int hashCode()
 @Test
 public void testHashCode() {
 int expectedHashCode = 27 * (27 * 17 + 3600000) + Hours.class.hashCode();
 int actualHashCode = period.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 // Test for int compareTo(BaseSingleFieldPeriod)
 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(3600000 + 1) {
 @Override
 public DurationFieldType getFieldType() {
 return new Hours(60);
 }
 };
 int result = period.compareTo(other);
 assertEquals(-1, result);
 }

 // Test for int compareTo(BaseSingleFieldPeriod) with same value but different type
 @Test
 public void testCompareTo_SameValueDifferentType() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(3600000) {
 @Override
 public DurationFieldType getFieldType() {
 return new Minutes(60);
 }
 };
 int result = period.compareTo(other);
 assertEquals(0, result);
 }
}