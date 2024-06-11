import org.joda.time.JodaTimeTestCase;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.DateTimeFieldType;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    private static final int TEST_YEAR = 2022;
    private static final int TEST_LEAP_MONTH = 2; // February
    private static final int TEST_MAX_MONTH = 12;

    @org.junit.Before
    public void setUp() {
        this.setUp(TEST_YEAR, new BasicGregorianChronology());
    }

    public void testBasicMonthOfYearDateTimeFieldConstruction() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH);
        assertNotNull(field);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(TEST_MAX_MONTH, field.getMaximumValue());
        assertEquals(false, field.isLenient());
    }

    public void testGetMonth() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);
        int expectedMonth = 3;
        int actualMonth = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).get(date.getMillis());
        assertEquals(expectedMonth, actualMonth);

        date = new LocalDateTime(TEST_YEAR, 12, 31, 0, 0);
        expectedMonth = 12;
        actualMonth = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).get(date.getMillis());
        assertEquals(expectedMonth, actualMonth);

        date = new LocalDateTime(TEST_YEAR, TEST_LEAP_MONTH, 29, 0, 0);
        expectedMonth = TEST_LEAP_MONTH;
        actualMonth = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).get(date.getMillis());
        assertEquals(expectedMonth, actualMonth);
    }

    public void testAddMonths() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        int expectedMonth = 4;
        long actualMillis = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).add(date.getMillis(), 1);
        assertEquals(expectedMonth, new LocalDateTime(actualMillis).getMonthOfYear());

        expectedMonth = 12;
        actualMillis = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).add(date.getMillis(), TEST_MAX_MONTH + 1);
        assertEquals(expectedMonth, new LocalDateTime(actualMillis).getMonthOfYear());

        expectedMonth = 3;
        actualMillis = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).add(date.getMillis(), TEST_MAX_MONTH + 1 - 3);
        assertEquals(expectedMonth, new LocalDateTime(actualMillis).getMonthOfYear());
    }

    public void testAddYears() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        int expectedYear = TEST_YEAR + 1;
        int expectedMonth = 1;
        long actualMillis = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).add(date.getMillis(), 12);
        assertEquals(expectedYear, new LocalDateTime(actualMillis).getYear());
        assertEquals(expectedMonth, new LocalDateTime(actualMillis).getMonthOfYear());

        long expectedMillis2 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).add(date.getMillis(), 5);
        int y2 = new LocalDateTime(expectedMillis2).getYear();
        int m2 = new LocalDateTime(expectedMillis2).getMonthOfYear();
        assertEquals(expectedYear, y2);
        assertEquals(expectedMonth, m2);
        assertEquals(expectedMonth + 5, m2);
    }

    public void testAddWrapped() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        long expectedMillis1 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).addWrapField(date.getMillis(), 12);
        int y1 = new LocalDateTime(expectedMillis1).getYear();
        int m1 = new LocalDateTime(expectedMillis1).getMonthOfYear();
        assertEquals(y1, TEST_YEAR + 1);
        assertEquals(m1, 1);

        long expectedMillis2 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).addWrapField(date.getMillis(), TEST_MAX_MONTH + 12);
        int y2 = new LocalDateTime(expectedMillis2).getYear();
        int m2 = new LocalDateTime(expectedMillis2).getMonthOfYear();
        assertEquals(y2, TEST_YEAR + 2);
        assertEquals(m2, 1);
    }

    public void testSetMonth() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        long expectedMillis1 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).set(date.getMillis(), 4);
        assertEquals(TEST_YEAR, new LocalDateTime(expectedMillis1).getYear());
        assertEquals(4, new LocalDateTime(expectedMillis1).getMonthOfYear());

        long expectedMillis2 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).set(date.getMillis(), TEST_MAX_MONTH + 1);
        assertEquals(TEST_YEAR + 1, new LocalDateTime(expectedMillis2).getYear());
        assertEquals(TEST_MAX_MONTH + 1, new LocalDateTime(expectedMillis2).getMonthOfYear());
    }

    public void testGetRange() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        DurationField expectedDuration = new BasicDurationField(1, DateTimeUnit.YEARS);
        DurationField actualDuration = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).getRangeDurationField();
        assertEquals(expectedDuration, actualDuration);
    }

    public void testIsLeap() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        assertTrue(new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).isLeap(date.getMillis()));
        assertFalse(new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).isLeap(date.getMillis() - 1));
    }

    public void testGetLeapAmount() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        assertEquals(0, new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).getLeapAmount(date.getMillis()));
        assertEquals(1, new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).getLeapAmount(date.getMillis() - 1));
    }

    public void testGetLeapDurationField() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        DurationField expectedDuration = new BasicDurationField(1, DateTimeUnit.DAYS);
        DurationField actualDuration = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).getLeapDurationField();
        assertEquals(expectedDuration, actualDuration);
    }

    public void testGetMinimumValue() {
        assertEquals(1, new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).getMinimumValue());
    }

    public void testGetMaximumValue() {
        assertEquals(TEST_MAX_MONTH, new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).getMaximumValue());
    }

    public void testRoundFloor() {
        LocalDateTime startDate = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);
        LocalDateTime endDate = new LocalDateTime(TEST_YEAR, TEST_MAX_MONTH, TEST_MAX_DAY_OF_MONTH, 23, 55, 59);

        long startMillis = startDate.getMillis();
        long endMillis = endDate.getMillis();

        long expectedMillis1 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).roundFloor(startMillis);
        long expectedMillis2 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).roundFloor(endMillis);

        assertEquals(startMillis, expectedMillis1);
        assertEquals(endMillis, expectedMillis2);
    }

    public void testRemainder() {
        LocalDateTime date = new LocalDateTime(TEST_YEAR, 1, 31, 0, 0);

        long expectedMillis1 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).roundFloor(date.getMillis()) + 1;
        long actualMillis = date.getMillis();
        long expectedRemainder = expectedMillis - new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).roundFloor(date.getMillis());
        long actualRemainder = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).remainder(actualMillis);
        assertEquals(expectedRemainder, actualRemainder);

        date = new LocalDateTime(TEST_YEAR, TEST_MAX_MONTH, TEST_MAX_DAY_OF_MONTH, 0, 0, 0);
        expectedMillis1 = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).roundFloor(date.getMillis()) + TEST_MAX_DAY_OF_MONTH;
        expectedMillis = date.getMillis();
        expectedRemainder = expectedMillis - new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).roundFloor(date.getMillis());
        actualRemainder = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), TEST_LEAP_MONTH).remainder(actualMillis);
        assertEquals(expectedRemainder, actualRemainder);
    }
}