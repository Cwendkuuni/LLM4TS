import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidConstruction() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
        assertEquals(5, period.getValue());
    }

    /**
     * Test getting the field type.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getting the period type.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test getting the size.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }

    /**
     * Test getting the field at index 0.
     */
    @Test
    public void testGetFieldAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(fieldType, period.getFieldType(0));
    }

    /**
     * Test getting the value at index 0.
     */
    @Test
    public void testgetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue(0);
        assertEquals(5, value);
    }

    /**
     * Test adding a valid period to this period.
     */
    @Test
    public void testAddValidPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = period1.add(period2);
        int value = period3.getValue();
        assertEquals(8, value);
    }

    /**
     * Test adding a null period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.add(null);
    }

    /**
     * Test adding a period with an unsupported field type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnsupportedPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3, DurationFieldType.HOURS);
        period1.add(period2);
    }

    /**
     * Test getting the period as a MutablePeriod.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        int value = mutablePeriod.get(DurationFieldType.DAYS);
        assertEquals(5, value);
    }

    /**
     * Test converting to a Period.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period period1 = period.toPeriod();
        int value = period1.getValues()[0];
        assertEquals(5, value);
    }

    /**
     * Test getting the number of whole units between two instants.
     */
    @Test
    public void testBetween() {
        long startMillis = DateTimeUtils.currentTimeMillis();
        long endMillis = startMillis + (30 * 60 * 1000);
        int value = BaseSingleFieldPeriod.between(DateTimeUtils.currentInstant(), new org.joda.time.LocalDate(2022, 1, 1).toInstant(), DurationFieldType.DAYS);
        assertEquals(0, value);
        value = BaseSingleFieldPeriod.between(DateTimeUtils.currentInstant(), new org.joda.time.LocalDateTime(2022, 1, 1, 0, 0).toInstant(), DurationFieldType.DAYS);
        assertEquals(0, value);
        value = BaseSingleFieldPeriod.between(DateTimeUtils.instant(startMillis), DateTimeUtils.instant(endMillis), DurationFieldType.DAYS);
        assertEquals(1, value);
    }

    /**
     * Test getting the number of whole units between two partials.
     */
    @Test
    public void testBetweenPartial() {
        org.joda.time.LocalDate start = new org.joda.time.LocalDate(2022, 1, 1);
        org.joda.time.LocalDate end = new org.joda.time.LocalDate(2022, 1, 2);
        int value = BaseSingleFieldPeriod.between(start, end, Period.class.getDeclaredField("ZERO").get(null));
        assertEquals(1, value);
    }

    /**
     * Test converting a period to a standard duration.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        long millisPerUnit = 86400000;
        int value = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(5, value);
    }

}