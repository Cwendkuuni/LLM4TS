import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test the between method with valid instants and field type.
     */
    @Test
    public void testBetweenValidInstants() {
        ReadableInstant start = DateTimeUtils.parseLocalDate("2022-01-01T00:00:00.000Z").toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDate("2022-01-02T00:00:00.000Z").toInstant();
        int days = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, days);
    }

    /**
     * Test the between method with null instants.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullInstant() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.days());
    }

    /**
     * Test the between method with different field types.
     */
    @Test
    public void testBetweenDifferentFieldTypes() {
        ReadableInstant start = DateTimeUtils.parseLocalDate("2022-01-01T00:00:00.000Z").toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDate("2022-01-02T00:00:00.000Z").toInstant();
        int days = BaseSingleFieldPeriod.between(start, end, DurationFieldType.minutes());
        assertFalse(days > 0);
    }

    /**
     * Test the between method with a regular partial and a zero partial.
     */
    @Test
    public void testBetweenPartialValid() {
        ReadablePartial start = new MutableLocalDate();
        start.setYear(2022);
        start.setMonthOfYear(1);
        start.setDayOfMonth(1);

        ReadablePartial end = new MutableLocalDate();
        end.setYear(2022);
        end.setMonthOfYear(1);
        end.setDayOfMonth(2);

        int days = BaseSingleFieldPeriod.between(start, end, PeriodType.standardInstance());
        assertEquals(1, days);
    }

    /**
     * Test the between method with null partials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullPartial() {
        BaseSingleFieldPeriod.between(null, null, PeriodType.standardInstance());
    }

    /**
     * Test the between method with different field sets in partials.
     */
    @Test
    public void testBetweenDifferentPartialFields() {
        ReadablePartial start = new MutableLocalDate();
        start.setYear(2022);
        start.setMonthOfYear(1);
        start.setDayOfMonth(1);
        start.setDayOfWeek(DateTimeConstants.SATURDAY);

        ReadablePartial end = new MutableLocalDate();
        end.setYear(2022);
        end.setMonthOfYear(1);
        end.setDayOfMonth(2);

        int days = BaseSingleFieldPeriod.between(start, end, PeriodType.standardInstance());
        assertFalse(days > 0);
    }

    /**
     * Test the standardPeriodIn method with valid period and millisPerUnit.
     */
    @Test
    public void testStandardPeriodInValidPeriod() {
        ReadablePeriod period = Days.ONE;
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, 60 * 60 * 1000);
        assertEquals(1, hours);
    }

    /**
     * Test the standardPeriodIn method with null period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 60 * 60 * 1000);
    }

    /**
     * Test the standardPeriodIn method with invalid period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = Period.parse("P1Y2M3D");
        BaseSingleFieldPeriod.standardPeriodIn(period, 60 * 60 * 1000);
    }

    /**
     * Test the constructor with valid period value.
     */
    @Test
    public void testConstructorValidPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertNotNull(period);
        assertEquals(1, period.getValue());
    }

    /**
     * Test the constructor with invalid period value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1);
    }

    /**
     * Test the getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = period.getFieldType();
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.days(), fieldType);
    }

    /**
     * Test the getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test the size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        int size = period.size();
        assertEquals(1, size);
    }

    /**
     * Test the getFieldType method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getFieldType(2);
    }

    /**
     * Test the getValue method.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        int value = period.getValue();
        assertEquals(1, value);
    }

    /**
     * Test the getValue method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getValue(2);
    }

    /**
     * Test the get method.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        int days = period.get(DurationFieldType.days());
        assertEquals(2, days);
    }

    /**
     * Test the isSupported method.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    /**
     * Test the toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        Period result = period.toPeriod();
        assertEquals(PeriodType.standard(), result.getPeriodType());
        assertEquals(3, result.getMillis());
    }

    /**
     * Test the toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(4);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(4, result.getMillis());
    }

    /**
     * Test the equals method with this object.
     */
    @Test
    public void testEqualsSelf() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(period));
    }

    /**
     * Test the equals method with null object.
     */
    @Test
    public void testEqualsNullObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(6);
        assertFalse(period.equals(null));
    }

    /**
     * Test the equals method with different type object.
     */
    @Test
    public void testEqualsDifferentTypeObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertFalse(period.equals(new Object()));
    }

    /**
     * Test the equals method with different value object.
     */
    @Test
    public void testEqualsDifferentValueObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(8);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(9);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test the equals method with matching object.
     */
    @Test
    public void testEqualsMatchingObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test the hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(11);
        int hashCode = period.hashCode();
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(11);
        assertEquals(hashCode, period2.hashCode());
    }

    /**
     * Test the compareTo method with greater object.
     */
    @Test
    public void testCompareToGreaterObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(12);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(11);
        int result = period1.compareTo(period2);
        assertEquals(1, result);
    }

    /**
     * Test the compareTo method with less object.
     */
    @Test
    public void testCompareToLessObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(12);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    /**
     * Test the compareTo method with equal object.
     */
    @Test
    public void testCompareToEqualObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(11);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(11);
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }
}