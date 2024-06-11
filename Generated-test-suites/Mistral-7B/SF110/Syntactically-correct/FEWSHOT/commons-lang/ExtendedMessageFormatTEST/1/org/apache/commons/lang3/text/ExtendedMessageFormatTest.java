package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.cli.ParsePosition;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructorWithPattern() {
        String pattern = "Hello {0}, your age is {1}, and you have {2} friends.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        String pattern = "Hello {0}, your age is {1}, and you have {2} friends.";
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertEquals("", format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
        String pattern = "Hello {0}, your age is {1}, and you have {2} friends.";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        assertEquals("", format.toPattern());
        assertEquals(registry, format.registry);
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocaleAndRegistry() {
        String pattern = "Hello {0}, your age is {1}, and you have {2} friends.";
        Locale locale = Locale.US;
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(locale, format.getLocale());
        assertEquals(registry, format.registry);
    }

    @Test
    public void testToPattern() {
        String pattern = "Hello {0}, your age is {1}, and you have {2} friends.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1}, and you have {2} friends.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String expectedPattern = "Hello {0}, your age is {1}, and you have {2} friends";
        format.applyPattern(pattern);
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "Hello {0}, your {1} is {2}, and you have {3} friends.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new DecimalFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        String expectedPattern = "Hello {0}, your {1} is {2,number,currency}, and you have {3} friends";
        format.applyPattern(pattern);
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format newFormat = new SimpleDateFormat("dd-MM-yyyy");
        format.setFormat(0, newFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format newFormat = new SimpleDateFormat("dd-MM-yyyy");
        format.setFormatByArgumentIndex(1, newFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] newFormats = {new SimpleDateFormat("dd-MM-yyyy"), new DecimalFormat()};
        format.setFormats(newFormats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] newFormats = {new SimpleDateFormat("dd-MM-yyyy"), new DecimalFormat()};
        format.setFormatsByArgumentIndex(newFormats);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your age is {1}.";
        String pattern2 = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2);
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        String pattern1 = "Hello {0}, your age is {1}.";
        String pattern2 = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2);
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithNullDesc() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.getFormat(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithEmptyDesc() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.getFormat("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithInvalidDesc() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.getFormat("invalidFormat");
    }

    @Test
    public void testGetFormatWithValidDesc() {
        String pattern = "Hello {0}, your {1,number,currency} is {2}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new DecimalFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        Format result = format.getFormat("currency");
        assertNotNull(result);
        assertTrue(result instanceof DecimalFormat);
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "Hello {0}, your {1,number,integer} is {2}.";
        ParsePosition pos = new ParsePosition(0);
        int index = format.readArgumentIndex(pattern, pos);
        assertEquals(0, index);

        pos.setIndex(5);
        index = format.readArgumentIndex(pattern, pos);
        assertEquals(1, index);

        pos.setIndex(13);
        index = format.readArgumentIndex(pattern, pos);
        assertEquals(2, index);

        pos.setIndex(21);
        index = format.readArgumentIndex(pattern, pos);
        assertEquals(-1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidPattern() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithEmptyDescription() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidDepth() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.parseFormatDescription(pattern, pos);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        String description = format.parseFormatDescription(pattern, pos).toString();
        assertFalse(description.matches("(.*){3}(.*)"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithUnterminatedString() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.getQuotedString(pattern, pos, false);
        format.next(pos);
        String description = format.parseFormatDescription(pattern, pos).toString();
        assertFalse(description.matches("(.*){3}(.*)"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithUnterminatedFormat() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        String description = format.parseFormatDescription(pattern, pos).toString();
        assertFalse(description.matches("(.*){3}(.*)"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidEscapeSequence() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.parseFormatDescription(pattern, pos);
        format.next(pos);
        format.getQuotedString(pattern, pos, true);
        String description = format.parseFormatDescription(pattern, pos).toString();
        assertFalse(description.matches("(.*){3}(.*)"));
    }
}