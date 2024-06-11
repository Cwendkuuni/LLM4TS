package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testPeriod;

 @SuppressWarnings("unchecked")
 @Test
 public void testBaseSingleFieldPeriod() {
 testPeriod = new BaseSingleFieldPeriod(5);
 assertEquals(5, testPeriod.getValue());
 DurationFieldType fieldType = testPeriod.getFieldType();
 assertEquals(DurationFieldType.DAYS, fieldType);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testBaseSingleFieldPeriodNegativeValue() {
 testPeriod = new BaseSingleFieldPeriod(-5);
 fail("Should not be able to create a BaseSingleFieldPeriod with a negative value.");
 }

 @Test
 public void testGetFieldType() {
 testPeriod = new BaseSingleFieldPeriod(5);
 DurationFieldType fieldType = testPeriod.getFieldType();
 assertEquals(DurationFieldType.DAYS, fieldType);
 }

 @Test
 public void testGetPeriodType() {
 testPeriod = new BaseSingleFieldPeriod(5);
 PeriodType periodType = testPeriod.getPeriodType();
 assertEquals(PeriodType.fixed(1, DurationFieldType.DAYS), periodType);
 }

 @Test
 public void testSize() {
 testPeriod = new BaseSingleFieldPeriod(5);
 int size = testPeriod.size();
 assertEquals(1, size);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeWrongIndex() {
 testPeriod = new BaseSingleFieldPeriod(5);
 testPeriod.getFieldType(2);
 fail("Should not be able to get field type at index 2.");
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWrongIndex() {
 testPeriod = new BaseSingleFieldPeriod(5);
 testPeriod.getValue(2);
 fail("Should not be able to get value at index 2.");
 }

 @Test
 public void testGetValue() {
 testPeriod = new BaseSingleFieldPeriod(5);
 int value = testPeriod.getValue();
 assertEquals(5, value);
 }

 @Test
 public void testGet() {
 testPeriod = new BaseSingleFieldPeriod(5);
 testPeriod.setChronology(ISOChronology.getInstance());
 DateTime baseDate = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstance());
 DateTime plusPeriod = baseDate.plus(testPeriod);
 int value = testPeriod.get(DurationFieldType.DAYS);
 int result = plusPeriod.getDayOfYear();
 assertEquals(value + 1, result);
 }

 @Test
 public void testEquals() {
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod anotherPeriod = new BaseSingleFieldPeriod(5);
 assertTrue(testPeriod.equals(anotherPeriod));
 }

 @Test
 public void testNotEquals() {
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod anotherPeriod = new BaseSingleFieldPeriod(3);
 assertFalse(testPeriod.equals(anotherPeriod));
 }

 @Test
 public void testHashCode() {
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod anotherPeriod = new BaseSingleFieldPeriod(5);
 int hashCode1 = testPeriod.hashCode();
 int hashCode2 = anotherPeriod.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testCompareToSameClass() {
 testPeriod = new BaseSingleFieldPeriod(3);
 BaseSingleFieldPeriod anotherPeriod = new BaseSingleFieldPeriod(3);
 int compareToResult = testPeriod.compareTo(anotherPeriod);
 assertEquals(0, compareToResult);
 }

 @Test
 public void testCompareToDifferentClass() {
 testPeriod = new BaseSingleFieldPeriod(3);
 BaseSingleFieldPeriodAnotherClass anotherPeriod = new BaseSingleFieldPeriodAnotherClass(3);
 int compareToResult = testPeriod.compareTo(anotherPeriod);
 assertEquals(-1, compareToResult);
 }

 @Test
 public void testIsSupported() {
 testPeriod = new BaseSingleFieldPeriod(3);
 DurationFieldType fieldType = DurationFieldType.DAYS;
 assertTrue(testPeriod.isSupported(fieldType));
 DurationFieldType fieldType2 = DurationFieldType.MILLIS;
 assertFalse(testPeriod.isSupported(fieldType2));
 }

 @Test
 public void testToPeriod() {
 testPeriod = new BaseSingleFieldPeriod(5);
 Period testPeriodObject = testPeriod.toPeriod();
 assertEquals(5, testPeriodObject.getYears());
 assertEquals(0, testPeriodObject.getMonths());
 assertEquals(0, testPeriodObject.getWeeks());
 assertEquals(5, testPeriodObject.getDays());
 assertEquals(0, testPeriodObject.getHours());
 assertEquals(0, testPeriodObject.getMinutes());
 assertEquals(0, testPeriodObject.getSeconds());
 assertEquals(0, testPeriodObject.getMillis());
 }

 @Test
 public void testToMutablePeriod() {
 testPeriod = new BaseSingleFieldPeriod(5);
 MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
 assertEquals(1, mutablePeriod.size());
 assertEquals(5, mutablePeriod.getValue(0));
 mutablePeriod.setValue(1, 3);
 assertEquals(3, mutablePeriod.getValue(0));
 mutablePeriod.setValue(1, 5);
 assertEquals(5, mutablePeriod.getValue(0));
 }
}

class BaseSingleFieldPeriodAnotherClass extends BaseSingleFieldPeriod {

 public BaseSingleFieldPeriodAnotherClass(final int iPeriod) {
 super(iPeriod);
 }

 @Override
 public DurationFieldType getFieldType() {
 throw new UnsupportedOperationException();
 }
}