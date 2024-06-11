import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.ChronologyUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        LocalDateTime date = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        long millis = date.getMillis();

        int month = field.get(millis);
        assertEquals(2, month);

        date = new LocalDateTime(2022, 12, 31, 12, 0, 0, 0);
        millis = date.getMillis();

        month = field.get(millis);
        assertEquals(12, month);

        date = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        millis = date.getMillis();

        month = field.get(millis);
        assertEquals(1, month);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        LocalDateTime date = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        long millis = date.getMillis();

        long newMillis = field.add(millis, 1);
        LocalDateTime newDate = new LocalDateTime(newMillis);

        int month = field.get(newMillis);
        assertEquals(3, month);

        millis = newDate.getMillis();

        LocalDate newDate2 = new LocalDate(newMillis);
        assertEquals(newDate2.getMonthOfYear(), 3);

        newMillis = field.add(millis, 11);
        LocalDateTime newDate3 = new LocalDateTime(newMillis);

        month = field.get(newMillis);
        assertEquals(14, month);

        millis = newDate3.getMillis();

        newDate2 = new LocalDate(newMillis);
        assertEquals(newDate2.getMonthOfYear(), 14);

        date = new LocalDateTime(2022, 1, 31, 12, 0, 0, 0);
        millis = date.getMillis();

        newMillis = field.add(millis, -12);
        LocalDateTime newDate4 = new LocalDateTime(newMillis);

        month = field.get(newMillis);
        assertEquals(11, month);

        millis = newDate4.getMillis();

        newDate2 = new LocalDate(newMillis);
        assertEquals(newDate2.getMonthOfYear(), 11);

        assertThrows(IllegalArgumentException.class, () -> field.add(millis, 366));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long millis = 0L;

        millis = field.addWrapField(millis, 13);
        assertEquals(26, field.get(millis) % 12 + 1);

        millis = field.addWrapField(millis, -13);
        assertEquals(1, field.get(millis) % 12 + 1);

        millis = Long.MIN_VALUE;

        millis = field.addWrapField(millis, 12);
        assertEquals(11, field.get(millis) % 12 + 1);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        LocalDateTime date1 = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        long millis1 = date1.getMillis();

        LocalDateTime date2 = new LocalDateTime(2022, 3, 31, 12, 0, 0, 0);
        long millis2 = date2.getMillis();

        long difference = field.getDifferenceAsLong(millis1, millis2);
        assertEquals(1, difference);

        date2 = new LocalDateTime(2022, 3, 30, 12, 0, 0, 0);
        millis2 = date2.getMillis();

        difference = field.getDifferenceAsLong(millis1, millis2);
        assertEquals(0, difference);

        date1 = new LocalDateTime(2022, 2, 27, 12, 0, 0, 0);
        millis1 = date1.getMillis();

        difference = field.getDifferenceAsLong(millis2, millis1);
        assertEquals(-1, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long millis = 0L;

        long newMillis = field.set(millis, 13);
        assertEquals(26, field.get(newMillis) % 12 + 1);

        newMillis = field.set(newMillis, 1);
        assertEquals(1, field.get(newMillis) % 12 + 1);

        date = new LocalDateTime(2022, 3, 31, 12, 0, 0, 0);
        millis = date.getMillis();

        newMillis = field.set(millis, 0);
        assertEquals(1, field.get(newMillis) % 12 + 1);

        newMillis = field.set(millis, 13 * 12 + 11);
        assertEquals(25, field.get(newMillis) % 12 + 1);

        date = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0);
        millis = date.getMillis();

        newMillis = field.set(millis, 12 * 12 + 1);
        assertEquals(1, field.get(newMillis) % 12 + 1);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        DurationFieldType durationField = field.getRangeDurationField();
        assertEquals(DurationFieldType.years(), durationField);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        LocalDateTime date = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        long millis = date.getMillis();

        boolean isLeap = field.isLeap(millis);
        assertFalse(isLeap);

        date = new LocalDateTime(2022, 2, 29, 12, 0, 0, 0);
        millis = date.getMillis();

        isLeap = field.isLeap(millis);
        assertTrue(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        LocalDateTime date = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        long millis = date.getMillis();

        int leapAmount = field.getLeapAmount(millis);
        assertEquals(0, leapAmount);

        date = new LocalDateTime(2022, 2, 29, 12, 0, 0, 0);
        millis = date.getMillis();

        leapAmount = field.getLeapAmount(millis);
        assertEquals(1, leapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        DurationFieldType durationField = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), durationField);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        int minimumValue = field.getMinimumValue();
        assertEquals(1, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        int maximumValue = field.getMaximumValue();
        assertEquals(12, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        LocalDateTime date = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        long millis = date.getMillis();

        long roundFloor = field.roundFloor(millis);
        assertEquals(new LocalDateTime(2022, 2, 28, 12, 0, 0, 0).getMillis(), roundFloor);

        date = new LocalDateTime(2022, 3, 1, 12, 0, 0, 0);
        millis = date.getMillis();

        roundFloor = field.roundFloor(millis);
        assertEquals(new LocalDateTime(2022, 3, 1, 12, 0, 0, 0).getMillis(), roundFloor);

        date = new LocalDateTime(2022, 2, 29, 12, 0, 0, 0);
        millis = date.getMillis();

        roundFloor = field.roundFloor(millis);
        assertEquals(new LocalDateTime(2022, 3, 1, 12, 0, 0, 0).getMillis(), roundFloor);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        LocalDateTime date = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        long millis = date.getMillis();

        long remainder = field.remainder(millis);
        assertEquals(0, remainder);

        date = new LocalDateTime(2022, 2, 29, 12, 0, 0, 0);
        millis = date.getMillis();

        remainder = field.remainder(millis);
        assertEquals(1, remainder);

        date = new LocalDateTime(2022, 3, 1, 12, 0, 0, 0);
        millis = date.getMillis();

        remainder = field.remainder(millis);
        assertEquals(0, remainder);
    }
}