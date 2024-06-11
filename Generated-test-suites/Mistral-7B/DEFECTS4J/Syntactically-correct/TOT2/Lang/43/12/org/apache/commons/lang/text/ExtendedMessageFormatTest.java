import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        String result = format.format(new Object[]{"John"});
        assertEquals("Hello John", result);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}", Locale.FRENCH);
        String result = format.format(new Object[]{"John"});
        assertTrue(StringUtils.isNotEmpty(result));
    }

    @Test
    public void testConstructorWithRegistry() {
        String pattern = "Hello {0, date, short} {1}";

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(arguments, locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = new Object[]{"John", new Date()};
        String result = format.format(args);
        String expected = String.format("Hello %s %s", "John", "Mon Jan 25 12:34:56 GMT+01:00 2021");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        new ExtendedMessageFormat("{0}Hello{1}"); // missing format element
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatName() {
        new ExtendedMessageFormat("{, date, short} {0}", Collections.emptyMap()); // missing format name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatStyle() {
        new ExtendedMessageFormat("{0, date} {1}", Collections.emptyMap()); // missing format style
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownFormatName() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, unknownFormat} {1}", Collections.emptyMap());
    }

    @Test
    public void testFormatElementWithCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MessageFormat("{0} {1}");
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, customFormat, date, short} {1}", registry);
        Object[] args = new Object[]{"John", new Date()};
        String result = format.format(args);
        String expected = String.format("Hello {0} %s {1}", "John", "Mon Jan 25 12:34:56 GMT+01:00 2021");
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        String pattern = "{0, unknownFormat} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        Object[] args = new Object[]{"John", new Date()};
        format.format(args);
    }
}