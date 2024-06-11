package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.field.*;
import org.joda.time.chrono.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_Constructor_Int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(42, period.getValue());
        assertNotNull(period.getFieldType());
        assertSame(PeriodType.instance(), period.getPeriodType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Constructor_NullPeriodType() {
        new BaseSingleFieldPeriod(null);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(PeriodType.instance(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_WrongIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        period.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_WrongIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        period.getValue(1);
    }

    @Test
    public void testGet_FieldTypeMatches() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(fieldType, period.getFieldType());
        assertEquals(fieldType, period.getFieldType(0));
    }

    @Test
    public void testGet_FieldTypeDoesntMatch() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        DurationFieldType wrongFieldType = DurationFieldType.hours();
        assertFalse(wrongFieldType.equals(period.getFieldType()));
        assertFalse(wrongFieldType.equals(period.getFieldType(0)));
    }

    @Test
    public void testEquals_SameInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = period1;
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testEquals_SameValue_SameType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testEquals_SameValue_DifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42, DurationFieldType.days());
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42, DurationFieldType.weeks());
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEquals_DifferentValues() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(43);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(43);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareTo_SameInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = period1;
        assertEquals(0, period1.compareTo(period2));
    }

    @Test
    public void testCompareTo_SameValue_SameType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42);
        assertEquals(0, period1.compareTo(period2));
    }

    @Test
    public void testCompareTo_DifferentValues_SameType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(43);
        assertEquals(-1, period1.compareTo(period2));
    }

    @Test
    public void testCompareTo_DifferentValues_DifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42, DurationFieldType.days());
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(43, DurationFieldType.weeks());
        assertEquals(-1, period1.compareTo(period2));
    }

    @Test
    public void testCompareTo_Null() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertNull(period.compareTo(null));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        Period result = period.toPeriod();
        assertEquals(Period.ZERO, result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period result = mutablePeriod.toPeriod();
        assertEquals(new Period(42, DurationFieldType.days()), result);
    }

    @Test
    public void testBetween_Instant_Instant_DurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42, DurationFieldType.days());
        Instant instant1 = new DateTime().toInstant();
        Instant instant2 = instant1.plus(Duration.standardDays(42));
        assertEquals(42, BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullInstant() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42, DurationFieldType.days());
        Instant instant1 = null;
        Instant instant2 = new DateTime().toInstant();
        BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullInstant2() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42, DurationFieldType.days());
        Instant instant1 = new DateTime().toInstant();
        Instant instant2 = null;
        BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_DifferentFieldTypes() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42, DurationFieldType.days());
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42, DurationFieldType.weeks());
        Instant instant1 = new DateTime().toInstant();
        Instant instant2 = new DateTime().toInstant();
        BaseSingleFieldPeriod.between(instant1, instant2, period1.getFieldType());
        BaseSingleFieldPeriod.between(instant1, instant2, period2.getFieldType());
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42, DurationFieldType.days());
        long n = 7;
        assertEquals(604800, BaseSingleFieldPeriod.standardPeriodIn(period, n));
    }
}