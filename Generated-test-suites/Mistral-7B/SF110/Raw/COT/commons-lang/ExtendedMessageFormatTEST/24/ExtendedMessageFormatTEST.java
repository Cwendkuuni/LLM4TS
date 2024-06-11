package org.junit.test;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    

    @org.junit.Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertNull(format.getRegistry());
    }

    @org.junit.Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        assertEquals("", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertNull(format.getRegistry());
    }

    @org.junit.Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {date}!", registry);
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @org.junit.Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {date}!", Locale.US, registry);
        assertEquals("", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @org.junit.Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, {0}!", format.toPattern());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatWithFormatElementIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormats(new Format[0]);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatsByArgumentIndex(new Format[0]);
    }

    @org.junit.Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello, {1}!");
        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(format3));
    }

    @org.junit.Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello, {1}!");
        assertEquals(format1.hashCode(), format1.hashCode());
        assertEquals(format1.hashCode(), format2.hashCode());
        assertNotEquals(format1.hashCode(), format3.hashCode());
    }

    @org.junit.Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {date}!", null, registry);
        Format dateFormat = format.getFormat("date");
        assertEquals("dd-MM-yyyy", dateFormat.toPattern());
        assertEquals(Locale.getDefault(), dateFormat.getLocale());
        assertNotNull(dateFormat);
        Format unknownFormat = format.getFormat("unknown");
        assertNull(unknownFormat);
    }

    @org.junit.Test
    public void testReadArgumentIndex() {
        String pattern = "Hello, {0}, {1}!";
        ParsePosition pos = new ParsePosition(0);
        int index1 = format.readArgumentIndex(pattern, pos);
        int index2 = format.readArgumentIndex(pattern, pos);
        assertEquals(0, index1);
        assertEquals(1, index2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionInvalidIndex() {
        String pattern = "Hello, {0}, {1}!";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionInvalidCustomPattern() {
        String pattern = "Hello, {{date}}!";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
    }

    @org.junit.Test
    public void testInsertFormats() {
        String pattern = "Hello, {0}, {1}!";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("dd-MM-yyyy");
        String formattedPattern = format.insertFormats(pattern, customPatterns);
        assertEquals("Hello, {0}, {1}!", formattedPattern);

        pattern = "Hello, {0}, {1}{2}!";
        customPatterns.clear();
        customPatterns.add("dd-MM-yyyy");
        customPatterns.add("HH:mm:ss");
        formattedPattern = format.insertFormats(pattern, customPatterns);
        assertEquals("Hello, {0}, {1}{2}!", formattedPattern);

        pattern = "Hello, {{date}}{{time}}, {{date}}{{time}}!";
        customPatterns.clear();
        customPatterns.add("dd-MM-yyyy");
        customPatterns.add("HH:mm:ss");
        formattedPattern = format.insertFormats(pattern, customPatterns);
        assertEquals("Hello, {{date}}{1}{{time}}{2}, {{date}}{1}{{time}}{2}!", formattedPattern);
    }

    
}