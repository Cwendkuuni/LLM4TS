package org.joda.time.base;

import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadOnlyDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createBaseSingleFieldPeriod(int iPeriod) {
 return new BaseSingleFieldPeriod(iPeriod) {
 @Override
 public DurationFieldType getFieldType() {
 return DurationFieldType.DAYS;
 }

 @Override
 public PeriodType getPeriodType() {
 return PeriodType.createEmpty();
 }
 };
 }

 @Test
 public void testBetween() {
 ReadableInstant instant1 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 ReadableInstant instant2 = new DateTime(2018, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC);
 DurationFieldType durationFieldType = DurationFieldType.DAYS;
 int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 int expected = 1;
 assertEquals("Unexpected difference between instants", expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenInstantsNull() {
 ReadableInstant instant1 = null;
 ReadableInstant instant2 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 DurationFieldType durationFieldType = DurationFieldType.DAYS;
 BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenInstants2Null() {
 ReadableInstant instant1 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 ReadableInstant instant2 = null;
 DurationFieldType durationFieldType = DurationFieldType.DAYS;
 BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 }

 @Test
 public void testStandardPeriodIn() {
 Period period = Period.days(1);
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 int expected = 52160000; // 2 * 24 * 60 * 60 * 1000
 assertEquals("Unexpected period duration", expected, result * n);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInNull() {
 Period period = null;
 long n = 2;
 BaseSingleFieldPeriod.standardPeriodIn(period, n);
 }

 @Test
 public void testNewInstance() {
 int iPeriod = 3;
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(iPeriod);
 int expected = iPeriod;
 assertEquals("Unexpected period value", expected, baseSingleFieldPeriod.getValue());
 }

 @Test
 public void testNewInstanceNegative() {
 int iPeriod = -3;
 try {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(iPeriod);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(1);
 assertTrue("Unexpected false equals result", baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 }

 @Test
 public void testNotEquals() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(2);
 assertFalse("Unexpected true equals result", baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareToZero() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 int result = baseSingleFieldPeriod.compareTo(BaseSingleFieldPeriod.ZERO);
 int expected = 1;
 assertEquals("Unexpected compareTo result", expected, result);
 }

 @Test
 public void testCompareToNegative() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(-1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(1);
 int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
 int expected = -1;
 assertEquals("Unexpected compareTo result", expected, result);
 }

 @Test
 public void testCompareToSame() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(1);
 int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
 int expected = 0;
 assertEquals("Unexpected compareTo result", expected, result);
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 DurationFieldType result = baseSingleFieldPeriod.getFieldType();
 DurationFieldType expected = DurationFieldType.DAYS;
 assertEquals("Unexpected field type", expected, result);
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 PeriodType result = baseSingleFieldPeriod.getPeriodType();
 PeriodType expected = PeriodType.createEmpty();
 assertEquals("Unexpected period type", expected, result);
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 int result = baseSingleFieldPeriod.size();
 int expected = 1;
 assertEquals("Unexpected size", expected, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueOutOfBounds() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 baseSingleFieldPeriod.getValue(2);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 int result = baseSingleFieldPeriod.getValue();
 int expected = 1;
 assertEquals("Unexpected value", expected, result);
 }

 @Test
 public void testGetValueNegative() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(-1);
 int result = baseSingleFieldPeriod.getValue();
 int expected = -1;
 assertEquals("Unexpected value", expected, result);
 }

 @Test
 public void testAddToMutablePeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 mutablePeriod.add(baseSingleFieldPeriod);
 Period result = mutablePeriod.toPeriod();
 Period expected = Period.days(1);
 assertEquals("Unexpected added period", expected, result);
 }

 @Test
 public void testAddToMutablePeriodNegative() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(-1);
 mutablePeriod.add(baseSingleFieldPeriod);
 Period result = mutablePeriod.toPeriod();
 Period expected = Period.ZERO;
 assertEquals("Unexpected added period", expected, result);
 }

 @Test
 public void testToString() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1);
 String result = baseSingleFieldPeriod.toString();
 String expected = "PT1D";
 assertEquals("Unexpected toString result", expected, result);
 }
}