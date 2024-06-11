package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createBaseSingleFieldPeriod(int iPeriod) {
 return new BaseSingleFieldPeriodForTest(iPeriod);
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
 assertNotNull(fieldType);
 assertEquals(DurationFieldType.YEARS, fieldType);
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
 assertNotNull(periodType);
 assertEquals(BaseSingleFieldPeriod.PERIOD_TYPE_INSTANCE, periodType);
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 int size = baseSingleFieldPeriod.size();
 assertEquals(1, size);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 int value = baseSingleFieldPeriod.getValue();
 assertEquals(3, value);
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 baseSingleFieldPeriod.setValue(4);
 int value = baseSingleFieldPeriod.getValue();
 assertEquals(4, value);
 }

 @Test
 public void testGetFieldType_InvalidArgument() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 assertThrows(IndexOutOfBoundsException.class, () -> baseSingleFieldPeriod.getFieldType(1));
 }

 @Test
 public void testGetValue_InvalidArgument() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 assertThrows(IndexOutOfBoundsException.class, () -> baseSingleFieldPeriod.getValue(1));
 }

 @Test
 public void testIsSupported() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 DurationFieldType durationFieldType = DurationFieldType.MONTHS;
 boolean isSupported = baseSingleFieldPeriod.isSupported(durationFieldType);
 assertFalse(isSupported);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(3);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(3);
 Period baseSingleFieldPeriod3 = Period.ZERO.withFields(new BaseSingleFieldPeriodForTest(3), new BaseSingleFieldPeriodForTest(3));
 assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod1));
 assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod3));
 BaseSingleFieldPeriod baseSingleFieldPeriod4 = createBaseSingleFieldPeriod(4);
 assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod4));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(3);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(3);
 int hashCode1 = baseSingleFieldPeriod1.hashCode();
 int hashCode2 = baseSingleFieldPeriod2.hashCode();
 assertEquals(hashCode1, hashCode2);
 BaseSingleFieldPeriod baseSingleFieldPeriod3 = createBaseSingleFieldPeriod(4);
 int hashCode3 = baseSingleFieldPeriod3.hashCode();
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(3);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(4);
 BaseSingleFieldPeriod baseSingleFieldPeriod3 = createBaseSingleFieldPeriod(3);
 int compareTo1 = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod1);
 assertEquals(0, compareTo1);
 int compareTo2 = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
 assertEquals(-1, compareTo2);
 int compareTo3 = baseSingleFieldPeriod2.compareTo(baseSingleFieldPeriod3);
 assertEquals(0, compareTo3);
 int compareTo4 = baseSingleFieldPeriod1.compareTo(null);
 assertNull(compareTo4);
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 Period period = baseSingleFieldPeriod.toPeriod();
 assertEquals(Period.ZERO.withFieldAdded(DurationFieldType.YEARS, 3), period);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
 assertEquals(Period.ZERO.withFieldAdded(DurationFieldType.YEARS, 3), mutablePeriod);
 }

 @Test
 public void testEquals_Null() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(3);
 assertFalse(baseSingleFieldPeriod.equals(null));
 }

 @Test
 public void testEquals_NotSameType() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(3);
 Period period1 = Period.ofYears(3);
 assertFalse(baseSingleFieldPeriod1.equals(period1));
 }
}

 class BaseSingleFieldPeriodForTest extends BaseSingleFieldPeriod {
 private BaseSingleFieldPeriodForTest(int iPeriod) {
 super(iPeriod);
 }
 }