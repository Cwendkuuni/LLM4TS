package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod period1;
 private BaseSingleFieldPeriod period2;

 @Before
 public void setUp() {
 // Create instances of the BaseSingleFieldPeriod subclass for testing
 period1 = new DailyFields(1, ISOChronology.getInstanceUTC());
 period2 = new DailyFields(2, ISOChronology.getInstanceUTC());
 }

 @Test
 public void testGetFieldType() {
 // Test getFieldType() method
 DurationFieldType fieldType = period1.getFieldType();
 assertEquals(DurationFieldType.DAYS, fieldType);
 }

 @Test
 public void testGetPeriodType() {
 // Test getPeriodType() method
 PeriodType periodType = period1.getPeriodType();
 assertEquals(PeriodType.DAYS, periodType);
 }

 @Test
 public void testSize() {
 // Test size() method
 int size = period1.size();
 assertEquals(1, size);
 }

 @Test
 public void testGetFieldTypeAtIndex() {
 // Test getFieldType(int) method
 DurationFieldType fieldType = period1.getFieldType(0);
 assertEquals(DurationFieldType.DAYS, fieldType);
 }

 @Test
 public void testGetValueAtIndex() {
 // Test getValue(int) method
 int value = period1.getValue(0);
 assertEquals(1, value);
 }

 @Test
 public void testGetValue() {
 // Test getValue() method
 int value = period1.getValue();
 assertEquals(1, value);
 }

 @Test
 public void testSetValue() {
 // Test setValue(int) method
 period1.setValue(3);
 assertEquals(3, period1.getValue());
 }

 @Test
 public void testConstructionWithValue() {
 // Test construction with value
 period1 = new BaseSingleFieldPeriod(3);
 assertEquals(3, period1.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueNegativeIndex() {
 // Test getValue(-1) method with negative index
 period1.getValue(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeNegativeIndex() {
 // Test getFieldType(-1) method with negative index
 period1.getFieldType(-1);
 }

 @Test
 public void testEqualsObjectNull() {
 // Test equals(null) method
 assertFalse(period1.equals(null));
 }

 @Test
 public void testEqualsObjectDifferentType() {
 // Test equals(Object) method with different types
 assertFalse(period1.equals(new Integer(1)));
 }

 @Test
 public void testEqualsObjectSameInstance() {
 // Test equals(Object) method with same instance
 assertTrue(period1.equals(period1));
 }

 @Test
 public void testEqualsObjectSameValueSameType() {
 // Test equals(Object) method with same value and same type
 BaseSingleFieldPeriod period3 = new DailyFields(1, ISOChronology.getInstanceUTC());
 assertTrue(period1.equals(period3));
 }

 @Test
 public void testEqualsObjectSameValueDifferentType() {
 // Test equals(Object) method with same value but different types
 BaseSingleFieldPeriod period3 = new WeeklyFields(1, ISOChronology.getInstanceUTC());
 assertTrue(period1.equals(period3)); // Both have the same value and are comparable
 }

 @Test
 public void testEqualsObjectDifferentValueSameType() {
 // Test equals(Object) method with different values but same type
 BaseSingleFieldPeriod period3 = new DailyFields(2, ISOChronology.getInstanceUTC());
 assertFalse(period1.equals(period3));
 }

 @Test
 public void testEqualsObjectDifferentValueDifferentType() {
 // Test equals(Object) method with different values and different types
 BaseSingleFieldPeriod period3 = new WeeklyFields(2, ISOChronology.getInstanceUTC());
 assertFalse(period1.equals(period3));
 }

 @Test
 public void testHashCode() {
 // Test hashCode() method
 int hashCode1 = period1.hashCode();
 int hashCode2 = period1.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testHashCodeDifferentValues() {
 // Test hashCode() method with different values
 int hashCode1 = period1.hashCode();
 int hashCode2 = period2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testHashCodeSameValuesDifferentTypes() {
 // Test hashCode() method with same values but different types
 BaseSingleFieldPeriod period3 = new DailyFields(1, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period4 = new WeeklyFields(1, ISOChronology.getInstanceUTC());
 int hashCode1 = period1.hashCode();
 int hashCode2 = period3.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 int hashCode3 = period1.hashCode();
 int hashCode4 = period4.hashCode();
 assertNotEquals(hashCode3, hashCode4);
 }

 @Test
 public void testCompareToNull() {
 // Test compareTo(BaseSingleFieldPeriod) method with null
 assertNull(period1.compareTo(null));
 }

 @Test
 public void testCompareToSameInstance() {
 // Test compareTo(BaseSingleFieldPeriod) method with same instance
 int compareTo = period1.compareTo(period1);
 assertEquals(0, compareTo);
 }

 @Test
 public void testCompareToDifferentInstanceSameValues() {
 // Test compareTo(BaseSingleFieldPeriod) method with different instances and same values
 BaseSingleFieldPeriod period3 = new DailyFields(1, ISOChronology.getInstanceUTC());
 int compareTo = period1.compareTo(period3);
 assertEquals(0, compareTo);
 }

 @Test
 public void testCompareToDifferentInstancesDifferentValues() {
 // Test compareTo(BaseSingleFieldPeriod) method with different instances and different values
 BaseSingleFieldPeriod period3 = new DailyFields(2, ISOChronology.getInstanceUTC());
 int compareTo = period1.compareTo(period3);
 assertEquals(-1, compareTo);
 }

 @Test
 public void testCompareToDifferentInstancesSameValuesDifferentTypes() {
 // Test compareTo(BaseSingleFieldPeriod) method with different instances, same values but different types
 BaseSingleFieldPeriod period3 = new WeeklyFields(1, ISOChronology.getInstanceUTC());
 int compareTo = period1.compareTo(period3);
 assertEquals(0, compareTo);
 }

 @Test
 public void testCompareToDifferentInstancesDifferentValuesDifferentTypes() {
 // Test compareTo(BaseSingleFieldPeriod) method with different instances, different values but same types
 BaseSingleFieldPeriod period3 = new DailyFields(2, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period4 = new WeeklyFields(1, ISOChronology.getInstanceUTC());
 int compareTo = period1.compareTo(period4);
 assertEquals(-1, compareTo);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToDifferentTypes() {
 // Test compareTo(BaseSingleFieldPeriod) method with different types
 BaseSingleFieldPeriod period3 = new DailyFields(1, ISOChronology.getInstanceUTC());
 Object obj = new Integer(1);
 int compareTo = period1.compareTo(obj);
 }

 @Test
 public void testBetween() {
 // Test between(ReadableInstant, ReadableInstant, DurationFieldType) method
 ReadableInstant readableInstant = DateTime.parse("2022-01-01T00:00:00.000Z").toInstant();
 ReadableInstant readableInstant2 = DateTime.parse("2022-01-02T00:00:00.000Z").toInstant();
 int between = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.DAYS);
 assertEquals(1, between);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant() {
 // Test between(ReadableInstant, ReadableInstant, DurationFieldType) method with null readableInstant
 BaseSingleFieldPeriod.between(null, readableInstant2, DurationFieldType.DAYS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant2() {
 // Test between(ReadableInstant, ReadableInstant, DurationFieldType) method with null readableInstant2
 BaseSingleFieldPeriod.between(readableInstant, null, DurationFieldType.DAYS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenInvalidDurationFieldType() {
 // Test between(ReadableInstant, ReadableInstant, DurationFieldType) method with invalid DurationFieldType
 BaseSingleFieldPeriod.between(readableInstant, readableInstant2, null);
 }

 @Test
 public void testStandardPeriodIn() {
 // Test standardPeriodIn(ReadablePeriod, long) method
 Period period = new Period(1, DurationFieldType.DAYS, new DateTime("2022-01-01T00:00:00.000Z"));
 int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, 1000 * 60 * 60 * 24 * 30);
 assertEquals(30, standardPeriodIn);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInNullReadablePeriod() {
 // Test standardPeriodIn(ReadablePeriod, long) method with null readablePeriod
 BaseSingleFieldPeriod.standardPeriodIn(null, 1000 * 60 * 60 * 24 * 30);
 }
}