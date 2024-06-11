package org.joda.time.base;

import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createInstance(int value) {
 return new BaseSingleFieldPeriod(value) {
 @Override
 public DurationFieldType getFieldType() {
 return DurationFieldType.DAYS;
 }
 };
 }

 @Test
 public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
 ReadableInstant readableInstant = new DateTime(2022, 1, 1, 12, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant readableInstant2 = new DateTime(2022, 1, 2, 12, 0, 0, 0, ISOChronology.getInstanceUTC());
 int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.DAYS);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstant_Null_DurationFieldType() {
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(new DateTime(2022, 1, 1, 12, 0, 0, 0, ISOChronology.getInstanceUTC()), null, DurationFieldType.DAYS));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_Null_ReadableInstant_DurationFieldType() {
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, new DateTime(2022, 1, 1, 12, 0, 0, 0, ISOChronology.getInstanceUTC()), DurationFieldType.DAYS));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_Null_ReadablePeriod() {
 ReadablePartial readablePartial = new GeneralBaseDateTime(123456789000L, DateTimeUtils.getUTC());
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, null, new MutablePeriod()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_Null_ReadablePartial_ReadablePeriod() {
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, new GeneralBaseDateTime(123456789000L, DateTimeUtils.getUTC()), new MutablePeriod()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_ReadablePartial_Null() {
 ReadablePartial readablePartial = new GeneralBaseDateTime(123456789000L, DateTimeUtils.getUTC());
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, new GeneralBaseDateTime(123456789000L, DateTimeUtils.getUTC()), null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_ReadablePartial_InvalidChronology() {
 ReadablePartial readablePartial = new GeneralBaseDateTime(123456789000L, DateTimeUtils.getChronology(new Locale("foo")));
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, new GeneralBaseDateTime(123456789000L, DateTimeUtils.getUTC()), new MutablePeriod()));
 }

 @Test
 public void testStandardPeriodIn_ReadablePeriod_Long() {
 ReadablePeriod readablePeriod = new Period(1, DurationFieldType.DAYS, 1, DurationFieldType.MONTHS, 1, DurationFieldType.YEARS);
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 2);
 assertEquals(366, result); // Assuming a non-leap year
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodIn_Null_ReadablePeriod() {
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(null, 2));
 }

 @Test
 public void testCreateInstance() {
 BaseSingleFieldPeriod instance = createInstance(1);
 assertEquals(1, instance.getValue());
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod instance = createInstance(5);
 assertEquals(5, instance.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValue_NegativeIndex() {
 BaseSingleFieldPeriod instance = createInstance(5);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.getValue(-1));
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod instance = createInstance(5);
 instance.setValue(3);
 assertEquals(3, instance.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetValue_NegativeIndex() {
 BaseSingleFieldPeriod instance = createInstance(5);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.setValue(-1));
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(1) {
 @Override
 public DurationFieldType getFieldType() {
 return null;
 }
 };
 assertEquals(1, instance.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldType_NegativeIndex() {
 BaseSingleFieldPeriod instance = createInstance(1);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.getFieldType(-1));
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod instance = createInstance(1);
 DurationFieldType expected = DurationFieldType.DAYS;
 DurationFieldType actual = instance.getFieldType();
 assertEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported_UnsupportedDurationFieldType() {
 BaseSingleFieldPeriod instance = createInstance(1);
 assertThrows(UnsupportedOperationException.class, () -> instance.isSupported(DurationFieldType.HOURS));
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod instance = createInstance(1);
 Period expected = new Period(1, DurationFieldType.DAYS);
 Period actual = instance.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod instance = createInstance(1);
 MutablePeriod expected = new MutablePeriod().set(1, DurationFieldType.DAYS);
 MutablePeriod actual = instance.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals_SameInstance() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 assertEquals(instance1, instance1);
 }

 @Test
 public void testEquals_DifferentInstanceSameValue() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 BaseSingleFieldPeriod instance2 = createInstance(1);
 assertEquals(instance1, instance2);
 }

 @Test
 public void testEquals_DifferentInstanceDifferentValue() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 BaseSingleFieldPeriod instance2 = createInstance(2);
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testHashCode_SameInstance() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 int expected = 27 * (27 * 17 + 1) + DurationFieldType.DAYS.hashCode();
 int actual = instance1.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCode_DifferentInstanceSameValue() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 BaseSingleFieldPeriod instance2 = createInstance(1);
 int expected = 27 * (27 * 17 + 1) + DurationFieldType.DAYS.hashCode();
 int actual = instance1.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCode_DifferentInstanceDifferentValue() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 BaseSingleFieldPeriod instance2 = createInstance(2);
 int expected1 = 27 * (27 * 17 + 1) + DurationFieldType.DAYS.hashCode();
 int expected2 = 27 * (27 * 17 + 2) + DurationFieldType.DAYS.hashCode();
 int actual = instance1.hashCode();
 assertEquals(expected1, actual);
 assertEquals(expected2, instance2.hashCode());
 }

 @Test
 public void testCompareTo_SameInstance() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 int expected = 0;
 int actual = instance1.compareTo(instance1);
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareTo_DifferentInstanceSameValue() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 BaseSingleFieldPeriod instance2 = createInstance(1);
 int expected = 0;
 int actual = instance1.compareTo(instance2);
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareTo_DifferentInstanceDifferentValue() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 BaseSingleFieldPeriod instance2 = createInstance(2);
 int expected = -1;
 int actual = instance1.compareTo(instance2);
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareTo_SameValueClassDifferentSubclass() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 BaseSingleFieldPeriod instance2 = new BasePeriod(1);
 int expected = 0;
 int actual = instance1.compareTo(instance2);
 assertEquals(expected, actual);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareTo_DifferentClass() {
 BaseSingleFieldPeriod instance1 = createInstance(1);
 Object instance2 = new Object();
 int actual = instance1.compareTo(instance2);
 assertNull(actual);
 assertThrows(ClassCastException.class, () -> instance1.compareTo(instance2));
 }
}