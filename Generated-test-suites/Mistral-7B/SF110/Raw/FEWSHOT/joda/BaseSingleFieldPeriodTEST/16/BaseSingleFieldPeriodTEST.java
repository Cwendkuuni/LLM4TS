package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.BasePeriod;
import org.joda.time.BasePeriodType;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time. Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.field.FieldLength;
import org.joda.time.field.GregorianLenientYearField;
import org.joda.time.field.IntegerLenientYearField;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() throws Exception {
        final Instant instant1 = new Instant(1310011200000L);
        final Instant instant2 = new Instant(1310011530000L);
        final DurationFieldType durationFieldType = DateTimeFieldType.millisOfDay();
        final int difference = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
        assertEquals(330L, difference);

        final LocalDateTime localDateTime1 = new LocalDateTime(2003, 12, 31, 13, 0);
        final LocalDateTime localDateTime2 = new LocalDateTime(2003, 12, 31, 13, 30);
        final ReadablePeriod readablePeriod = PeriodType.standardWeeks().withFields(new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DateTimeFieldType.weeks();
            }
        });
        final int difference2 = BaseSingleFieldPeriod.between(localDateTime1, localDateTime2, readablePeriod);
        assertEquals(7, difference2);

        final LocalDate localDate1 = new LocalDate(2003, 12, 31);
        final LocalDate localDate2 = new LocalDate(2004, 1, 1);
        final ReadablePeriod readablePeriod2 = PeriodType.standardYears().withFields(new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return new DateTimeFieldType("year", BasePeriodType.instance(), new GregorianLenientYearField(true, new IntegerLenientYearField(), new FieldLength(12)));
            }
        });
        final int difference3 = BaseSingleFieldPeriod.between(localDate1, localDate2, readablePeriod2);
        assertEquals(1, difference3);
    }

    @Test
    public void testStandardPeriodIn() {
        final Period period = Period.parse("P1W2D3S");
        assertEquals(604800001L, BaseSingleFieldPeriod.standardPeriodIn(period, 1000));
    }

    @Test
    public void testGetFieldType() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1234) {
            @Override
            public DurationFieldType getFieldType() {
                return new DurationFieldType("test", BasePeriodType.instance(), new GregorianLenientYearField(true, new IntegerLenientYearField(), new FieldLength(12))) {
                    @Override
                    public String getName() {
                        return "testDuration";
                    }

                    @Override
                    public long getUnitMillis() {
                        return 123456789L;
                    }
                };
            }
        };
        assertEquals("testDuration", baseSingleFieldPeriod.getFieldType().getName());
        assertEquals(123456789L, baseSingleFieldPeriod.getFieldType().getUnitMillis());
    }

    @Test
    public void testSize() {
        assertEquals(1, new BaseSingleFieldPeriod(1234).size());
    }

    @Test
    public void testGetValue() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1234);
        assertEquals(1234, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testGetValue_NegativeIndex() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1234);
        try {
            baseSingleFieldPeriod.getValue(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetFieldType_NegativeIndex() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1234);
        try {
            baseSingleFieldPeriod.getFieldType(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetValue_Null() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1234);
        try {
            baseSingleFieldPeriod.getValue(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1234);
        final BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1234);
        final BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(5678);
        assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
        assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod3));
        assertFalse(baseSingleFieldPeriod1.equals(null));
        assertFalse(baseSingleFieldPeriod1.equals("foo"));
    }

    @Test
    public void testHashCode() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1234);
        final BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1234);
        final BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(5678);
        assertEquals(27 * (27 * 17 + 1234) + baseSingleFieldPeriod1.getFieldType().hashCode(), baseSingleFieldPeriod1.hashCode());
        assertEquals(27 * (27 * 17 + 1234) + baseSingleFieldPeriod2.getFieldType().hashCode(), baseSingleFieldPeriod2.hashCode());
        assertNotEquals(baseSingleFieldPeriod1.hashCode(), baseSingleFieldPeriod3.hashCode());
    }

    @Test
    public void testCompareTo() throws Exception {
        final BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1234);
        final BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5678);
        final BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(1234);
        assertEquals(0, baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod3));
        assertEquals(1, baseSingleFieldPeriod2.compareTo(baseSingleFieldPeriod1));
        assertEquals(-1, baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2));
    }

    @Test
    public void testToPeriod() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1234);
        assertEquals(Period.withFields(new BasePeriod.Fields(baseSingleFieldPeriod.getFieldType(), baseSingleFieldPeriod.getValue())), baseSingleFieldPeriod.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        final BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1234);
        final MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(baseSingleFieldPeriod);
        assertEquals(new BasePeriod.Fields(baseSingleFieldPeriod.getFieldType(), baseSingleFieldPeriod.getValue()), mutablePeriod.toPeriod().getFields());
    }
}