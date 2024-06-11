import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(null, format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals("", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertEquals(null, format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        String pattern = "Hello {0}, your age is {1}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        String pattern = "Hello {0}, your age is {1}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals("", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testToPattern() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormat(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your age is {1}";
        String pattern2 = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern1);
        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format3));
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(null));
    }

    @Test
    public void testHashCode() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern);
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testGetFormat() {
        String pattern = "Hello {0}, your {2} is {1}";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new Format() {};
            }
        };
        registry.put("customFormat", factory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        Format result = format.getFormat("customFormat");
        assertNotNull(result);
    }
}