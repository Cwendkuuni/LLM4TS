package org.joda.time.base;

import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.GeneralBasePeriod;
import org.joda.time.Period;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testgetValue() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        int expectedValue = 5;
        int actualValue = testPeriod.getValue();
        assertEquals(expectedValue, actualValue);
    }

    // Edge case: Test with a negative value
    @Test(expected = IllegalArgumentException.class)
    public void testNegativegetValue() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(-5);
    }

    @Test
    public void testgetFieldType() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        DurationFieldType expectedFieldType = DurationFieldType.days();
        DurationFieldType actualFieldType = testPeriod.getFieldType();
        assertEquals(expectedFieldType, actualFieldType);
    }

    // Test with valid ReadableInstant objects and DurationFieldType
    @Test
    public void testBetweenValid() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Instant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        DurationFieldType durationFieldType = DurationFieldType.days();
        int expectedResult = 1;
        int actualResult = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
        assertEquals(expectedResult, actualResult);
    }

    // Edge case: Test with null ReadableInstant objects
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadableInstant() {
        Instant instant = null;
        Instant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        DurationFieldType durationFieldType = DurationFieldType.days();
        int actualResult = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
    }

    // Test with valid ReadablePartial and ReadablePeriod objects
    @Test
    public void testBetweenValid() {
        ReadablePartial readablePartial = new GeneralBasePeriod(2, DurationFieldType.days(), DateTimeZone.UTC);
        ReadablePartial readablePartial2 = new GeneralBasePeriod(1, DurationFieldType.days(), DateTimeZone.UTC);
        ReadablePeriod readablePeriod = Period.ONE.withFields(DurationFieldType.days(), 2);
        int expectedResult = 1;
        int actualResult = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
        assertEquals(expectedResult, actualResult);
    }

    // Edge case: Test with null ReadablePartial objects
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadablePartial() {
        ReadablePartial readablePartial = null;
        ReadablePartial readablePartial2 = new GeneralBasePeriod(1, DurationFieldType.days(), DateTimeZone.UTC);
        ReadablePeriod readablePeriod = Period.ONE.withFields(DurationFieldType.days(), 2);
        int actualResult = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }

    // Test with valid DurationFieldType
    @Test
    public void testGetValid() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        DurationFieldType durationFieldType = DurationFieldType.days();
        int expectedValue = 5;
        int actualValue = testPeriod.get(durationFieldType);
        assertEquals(expectedValue, actualValue);
    }

    // Edge case: Test with an invalid DurationFieldType
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalid() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        DurationFieldType durationFieldType = new DurationFieldType() {}; // Invalid DurationFieldType
        int actualValue = testPeriod.get(durationFieldType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        int expectedSize = 1;
        int actualSize = testPeriod.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetValidFieldValue() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        DurationFieldType durationFieldType = DurationFieldType.days();
        int expectedValue = 5;
        int actualValue = testPeriod.get(durationFieldType);
        assertEquals(expectedValue, actualValue);
    }

    // Edge case: Test get with an invalid DurationFieldType
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidFieldValue() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        DurationFieldType durationFieldType = new DurationFieldType() {}; // Invalid DurationFieldType
        int actualValue = testPeriod.get(durationFieldType);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod testPeriod1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod2 = new MySingleFieldPeriod(5);
        assertTrue(testPeriod1.equals(testPeriod2));
    }

    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod testPeriod1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod2 = new MySingleFieldPeriod(3);
        assertFalse(testPeriod1.equals(testPeriod2));
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        int expectedResult = 0;
        int actualResult = testPeriod.compareTo(new BaseSingleFieldPeriod(0));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCompareToNegative() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        int expectedResult = 1;
        int actualResult = testPeriod.compareTo(new BaseSingleFieldPeriod(-1));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCompareToPositive() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        int expectedResult = 1;
        int actualResult = testPeriod.compareTo(new BaseSingleFieldPeriod(1));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToString() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        String expectedToString = "P5D";
        String actualToString = testPeriod.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testAdd() {
        BaseSingleFieldPeriod testPeriod1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod2 = new MySingleFieldPeriod(3);
        BaseSingleFieldPeriod expectedTestPeriod = new MySingleFieldPeriod(8);
        BaseSingleFieldPeriod actualTestPeriod = testPeriod1.add(testPeriod2);
        assertEquals(expectedTestPeriod, actualTestPeriod);
    }

    @Test
    public void testAddNegative() {
        BaseSingleFieldPeriod testPeriod1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod2 = new MySingleFieldPeriod(-3);
        BaseSingleFieldPeriod expectedTestPeriod = new MySingleFieldPeriod(2);
        BaseSingleFieldPeriod actualTestPeriod = testPeriod1.add(testPeriod2);
        assertEquals(expectedTestPeriod, actualTestPeriod);
    }
}