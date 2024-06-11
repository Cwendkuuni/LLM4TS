package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.Format;
import java.text.ParseException;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    

    @org.junit.Test
    public void testConstructorWithDefaultPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNotNull(format);
    }

    @org.junit.Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("{0}, {1}", format.toPattern());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternWithMissingFormat() {
        new ExtendedMessageFormat("{0}");
    }

    @org.junit.Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("{0}, {1}", format.toPattern());
    }

    @org.junit.Test
    public void testApplyPattern() {
        String pattern = "{0}, {1}, {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("dd/MM/yyyy"));
        }});

        Object[] args = {"Hello", new Date(), "World"};
        String formattedMessage = format.format(args);
        assertEquals("Hello, 29/12/2022, World", formattedMessage);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        format.applyPattern("{0}, {invalid}");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMissingFormatFactory() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {date}", Locale.US);
        format.applyPattern("{1}, {date}");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {date}", Locale.US);
        format.applyPattern("{0}, {date}, {date}");
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        format.setFormat(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        format.setFormatByArgumentIndex(1, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        format.setFormats(new Format[]{new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd/MM/yyyy")});
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        format.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd/MM/yyyy")});
    }

    @org.junit.Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}, {2}", Locale.US);

        assertEquals(format1, format1);
        assertEquals(format1, format2);
        assertNotEquals(format1, format3);
    }

    @org.junit.Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}, {2}", Locale.US);

        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        int hashCode3 = format3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @org.junit.Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {date}", Locale.US);
        Format format1 = format.getFormat("date");
        assertTrue(format1 instanceof SimpleDateFormat);
    }

    @org.junit.Test
    public void testReadArgumentIndex() throws ParseException {
        String pattern = "{0}, {1}, {2}";
        ParsePosition pos = new ParsePosition(0);

        int index1 = formatPattern(pattern, pos, 0, "0");
        int index2 = formatPattern(pattern, pos, 1, "1");
        int index3 = formatPattern(pattern, pos, 2, "2");

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
    }

    @org.junit.Test
    public void testParseFormatDescription() throws ParseException {
        String pattern = "{0}, {date}";
        ParsePosition pos = new ParsePosition(0);

        String description1 = parseFormatDescription(pattern, pos, "date");
        String description2 = parseFormatDescription(pattern, pos, "dateFormat");

        assertEquals("date", description1);
        assertEquals(null, description2);
    }

    @org.junit.Test
    public void testInsertFormats() {
        String pattern = "{0}, {1}{2}, {3}";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{date1}");
        customPatterns.add("{date2}");

        String formattedPattern = formatPattern(pattern, new StringBuilder(), customPatterns, true);
        assertEquals("{0}, {1}{2}, {date1}{3}", formattedPattern.toString());
    }

    private int formatPattern(String pattern, ParsePosition pos, int index, String formatDescription) {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        format.applyPattern("{0}, {1}, {2}");

        try {
            return format.readArgumentIndex(pattern, pos);
        } catch (IllegalArgumentException e) {
            int start = pos.getIndex();
            assertEquals(formatDescription, pattern.substring(start, pos.getIndex()));
            pos.setIndex(start + formatDescription.length());
            return index;
        }
    }

    private String parseFormatDescription(String pattern, ParsePosition pos, String formatDescription) {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        format.applyPattern("{0}, {1}, {2}");

        try {
            return format.parseFormatDescription(pattern, pos);
        } catch (IllegalArgumentException e) {
            throw new AssertionError(e);
        }
    }

    
}