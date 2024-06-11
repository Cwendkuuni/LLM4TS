package org.joda.time.base;

import org.junit.Test;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.mockito.Mockito;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(5);
        int expectedResult = 5;
        int actualResult = testObject.getValue();
        assert(expectedResult == actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_nullObject() {
        BaseSingleFieldPeriod testObject = null;
        int actualResult = testObject.getValue();
    }

    @Test
    public void testGetValue_negative() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(-5);
        int expectedResult = -5;
        int actualResult = testObject.getValue();
        assert(expectedResult == actualResult);
    }

    @Test
    public void testGetValue_zero() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(0);
        int expectedResult = 0;
        int actualResult = testObject.getValue();
        assert(expectedResult == actualResult);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(5);
        DurationFieldType mockFieldType = Mockito.mock(DurationFieldType.class);
        testObject = new MySingleFieldPeriod(5, mockFieldType);
        Mockito.verify(mockFieldType, Mockito.times(1)).getValue();
    }

    @Test(expected = NullPointerException.class)
    public void testGetFieldType_nullObject() {
        BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(5);
        testObject = new MySingleFieldPeriod(5, null);
    }
}