import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Fields;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet_ValidDate() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        int expected = DateTimeConstants.FEBRUARY;
        int actual = field.get(date);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd_ValidDates_NoWrap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        long expected = new LocalDate(2023, 4, 30).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        long actual = field.add(date, 2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd_ValidDates_Wrap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 12, 31).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        long expected = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        long actual = field.add(date, -1);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_InvalidArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        field.add(date, Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapField_InvalidArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        field.addWrapField(date, Integer.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_InvalidArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        field.set(date, Integer.MAX_VALUE + 1);
    }

    @Test
    public void testSet_ValidDate() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        int expected = DateTimeConstants.APRIL;
        long actual = field.set(date, 2);
        Assert.assertEquals(expected, Fields.monthOfYear().get(new LocalDateTime(actual)));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        DurationFieldType fieldType = DurationFieldType.years();
        DurationField rangeDurationField = field.getRangeDurationField();
        long duration = rangeDurationField.getMillis(1);
        int expected = 12;
        int actual = (int) Math.abs(DurationFieldType.months().getMillis(expected));
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(duration, actual * fieldType.getMillisPerUnit());
    }

    @Test
    public void testIsLeap_ValidDate() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        boolean expected = true;
        boolean actual = field.isLeap(date);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount_ValidDate() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        int expected = 1;
        int actual = field.getLeapAmount(date);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        DurationFieldType fieldType = DurationFieldType.days();
        DurationField leapDurationField = field.getLeapDurationField();
        long duration = leapDurationField.getMillis(1);
        int expected = 1;
        Assert.assertEquals(expected, Fields.daysOfMonth().get(new LocalDate(2023, 2, 28).getYear(), 2).length);
        Assert.assertEquals(duration, expected * fieldType.getMillisPerUnit());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        int expected = 1;
        int actual = field.getMinimumValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        int expected = 12;
        int actual = field.getMaximumValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor_ValidDate() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 28).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        long expected = new LocalDate(2023, 2, 1).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        long actual = field.roundFloor(date);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemainder_ValidDate() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY_LEAP_MONTH);
        long date = new LocalDate(2023, 2, 29).toDateTimeAtStartOfDay(DateTimeUtils.UTC).getMillis();
        long expected = 86400000L; // One day in milliseconds
        long actual = field.remainder(date);
        Assert.assertEquals(expected, actual);
    }
}