package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.SimpleDateFormatFactory;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testExtendedMessageFormatConstruction() {
        String pattern = "Hello, {0}! Your birthday is {1, date, short}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertNull(format.getRegistry());
    }

    @org.junit.Test
    public void testExtendedMessageFormatConstructionWithLocale() {
        String pattern = "Hello, {0}! Your birthday is {1, date, short}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals("Hello, {0}! Your birthday is {1,date,short}.", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertNull(format.getRegistry());
    }

    @org.junit.Test
    public void testExtendedMessageFormatConstructionWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        SimpleDateFormatFactory dateFactory = new SimpleDateFormatFactory();
        registry.put("date", dateFactory);
        String pattern = "Hello, {0}! Your birthday is {1, date, short}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals("Hello, {0}! Your birthday is {1,date,short}.", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @org.junit.Test
    public void testExtendedMessageFormatConstructionWithRegistryAndPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        SimpleDateFormatFactory dateFactory = new SimpleDateFormatFactory();
        registry.put("date", dateFactory);
        String pattern = "{0, date, long} is the date, and {1, date, short} is the short date.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals("{0,date,long} is the date, and {1,date,short}.", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @org.junit.Test
    public void testApplyPatternWithSimpleArguments() {
        String pattern = "Hello, {0}! Your birthday is {1, date, short}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        Object[] args = {"John Doe", new java.util.Date()};
        String result = format.format(args);
        assertEquals("Hello, John Doe! Your birthday is Jan 14, 2023.", result);
    }

    @org.junit.Test
    public void testApplyPatternWithCustomFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        SimpleDateFormatFactory dateFactory = new SimpleDateFormatFactory();
        registry.put("date", dateFactory);
        String pattern = "{0, name, text, date, 'dd-MM-yyyy'} is the name and {1, date, 'dd-MM-yyyy'} is the date.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        Object[] args = {"John Doe", new java.util.Date()};
        String result = format.format(args);
        assertEquals("John Doe is the name and 14-01-2023 is the date.", result);
    }

    @org.junit.Test
    public void testApplyPatternWithEscapedQuotes() {
        String pattern = "Hello, {0}! Your birthday is {1, date, ''short''}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        Object[] args = {"John Doe", new java.util.Date()};
        String result = format.format(args);
        assertEquals("Hello, John Doe! Your birthday is 'Jan' '14, 2023'.", result);
    }

    @org.junit.Test
    public void testApplyPatternWithNestedFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        SimpleDateFormatFactory dateFactory = new SimpleDateFormatFactory();
        registry.put("date", dateFactory);
        String pattern = "Hello, {0}! Your birthday is {1, date, 'dd-MM-yyyy'}, and your age is {2, number, integer, #}. Your full name is {3, name, text}. {4}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        Object[] args = {"John Doe", new java.util.Date(), 14, "John Doe Smith", "Nice to meet you!"};
        String result = format.format(args);
        assertEquals("Hello, John Doe! Your birthday is 14-01-2023, and your age is 14. Your full name is John Doe Smith. Nice to meet you!", result);
    }

    @org.junit.Test
    public void testEqualsAndHashCode() {
        Map<String, FormatFactory> registry1 = new HashMap<>();
        SimpleDateFormatFactory dateFactory = new SimpleDateFormatFactory();
        registry1.put("date", dateFactory);
        String pattern1 = "{0, name, text, date, 'dd-MM-yyyy'} is the name and {1, date, 'dd-MM-yyyy'} is the date.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1, Locale.US, registry1);

        Map<String, FormatFactory> registry2 = new HashMap<>();
        SimpleDateFormatFactory dateFactory2 = new SimpleDateFormatFactory();
        registry2.put("date", dateFactory2);
        String pattern2 = "{0, name, text, date, 'dd-MM-yyyy'} is the name and {1, date, 'dd-MM-yyyy'} is the date.";
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2, Locale.US, registry2);

        Map<String, FormatFactory> registry3 = new HashMap<>();
        SimpleDateFormatFactory dateFactory3 = new SimpleDateFormatFactory();
        registry3.put("date", dateFactory3);
        String pattern3 = "{0, name, text, date, 'dd-MM-yyyy'} is the name and {1, date, 'dd-MM-yyyy'} is the date.";
        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern3, Locale.CA, registry3);

        Map<String, FormatFactory> registry4 = new HashMap<>();
        SimpleDateFormatFactory dateFactory4 = new SimpleDateFormatFactory();
        registry4.put("date", dateFactory4);
        String pattern4 = "{0, name, text, date, 'dd-MM-yyyy'} is the name and {1, date, 'dd-MM-yyyy'} is the date.";
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(pattern4, Locale.US, registry4);

        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
        assertTrue(format1.equals(format3));
        assertTrue(format1.equals(format4));

        assertTrue(format1.hashCode() == format2.hashCode());
        assertTrue(format1.hashCode() == format3.hashCode());
        assertTrue(format1.hashCode() == format4.hashCode());

        assertFalse(format1.equals(null));
        assertFalse(format1.equals(""));
        assertFalse(format1.equals(new MessageFormat("")));
        assertFalse(format1.equals(new Object()));
    }

    @org.junit.Test
    public void testSetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        SimpleDateFormatFactory dateFactory = new SimpleDateFormatFactory();
        registry.put("date", dateFactory);
        String pattern = "{0, name, text, date, 'dd-MM-yyyy'} is the name and {1, date, 'dd-MM-yyyy'} is the date.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);

        try {
            format.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
            fail("Should not be able to set format for an element.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
            fail("Should not be able to set format for an element.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            format.setFormats(new Format[0]);
            fail("Should not be able to set formats.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            format.setFormatsByArgumentIndex(new Format[0]);
            fail("Should not be able to set formats.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}

class SimpleDateFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String pattern, Locale locale, Locale alternatives) {
        return new SimpleDateFormat(pattern, locale);
    }
}