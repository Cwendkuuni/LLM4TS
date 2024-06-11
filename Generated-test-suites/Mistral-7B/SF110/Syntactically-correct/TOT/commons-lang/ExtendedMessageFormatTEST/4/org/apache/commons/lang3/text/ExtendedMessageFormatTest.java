package org.apache.commons.lang3.text;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testBasicFunctionality() {
        final String pattern = "{0}, the number {1} squared is {2}";
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());

        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        final Object[] args = {1, 2, 3};
        final String result = format.format(args);
        assertEquals("1, the number 2 squared is 3", result);

        final ParsePosition pos = new ParsePosition(0);
        try {
            final Object[] parsedArgs = format.parse(result, args, pos);
            assertEquals(args, parsedArgs);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatElement() {
        final String pattern = "{0}, the number {1} squared is {";
        final Map<String, FormatFactory> registry = new HashMap<>();
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        final Object[] args = {1, 2, 3};
        format.format(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatDescription() {
        final String pattern = "{0}, the number {1} squared is {format}";
        final Map<String, FormatFactory> registry = new HashMap<>();
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        final Object[] args = {1, 2, 3};
        format.format(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        final String pattern = "{0}, the number {1} squared is {invalidFormat}";
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("invalidFormat", new InvalidFormatFactory());
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        final Object[] args = {1, 2, 3};
        format.format(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        final String pattern = "{10}, the number {1} squared is {2}";
        final Map<String, FormatFactory> registry = new HashMap<>();
        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        final Object[] args = {1, 2, 3};
        format.applyPattern(pattern);
    }

    @Test
    public void testCustomFormat() {
        final String pattern = "{0}, the number {1} formatted as {customFormat} is {2}";
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        final ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        final Object[] args = {1, 2, 3};
        final String result = format.format(args);
        assertEquals("1, the number 2 formatted as PERCENT is 3", result);

        final ParsePosition pos = new ParsePosition(0);
        try {
            final Object[] parsedArgs = format.parse(result, args, pos);
            assertEquals(args, parsedArgs);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}

class NumberFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String pattern, Locale locale) {
        return new DecimalFormat(pattern);
    }
}

class InvalidFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String pattern, Locale locale) {
        throw new IllegalArgumentException("Invalid factory description");
    }
}

class CustomFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String pattern, Locale locale) {
        return new DecimalFormat("0%");
    }
}