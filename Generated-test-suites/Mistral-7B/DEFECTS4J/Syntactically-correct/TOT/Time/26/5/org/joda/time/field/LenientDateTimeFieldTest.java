import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class LenientDateTimeFieldTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private final DateTimeField field;
    private final Chronology base;

    public LenientDateTimeFieldTest(DateTimeField field, Chronology base) {
        this.field = field;
        this.base = base;
    }

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = Arrays.asList(new Object[][]{
                new DateTimeField[]{DateTimeFieldType.dayOfYear(), BaseDateTime.UTC},
                new DateTimeField[]{DateTimeFieldType.hourOfDay(), LocalDate.now().withZone(base).toCompactDateTime().plus(3600000).withZone(BaseDateTime.UTC).getField(DateTimeFieldType.hourOfDay()), base},
                new DateTimeField[]{DateTimeFieldType.minuteOfHour(), new LocalDateTime(LocalDate.now().withZone(base), new LocalTime(0, 30, 0)).toDateTimeAtStartOfDay().getField(DateTimeFieldType.minuteOfHour()), base}
        });
        return Arrays.asList(data.toArray());
    }

    @Test
    public void testInstance() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertTrue(lenientField instanceof LenientDateTimeField);
        assertEquals(field, lenientField.getDelegate());
        assertEquals(base, lenientField.getBase());
    }

    @Test
    public void testIsLenient() {
        assertTrue(field.isLenient());
        assertTrue(LenientDateTimeField.getInstance(field, base).isLenient());
    }

    @Test
    public void testSet_validValue() {
        MutableDateTime dateTime = new MutableDateTime(FieldUtils.safeAdd(instant, get(instant)), base);
        long newValue = FieldUtils.safeAdd(value, get(instant));
        long expected = lenientSet(instant, newValue);

        long actual = LenientDateTimeField.getInstance(field, base).set(instant, value);
        assertEquals(expected, actual);
        assertEquals(newValue, get(expected));
        assertEquals(instant, iBase.getZone().convertLocalToUTC(expected, false));
    }

    @Test
    public void testSet_outOfBoundsValue() {
        MutableDateTime dateTime = new MutableDateTime(instant, base);
        long newValue = get(instant) + 1;
        long expected = FieldUtils.safeAdd(instant, getDifference(newValue, get(instant)));

        try {
            LenientDateTimeField.getInstance(field, base).set(instant, newValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long actual = LenientDateTimeField.getInstance(field, base).set(instant, newValue);
        assertEquals(expected, actual);
        assertEquals(newValue, get(expected));
        assertEquals(instant, iBase.getZone().convertLocalToUTC(expected, false));
    }

    private long lenientSet(long instant, int value) {
        long localInstant = iBase.getZone().convertUTCToLocal(instant);
        long difference = FieldUtils.safeSubtract(value, get(instant));
        localInstant = getType().getField(iBase.withUTC()).add(localInstant, difference);
        return iBase.getZone().convertLocalToUTC(localInstant, false);
    }

    private long getDifference(int value) {
        return FieldUtils.safeSubtract(value, get(instant));
    }

    @Test
    public void testSet_offsetValue() {
        MutableDateTime dateTime = new MutableDateTime(instant, base);
        long offset = 12 * 60 * 60 * 1000; // 12 hours
        long expected = FieldUtils.safeAdd(instant, getDifference(value + offset));

        long actual = LenientDateTimeField.getInstance(field, base).set(instant, value + offset);
        assertEquals(expected, actual);
        assertEquals(value + offset, get(expected));
        assertEquals(instant, iBase.getZone().convertLocalToUTC(expected, false));
    }

    private static DateTimeFieldType getType() {
        return field.getFieldType();
    }
}