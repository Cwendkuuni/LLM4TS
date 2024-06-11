package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

import java.io.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() throws Exception {
        // Test between methods with ReadableInstant arguments
        ISODateTime instance1 = new ISODateTime(2021, 1, 1, 0, 0, 0, 0, ISODateTime.UTC);
        ISODateTime instance2 = new ISODateTime(2021, 1, 2, 0, 0, 0, 0, ISODateTime.UTC);
        int result1 = BaseSingleFieldPeriod.between(instance1, instance2, DurationFieldType.HOURS);
        int expected1 = 24;
        assertEquals(expected1, result1);

        // Test between methods with ReadablePartial arguments
        DateTime dt1 = new DateTime(2021, 1, 1, 0, 0, 0, 0, ISODateUtils.UTC);
        DateTime dt2 = new DateTime(2021, 1, 2, 0, 0, 0, 0, ISODateUtils.UTC);
        LocalDate ld1 = dt1.toLocalDate();
        LocalDate ld2 = dt2.toLocalDate();
        LocalDateTime ldt1 = dt1.toLocalDateTime();
        LocalDateTime ldt2 = dt2.toLocalDateTime();
        Period p = new Period(DurationFieldType.DAYS, 1, null);
        ReadablePartial readablePartial1 = new DateTime(ld1.toDate().getTime()).with(p);
        ReadablePartial readablePartial2 = new DateTime(ld2.toDate().getTime()).with(p);
        int result2 = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, new Duration(1, TimeUnit.HOURS).toPeriod());
        int expected2 = 24;
        assertEquals(expected2, result2);

        // Test between methods with null arguments
        try {
            BaseSingleFieldPeriod.between(null, null, DurationFieldType.HOURS);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(instance1, null, DurationFieldType.HOURS);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(instance1, instance2, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodIn() throws Exception {
        // Test standardPeriodIn method with ReadablePeriod argument
        Period period = new Period(DurationFieldType.DAYS, 3, null);
        long n = 2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        int expected = 6;
        assertEquals(expected, result);

        // Test standardPeriodIn method with null argument
        Period nullPeriod = null;
        try {
            BaseSingleFieldPeriod.standardPeriodIn(nullPeriod, 2);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructors() throws Exception {
        // Test default constructor
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod();
        assertEquals(0, period1.getValue());
        DurationFieldType expectedFieldType = DurationFieldType.INSTANT;
        assertEquals(expectedFieldType, period1.getFieldType());

        // Test constructor with int argument
        int value = 42;
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(value);
        assertEquals(value, period2.getValue());
        assertEquals(DurationFieldType.INSTANT, period2.getFieldType());
    }

    @Test
    public void testGetters() throws Exception {
        // Test getValue method
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        int expected = 42;
        int actual = period.getValue();
        assertEquals(expected, actual);

        // Test setValue method
        period.setValue(21);
        expected = 21;
        actual = period.getValue();
        assertEquals(expected, actual);

        // Test getFieldType method
        DurationFieldType expectedFieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod periodWithFieldType = new BaseSingleFieldPeriod(42) {
            @Override
            public DurationFieldType getFieldType() {
                return expectedFieldType;
            }
        };
        assertEquals(expectedFieldType, periodWithFieldType.getFieldType());

        // Test getPeriodType method
        PeriodType expectedPeriodType = PeriodType.standardInstants();
        assertEquals(expectedPeriodType, period.getPeriodType());

        // Test size method
        int expectedSize = 1;
        int actualSize = period.size();
        assertEquals(expectedSize, actualSize);

        // Test getFieldType and getValue methods with invalid index
        try {
            period.getFieldType(2);
            fail("IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            period.getValue(2);
            fail("IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() throws Exception {
        // Test equals method with same instance
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        assertTrue(period1.equals(period1));

        // Test equals method with null object
        assertFalse(period1.equals(null));

        // Test equals method with object of different class
        Object obj = new Object();
        assertFalse(period1.equals(obj));

        // Test equals method with object of same class but different value
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(21);
        assertFalse(period1.equals(period2));

        // Test equals method with object of same class but same value
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(42);
        assertTrue(period1.equals(period3));
    }

    @Test
    public void testHashCode() throws Exception {
        // Test hashCode method with same instance
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        int expectedHashCode1 = 12345; // Arbitrary hash code value
        int actualHashCode1 = period1.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);

        // Test hashCode method with different instance but same value
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42);
        int expectedHashCode2 = 54321; // Arbitrary hash code value
        int actualHashCode2 = period2.hashCode();
        assertEquals(expectedHashCode2, actualHashCode2);

        // Test hashCode method with different instances and different values
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(21);
        int expectedHashCode3 = 67890; // Arbitrary hash code value
        int actualHashCode3 = period3.hashCode();
        assertNotEqual(expectedHashCode1, actualHashCode3);

        // Test hashCode method with null object
        assertNotEqual(period1.hashCode(), null.hashCode());
    }

    @Test
    public void testCompareTo() throws Exception {
        // Test compareTo method with same instance
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        int expected = 0;
        int actual = period1.compareTo(period1);
        assertEquals(expected, actual);

        // Test compareTo method with different instances but same value
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42);
        expected = 0;
        actual = period1.compareTo(period2);
        assertEquals(expected, actual);

        // Test compareTo method with different instances and different values
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(21);
        int expectedNegative = -1;
        int actualNegative = period1.compareTo(period3);
        assertEquals(expectedNegative, actualNegative);

        int expectedPositive = 1;
        int actualPositive = period3.compareTo(period1);
        assertEquals(expectedPositive, actualPositive);

        // Test compareTo method with instances of different classes
        BaseSingleFieldPeriod period4 = new Object() {
            @Override
            public int compareTo(BaseSingleFieldPeriod o) {
                return 42;
            }
        };
        int expectedDifferentClasses = Integer.MIN_VALUE;
        int actualDifferentClasses = period1.compareTo(period4);
        assertEquals(expectedDifferentClasses, actualDifferentClasses);
    }
}