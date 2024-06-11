import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * JUnit 4 test file for ExtendedMessageFormat class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with simple pattern.
     */
    @org.junit.Test
    public void testSimplePattern() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String result = format.format(new Object[]{"World"});
        assertEquals("Hello, World!", result);
    }

    /**
     * Test constructor with simple pattern and custom registry.
     */
    @org.junit.Test
    public void testSimplePatternWithRegistry() {
        String pattern = "Hello, {0, myCustomFormat}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myCustomFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.format(new Object[]{"World"});
        assertEquals("Hello, [CustomFormat:World]!", result);
    }

    /**
     * Test constructor with complex pattern.
     */
    @org.junit.Test
    public void testComplexPattern() {
        String pattern = "Hello, {0} ({1, choice, 0#No|1#Yes} {2, plural, one={3} two={4} other={5}}).";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myCustomFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String[] arguments = {"John", 1, "apple", 1, "apples", "fruit"};
        String result = format.format(arguments);
        assertEquals("Hello, John (Yes apple).", result);
    }

    /**
     * Test setLocale method.
     */
    @org.junit.Test
    public void testSetLocale() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setLocale(Locale.FRENCH);
        String result = format.format(new Object[]{"World"});
        assertEquals("Hello, Monde!", result);
    }

    /**
     * Test toPattern method.
     */
    @org.junit.Test
    public void testToPattern() {
        String pattern = "Hello, {0, choice, 0#No|1#Yes} {1, plural, one={2} two={3} other={4}}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myCustomFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.toPattern();
        assertTrue(result.contains("{0, choice, 0#No|1#Yes}"));
        assertTrue(result.contains("{1, plural, one={2} two={3} other={4}}"));
    }

    /**
     * Test format method with various input.
     */
    @org.junit.Test
    public void testFormat() {
        String pattern = "Hello, {0}! Your age is {1, number, integer}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] arguments = {"John", 25};
        String result = format.format(arguments);
        assertEquals("Hello, John! Your age is 25.", result);
    }

    /**
     * Test parse method.
     */
    @org.junit.Test
    public void testParse() {
        String pattern = "Hello, {0, choice, 0#No|1#Yes} {1, plural, one={2} two={3} other={4}}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myCustomFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String input = "Hello, No apple.";
        ParsePosition pos = new ParsePosition(0);
        Object[] arguments = format.parse(input, pos);
        assertEquals(2, arguments.length);
        assertEquals("No", arguments[0]);
        assertEquals(null, arguments[1]);
    }

    /**
     * Test blank pattern.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBlankPattern() {
        new ExtendedMessageFormat("");
    }

    /**
     * Test custom format factory.
     */
    private static class CustomFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            return new CustomFormat();
        }
    }

    /**
     * Test custom format.
     */
    private static class CustomFormat implements Format {

        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
            if (obj != null) {
                return "[CustomFormat:" + obj.toString() + "]";
            }
            return "";
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }
}