package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNotNull(format);
        assertEquals("", format.toPattern());
        assertNull(format.getRegistry());
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertNotNull(format);
        assertEquals("{0}, {1}", format.toPattern());
        assertNull(format.getRegistry());
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.ENGLISH);
        assertNotNull(format);
        assertEquals("{0}, {1}", format.toPattern());
        assertEquals(Locale.ENGLISH, format.getLocale());
        assertNull(format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=date}, {1}", registry);
        assertNotNull(format);
        assertEquals("{0, format=date}, {1}", format.toPattern());
        assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=date}, {1}", Locale.ENGLISH, registry);
        assertNotNull(format);
        assertEquals("{0, format=date}, {1}", format.toPattern());
        assertEquals(Locale.ENGLISH, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", format.toPattern());

        format = new ExtendedMessageFormat("{0, format=date}, {1}", new HashMap<>());
        assertEquals("{0, format=date}, {1}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormat(0, new MessageFormat("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormatByArgumentIndex(0, new MessageFormat("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormats(new MessageFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormatsByArgumentIndex(new MessageFormat[0]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}");
        assertTrue(format1.equals(format2));

        FormatFactory registry = new DateFormatFactory();
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0, format=date}, {1}", registry);
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0, format=date}, {1}", Collections.singletonMap("date", registry));
        assertTrue(format3.equals(format4));

        format1.setRegistry(registry);
        assertTrue(format1.equals(format3));
        assertTrue(format1.equals(format4));

        format1.setLocale(Locale.ENGLISH);
        format2.setLocale(Locale.ENGLISH);
        assertTrue(format1.equals(format2));

        format1.setLocale(Locale.US);
        assertFalse(format1.equals(format2));

        format1.setPattern("{1}, {0}");
        assertFalse(format1.equals(format2));
    }

    @Test
    public void testHashcode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}");
        assertEquals(format1.hashCode(), format2.hashCode());

        FormatFactory registry = new DateFormatFactory();
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0, format=date}, {1}", registry);
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0, format=date}, {1}", Collections.singletonMap("date", registry));
        assertEquals(format3.hashCode(), format4.hashCode());

        format1.setRegistry(registry);
        assertEquals(format1.hashCode(), format3.hashCode());
        assertEquals(format1.hashCode(), format4.hashCode());

        format1.setLocale(Locale.ENGLISH);
        format2.setLocale(Locale.ENGLISH);
        assertEquals(format1.hashCode(), format2.hashCode());

        format1.setLocale(Locale.US);
        int expectedHashcodeDifference = ObjectUtils.IDENTITY_HASH_CODE * 2;
        assertEquals(format1.hashCode(), format2.hashCode() + expectedHashcodeDifference);

        format1.setPattern("{1}, {0}");
        int expectedHashcodeDifferencePattern = "{1}, {0}".hashCode() * 2;
        assertEquals(format1.hashCode(), format2.hashCode() + expectedHashcodeDifference + expectedHashcodeDifferencePattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.applyPattern("{3}, {2}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", registry);
        format.applyPattern("{2}, {0}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", registry);
        format.applyPattern("{format=date}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnreadableFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", registry);
        format.applyPattern("{0, format=date, unreadable}");
    }

    @Test
    public void testFormatApplication() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=date}, {1}", registry);
        format.applyPattern("{0}, {1}");
        String formatString = format.format(new Object[]{"Hello", new Date()});
        assertEquals("Hello, Mon Dec 27 13:52:46 CET 2021", formatString);
    }

    @Test
    public void testFormatApplicationWithRegistry() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=date}, {1}", registry);
        format.applyPattern("{0}, {1}");
        String formatString = format.format(new Object[]{"Hello", new Date()}, new Object[]{Locale.US, null});
        assertEquals("Hello, Mon Dec 27 09:52:46 CST 2021", formatString);
    }

    @Test
    public void testFormatApplicationWithCustomPattern() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", registry);
        format.applyPattern("{0},{1, format=date}");
        String formatString = format.format(new Object[]{"Hello", new Date()});
        assertEquals("Hello, Mon Dec 27 13:52:46 CET 2021, Mon Dec 27 13:52:46 CET 2021", formatString);
    }

    @Test
    public void testFormatApplicationWithCustomPatternAndLocale() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1, format=date}", registry);
        format.applyPattern("{0},{1, format=date}");
        String formatString = format.format(new Object[]{"Hello", new Date()}, new Object[]{Locale.US, null});
        assertEquals("Hello, Mon Dec 27 09:52:46 CST 2021, Mon Dec 27 09:52:46 CST 2021", formatString);
    }
}