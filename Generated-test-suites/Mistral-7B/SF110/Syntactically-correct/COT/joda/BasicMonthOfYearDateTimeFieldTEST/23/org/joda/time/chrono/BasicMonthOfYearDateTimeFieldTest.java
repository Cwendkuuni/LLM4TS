import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);
        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();
        int expectedMonth = 2;

        int actualMonth = field.get(testDate);
        Assert.assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();
        long addAmount = 2;

        long expectedResult = new LocalDateTime(2022, 4, 28, 12, 0, 0).toInstant().getMillis();
        long result = field.add(testDate, addAmount);

        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithIllegalArgumentException() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();
        long addAmount = Long.MAX_VALUE;

        field.add(testDate, addAmount);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();
        int addAmount = 13;

        long expectedResult = new LocalDateTime(2022, 3, 28, 12, 0, 0).toInstant().getMillis();
        long result = field.addWrapField(testDate, addAmount);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate1 = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();
        long testDate2 = new LocalDateTime(2022, 4, 30, 12, 0, 0).toInstant().toEpochMilli();

        long expectedResult = 2;
        long result = field.getDifferenceAsLong(testDate1, testDate2);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();
        int newMonth = 4;

        long expectedResult = new LocalDateTime(2022, 4, 28, 12, 0, 0).toInstant().getMillis();
        long result = field.set(testDate, newMonth);

        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithIllegalArgumentException() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();
        int newMonth = 13;

        field.set(testDate, newMonth);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        DurationFieldType expectedResult = DurationFieldType.years();
        DurationFieldType result = field.getRangeDurationField();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();

        boolean expectedResult = false;
        boolean result = field.isLeap(testDate);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 2, 28, 12, 0, 0).toInstant().toEpochMilli();

        int expectedResult = 0;
        int result = field.getLeapAmount(testDate);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        DurationFieldType expectedResult = DurationFieldType.days();
        DurationFieldType result = field.getLeapDurationField();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        int expectedResult = 1;
        int result = field.getMinimumValue();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        int expectedResult = 12;
        int result = field.getMaximumValue();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 3, 2, 12, 0, 0).toInstant().toEpochMilli();

        long expectedResult = new LocalDateTime(2022, 3, 1, 12, 0, 0).toInstant().getMillis();
        long result = field.roundFloor(testDate);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);

        long testDate = new LocalDateTime(2022, 3, 3, 12, 0, 0).toInstant().toEpochMilli();

        long expectedResult = 2;
        long result = field.remainder(testDate);

        Assert.assertEquals(expectedResult, result);
    }
}