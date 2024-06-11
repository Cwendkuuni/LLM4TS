import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.MutableDateTime;
import org.joda.time.Zoneoffset;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest {

    /**
     * Test method for {@link LenientDateTimeField#LenientDateTimeField(DateTimeField, Chronology)} .
     */
    @Test
    public void testLenientDateTimeField() {
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        Chronology chronology = ISOChronology.INSTANCE;
        DateTimeField field = new StrictDateTimeField(fieldType, 0, chronology);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        assertNotNull(lenientField);
        assertSame(field, lenientField.getWrappedField());
        assertSame(chronology, lenientField.getBase());
        assertFalse(lenientField.isLenient());
        assertTrue(lenientField instanceof LenientDateTimeField);
    }

    /**
     * Test method for {@link LenientDateTimeField#getInstance(DateTimeField, Chronology)} .
     */
    @Test
    public void testGetInstance() {
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        Chronology chronology = ISOChronology.INSTANCE;
        DateTimeField strictField = new StrictDateTimeField(fieldType, 0, chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, chronology);
        assertNotNull(lenientField);
        assertSame(strictField, lenientField.getWrappedField());
        assertSame(chronology, lenientField.getBase());
        assertTrue(lenientField.isLenient());
        assertTrue(lenientField instanceof LenientDateTimeField);
    }

    /**
     * Test method for {@link LenientDateTimeField#isLenient()} .
     */
    @Test
    public void testIsLenient() {
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        Chronology chronology = ISOChronology.INSTANCE;
        LenientDateTimeField lenientField = new LenientDateTimeField(new StrictDateTimeField(fieldType, 0, chronology), chronology);
        assertTrue(lenientField.isLenient());
    }

    /**
     * Test method for {@link LenientDateTimeField#set(long, int)} .
     */
    @Test
    public void testSet() {
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        Chronology chronology = ISOChronology.INSTANCE;
        DateTimeField strictField = new StrictDateTimeField(fieldType, 0, chronology);
        LenientDateTimeField lenientField = new LenientDateTimeField(strictField, chronology);

        // Test within bounds
        MutableDateTime dateTime = new MutableDateTime(0, chronology);
        long initialInstant = dateTime.getMillisecond();
        int value = 12;
        long expectedInstant = initialInstant + DateTimeConstants.MILLIS_PER_HOUR * value;
        long result = lenientField.set(initialInstant, value);
        assertEquals(expectedInstant, result);

        // Test out of bounds (add one hour)
        value++;
        expectedInstant += DateTimeConstants.MILLIS_PER_HOUR;
        result = lenientField.set(initialInstant, value);
        assertEquals(expectedInstant, result);

        // Test out of bounds (subtract one hour)
        value--;
        expectedInstant -= DateTimeConstants.MILLIS_PER_HOUR;
        result = lenientField.set(initialInstant, value);
        assertEquals(expectedInstant, result);

        // Test out of bounds (add one day)
        LocalDateTime localDateTime = new LocalDateTime(dateTime);
        LocalDateTimeFieldVerifier.verify(lenientField, localDateTime);
        value += 24;
        result = lenientField.set(initialInstant, value);
        LocalDateTime expectedLocalDateTime = localDateTime.plusHours(value);
        LocalDateTimeFieldVerifier.verify(lenientField, expectedLocalDateTime.toDateTime(Zoneoffset.UTC));

        // Test out of bounds (subtract one day)
        value--;
        LocalDateTime expectedLocalDateTime2 = localDateTime.minusDays(1);
        result = lenientField.set(initialInstant, value);
        LocalDateTimeFieldVerifier.verify(lenientField, expectedLocalDateTime2.toDateTime(Zoneoffset.UTC));
    }
}