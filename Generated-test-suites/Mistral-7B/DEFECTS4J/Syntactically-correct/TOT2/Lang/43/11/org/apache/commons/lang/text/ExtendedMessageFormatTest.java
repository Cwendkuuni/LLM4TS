package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.ExtendedMessageFormat;

/**
 * Test for {@link ExtendedMessageFormat} class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with empty pattern.
     */
    @Test
    public void testEmptyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertNotNull(fmt);
    }

    /**
     * Test constructor with default locale and registry as null.
     */
    @Test
    public void testDefaultLocaleAndNullRegistry() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        assertNotNull(fmt);
        assertEquals("Hello , your age is .", fmt.toPattern());
    }

    /**
     * Test constructor with default locale and custom registry.
     */
    @Test
    public void testDefaultLocaleAndCustomRegistry() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        assertNotNull(fmt);
        assertEquals("Hello {0}, your age is {1}", fmt.toPattern());
        assertEquals(getMessage("Hello {0}, your age is {1}", new Object[] {"John", 25}),
                fmt.format(new Object[] {"John", 25}));
    }

    /**
     * Test constructor with specified locale and registry as null.
     */
    @Test
    public void testSpecifiedLocaleAndNullRegistry() {
        Locale locale = Locale.CHINESE;
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, locale);
        assertNotNull(fmt);
        assertEquals("Hello {0}, your age is {1}", fmt.toPattern());
        assertEquals(getMessage("Hello {0}, your age is {1}", new Object[] {"John", 25}),
                fmt.format(new Object[] {"John", 25}));
    }

    /**
     * Test constructor with specified locale and custom registry.
     */
    @Test
    public void testSpecifiedLocaleAndCustomRegistry() {
        Locale locale = Locale.CHINESE;
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, locale, registry);
        assertNotNull(fmt);
        assertEquals("Hello {0}, your age is {1}", fmt.toPattern());
        assertEquals(getMessage("Hello {0}, your age is {1}", new Object[] {"John", 25}),
                fmt.format(new Object[] {"John", 25}));
    }

    /**
     * Test toPattern() method.
     */
    @Test
    public void testToPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        assertEquals("Hello {0}, your age is {1}", fmt.toPattern());
    }

    /**
     * Test applyPattern() method with empty pattern.
     */
    @Test
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        fmt.applyPattern("");
        assertEquals("", fmt.toPattern());
    }

    /**
     * Test applyPattern() method with simple pattern.
     */
    @Test
    public void testApplyPatternWithSimplePattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("Hello {0}");
        String pattern = "Hello {0}";
        fmt.applyPattern(pattern);
        assertEquals(pattern, fmt.toPattern());
    }

    /**
     * Test applyPattern() method with more complex pattern.
     */
    @Test
    public void testApplyPatternWithComplexPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        String pattern = "Hello {0}, your age is {1}.";
        fmt.applyPattern(pattern);
        assertEquals(pattern, fmt.toPattern());
    }

    /**
     * Test applyPattern() method with invalid pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        String pattern = "Hello {0}, your age is {1}.";
        fmt.applyPattern("{0}");
    }

    /**
     * Test getFormat() method with valid format name and style.
     */
    @Test
    public void testGetFormatWithValidFormatNameAndStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", registry);
        Format format = fmt.getFormat("customFormat");
        assertNotNull(format);
    }

    /**
     * Test getFormat() method with invalid format name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", registry);
        fmt.getFormat("InvalidFormat");
    }

    /**
     * Test setFormat() method with valid format name and format.
     */
    @Test
    public void testSetFormatWithValidFormatNameAndFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", registry);
        Format format = new SimpleFormat("CustomFormat");
        fmt.setFormat("customFormat", format);
        Format getFormat = fmt.getFormat("customFormat");
        assertEquals(format, getFormat);
    }

    /**
     * Test setFormat() method with invalid format name.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", registry);
        fmt.setFormat("customFormat", new SimpleFormat("CustomFormat"));
    }

    /**
     * Test format() method with empty arguments.
     */
    @Test
    public void testFormatWithEmptyArguments() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        String formatted = fmt.format(new Object[0]);
        assertEquals("", formatted);
    }

    /**
     * Test format() method with valid arguments.
     */
    @Test
    public void testFormatWithValidArguments() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("Hello {0}");
        String formatted = fmt.format(new Object[] {"John"});
        assertEquals("Hello John", formatted);
    }

    /**
     * Test format() method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidArguments() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("Hello {0}");
        fmt.format(new Object[2]);
    }

    /**
     * Test format() method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullArguments() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("Hello {0}");
        fmt.format(new Object[] {null});
    }

    /**
     * Helper method to get a message using MessageFormat.
     *
     * @param pattern message pattern
     * @param args arguments to format
     * @return formatted message
     */
    private String getMessage(String pattern, Object[] args) {
        MessageFormat messageFormat = new MessageFormat(pattern);
        String formatted = messageFormat.format(args);
        return formatted;
    }

    /**
     * Custom FormatFactory implementation for testing.
     */
    static class CustomFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            return new SimpleFormat("CustomFormat");
        }
    }

    /**
     * Simple Format implementation for testing.
     */
    static class SimpleFormat implements Format {

        private final String name;

        SimpleFormat(String name) {
            this.name = name;
        }

        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition pos) {
            return name;
        }

        @Override
        public Class<?> getParameterTypes() {
            return Object.class;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}