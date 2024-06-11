package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        assertEquals("", messageFormat.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPattern() {
        new ExtendedMessageFormat("InvalidPattern");
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!", Locale.US);
        assertEquals("{0} Hello, {1}!", messageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new SimpleDateFormat(args);
            }
        };
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0, date} {1, date}", new HashMap<String, FormatFactory>() {{
            put("date", formatFactory);
        }}, Locale.US);
        assertEquals("{0, date} {1, date}", messageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        messageFormat.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        messageFormat.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        messageFormat.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        messageFormat.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("{0} Hello, {1}!");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("{0} Hello, {1}!");
        assertTrue(messageFormat1.equals(messageFormat2));
        assertFalse(messageFormat1.equals("Not a message format"));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("{0} Hello, {1}!");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("{0} Hello, {1}!");
        assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
        assertNotSame(messageFormat1.hashCode(), new Object().hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithInvalidDescription() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        messageFormat.getFormat("InvalidDescription");
    }

    @Test
    public void testGetFormatWithValidDescription() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new SimpleDateFormat(args);
            }
        };
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0, date} {1, date}", new HashMap<String, FormatFactory>() {{
            put("date", formatFactory);
        }}, Locale.US);
        Format format = messageFormat.getFormat("date");
        assertNotNull(format);
    }

    @Test
    public void testReadArgumentIndexWithValidIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        int index = messageFormat.readArgumentIndex(messageFormat.toPattern(), messageFormat.new ParsePosition(0));
        assertEquals(0, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithInvalidIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        messageFormat.new ParsePosition(1).setIndex(100);
        try {
            messageFormat.readArgumentIndex(messageFormat.toPattern(), messageFormat.new ParsePosition(0));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseFormatDescriptionWithValidPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0, date, time} {1}");
        String description = messageFormat.parseFormatDescription(messageFormat.toPattern(), messageFormat.new ParsePosition(0));
        assertEquals("date, time", description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        messageFormat.new ParsePosition(1).setIndex(100);
        try {
            messageFormat.parseFormatDescription(messageFormat.toPattern(), messageFormat.new ParsePosition(0));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInsertFormatsWithEmptyCustomPatterns() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        String pattern = messageFormat.insertFormats(messageFormat.toPattern(), new ArrayList<>());
        assertEquals("{0} Hello, {1}", pattern);
    }

    @Test
    public void testInsertFormatsWithCustomPatterns() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} {1, number, currency}");
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("date");
        customPatterns.add("number, currency");
        String pattern = messageFormat.insertFormats(messageFormat.toPattern(), customPatterns);
        assertEquals("{0} {1, date} {2, number, currency}", pattern);
    }

    @Test(expected = ParseException.class)
    public void testFormatWithInvalidPatternAndArgument() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {2}!");
        Object[] arguments = {"John", "Doe"};
        messageFormat.format(arguments);
        fail("ParseException expected");
    }

    @Test
    public void testFormatWithValidPatternAndArguments() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} Hello, {1}!");
        Object[] arguments = {"John", "Doe"};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("John Hello, Doe", formattedMessage);
    }

}