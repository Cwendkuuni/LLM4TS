package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.field.AbstractLocalField;
import org.joda.time.field.LocalDateField;
import org.joda.time.field.LocalDateTimeField;
import org.joda.time.field.LocalTimeField;
import org.joda.time.util.FieldUtils;
import org.joda.time.util.ISOChronology;

import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod testPeriod;
    private BaseSingleFieldPeriod testPeriod2;
    private BaseSingleFieldPeriod testPeriod3;
    private Chronology chronology;
    private Instant instant;
    private Instant instant2;
    private ReadableInstant readableInstant;
    private ReadableInstant readableInstant2;
    private ReadablePartial readablePartial;
    private ReadablePeriod readablePeriod;
    private ReadablePeriod readablePeriod2;
    private DurationFieldType durationFieldType;
    private PeriodType periodType;

    @Before
    public void setUp() {
        testPeriod = new MySingleFieldPeriod(1);
        testPeriod2 = new MySingleFieldPeriod(2);
        testPeriod3 = new MySingleFieldPeriod(3);
        chronology = ISOChronology.getInstance();
        instant = new BaseDateTime(chronology, 2022, 1, 1, 0, 0, 0, 0).toInstant();
        instant2 = new BaseDateTime(chronology, 2022, 1, 1, 0, 0, 1, 0).toInstant();
        readableInstant = Instant.instantiate(chronology, 63072000000L);
        readableInstant2 = Instant.instantiate(chronology, 63072000010L);
        readablePartial = new LocalDate().withFieldAdded(LocalDateField.YEARS, 1).toLocalDateTime().withFieldAdded(LocalDateTimeField.HOUR_OF_DAY, 1).toLocalTime();
        readablePeriod = Period.withFields(new LocalDateField(DurationFieldType.YEARS, 1), new LocalDateTimeField(DurationFieldType.HOURS, 1), new LocalTimeField(DurationFieldType.MINUTES, 1));
        readablePeriod2 = Period.withFields(new LocalDateField(DurationFieldType.YEARS, -1), new LocalDateTimeField(DurationFieldType.HOURS, -1), new LocalTimeField(DurationFieldType.MINUTES, -1));
        durationFieldType = DurationFieldType.MINUTES;
        periodType = PeriodType.standard();
    }

    @Test
    public void testConstructor() {
        // Test default constructor
        assertNull(new BaseSingleFieldPeriod(0));

        // Test constructor with value
        assertEquals(1, new MySingleFieldPeriod(1).getValue());
    }

    @Test
    public void testGetValue() {
        assertEquals(1, testPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        testPeriod.setValue(2);
        assertEquals(2, testPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        assertNotNull(testPeriod.getFieldType());
        assertEquals(DurationFieldType.getInstance(MySingleFieldPeriod.FIELD_TYPE), testPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), testPeriod.getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, testPeriod.size());
    }

    @Test
    public void testGetFieldType_invalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> testPeriod.getFieldType(2));
    }

    @Test
    public void testGetValue_invalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> testPeriod.getValue(2));
    }

    @Test
    public void testGet_validIndex() {
        assertEquals(1, testPeriod.get(durationFieldType));
    }

    @Test
    public void testGet_invalidIndex() {
        assertNull(testPeriod.get(new DurationFieldType("invalidFieldType")));
    }

    @Test
    public void testIsSupported() {
        assertTrue(testPeriod.isSupported(durationFieldType));
        assertFalse(testPeriod.isSupported(new DurationFieldType("invalidFieldType")));
    }

    @Test
    public void testToPeriod() {
        Period expectedPeriod = Period.withFields(new LocalDateField(DurationFieldType.YEARS, 1), new LocalDateTimeField(DurationFieldType.HOURS, 0), new LocalTimeField(DurationFieldType.MINUTES, 0));
        Period actualPeriod = testPeriod.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod expectedPeriod = new MutablePeriod().withFields(new LocalDateField(DurationFieldType.YEARS, 1), new LocalDateTimeField(DurationFieldType.HOURS, 0), new LocalTimeField(DurationFieldType.MINUTES, 0));
        MutablePeriod actualPeriod = testPeriod.toMutablePeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    @Test
    public void testEquals() {
        MySingleFieldPeriod testPeriod4 = new MySingleFieldPeriod(1);
        assertTrue(testPeriod.equals(testPeriod));
        assertTrue(testPeriod.equals(testPeriod4));
    }

    @Test
    public void testEquals_null() {
        assertFalse(testPeriod.equals(null));
    }

    @Test
    public void testEquals_differentType() {
        assertFalse(testPeriod.equals(new Object()));
    }

    @Test
    public void testEquals_differentValue() {
        assertFalse(testPeriod.equals(testPeriod2));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 27 * (27 * 17 + 1) + durationFieldType.hashCode();
        int actualHashCode = testPeriod.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testCompareTo_samePeriod() {
        assertEquals(0, testPeriod.compareTo(testPeriod));
    }

    @Test
    public void testCompareTo_differentPeriodWithSameTypeAndSameValue() {
        assertEquals(0, testPeriod.compareTo(testPeriod2));
    }

    @Test
    public void testCompareTo_differentPeriodWithSameTypeButDifferentValue() {
        int expected = testPeriod.compareTo(testPeriod3);
        int actual = testPeriod2.compareTo(testPeriod3);
        assertEquals(expected, actual); // Test is commutative
    }

    @Test
    public void testCompareTo_differentPeriodWithDifferentType() {
        assertThrows(ClassCastException.class, () -> testPeriod.compareTo(testPeriod2));
    }

    @Test
    public void testCompareTo_null() {
        assertThrows(ClassCastException.class, () -> testPeriod.compareTo(null));
    }

    @Test
    public void testBetween() {
        // Test between(ReadableInstant, ReadableInstant, DurationFieldType)
        int expectedBetween1 = 60;
        int actualBetween1 = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
        assertEquals(expectedBetween1, actualBetween1);

        // Test between(ReadablePartial, ReadablePartial, ReadablePeriod)
        int expectedBetween2 = 1440;
        int actualBetween2 = BaseSingleFieldPeriod.between(readablePartial, readablePartial, readPeriod);
        assertEquals(expectedBetween2, actualBetween2);

        // Test error cases
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, readablePartial, durationFieldType));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, null, durationFieldType));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, readablePartial, null));
    }

    @Test
    public void testStandardPeriodIn() {
        // Test positive case
        int expectedStandardPeriodIn1 = 365;
        int actualStandardPeriodIn1 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 1);
        assertEquals(expectedStandardPeriodIn1, actualStandardPeriodIn1);

        // Test negative case
        int expectedStandardPeriodIn2 = -365;
        int actualStandardPeriodIn2 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod2, 1);
        assertEquals(expectedStandardPeriodIn2, actualStandardPeriodIn2);

        // Test zero case
        int expectedStandardPeriodIn3 = 0;
        int actualStandardPeriodIn3 = BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 1);
        assertEquals(expectedStandardPeriodIn3, actualStandardPeriodIn3);
    }

    private static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        public static final String FIELD_TYPE = "MyFieldType";

        public MySingleFieldPeriod(int value) {
            super(value);
        }

        @Override
        public DurationFieldType getFieldType() {
            return new AbstractLocalField() {
                @Override
                public String getName() {
                    return MySingleFieldPeriod.FIELD_TYPE;
                }

                @Override
                public int getPrecision() {
                    return DurationFieldType.MILLISECONDS.getPrecision();
                }

                @Override
                public long getUnitMillis() {
                    return DurationFieldType.MINUTES.getUnitMillis() * 60;
                }

                @Override
                public int getDifference(long value, long value2) {
                    return FieldUtils.safeToInt(FieldUtils.safeSubtract(FieldUtils.safeMultiply(this.getUnitMillis(), value), FieldUtils.safeMultiply(this.getUnitMillis(), value2)));
                }
            }.asDurationFieldType();
        }
    }
}