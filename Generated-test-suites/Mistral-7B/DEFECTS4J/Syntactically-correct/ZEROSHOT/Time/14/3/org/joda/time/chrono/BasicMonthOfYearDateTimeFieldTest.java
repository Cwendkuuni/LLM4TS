import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.FieldVerifier;
import org.joda.time.JodaTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.TimeUtils;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private Chronology chronology;
    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        chronology = JodaTime.getDateTime("2012-01-01").getChronology();
        field = (BasicMonthOfYearDateTimeField) chronology.monthOfYear();
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        int expected = DateTimeConstants.JANUARY;
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGet_InvalidInstant() {
        try {
            field.get(-1L);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAdd_Zero() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        long expected = instant;
        long actual = field.add(instant, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd_One() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        long expected = JodaTime.getDateTime("2012-02-01").getMillisecond();
        long actual = field.add(instant, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd_NegativeOne() {
        long instant = JodaTime.getDateTime("2012-02-01").getMillisecond();
        long expected = JodaTime.getDateTime("2012-01-31").getMillisecond();
        long actual = field.add(instant, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd_MoreThanMax() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        long expected = JodaTime.getDateTime("2013-01-31").getMillisecond();
        long actual = field.add(instant, DateTimeConstants.NUM_MONTHS);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd_MoreThanMax_Negative() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        long expected = JodaTime.getDateTime("2011-12-31").getMillisecond();
        long actual = field.add(instant, -DateTimeConstants.NUM_MONTHS);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        int max = DateTimeConstants.NUM_MONTHS;
        long expected = JodaTime.getDateTime("2011-12-31").getMillisecond();
        long actual = field.addWrapField(instant, max);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = JodaTime.getDateTime("2013-01-31").getMillisecond();
        long subtrahendInstant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        long expected = 12;
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        int month = DateTimeConstants.FEBRUARY;
        long expected = JodaTime.getDateTime("2012-02-01").getMillisecond();
        long actual = field.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_InvalidMonth() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        int month = DateTimeConstants.FEBRUARY + 1;
        field.set(instant, month);
        fail();
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField expected = DateTimeFieldType.years();
        DurationField actual = field.getRangeDurationField();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        long instant = JodaTime.getDateTime("2012-02-29").getMillisecond();
        assertTrue(field.isLeap(instant));
        long instant2 = JodaTime.getDateTime("2013-02-28").getMillisecond();
        assertFalse(field.isLeap(instant2));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = JodaTime.getDateTime("2012-02-29").getMillisecond();
        int expected = 1;
        int actual = field.getLeapAmount(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField expected = DateTimeFieldType.days();
        DurationField actual = field.getLeapDurationField();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        int expected = DateTimeConstants.JANUARY;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = DateTimeConstants.NUM_MONTHS;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        long instant = JodaTime.getDateTime("2012-01-31 12:30:00.123").getMillisecond();
        long expected = JodaTime.getDateTime("2012-01-31 00:00:00").getMillisecond();
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        long instant = JodaTime.getDateTime("2012-01-31 12:30:00.123").getMillisecond();
        long expected = JodaTime.getDateTime("2012-01-31 12:30:00.123").getMillisecond() % TimeUtils.MILLIS_PER_DAY;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializable() {
        long instant = JodaTime.getDateTime("2012-01-31").getMillisecond();
        MutableLocalDateTime mutableDateTime = new MutableLocalDateTime();
        mutableDateTime.setDateTime(instant);
        LocalDateTime localDateTime = mutableDateTime.toLocalDateTime();
        Object object = FieldVerifier.verifyObject(localDateTime, "test", field);
        assertTrue(object instanceof BasicMonthOfYearDateTimeField);
        BasicMonthOfYearDateTimeField restoredField = (BasicMonthOfYearDateTimeField) object;
        assertEquals(field.getMinimumValue(), restoredField.getMinimumValue());
        assertEquals(field.getMaximumValue(), restoredField.getMaximumValue());
        assertEquals(field.isLeap(instant), restoredField.isLeap(instant));
        assertEquals(field.getLeapAmount(instant), restoredField.getLeapAmount(instant));
        assertEquals(field.getLeapDurationField(), restoredField.getLeapDurationField());
        assertEquals(field.getRangeDurationField(), restoredField.getRangeDurationField());
    }
}