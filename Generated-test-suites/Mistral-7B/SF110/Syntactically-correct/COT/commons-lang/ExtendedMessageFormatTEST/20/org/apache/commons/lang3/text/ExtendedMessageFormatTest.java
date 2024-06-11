package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.StrMatcher;

public class ExtendedMessageFormatTest {

    @SuppressWarnings("unchecked")
    private static FormatFactory newFormatFactory() {
        return (FormatFactory) () -> new SimpleDateFormat("");
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.toPattern());
        assertNull(format.registry);
    }

    @org.junit.Test
    public void testDefaultLocaleConstructor() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
        assertNull(format.registry);
    }

    @org.junit.Test
    public void testRegistryConstructor() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", newFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(registry, format.registry);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testRegistryConstructorNullPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        new ExtendedMessageFormat(null, registry);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testRegistryConstructorEmptyPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        new ExtendedMessageFormat("", registry);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testRegistryConstructorInvalidPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        new ExtendedMessageFormat("Hello, {0}!", registry);
    }

    @org.junit.Test
    public void testApplyPattern() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", newFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"John", new Date()};
        String result = format.format(args);
        String expected = "Hello, John, " + new SimpleDateFormat("").format(args[1]) + "!";
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPatternMissingArgumentIndex() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern("Hello, {2}, {1}!");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalidFormatDescription() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("invalid", newFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        try {
            format.applyPattern("Hello, {0}, {1}, {invalid}!");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format argument index at position 3: invalid", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPatternEmptyFormatDescription() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        try {
            format.applyPattern("Hello, {0}, {1}, {}!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unterminated format element at position 4", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPatternEmptyFormatRegistry() {
        String pattern = "Hello, {0}, {1}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Collections.emptyMap());
        try {
            format.applyPattern("Hello, {0}, {1}, {date}!");
        } catch (IllegalArgumentException e) {
            assertEquals("Format Factory for format 'date' not found", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPatternFormatFactoryNull() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", null);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        try {
            format.applyPattern("Hello, {0}, {1}, {date}!");
        } catch (IllegalArgumentException e) {
            assertEquals("Format Factory for format 'date' must not be null", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormat(0, new SimpleDateFormat(""));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormatByArgumentIndex(0, new SimpleDateFormat(""));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormats(new Object[] { new SimpleDateFormat(""), new SimpleDateFormat("") });
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormatsByArgumentIndex(new Object[] { new SimpleDateFormat(""), new SimpleDateFormat("") });
    }

    @org.junit.Test
    public void testEquals() {
        String pattern1 = "Hello, {0}, {1}!";
        String pattern2 = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1, registry);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2, registry);
        assertTrue(format1.equals(format2));
        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern1, null);
        assertFalse(format1.equals(format3));
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(pattern2, null);
        assertFalse(format1.equals(format4));
    }

    @org.junit.Test
    public void testHashCode() {
        String pattern1 = "Hello, {0}, {1}!";
        String pattern2 = "Hello, {0}, {1}!";
        Map<String, FormatFactory> registry1 = new HashMap<>();
        Map<String, FormatFactory> registry2 = new HashMap<>();
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1, registry1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2, registry2);
        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        assertEquals(hashCode1, hashCode2);
        registry1.put("date", newFormatFactory());
        int newHashCode1 = format1.hashCode();
        assertNotEquals(hashCode1, newHashCode1);
        assertEquals(newHashCode1, format1.hashCode());
    }

}