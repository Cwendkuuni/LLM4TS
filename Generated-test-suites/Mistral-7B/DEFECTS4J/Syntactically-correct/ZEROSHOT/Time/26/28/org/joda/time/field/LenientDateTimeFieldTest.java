package org.joda.time.field.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@RunWith(Parameterized.class)
public class LenientDateTimeFieldTest {

    private static final Chronology UTC = Chronology.UTC;
    private static final DateTimeField DAY_OF_MONTH = DateTimeFields.dayOfMonth();
    private static final DateTimeField DAY_OF_YEAR = DateTimeFields.dayOfYear();
    private static final DateTimeField MONTH_OF_YEAR = DateTimeFields.monthOfYear();
    private static final DateTimeField YEAR = DateTimeFields.year();
    private static final DateTimeField LENIENT_DAY_OF_MONTH = LenientDateTimeField.getInstance(DAY_OF_MONTH, UTC);
    private static final DateTimeField LENIENT_DAY_OF_YEAR = LenientDateTimeField.getInstance(DAY_OF_YEAR, UTC);
    private static final DateTimeField LENIENT_MONTH_OF_YEAR = LenientDateTimeField.getInstance(MONTH_OF_YEAR, UTC);
    private static final DateTimeField LENIENT_YEAR = LenientDateTimeField.getInstance(YEAR, UTC);
    private final long testValue;
    private final int fieldValue;
    private final int expectedValue;

    public LenientDateTimeFieldTest(int fieldValue, long testValue, int expectedValue) {
        this.fieldValue = fieldValue;
        this.testValue = testValue;
        this.expectedValue = expectedValue;
    }

    @Parameterized.Parameters(name = "{index}: lenientDateTimeField.set({0}, {1}) = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {31, -1, 30},
                {31, 32, 32},
                {12, 31, 32},
                {12, 13, 13},
                {0, 366, 1},
                {0, 365, 0},
                {12, 366, 1},
                {12, 365, 11},
                {9999, 4000, 4000},
                {9999, 3999, 3999},
                {-9999, 3999, 4000},
                {-9999, 3998, 3998}
        });
    }

    @Test
    public void testSetLenientDateTimeField() {
        BaseDateTime dateTime = new BaseDateTime(testValue, UTC);
        long result = LENIENT_DAY_OF_MONTH.set(dateTime.getMillis(), fieldValue);
        assertEquals("Unexpected result for setLenientDateTimeField(dayOfMonth, {0}, {1})",
                expectedValue, FieldUtils.getDaysOffset(UTC.getUTCMillis(result), UTC.getUTCMillis(dateTime.getMillis())));

        result = LENIENT_DAY_OF_YEAR.set(dateTime.getMillis(), fieldValue);
        assertEquals("Unexpected result for setLenientDateTimeField(dayOfYear, {0}, {1})",
                expectedValue, FieldUtils.getDaysOffset(UTC.getUTCMillis(result), UTC.getUTCMillis(dateTime.getMillis())));

        result = LENIENT_MONTH_OF_YEAR.set(dateTime.getMillis(), fieldValue);
        assertEquals("Unexpected result for setLenientDateTimeField(monthOfYear, {0}, {1})",
                expectedValue, FieldUtils.getMonthsOffset(UTC.getUTCMillis(result), UTC.getUTCMillis(dateTime.getMillis())));

        result = LENIENT_YEAR.set(dateTime.getMillis(), fieldValue);
        assertEquals("Unexpected result for setLenientDateTimeField(year, {0}, {1})",
                expectedValue, FieldUtils.getYearsOffset(UTC.getUTCMillis(result), UTC.getUTCMillis(dateTime.getMillis())));
    }

    @Test
    public void testSetLenientDateTimeFieldWithDifferentChronology() {
        BaseDateTime dateTime = new BaseDateTime(testValue, UTC);
        DateTimeField dayOfMonth = DateTimeFields.dayOfMonth();
        DateTimeField lenientDayOfMonth = LenientDateTimeField.getInstance(dayOfMonth, BaseDateTime.PROVIDED_CHRONO_INSTANCE);
        long result = lenientDayOfMonth.set(dateTime.getMillis(), fieldValue);
        BaseDateTime resultDateTime = new BaseDateTime(UTC.getUTCMillis(result), UTC);
        assertNotSame("Unexpected result for setLenientDateTimeField(dayOfMonth, {0}, {1}) with different chronologies",
                dateTime, resultDateTime);

        result = LENIENT_DAY_OF_YEAR.set(dateTime.getMillis(), fieldValue);
        resultDateTime = new BaseDateTime(UTC.getUTCMillis(result), UTC);
        assertNotSame("Unexpected result for setLenientDateTimeField(dayOfYear, {0}, {1}) with different chronologies",
                dateTime, resultDateTime);

        result = LENIENT_MONTH_OF_YEAR.set(dateTime.getMillis(), fieldValue);
        resultDateTime = new BaseDateTime(UTC.getUTCMillis(result), UTC);
        assertNotSame("Unexpected result for setLenientDateTimeField(monthOfYear, {0}, {1}) with different chronologies",
                dateTime, resultDateTime);

        result = LENIENT_YEAR.set(dateTime.getMillis(), fieldValue);
        resultDateTime = new BaseDateTime(UTC.getUTCMillis(result), UTC);
        assertNotSame("Unexpected result for setLenientDateTimeField(year, {0}, {1}) with different chronologies",
                dateTime, resultDateTime);
    }

    @Test
    public void testLenientDateTimeFieldIsLenient() {
        assertEquals("Unexpected result for isLenient()", true, LENIENT_DAY_OF_MONTH.isLenient());
        assertEquals("Unexpected result for isLenient()", true, LENIENT_DAY_OF_YEAR.isLenient());
        assertEquals("Unexpected result for isLenient()", true, LENIENT_MONTH_OF_YEAR.isLenient());
        assertEquals("Unexpected result for isLenient()", true, LENIENT_YEAR.isLenient());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLenientDateTimeFieldThrowsExceptionOnGet() {
        LENIENT_DAY_OF_MONTH.get(LocalDateTime.parse("2019-01-01T00:00:00.000Z").toLong());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLenientDateTimeFieldThrowsExceptionOnGetWithDifferentChronology() {
        LocalDate date = new LocalDate(2019, 1, 1).withZone(BaseDateTime.PROVIDED_CHRONO_INSTANCE);
        LENIENT_DAY_OF_MONTH.get(date.getMillis());
    }
}