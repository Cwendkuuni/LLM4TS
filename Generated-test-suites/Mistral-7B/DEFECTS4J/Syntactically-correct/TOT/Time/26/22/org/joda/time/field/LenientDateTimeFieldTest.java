import org.joda.time.BasicDate;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        Chronology isoChrono = org.joda.time.chrono.ISOChronology.INSTANCE;
        DateTimeField dayOfWeekField = DateTimeFieldType.dayOfWeek();
        DateTimeField lenientDayOfWeekField = LenientDateTimeField.getInstance(dayOfWeekField, isoChrono);
        DateTimeField strictDayOfWeekField = dayOfWeekField;
        assertFalse(lenientDayOfWeekField.isLenient());
        assertNotNull(lenientDayOfWeekField);
        assertTrue(strictDayOfWeekField.isLenient());
        assertSame(lenientDayOfWeekField.getClass(), LenientDateTimeField.class);
        assertSame(strictDayOfWeekField.getClass(), StrictDateTimeField.class);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictDayOfWeekField, isoChrono);
        assertSame(lenientDayOfWeekField, lenientField);
        assertNull(LenientDateTimeField.getInstance(null, isoChrono));
    }

    @Test
    public void testLenientSet() {
        Chronology isoChrono = org.joda.time.chrono.ISOChronology.INSTANCE;
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, isoChrono);

        MutableDateTime dt = new MutableDateTime(1475728000000L, isoChrono);
        long setValue = 20;

        long result = lenientField.set(dt.getLong(), setValue);
        LocalDateTime expected = new LocalDateTime(isoChrono, 2016, 11, 1, 0, 0, 0).plusDays(setValue);
        assertEquals(expected.getLong(), result);
        assertEquals(setValue, lenientField.get(result));

        MutableLocalDateTime mldt = new MutableLocalDateTime(1475728000000L, new LocalTime(), isoChrono);
        result = lenientField.set(mldt.toDateTime().getLong(), setValue);
        expected = new LocalDateTime(isoChrono, 2016, 11, 1, 0, 0, 0).plusDays(setValue).toLocalDateTime();
        assertEquals(expected, mldt.toLocalDateTime());
        assertEquals(setValue, lenientField.get(result));

        MutableDateTime invalidDate = new MutableDateTime(Long.MIN_VALUE, isoChrono);
        assertNull(lenientField.set(invalidDate.getLong(), setValue));
    }

    @Test
    public void testGetType() {
        Chronology isoChrono = org.joda.time.chrono.ISOChronology.INSTANCE;
        DateTimeField field = DateTimeFieldType.dayOfWeek();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, isoChrono);
        assertSame(field.getType(), lenientField.getType());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testIsLenient() {
        Chronology isoChrono = org.joda.time.chrono.ISOChronology.INSTANCE;
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, isoChrono);
        assertTrue(lenientField.isLenient());
        DateTimeField strictField = new StrictDateTimeField(field, isoChrono);
        assertFalse(strictField.isLenient());
    }
}