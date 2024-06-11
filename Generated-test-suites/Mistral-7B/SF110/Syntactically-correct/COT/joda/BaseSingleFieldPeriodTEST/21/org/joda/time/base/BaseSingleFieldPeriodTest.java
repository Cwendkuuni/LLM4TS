package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenMethodWithValidArguments() {
        ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
        ReadableInstant readableInstant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISO.UTC);
        DurationFieldType durationFieldType = Seconds.getInstance();
        int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
        int expected = 86400;
        assertEquals(expected, result);
    }

    @Test
    public void testBetweenMethodWithNullArguments() {
        try {
            ReadableInstant readableInstant = null;
            ReadableInstant readableInstant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISO.UTC);
            DurationFieldType durationFieldType = Seconds.getInstance();
            int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
            ReadableInstant readableInstant2 = null;
            DurationFieldType durationFieldType = Seconds.getInstance();
            int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
            ReadableInstant readableInstant2 = readableInstant;
            DurationFieldType durationFieldType = Seconds.getInstance();
            int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
            ReadableInstant readableInstant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISO.UTC);
            DurationFieldType durationFieldType = null;
            int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBetweenMethodWithDifferentChronologies() {
        ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
        ReadableInstant readableInstant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISO.UTC);
        DurationFieldType durationFieldType = Seconds.getInstance();
        int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
        int expected = 86400;
        assertEquals(expected, result);

        ReadableInstant readableInstant3 = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeUtils.GREGORIAN_CHRONOLOGY);
        ReadableInstant readableInstant4 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeUtils.GREGORIAN_CHRONOLOGY);
        int result2 = BaseSingleFieldPeriod.between(readableInstant3, readableInstant4, durationFieldType);
        int expected2 = 86400 * 1000; // Convert to milliseconds for GregorianChronology
        assertEquals(expected2, result2);
    }

    @Test
    public void testBetweenMethodWithDifferentFields() {
        ReadablePartial readablePartial = new GeneralBasePeriod(1, Milliseconds.getInstance(), ISO.UTC);
        ReadablePartial readablePartial2 = new GeneralBasePeriod(1, Days.getInstance(), ISO.UTC);
        ReadablePeriod readablePeriod = Duration.standard(1, TimeUnit.MINUTES);

        try {
            int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial, readablePeriod);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod.plus(1, Minutes.getInstance()));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodInMethodWithValidArguments() {
        ReadablePeriod readablePeriod = Duration.standard(1, TimeUnit.DAYS);
        long n = 1000;
        int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
        int expected = 1;
        assertEquals(expected, result);

        ReadablePeriod readablePeriod2 = Period.standard(1, TimeUnit.HOURS, 36, Minutes.getInstance(), 30, Seconds.getInstance());
        long n2 = 2;
        int result2 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod2, n2);
        int expected2 = 36 + 30 / 60 + 1;
        assertEquals(expected2, result2);
    }

    @Test
    public void testStandardPeriodInMethodWithNullArguments() {
        try {
            ReadablePeriod readablePeriod = null;
            long n = 1000;
            int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodInMethodWithDifferentFields() {
        ReadablePeriod readablePeriod = Duration.standard(1, TimeUnit.DAYS);
        long n = 1000L * 365;
        int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
        int expected = 1;
        assertEquals(expected, result);

        ReadablePeriod readablePeriod2 = Period.standard(1, TimeUnit.HOURS, 36, Minutes.getInstance(), 30, Seconds.getInstance());
        long n2 = 2 * 24 * 60 * 60;
        int result2 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod2, n2);
        int expected2 = 36 + 30 / 60;
        assertEquals(expected2, result2);
    }

    @Test
    public void testConstructorWithValidArguments() {
        int iPeriod = 123;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(iPeriod);
        int expected = 123;
        assertEquals(expected, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testConstructorWithInvalidArguments() {
        try {
            BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(-123);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetFieldTypeMethod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
        assertEquals(Seconds.getInstance(), fieldType);
    }

    @Test
    public void testGetPeriodTypeMethod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
        assertEquals(Minutes.MINUTES, periodType);
    }

    @Test
    public void testSizeMethod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        int size = baseSingleFieldPeriod.size();
        assertEquals(1, size);
    }

    @Test
    public void testGetValueMethod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(123);
        int value = baseSingleFieldPeriod.getValue();
        assertEquals(123, value);
    }

    @Test
    public void testSetValueMethod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        baseSingleFieldPeriod.setValue(123);
        int value = baseSingleFieldPeriod.getValue();
        assertEquals(123, value);
    }

    @Test
    public void testGetMethodWithValidArguments() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        DurationFieldType durationFieldType = Seconds.getInstance();
        int result = baseSingleFieldPeriod.get(durationFieldType);
        int expected = 1;
        assertEquals(expected, result);
    }

    @Test
    public void testGetMethodWithInvalidArguments() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        DurationFieldType durationFieldType = Minutes.getInstance();
        int result = baseSingleFieldPeriod.get(durationFieldType);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testIsSupportedMethodWithValidArguments() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        DurationFieldType durationFieldType = Seconds.getInstance();
        boolean result = baseSingleFieldPeriod.isSupported(durationFieldType);
        boolean expected = true;
        assertTrue(result);
    }

    @Test
    public void testIsSupportedMethodWithInvalidArguments() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        DurationFieldType durationFieldType = Minutes.getInstance();
        boolean result = baseSingleFieldPeriod.isSupported(Minutes.HOURS);
        boolean expected = false;
        assertFalse(result);
    }

    @Test
    public void testToPeriodMethod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        Period result = baseSingleFieldPeriod.toPeriod();
        Period expected = Period.ONE;
        assertEquals(expected, result);
    }

    @Test
    public void testToMutablePeriodMethod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        Period expected = Period.ONE;
        assertEquals(expected, mutablePeriod.toPeriod());
    }

    @Test
    public void testEqualsMethodWithSameObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        boolean expected = true;
        assertTrue(result);
    }

    @Test
    public void testEqualsMethodWithDifferentObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        boolean expected = false;
        assertFalse(result);
    }

    @Test
    public void testEqualsMethodWithDifferentClasses() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
        Object otherObject = new Object();
        boolean result = baseSingleFieldPeriod.equals(otherObject);
        boolean expected = false;
        assertFalse(result);
    }

    @Test
    public void testHashCodeMethodWithSameObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
        int result = baseSingleFieldPeriod1.hashCode();
        int result2 = baseSingleFieldPeriod2.hashCode();
        int expected = result;
        assertEquals(expected, result2);
    }

    @Test
    public void testHashCodeMethodWithDifferentObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);
        int result = baseSingleFieldPeriod1.hashCode();
        int result2 = baseSingleFieldPeriod2.hashCode();
        int expected = result != result2 ? result : result2;
        assertNotEquals(expected, result2);
    }

    @Test
    public void testCompareToMethodWithSameObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethodWithDifferentObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        int expected = -1;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethodWithSameClassAndSameValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethodWithSameClassAndDifferentValues() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        int expected = -1;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethodWithDifferentClasses() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
        Object baseSingleFieldPeriod2 = new Object();
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        int expected = -1;
        assertEquals(expected, result);
    }
}