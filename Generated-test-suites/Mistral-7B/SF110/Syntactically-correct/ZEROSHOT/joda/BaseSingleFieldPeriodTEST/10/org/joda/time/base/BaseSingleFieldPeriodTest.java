package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.DecimalField;
import org.joda.time.field.DurationField;
import org.joda.time.field.FieldEnum;
import org.joda.time.field.FieldPosition;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_instant_between() {
        Instant instant = new Instant(1516041620000L); // 2017-11-19 17:00:20.000
        Instant instant2 = new Instant(1516041940000L); // 2017-11-19 17:12:20.000
        DurationFieldType durationFieldType = DurationFieldType.days();
        int between = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
        assertEquals(2, between);
    }

    @Test
    public void testBaseSingleFieldPeriod_partial_between() {
        LocalDateTime localDateTime = new LocalDateTime(2017, 11, 19, 17, 10, 0); // 2017-11-19 17:10:00
        LocalDateTime localDateTime2 = new LocalDateTime(2017, 11, 19, 17, 12, 0); // 2017-11-19 17:12:00
        BasePeriod basePeriod = Period.days(2);
        int between = BaseSingleFieldPeriod.between(localDateTime.toLocalDate(), localDateTime2.toLocalDate(), basePeriod.getPeriodType());
        assertEquals(2, between);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_instant_between_null() {
        Instant instant = null;
        Instant instant2 = new Instant(1516041620000L);
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_partial_between_null() {
        LocalDate localDate = null;
        LocalDate localDate2 = new LocalDate(2017, 11, 19);
        BasePeriod basePeriod = Period.days(2);
        BaseSingleFieldPeriod.between(localDate, localDate2, basePeriod.getPeriodType());
    }

    @Test
    public void testBaseSingleFieldPeriod_standardPeriodIn() {
        BasePeriod basePeriod = Period.days(2);
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(basePeriod, 1000 * 60 * 60 * 24 * 30);
        assertEquals(60, standardPeriodIn);
    }

    @Test
    public void testBaseSingleFieldPeriod_newInstance() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        assertEquals(2, baseSingleFieldPeriod.getValue());
        assertEquals(durationFieldType, baseSingleFieldPeriod.getFieldType());
        assertEquals(PeriodType.DAYS, baseSingleFieldPeriod.getPeriodType());
        assertEquals(1, baseSingleFieldPeriod.size());
    }

    @Test
    public void testBaseSingleFieldPeriod_equals() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }

    @Test
    public void testBaseSingleFieldPeriod_notEquals() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        int compareTo = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriod_toPeriod() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        Period period = baseSingleFieldPeriod.toPeriod();
        assertEquals(Period.days(2), period);
    }

    @Test
    public void testBaseSingleFieldPeriod_toMutablePeriod() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        mutablePeriod.add(Period.days(1));
        assertEquals(Period.days(3), mutablePeriod.toPeriod());
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldType() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        assertEquals(durationFieldType, baseSingleFieldPeriod.getFieldType());
    }

    @Test
    public void testBaseSingleFieldPeriod_getPeriodType() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        assertEquals(PeriodType.DAYS, baseSingleFieldPeriod.getPeriodType());
    }

    @Test
    public void testBaseSingleFieldPeriod_getValue() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        assertEquals(2, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testBaseSingleFieldPeriod_setValue() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        baseSingleFieldPeriod.setValue(3);
        assertEquals(3, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldTypeName() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        assertEquals(DurationFieldType.DAYS.getName(), baseSingleFieldPeriod.getFieldType().getName());
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldTypeInstance() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        DurationField field = durationFieldType.getField(ISOChronology.getInstanceUTC());
        assertEquals(FieldUtils.SIXTY_SECONDS, baseSingleFieldPeriod.getFieldType().getUnitMillis());
        assertEquals(FieldPosition.FIRST_INSTANT, field.getPosition());
        assertEquals(FieldEnum.DAYS, field.getFieldType());
        assertEquals("days", field.getName());
    }

    @Test
    public void testBaseSingleFieldPeriod_toString() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return durationFieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.DAYS;
            }
        };
        String toString = baseSingleFieldPeriod.toString();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d 'days'");
        String pattern = formatter.print(Period.days(2));
        assertEquals(pattern, toString);
    }
}