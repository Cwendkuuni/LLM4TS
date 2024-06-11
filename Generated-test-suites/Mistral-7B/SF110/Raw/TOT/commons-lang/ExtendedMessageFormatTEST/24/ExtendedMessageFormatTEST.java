package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatWithDefaultRegistry() {
        final String pattern = "Hello, {0}, your balance is {1}, {2}.";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        final String result = format.format(new Object[]{"John", 123.45, "Doe"});
        assertEquals("Hello, John, your balance is 123.45, Doe.", result);
    }

    @Test
    public void testExtendedMessageFormatWithCustomRegistry() {
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = Collections.singletonMap("currency", new CurrencyFormatFactory());
        final String pattern = "{0,date,short} {1,number,currency} {2}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        final Object[] args = {new Date(), 123.45, "EUR"};
        final String result = format.format(args);
        assertEquals("Jan 31, 2023 $123.45 EUR", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternFormat() {
        final String pattern = "Hello, {0, {1}}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        // This test should throw an IllegalArgumentException
        format.format(new Object[]{"John"});
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidFormatArgument() {
        final String pattern = "Hello, {0}, your balance is {1,number,invalid}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        final Object[] args = {new Object[]{"John"}, 123.45};
        // This test should throw a NumberFormatException
        format.format(args);
    }

    @Test
    public void testFormatWithNullArguments() {
        final String pattern = "Hello, {0}, {1}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        final Object[] args = {null, null};
        final String result = format.format(args);
        assertEquals("Hello, null, null", result);
    }

    @Test
    public void testFormatWithEmptyArguments() {
        final String pattern = "Hello, {0}, {1}";
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        final Object[] args = {};
        final String result = format.format(args);
        assertEquals("Hello, , ", result);
    }
}