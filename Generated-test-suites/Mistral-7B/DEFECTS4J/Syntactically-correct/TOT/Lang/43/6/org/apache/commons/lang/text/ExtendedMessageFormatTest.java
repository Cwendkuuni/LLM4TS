package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.text.ExtendedMessageFormat;

/**
 * Test cases for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with empty pattern.
     */
    @org.junit.Test
    public void testEmptyPattern() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        assertNull(format.getArguments());
    }

    /**
     * Test constructor with null pattern.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullPattern() {
        new ExtendedMessageFormat(null);
    }

    /**
     * Test constructor with simple pattern.
     */
    @org.junit.Test
    public void testSimplePattern() {
        final String pattern = "Hello {0}!";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
        assertNotNull(format.getArguments());
        assertEquals(0, format.getArguments().length);
    }

    /**
     * Test constructor with registry.
     */
    @org.junit.Test
    public void testRegistry() {
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Currency", new CurrencyFormatFactory());

        final String pattern = "The price is {0, format=Currency}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertNotNull(format.getArguments());
        assertEquals(1, format.getArguments().length);
    }

    /**
     * Test constructor with null registry.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullRegistry() {
        final String pattern = "The price is {0}";
        new ExtendedMessageFormat(pattern, null);
    }

    /**
     * Test applyPattern with empty pattern.
     */
    @org.junit.Test
    public void testApplyPatternEmptyPattern() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.applyPattern("Hello {0}!");
        assertEquals("", format.toPattern());
    }

    /**
     * Test applyPattern with no format elements.
     */
    @org.junit.Test
    public void testApplyPatternNoFormatElements() {
        final String pattern = "Hello {0}!";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello {0}!");
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test applyPattern with one format element.
     */
    @org.junit.Test
    public void testApplyPatternOneFormatElement() {
        final String pattern = "Hello {0}!";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello {0}! {1}");
        assertEquals("Hello {0}! {1}", format.toPattern());
    }

    /**
     * Test applyPattern with multiple format elements.
     */
    @org.junit.Test
    public void testApplyPatternMultipleFormatElements() {
        final String pattern = "Hello {0, number, currency} {1, choice, 0#No|1#Yes}!";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello {0, number, currency} {1, choice, 0#No|1#Yes}! {2}");
        assertEquals("Hello {0, number, currency} {1, choice, 0#No|1#Yes}! {2}",
                format.toPattern());
    }

    /**
     * Test applyPattern with invalid format name.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalidFormatName() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1, invalid}");
        format.applyPattern("{0} {1, invalid}");
    }

    /**
     * Test setFormat with null format.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatNullFormat() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormat(0, null);
    }

    /**
     * Test setFormat with valid format.
     */
    @org.junit.Test
    public void testSetFormatValidFormat() {
        final Format format = new SimpleDateFormat("dd.MM.yyyy");
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, dd.MM.yyyy}");
        format.setFormat(0, format);
        assertEquals("{0, date, dd.MM.yyyy}", format.toPattern());
    }

    /**
     * Test format with no arguments.
     */
    @org.junit.Test
    public void testFormatNoArguments() {
        final String pattern = "Hello {0}!";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("Hello !", format.format());
    }

    /**
     * Test format with one argument.
     */
    @org.junit.Test
    public void testFormatOneArgument() {
        final String pattern = "Hello {0}!";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("Hello World!", format.format(new Object[]{"World"}));
    }

    /**
     * Test format with multiple arguments.
     */
    @org.junit.Test
    public void testFormatMultipleArguments() {
        final String pattern = "Hello {0}! {1, number, currency}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("Hello World! $10.23", format.format(new Object[]{"World", 10.23}));
    }

    /**
     * Test parse with no format elements.
     */
    @org.junit.Test
    public void testParseNoFormatElements() throws ParseException {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        final ParsePosition pos = new ParsePosition(0);
        format.parse("Hello World!", pos);
        assertEquals(14, pos.getIndex());
    }

    /**
     * Test parse with one format element.
     */
    @org.junit.Test
    public void testParseOneFormatElement() throws ParseException {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        final ParsePosition pos = new ParsePosition(0);
        format.parse("Hello John! {1}!", pos);
        assertEquals(12, pos.getIndex());
    }

    /**
     * Test parse with multiple format elements.
     */
    @org.junit.Test
    public void testParseMultipleFormatElements() throws ParseException {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}! {1, number, currency}");
        final ParsePosition pos = new ParsePosition(0);
        format.parse("Hello John! {1, number, currency} {2}", pos);
        assertEquals(18, pos.getIndex());
    }

    /**
     * Test parse with invalid format element.
     */
    @org.junit.Test(expected = ParseException.class)
    public void testParseInvalidFormatElement() throws ParseException {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1, invalid}!");
        final ParsePosition pos = new ParsePosition(0);
        format.parse("Hello World {1, invalid}!", pos);
    }

    /**
     * Test parse with missing argument.
     */
    @org.junit.Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}!");
        final ParsePosition pos = new ParsePosition(0);
        format.parse("Hello World!", pos);
    }

    /**
     * Test parse with extra argument.
     */
    @org.junit.Test(expected = ParseException.class)
    public void testParseExtraArgument() throws ParseException {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        final ParsePosition pos = new ParsePosition(0);
        format.parse("Hello World! {1}", pos);
    }

    /**
     * Test parse with empty argument.
     */
    @org.junit.Test
    public void testParseEmptyArgument() throws ParseException {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        final ParsePosition pos = new ParsePosition(0);
        format.parse("Hello {}!", pos);
        assertEquals(6, pos.getIndex());
    }

    /**
     * Test format with format factory.
     */
    @org.junit.Test
    public void testFormatWithFormatFactory() {
        final String pattern = "The price is {0, format=Currency}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("The price is $10.23", format.format(new Object[]{10.23}));
    }

    /**
     * Test format with format factory and invalid format name.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatWithFormatFactoryInvalidFormatName() {
        final String pattern = "The price is {0, format=Invalid}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.format(new Object[]{10.23});
    }
}

class CurrencyFormatFactory implements FormatFactory {

    @Override
    public Format getFormat(String name, String argument, Locale locale) {
        if ("Currency".equals(name)) {
            return new SimpleDateFormat("$###,###.##");
        }
        throw new IllegalArgumentException("Invalid format name: " + name);
    }
}