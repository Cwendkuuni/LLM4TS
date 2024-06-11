package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createInstance(int iPeriod) {
 return new AnonymousSingleFieldPeriod(iPeriod);
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod instance = createInstance(1);
 assertEquals(DurationFieldType.DAYS, instance.getFieldType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetFieldTypeUnsupported() {
 BaseSingleFieldPeriod instance = createInstance(1);
 try {
 instance.getFieldType(1);
 } catch (IndexOutOfBoundsException e) {
 throw new AssertionError("Expected UnsupportedOperationException", e);
 }
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod instance = createInstance(1);
 assertEquals(PeriodType.fixedPeriod, instance.getPeriodType());
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod instance = createInstance(1);
 assertEquals(1, instance.size());
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod instance = createInstance(3);
 assertEquals(3, instance.getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueUnsupported() {
 BaseSingleFieldPeriod instance = createInstance(1);
 try {
 instance.getValue(1);
 } catch (IndexOutOfBoundsException e) {
 throw new AssertionError("Expected UnsupportedOperationException", e);
 }
 }

 @Test
 public void testGet() {
 BaseSingleFieldPeriod instance = createInstance(3);
 DurationFieldType durationFieldType = DurationFieldType.DAYS;
 assertEquals(3, instance.get(durationFieldType));
 }

 @Test
 public void testIsSupported() {
 BaseSingleFieldPeriod instance = createInstance(1);
 assertTrue(instance.isSupported(DurationFieldType.DAYS));
 assertFalse(instance.isSupported(DurationFieldType.HOURS));
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod instance = createInstance(3);
 Period expected = new Period(3, DurationFieldType.DAYS);
 Period actual = instance.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod instance = createInstance(3);
 MutablePeriod expected = new MutablePeriod().withValue(DurationFieldType.DAYS, 3);
 MutablePeriod actual = instance.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod instance1 = createInstance(2);
 BaseSingleFieldPeriod instance2 = createInstance(2);
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testEqualsDifferentInstance() {
 BaseSingleFieldPeriod instance1 = createInstance(2);
 BaseSingleFieldPeriod instance2 = createInstance(3);
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testEqualsNull() {
 BaseSingleFieldPeriod instance = createInstance(2);
 assertFalse(instance.equals(null));
 }

 @Test
 public void testCompareToSameInstance() {
 BaseSingleFieldPeriod instance1 = createInstance(2);
 int result = instance1.compareTo(instance1);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToZero() {
 BaseSingleFieldPeriod instance1 = createInstance(2);
 int result = instance1.compareTo(BaseSingleFieldPeriod.ZERO);
 assertTrue(result > 0);
 }

 @Test
 public void testCompareToNegative() {
 BaseSingleFieldPeriod instance1 = createInstance(-2);
 int result = instance1.compareTo(BaseSingleFieldPeriod.ZERO);
 assertTrue(result < 0);
 }

 @Test
 public void testCompareToDifferentInstance() {
 BaseSingleFieldPeriod instance1 = createInstance(2);
 BaseSingleFieldPeriod instance2 = createInstance(3);
 int result = instance1.compareTo(instance2);
 assertTrue(result < 0);
 }

 private static class AnonymousSingleFieldPeriod extends BaseSingleFieldPeriod {
 AnonymousSingleFieldPeriod(int iPeriod) {
 super(iPeriod);
 }
 }
}