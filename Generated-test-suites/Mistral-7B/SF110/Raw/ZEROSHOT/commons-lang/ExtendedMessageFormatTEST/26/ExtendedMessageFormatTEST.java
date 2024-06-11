package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertEquals("", fmt.toPattern());
    }

    @org.junit.Test
    public void testDefaultConstructorWithPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        assertEquals("{0}", fmt.toPattern());
    }

    @org.junit.Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        assertEquals("{0}", fmt.toPattern());
    }

    @org.junit.Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry);
        assertEquals("{1, date}", fmt.toPattern());
    }

    @org.junit.Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date},{0}", registry);
        assertEquals("{1, date},{0}", fmt.toPattern());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        fmt.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        fmt.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        Format[] formats = new Format[1];
        formats[0] = new SimpleDateFormat("dd-MM-yyyy");
        fmt.setFormats(formats);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        Format[] formats = new Format[1];
        formats[0] = new SimpleDateFormat("dd-MM-yyyy");
        fmt.setFormatsByArgumentIndex(formats);
    }

    @org.junit.Test
    public void testEquals() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}");
        assertTrue(fmt1.equals(fmt2));

        fmt1 = new ExtendedMessageFormat("{0}", Locale.US);
        fmt2 = new ExtendedMessageFormat("{0}", Locale.UK);
        assertFalse(fmt1.equals(fmt2));

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        fmt1 = new ExtendedMessageFormat("{1, date}", registry);
        fmt2 = new ExtendedMessageFormat("{1, date}", registry);
        assertTrue(fmt1.equals(fmt2));
    }

    @org.junit.Test
    public void testHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}");
        assertEquals(fmt1.hashCode(), fmt2.hashCode());

        fmt1 = new ExtendedMessageFormat("{0}", Locale.US);
        fmt2 = new ExtendedMessageFormat("{0}", Locale.UK);
        assertNotEquals(fmt1.hashCode(), fmt2.hashCode());

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        fmt1 = new ExtendedMessageFormat("{1, date}", registry);
        fmt2 = new ExtendedMessageFormat("{1, date}", registry);
        assertEquals(fmt1.hashCode(), fmt2.hashCode());
    }

    @org.junit.Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1, date}", registry);
        Format dateFormat = fmt.getFormat("date");
        assertEquals("dd-MM-yyyy", dateFormat.toPattern());
        assertEquals(Locale.getDefault(), dateFormat.getLocale());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithNullName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", registry);
        fmt.getFormat(null);
    }

    @org.junit.Test
    public void testReadArgumentIndex() {
        String pattern = "{0, number, integer, 10}";
        ParsePosition pos = new ParsePosition(0);
        int index = ExtendedMessageFormat.readArgumentIndex(pattern, pos);
        assertEquals(10, index);
        assertEquals(16, pos.getIndex());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexInvalidFormat() {
        String pattern = "{0, invalidFormat}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat.readArgumentIndex(pattern, pos);
    }

    @org.junit.Test
    public void testParseFormatDescription() {
        String pattern = "{1, date, 'dd-MM-yyyy'}";
        ParsePosition pos = new ParsePosition(0);
        String description = ExtendedMessageFormat.parseFormatDescription(pattern, pos);
        assertEquals("date", description);
        assertEquals(7, pos.getIndex());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionInvalidFormat() {
        String pattern = "{1, invalidFormat, 'dd-MM-yyyy'}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat.parseFormatDescription(pattern, pos);
    }

    @org.junit.Test
    public void testInsertFormats() {
        String pattern = "{0, number, integer, 10},{1, date, 'dd-MM-yyyy'}";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{2, number, integer, 5}");
        String customPatternInserted = ExtendedMessageFormat.insertFormats(pattern, customPatterns);
        assertEquals("{0, number, integer, 10},{1, date, 'dd-MM-yyyy'}{2, number, integer, 5}", customPatternInserted);
    }
}