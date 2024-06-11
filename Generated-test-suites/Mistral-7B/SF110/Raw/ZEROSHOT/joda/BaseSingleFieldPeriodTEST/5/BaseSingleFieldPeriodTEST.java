package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createPeriod(int value) {
 return new BaseSingleFieldPeriodForTest(value);
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod period = createPeriod(5);
 assertEquals("Should return the correct field type", DurationFieldType.DAYS, period.getFieldType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetFieldTypeThrowsExceptionWhenNotSubclass() {
 BaseSingleFieldPeriod period = createPeriod(5);
 try {
 period.getClass().getDeclaredMethod("getFieldType2");
 fail("Should not be able to access non-existent method");
 } catch (NoSuchMethodException e) {
 // Expected
 }

 try {
 period.getFieldType2();
 fail("Should throw UnsupportedOperationException when trying to call non-existent method");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod period = createPeriod(5);
 assertEquals("Should return the correct period type", PeriodType.INSTANCE, period.getPeriodType());
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod period = createPeriod(5);
 assertEquals("Size should be 1", 1, period.size());
 }

 @Test
 public void testGetFieldTypeAt() {
 BaseSingleFieldPeriod period = createPeriod(5);
 assertEquals("Should return the correct field type", DurationFieldType.DAYS, period.getFieldType(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeAtThrowsException() {
 BaseSingleFieldPeriod period = createPeriod(5);
 try {
 period.getFieldType(1);
 fail("Should throw IndexOutOfBoundsException when trying to get non-existent field type");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod period = createPeriod(5);
 assertEquals("Value should be 5", 5, period.getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetValueThrowsException() {
 BaseSingleFieldPeriod period = createPeriod(5);
 try {
 period.setValue(3);
 fail("Should throw UnsupportedOperationException when trying to set value");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod period = createPeriod(5);
 Period expected = new Period(DurationFieldType.DAYS, 5);
 Period actual = period.toPeriod();
 assertEquals("Should return the correct period", expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod period = createPeriod(5);
 MutablePeriod expected = new MutablePeriod().withFields(new BasicFieldSet(DurationFieldType.DAYS, 5));
 MutablePeriod actual = period.toMutablePeriod();
 assertEquals("Should return the correct mutable period", expected, actual);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = createPeriod(5);
 BaseSingleFieldPeriod period2 = createPeriod(5);
 assertTrue("Should be equal", period1.equals(period2));

 BaseSingleFieldPeriod period3 = createPeriod(3);
 assertFalse("Should not be equal", period1.equals(period3));

 Object obj = new Object();
 assertFalse("Should not be equal", period1.equals(obj));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod period1 = createPeriod(5);
 BaseSingleFieldPeriod period2 = createPeriod(5);
 int hashCode1 = period1.hashCode();
 int hashCode2 = period2.hashCode();
 assertEquals("Hash codes should be equal", hashCode1, hashCode2);

 BaseSingleFieldPeriod period3 = createPeriod(3);
 int hashCode3 = period3.hashCode();
 assertNotEquals("Hash codes should not be equal", hashCode1, hashCode3);
 }

 @Test
 public void testCompareToZero() {
 BaseSingleFieldPeriod period1 = createPeriod(5);
 int comparison = period1.compareTo(BaseSingleFieldPeriod.ZERO);
 assertEquals("Comparison with zero should be positive", 1, comparison);
 }

 @Test
 public void testCompareToNegative() {
 BaseSingleFieldPeriod period1 = createPeriod(-5);
 int comparison = period1.compareTo(BaseSingleFieldPeriod.ZERO);
 assertEquals("Comparison with zero should be negative", -1, comparison);
 }

 @Test
 public void testCompareToEqual() {
 BaseSingleFieldPeriod period1 = createPeriod(5);
 BaseSingleFieldPeriod period2 = createPeriod(5);
 int comparison = period1.compareTo(period2);
 assertEquals("Comparisons with equal periods should be 0", 0, comparison);
 }

 @Test
 public void testCompareToDifferent() {
 BaseSingleFieldPeriod period1 = createPeriod(5);
 BaseSingleFieldPeriod period2 = createPeriod(3);
 int comparison = period1.compareTo(period2);
 assertTrue("Comparison with different periods should be positive", comparison > 0);
 }

 @Test
 public void testCompareToDifferentSign() {
 BaseSingleFieldPeriod period1 = createPeriod(-5);
 BaseSingleFieldPeriod period2 = createPeriod(3);
 int comparison = period1.compareTo(period2);
 assertTrue("Comparison with different signs and periods should be negative", comparison < 0);
 }
}

 class BaseSingleFieldPeriodForTest extends BaseSingleFieldPeriod {
 private static final long serialVersionUID = 8325731334884L;

 BaseSingleFieldPeriodForTest(int value) {
 super(value);
 }

 // Non-existent method to test UnsupportedOperationException
 public int getFieldType2() {
 return 0;
 }
}