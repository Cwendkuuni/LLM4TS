import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class ExtendedMessageFormatTest {

    @Test
    public void testBasicUsage() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}{2}", registry);
        Object[] args = {"Hello", "World", "!"};
        String result = format.format(args);
        String expected = "Hello, World!";
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyArgumentList() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        Object[] args = {};
        String result = format.format(args);
        assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, percentage}", registry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, myFormat}", registry);
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormatPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, {1}}", registry);
    }

    @Test
    public void testLocaleChange() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("dateFormat", new DateFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, locale=fr_FR}", registry);
        Object[] args = {"2023-03-15"};
        String result = format.format(args);
        String expected = "15 mars 2023";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatFactoryException() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("errorFormatFactory", new ErrorFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        Object[] args = {"Error Occurred"};

        try {
            format.format(args);
            fail("Format factory exception expected");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testFormatFactoryNullArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("nullArgumentFormatFactory", new NullArgumentFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        Object[] args = {null};

        try {
            format.format(args);
            fail("Format factory null argument exception expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            return new CustomFormat();
        }

        private class CustomFormat extends MessageFormat {
            public CustomFormat() {
                super("Custom Format", Locale.getDefault(), null);
            }

            @Override
            public String format(Object[] args, String format, Locale locale) {
                return Arrays.toString(args);
            }
        }
    }

    private static class DateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            return new java.text.SimpleDateFormat(style, locale);
        }
    }

    private static class ErrorFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            throw new RuntimeException("Error occurred");
        }
    }

    private static class NullArgumentFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            throw new NullPointerException();
        }
    }
}