package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testPeriod;
 private ReadableInstant start;
 private ReadableInstant end;
 private ReadablePartial startPartial;
 private ReadablePartial endPartial;
 private ReadablePeriod testPeriod1;
 private ReadablePeriod testPeriod2;

 @Test
 public void testBetween() {
 // Test with valid ReadableInstant objects
 start = new DateTime(1972, 1, 1, 0, 0, 0, 0).toInstant();
 end = new DateTime(1972, 1, 2, 0, 0, 0, 0).toInstant();
 testPeriod = new BaseSingleFieldPeriod(5) {
 @Override
 public DurationFieldType getFieldType() {
 return new Days();
 }
 };
 int actual = BaseSingleFieldPeriod.between(start, end, new Days());
 int expected = 1;
 assertEquals("Expected 1 day between two instants", expected, actual);

 // Test with null ReadableInstant objects
 try {
 BaseSingleFieldPeriod.between(null, start, new Days());
 fail("Expected IllegalArgumentException with null start");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 BaseSingleFieldPeriod.between(start, null, new Days());
 fail("Expected IllegalArgumentException with null end");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetweenPartial() {
 // Test with valid ReadablePartial objects
 startPartial = new GeneralDateTime(1972, 1, 1, 0, 0, 0, 0).toLocalDateTime().toLocalDate().toLocalDateParts();
 endPartial = new GeneralDateTime(1972, 1, 2, 0, 0, 0, 0).toLocalDateTime().toLocalDate().toLocalDateParts();
 testPeriod = new BaseSingleFieldPeriod(5) {
 @Override
 public DurationFieldType getFieldType() {
 return new Days();
 }
 };
 int actual = BaseSingleFieldPeriod.between(startPartial, endPartial, testPeriod);
 int expected = 1;
 assertEquals("Expected 1 day between two partials", expected, actual);

 // Test with null ReadablePartial objects
 try {
 BaseSingleFieldPeriod.between(null, startPartial, testPeriod);
 fail("Expected IllegalArgumentException with null startPartial");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 BaseSingleFieldPeriod.between(startPartial, null, testPeriod);
 fail("Expected IllegalArgumentException with null endPartial");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStandardPeriodIn() {
 testPeriod1 = new Period(1, new Days(), 0, 0, 0, 0, 0);
 int actual = BaseSingleFieldPeriod.standardPeriodIn(testPeriod1, 7);
 int expected = 14; // 1 day * 7 days = 14 days
 assertEquals("Expected 14 days", expected, actual);
 }

 @Test
 public void testGetValue() {
 // Test with valid BaseSingleFieldPeriod instance
 testPeriod = new BaseSingleFieldPeriod(5);
 int actual = testPeriod.getValue();
 int expected = 5;
 assertEquals("Expected 5 as value", expected, actual);

 // Test with null BaseSingleFieldPeriod instance
 testPeriod = null;
 try {
 int value = testPeriod.getValue();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSetValue() {
 // Test with valid BaseSingleFieldPeriod instance
 testPeriod = new BaseSingleFieldPeriod(5);
 testPeriod.setValue(10);
 int actual = testPeriod.getValue();
 int expected = 10;
 assertEquals("Expected 10 as value after setting it", expected, actual);

 // Test with null BaseSingleFieldPeriod instance
 testPeriod = null;
 try {
 testPeriod.setValue(10);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testSize() {
 // Test with valid BaseSingleFieldPeriod instance
 testPeriod = new BaseSingleFieldPeriod(5);
 int actual = testPeriod.size();
 int expected = 1;
 assertEquals("Expected 1 as size", expected, actual);

 // Test with null BaseSingleFieldPeriod instance
 testPeriod = null;
 try {
 int size = testPeriod.size();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetFieldType() {
 // Test with valid BaseSingleFieldPeriod instance
 testPeriod = new BaseSingleFieldPeriod(5);
 DurationFieldType actual = testPeriod.getFieldType();
 DurationFieldType expected = new Days();
 assertEquals("Expected Days as field type", expected, actual);

 // Test with null BaseSingleFieldPeriod instance
 testPeriod = null;
 try {
 DurationFieldType fieldType = testPeriod.getFieldType();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetPeriodType() {
 // Test with valid BaseSingleFieldPeriod instance
 testPeriod = new BaseSingleFieldPeriod(5);
 PeriodType actual = testPeriod.getPeriodType();
 PeriodType expected = PeriodType.createInstance("P1D");
 assertEquals("Expected PeriodType P1D", expected, actual);

 // Test with null BaseSingleFieldPeriod instance
 testPeriod = null;
 try {
 PeriodType periodType = testPeriod.getPeriodType();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testEquals() {
 // Test with same instance
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod testPeriod2 = testPeriod;
 assertTrue("Expected true for same instance", testPeriod.equals(testPeriod2));

 // Test with null object
 testPeriod = new BaseSingleFieldPeriod(5);
 assertFalse("Expected false for null object", testPeriod.equals(null));

 // Test with different instance of same class
 testPeriod = new BaseSingleFieldPeriod(5);
 testPeriod2 = new BaseSingleFieldPeriod(7);
 assertFalse("Expected false for different instances", testPeriod.equals(testPeriod2));

 // Test with different instance of different class
 testPeriod = new BaseSingleFieldPeriod(5);
 Period testPeriod2 = new Period(5, new Days());
 assertFalse("Expected false for different classes", testPeriod.equals(testPeriod2));
 }

 @Test
 public void testHashCode() {
 // Test with same instance
 testPeriod = new BaseSingleFieldPeriod(5);
 int hashCode1 = testPeriod.hashCode();
 testPeriod = testPeriod;
 int hashCode2 = testPeriod.hashCode();
 assertEquals("Expected same hashCode for same instance", hashCode1, hashCode2);

 // Test with different instance of same class
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(7);
 int hashCode1 = testPeriod.hashCode();
 int hashCode2 = testPeriod2.hashCode();
 assertNotEquals("Expected different hashCodes for different instances", hashCode1, hashCode2);

 // Test with different instance of different class
 testPeriod = new BaseSingleFieldPeriod(5);
 Period testPeriod2 = new Period(5, new Days());
 int hashCode1 = testPeriod.hashCode();
 int hashCode2 = testPeriod2.hashCode();
 assertNotEquals("Expected different hashCodes for different classes", hashCode1, hashCode2);
 }

 @Test
 public void testCompareTo() {
 // Test with same instance
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod testPeriod2 = testPeriod;
 int actual = testPeriod.compareTo(testPeriod2);
 int expected = 0;
 assertEquals("Expected 0 for comparing same instances", expected, actual);

 // Test with different instances of same class with same value
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(5);
 int actual = testPeriod.compareTo(testPeriod2);
 int expected = 0;
 assertEquals("Expected 0 for comparing instances with same value", expected, actual);

 // Test with different instances of same class with different values
 testPeriod = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(7);
 int actual = testPeriod.compareTo(testPeriod2);
 int expected = -2; // since 5 < 7
 assertEquals("Expected -2 for comparing instances with different values", expected, actual);

 // Test with different instances of different classes with same values
 testPeriod = new BaseSingleFieldPeriod(5);
 Period testPeriod2 = new Period(5, new Days());
 int actual = testPeriod.compareTo(testPeriod2);
 int expected = -1; // since BaseSingleFieldPeriod is not equal to Period
 assertEquals("Expected -1 for comparing instances of different classes with same values", expected, actual);
}

}