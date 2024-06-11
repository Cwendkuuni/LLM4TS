package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.ExtendedMessageFormat;

/**
 * Test cases for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with default locale.
     */
    @org.junit.Test
    public void testConstructDefaultLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Validate.notNull(format);
        assertEquals("", format.toPattern());
    }

    /**
     * Test constructor with specified locale.
     */
    @org.junit.Test
    public void testConstructSpecifiedLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        Locale locale = new Locale("de", "DE");
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        Validate.notNull(format);
        assertEquals("", format.toPattern());
    }

    /**
     * Test constructor with registry.
     */
    @org.junit.Test
    public void testConstructRegistry() {
        String pattern = "Hello {0}, your age is {1, date, 'medium'}. ";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormatFactory("yyyy-MM-dd"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Validate.notNull(format);
        assertEquals("", format.toPattern());
    }

    /**
     * Test applying a custom pattern.
     */
    @org.junit.Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1, date, 'medium'}. ";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello {0}, your age is {1, date, 'medium'}. ");
        assertEquals("Hello {0}, your age is {1, date, 'medium'}. ", format.toPattern());
    }

    /**
     * Test applying a custom pattern with registry.
     */
    @org.junit.Test
    public void testApplyPatternWithRegistry() {
        String pattern = "Hello {0}, your age is {1, date, 'medium'}. ";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormatFactory("yyyy-MM-dd"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern("Hello {0}, your age is {1, date, 'medium'}. ");
        assertEquals("Hello {0}, your age is {1, date, 'medium'}. ", format.toPattern());
    }

    /**
     * Test setting an invalid format index.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetFormatInvalidIndex() {
        String pattern = "Hello {0}, your age is {1}. ";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormat(1, new MessageFormat("Invalid format"));
    }

    /**
     * Test setting a format by argument index.
     */
    @org.junit.Test
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}. ";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatByArgumentIndex(1, new MessageFormat("Age (years): {0}"));
        String formatted = format.format(new Object[]{"John Doe", 30});
        assertEquals("Hello John Doe, your age is Age (years): 30. ", formatted);
    }

    /**
     * Test setting formats.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}, your age is {1}. ";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = new Format[]{new MessageFormat("Age (years): {0}")};
        format.setFormats(formats);
    }

    /**
     * Test setting formats by argument index.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}. ";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = new Format[]{new MessageFormat("Age (years): {0}")};
        format.setFormatsByArgumentIndex(new int[]{1}, formats);
    }

    /**
     * Test setting a null pattern.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructNullPattern() {
        new ExtendedMessageFormat(null);
    }

    /**
     * Test setting a null format factory.
     */
    @org.junit.Test
    public void testConstructNullFormatFactory() {
        String pattern = "Hello {0}, your age is {1, date, 'medium'}. ";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", null);
        assertThrows(NullPointerException.class, () -> new ExtendedMessageFormat(pattern, registry));
    }

    /**
     * Test setting an invalid format description.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetFormatInvalidDescription() {
        String pattern = "Hello {0}, your age is {1}. ";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
        format.setFormatByArgumentIndex(1, new Object(){});
    }

}