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
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant start = new org.joda.time.LocalDateTime(2013, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new org.joda.time.LocalDateTime(2013, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBetweenInvalidStart() {
        ReadableInstant start = null;
        ReadableInstant end = new org.joda.time.LocalDateTime(2013, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBetweenInvalidEnd() {
        ReadableInstant start = new org.joda.time.LocalDateTime(2013, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = null;
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
    }

    @Test
    public void testBetweenPartial() {
        ReadablePartial start = new org.joda.time.LocalDate(2013, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDate(2013, 1, 2);
        ReadablePeriod zeroInstance = new org.joda.time.Duration(1, DurationFieldType.days());
        int period = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(1, period);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBetweenInvalidStartPartial() {
        ReadablePartial start = null;
        ReadablePartial end = new org.joda.time.LocalDate(2013, 1, 1);
        ReadablePeriod zeroInstance = new org.joda.time.Duration(1, DurationFieldType.days());
        int period = BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBetweenInvalidEndPartial() {
        ReadablePartial start = new org.joda.time.LocalDate(2013, 1, 1);
        ReadablePartial end = null;
        ReadablePeriod zeroInstance = new org.joda.time.Duration(1, DurationFieldType.days());
        int period = BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBetweenInvalidFieldsPartial() {
        ReadablePartial start = new org.joda.time.LocalDate(2013, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDate(2013, 1, 1);
        ReadablePeriod zeroInstance = new org.joda.time.LocalDateTime(2013, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toPeriod();
        int period = BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new org.joda.time.Duration(1, DurationFieldType.days());
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, org.joda.time.Duration.ONE_HOUR.getMillis());
        assertEquals(24, hours);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = null;
        long millisPerUnit = org.joda.time.Duration.ONE_HOUR.getMillis();
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    @Test
    public void testNewInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        assertEquals(3, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.days(), fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        int size = period.size();
        assertEquals(1, size);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        period.getFieldType(2);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        int value = period.getValue();
        assertEquals(3, value);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        period.getValue(2);
    }

    @Test
    public void testGetValueFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        int value = period.get(DurationFieldType.days());
        assertEquals(3, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        boolean supported = period.isSupported(DurationFieldType.days());
        assertTrue(supported);
        boolean notSupported = period.isSupported(DurationFieldType.months());
        assertFalse(notSupported);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        Period p = period.toPeriod();
        assertEquals(3, p.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        MutablePeriod mPeriod = period.toMutablePeriod();
        mPeriod.add(DurationFieldType.days(), 2);
        assertEquals(5, mPeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testNotEqualsDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(4);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}