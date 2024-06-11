package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

public class ExtendedMessageFormatTest {

    @Test
    public void testBasicUsage() {
        final String pattern = "Hello {0}, your age is {1}.";
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        final String result = messageFormat.format(new Object[]{"John", 25});
        assertEquals("Hello John, your age is 25.", result);
    }

    @Test
    public void testEmptyPattern() {
        final String pattern = "";
        try {
            new ExtendedMessageFormat(pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInvalidFormatElement() {
        final String pattern = "Hello {0, invalidFormat}.";
        try {
            new ExtendedMessageFormat(pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Unreadable format element at position ";
            assertTrue(e.getMessage().startsWith(expectedMessage));
        }
    }

    @Test
    public void testMissingFormatDescription() {
        final String pattern = "Hello {{0}}, {{1}}.";
        try {
            new ExtendedMessageFormat(pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Unterminated format element at position ";
            assertTrue(e.getMessage().startsWith(expectedMessage));
        }
    }

    @Test
    public void testQuotedString() {
        final String pattern = "Hello {0}, your name is '{1}'.";
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        final String result = messageFormat.format(new Object[]{"John", "Doe"});
        assertEquals("Hello John, your name is 'Doe'.", result);
    }

    @Test
    public void testCustomFormat() {
        final String pattern = "Hello {0}, your age is {format:ageFormat}.{1}.";
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, new HashMap<String, FormatFactory>() {{
            put("ageFormat", (formatName, args, locale) -> new SimpleDateFormat("yyyy"));
        }});
        final Object[] args = {"John", new Integer(25)};
        final String result = messageFormat.format(args);
        assertEquals("Hello John, your age is 1995.", result);
    }

    @Test
    public void testMultipleArguments() {
        final String pattern = "Hello {0}, your age is {1}, and you are {2} years old.";
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        final Object[] args = {"John", 25, "a young man"};
        final String result = messageFormat.format(args);
        assertEquals("Hello John, your age is 25, and you are a young man years old.", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormats(new Format[]{new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy")});
    }

    @Test(expected = ParseException.class)
    public void testInvalidPattern() {
        final String pattern = "Hello {0, invalidPattern}.";
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.format(new Object[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testFormatAfterInvalidPattern() {
        final String pattern = "Hello {0, invalidPattern}.";
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.format(new Object[]{"John"});
    }
}