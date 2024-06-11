import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() throws Exception {
        ReadableInstant start = DateTimeUtils.parseInstant("2014-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2014-01-02T00:00:00.000Z");
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        start = DateTimeUtils.parseInstant("2014-01-01T12:34:56.789Z");
        end = DateTimeUtils.parseInstant("2014-01-02T12:34:56.789Z");
        assertEquals(0, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        start = DateTimeUtils.parseInstant("2014-01-01T12:34:56.789Z");
        end = DateTimeUtils.parseInstant("2014-01-02T00:00:00.000Z");
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        start = DateTimeUtils.parseInstant("2014-01-01T00:00:00.000Z");
        end = DateTimeUtils.parseInstant("2014-01-02T12:34:56.789Z");
        assertEquals(2, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        end = DateTimeUtils.parseInstant("2014-01-01T00:00:00.000Z");
        assertEquals(-1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        Chronology chrono = ISOChronology.getInstanceUTC();
        end = chrono.get(2014, 1, 2, 0, 0, 0, 0, 0);
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        ReadablePeriod zeroInstance = Period.ZERO;
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()), zeroInstance.getMillis());

        start = DateTimeUtils.parseInstant("2014-01-01T00:00:00.001Z");
        end = DateTimeUtils.parseInstant("2014-01-02T00:00:00.002Z");
        assertFalse(BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()) > 1);
    }

    @Test
    public void testStandardPeriodIn() throws Exception {
        ReadablePeriod period = Period.days(1).withHours(2).withMinutes(3).withSeconds(4).withMillis(567);
        assertEquals(4876, BaseSingleFieldPeriod.standardPeriodIn(period, Chronology.getInstanceUTC().getMillisPerDay()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() throws Exception {
        ReadablePeriod period = Period.days(1).withMonths(2);
        BaseSingleFieldPeriod.standardPeriodIn(period, Chronology.getInstanceUTC().getMillisPerDay());
    }

    @Test
    public void testBaseSingleFieldPeriod() throws Exception {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(4876) {};
        assertEquals(4876, period.getValue());
        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertNull(period.getFieldType(1));
        assertNull(period.getValue(1));
        assertFalse(period.isSupported(DurationFieldType.hours()));
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertEquals(Period.ZERO, period.toPeriod());
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        mutablePeriod.add(DurationFieldType.days(), 1);
        assertFalse(period.equals(mutablePeriod));
        assertFalse(mutablePeriod.equals(period));
        assertEquals(-1, period.compareTo(mutablePeriod));
        assertEquals(0, period.compareTo(period));
        assertEquals(period.hashCode(), period.hashCode());
    }
}