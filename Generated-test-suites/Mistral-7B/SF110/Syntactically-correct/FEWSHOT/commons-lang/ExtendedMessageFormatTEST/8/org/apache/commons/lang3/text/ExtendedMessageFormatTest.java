package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, messageFormat.toPattern());
        assertNull(messageFormat.registry);

        pattern = "Hello {0}, your age is {1}, {2} is a number";
        messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, messageFormat.toPattern());
        assertNull(messageFormat.registry);

        pattern = "Hello {0}, your age is {1}, {2} is {3}";
        messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, messageFormat.toPattern());
        assertNull(messageFormat.registry);
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        String pattern = "Hello {0}, your age is {1}, {2} is {numberFormat number '0.0'}";
        MessageFormatFactory numberFormatFactory = new MessageFormat("0.0", Locale.getDefault());
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("numberFormat", numberFormatFactory);
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, messageFormat.toPattern());
        assertEquals(numberFormatFactory, messageFormat.registry.get("numberFormat"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormat(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormatByArgumentIndex(1, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormats(new MessageFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormatsByArgumentIndex(new MessageFormat[0]);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1);
        String pattern2 = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2);
        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern1);
        assertTrue(messageFormat1.equals(messageFormat1));
        assertTrue(messageFormat1.equals(messageFormat3));
        assertTrue(messageFormat1.equals(messageFormat2));
        assertFalse(messageFormat1.equals(null));
        assertFalse(messageFormat1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        String pattern1 = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1);
        String pattern2 = "Hello {0}, your age is {1}";
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2);
        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern1);
        int hashCode1 = messageFormat1.hashCode();
        int hashCode2 = messageFormat2.hashCode();
        int hashCode3 = messageFormat3.hashCode();
        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }
}