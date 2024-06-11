package org.apache.commons.lang.text;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testBasicUsage() {
        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0, choice, 0} {1} {2, format, customFormat}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{"world", "foo", "bar"};
        String result = format.format(args);
        assertEquals("Hello world foo bar", result);
    }

    @Test
    public void testMissingFormat() {
        Map<String, FormatFactory> registry = new java.util.HashMap<>();

        String pattern = "Hello {0, format, missingFormat}.{1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{"world"};
        try {
            format.format(args);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCustomFormat() {
        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0, choice, 0} {1, format, customFormat}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{"world", "Custom: {0}"};
        String result = format.format(args);
        assertEquals("Hello world Custom: world", result);
    }

    @Test
    public void testNestedFormats() {
        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        registry.put("customFormat1", new CustomFormatFactory1());
        registry.put("customFormat2", new CustomFormatFactory2());

        String pattern = "Hello {0, choice, 0} {1, format, customFormat1} {2, format, customFormat2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{"world", "Custom1: {0}", "Custom2: {0}"};
        String result = format.format(args);
        assertEquals("Hello world Custom1: Custom1: world Custom2: Custom2: world", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, null);
        format.setFormat(0, new Format() {});
        // Should throw UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, null);
        format.setFormatByArgumentIndex(0, new Format() {});
        // Should throw UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, null);
        Format[] formats = new Format[1];
        format.setFormats(formats);
        // Should throw UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, null);
        Format[] formats = new Format[1];
        format.setFormatsByArgumentIndex(formats);
        // Should throw UnsupportedOperationException
    }

    static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new CustomFormat();
        }
    }

    static class CustomFormatFactory1 implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new CustomFormat1();
        }
    }

    static class CustomFormatFactory2 implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new CustomFormat2();
        }
    }

    static class CustomFormat implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "Custom";
        }

        @Override
        public String format(Object obj, Locale locale) {
            return "Custom";
        }
    }

    static class CustomFormat1 implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "{0}";
        }

        @Override
        public String format(Object obj, Locale locale) {
            String arg = (String) obj;
            return arg;
        }
    }

    static class CustomFormat2 implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "{0}";
        }

        @Override
        public String format(Object obj, Locale locale) {
            String arg = (String) obj;
            return arg.toUpperCase();
        }
    }
}