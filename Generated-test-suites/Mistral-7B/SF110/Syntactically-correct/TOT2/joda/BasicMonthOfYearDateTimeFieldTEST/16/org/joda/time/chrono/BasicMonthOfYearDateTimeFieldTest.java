
import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        DateTime date = new DateTime(2022, 3, 15);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        int expectedMonth = 3; // For March
        int actualMonth = field.get(date.getMillis());
        assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testIsLeapForNonLeapYear() {
        DateTime date = new DateTime(2015, 2, 28);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        boolean expectedLeap = false;
        boolean actualLeap = field.isLeap(date.getMillis());
        assertEquals(expectedLeap, actualLeap);
    }

    @Test
    public void testIsLeapForLeapYear() {
        DateTime date = new DateTime(2020, 2, 29);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        boolean expectedLeap = true;
        boolean actualLeap = field.isLeap(date.getMillis());
        assertEquals(expectedLeap, actualLeap);
    }

    @Test
    public void testIsLeapForNonMonthOfLeapYear() {
        DateTime date = new DateTime(2020, 1, 31);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        boolean expectedLeap = false;
        boolean actualLeap = field.isLeap(date.getMillis());
        assertEquals(expectedLeap, actualLeap);
    }

    @Test
    public void testIsLeapForMonthOfLeapYear() {
        DateTime date = new DateTime(2020, 2, 29);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        boolean expectedLeap = true;
        boolean actualLeap = field.isLeap(date.getMillis());
        assertEquals(expectedLeap, actualLeap);
    }

    @Test
    public void testAddMethodWithNegativeAmount() {
        DateTime date = new DateTime(2022, 3, 15);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        long expectedDate = date.minusMonths(2).getMillis(); // Expected date after subtracting two months
        long actualDate = field.add(date.getMillis(), -2);
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNegativeAmountToNegativeDate() {
        DateTime date = new DateTime(-3650000, DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        long expectedDate = -3650000; // Negative date
        long actualDate = field.add(expectedDate, -12); // Adding 12 months to a negative date should throw an IllegalArgumentException
        fail("Expected an IllegalArgumentException but got: " + actualDate);
    }

    @Test
    public void testAddWithZeroAmount() {
        DateTime date = new DateTime(2022, 3, 15);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        long expectedDate = date.getMillis();
        long actualDate = field.add(expectedDate, 0);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testAddWithValidAmount() {
        DateTime date = new DateTime(2022, 3, 15);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        long expectedDate = date.plusMonths(1).getMillis(); // Expected date after adding one month
        long actualDate = field.add(date.getMillis(), 1);
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithInvalidAmount() {
        DateTime date = new DateTime(2022, 3, 15);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        long expectedDate = date.getMillis(); // Expected initial date
        long actualDate = field.add(expectedDate, Integer.MAX_VALUE); // Adding too many months should throw an IllegalArgumentException
        fail("Expected an IllegalArgumentException but got: " + actualDate);
    }

    @Test
    public void testSetMethod() {
        DateTime date = new DateTime(2022, 3, 15);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        long expectedDate = date.plusMonths(2).getMillis(); // Expected date after adding two months
        long actualDate = field.set(date.getMillis(), 3); // Setting the month to 3 (April)
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapFieldWithNegativeAmount() {
        DateTime date = new DateTime(2022, 3, 15);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(date.getChronology(), -1);
        long expectedDate = date.getMillis(); // Expected initial date
        long actualDate = field.addWrapField(expectedDate, -12); // Wrapping a negative amount should throw an IllegalArgumentException
        fail("Expected an IllegalArgumentException but got: " + actualDate);
    }
}
