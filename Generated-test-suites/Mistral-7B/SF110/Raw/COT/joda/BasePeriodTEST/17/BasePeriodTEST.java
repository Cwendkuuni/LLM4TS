import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testConstructorWithIntArray() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        for (int i = 0; i < 8; i++) {
            assertEquals(values[i], period.getValue(i));
        }
    }

    @Test
    public void testConstructorWithPeriodType() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new BasePeriod(null, PeriodType.standard());
    }

    @Test
    public void testCheckPeriodType() {
        PeriodType standard = PeriodType.standard();
        PeriodType test = new PeriodType() {};
        assertEquals(standard, BasePeriod.checkPeriodType(standard));
        assertEquals(test, BasePeriod.checkPeriodType(test));
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        assertEquals(1, period.getValue(0));
        assertEquals(8, period.getValue(7));
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        ReadableInstant start = new DateTime(0);
        Duration duration = period.toDurationFrom(start);
        long expectedDurationMillis = 1 * 3153600000L + 2 * 86400000L + 3 * 3600000L + 4 * 60000L + 5 * 60000L + 6 * 60000L + 7 * 60000L + 8 * 60000L;
        assertEquals(expectedDurationMillis, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        ReadableInstant end = new DateTime(0);
        Duration duration = period.toDurationTo(end);
        long expectedDurationMillis = -(1 * 3153600000L + 2 * 86400000L + 3 * 3600000L + 4 * 60000L + 5 * 60000L + 6 * 60000L + 7 * 60000L + 8 * 60000L);
        assertEquals(expectedDurationMillis, duration.getMillis());
    }

    @Test
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        int[] values = period.getValues();
        DurationFieldType year = DurationFieldType.years();
        period.checkAndUpdate(year, values, 1);
        assertEquals(1, values[BasePeriod.YEARS]);
        period.checkAndUpdate(year, values, 0);
        assertEquals(1, values[BasePeriod.YEARS]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdateWithInvalidField() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        DurationFieldType invalidField = new DurationFieldType() {};
        int[] values = period.getValues();
        period.checkAndUpdate(invalidField, values, 1);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period1 = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, PeriodType.standard());
        period1.setPeriod(period2);
        for (int i = 0; i < 8; i++) {
            assertEquals(period2.getValue(i), period1.getValue(i));
        }
    }

    @Test
    public void testSetPeriodInternal() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        int[] values = period.getValues();
        values[0] = 2;
        period.setPeriodInternal(values);
        assertEquals(2, period.getValue(0));
    }

    @Test
    public void testSetPeriodWithNull() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.setPeriod(null);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, period.getValue(i));
        }
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        DurationFieldType year = DurationFieldType.years();
        period.setField(year, 2);
        assertEquals(2, period.getValue(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldWithNull() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.setField(null, 2);
    }

    @Test
    public void testSetFieldInto() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        int[] values = period.getValues();
        values[0] = 2;
        period.setFieldInto(values, DurationFieldType.years(), 2);
        assertEquals(2, period.getValue(0));
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        DurationFieldType year = DurationFieldType.years();
        period.addField(year, 2);
        assertEquals(3, period.getValue(0));
    }

    @Test
    public void testAddFieldInto() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        int[] values = period.getValues();
        values[0] = 1;
        period.addFieldInto(values, DurationFieldType.years(), 2);
        assertEquals(3, period.getValue(0));
    }

    @Test
    public void testMergePeriodInto() {
        BasePeriod period1 = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, PeriodType.standard());
        int[] values = period1.getValues();
        period1.mergePeriodInto(values, period2);
        for (int i = 0; i < 8; i++) {
            assertEquals(period2.getValue(i), period1.getValue(i));
        }
    }

    @Test
    public void testAddPeriodInto() {
        BasePeriod period1 = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, PeriodType.standard());
        int[] values = period1.getValues();
        period1.addPeriodInto(values, period2);
        for (int i = 0; i < 8; i++) {
            assertEquals(period1.getValue(i) + period2.getValue(i), period1.getValue(i));
        }
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period1 = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, PeriodType.standard());
        period1.addPeriod(period2);
        for (int i = 0; i < 8; i++) {
            assertEquals(period1.getValue(i) + period2.getValue(i), period1.getValue(i));
        }
    }

    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        period.setValues(values);
        for (int i = 0; i < 8; i++) {
            assertEquals(values[i], period.getValue(i));
        }
    }

    @Test
    public void testSetValuesWithNull() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        period.setValues(null);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, period.getValue(i));
        }
    }
}