package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.ZoneConstants;
import org.joda.time.ZonedDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.BaseLenientDateTimeField;
import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFields;
import org.joda.time.field.FixedDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology base;
    private DateTimeField field;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        base = new ISOChronology();
        field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH, DateTimeFields.dayOfWeekFirst(), null);
        lenientField = LenientDateTimeField.getInstance(field, base);
    }

    @Test
    public void testGetInstance() {
        assertNotNull(LenientDateTimeField.getInstance(null, base));
        assertNotNull(LenientDateTimeField.getInstance(field, null));
        assertNotNull(LenientDateTimeField.getInstance(field, base));
        assertNotNull(LenientDateTimeField.getInstance(new FixedDateTimeField(DateTimeFieldType.DAY_OF_MONTH, 31), base));
        assertNotNull(LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.HOUR_OF_DAY, 23), base));
        assertNotNull(LenientDateTimeField.getInstance(new BaseLenientDateTimeField(DateTimeFieldType.MILLISECOND, 0, null), base));
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        ZonedDateTime zdt = new ZonedDateTime(2000, 1, 1, 0, 0, 0, 0, base.getLocale());
        long instant = zdt.getMillis();

        int value = get(instant) + 3;
        long result = lenientField.set(instant, value);
        LocalDateTime localDateTime = new LocalDateTime(instant, base);
        LocalDateTime expectedLocalDateTime = new LocalDateTime(localDateTime.getYear(), localDateTime.getMonthOfYear(), localDateTime.getDayOfMonth(), localDateTime.getHourOfDay(), localDateTime.getMinuteOfHour(), localDateTime.getSecondOfMinute(), 0);
        expectedLocalDateTime = expectedLocalDateTime.plusDays(3);
        LocalDateTime actualLocalDateTime = new LocalDateTime(result, base);
        assertEquals("Expected set to return " + expectedLocalDateTime, expectedLocalDateTime, actualLocalDateTime);

        value = get(instant) - 3;
        result = lenientField.set(instant, value);
        expectedLocalDateTime = new LocalDateTime(localDateTime.getYear(), localDateTime.getMonthOfYear(), localDateTime.getDayOfMonth(), localDateTime.getHourOfDay(), localDateTime.getMinuteOfHour(), localDateTime.getSecondOfMinute(), 0);
        expectedLocalDateTime = expectedLocalDateTime.minusDays(3);
        actualLocalDateTime = new LocalDateTime(result, base);
        assertEquals("Expected set to return " + expectedLocalDateTime, expectedLocalDateTime, actualLocalDateTime);

        value = get(instant) + 60 * 60 * 24 * 30 * 12;
        result = lenientField.set(instant, value);
        LocalDateTime actualDateTime = new LocalDateTime(result, base);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        ZonedDateTime expectedDateTime = new ZonedDateTime(formatter.parseLocal("2013-01-01T00:00:00+00:00").toLocalDate().getYear(), formatter.parseLocal("2013-01-01T00:00:00+00:00").getMonthValue(), formatter.parseLocal("2013-01-01T00:00:00+00:00").getDayOfMonth(), 0, 0, 0, 0, base.getZone());
        assertEquals("Expected set to return " + expectedDateTime, expectedDateTime, new ZonedDateTime(result, base));
    }

    private int get(long instant) {
        if (field instanceof LocalDateTimeField) {
            LocalDateTimeField localDateTimeField = (LocalDateTimeField) field;
            return localDateTimeField.get(new LocalDateTime(instant, base));
        } else if (field instanceof LocalTimeField) {
            LocalTimeField localTimeField = (LocalTimeField) field;
            return localTimeField.get(new LocalTime(instant, base));
        } else {
            return FieldUtils.getLong(instant, field);
        }
    }
}