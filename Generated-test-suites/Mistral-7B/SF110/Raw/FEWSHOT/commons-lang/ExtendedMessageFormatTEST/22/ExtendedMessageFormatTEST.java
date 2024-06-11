package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        String pattern = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
        assertEquals(pattern, messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat(pattern, null);
        assertEquals(pattern, messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat(pattern, new HashMap<>());
        assertEquals(pattern, messageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "Hello {0}, your age is {1, date, short}, and you live at {2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, new HashMap<>());
        messageFormat.applyPattern(pattern);

        Format format = messageFormat.getFormats()[0];
        assertEquals("date", format.getClass().getSimpleName());
        assertEquals("short", format.getFormatName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        String pattern = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormat(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        String pattern = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormatByArgumentIndex(1, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        String pattern = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormats(new MessageFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        String pattern = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        messageFormat.setFormatsByArgumentIndex(new MessageFormat[0]);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1);
        String pattern2 = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2);

        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern1);
        assertTrue(messageFormat1.equals(messageFormat3));

        ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat(pattern2);
        assertFalse(messageFormat1.equals(messageFormat4));

        ExtendedMessageFormat messageFormat5 = new Object() {};
        assertFalse(messageFormat1.equals(messageFormat5));

        assertTrue(messageFormat1.equals(messageFormat1));
    }

    @Test
    public void testHashCode() {
        String pattern = "Hello {0}, your age is {1}, and you live at {2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);

        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern);
        assertEquals(messageFormat.hashCode(), messageFormat2.hashCode());

        String pattern2 = "Hello {0}, your age is {1, date, short}, and you live at {2}.";
        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern2);
        assertNotEquals(messageFormat.hashCode(), messageFormat3.hashCode());

        messageFormat3.setLocale(Locale.US);
        assertEquals(messageFormat.hashCode(), messageFormat3.hashCode());
    }
}