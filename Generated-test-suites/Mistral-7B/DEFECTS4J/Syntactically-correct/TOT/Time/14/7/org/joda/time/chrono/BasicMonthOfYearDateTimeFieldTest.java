package org.joda.time.chrono;

import org.joda.time.BasicDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Tests for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    /**
     * Default constructor.
     */
    public BasicMonthOfYearDateTimeFieldTest() {
        super();
    }

    /**
     * Constructor with a specified precision.
     * @param precision the precision to use.
     */
    public BasicMonthOfYearDateTimeFieldTest(int precision) {
        super(precision);
    }

    /**
     * Sets up test environment.
     * @throws Exception if an error occurs.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tears down test environment.
     * @throws Exception if an error occurs.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the constructor.
     */
    public void testConstructor() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(chronology, field.getChronology());
        assertEquals(false, field.isLenient());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
        assertEquals(31, field.getLeapAmount(0));
        assertEquals(30, field.getLeapAmount(4));
        assertEquals(31, field.getLeapAmount(11));
        assertEquals(DateTimeFieldType.monthOfYear(), field.getRangeDurationField());
        assertEquals(DurationFieldType.days(), field.getLeapDurationField());
    }

    /**
     * Tests the get method.
     */
    public void testGet() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        int month = field.get(instant);
        assertEquals(1, month);
        long instant2 = LocalDateTime.parse("2005-02-29T00:00:00.000Z").toInstant().getMillis();
        int month2 = field.get(instant2);
        assertEquals(2, month2);
        long instant3 = LocalDateTime.parse("2005-02-28T00:00:00.000Z").toInstant().getMillis();
        int month3 = field.get(instant3);
        assertEquals(2, month3);
    }

    /**
     * Tests the add method.
     */
    public void testAdd() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        long result = field.add(instant, 1);
        long expected = LocalDateTime.parse("2005-02-28T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        result = field.add(instant, -1);
        expected = LocalDateTime.parse("2005-01-30T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        result = field.add(instant, 13);
        expected = LocalDateTime.parse("2005-03-31T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        result = field.add(instant, -13);
        expected = LocalDateTime.parse("2005-01-28T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
    }

    /**
     * Tests the addWrapField method.
     */
    public void testAddWrapField() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2005-13-31T00:00:00.000Z").toInstant().getMillis();
        long result = field.addWrapField(instant, 2);
        long expected = LocalDateTime.parse("2005-02-28T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        result = field.addWrapField(instant, -2);
        expected = LocalDateTime.parse("2005-12-31T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
    }

    /**
     * Tests the set method.
     */
    public void testSet() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        long result = field.set(instant, 13);
        long expected = LocalDateTime.parse("2005-02-28T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        result = field.set(instant, 0);
        expected = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        result = field.set(instant, 13
            + chronology.getDaysInYearMonth(getYear(instant), 13));
        expected = LocalDateTime.parse("2005-03-31T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        try {
            result = field.set(instant, 14);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the getDifferenceAsLong method.
     */
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        long instant1 = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        long instant2 = LocalDateTime.parse("2005-02-28T00:00:00.000Z").toInstant().getMillis();
        long result = field.getDifferenceAsLong(instant1, instant2);
        long expected = -31;
        assertEquals(expected, result);
        result = field.getDifferenceAsLong(instant2, instant1);
        expected = 31;
        assertEquals(expected, result);
    }

    /**
     * Tests the roundFloor method.
     */
    public void testRoundFloor() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        long result = field.roundFloor(instant);
        long expected = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        instant = LocalDateTime.parse("2005-01-30T23:59:59.999Z").toInstant().getMillis();
        result = field.roundFloor(instant);
        expected = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
        instant = LocalDateTime.parse("2005-02-01T00:00:00.001Z").toInstant().getMillis();
        result = field.roundFloor(instant);
        expected = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        assertEquals(expected, result);
    }

    /**
     * Tests the remainder method.
     */
    public void testRemainder() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2005-01-31T00:00:00.000Z").toInstant().getMillis();
        long result = field.remainder(instant);
        long expected = 0;
        assertEquals(expected, result);
        instant = LocalDateTime.parse("2005-01-31T12:34:56.789Z").toInstant().getMillis();
        result = field.remainder(instant);
        expected = 0;
        assertEquals(expected, result);
        instant = LocalDateTime.parse("2005-02-28T23:59:59.999Z").toInstant().getMillis();
        result = field.remainder(instant);
        expected = 0;
        assertEquals(expected, result);
        instant = LocalDateTime.parse("2005-03-01T00:00:00.001Z").toInstant().getMillis();
        result = field.remainder(instant);
        expected = 1;
        assertEquals(expected, result);
    }

    /**
     * Tests the serialization.
     */
    public void testSerialization() {
        BasicChronology chronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
            chronology, DateTimeConstants.JANUARY);
        byte[] bytes = getBytes(field);
        BasicMonthOfYearDateTimeField field2 = (BasicMonthOfYearDateTimeField) getObject(bytes);
        assertEquals(field, field2);
    }
}