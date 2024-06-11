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
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests the between method when given valid instants.
     */
    @Test
    public void testBetweenInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new org.joda.time.local.LocalDateTime(2012, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new org.joda.time.local.LocalDateTime(2012, 1, 2, 0, 0, 0, 0, chrono).toInstant();
        BaseSingleFieldPeriod period = new BaseDays(3);
        int days = period.between(start, end, DurationFieldType.days());
        assertEquals(3, days);
    }

    /**
     * Tests the between method when given invalid instants.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInstantInvalid() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        ReadableInstant start = null;
        ReadableInstant end = new org.joda.time.local.LocalDateTime(2012, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        period.between(start, end, DurationFieldType.days());
    }

    /**
     * Tests the between method when given valid partials.
     */
    @Test
    public void testBetweenPartial() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial start = new org.joda.time.local.LocalDate(2012, 1, 1);
        ReadablePartial end = new org.joda.time.local.LocalDate(2012, 1, 3);
        BaseSingleFieldPeriod period = new BaseDays(2);
        int days = period.between(start, end, PeriodType.standardInstance());
        assertEquals(2, days);
    }

    /**
     * Tests the between method when given invalid partials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenPartialInvalid() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        ReadablePartial start = null;
        ReadablePartial end = new org.joda.time.local.LocalDate(2012, 1, 1);
        period.between(start, end, PeriodType.standardInstance());
    }

    /**
     * Tests the standardPeriodIn method with a valid period.
     */
    @Test
    public void testStandardPeriodInValidPeriod() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        int hours = BaseSingleFieldPeriod.standardPeriodIn(new Period(3, 4, 5, 6), 3600 * 1000);
        assertEquals(10, hours);
    }

    /**
     * Tests the standardPeriodIn method with an invalid period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        new Period(3, 4, 5, 6, 7, 8, 9); // contains month field which cannot be converted
        int hours = BaseSingleFieldPeriod.standardPeriodIn(new Period(3, 4, 5, 6, 7, 8, 9), 3600 * 1000);
    }

    /**
     * Tests the constructor with a valid value.
     */
    @Test
    public void testConstructorValidValue() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        assertEquals(3, period.getValue());
    }

    /**
     * Tests the constructor with an invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidValue() {
        new BaseDays(-1);
    }

    /**
     * Tests the getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.days(), fieldType);
    }

    /**
     * Tests the getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
    }

    /**
     * Tests the size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        assertEquals(1, period.size());
    }

    /**
     * Tests the equals method with null object.
     */
    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        assertFalse(period.equals(null));
    }

    /**
     * Tests the equals method with different types.
     */
    @Test
    public void testEqualsDifferentTypes() {
        BaseSingleFieldPeriod period1 = new BaseDays(3);
        Object period2 = new java.util.GregorianCalendar(2012, 1, 3).getTimeInMillis();
        assertFalse(period1.equals(period2));
    }

    /**
     * Tests the equals method with same object.
     */
    @Test
    public void testEqualsSameObject() {
        BaseSingleFieldPeriod period1 = new BaseDays(3);
        BaseSingleFieldPeriod period2 = period1;
        assertTrue(period1.equals(period2));
    }

    /**
     * Tests the equals method with same value.
     */
    @Test
    public void testEqualsSameValue() {
        BaseSingleFieldPeriod period1 = new BaseDays(3);
        BaseSingleFieldPeriod period2 = new BaseDays(3);
        assertTrue(period1.equals(period2));
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseDays(3);
        BaseSingleFieldPeriod period2 = new BaseDays(3);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Tests the compareTo method with null object.
     */
    @Test(expected = NullPointerException.class)
    public void testCompareToNull() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        period.compareTo(null);
    }

    /**
     * Tests the compareTo method with different classes.
     */
    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClasses() {
        BaseSingleFieldPeriod period1 = new BaseDays(3);
        Object period2 = new java.util.GregorianCalendar(2012, 1, 3).getTimeInMillis();
        period1.compareTo(period2);
    }

    /**
     * Tests the compareTo method with the same object.
     */
    @Test
    public void testCompareToSameObject() {
        BaseSingleFieldPeriod period1 = new BaseDays(3);
        BaseSingleFieldPeriod period2 = period1;
        assertEquals(0, period1.compareTo(period2));
    }

    /**
     * Tests the compareTo method with different values.
     */
    @Test
    public void testCompareToDifferentValues() {
        BaseSingleFieldPeriod period1 = new BaseDays(3);
        BaseSingleFieldPeriod period2 = new BaseDays(5);
        assertEquals(2, period1.compareTo(period2));
    }

    /**
     * Tests the toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        Period expected = new Period(3, 0, 0, 0);
        Period actual = period.toPeriod();
        assertSame(expected, actual);
    }

    /**
     * Tests the toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseDays(3);
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(3, actual.getYears());
        assertEquals(0, actual.getMonths());
        assertEquals(0, actual.getWeeks());
        assertEquals(0, actual.getDays());
        assertEquals(0, actual.getHours());
        assertEquals(0, actual.getMinutes());
        assertEquals(0, actual.getSeconds());
        assertEquals(0, actual.getMillis());
    }
}