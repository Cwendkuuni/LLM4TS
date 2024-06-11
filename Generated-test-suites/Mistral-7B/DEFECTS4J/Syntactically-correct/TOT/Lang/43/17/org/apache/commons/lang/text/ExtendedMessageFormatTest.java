package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.text.ExtendedMessageFormat;

/**
 * Tests for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Helper method to create an {@link ExtendedMessageFormat} instance with
     * given pattern and format registry.
     *
     * @param pattern Message format pattern.
     * @param registry Map of format factories.
     * @return ExtendedMessageFormat instance.
     */
    private static ExtendedMessageFormat createFormat(String pattern, Map<String, FormatFactory> registry) {
        return new ExtendedMessageFormat(pattern, registry);
    }

    /**
     * Tests basic case: no custom formats.
     */
    @Test
    public void testBasic() {
        Map<String, FormatFactory> registry = Collections.emptyMap();
        ExtendedMessageFormat format = createFormat("{0} {1}", registry);
        Object[] args = {"Hello", "world"};
        String result = format.format(args);
        assertEquals("Hello world", result);
    }

    /**
     * Tests case with custom format.
     */
    @Test
    public void testCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", (formatName, args, locale) -> new CustomFormat("{0} {1}", args[0]));
        ExtendedMessageFormat format = createFormat("{custom,date,long}" +
                "{0} {1}", registry);
        Object[] args = {"2023-03-15", "is a great day"};
        String result = format.format(args);
        assertEquals("2023-03-15 is a great day", result);
    }

    /**
     * Tests case with invalid format element.
     */
    @Test
    public void testInvalidFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = createFormat("{0}{1} is invalid", registry);
        Object[] args = {"Hello", "world"};
        assertThrows(IllegalArgumentException.class, () -> format.format(args));
    }

    /**
     * Tests case with missing format factory.
     */
    @Test
    public void testMissingFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = createFormat("{missingFormat}{0}", registry);
        Object[] args = {"Hello"};
        assertNull(format.format(args));
    }

    /**
     * Tests case with parse error.
     */
    @Test
    public void testParseError() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", (formatName, args, locale) -> new CustomFormat("{0}"));
        ExtendedMessageFormat format = createFormat("{custom,date,long}{ invalid }", registry);
        Object[] args = {};
        assertThrows(ParseException.class, () -> format.parse(null, args));
    }

    /**
     * Tests case with null arguments.
     */
    @Test
    public void testNullArguments() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = createFormat("{0} {1}", registry);
        Object[] args = {null, "world"};
        String result = format.format(args);
        assertEquals("null world", result);
    }
}

class CustomFormat implements Format {

    private final String pattern;

    public CustomFormat(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String format(Object arg0) {
        return String.format(pattern, arg0);
    }

    @Override
    public ParsePosition parse(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
}