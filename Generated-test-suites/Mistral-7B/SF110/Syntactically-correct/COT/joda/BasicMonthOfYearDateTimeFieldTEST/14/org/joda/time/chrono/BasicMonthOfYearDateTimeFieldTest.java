import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        int actual = field.get(date);
        int expected = DateTimeConstants.JANUARY;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddWithValidInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        long expected = this.getExpectedValue(DateTimeConstants.FEBRUARY, 1, date);
        long actual = field.add(date, 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddWithNegativeInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        long expected = this.getExpectedValue(DateTimeConstants.DECEMBER, -1, date);
        long actual = field.add(date, -1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddWithLargeInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        long expected = this.getExpectedValue(DateTimeConstants.FEBRUARY, 12, date);
        long actual = field.add(date, 12);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithTooLargeInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        long input = Long.MAX_VALUE + 1;
        field.add(date, input);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        int input = 13;
        long expected = this.getExpectedValue(DateTimeConstants.FEBRUARY, 1, date);
        long actual = field.addWrapField(date, input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date1 = BaseDateTime.getInstance().getMillis();
        long date2 = BaseDateTime.getInstance().plusYears(1).getMillis();
        long expected = 12;
        long actual = field.getDifferenceAsLong(date1, date2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        int input = DateTimeConstants.FEBRUARY;
        long expected = this.getExpectedValue(DateTimeConstants.FEBRUARY, 1, date);
        long actual = field.set(date, input);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        int input = 0;
        field.set(date, input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidInput2() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        int input = 13;
        field.set(date, input);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.FEBRUARY);

        long date = BaseDateTime.getInstance().getMillis();
        boolean actual = field.isLeap(date);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.FEBRUARY);

        long date = BaseDateTime.getInstance().getMillis();
        int actual = field.getLeapAmount(date);
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        int actual = field.getMaximumValue();
        int expected = 12;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        long expected = this.getExpectedValue(DateTimeConstants.JANUARY, 1, date);
        long actual = field.roundFloor(date);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

        long date = BaseDateTime.getInstance().getMillis();
        long expected = 0;
        long actual = field.remainder(date);
        Assert.assertEquals(expected, actual);
    }

    private long getExpectedValue(int month, int dayOfMonth, long date) {
        return new BaseDateTime(JulianChronology.INSTANCE, year, month, dayOfMonth, 0, 0, 0).getMillis();
    }
}