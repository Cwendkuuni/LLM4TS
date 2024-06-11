package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.MessageFormatFactory;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        // Test default constructor
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.toPattern());
        assertNotNull(format.getLocale());
        assertEquals(Locale.getDefault(), format.getLocale());

        // Test constructors with pattern
        String pattern = "Hello {0}, your age is {1,number,integer}.";
        ExtendedMessageFormat formatWithPattern = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, formatWithPattern.toPattern());
        assertEquals(Locale.getDefault(), formatWithPattern.getLocale());

        // Test constructors with pattern and locale
        ExtendedMessageFormat formatWithPatternAndLocale = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals(pattern, formatWithPatternAndLocale.toPattern());
        assertEquals(Locale.US, formatWithPatternAndLocale.getLocale());

        // Test constructor with pattern and registry
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
        ExtendedMessageFormat formatWithPatternAndRegistry = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, formatWithPatternAndRegistry.toPattern());
        assertEquals(Locale.getDefault(), formatWithPatternAndRegistry.getLocale());
        assertEquals("date", formatWithPatternAndRegistry.getFormat("date").getClass().getSimpleName());

        // Test constructors with pattern, locale, and registry
        ExtendedMessageFormat formatWithPatternAndLocaleAndRegistry = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals(pattern, formatWithPatternAndLocaleAndRegistry.toPattern());
        assertEquals(Locale.US, formatWithPatternAndLocaleAndRegistry.getLocale());
        assertEquals("date", formatWithPatternAndLocaleAndRegistry.getFormat("date").getClass().getSimpleName());
    }

    @Test
    public void testApplyPatternAndInsertFormats() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        // Test empty format list
        format.applyPattern(pattern);
        assertEquals(pattern, format.toPattern());

        // Test inserting single format
        format.applyPattern("Hello {0}, your {1} is {2,date,dd-MM-yyyy}.");
        assertEquals("Hello {0}, your {1} is {2,date,dd-MM-yyyy}", format.toPattern());

        // Test inserting multiple formats
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        registry.put("currency", new DecimalFormat("#,##0.00"));
        format.applyPattern("Hello {0}, your {1,date,dd-MM-yyyy} is worth {2,currency}.");
        assertEquals("Hello {0}, your {1,date,dd-MM-yyyy} is worth {2,currency}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormats(new MessageFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatsByArgumentIndex(new MessageFormat[0]);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your {1} is {2}.";
        String pattern2 = "Hello {0}, your {1,number,integer} is {2}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2);
        assertFalse(format1.equals(format2));

        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern1);
        assertTrue(format1.equals(format3));

        // Test with different locale
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(pattern1, Locale.US);
        assertFalse(format1.equals(format4));

        // Test with same locale and different registry
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat format5 = new ExtendedMessageFormat(pattern1, registry);
        assertFalse(format1.equals(format5));

        // Test with same locale and registry
        Map<String, MessageFormatFactory> registry2 = new HashMap<>();
        registry2.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat format6 = new ExtendedMessageFormat(pattern1, registry2);
        assertTrue(format1.equals(format6));

        // Test with same pattern and locale and registry
        ExtendedMessageFormat format7 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), registry);
        assertTrue(format1.equals(format7));
    }

    @Test
    public void testHashCode() {
        String pattern1 = "Hello {0}, your {1} is {2}.";
        String pattern2 = "Hello {0}, your {1,number,integer} is {2}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2);
        assertNotEquals(format1.hashCode(), format2.hashCode());

        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern1);
        assertEquals(format1.hashCode(), format3.hashCode());

        // Test with different locale
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(pattern1, Locale.US);
        assertNotEquals(format1.hashCode(), format4.hashCode());

        // Test with same locale and different registry
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat format5 = new ExtendedMessageFormat(pattern1, registry);
        assertNotEquals(format1.hashCode(), format5.hashCode());

        // Test with same pattern, locale, and registry
        Map<String, MessageFormatFactory> registry2 = new HashMap<>();
        registry2.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat format6 = new ExtendedMessageFormat(pattern1, Locale.getDefault(), registry2);
        assertEquals(format1.hashCode(), format6.hashCode());
    }
}