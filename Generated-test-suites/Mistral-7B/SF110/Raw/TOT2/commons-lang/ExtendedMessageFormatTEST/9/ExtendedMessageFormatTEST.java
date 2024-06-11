import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertNull(fmt.toPattern());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", fmt.toPattern());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        final Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,dd-MM-yyyy}, {1}", registry);
        assertEquals("{0,date,dd-MM-yyyy}, {1}", fmt.toPattern());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testApplyPatternWithValidPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        String pattern = "Hello {0}, your age is {1}.";
        fmt.applyPattern(pattern);
        assertEquals("Hello {0}, your age is {1}", fmt.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        String pattern = "Hello {0}, your age is {1, 2}.";
        fmt.applyPattern(pattern);
    }

    @Test
    public void testGetFormat() {
        final FormatFactory dateFormatFactory = new SimpleDateFormat("dd-MM-yyyy");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", dateFormatFactory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, dd-MM-yyyy}", registry);
        Format format = fmt.getFormat("date");
        assertEquals(dateFormatFactory.getFormat("dd-MM-yyyy"), format);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFormatWithNullRegistry() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, dd-MM-yyyy}");
        Format format = fmt.getFormat(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithUnknownFormat() {
        final FormatFactory dateFormatFactory = new SimpleDateFormat("dd-MM-yyyy");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", dateFormatFactory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, dd-MM-yyyy}", registry);
        Format format = fmt.getFormat("unknown");
    }

    @Test
    public void testEqualsAndHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt3 = new ExtendedMessageFormat("{0}, {2}");
        assertTrue(fmt1.equals(fmt1));
        assertTrue(fmt1.equals(fmt2));
        assertFalse(fmt1.equals(fmt3));
        assertEquals(fmt1.hashCode(), fmt2.hashCode());
    }
}