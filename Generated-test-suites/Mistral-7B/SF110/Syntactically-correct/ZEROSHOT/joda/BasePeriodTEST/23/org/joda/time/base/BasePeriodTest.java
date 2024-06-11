import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithArguments() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(5, 2, 3, 4, 6, 7, 8, 9, periodType);
        assertEquals(periodType, period.getPeriodType());
        assertArrayEquals(new int[]{5, 2, 3, 4, 6, 7, 8, 9}, period.iValues);
    }

    @Test
    public void testBasePeriodConstructionWithLong() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(123456789);
        assertEquals(PeriodType.time(), period.getPeriodType());
        assertArrayEquals(ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, 123456789), period.iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstant() {
        PeriodType periodType = PeriodType.standard();
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        BasePeriod period = new BasePeriod(dateTime, periodType);
        assertEquals(periodType, period.getPeriodType());
        LocalDateTime dateTime2 = new LocalDateTime(2022, 1, 1, 13, 0, 0);
        period = new BasePeriod(dateTime2, period, periodType);
        assertArrayEquals(new int[]{0, 1, 0, 1, 0, 1, 0, 0}, period.iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartial() {
        PeriodType periodType = PeriodType.standard();
        LocalDate date = new LocalDate(2022, 1, 1);
        BaseLocalPartial partial = new BaseLocalDate(date, PeriodType.standard());
        BasePeriod period = new BasePeriod(partial, partial2 = new BaseLocalDate(new LocalDate(2022, 1, 2)), periodType);
        assertEquals(periodType, period.getPeriodType());
        LocalDate date2 = new LocalDate(2022, 1, 3);
        period = new BasePeriod(new BaseLocalDate(date, PeriodType.standard()), new BaseLocalDate(date2, PeriodType.standard()), periodType);
        assertArrayEquals(new int[]{0, 1, 0, 0, 1, 0, 0, 1}, period.iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableDuration() {
        PeriodType periodType = PeriodType.standard();
        Duration duration = new Duration(1, Chronology.UTC);
        BasePeriod period = new BasePeriod(duration, periodType);
        assertEquals(periodType, period.getPeriodType());
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        period = new BasePeriod(duration, dateTime, periodType);
        assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 1}, period.iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstantAndReadableDuration() {
        PeriodType periodType = PeriodType.standard();
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        Duration duration = new Duration(1, Chronology.UTC);
        BasePeriod period = new BasePeriod(dateTime, duration, periodType);
        assertEquals(periodType, period.getPeriodType());
        LocalDateTime dateTime2 = new LocalDateTime(2022, 1, 1, 13, 0, 0);
        period = new BasePeriod(dateTime2, duration, periodType);
        assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 1}, period.iValues);
    }

    @Test
    public void testBasePeriodGetPeriodType() {
        BasePeriod period = new BasePeriod();
        period.setPeriodType(PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodGetValue() {
        BasePeriod period = new BasePeriod();
        period.setValue(0, 5);
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testBasePeriodGetValues() {
        BasePeriod period = new BasePeriod();
        int[] values = new int[8];
        period.setValues(values);
        period.setValue(0, 5);
        period.setValue(1, 2);
        period.setValue(2, 3);
        period.setValue(3, 4);
        period.setValue(4, 6);
        period.setValue(5, 7);
        period.setValue(6, 8);
        period.setValue(7, 9);
        assertArrayEquals(new int[]{5, 2, 3, 4, 6, 7, 8, 9}, period.getValues());
    }

    @Test
    public void testBasePeriodToString() {
        BasePeriod period = new BasePeriod();
        period.setValue(0, 5);
        period.setValue(1, 2);
        period.setValue(2, 3);
        period.setValue(3, 4);
        period.setValue(4, 6);
        period.setValue(5, 7);
        period.setValue(6, 8);
        period.setValue(7, 9);
        String result = period.toString();
        assertEquals("PT5S2PT3S4PT6S7PT8S9", result);
    }

    @Test
    public void testBasePeriodToDurationFrom() {
        BasePeriod period = new BasePeriod();
        period.setValue(0, 5);
        period.setValue(1, 2);
        period.setValue(2, 3);
        period.setValue(3, 4);
        period.setValue(4, 6);
        period.setValue(5, 7);
        period.setValue(6, 8);
        period.setValue(7, 9);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        Duration duration = period.toDurationFrom(dateTime);
        assertEquals(new Duration(new LocalDateTime(2022, 1, 1, 12, 5, 2, 0), new LocalDateTime(2022, 1, 1, 12, 7, 5, 0)), duration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        BasePeriod period = new BasePeriod();
        period.setValue(0, 5);
        period.setValue(1, 2);
        period.setValue(2, 3);
        period.setValue(3, 4);
        period.setValue(4, 6);
        period.setValue(5, 7);
        period.setValue(6, 8);
        period.setValue(7, 9);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 13, 0, 0);
        Duration duration = period.toDurationTo(dateTime);
        assertEquals(new Duration(new LocalDateTime(2022, 1, 1, 12, 7, 5, 0), new LocalDateTime(2022, 1, 1, 13, 5, 2, 0)), duration);
    }

    @Test
    public void testBasePeriodCheckAndUpdate() {
        BasePeriod period = new BasePeriod();
        period.checkAndUpdate(DurationFieldType.years(), new int[]{5, 2, 3, 4, 6, 7, 8, 9}, 10);
        assertEquals(10, period.getValue(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodCheckAndUpdateWithNullPeriodType() {
        BasePeriod period = new BasePeriod();
        period.checkAndUpdate(null, new int[]{5, 2, 3, 4, 6, 7, 8, 9}, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodCheckAndUpdateWithWrongFieldType() {
        BasePeriod period = new BasePeriod();
        period.checkAndUpdate(DurationFieldType.seconds(), new int[]{5, 2, 3, 4, 6, 7, 8, 9}, 10);
    }

    @Test
    public void testBasePeriodSetPeriod() {
        BasePeriod period = new BasePeriod();
        ReadablePeriod periodInternal = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(periodInternal);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @Test
    public void testBasePeriodSetPeriodInternal() {
        BasePeriod period = new BasePeriod();
        ReadablePeriod periodInternal = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriodInternal(periodInternal);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @Test
    public void testBasePeriodSetField() {
        BasePeriod period = new BasePeriod();
        period.setField(DurationFieldType.years(), 10);
        assertEquals(10, period.getValue(0));
    }

    @Test
    public void testBasePeriodSetFieldInto() {
        int[] values = new int[8];
        period.setFieldInto(values, DurationFieldType.years(), 10);
        assertEquals(10, values[0]);
    }

    @Test
    public void testBasePeriodAddField() {
        BasePeriod period = new BasePeriod();
        period.addField(DurationFieldType.years(), 10);
        assertEquals(10, period.getValue(0));
    }

    @Test
    public void testBasePeriodAddFieldInto() {
        int[] values = new int[8];
        period.addFieldInto(values, DurationFieldType.years(), 10);
        assertEquals(10, values[0]);
    }

    @Test
    public void testBasePeriodMergePeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
        period1.mergePeriod(period2);
        assertEquals(PeriodType.standard(), period1.getPeriodType());
        assertArrayEquals(new int[]{4, 6, 8, 10, 5, 7, 8, 11}, period1.iValues);
    }

    @Test
    public void testBasePeriodAddPeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
        period1.addPeriod(period2);
        assertEquals(PeriodType.standard(), period1.getPeriodType());
        assertArrayEquals(new int[]{4, 6, 8, 10, 5, 7, 8, 11}, period1.iValues);
    }
}