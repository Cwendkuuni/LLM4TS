import org.joda.time.BaseTestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;

public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    private static final int TEST_YEAR = 2023;
    private static final int TEST_MONTH = 3;
    private static final int TEST_LEAP_MONTH = 2;
    private static final int TEST_DAY = 28;
    private static final long TEST_MILLIS = LocalDateTime.of(TEST_YEAR, TEST_MONTH, TEST_DAY, 12, 0).toInstant().toEpochMilli();

    @org.junit.Test
    public void testGet() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        int actual = field.get(TEST_MILLIS);
        assertEquals(TEST_MONTH, actual);
    }

    @org.junit.Test
    public void testAdd() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        long actual = field.add(TEST_MILLIS, 1);
        LocalDateTime expected = new LocalDateTime(TEST_YEAR, TEST_MONTH + 1, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);

        actual = field.add(TEST_MILLIS, -1);
        expected = new LocalDateTime(TEST_YEAR, TEST_MONTH - 1, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);

        actual = field.add(TEST_MILLIS, 12);
        expected = new LocalDateTime(TEST_YEAR + 1, TEST_MONTH, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);

        actual = field.add(TEST_MILLIS, -12);
        expected = new LocalDateTime(TEST_YEAR - 1, TEST_MONTH, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);
    }

    @org.junit.Test
    public void testAddLong() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        long actual = field.add(TEST_MILLIS, 5L);
        LocalDateTime expected = new LocalDateTime(TEST_YEAR, TEST_MONTH + 5, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);

        actual = field.add(TEST_MILLIS, -5L);
        expected = new LocalDateTime(TEST_YEAR, TEST_MONTH - 5, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);
    }

    @org.junit.Test
    public void testAddWrapField() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        long actual = field.addWrapField(TEST_MILLIS, 14);
        LocalDateTime expected = new LocalDateTime(TEST_YEAR, TEST_MONTH + 2, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);
    }

    @org.junit.Test
    public void testSet() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        long actual = field.set(TEST_MILLIS, TEST_MONTH + 1);
        LocalDateTime expected = new LocalDateTime(TEST_YEAR, TEST_MONTH + 1, 1, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);
    }

    @org.junit.Test
    public void testGetRangeDurationField() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        DurationField actual = field.getRangeDurationField();
        DurationField expected = new org.joda.time.DurationField(DateTimeFieldType.years(), 1);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testIsLeap() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        boolean actual = field.isLeap(TEST_MILLIS);
        assertTrue(TEST_LEAP_MONTH == 2 && chronology.isLeapYear(TEST_YEAR), actual);

        actual = field.isLeap(TEST_MILLIS + 86400000L);
        assertFalse(TEST_LEAP_MONTH == 2 && chronology.isLeapYear(TEST_YEAR), actual);
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        int actual = field.getLeapAmount(TEST_MILLIS);
        assertEquals(TEST_LEAP_MONTH == 2 && chronology.isLeapYear(TEST_YEAR) ? 1 : 0, actual);
    }

    @org.junit.Test
    public void testGetLeapDurationField() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        DurationField actual = field.getLeapDurationField();
        DurationField expected = new org.joda.time.DurationField(DateTimeFieldType.days(), 1);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetMinimumValue() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        int actual = field.getMinimumValue();
        assertEquals(1, actual);
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        int actual = field.getMaximumValue();
        assertEquals(chronology.getMaxMonth(), actual);
    }

    @org.junit.Test
    public void testRoundFloor() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        long actual = field.roundFloor(TEST_MILLIS);
        LocalDateTime expected = new LocalDateTime(TEST_YEAR, TEST_MONTH, TEST_DAY, 0, 0);
        assertEquals(expected.toInstant().toEpochMilli(), actual);
    }

    @org.junit.Test
    public void testRemainder() {
        Chronology chronology = ISOChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
        long actual = field.remainder(TEST_MILLIS);
        long expected = TEST_MILLIS % (chronology.getAverageMillisPerMonth() * chronology.getMaxMonth());
        assertEquals(expected, actual);
    }
}