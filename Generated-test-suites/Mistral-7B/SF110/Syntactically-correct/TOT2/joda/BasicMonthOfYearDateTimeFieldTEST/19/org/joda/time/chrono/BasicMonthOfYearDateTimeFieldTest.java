import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long testDate = new GregorianDate(2012, 11, 30).getTime(); // December is the 12th month
        int expected = 12;
        int actual = field.get(testDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 1);
        int minValue = field.getMinimumValue();
        assertEquals(1, minValue);
    }

    @Test
    public void testMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 1);
        int maxValue = field.getMaximumValue();
        assertEquals(12, maxValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValues() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 1);
        long negativeTestDate = new GregorianDate(-100, 11, 30).getTime();
        int actual = field.get(negativeTestDate);
        assertEquals(-1, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutOfRangeValues() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 1);
        long outOfRangeTestDate = new GregorianDate(3000, 13, 30).getTime();
        int actual = field.get(outOfRangeTestDate);
        assertEquals(0, actual);
    }

}