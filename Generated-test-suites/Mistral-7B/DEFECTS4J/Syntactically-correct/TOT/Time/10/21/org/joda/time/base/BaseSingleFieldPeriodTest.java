import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.*;

/**
 * Tests for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test for the BaseSingleFieldPeriod constructor with a valid period.
     */
    @Test
    public void testValidPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(7, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Period.valueOf("P7D"), period.toPeriod());
        assertEquals(MutablePeriod.valueOf(7, DurationFieldType.DAYS), period.toMutablePeriod());
    }

    /**
     * Test for the BaseSingleFieldPeriod constructor with a zero period.
     */
    @Test
    public void testZeroPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        assertEquals(0, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Period.ZERO, period.toPeriod());
        assertEquals(MutablePeriod.ZERO, period.toMutablePeriod());
    }

    /**
     * Test for the BaseSingleFieldPeriod get method.
     */
    @Test
    public void testGetMethod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test for the BaseSingleFieldPeriod equals method with a null object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEqualsNullObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        assertFalse(period.equals(null));
    }

    /**
     * Test for the BaseSingleFieldPeriod equals method with a different type.
     */
    @Test
    public void testEqualsDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(2);
        MutablePeriod period2 = new MutablePeriod();
        period2.setYears(1);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test for the BaseSingleFieldPeriod equals method with a different value.
     */
    @Test
    public void testEqualsDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(4);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test for the BaseSingleFieldPeriod equals method with a equal value.
     */
    @Test
    public void testEqualsEqualValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(6);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test for the BaseSingleFieldPeriod hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertEquals(period1.hashCode(), period2.hashCode());
    }

    /**
     * Test for the BaseSingleFieldPeriod compareTo method with a null object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompareToNullObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(8);
        period.compareTo(null);
    }

    /**
     * Test for the BaseSingleFieldPeriod compareTo method with a different type.
     */
    @Test
    public void testCompareToDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        MutablePeriod period2 = new MutablePeriod();
        period2.setYears(1);
        int comparisonResult = period1.compareTo(period2);
        assertTrue(comparisonResult < 0);
    }

    /**
     * Test for the BaseSingleFieldPeriod compareTo method with a smaller value.
     */
    @Test
    public void testCompareToSmallerValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(6);
        int comparisonResult = period1.compareTo(period2);
        assertTrue(comparisonResult < 0);
    }

    /**
     * Test for the BaseSingleFieldPeriod compareTo method with a greater value.
     */
    @Test
    public void testCompareToGreaterValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(8);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int comparisonResult = period1.compareTo(period2);
        assertTrue(comparisonResult > 0);
    }

    /**
     * Test for the BaseSingleFieldPeriod compareTo method with an equal value.
     */
    @Test
    public void testCompareToEqualValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
        int comparisonResult = period1.compareTo(period2);
        assertEquals(0, comparisonResult);
    }
}