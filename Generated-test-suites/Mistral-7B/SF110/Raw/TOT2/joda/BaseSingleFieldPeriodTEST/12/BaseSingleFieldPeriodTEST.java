package org.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testgetValue() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(10);
        int actual = testObject.getValue();
        int expected = 10;
        assertEquals(expected, actual);
    }

    // Edge case: Minimum value
    @Test
    public void testMinValue() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(Integer.MIN_VALUE);
        int actual = testObject.getValue();
        int expected = Integer.MIN_VALUE;
        assertEquals(expected, actual);
    }

    // Edge case: Maximum value
    @Test
    public void testMaxValue() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(Integer.MAX_VALUE);
        int actual = testObject.getValue();
        int expected = Integer.MAX_VALUE;
        assertEquals(expected, actual);
    }

    // Method 1: getValue()

    // Method 2: setValue(int iPeriod)

    @Test
    public void testsetValue() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(10);
        testObject.setValue(20);
        int actual = testObject.getValue();
        int expected = 20;
        assertEquals(expected, actual);
    }

    // Edge case: Minimum value
    @Test
    public void testMinValue_setValue() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(Integer.MIN_VALUE);
        testObject.setValue(5);
        int actual = testObject.getValue();
        int expected = 5;
        assertEquals(expected, actual);
    }

    // Edge case: Maximum value
    @Test
    public void testMaxValue_setValue() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(Integer.MAX_VALUE);
        testObject.setValue(-100);
        int actual = testObject.getValue();
        int expected = -100;
        assertEquals(expected, actual);
    }

    // Method 3: getFieldType()

    @Test
    public void testgetFieldType() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(10);
        DurationFieldType actual = testObject.getFieldType();
        DurationFieldType expected = MySingleFieldPeriod.FIELD_TYPE;
        assertEquals(expected, actual);
    }

    // ... test other methods
}

class MySingleFieldPeriod extends BaseSingleFieldPeriod {
    static final DurationFieldType FIELD_TYPE = DurationFieldType.MILLISECONDS;

    public MySingleFieldPeriod(int period) {
        super(period);
    }
}