import org.joda.time.BaseDateTime;
import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Months;
import org.joda.time.Years;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology ISOTemporalChronology = DateTimeUtils.ISO_INSTANT;

    @Test
    public void testBasicGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        int expected = 1;
        int actual = field.get(instant);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        int months = 1;
        long expected = 1694972800000L; // 2014-02-01T00:00:00.0Z
        long actual = field.add(instant, months);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        int months = 13;
        long expected = 1697433600000L; // 2014-12-01T00:00:00.0Z
        long actual = field.addWrapField(instant, months);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicGetDifference() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long minuendInstant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        long subtrahendInstant = 1694972800000L; // 2014-02-01T00:00:00.0Z
        int expected = -1;
        int actual = field.getDifferenceAsInt(minuendInstant, subtrahendInstant);
        Assert.assertEquals(expected, actual);

        long instant = 1694972800000L; // 2014-02-01T00:00:00.0Z
        minuendInstant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        expected = 1;
        actual = field.getDifferenceAsInt(subtrahendInstant, minuendInstant);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        int month = 2;
        long expected = 1694838400000L; // 2014-02-01T00:00:00.0Z
        long actual = field.set(instant, month);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        DurationField expected = Years.ONE;
        DurationField actual = field.getRangeDurationField();
        Assert.assertSame(expected, actual);
    }

    @Test
    public void testBasicIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        boolean expected = false;
        boolean actual = field.isLeap(instant);
        Assert.assertEquals(expected, actual);

        instant = 1697952000000L; // 2015-02-29T00:00:00.0Z
        expected = true;
        actual = field.isLeap(instant);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        int expected = 0;
        int actual = field.getLeapAmount(instant);
        Assert.assertEquals(expected, actual);

        instant = 1697952000000L; // 2015-02-29T00:00:00.0Z
        expected = 1;
        actual = field.getLeapAmount(instant);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        DurationField expected = Days.ONE;
        DurationField actual = field.getLeapDurationField();
        Assert.assertSame(expected, actual);
    }

    @Test
    public void testBasicGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        int expected = DateTimeConstants.JANUARY;
        int actual = field.getMinimumValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        int expected = DateTimeConstants.DECEMBER;
        int actual = field.getMaximumValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        long expected = 1680979200000L;
        long actual = field.roundFloor(instant);
        Assert.assertEquals(expected, actual);

        instant = 1681011200000L; // 2014-01-01T01:00:00.0Z
        expected = 1680979200000L;
        actual = field.roundFloor(instant);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        long expected = 0L;
        long actual = field.remainder(instant);
        Assert.assertEquals(expected, actual);

        instant = 1681011200000L; // 2014-01-01T01:00:00.0Z
        expected = 3600000L;
        actual = field.remainder(instant);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicIsLeapYear() {
        Chronology chronology = ISOTemporalChronology;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        int year = 2000;
        boolean expected = false;
        boolean actual = field.isLeap(chronology.getMillis(new BaseDateTime(year, 1, 1, 0, 0, 0, 0).toDate()));
        Assert.assertEquals(expected, actual);

        year = 2004;
        expected = true;
        actual = field.isLeap(chronology.getMillis(new BaseDateTime(year, 2, 29, 0, 0, 0, 0).toDate()));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicAddMonths() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z
        int months = 12;
        long expected = 1709113600000L; // 2015-01-01T00:00:00.0Z
        long actual = field.add(instant, months);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicAddReadablePartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z

        LocalDate date = new LocalDate(instant);
        LocalDateTime time = new LocalDateTime(instant);
        LocalTime localTime = new LocalTime(instant, 0, 0);

        ReadablePartial readablePartial = new LocalDate(DateTimeConstants.JANUARY, 31, 2014);
        int fieldIndex = FieldUtils.getFieldIndex(readablePartial.getClass(), "monthOfYear");
        int[] values = {1};
        int valueToAdd = -1;

        long expected = 1677756800000L; // 2013-12-31T00:00:00.0Z
        long actual = field.add(readablePartial, fieldIndex, values, valueToAdd).getMillis();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBasicAddWrapFieldReadablePartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOTemporalChronology, DateTimeConstants.JANUARY);
        long instant = 1680979200000L; // 2014-01-01T00:00:00.0Z

        LocalDate date = new LocalDate(instant);
        LocalDateTime time = new LocalDateTime(instant);
        LocalTime localTime = new LocalTime(instant, 0, 0);

        ReadablePartial readablePartial = new LocalDate(DateTimeConstants.DECEMBER, 31, 2014);
        int fieldIndex = FieldUtils.getFieldIndex(readablePartial.getClass(), "monthOfYear");
        int[] values = {13};
        int valueToAdd = 0;

        long expected = 1717752800000L; // 2016-01-31T00:00:00.0Z
        long actual = field.add(readablePartial, fieldIndex, values, valueToAdd).getMillis();
        Assert.assertEquals(expected, actual);
    }
}