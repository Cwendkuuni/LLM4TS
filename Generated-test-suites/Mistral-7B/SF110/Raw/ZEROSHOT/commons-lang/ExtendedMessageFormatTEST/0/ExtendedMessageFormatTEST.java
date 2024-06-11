package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertNotNull(fmt);
        assertEquals("", fmt.toPattern());
        assertNull(fmt.registry);
    }

    @org.junit.Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        assertNotNull(fmt);
        assertEquals("{0}, {1}", fmt.toPattern());
        assertNull(fmt.registry);
    }

    @org.junit.Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertNotNull(fmt);
        assertEquals("{0}, {1}", fmt.toPattern());
        assertNull(fmt.registry);
    }

    @org.junit.Test
    public void testConstructorWithPatternAndRegistry() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (FormatFactory) (name, args, locale) -> sdf);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{date}, {0}", registry);
        assertNotNull(fmt);
        assertEquals("{date}, {0}", fmt.toPattern());
        assertEquals(registry, fmt.registry);
    }

    @org.junit.Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (FormatFactory) (name, args, locale) -> sdf);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{date}, {0}", Locale.US, registry);
        assertNotNull(fmt);
        assertEquals("{date}, {0}", fmt.toPattern());
        assertEquals(registry, fmt.registry);
    }

    @org.junit.Test
    public void testToPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", fmt.toPattern());

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (FormatFactory) (name, args, locale) -> new SimpleDateFormat("yyyy-MM-dd"));
        fmt = new ExtendedMessageFormat("{date}, {0}", registry);
        assertEquals("{date}, {0}", fmt.toPattern());
    }

    @org.junit.Test
    public void testApplyPattern() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (FormatFactory) (name, args, locale) -> sdf);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{date}, {0}", registry);
        Object[] args = {"John", new Date()};
        String result = fmt.format(args);
        assertEquals("{date}, John", fmt.toPattern());
        assertEquals("2023-03-29, John", result);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormats(new Format[0]);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormatsByArgumentIndex(new Format[0]);
    }

    @org.junit.Test
    public void testEquals() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}, {1}");
        assertTrue(fmt1.equals(fmt2));
        fmt2.registry = new HashMap<>();
        assertFalse(fmt1.equals(fmt2));
        fmt1.registry = new HashMap<>();
        assertTrue(fmt1.equals(fmt2));
    }

    @org.junit.Test
    public void testHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}, {1}");
        int hc1 = fmt1.hashCode();
        int hc2 = fmt2.hashCode();
        assertEquals(hc1, hc2);

        fmt2.registry = new HashMap<>();
        int hc3 = fmt2.hashCode();
        assertNotEquals(hc1, hc3);

        fmt1.registry = new HashMap<>();
        int hc4 = fmt1.hashCode();
        assertEquals(hc3, hc4);
    }

    @org.junit.Test
    public void testGetFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (FormatFactory) (name, args, locale) -> sdf);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{date}, {0}", registry);
        Format format = fmt.getFormat("date");
        assertNotNull(format);
        assertTrue(format instanceof SimpleDateFormat);
    }

    @org.junit.Test
    public void testReadArgumentIndex() {
        String pattern = "Hello, {0}, {1}";
        ParsePosition pos = new ParsePosition(0);
        int index = fmt.readArgumentIndex(pattern, pos);
        assertEquals(0, index);

        pos.setIndex(4);
        index = fmt.readArgumentIndex(pattern, pos);
        assertEquals(1, index);

        pos.setIndex(11);
        try {
            index = fmt.readArgumentIndex(pattern, pos);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseFormatDescription() {
        String pattern = "Hello, {date}, {0}";
        ParsePosition pos = new ParsePosition(0);
        String description = fmt.parseFormatDescription(pattern, pos);
        assertEquals("date", description);

        pos.setIndex(7);
        description = fmt.parseFormatDescription(pattern, pos);
        assertEquals(null, description);
    }

}