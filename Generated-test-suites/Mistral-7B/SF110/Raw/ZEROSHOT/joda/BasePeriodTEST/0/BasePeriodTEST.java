import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testConstructorWithValues() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], period.getValue(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidValues() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        new BasePeriod(values, PeriodType.standard());
    }

    @Test
    public void testConstructorWithInstant() {
        long instant = 1L * 60 * 60 * 1000; // 1 hour
        BasePeriod period = new BasePeriod(instant, PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(1, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructorWithDurations() {
        long durationMillis = 1000L;
        BasePeriod period = new BasePeriod(Duration.standardMillis(durationMillis), PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(1, period.getValue(DurationFieldType.seconds()));
        assertEquals(-1 * durationMillis, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructorWithPeriodTypeAndChronology() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructorWithObjectAndPeriodTypeAndChronology() {
        Object periodObject = new Object();
        BasePeriod period = new BasePeriod(periodObject, PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));

        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        period = new BasePeriod(values, PeriodType.standard());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], period.getValue(DurationFieldType.values()[i]));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.getValue(-1);
    }

    @Test
    public void testToDurationFrom() {
        long instant = 1L * 60 * 60 * 1000; // 1 hour
        BasePeriod period = new BasePeriod(instant, PeriodType.standard(), ISOChronology.getInstanceUTC());
        Duration duration = period.toDurationFrom(DateTimeUtils.currentTime());
        assertEquals(1, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        long instant = 1L * 60 * 60 * 1000; // 1 hour
        BasePeriod period = new BasePeriod(instant, PeriodType.standard(), ISOChronology.getInstanceUTC());
        Duration duration = period.toDurationTo(DateTimeUtils.currentTime().minus(Duration.standardHours(1)));
        assertEquals(-1, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMillis());
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.setField(DurationFieldType.days(), 1);
        assertEquals(1, period.getValue(DurationFieldType.days()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldWithInvalidFieldType() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.setField(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldWithInvalidValue() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.setField(DurationFieldType.years(), 0xFFFF);
    }

    @Test
    public void testSetFieldInto() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        periodSetFieldInto(values, DurationFieldType.days(), 1);
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.days()));
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.addField(DurationFieldType.days(), 1);
        assertEquals(1, period.getValue(DurationFieldType.days()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFieldWithInvalidFieldType() {
        BasePeriod period = new BasePeriod(0, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.addField(null, 1);
    }

    @Test
    public void testAddFieldInto() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        periodAddFieldInto(values, DurationFieldType.days(), 1);
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.days()));
    }

    @Test
    public void testMergePeriod() {
        int[] values1 = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] values2 = {3, 4, 5, 6, 7, 8, 9, 10};
        BasePeriod period1 = new BasePeriod(values1, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(values2, PeriodType.standard());
        period1.mergePeriod(period2);
        int[] expectedValues = {4, 6, 8, 10, 7, 8, 9, 10};
        int[] actualValues = period1.getValues();
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testAddPeriod() {
        int[] values1 = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] values2 = {3, 4, 5, 6, 7, 8, 9, 10};
        BasePeriod period1 = new BasePeriod(values1, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(values2, PeriodType.standard());
        period1.addPeriod(period2);
        int[] expectedValues = {4, 6, 8, 10, 7, 8, 9, 10};
        int[] actualValues = period1.getValues();
        assertArrayEquals(expectedValues, actualValues);
    }

    private void periodSetFieldInto(int[] values, DurationFieldType fieldType, int value) {
        periodSetFieldInto(values, PeriodType.standard(), fieldType, value);
    }

    private void periodSetFieldInto(int[] values, PeriodType periodType, DurationFieldType fieldType, int value) {
        BasePeriod period = new BasePeriod(0, periodType, ISOChronology.getInstanceUTC());
        period.setFieldInto(values, fieldType, value);
    }

    private void periodAddFieldInto(int[] values, DurationFieldType fieldType, int value) {
        periodAddFieldInto(values, PeriodType.standard(), fieldType, value);
    }

    private void periodAddFieldInto(int[] values, PeriodType periodType, DurationFieldType fieldType, int value) {
        BasePeriod period = new BasePeriod(0, periodType, ISOChronology.getInstanceUTC());
        period.addFieldInto(values, fieldType, value);
    }
}