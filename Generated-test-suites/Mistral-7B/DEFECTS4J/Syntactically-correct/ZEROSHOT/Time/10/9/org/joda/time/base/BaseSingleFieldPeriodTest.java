package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.IntDurationField;
import org.joda.time.field.LocalDateField;
import org.joda.time.field.LocalDateTimeField;
import org.joda.time.field.LocalTimeField;
import org.joda.time.field.Months;
import org.joda.time.field.Weeks;

public class BaseSingleFieldPeriodTest {

    private static final long serialVersionUID = 9386874258972L;

    @org.junit.Test
    public void test_between() {
        Instant start = new BaseDateTime(0, 0, 0, 0, 0, 1972, ISOChronology.getInstanceUTC()).toInstant();
        Instant end = new BaseDateTime(0, 0, 0, 0, 0, 1973, ISOChronology.getInstanceUTC()).toInstant();
        int daysBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(365, daysBetween);

        // test with different chronologies
        Chronology chronology = DateTimeUtils.getInstantChronology(start);
        int daysBetween_customChronology = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days(), chronology);
        assertEquals(365, daysBetween_customChronology);

        // test with partials
        BaseDateTime startPartial = new BaseDateTime(1, 1, 2019, ISOChronology.getInstanceUTC());
        BaseDateTime endPartial = new BaseDateTime(2, 2, 2019, ISOChronology.getInstanceUTC());
        int daysBetween_partials = BaseSingleFieldPeriod.between(startPartial, endPartial, Period.DAYS);
        assertEquals(1, daysBetween_partials);

        // test with invalid inputs
        assertNull(BaseSingleFieldPeriod.between(null, end, DurationFieldType.days()));
        assertNull(BaseSingleFieldPeriod.between(start, null, DurationFieldType.days()));
        assertNull(BaseSingleFieldPeriod.between(start, end, null));
    }

    @org.junit.Test
    public void test_standardPeriodIn() {
        ReadablePeriod period = Period.parse("PT1Y2M3D");
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, Duration.ofMillis(1).toHours());
        assertEquals(876, hours);
    }

    @org.junit.Test
    public void test_newInstance() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(365) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // test getters
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(fieldType, period.getFieldType(0));
        assertEquals(365, period.getValue());
        assertEquals(365, period.getValue(0));

        // test equals
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(365) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(period, period2);

        // test not equals
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(364) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertFalse(period.equals(period3));

        // test hashcode
        int hashcode1 = period.hashCode();
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(365) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int hashcode2 = period4.hashCode();
        assertEquals(hashcode1, hashcode2);

        // test compareTo
        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(366) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int compareResult = period.compareTo(period5);
        assertTrue(compareResult < 0);

        // test toPeriod
        Period periodToPeriod = period.toPeriod();
        assertEquals(PeriodType.standard(), periodToPeriod.getPeriodType());
        assertEquals(365, periodToPeriod.getValues()[0]);
        assertEquals(fieldType, periodToPeriod.getFieldType(0));

        // test toMutablePeriod
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(365, mutablePeriod.getValues()[0]);
        assertEquals(fieldType, mutablePeriod.getFieldType(0));
        assertEquals(mutablePeriod, period.toMutablePeriod());
    }
}