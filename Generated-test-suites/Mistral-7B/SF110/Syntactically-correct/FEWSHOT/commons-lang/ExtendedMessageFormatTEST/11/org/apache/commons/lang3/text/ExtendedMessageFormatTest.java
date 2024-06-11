package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.text.*;
import java.util.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstruction() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, messageFormat.toPattern());
        assertNull(messageFormat.registry);

        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
        assertEquals(pattern, messageFormat.toPattern());
        assertNull(messageFormat.registry);

        messageFormat = new ExtendedMessageFormat(pattern, null);
        assertEquals(pattern, messageFormat.toPattern());
        assertNotNull(messageFormat.registry);

        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        assertEquals(pattern, messageFormat.toPattern());
        assertNotNull(messageFormat.registry);
    }

    @Test
    public void testExtendedMessageFormatApplyPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);

        String[] args = {"John", "25"};
        Object[] objects = {args[0], Integer.valueOf(args[1])};
        MessageFormat formatter = messageFormat.getFormat();
        assertEquals(pattern, formatter.toPattern());

        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello John, your age is 25.", formattedMessage);

        formatter.applyPattern("Hello {1}, your age is {0}.");
        messageFormat.applyPattern(pattern);
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());

        messageFormat.applyPattern("Hello {1}, {0} is your age.");
        assertEquals("Hello {1}, {0} is your age.", messageFormat.toPattern());
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

        messageFormat.setFormatByArgumentIndex(1, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);

        messageFormat.setFormats(new Format[]{new SimpleDateFormat("yyyy-MM-dd")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);

        messageFormat.setFormatsByArgumentIndex(1, new Format[]{new SimpleDateFormat("yyyy-MM-dd")});
    }

    @Test
    public void testGetFormat() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);

        Format format = messageFormat.getFormat();
        assertNull(format);

        messageFormat.applyPattern("Hello {1}, {0} is your age.");
        format = messageFormat.getFormat();
        MessageFormat msgFormat = (MessageFormat)format;
        assertEquals("Hello {1}, {0} is your age.", msgFormat.toPattern());
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your age is {1}.";
        String pattern2 = "Hello {1}, {0} is your age.";

        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2, Locale.getDefault(), null);

        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        assertTrue(messageFormat1.equals(messageFormat3));

        assertFalse(messageFormat1.equals(messageFormat2));
        assertFalse(messageFormat1.equals(null));
        assertFalse(messageFormat1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        String pattern1 = "Hello {0}, your age is {1}.";
        String pattern2 = "Hello {1}, {0} is your age.";

        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), null);
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2, Locale.getDefault(), null);

        int hashCode1 = messageFormat1.hashCode();
        int hashCode2 = messageFormat2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}