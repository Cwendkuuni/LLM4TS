package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructors() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals("", messageFormat.toPattern());
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
        assertEquals(pattern, messageFormat.toPattern());
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        assertEquals(pattern, messageFormat.toPattern());
        messageFormat = new ExtendedMessageFormat(pattern, null, Map.of("date", DateFormat.getInstance()));
        assertEquals(pattern, messageFormat.toPattern());
        messageFormat.applyPattern(pattern);
        assertEquals(pattern, messageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        messageFormat.applyPattern(pattern);
        assertEquals(pattern, messageFormat.toPattern());

        pattern = "Hello {{0}}, your age is {{1}}.";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        messageFormat.applyPattern(pattern);
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());

        pattern = "Hello {{0}}, your age is {{1}}.";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        messageFormat.applyPattern("Hello {{0}}, your age is {{2}}.");
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());

        pattern = "Hello {{0}}, your age is {{1}}.";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), Map.of("date", DateFormat.getInstance()));
        messageFormat.applyPattern("Hello {{0}}, your date of birth is {{date 'EEEE, MMM d, ''yyyy'}}.");
        assertEquals("Hello {0}, your date of birth is EEEE, MMM d, ''yyyy.'.", messageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        messageFormat.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        messageFormat.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        messageFormat.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your age is {1}.";
        String pattern2 = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2, Locale.getDefault(), null);
        assertTrue(messageFormat1.equals(messageFormat2));

        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        messageFormat3.applyPattern("Hello {{0}}, your age is {{1}}.");
        assertFalse(messageFormat1.equals(messageFormat3));

        ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        messageFormat4.setLocale(Locale.US);
        assertFalse(messageFormat1.equals(messageFormat4));
    }

    @Test
    public void testHashCode() {
        String pattern1 = "Hello {0}, your age is {1}.";
        String pattern2 = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2, Locale.getDefault(), null);
        assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        messageFormat3.applyPattern("Hello {{0}}, your age is {{1}}.");
        assertNotEquals(messageFormat1.hashCode(), messageFormat3.hashCode());

        ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        messageFormat4.setLocale(Locale.US);
        assertNotEquals(messageFormat1.hashCode(), messageFormat4.hashCode());
    }
}