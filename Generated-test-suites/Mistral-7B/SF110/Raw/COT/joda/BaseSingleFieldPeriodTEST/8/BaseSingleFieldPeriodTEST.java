package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.BasePeriodType;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadOnlyDateTime;
import org.joda.time.ReadOnlyField;
import org.joda.time.ReadOnlyFieldSet;
import org.joda.time.YearMonth;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class BaseSingleFieldPeriodTest {

    @org.junit.Test
    public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.DATE_UTC);
        ReadableInstant instant = new Instant(63072000000L, chronology);
        ReadableInstant instant2 = new Instant(63072100000L, chronology);
        DurationFieldType durationFieldType = BaseDurationFieldType.days();

        int result = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
        assertEquals(1, result);
    }

    @org.junit.Test
    public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType_NullReadableInstant() {
        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.DATE_UTC);
        ReadableInstant instant = new Instant(63072000000L, chronology);
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(instant, null, BaseDurationFieldType.days()));
    }

    @org.junit.Test
    public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType_NullDurationFieldType() {
        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.DATE_UTC);
        ReadableInstant instant = new Instant(63072000000L, chronology);
        ReadableInstant instant2 = new Instant(63072100000L, chronology);
        assertThrows(NullPointerException.class, () -> BaseSingleFieldPeriod.between(instant, instant2, null));
    }

    @org.junit.Test
    public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod() {
        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.DATE_UTC);
        LocalDate date = new LocalDate(2023, 1, 1, chronology);
        LocalDate date2 = new LocalDate(2023, 1, 2, chronology);
        LocalDateTime dateTime = new LocalDateTime(2023, 1, 1, 12, 0, 0, chronology);
        LocalDateTime dateTime2 = new LocalDateTime(2023, 1, 2, 12, 0, 0, chronology);
        ReadablePeriod readablePeriod = Period.days(2);

        int result = BaseSingleFieldPeriod.between(date, date2, readablePeriod);
        assertEquals(2, result);

        result = BaseSingleFieldPeriod.between(dateTime, dateTime2, readablePeriod);
        assertEquals(0, result);
    }

    @org.junit.Test
    public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod_NullReadablePartial() {
        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.DATE_UTC);
        LocalDate date = new LocalDate(2023, 1, 1, chronology);
        LocalDate date2 = new LocalDate(2023, 1, 2, chronology);
        ReadablePeriod readablePeriod = Period.days(2);

        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, date2, readablePeriod));
    }

    @org.junit.Test
    public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod_NullReadablePeriod() {
        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.DATE_UTC);
        LocalDate date = new LocalDate(2023, 1, 1, chronology);
        LocalDate date2 = new LocalDate(2023, 1, 2, chronology);

        assertThrows(NullPointerException.class, () -> BaseSingleFieldPeriod.between(date, date2, null));
    }

    @org.junit.Test
    public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod_NonContiguous() {
        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.DATE_UTC);
        LocalDate date = new LocalDate(2023, 1, 1, chronology);
        LocalDate date2 = new LocalDate(2023, 1, 3, chronology);
        ReadablePeriod readablePeriod = Period.days(2);

        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(date, date2, readablePeriod));
    }

    @org.junit.Test
    public void testStandardPeriodIn_ReadablePeriod_Long() {
        ISOChronology instanceUTC = ISOChronology.getInstanceUTC();
        Period period = Period.days(3).plus(Period.weeks(1));

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 7);
        assertEquals(1, result);
    }

    @org.junit.Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 7);
        assertEquals(0, result);
    }

    @org.junit.Test
    public void testConstructor_Int() {
        int period = 5;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(period);
        assertEquals(period, baseSingleFieldPeriod.getValue());
    }

    @org.junit.Test
    public void testGetFieldType_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {
            @Override
            public DurationFieldType getFieldType() {
                return BaseDurationFieldType.days();
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return periodType;
            }
        };

        DurationFieldType result = baseSingleFieldPeriod.getFieldType();
        assertEquals(BaseDurationFieldType.days(), result);
    }

    @org.junit.Test
    public void testGetPeriodType_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        PeriodType result = baseSingleFieldPeriod.getPeriodType();
        assertEquals(PeriodType.days(), result);
    }

    @org.junit.Test
    public void testSize_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {};
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        int result = baseSingleFieldPeriod.size();
        assertEquals(1, result);
    }

    @org.junit.Test
    public void testGetFieldType_BaseSingleFieldPeriod_InvalidIndex() {
        BasePeriodType periodType = new BasePeriodType() {};
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        assertThrows(IndexOutOfBoundsException.class, () -> baseSingleFieldPeriod.getFieldType(2));
    }

    @org.junit.Test
    public void testGetValue_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {};
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        int result = baseSingleFieldPeriod.getValue();
        assertEquals(5, result);
    }

    @org.junit.Test
    public void testSetValue_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {};
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        baseSingleFieldPeriod.setValue(10);
        int result = baseSingleFieldPeriod.getValue();
        assertEquals(10, result);
    }

    @org.junit.Test
    public void testToPeriod_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        Period result = baseSingleFieldPeriod.toPeriod();
        assertEquals(Period.days(5), result);
    }

    @org.junit.Test
    public void testToMutablePeriod_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        mutablePeriod.add(Period.days(1));
        Period result = mutablePeriod.toPeriod();
        assertEquals(Period.days(6), result);
    }

    @org.junit.Test
    public void testEquals_Object() {
        BasePeriodType periodType = new BasePeriodType() {};
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(6) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
        assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod3));
    }

    @org.junit.Test
    public void testHashCode_Object() {
        BasePeriodType periodType = new BasePeriodType() {};
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(6) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        int baseSingleFieldPeriod1HashCode = baseSingleFieldPeriod1.hashCode();
        int baseSingleFieldPeriod2HashCode = baseSingleFieldPeriod2.hashCode();
        int baseSingleFieldPeriod3HashCode = baseSingleFieldPeriod3.hashCode();

        List<Integer> hashCodes = Arrays.asList(baseSingleFieldPeriod1HashCode, baseSingleFieldPeriod2HashCode, baseSingleFieldPeriod3HashCode);

        int expectedHashCode = 27 * (27 * 17 + 5) + periodType.hashCode();
        assertTrue(hashCodes.contains(expectedHashCode));
    }

    @org.junit.Test
    public void testCompareTo_BaseSingleFieldPeriod() {
        BasePeriodType periodType = new BasePeriodType() {};
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(6) {
            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        assertEquals(0, result);

        result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod3);
        assertEquals(-1, result);
    }
}