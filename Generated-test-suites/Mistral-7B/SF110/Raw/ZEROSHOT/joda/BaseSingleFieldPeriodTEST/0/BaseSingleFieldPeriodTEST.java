package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.BasePeriodType;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
        // Test valid input
        final Instant instant = Instant.now();
        final Instant instant2 = Instant.parse("2022-01-01T00:00:01.000Z");
        final DurationFieldType durationFieldType = BaseDurationFieldType.YEARS;
        final int result = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
        assertEquals(1, result);

        // Test null input
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, instant2, durationFieldType));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(instant, null, durationFieldType));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(instant, instant2, null));
    }

    @Test
    public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod() {
        // Test valid input
        final LocalDate date = LocalDate.parse("2022-01-01");
        final LocalDate date2 = LocalDate.parse("2022-01-10");
        final LocalTime time = LocalTime.MIDNIGHT;
        final LocalDateTime dateTime = new LocalDateTime(date.toDate(), time);
        final LocalDateTime dateTime2 = new LocalDateTime(date2.toDate(), time);
        final ReadablePeriod readablePeriod = Period.days(5);
        final Chronology withUTC = DateTimeUtils.getChronology(date.getChronology()).withUTC();
        final int result = BaseSingleFieldPeriod.between(dateTime, dateTime2, readablePeriod);
        assertEquals(5, result);

        // Test null input
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, dateTime2, readablePeriod));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(dateTime, null, readablePeriod));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(dateTime, dateTime2, null));
    }

    @Test
    public void testStandardPeriodIn_ReadablePeriod_Long() {
        // Test valid input
        final Period period = Period.days(2).years(3);
        final long n = 2;
        final int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(7, result);

        // Test null input
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, n));
    }

    @Test
    public void testConstructor_Int() {
        // Test valid input
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertNotNull(period);
        assertEquals(10, period.getValue());

        // Test null input
        assertThrows(IllegalArgumentException.class, () -> new BaseSingleFieldPeriod(null));
    }

    @Test
    public void testGetValue() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return BaseDurationFieldType.YEARS;
            }
        };
        assertNotNull(period.getFieldType());
        assertEquals(BaseDurationFieldType.YEARS, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public PeriodType getPeriodType() {
                return BasePeriodType.standardInstance();
            }
        };
        assertNotNull(period.getPeriodType());
        assertEquals(BasePeriodType.standardInstance(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldType_Int() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType(int n) {
                return BaseDurationFieldType.YEARS;
            }
        };
        assertEquals(BaseDurationFieldType.YEARS, period.getFieldType(0));
        assertThrows(IndexOutOfBoundsException.class, () -> period.getFieldType(1));
    }

    @Test
    public void testGetValue_Int() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public int getValue(int n) {
                if (n != 0) {
                    throw new IndexOutOfBoundsException("Invalid index: " + n);
                }
                return 10;
            }
        };
        assertEquals(10, period.getValue(0));
        assertThrows(IndexOutOfBoundsException.class, () -> period.getValue(1));
    }

    @Test
    public void testGet_DurationFieldType() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public int get(DurationFieldType durationFieldType) {
                if (durationFieldType != BaseDurationFieldType.YEARS) {
                    return 0;
                }
                return 10;
            }
        };
        assertEquals(10, period.get(BaseDurationFieldType.YEARS));
        assertEquals(0, period.get(BaseDurationFieldType.MONTHS));
    }

    @Test
    public void testIsSupported_DurationFieldType() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public boolean isSupported(DurationFieldType durationFieldType) {
                if (durationFieldType == BaseDurationFieldType.YEARS) {
                    return true;
                }
                return false;
            }
        };
        assertTrue(period.isSupported(BaseDurationFieldType.YEARS));
        assertFalse(period.isSupported(BaseDurationFieldType.MONTHS));
    }

    @Test
    public void testToPeriod() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return BaseDurationFieldType.YEARS;
            }

            @Override
            public PeriodType getPeriodType() {
                return BasePeriodType.standardInstance();
            }
        };
        final Period expected = Period.years(10);
        final Period result = period.toPeriod();
        assertEquals(expected, result);
    }

    @Test
    public void testToMutablePeriod() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return BaseDurationFieldType.YEARS;
            }
        };
        final MutablePeriod mutablePeriod = period.toMutablePeriod();
        mutablePeriod.add(Period.years(5));
        assertEquals(Period.years(15), mutablePeriod.toPeriod());
    }

    @Test
    public void testEquals_Object() {
        final BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        final BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        final Object obj = new Object();
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(obj));
    }

    @Test
    public void testHashCode() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int expectedHashCode = 27 * (27 * 17 + 10) + BaseDurationFieldType.YEARS.hashCode();
        assertEquals(expectedHashCode, period.hashCode());
    }

    @Test
    public void testCompareTo_BaseSingleFieldPeriod() {
        final BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        final BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        final BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10);
        assertEquals(0, period1.compareTo(period3));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(-1, period1.compareTo(period2));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_Object() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.compareTo(new Object());
    }
}