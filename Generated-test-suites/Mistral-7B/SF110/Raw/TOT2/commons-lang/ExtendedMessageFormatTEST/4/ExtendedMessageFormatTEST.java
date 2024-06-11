import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat();
        assertNull(fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(0, fmt.getFormats().length);
    }

    @Test
    public void testStringConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, short}");
        assertEquals("{0, date, short}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(0, fmt.getFormats().length);
    }

    @Test
    public void testLocaleConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, short}", Locale.US);
        assertEquals("{0, date, short}", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertEquals(0, fmt.getFormats().length);
    }

    @Test
    public void testRegistryConstructor() {
        FormatFactory numberFormatFactory = new DecimalFormatFactory();
        HashMap<String, FormatFactory> registry = new HashMap<>();
        registry.put("NUMBER", numberFormatFactory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, NUMBER}", registry);
        assertEquals("{0, NUMBER}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(new DecimalFormat("", numberFormatFactory), fmt.getFormats()[0]);
    }

    @Test
    public void testStringConstructorWithRegistry() {
        FormatFactory numberFormatFactory = new DecimalFormatFactory();
        HashMap<String, FormatFactory> registry = new HashMap<>();
        registry.put("NUMBER", numberFormatFactory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, locale, NUMBER}", registry);
        assertEquals("{0, locale, NUMBER}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(new DecimalFormat("", numberFormatFactory), fmt.getFormats()[0]);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, short}");
        String formatted = fmt.format(new Date());
        assertEquals("Jan 3, 2023", formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, invalid, short}");
        fmt.applyPattern("{0, invalid, short}");
        fail("Exception not thrown");
    }
}