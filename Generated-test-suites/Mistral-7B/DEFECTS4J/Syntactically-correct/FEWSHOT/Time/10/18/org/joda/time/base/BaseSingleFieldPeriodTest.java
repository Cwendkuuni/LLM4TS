package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        // Test empty constructor
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        assertEquals(0, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        assertEquals(0, period.getValue(0));
        assertEquals(Period.ZERO, period.toPeriod());

        // Test constructor with value
        period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        assertEquals(5, period.getValue(0));
        assertEquals(Period.valueOf(5, PeriodType.DAYS), period.toPeriod());

        // Test constructor with value and field type
        period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        assertEquals(5, period.getValue(0));
        assertEquals(Period.valueOf(5, PeriodType.DAYS), period.toPeriod());

        // Test constructor with value and period type
        period = new BaseSingleFieldPeriod(5, PeriodType.days());
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.days(), period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        assertEquals(5, period.getValue(0));
        assertEquals(Period.valueOf(5, PeriodType.DAYS), period.toPeriod());

        // Test constructor with value, field type, and period type
        period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS, PeriodType.days());
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.days(), period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        assertEquals(5, period.getValue(0));
        assertEquals(Period.valueOf(5, PeriodType.DAYS), period.toPeriod());

        // Test constructor with invalid period type
        try {
            new BaseSingleFieldPeriod(5, DurationFieldType.HOURS, PeriodType.days());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test set and get value
        period.setValue(10);
        assertEquals(10, period.getValue());
        period.setValue(20);
        assertEquals(20, period.getValue());

        // Test add
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod sum = p1.add(p2);
        assertEquals(5, sum.getValue());
        assertEquals(DurationFieldType.DAYS, sum.getFieldType());
        assertEquals(PeriodType.standard(), sum.getPeriodType());

        // Test subtract
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod diff = p1.subtract(p3);
        assertEquals(1, diff.getValue());
        assertEquals(DurationFieldType.DAYS, diff.getFieldType());
        assertEquals(PeriodType.standard(), diff.getPeriodType());

        // Test negate
        BaseSingleFieldPeriod neg = p1.negate();
        assertEquals(-3, neg.getValue());
        assertEquals(DurationFieldType.DAYS, neg.getFieldType());
        assertEquals(PeriodType.standard(), neg.getPeriodType());

        // Test comparison
        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod(4);
        BaseSingleFieldPeriod smaller = p1.compareTo(p4) < 0 ? p1 : p4;
        assertEquals(3, smaller.getValue());
        BaseSingleFieldPeriod larger = p1.compareTo(p4) > 0 ? p1 : p4;
        assertEquals(4, larger.getValue());
        assertEquals(p1, larger.compareTo(p4) > 0 ? p1 : p4);
        assertEquals(p4, smaller.compareTo(p1) < 0 ? p4 : p1);

        // Test equals
        BaseSingleFieldPeriod p5 = new BaseSingleFieldPeriod(3);
        assertEquals(p1, p5);
        BaseSingleFieldPeriod p6 = new BaseSingleFieldPeriod(4);
        assertNotEquals(p1, p6);
        BaseSingleFieldPeriod p7 = new BaseSingleFieldPeriod(3, DurationFieldType.HOURS, PeriodType.hours());
        assertNotEquals(p1, p7);

        // Test hash code
        int hashCode1 = p1.hashCode();
        int hashCode2 = p1.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test toString
        assertEquals("P3D", p1.toString());

        // Test between
        long startMillis = BaseDateTime.UTC.getMillis();
        long endMillis = startMillis + 3 * 24 * 60 * 60 * 1000;
        BaseSingleFieldPeriod periodBetween = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.between(
                BaseDateTime.now(), BaseDateTime.now().withMillisAdded(3 * 24 * 60 * 60 * 1000), DurationFieldType.DAYS));
        assertEquals(3, periodBetween.getValue());

        // Test standardPeriodIn
        Period periodToTest = Period.valueOf(4, DurationFieldType.DAYS, 2, DurationFieldType.HOURS, 30, DurationFieldType.MINUTES);
        assertEquals(11 * 24 * 60 * 60 + 2 * 60 * 60 + 30 * 60, BaseSingleFieldPeriod.standardPeriodIn(periodToTest, 60 * 60 * 1000));

        // Test toMutablePeriod
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(p1);
        assertEquals(p1, mutablePeriod.toPeriod());

        // Test toPeriod
        Period period = p1.toPeriod();
        assertEquals(3, period.getFields()[0]);
        assertEquals(PeriodType.DAYS, period.getPeriodType());

        // Test getChronology
        Chronology chrono = ISOChronology.getInstance();
        BaseSingleFieldPeriod chronoPeriod = new BaseSingleFieldPeriod(3, DurationFieldType.DAYS, chrono);
        assertEquals(chrono, chronoPeriod.getChronology());

        // Test isSupported
        assertEquals(true, p1.isSupported(DurationFieldType.DAYS));
        assertEquals(false, p1.isSupported(DurationFieldType.HOURS));

        // Test getValue
        assertEquals(3, p1.getValue(0));

        // Test getFieldType
        assertEquals(DurationFieldType.DAYS, p1.getFieldType());

        // Test getPeriodType
        assertEquals(PeriodType.standard(), p1.getPeriodType());

        // Test size
        assertEquals(1, p1.size());
    }
}