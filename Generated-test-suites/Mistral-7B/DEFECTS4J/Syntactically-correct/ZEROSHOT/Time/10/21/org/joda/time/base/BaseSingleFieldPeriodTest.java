import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        long startMillis = DateTimeUtils.currentMillis();
        long endMillis = startMillis + 3600000L; // 1 hour
        DurationFieldType field = DurationFieldType.HOURS;
        int period = BaseSingleFieldPeriod.between(DateTimeUtils.currentInstants().withMillis(startMillis),
                DateTimeUtils.currentInstants().withMillis(endMillis), field);
        assertEquals(1, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidStart() {
        long startMillis = Long.MIN_VALUE;
        long endMillis = DateTimeUtils.currentMillis();
        DurationFieldType field = DurationFieldType.HOURS;
        BaseSingleFieldPeriod.between(DateTimeUtils.currentInstants().withMillis(startMillis),
                DateTimeUtils.currentInstants().withMillis(DateTimeUtils.currentMillis()), field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidEnd() {
        long startMillis = DateTimeUtils.currentMillis();
        long endMillis = Long.MAX_VALUE;
        DurationFieldType field = DurationFieldType.HOURS;
        BaseSingleFieldPeriod.between(DateTimeUtils.currentInstants().withMillis(startMillis),
                DateTimeUtils.currentInstants().withMillis(endMillis), field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidField() {
        long startMillis = DateTimeUtils.currentMillis();
        long endMillis = startMillis + 3600000L; // 1 hour
        DurationFieldType field = DurationFieldType.MONTHS;
        BaseSingleFieldPeriod.between(DateTimeUtils.currentInstants().withMillis(startMillis),
                DateTimeUtils.currentInstants().withMillis(endMillis), field);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.parse("PT1H");
        long millisPerUnit = 1000 * 60 * 60; // 1 hour
        int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(1, standardPeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = null;
        long millisPerUnit = 1000 * 60 * 60; // 1 hour
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.HOURS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getValue(2);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        int value = period.get(DurationFieldType.HOURS);
        assertEquals(1, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertTrue(period.isSupported(DurationFieldType.HOURS));
        assertFalse(period.isSupported(DurationFieldType.MINUTES));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        Period result = period.toPeriod();
        assertEquals(1, result.getHours());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        MutablePeriod result = period.toMutablePeriod();
        result.add(DurationFieldType.HOURS, 1);
        assertEquals(2, result.getHours());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(0, period.compareTo(period));
    }

    @Test
    public void testCompareToDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToSameTypeDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        Object period2 = new Object();
        period1.compareTo(period2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareToNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.compareTo(null);
    }

}