package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertEquals("", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(null, fmt.getRegistry());
    }

    @org.junit.Test
    public void testStringConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}");
        assertEquals("{0} {1} {2}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(null, fmt.getRegistry());
    }

    @org.junit.Test
    public void testLocaleConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        assertEquals("{0} {1} {2}", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertEquals(null, fmt.getRegistry());
    }

    @org.junit.Test
    public void testRegistryConstructor() {
        Properties props = new Properties();
        props.setProperty("myFormat", "java.text.DecimalFormat");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String format, Locale locale) {
                return new DecimalFormat(format);
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format,number,\"###.##\"}", registry);
        assertEquals("{format,number,\"###.##\"}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @org.junit.Test
    public void testLocaleRegistryConstructor() {
        Properties props = new Properties();
        props.setProperty("myFormat", "java.text.DecimalFormat");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String format, Locale locale) {
                return new DecimalFormat(format);
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format,number,\"###.##\"}", Locale.US, registry);
        assertEquals("{format,number,\"###.##\"}", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        fmt.setFormat(1, new MessageFormat("%1$s"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        fmt.setFormatByArgumentIndex(1, new MessageFormat("%1$s"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        Format[] formats = new MessageFormat("%1$s", Locale.US).getFormats();
        fmt.setFormats(formats);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        Format[] formats = new MessageFormat("%1$s", Locale.US).getFormats();
        fmt.setFormatsByArgumentIndex(formats);
    }

    @org.junit.Test
    public void testEquals() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        assertTrue(fmt1.equals(fmt2));
        assertTrue(fmt2.equals(fmt1));
        ExtendedMessageFormat fmt3 = new ExtendedMessageFormat("{0} {1} {2}", Locale.US, new HashMap<>());
        assertFalse(fmt1.equals(fmt3));
        assertFalse(fmt3.equals(fmt1));
        ExtendedMessageFormat fmt4 = new ExtendedMessageFormat("{0} {1} {2}", Locale.US, new HashMap<>());
        fmt4.setRegistry(new HashMap<>());
        assertFalse(fmt1.equals(fmt4));
        assertFalse(fmt4.equals(fmt1));
        assertFalse(fmt1.equals(null));
        assertFalse(null.equals(fmt1));
    }

    @org.junit.Test
    public void testHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        assertEquals(fmt1.hashCode(), fmt2.hashCode());
        ExtendedMessageFormat fmt3 = new ExtendedMessageFormat("{0} {1} {2}", Locale.US, new HashMap<>());
        assertNotSame(fmt1.hashCode(), fmt3.hashCode());
        assertNotSame(fmt3.hashCode(), fmt1.hashCode());
    }

    @org.junit.Test
    public void testGetFormat() {
        Properties props = new Properties();
        props.setProperty("myFormat", "java.text.DecimalFormat");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String format, Locale locale) {
                return new DecimalFormat(format);
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format,number,\"###.##\"}", registry);
        Format format = fmt.getFormat("myFormat");
        assertNotNull(format);
        assertTrue(format instanceof DecimalFormat);
        assertEquals("###.##", ((DecimalFormat)format).getPattern());
    }

    @org.junit.Test
    public void testGetFormatNull() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        Format format = fmt.getFormat("missingFormat");
        assertNull(format);
    }

    @org.junit.Test
    public void testApplyPattern() {
        String pattern = "{0} {1,number,#.##} {2,date,\"dd.MM.yyyy\"}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Locale.US);
        assertNotNull(fmt.getFormat("number"));
        assertNotNull(fmt.getFormat("date"));
        String result = fmt.format(new Object[]{"Hello", 3.14, new Date()});
        String expected = "Hello 3.1400.00 31.10.2023";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testApplyPatternNullRegistry() {
        String pattern = "{0} {1,number,#.##} {2,date,\"dd.MM.yyyy\"}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Locale.US, null);
        assertNull(fmt.getFormat("number"));
        assertNull(fmt.getFormat("date"));
        String result = fmt.format(new Object[]{"Hello", 3.14, new Date()});
        assertNotNull(result);
        assertTrue(StringUtils.isBlank(result));
    }

    @org.junit.Test
    public void testApplyPatternInvalidFormatName() {
        Properties props = new Properties();
        props.setProperty("myFormat", "java.text.DecimalFormat");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String format, Locale locale) {
                throw new IllegalArgumentException("Invalid format name: " + formatName);
            }
        });
        String pattern = "{myFormat,number,\"###.##\"}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        try {
            fmt.applyPattern(pattern);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format name: myFormat", e.getMessage());
        }
    }
}