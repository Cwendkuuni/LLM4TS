package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.format.DateFormatFactory;
import org.apache.commons.lang3.text.format.NumberFormatFactory;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testExtendedMessageFormat() {
        // Test default constructor
        ExtendedMessageFormat defaultFormat = new ExtendedMessageFormat("");
        assertNull(defaultFormat.toPattern());
        assertNull(defaultFormat.registry);

        // Test constructor with pattern
        String pattern = "Hello {0}, your age is {1}, and today is {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
        assertNull(format.registry);

        // Test constructor with pattern and locale
        ExtendedMessageFormat formatLocale = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals(pattern, formatLocale.toPattern());
        assertNull(formatLocale.registry);

        // Test constructor with pattern, locale and registry
        Map<String, FormatFactory> registry = new Properties();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageFormat formatRegistry = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals(pattern, formatRegistry.toPattern());
        assertEquals(registry, formatRegistry.registry);

        // Test getFormat method
        Format dateFormat = formatRegistry.getFormat("date");
        assertNotNull(dateFormat);
        assertTrue(dateFormat instanceof DateFormat);

        Format numberFormat = formatRegistry.getFormat("number");
        assertNotNull(numberFormat);
        assertTrue(numberFormat instanceof NumberFormat);

        Format unknownFormat = formatRegistry.getFormat("unknown");
        assertNull(unknownFormat);

        // Test toPattern method
        String newPattern = "Hello {0} ({2}), your age is {1}, and today is {3}";
        formatRegistry.applyPattern(newPattern);
        assertEquals(newPattern, formatRegistry.toPattern());

        // Test setFormat method (Unsupported)
        try {
            formatRegistry.setFormat(0, new DateFormat());
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            formatRegistry.setFormatByArgumentIndex(1, new NumberFormat());
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            formatRegistry.setFormats(new Format[0]);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            formatRegistry.setFormatsByArgumentIndex(new Format[0]);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test equals method
        ExtendedMessageFormat formatEqual = new ExtendedMessageFormat(newPattern, new Locale("en", "US"), registry);
        assertTrue(formatRegistry.equals(formatEqual));

        ExtendedMessageFormat formatNotEqual = new ExtendedMessageFormat(newPattern, new Locale("en", "GB"), registry);
        assertFalse(formatRegistry.equals(formatNotEqual));

        ExtendedMessageFormat formatDifferentPattern = new ExtendedMessageFormat(newPattern, new Locale("en", "US"), registry);
        formatDifferentPattern.applyPattern("Hello {0} ({4}), your age is {1}, and today is {3}");
        assertFalse(formatRegistry.equals(formatDifferentPattern));

        // Test hashCode method
        ExtendedMessageFormat formatHashCode = new ExtendedMessageFormat(newPattern, new Locale("en", "US"), registry);
        ExtendedMessageFormat formatHashCode2 = new ExtendedMessageFormat(newPattern, new Locale("en", "US"), registry);
        int hashCode = formatHashCode.hashCode();
        int hashCode2 = formatHashCode2.hashCode();
        assertEquals(hashCode, hashCode2);

        formatRegistry.registry.put("date", new DateFormatFactory());
        hashCode = formatRegistry.hashCode();
        hashCode2 = new ExtendedMessageFormat(newPattern, new Locale("en", "US"), registry).hashCode();
        assertNotEquals(hashCode, hashCode2);

        formatRegistry.toPattern = newPattern + "x";
        hashCode = formatRegistry.hashCode();
        hashCode2 = new ExtendedMessageFormat(newPattern, new Locale("en", "US"), registry).hashCode();
        assertNotEquals(hashCode, hashCode2);
    }
}