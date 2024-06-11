package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseDateTime;
import org.joda.time.BaseLocalDateTime;
import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class BasePeriodTest {

    @Test
    public void testBasePeriodSerialization() throws Exception {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(period);
        objectOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        BasePeriod deserializedPeriod = (BasePeriod) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(period, deserializedPeriod);
    }

    @Test
    public void testCreatePeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] values = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values);
    }

    @Test
    public void testCreatePeriodFromInstant() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        BasePeriod period = new BasePeriod(dateTime, dateTime.plusDays(1), PeriodType.standard());

        PeriodType periodType = period.getPeriodType();
        int[] values = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 1}, values);
    }

    @Test
    public void testCreatePeriodFromReadablePeriod() {
        BaseLocalDateTime baseLocalDateTime = new BaseLocalDateTime(2022, 1, 1, 12, 0, 0, 0, ISOChronology.getInstanceUTC());
        BasePeriod period1 = new BasePeriod(baseLocalDateTime, baseLocalDateTime.plusDays(1), PeriodType.standard());
        BasePeriod period2 = new BasePeriod(period1, PeriodType.standard());

        PeriodType periodType = period2.getPeriodType();
        int[] values = period2.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 1}, values);
    }

    @Test
    public void testCreatePeriodFromReadableInstantAndDuration() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        Duration duration = new Duration(1, 2, 3, 4, 5, 6, 7);
        BasePeriod period = new BasePeriod(dateTime, duration, PeriodType.standard());

        PeriodType periodType = period.getPeriodType();
        int[] values = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 1}, values);
    }

    @Test
    public void testCreatePeriodFromReadableDurationAndReadableInstant() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        Duration duration = new Duration(1, 2, 3, 4, 5, 6, 7);
        BasePeriod period = new BasePeriod(duration, dateTime, PeriodType.standard());

        PeriodType periodType = period.getPeriodType();
        int[] values = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, -1}, values);
    }

    @Test
    public void testCreatePeriodFromObject() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        Duration duration = new Duration(1, 2, 3, 4, 5, 6, 7);
        BasePeriod period = new BasePeriod(duration, dateTime, PeriodType.standard());
        Object object = period;

        BasePeriod period2 = new BasePeriod(object, PeriodType.standard());

        PeriodType periodType = period2.getPeriodType();
        int[] values = period2.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, -1}, values);
    }

    @Test
    public void testCreatePeriodFromIntArrayAndPeriodType() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());

        PeriodType periodType = period.getPeriodType();
        int[] periodValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(values, periodValues);
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();

        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

        for (int i = 0; i < period.size(); i++) {
            assertEquals(i, period.getValue(i));
        }
    }

    @Test
    public void testToDurationFrom() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        BasePeriod period = new BasePeriod(dateTime, dateTime.plusDays(1), PeriodType.standard());
        Duration duration = period.toDurationFrom(dateTime);

        assertEquals(Duration.standardDays(1), duration);
    }

    @Test
    public void testToDurationTo() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        BasePeriod period = new BasePeriod(dateTime, dateTime.plusDays(1), PeriodType.standard());
        Duration duration = period.toDurationTo(dateTime);

        assertEquals(Duration.standardDays(-1), duration);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadablePeriod periodInternal = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(periodInternal);

        PeriodType periodType = period.getPeriodType();
        int[] periodValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, periodValues);
    }

    @Test
    public void testMergePeriods() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
        BasePeriod mergedPeriod = new BasePeriod();
        mergedPeriod.mergePeriod(period1);
        mergedPeriod.mergePeriod(period2);

        PeriodType periodType = mergedPeriod.getPeriodType();
        int[] periodValues = mergedPeriod.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{4, 6, 8, 10, 5, 7, 9, 11}, periodValues);
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setField(DurationFieldType.years(), 3);

        PeriodType periodType = period.getPeriodType();
        int[] periodValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{3, 2, 3, 4, 5, 6, 7, 8}, periodValues);
    }

    @Test
    public void testSetFieldInto() {
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        period.setFieldInto(values, DurationFieldType.weeks(), 3);

        int[] periodValues = period.getValues();

        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 11}, periodValues);
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addField(DurationFieldType.days(), 3);

        PeriodType periodType = period.getPeriodType();
        int[] periodValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{4, 2, 3, 4, 5, 6, 7, 11}, periodValues);
    }

    @Test
    public void testAddFieldInto() {
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        period.addFieldInto(values, DurationFieldType.days(), 3);

        int[] periodValues = period.getValues();

        assertArrayEquals(new int[]{4, 2, 3, 7, 5, 6, 7, 11}, periodValues);
    }
}