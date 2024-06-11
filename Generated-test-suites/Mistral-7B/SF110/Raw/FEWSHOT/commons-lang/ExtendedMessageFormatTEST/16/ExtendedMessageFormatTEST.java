package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        String pattern = "Hello {0}, {1} {2} {3}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        String pattern = "Hello {0,date,dd.MM.yy}, {1}, {2,number,#}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory dateFormatFactory = new SimpleDateFormat("dd.MM.yy");
        registry.put("date", dateFormatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertTrue(format.registry.containsKey("date"));
        assertEquals(dateFormatFactory, format.registry.get("date"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        format.setFormat(0, new SimpleDateFormat("dd.MM.yy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd.MM.yy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        Format[] newFormats = {new SimpleDateFormat("dd.MM.yy")};
        format.setFormats(newFormats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        Format[] newFormats = {new SimpleDateFormat("dd.MM.yy")};
        format.setFormatsByArgumentIndex(newFormats);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}.";
        String pattern2 = "Hello {0}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2);
        assertTrue(format1.equals(format2));

        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern1, Locale.US);
        assertFalse(format1.equals(format3));

        String pattern3 = "Hello {1}.";
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(pattern3);
        assertFalse(format1.equals(format4));

        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new SimpleDateFormat("dd.MM.yy");
        registry.put("date", formatFactory);
        ExtendedMessageFormat format5 = new ExtendedMessageFormat(pattern1, registry);
        assertTrue(format1.equals(format5));
    }

    @Test
    public void testHashCode() {
        String pattern = "Hello {0}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern);
        assertEquals(format1.hashCode(), format2.hashCode());

        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new SimpleDateFormat("dd.MM.yy");
        registry.put("date", formatFactory);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern, registry);
        assertNotEquals(format1.hashCode(), format3.hashCode());
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new SimpleDateFormat("dd.MM.yy");
        registry.put("date", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,date}, {1}.");
        Format dateFormat = format.getFormat("date");
        assertEquals(formatFactory, dateFormat);
        assertNull(format.getFormat("nonExistent"));
    }
}