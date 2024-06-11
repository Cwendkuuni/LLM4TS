import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests the BaseSingleFieldPeriod class.
     */
    @Test
    public void testBaseSingleFieldPeriod() {
        // Test with ReadingInstant
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        ReadableInstant start = DateTimeUtils.parseInstant("2022-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2022-01-08T00:00:00.000Z");
        int daysBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(daysBetween);

        assertEquals(p1, p2);
        assertEquals(p1.toString(), p2.toString());
        assertEquals(p1.getClass(), p2.getClass());

        // Test with ReadingPartial
        ReadablePartial startPartial = DateTimeUtils.parseLocalDate("2022-01-01");
        ReadablePartial endPartial = DateTimeUtils.parseLocalDate("2022-01-08");
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.between(startPartial, endPartial, PeriodType.standard()));

        assertEquals(p1, p3);
        assertEquals(p1.toString(), p3.toString());
        assertEquals(p1.getClass(), p3.getClass());

        // Test invalid input
        assertFalse(new BaseSingleFieldPeriod(-1) instanceof BaseSingleFieldPeriod);
        assertFalse(new BaseSingleFieldPeriod(0) instanceof BaseSingleFieldPeriod);

        // Test addition and subtraction
        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod(p1).plus(new BaseSingleFieldPeriod(2));
        assertEquals(p1.getValue(), p4.getValue() - 2);
        BaseSingleFieldPeriod p5 = new BaseSingleFieldPeriod(p1).minus(new BaseSingleFieldPeriod(2));
        assertEquals(p1.getValue(), p5.getValue() + 2);

        // Test toPeriod and toMutablePeriod
        Period p6 = p1.toPeriod();
        assertEquals(p1.getValue(), p6.getYears() * 365L + p6.getDays());
        MutablePeriod p7 = p1.toMutablePeriod();
        p7.add(p1);
        assertEquals(p1, p7.toPeriod());

        // Test compareTo
        BaseSingleFieldPeriod p8 = new BaseSingleFieldPeriod(p1.getValue() + 1);
        int c = p1.compareTo(p8);
        assertTrue(c < 0);
        BaseSingleFieldPeriod p9 = new BaseSingleFieldPeriod(p1.getValue() - 1);
        int c2 = p1.compareTo(p9);
        assertTrue(c > 0);
    }
}