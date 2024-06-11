package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriodSerialization() throws Exception {
        BaseDaily baseDaily = new BaseDaily(3);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(baseDaily);
        objectOutputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        BaseDaily baseDaily2 = (BaseDaily) objectInputStream.readObject();
        objectInputStream.close();
        assertEquals("serialized and deserialized instances must be equal", baseDaily, baseDaily2);
    }

    @Test
    public void testBetween() {
        DateTimeZone dateTimeZone = DateTimeZone.forID("America/New_York");
        Instant instant = new DateTime(2023, 1, 1, 0, 0, 0, 0, dateTimeZone).toInstant();
        Instant instant2 = new DateTime(2023, 1, 2, 0, 0, 0, 0, dateTimeZone).toInstant();
        LocalDate localDate = new LocalDate(2023, 1, 2, dateTimeZone);
        LocalDate localDate2 = new LocalDate(2023, 1, 1, dateTimeZone);
        LocalDateTime localDateTime = new LocalDateTime(2023, 1, 1, 12, 0, 0, 300, dateTimeZone);
        LocalDateTime localDateTime2 = new LocalDateTime(2023, 1, 2, 12, 0, 0, 0, dateTimeZone);

        DurationFieldType dayOfYearField = GregorianFields.DAY_OF_YEAR;
        DurationFieldType hourOfDayField = GregorianFields.HOUR_OF_DAY;
        DurationFieldType minuteField = GregorianFields.MINUTE_OF_HOUR;
        DurationFieldType secondField = GregorianFields.SECOND_OF_MINUTE;

        // between Instant and Instant
        assertEquals(1, BaseSingleFieldPeriod.between(instant, instant2, dayOfYearField));

        // between Instant and LocalDate
        assertEquals(1, BaseSingleFieldPeriod.between(instant, localDate, dayOfYearField));
        assertEquals(1, BaseSingleFieldPeriod.between(instant, localDate2, dayOfYearField.getField(DateTimeUtils.getUTC()).isLenient() ? GregorianChronology.INSTANCE : GregorianChronology.INSTANCE));

        // between Instant and LocalDateTime
        assertEquals(1, BaseSingleFieldPeriod.between(instant, localDateTime, hourOfDayField));
        assertEquals(1, BaseSingleFieldPeriod.between(instant, localDateTime2, hourOfDayField.getField(DateTimeUtils.getUTC()).isLenient() ? GregorianChronology.INSTANCE : GregorianChronology.INSTANCE));

        // between LocalDate and LocalDate
        assertEquals(1, BaseSingleFieldPeriod.between(localDate, localDate2, dayOfYearField));

        // between LocalDateTime and LocalDateTime
        assertEquals(303, BaseSingleFieldPeriod.between(localDateTime, localDateTime2, secondField));
        assertEquals(302, BaseSingleFieldPeriod.between(localDateTime, localDateTime2, secondField.getField(DateTimeUtils.getUTC()).isLenient() ? GregorianChronology.INSTANCE : GregorianChronology.INSTANCE));

        // between LocalDateTime and LocalDate (with LocalDate as reference)
        assertEquals(BaseSingleFieldPeriod.ONE, BaseSingleFieldPeriod.between(localDateTime, localDate2, hourOfDayField));
        assertEquals(BaseSingleFieldPeriod.ONE, BaseSingleFieldPeriod.between(localDateTime, localDate2, minuteField));
        assertEquals(BaseSingleFieldPeriod.ONE, BaseSingleFieldPeriod.between(localDateTime, localDate2, secondField));
        assertEquals(BaseSingleFieldPeriod.ONE, BaseSingleFieldPeriod.between(localDateTime, localDate2, dayOfYearField));

        // between ReadablePeriod and long
        LocalDateTime localDateTime3 = new LocalDateTime(2023, 1, 1, 12, 30, 0, 0, dateTimeZone);
        Period period = new Period(3, 30, 0);
        assertEquals(259200033, BaseSingleFieldPeriod.standardPeriodIn(period, 1000L * 60 * 60 * 24));
        assertEquals(BaseSingleFieldPeriod.ONE, BaseSingleFieldPeriod.between(period, 259200033, dayOfYearField));
    }

    @Test
    public void testGetFieldType() {
        BaseDaily baseDaily = new BaseDaily(3);
        assertEquals("BaseDaily has only dayOfYear field", dayOfYearField, baseDaily.getFieldType());

        BaseHour baseHour = new BaseHour(3);
        assertEquals("BaseHour has only hourOfDay field", hourOfDayField, baseHour.getFieldType());

        BaseMinute baseMinute = new BaseMinute(3);
        assertEquals("BaseMinute has only minuteOfHour field", minuteField, baseMinute.getFieldType());

        BaseSecond baseSecond = new BaseSecond(3);
        assertEquals("BaseSecond has only secondOfMinute field", secondField, baseSecond.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseDaily baseDaily = new BaseDaily(3);
        PeriodType periodType = PeriodType.dayTime();
        assertEquals("BaseDaily has PeriodType.dayTime()", periodType, baseDaily.getPeriodType());

        BaseHour baseHour = new BaseHour(3);
        periodType = PeriodType.hourMinute();
        assertEquals("BaseHour has PeriodType.hourMinute()", periodType, baseHour.getPeriodType());

        BaseMinute baseMinute = new BaseMinute(3);
        periodType = PeriodType.minuteSecond();
        assertEquals("BaseMinute has PeriodType.minuteSecond()", periodType, baseMinute.getPeriodType());

        BaseSecond baseSecond = new BaseSecond(3);
        periodType = PeriodType.second();
        assertEquals("BaseSecond has PeriodType.second()", periodType, baseSecond.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseDaily baseDaily = new BaseDaily(3);
        assertEquals("BaseDaily has size 1", 1, baseDaily.size());
    }

    @Test
    public void testGetFieldTypeAt() {
        BaseDaily baseDaily = new BaseDaily(3);
        assertEquals("BaseDaily has only dayOfYear field", dayOfYearField, baseDaily.getFieldType(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtNegative() {
        BaseDaily baseDaily = new BaseDaily(3);
        baseDaily.getFieldType(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtNegative() {
        BaseDaily baseDaily = new BaseDaily(3);
        baseDaily.getValue(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAt() {
        BaseDaily baseDaily = new BaseDaily(3);
        baseDaily.setValue(4);
        baseDaily.getValue(1);
    }

    @Test
    public void testGetValue() {
        BaseDaily baseDaily = new BaseDaily(3);
        assertEquals("BaseDaily has value 3", 3, baseDaily.getValue());
    }

    @Test
    public void testSetValue() {
        BaseDaily baseDaily = new BaseDaily(3);
        baseDaily.setValue(4);
        assertEquals("BaseDaily has value 4", 4, baseDaily.getValue());
    }

    @Test
    public void testToPeriod() {
        BaseDaily baseDaily = new BaseDaily(3);
        Period period = Period.ZERO.withFields(baseDaily);
        assertEquals("BaseDaily toPeriod method returns correct Period instance", period, baseDaily.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseDaily baseDaily = new BaseDaily(3);
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(baseDaily);
        assertEquals("BaseDaily toMutablePeriod method returns correct MutablePeriod instance", mutablePeriod, baseDaily.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseDaily baseDaily1 = new BaseDaily(3);
        BaseDaily baseDaily2 = new BaseDaily(3);
        BaseDaily baseDaily3 = new BaseDaily(4);

        Period period1 = Period.ZERO.withFields(baseDaily1);
        Period period2 = Period.ZERO.withFields(baseDaily2);
        Period period3 = Period.ZERO.withFields(baseDaily3);

        assertEquals("BaseDaily instances with same value are equal", baseDaily1, baseDaily2);
        assertEquals("BaseDaily instances with same value are equal through Period instances", period1, period2);
        assertNotEquals("BaseDaily instances with different values are not equal", baseDaily1, baseDaily3);
        assertNotEquals("BaseDaily instances with different values are not equal through Period instances", period1, period3);
    }

    @Test
    public void testHashCode() {
        BaseDaily baseDaily1 = new BaseDaily(3);
        BaseDaily baseDaily2 = new BaseDaily(3);
        BaseDaily baseDaily3 = new BaseDaily(4);

        int hashCode1 = baseDaily1.hashCode();
        int hashCode2 = baseDaily2.hashCode();
        int hashCode3 = baseDaily3.hashCode();

        assertEquals("BaseDaily instances with same value have same hash code", hashCode1, hashCode2);
        assertNotEquals("BaseDaily instances with different values have different hash codes", hashCode1, hashCode3);
    }

    @Test
    public void testCompareToBaseSingleFieldPeriod() {
        BaseDaily baseDaily1 = new BaseDaily(3);
        BaseDaily baseDaily2 = new BaseDaily(3);
        BaseDaily baseDaily3 = new BaseDaily(4);
        BaseDaily baseDaily4 = new BaseDaily(3);

        assertEquals("BaseDaily with same value is equal to itself", 0, baseDaily1.compareTo(baseDaily1));
        assertEquals("BaseDaily with same value is equal to another instance with same value", 0, baseDaily1.compareTo(baseDaily2));
        assertEquals("BaseDaily with different values are not equal", 1, baseDaily1.compareTo(baseDaily3));
        assertEquals("BaseDaily with same values are equal", 0, baseDaily1.compareTo(baseDaily4));
    }

    @Test
    public void testCompareToObject() {
        BaseDaily baseDaily1 = new BaseDaily(3);
        BaseDaily baseDaily2 = new BaseDaily(3);
        BaseDaily baseDaily3 = new BaseDaily(4);
        String string = "some string";

        assertEquals("BaseDaily is not less than itself", 0, baseDaily1.compareTo(baseDaily1));
        assertEquals("BaseDaily is not less than another BaseDaily with same value", 0, baseDaily1.compareTo(baseDaily2));
        assertEquals("BaseDaily is greater than another BaseDaily with different value", 1, baseDaily1.compareTo(baseDaily3));
        assertEquals("BaseDaily is not less than a String", 1, baseDaily1.compareTo(string));
    }

    @Test
    public void testToString() {
        BaseDaily baseDaily = new BaseDaily(3);
        assertEquals("BaseDaily has correct toString representation", "P1D", baseDaily.toString());
    }
}