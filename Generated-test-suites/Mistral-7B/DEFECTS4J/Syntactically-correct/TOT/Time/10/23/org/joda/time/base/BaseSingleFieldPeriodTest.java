import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriodInstance() {
        // Test creating a BaseSingleFieldPeriod instance
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(7);
        assertEquals(7, p.getValue());
        assertSame(DurationFieldType.DAYS, p.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        // Test getting the PeriodType for a BaseSingleFieldPeriod instance
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(7);
        PeriodType pt = p.getPeriodType();
        assertEquals(PeriodType.standard(), pt);
    }

    @Test
    public void testSize() {
        // Test the size of a BaseSingleFieldPeriod instance
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(7);
        assertEquals(1, p.size());
    }

    @Test
    public void testGetValue() {
        // Test getting the value of a BaseSingleFieldPeriod instance
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(7);
        assertEquals(7, p.getValue(0));
    }

    @Test
    public void testGetFieldType() {
        // Test getting the DurationFieldType for a BaseSingleFieldPeriod instance
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(7);
        DurationFieldType dft = p.getFieldType();
        assertEquals(DurationFieldType.DAYS, dft);
    }

    @Test
    public void testEquals() {
        // Test equals method for BaseSingleFieldPeriod instances
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p4 = null;

        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertFalse(p1.equals(p3));
        assertFalse(p1.equals(p4));
    }

    @Test
    public void testHashCode() {
        // Test hashCode method for BaseSingleFieldPeriod instances
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(5);

        int hash1 = p1.hashCode();
        int hash2 = p2.hashCode();
        int hash3 = p3.hashCode();

        assertEquals(hash1, hash2); // Should be equal since they are equal
        assertNotSame(hash1, hash3); // Should be different
    }

    @Test
    public void testCompareTo() {
        // Test compareTo method for BaseSingleFieldPeriod instances
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(7);

        int result1 = p1.compareTo(p2);
        int result2 = p2.compareTo(p1);
        int result3 = p1.compareTo(p3);
        int result4 = p3.compareTo(p1);

        assertTrue(result1 > 0);
        assertTrue(result2 < 0);
        assertTrue(result3 == 0);
        assertTrue(result4 == 0);
    }

    @Test
    public void testToPeriod() {
        // Test converting a BaseSingleFieldPeriod to a Period instance
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(7);
        Period period = p.toPeriod();

        assertEquals(7, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        // Test converting a BaseSingleFieldPeriod to a MutablePeriod instance
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(7);
        MutablePeriod mutablePeriod = p.toMutablePeriod();

        assertEquals(7, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());

        mutablePeriod.setYears(5);
        mutablePeriod.setDays(2);

        assertEquals(5, p.getYears());
        assertEquals(2, p.getDays());
    }
}