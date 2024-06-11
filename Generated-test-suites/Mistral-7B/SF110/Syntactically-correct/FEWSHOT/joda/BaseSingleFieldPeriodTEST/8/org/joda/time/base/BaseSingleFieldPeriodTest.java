package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES);
        assertEquals(IOADayOfYear.INSTANCES, instance.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES);
        assertEquals(PeriodType.DAYS, instance.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES);
        assertEquals(1, instance.size());
    }

    @Test
    public void testGetFieldTypeAt() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES);
        assertEquals(IOADayOfYear.INSTANCES, instance.getFieldType(0));
    }

    @Test
    public void testGetValueAt() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        assertEquals(521, instance.getValue(0));
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        assertEquals(521, instance.getValue());
    }

    @Test
    public void testGetAt() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        assertEquals(521, instance.get(IOADayOfYear.INSTANCES));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES);
        assertTrue(instance.isSupported(IOADayOfYear.INSTANCES));
        assertFalse(instance.isSupported(IDays.INSTANCES));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        Period period = instance.toPeriod();
        assertEquals(521, period.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod instance = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        MutablePeriod mutablePeriod = instance.toMutablePeriod();
        assertEquals(521, mutablePeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod instance1 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        BaseSingleFieldPeriod instance2 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod instance1 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        BaseSingleFieldPeriod instance2 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod instance1 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        BaseSingleFieldPeriod instance2 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(520);
        assertEquals(1, instance1.compareTo(instance2));

        BaseSingleFieldPeriod instance3 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        BaseSingleFieldPeriod instance4 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(521);
        assertEquals(0, instance3.compareTo(instance4));

        BaseSingleFieldPeriod instance5 = new DurationField(365, IOADayOfYear.INSTANCES).withValue(520);
        assertEquals(-1, instance1.compareTo(instance5));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod instance1 = new DurationField(365, IOADayOfYear.INSTANCES);
        String instance2 = "test";
        instance1.compareTo(instance2);
    }
}