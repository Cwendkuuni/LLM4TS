package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private static final int TEST_VALUE = 5;

 @Test
 public void testConstructor() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(TEST_VALUE);
 assertEquals(TEST_VALUE, period.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNegativeValue() {
 new BaseSingleFieldPeriod(-1);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType fieldType = BaseSingleFieldPeriod.class.getDeclaredField("iPeriod").get(new BaseSingleFieldPeriod(TEST_VALUE)).getFieldType();
 assertEquals(DurationFieldType.HOURS, fieldType);
 }

 @Test
 public void testGetPeriodType() {
 PeriodType periodType = new BaseSingleFieldPeriod(TEST_VALUE).getPeriodType();
 assertEquals(PeriodType.HOURS, periodType);
 }

 @Test
 public void testSize() {
 assertEquals(1, new BaseSingleFieldPeriod(TEST_VALUE).size());
 }

 @Test
 public void testGetValue() {
 assertEquals(TEST_VALUE, new BaseSingleFieldPeriod(TEST_VALUE).getValue());
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 period.setValue(TEST_VALUE);
 assertEquals(TEST_VALUE, period.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueOutOfBounds() {
 new BaseSingleFieldPeriod(TEST_VALUE).getValue(1);
 }

 @Test
 public void testGet() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(TEST_VALUE);
 DurationField field = DurationFieldType.HOURS.getField(ISOChronology.getInstanceUTC());
 assertEquals(FieldUtils.safeMultiply(FieldUtils.safeToLong(TEST_VALUE), field.getUnitMillis()), period.get(field));
 }

 @Test
 public void testIsSupported() {
 DurationFieldType fieldType = DurationFieldType.HOURS;
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(TEST_VALUE);
 assertTrue(period.isSupported(fieldType));
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToDifferentClass() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE);
 Object period2 = new Object();
 period1.compareTo(period2);
 }

 @Test
 public void testCompareToEqual() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(TEST_VALUE);
 assertEquals(0, period1.compareTo(period2));
 }

 @Test
 public void testCompareToGreater() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE + 1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(TEST_VALUE);
 assertTrue(period1.compareTo(period2) > 0);
 }

 @Test
 public void testCompareToLess() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(TEST_VALUE + 1);
 assertTrue(period1.compareTo(period2) < 0);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(TEST_VALUE);
 assertTrue(period1.equals(period2));
 }

 @Test
 public void testNotEquals() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(TEST_VALUE + 1);
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(TEST_VALUE);
 assertEquals(period1.hashCode(), period2.hashCode());
 }

 @Test
 public void testHashCodeDifferentValues() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(TEST_VALUE);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(TEST_VALUE + 1);
 assertNotEquals(period1.hashCode(), period2.hashCode());
 }
}