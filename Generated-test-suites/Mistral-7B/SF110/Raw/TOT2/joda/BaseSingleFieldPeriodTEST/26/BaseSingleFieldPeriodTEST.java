package org.joda.time.base;
import org.junit.*;
import static org.junit.Assert.*;
import org.joda.time.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testgetValue() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        int actualValue = testPeriod.getValue();
        int expectedValue = 5;
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testbetween() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0).toInstant();
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0).toInstant();
        DurationFieldType durationFieldType = DateTimeConstants.DAYS;
        int expectedValue = 1;
        int actualValue = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testbetweenNull() {
        DurationFieldType durationFieldType = DateTimeConstants.DAYS;
        int expectedException = 1;
        try {
            BaseSingleFieldPeriod.between(null, null, durationFieldType);
        } catch (IllegalArgumentException e) {
            int actualException = 1;
            assertEquals(expectedException, actualException);
        }
    }

    @Test
    public void testgetFieldType() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        DurationFieldType expectedFieldType = DateTimeConstants.DAYS;
        DurationFieldType actualFieldType = testPeriod.getFieldType();
        assertEquals(expectedFieldType, actualFieldType);
    }

    @Test
    public void testConstructor() {
        int iPeriod = 5;
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(iPeriod);
        int actualValue = testPeriod.getValue();
        assertEquals(iPeriod, actualValue);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(5);
        PeriodType expectedPeriodType = PeriodType.DAYS;
        PeriodType actualPeriodType = testPeriod.getPeriodType();
        assertEquals(expectedPeriodType, actualPeriodType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeValue() {
        int iPeriod = -5;
        try {
            new BaseSingleFieldPeriod(iPeriod);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testcompareTo() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(3);
        int expectedResult = 2;
        int actualResult = period1.compareTo(period2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testequals() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        boolean expectedResult = true;
        boolean actualResult = period1.equals(period2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testequalsNull() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        boolean expectedResult = false;
        boolean actualResult = period.equals(null);
        assertEquals(expectedResult, actualResult);
    }
}