package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructors() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, fmt.toPattern());

        pattern = "Hello, {0}, {1}!";
        Locale locale = Locale.US;
        fmt = new ExtendedMessageFormat(pattern, locale);
        assertEquals(pattern, fmt.toPattern());
        assertEquals(locale, fmt.getLocale());

        Map<String, FormatFactory> registry = new HashMap<>();
        pattern = "Hello, {0}, {1}!";
        fmt = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, fmt.toPattern());

        pattern = "Hello, {0}, {1}!";
        locale = Locale.US;
        registry = new HashMap<>();
        fmt = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(pattern, fmt.toPattern());
        assertEquals(locale, fmt.getLocale());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);

        Object[] args = {"Alice", "Bob"};
        String result = fmt.format(args);
        assertEquals("Hello, Alice, Bob!", result);

        pattern = "Hello, {0}, {1}{2}!";
        fmt = new ExtendedMessageFormat(pattern);
        args = new Object[]{"Alice", "Bob", "!"};
        result = fmt.format(args);
        assertEquals("Hello, Alice, Bob!", result);

        pattern = "Hello, {0}, {1}{2}!";
        fmt = new ExtendedMessageFormat(pattern);
        args = new Object[]{"Alice", null, "!"};
        result = fmt.format(args);
        assertNull(result);

        pattern = "Hello, {0}, {1}{2}!";
        fmt = new ExtendedMessageFormat(pattern);
        args = new Object[]{"Alice"};
        result = fmt.format(args);
        assertEquals("Hello, Alice, !", result);

        pattern = "Hello, {1}, {0}!";
        fmt = new ExtendedMessageFormat(pattern);
        args = new Object[]{"Bob", "Alice"};
        result = fmt.format(args);
        assertEquals("Hello, Bob, Alice!", result);

        pattern = "Hello, {0}, {1}! {2}";
        fmt = new ExtendedMessageFormat(pattern);
        args = new Object[]{"Alice", "Bob", "Charlie"};
        result = fmt.format(args);
        assertEquals("Hello, Alice, Bob! Charlie", result);

        pattern = "Hello, {0}, {1}! {2}{3}";
        fmt = new ExtendedMessageFormat(pattern);
        args = new Object[]{"Alice", "Bob", null, "Charlie"};
        result = fmt.format(args);
        assertEquals("Hello, Alice, Bob! Charlie", result);

        pattern = "Hello, {0}, {1}! {2}{3}";
        fmt = new ExtendedMessageFormat(pattern);
        args = new Object[]{"Alice", null, "Charlie", null};
        result = fmt.format(args);
        assertEquals("Hello, Alice, ! Charlie", result);

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        pattern = "Hello, {0}, born on {1}.";
        fmt = new ExtendedMessageFormat(pattern, registry);
        args = new Object[]{"Alice", new Date()};
        result = fmt.format(args);
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormatByArgumentIndex(1, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Format[] formats = {new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy-MM-dd")};
        fmt.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Format[] formats = {new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy-MM-dd")};
        fmt.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello, {0}, {1}!";
        String pattern2 = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(pattern2);
        assertTrue(fmt1.equals(fmt2));

        String pattern3 = "Hello, {1}, {0}!";
        ExtendedMessageFormat fmt3 = new ExtendedMessageFormat(pattern3);
        assertFalse(fmt1.equals(fmt3));

        String pattern4 = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt4 = new ExtendedMessageFormat(pattern4, Locale.US);
        assertTrue(fmt1.equals(fmt4));

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        ExtendedMessageFormat fmt5 = new ExtendedMessageFormat(pattern1, registry);
        assertTrue(fmt1.equals(fmt5));
    }

    @Test
    public void testHashCode() {
        String pattern1 = "Hello, {0}, {1}!";
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(pattern1);
        String pattern2 = "Hello, {1}, {0}!";
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(pattern2);
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        ExtendedMessageFormat fmt3 = new ExtendedMessageFormat(pattern1, registry);

        int hashCode1 = fmt1.hashCode();
        int hashCode2 = fmt2.hashCode();
        int hashCode3 = fmt3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }
}