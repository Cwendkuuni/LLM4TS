import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testInstance() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(7, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertFalse(period.isSupported(DurationFieldType.months()));
    }

    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.parseInstant("2012-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2012-01-08T00:00:00.000Z");
        int amount = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(amount) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(amount, period.getValue());
    }

    @Test
    public void testStandardPeriod() {
        ReadablePeriod period = Period.days(3);
        long millisPerUnit = 24 * 60 * 60 * 1000; // hours * minutes * seconds * milliseconds
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        BaseSingleFieldPeriod standardPeriod = new BaseSingleFieldPeriod(hours) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(hours, standardPeriod.getValue());
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Period result = period.toPeriod();
        assertEquals(PeriodType.standard(), result.getPeriodType());
        assertEquals(7, result.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(7, mutablePeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period4 = new Object();
        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, period4);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();
        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int result = period1.compareTo(period2);
        assertTrue(result < 0);
        result = period2.compareTo(period3);
        assertTrue(result == 0);
        result = period3.compareTo(period1);
        assertTrue(result > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullStart() {
        ReadableInstant start = null;
        ReadableInstant end = DateTimeUtils.parseInstant("2012-01-01T00:00:00.000Z");
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullEnd() {
        ReadableInstant start = DateTimeUtils.parseInstant("2012-01-01T00:00:00.000Z");
        ReadableInstant end = null;
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonMatchingChronology() {
        ReadableInstant start = DateTimeUtils.parseInstant("2012-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2012-01-02T00:00:00.000Z");
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        Chronology chronoA = DateTimeUtils.getISODateChronology();
        Chronology chronoB = DateTimeUtils.getGregorianChronology();
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.days(), chronoA);
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.days(), chronoB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonMatchingFieldType() {
        ReadableInstant start = DateTimeUtils.parseInstant("2012-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2012-01-02T00:00:00.000Z");
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.months());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousStart() {
        ReadablePartial start = DateTimeUtils.parseLocalDate("2012-01-01").withFieldValueSet(DateTimeConstants.MONTH_OF_YEAR, 13);
        ReadablePartial end = DateTimeUtils.parseLocalDate("2012-01-02");
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodWithNonSupportedPeriod() {
        ReadablePeriod period = Period.days(3).withMonths(2);
        long millisPerUnit = 24 * 60 * 60 * 1000; // hours * minutes * seconds * milliseconds
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }
}