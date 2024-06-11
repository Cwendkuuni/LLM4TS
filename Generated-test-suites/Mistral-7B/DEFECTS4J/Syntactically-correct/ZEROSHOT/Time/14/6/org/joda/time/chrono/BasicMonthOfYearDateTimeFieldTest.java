import org.joda.time.BaseTestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    private static final int TEST_MONTH_FIELD = DateTimeFieldType.monthOfYear().getCode();

    private static final int MIN_YEAR = 1;
    private static final int MAX_YEAR = 5000;
    private static final int LEAP_MONTH = DateTimeConstants.FEBRUARY;

    private Chronology chrono;
    private MonthOfYearDateTimeField monthField;

    @Override
    public void setUp() {
        chrono = new BasicChronology("TestChrono", DateTimeZone.UTC, "yyyy-MM-dd");
        monthField = new BasicMonthOfYearDateTimeField(chrono, LEAP_MONTH);
    }

    @org.junit.Test
    public void testIsLenient() {
        assertFalse(monthField.isLenient());
    }

    @org.junit.Test
    public void testGet() {
        LocalDateTime testTime = new LocalDateTime(2022, 11, 11, 12, 0, 0, 0);
        int expected = 11;
        int result = monthField.get(testTime.getMillis());
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testAdd() {
        LocalDateTime testTime = new LocalDateTime(2022, 11, 11, 12, 0, 0, 0);

        // test adding months
        long result = monthField.add(testTime.getMillis(), 12);
        LocalDateTime expected = new LocalDateTime(2023, 1, 11, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));

        // test adding months with limit test
        result = monthField.add(testTime.getMillis(), 13);
        expected = new LocalDateTime(2023, 2, 11, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));

        // test adding months with negative value
        result = monthField.add(testTime.getMillis(), -12);
        expected = new LocalDateTime(2021, 10, 11, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));
    }

    @org.junit.Test
    public void testAddWrapField() {
        LocalDateTime testTime = new LocalDateTime(2022, 12, 31, 12, 0, 0, 0);

        long result = monthField.addWrapField(testTime.getMillis(), 1);
        LocalDateTime expected = new LocalDateTime(2023, 1, 31, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));

        result = monthField.addWrapField(testTime.getMillis(), -1);
        expected = new LocalDateTime(2022, 11, 30, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));

        result = monthField.addWrapField(testTime.getMillis(), 13);
        expected = new LocalDateTime(2023, 2, 28, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));
    }

    @org.junit.Test
    public void testGetDifferenceAsLong() {
        LocalDateTime minuend = new LocalDateTime(2022, 11, 30, 12, 0, 0, 0);
        LocalDateTime subtrahend = new LocalDateTime(2022, 11, 25, 12, 0, 0, 0);

        long result = monthField.getDifferenceAsLong(minuend.getMillis(), subtrahend.getMillis());
        int expected = 5;
        assertEquals(expected, (int) result);

        minuend = new LocalDateTime(2022, 11, 28, 12, 0, 0, 0);
        subtrahend = new LocalDateTime(2022, 11, 25, 12, 0, 0, 0);

        result = monthField.getDifferenceAsLong(minuend.getMillis(), subtrahend.getMillis());
        expected = 3;
        assertEquals(expected, (int) result);

        minuend = new LocalDateTime(2022, 11, 30, 12, 0, 0, 0);
        subtrahend = new LocalDateTime(2023, 1, 31, 12, 0, 0, 0);

        result = monthField.getDifferenceAsLong(minuend.getMillis(), subtrahend.getMillis());
        expected = 112;
        assertEquals(expected, (int) result);
    }

    @org.junit.Test
    public void testSet() {
        LocalDateTime testTime = new LocalDateTime(2022, 11, 11, 12, 0, 0, 0);

        long result = monthField.set(testTime.getMillis(), 12);
        LocalDateTime expected = new LocalDateTime(2023, 1, 11, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));

        result = monthField.set(testTime.getMillis(), 0);
        expected = new LocalDateTime(2022, 1, 11, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));

        result = monthField.set(testTime.getMillis(), 13);
        expected = new LocalDateTime(2023, 2, 28, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(result));
    }

    @org.junit.Test
    public void testGetRangeDurationField() {
        DurationField result = monthField.getRangeDurationField();
        DurationField expected = Months.YEARS;
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsLeap() {
        LocalDateTime testTime = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        assertTrue(monthField.isLeap(testTime.getMillis()));

        testTime = new LocalDateTime(2022, 2, 27, 12, 0, 0, 0);
        assertFalse(monthField.isLeap(testTime.getMillis()));
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        LocalDateTime testTime = new LocalDateTime(2022, 2, 28, 12, 0, 0, 0);
        int result = monthField.getLeapAmount(testTime.getMillis());
        int expected = 1;
        assertEquals(expected, result);

        testTime = new LocalDateTime(2022, 2, 27, 12, 0, 0, 0);
        result = monthField.getLeapAmount(testTime.getMillis());
        assertEquals(expected, 0);
    }

    @org.junit.Test
    public void testGetLeapDurationField() {
        DurationField result = monthField.getLeapDurationField();
        DurationField expected = Days.ONE;
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testGetMinimumValue() {
        int result = monthField.getMinimumValue();
        int expected = DateTimeConstants.JANUARY;
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        int result = monthField.getMaximumValue();
        int expected = DateTimeConstants.DECEMBER;
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testRoundFloor() {
        LocalDateTime testTime = new LocalDateTime(2022, 11, 11, 12, 0, 0, 0);
        long result = monthField.roundFloor(testTime.getMillis());
        long expected = iChronology.getYearMonthMillis(2022, 11);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testRemainder() {
        LocalDateTime testTime = new LocalDateTime(2022, 11, 30, 12, 0, 0, 0);
        long result = monthField.remainder(testTime.getMillis());
        long expected = iChronology.getMillisOfDay(testTime.getMillis())
                % iChronology.getMillisPerDay();
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSerializable() throws Exception {
        LocalDateTime testTime = new LocalDateTime(2022, 11, 11, 12, 0, 0, 0);
        monthField = new BasicMonthOfYearDateTimeField(chrono, LEAP_MONTH);
        monthField.setValue(testTime.getMillis());

        byte[] serialized = monthField.toByteArray();
        MonthOfYearDateTimeField deserialized = (MonthOfYearDateTimeField) monthField.getClass()
                .newInstance();
        deserialized.readResolve();
        deserialized.setValue(serialized);

        LocalDateTime expected = new LocalDateTime(2023, 1, 11, 12, 0, 0, 0);
        assertEquals(expected, new LocalDateTime(deserialized.getValue()));
    }
}