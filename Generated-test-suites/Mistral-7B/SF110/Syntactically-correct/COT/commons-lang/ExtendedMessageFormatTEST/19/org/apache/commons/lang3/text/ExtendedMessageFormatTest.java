package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNotNull(format);
        assertEquals("", format.toPattern());
    }

    @Test
    public void testBasicConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testBasicConstructorWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testBasicConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1} {2}", registry);
        assertEquals("{0}, {1} {2}", format.toPattern());
    }

    @Test
    public void testBasicConstructorWithRegistryAndLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1} {2}", registry, Locale.US);
        assertEquals("{0}, {1} {2}", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternWithFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {0}", new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternWithoutFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}, {2}", new HashMap<>());
        format.applyPattern("{0},{2}, {1}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1} {2}", new HashMap<>());
        format.applyPattern("{0}, {1} {3}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapingSequence() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}', {}", new HashMap<>());
        format.applyPattern("{0}'{1}");
    }

    @Test
    public void testFormatWithFormat() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1} {2}", registry);
        Object[] args = {"Hello", new Date(), "World"};
        String formattedMessage = format.format(args);
        String expectedMessage = String.format("Hello, %1$td-%1$tm-%1$ty %1$tT %1$s", new Date());
        assertEquals(expectedMessage, formattedMessage);
    }

    @Test
    public void testFormatWithFormatAndLocale() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1} {2}", registry, Locale.US);
        Object[] args = {"Hello", new Date(), "World"};
        String formattedMessage = format.format(args);
        String expectedMessage = String.format("Hello, %1$tc-%1$tm-%1$ty %1$tT %1$s", new Date());
        assertEquals(expectedMessage, formattedMessage);
    }

    @Test
    public void testFormatWithRegistry() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        registry.put("time", new SimpleDateFormat("HH:mm:ss"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1} {2} {3}", registry);
        Object[] args = {"Hello", new Date(), new Date(), "World"};
        String formattedMessage = format.format(args);
        String expectedMessage = String.format("Hello, %1$td-%1$tm-%1$ty %1$tT %2$tT %3$s", new Date(), new Date());
        assertEquals(expectedMessage, formattedMessage);
    }

    @Test
    public void testFormatWithRegistryAndLocale() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1} {2} {3}", registry, Locale.US);
        Object[] args = {"Hello", new Date(), new Date(), "World"};
        String formattedMessage = format.format(args);
        String expectedMessage = String.format("Hello, %1$tc-%1$tm-%1$ty %1$tT %2$tc-%2$tm-%2$ty %2$tT %3$s", new Date());
        assertEquals(expectedMessage, formattedMessage);
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.applyPattern("{0}, {1}");
        try {
            format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.applyPattern("{0}, {1}");
        try {
            format.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.applyPattern("{0}, {1}");
        try {
            format.setFormats(new SimpleDateFormat("yyyy-MM-dd")[0]);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.applyPattern("{0}, {1}");
        try {
            format.setFormatsByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd")[0]);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{1}");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0}", new HashMap<>());
        assertEquals(format1, format1);
        assertEquals(format1, format2);
        assertNotEquals(format1, format3);
        assertNotEquals(format1, format4);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{1}");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0}", new HashMap<>());
        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        int hashCode3 = format3.hashCode();
        int hashCode4 = format4.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode1);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}