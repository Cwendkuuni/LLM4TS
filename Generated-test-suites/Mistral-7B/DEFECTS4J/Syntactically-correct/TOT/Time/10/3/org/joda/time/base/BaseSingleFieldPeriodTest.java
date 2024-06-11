import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DatetimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadOnlyDateTime;
import org.joda.time.base.AbstractBaseSingleFieldPeriodTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest extends AbstractBaseSingleFieldPeriodTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test getFieldType.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertNotNull(fieldType);
        assertTrue(fieldType.isSingular());
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getPeriodType.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }

    /**
     * Test getFieldType(int).
     */
    @Test
    public void testGetFieldTypeInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType(0);
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getValue(int).
     */
    @Test
    public void testGetValueInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue(0);
        assertEquals(5, value);
    }

    /**
     * Test get(DurationFieldType).
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(5, value);
    }

    /**
     * Test isSupported(DurationFieldType).
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean supported = period.isSupported(DurationFieldType.DAYS);
        assertTrue(supported);
    }

    /**
     * Test toPeriod.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertSame(PeriodType.standard(), result.getPeriodType());
        assertEquals(5, result.getMillis());
    }

    /**
     * Test toMutablePeriod.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(5, result.getMillis());
    }

    /**
     * Test equals(Object).
     */
    @Test
    public void testEqualsObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(null));
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertFalse(period1.equals(period3));
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertNotSame(period1.hashCode(), period3.hashCode());
    }

    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int cmp = period1.compareTo(period2);
        assertTrue(cmp > 0);

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(3);
        int cmp2 = period1.compareTo(period3);
        assertTrue(cmp2 > 0);

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(5);
        int cmp3 = period1.compareTo(period4);
        assertEquals(0, cmp3);

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(7);
        int cmp4 = period1.compareTo(period5);
        assertTrue(cmp4 < 0);
    }

    /**
     * Test between.
     */
    @Test
    public void testBetween() {
        ReadableInstant start = DatetimeUtils.currentInstant();
        ReadableInstant end = start.plus(Duration.standardDays(3));
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(3, period);
    }

    /**
     * Test between(ReadablePeriod, ReadablePeriod).
     */
    @Test
    public void testBetweenPeriods() {
        Period period = Period.duration(3, DurationFieldType.DAYS);
        int value = BaseSingleFieldPeriod.between(new DateTime(0), new DateTime(), DurationFieldType.DAYS);
        assertEquals(0, value);
        int value2 = BaseSingleFieldPeriod.between(new DateTime(), new DateTime().plus(period), DurationFieldType.DAYS);
        assertEquals(3, value2);
    }

    /**
     * Test standardPeriodIn.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int millisPerUnit = Chronology.getInstanceUTC().getMillisPerDay();
        int periodInUnits = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(5, periodInUnits);
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        String text = period.toString();
        assertEquals("P5D", text);
    }

}