package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat(pattern, Map.of("date", new SimpleDateFormat("dd-MM-yyyy")));
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), Map.of("date", new SimpleDateFormat("dd-MM-yyyy")));
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1} {2}.");
        assertEquals("{0}, {1} {2}.", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat("{format:date 'dd-MM-yyyy'} {0} {format:date 'HH:mm'}");
        assertEquals("{format date='dd-MM-yyyy'} {0} {format date='HH:mm'}", messageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormats(new Format[]{new SimpleDateFormat("dd-MM-yyyy")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}.");
        messageFormat.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("dd-MM-yyyy")});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}.");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}.");
        assertTrue(messageFormat1.equals(messageFormat2));

        messageFormat2 = new ExtendedMessageFormat("Hello {1}.");
        assertFalse(messageFormat1.equals(messageFormat2));

        messageFormat2 = new ExtendedMessageFormat("Hello {0}.", Locale.US);
        assertFalse(messageFormat1.equals(messageFormat2));

        messageFormat2 = new ExtendedMessageFormat("Hello {0}.", Map.of("date", new SimpleDateFormat("dd-MM-yyyy")));
        assertFalse(messageFormat1.equals(messageFormat2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}.");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}.");
        assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

        messageFormat2 = new ExtendedMessageFormat("Hello {1}.");
        assertNotEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

        messageFormat2 = new ExtendedMessageFormat("Hello {0}.", Locale.US);
        assertNotEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

        messageFormat2 = new ExtendedMessageFormat("Hello {0}.", Map.of("date", new SimpleDateFormat("dd-MM-yyyy")));
        assertNotEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
    }
}