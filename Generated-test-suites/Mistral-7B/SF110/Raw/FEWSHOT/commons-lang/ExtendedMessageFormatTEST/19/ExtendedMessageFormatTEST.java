package org.apache.commons.lang3.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.Format;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertSame(MessageFormat.class, fmt.getClass());
        assertEquals("", fmt.toPattern());
        assertNull(fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testLocaleConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        assertSame(MessageFormat.class, fmt.getClass());
        assertEquals("{0}", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testRegistryConstructor() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry);
        assertSame(MessageFormat.class, fmt.getClass());
        assertEquals("{1, date}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testRegistryConstructorWithLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        assertSame(MessageFormat.class, fmt.getClass());
        assertEquals("{1, date}", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0} {1, date}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Locale.US);
        String expected = "foo 26-Mar-2023";
        Object[] args = {"foo", new java.text.DateFormatSymbols(Locale.US)};
        assertEquals(expected, fmt.format(args));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        fmt.setFormat(0, new DateFormat()); // Unsupported
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        fmt.setFormatByArgumentIndex(0, new DateFormat()); // Unsupported
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        Format[] formats = new Format[1];
        formats[0] = new DateFormat();
        fmt.setFormats(formats); // Unsupported
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        Format[] formats = new Format[1];
        formats[0] = new DateFormat();
        fmt.setFormatsByArgumentIndex(formats); // Unsupported
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        Format format = fmt.getFormat("date");
        assertNotNull(format);
        assertTrue(format instanceof DateFormat);
    }

    @Test
    public void testGetFormatNull() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        Format format = fmt.getFormat("unknown");
        assertNull(format);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}", Locale.US);
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}", Locale.US);
        assertTrue(fmt1.equals(fmt2));

        fmt2 = new ExtendedMessageFormat("{0}", Locale.UK);
        assertFalse(fmt1.equals(fmt2));

        fmt1 = new ExtendedMessageFormat("{0}", Locale.US);
        fmt2 = new ExtendedMessageFormat("{0}", null);
        assertFalse(fmt1.equals(fmt2));

        fmt1 = new ExtendedMessageFormat("{0}", null);
        fmt2 = new ExtendedMessageFormat("{1}", Locale.US);
        assertFalse(fmt1.equals(fmt2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}", Locale.US);
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}", Locale.US);
        assertEquals(fmt1.hashCode(), fmt2.hashCode());

        fmt2 = new ExtendedMessageFormat("{0}", Locale.UK);
        assertNotEquals(fmt1.hashCode(), fmt2.hashCode());

        fmt1 = new ExtendedMessageFormat("{0}", Locale.US);
        fmt2 = new ExtendedMessageFormat("{1}", Locale.US);
        assertNotEquals(fmt1.hashCode(), fmt2.hashCode());

        fmt1 = new ExtendedMessageFormat("{0}", Locale.US);
        fmt2 = new ExtendedMessageFormat("{0}", new HashMap<>());
        assertNotEquals(fmt1.hashCode(), fmt2.hashCode());

        fmt1 = new ExtendedMessageFormat("{0}", new HashMap<>());
        fmt2 = new ExtendedMessageFormat("{0}", Locale.US);
        assertNotEquals(fmt1.hashCode(), fmt2.hashCode());
    }

    @Test
    public void testRegistryGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        Format format = fmt.getFormat("date");
        assertNotNull(format);
        assertTrue(format instanceof DateFormat);
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        String expected = "26-Mar-2023";
        Object[] args = {null};
        assertEquals(expected, fmt.format(args));
    }

    @Test
    public void testApplyPatternWithRegistryAndLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        String expected = "26-Mar-2023";
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US)};
        assertEquals(expected, fmt.format(args));
    }

    @Test
    public void testApplyPatternWithCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new CustomDateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        String expected = "CustomDateFormat";
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US)};
        assertEquals(expected, fmt.format(args));
    }

    @Test
    public void testApplyPatternWithCustomFormatAndLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new CustomDateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        String expected = "CustomDateFormat";
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.UK)};
        assertEquals(expected, fmt.format(args));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US)};
        fmt.applyPattern("{3, date}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry, Locale.US);
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US)};
        fmt.applyPattern("{1, invalid}");
    }

    @Test
    public void testApplyPatternWithMultipleFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date} {0, number}", registry, Locale.US);
        String expected = "26-Mar-2023 42";
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US), new DecimalFormat()};
        assertEquals(expected, fmt.format(args));
    }

    @Test
    public void testApplyPatternWithEmptyFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, } {0}", registry, Locale.US);
        String expected = " { }";
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US)};
        assertEquals(expected, fmt.format(args));
    }

    @Test
    public void testApplyPatternWithMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date} {0, format}", registry, Locale.US);
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US), new DateFormat()};
        try {
            fmt.applyPattern("{1, date} {0, format}");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApplyPatternWithMissingFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date} {0, format}", registry, Locale.US);
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US), null};
        try {
            fmt.applyPattern("{1, date} {0, format}");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApplyPatternWithMissingFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, unknown}", registry, Locale.US);
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US), new DateFormat()};
        try {
            fmt.applyPattern("{1, unknown}");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApplyPatternWithMissingLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date} {0}", registry);
        Object[] args = {"", new java.text.DateFormatSymbols(Locale.US), new DateFormat()};
        try {
            fmt.applyPattern("{1, date} {0}");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private static class DateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String description, Locale locale, Object arg) {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    }

    private static class NumberFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String description, Locale locale, Object arg) {
            return new DecimalFormat();
        }
    }

    private static class CustomDateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String description, Locale locale, Object arg) {
            return new SimpleDateFormat("CustomDateFormat");
        }
    }
}