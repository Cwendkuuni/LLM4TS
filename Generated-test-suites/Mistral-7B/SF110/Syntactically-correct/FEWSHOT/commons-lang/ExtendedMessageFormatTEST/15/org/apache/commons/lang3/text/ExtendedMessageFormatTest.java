package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstanceWithNullPattern() {
        assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat(null));
    }

    @Test
    public void testNewInstanceWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
    }

    @Test
    public void testNewInstanceWithDefaultRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        assertEquals("Hello {0}, your age is {1}", format.toPattern());
    }

    @Test
    public void testNewInstanceWithCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, date}, your age is {1}.");
        assertEquals("Hello {0, yyyy-MM-dd}, your age is {1}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.applyPattern("");
        assertEquals("", format.toPattern());
    }

    @Test
    public void testApplyPatternWithSimplePattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        format.applyPattern("Hello {0}");
        assertEquals("Hello {0}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithCustomPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, date}, your age is {1}.");
        format.applyPattern("Hello {0, date}, your age is {1}");
        assertEquals("Hello {0, yyyy-MM-dd}, your age is {1}", format.toPattern());
    }

    @Test
    public void testGetFormatWithNullDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        assertNull(format.getFormat(null));
    }

    @Test
    public void testGetFormatWithEmptyDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        assertNull(format.getFormat(""));
    }

    @Test
    public void testGetFormatWithInvalidDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, date}, your age is {1}.");
        assertNull(format.getFormat("invalid"));
    }

    @Test
    public void testGetFormatWithValidDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, date}, your age is {1}.");
        Format dateFormat = format.getFormat("date");
        assertEquals(DateFormat.class, dateFormat.getClass());
        assertEquals("yyyy-MM-dd", dateFormat.toPattern());
    }

    @Test
    public void testHashCodeAndEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}.");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}.");

        assertEquals(format1, format1);
        assertEquals(format1.hashCode(), format1.hashCode());

        assertEquals(format1, format2);
        assertEquals(format1.hashCode(), format2.hashCode());

        format1.applyPattern("Hello {1}.");
        assertNotEquals(format1, format2);
        assertNotEquals(format1.hashCode(), format2.hashCode());

        format2.applyPattern("Hello {1}.");
        assertEquals(format1, format2);
        assertEquals(format1.hashCode(), format2.hashCode());
    }
}