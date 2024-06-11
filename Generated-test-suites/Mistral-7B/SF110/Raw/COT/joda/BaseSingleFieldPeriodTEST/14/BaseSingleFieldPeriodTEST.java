package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod baseSingleFieldPeriod;
 private BaseSingleFieldPeriod baseSingleFieldPeriod2;

 @Test
 public void testBetweenMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(10);
 assertEquals(5, BaseSingleFieldPeriod.between(DateTime.now(), DateTime.now().plus(10, baseSingleFieldPeriod.getFieldType()), baseSingleFieldPeriod.getFieldType()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodNullArgument() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 try {
 BaseSingleFieldPeriod.between(null, DateTime.now(), baseSingleFieldPeriod.getFieldType());
 } catch (IllegalArgumentException e) {
 assertEquals("ReadableInstant objects must not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodNullSecondArgument() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 try {
 BaseSingleFieldPeriod.between(DateTime.now(), null, baseSingleFieldPeriod.getFieldType());
 } catch (IllegalArgumentException e) {
 assertEquals("ReadableInstant objects must not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodDifferentFieldTypes() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5, DateTimeUtils.UTC);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(10, DateTimeUtils.GREGORIAN);
 try {
 BaseSingleFieldPeriod.between(DateTime.now(), DateTime.now().plus(1, new Days()), baseSingleFieldPeriod.getFieldType());
 } catch (IllegalArgumentException e) {
 assertEquals("ReadablePeriod objects must have the same chronology", e.getMessage());
 }
 }

 @Test
 public void testBetweenMethodContiguousPeriods() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(10);
 assertEquals(5, BaseSingleFieldPeriod.between(DateTime.now(), DateTime.now().plus(15, baseSingleFieldPeriod.getFieldType()), baseSingleFieldPeriod.getFieldType()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodNonContiguousPeriods() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(2, 3, 4);
 try {
 BaseSingleFieldPeriod.between(DateTime.now(), DateTime.now().plus(15, baseSingleFieldPeriod.getFieldType()), baseSingleFieldPeriod2.getFieldType());
 } catch (IllegalArgumentException e) {
 assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
 }
 }

 @Test
 public void testStandardPeriodInMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(Period.withFields(new Days(1), new Hours(1)), 24 * 60 * 60 * 1000L));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodNullPeriod() {
 BaseSingleFieldPeriod.standardPeriodIn(null, 24 * 60 * 60 * 1000L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodNonContiguousPeriod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5, DateTimeUtils.UTC);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(2, DateTimeUtils.UTC);
 try {
 BaseSingleFieldPeriod.standardPeriodIn(Period.withFields(baseSingleFieldPeriod, new Days(1), new Hours(1)), 24 * 60 * 60 * 1000L);
 } catch (IllegalArgumentException e) {
 assertEquals("Period objects must be contiguous", e.getMessage());
 }
 }

 @Test
 public void testConstructionWithInteger() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 assertEquals(5, baseSingleFieldPeriod.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructionWithNegativeInteger() {
 try {
 new MySingleFieldPeriod(-5);
 } catch (IllegalArgumentException e) {
 assertEquals("The value must be non-negative", e.getMessage());
 }
 }

 @Test
 public void testGetValueMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 assertEquals(5, baseSingleFieldPeriod.getValue());
 }

 @Test
 public void testSetValueMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod.setValue(10);
 assertEquals(10, baseSingleFieldPeriod.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueMethodWithInvalidIndex() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 try {
 baseSingleFieldPeriod.getValue(1);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Index: 1, Size: 1", e.getMessage());
 }
 }

 @Test
 public void testGetFieldTypeMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 assertSame(Days.INSTANCE, baseSingleFieldPeriod.getFieldType());
 }

 @Test
 public void testGetPeriodTypeMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 assertSame(PeriodType.INSTANCE, baseSingleFieldPeriod.getPeriodType());
 }

 @Test
 public void testSizeMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 assertEquals(1, baseSingleFieldPeriod.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeMethodWithInvalidIndex() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 try {
 baseSingleFieldPeriod.getFieldType(1);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Index: 1, Size: 1", e.getMessage());
 }
 }

 @Test
 public void testGetValueMethodWithInvalidIndex() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 try {
 baseSingleFieldPeriod.getValue(1);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Index: 1, Size: 1", e.getMessage());
 }
 }

 @Test
 public void testEqualsMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(5);
 assertTrue(baseSingleFieldPeriod.equals(baseSingleFieldPeriod2));
 }

 @Test
 public void testHashCodeMethod() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 int hashCode = baseSingleFieldPeriod.hashCode();
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(5);
 assertEquals(hashCode, baseSingleFieldPeriod2.hashCode());
 }

 @Test
 public void testCompareToMethodSameObjects() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod));
 }

 @Test
 public void testCompareToMethodDifferentObjectsWithEqualValues() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(5);
 assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareToMethodDifferentObjectsWithDifferentValues() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(10);
 assertEquals(-5, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareToMethodSameClassDifferentTypes() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5, DateTimeUtils.UTC);
 baseSingleFieldPeriod2 = new MySingleFieldPeriod(5, DateTimeUtils.GREGORIAN);
 assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareToMethodDifferentClasses() {
 baseSingleFieldPeriod = new MySingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new Period();
 assertEquals(-1, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2));
 }

}

 class MySingleFieldPeriod extends BaseSingleFieldPeriod {
 MySingleFieldPeriod(int iPeriod) {
 super(iPeriod);
 }

 MySingleFieldPeriod(int iPeriod, Chronology chrono) {
 super(iPeriod);
 this.setChronology(chrono);
 }

 @Override
 public DurationFieldType getFieldType() {
 return Days.INSTANCE;
 }
 }