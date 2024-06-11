import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant start = new org.joda.time.LocalDateTime(2011, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new org.joda.time.LocalDateTime(2011, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        assertEquals(604800000L, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, end, DurationFieldType.days()));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, null, DurationFieldType.days()));
    }

    @Test
    public void testBetweenPartial() {
        LocalDate start = new LocalDate(2011, 1, 1);
        LocalDate end = new LocalDate(2011, 1, 2);

        Period zero = Period.ZERO;
        assertEquals(604800000L, BaseSingleFieldPeriod.between(start, end, zero));

        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, end, zero));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, null, zero));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, end, null));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6);
        assertEquals(6227200000L, BaseSingleFieldPeriod.standardPeriodIn(period, 1000));

        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(null, 1000));
    }

    @Test
    public void testCreate() {
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
        assertEquals(7, period.getValue());
        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        assertEquals(7, period.get(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));

        Period periodObj = period.toPeriod();
        assertEquals(7, periodObj.getDays());
        assertEquals(0, periodObj.getHours());
        assertEquals(0, periodObj.getMinutes());
        assertEquals(0, periodObj.getSeconds());
        assertEquals(0, periodObj.getMillis());

        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(7, mutablePeriod.getDays());
        mutablePeriod.setHours(2);
        assertEquals(84, mutablePeriod.toStandardPeriod().getHours());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertFalse(p1.equals(p3));

        assertNull(p1.toPeriod().compareTo(null));
        assertNull(p1.toMutablePeriod().compareTo(null));
        assertNull(p1.toPeriod().compareTo(new Period()));
        assertNull(p1.toMutablePeriod().compareTo(new MutablePeriod()));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int hashCodeP1 = p1.hashCode();
        int hashCodeP2 = p2.hashCode();
        int hashCodeP3 = p3.hashCode();

        assertEquals(hashCodeP1, hashCodeP1);
        assertEquals(hashCodeP1, hashCodeP2);
        assertNotSame(hashCodeP1, hashCodeP3);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(0, p1.compareTo(p3));
        assertEquals(-1, p3.compareTo(p1));
        assertEquals(1, p2.compareTo(p1));
        assertEquals(-1, p1.compareTo(p2));
    }
}