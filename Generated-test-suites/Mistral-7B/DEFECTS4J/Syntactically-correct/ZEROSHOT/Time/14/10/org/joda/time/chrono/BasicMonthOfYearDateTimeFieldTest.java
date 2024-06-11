import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearDateTimeField;
import org.joda.time.field.Months;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology CHRONOLOGY = new BasicChronology("TestChronology", DateTimeConstants.PROLEPTIC_GREGORIAN);
    private static final MonthOfYearDateTimeField FIELD = new MonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    @org.junit.BeforeClass
    public static void setup() {
        CHRONOLOGY.setLeapYearRule(new BasicGregorianLeap(null));
    }

    @org.junit.Test
    public void testIsLenient() {
        assertFalse(FIELD.isLenient());
    }

    @org.junit.Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, FIELD.getMinimumValue());
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, FIELD.getMaximumValue());
    }

    @org.junit.Test
    public void testGet() {
        long instant = BaseDateTime.parse("2012-12-31T23:59:59.999Z").getMillis();
        assertEquals(DateTimeConstants.DECEMBER, FIELD.get(instant));

        instant = BaseDateTime.parse("2012-01-31T23:59:59.999Z").getMillis();
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));

        instant = BaseDateTime.parse("2012-02-29T23:59:59.999Z").getMillis();
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(instant));

        instant = BaseDateTime.parse("2012-02-28T23:59:59.999Z").getMillis();
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(instant));
    }

    @org.junit.Test
    public void testAdd() {
        long instant = BaseDateTime.parse("2012-01-31T23:59:59.999Z").getMillis();
        long result = FIELD.add(instant, 1);
        assertEquals(BaseDateTime.parse("2012-02-28T23:59:59.999Z").getMillis(), result);

        result = FIELD.add(instant, -1);
        assertEquals(BaseDateTime.parse("2012-01-31T23:59:59.999Z").getMillis(), result);

        result = FIELD.add(instant, 12);
        assertEquals(BaseDateTime.parse("2012-02-29T23:59:59.999Z").getMillis(), result);

        result = FIELD.add(instant, Months.getNumberOfMonths(13));
        assertEquals(BaseDateTime.parse("2013-03-01T00:00:00.000Z").getMillis(), result);
    }

    @org.junit.Test
    public void testAddWrapField() {
        long instant = BaseDateTime.parse("2013-01-31T23:59:59.999Z").getMillis();
        long result = FIELD.addWrapField(instant, 13);
        assertEquals(BaseDateTime.parse("2014-02-28T23:59:59.999Z").getMillis(), result);

        result = FIELD.addWrapField(instant, 16);
        assertEquals(BaseDateTime.parse("2014-03-31T23:59:59.999Z").getMillis(), result);
    }

    @org.junit.Test
    public void testSet() {
        long instant = BaseDateTime.parse("2012-01-31T23:59:59.999Z").getMillis();
        long result = FIELD.set(instant, DateTimeConstants.FEBRUARY);
        assertEquals(BaseDateTime.parse("2012-02-28T23:59:59.999Z").getMillis(), result);

        result = FIELD.set(instant, DateTimeConstants.MAY);
        assertEquals(BaseDateTime.parse("2012-05-31T23:59:59.999Z").getMillis(), result);

        result = FIELD.set(instant, DateTimeConstants.DECEMBER + 5);
        assertEquals(BaseDateTime.parse("2013-01-31T23:59:59.999Z").getMillis(), result);
    }

    @org.junit.Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = BaseDateTime.parse("2012-12-31T23:59:59.999Z").getMillis();
        long subtrahendInstant = BaseDateTime.parse("2012-11-30T23:59:59.999Z").getMillis();
        long difference = FIELD.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(2, difference);

        minuendInstant = BaseDateTime.parse("2012-02-29T23:59:59.999Z").getMillis();
        subtrahendInstant = BaseDateTime.parse("2012-03-31T23:59:59.999Z").getMillis();
        difference = FIELD.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);
    }

    @org.junit.Test
    public void testIsLeap() {
        long instant = BaseDateTime.parse("2012-02-29T23:59:59.999Z").getMillis();
        assertTrue(FIELD.isLeap(instant));

        instant = BaseDateTime.parse("2012-02-28T23:59:59.999Z").getMillis();
        assertFalse(FIELD.isLeap(instant));
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        long instant = BaseDateTime.parse("2012-02-29T23:59:59.999Z").getMillis();
        assertEquals(1, FIELD.getLeapAmount(instant));

        instant = BaseDateTime.parse("2012-02-28T23:59:59.999Z").getMillis();
        assertEquals(0, FIELD.getLeapAmount(instant));
    }

    @org.junit.Test
    public void testGetLeapDurationField() {
        DurationField result = FIELD.getLeapDurationField();
        assertEquals(FieldUtils.days(), result);
    }

    @org.junit.Test
    public void testRoundFloor() {
        long instant = BaseDateTime.parse("2012-12-31T23:59:59.999Z").getMillis();
        assertEquals(BaseDateTime.parse("2012-12-31T00:00:00.000Z").getMillis(), FIELD.roundFloor(instant));

        instant = BaseDateTime.parse("2012-01-31T23:59:59.999Z").getMillis();
        assertEquals(BaseDateTime.parse("2012-01-31T00:00:00.000Z").getMillis(), FIELD.roundFloor(instant));

        instant = BaseDateTime.parse("2012-12-31T23:59:59.999Z").getMillis() - 100;
        assertEquals(BaseDateTime.parse("2012-12-30T23:59:59.999Z").getMillis(), FIELD.roundFloor(instant));
    }

    @org.junit.Test
    public void testRemainder() {
        long instant = BaseDateTime.parse("2012-12-31T23:59:59.999Z").getMillis();
        assertEquals(0, FIELD.remainder(instant));

        instant = BaseDateTime.parse("2012-01-31T23:59:59.999Z").getMillis();
        assertEquals(0, FIELD.remainder(instant));

        instant = BaseDateTime.parse("2012-12-31T23:59:59.998Z").getMillis();
        assertEquals(-1, FIELD.remainder(instant));
    }

    @org.junit.Test
    public void testAddWrapFieldWithLargeValues() {
        long minuendInstant = BaseDateTime.parse("9999-12-31T23:59:59.999Z").getMillis();
        long subtrahendInstant = BaseDateTime.parse("9999-11-30T23:59:59.999Z").getMillis();
        long difference = FIELD.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        long remainder = FIELD.remainder(minuendInstant);
        long maxValue = FIELD.getMaximumValue();
        long result = FIELD.addWrapField(minuendInstant, difference + remainder + maxValue);
        assertEquals(BaseDateTime.parse("10000-01-31T23:59:59.999Z").getMillis(), result);

        result = FIELD.addWrapField(minuendInstant, difference + maxValue);
        assertEquals(BaseDateTime.parse("10000-01-31T23:59:59.999Z").getMillis(), result);
    }
}