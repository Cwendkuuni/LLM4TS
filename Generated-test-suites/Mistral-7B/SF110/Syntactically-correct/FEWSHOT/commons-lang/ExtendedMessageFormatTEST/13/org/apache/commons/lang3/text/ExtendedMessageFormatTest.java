package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Test;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateExtendedMessageFormatWithDefaults() {
        final String pattern = "Hello {0}, your order {1} will be processed soon.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
        assertNull(format.getRegistry());
        assertEquals(Locale.getDefault(), format.getLocale());
    }

    @Test
    public void testCreateExtendedMessageFormatWithDefaultsAndRegistry() {
        final String pattern = "Hello {0}, your order {1} will be processed soon.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(registry, format.getRegistry());
        assertEquals(Locale.getDefault(), format.getLocale());
    }

    @Test
    public void testCreateExtendedMessageFormatWithLocale() {
        final String pattern = "Hello {0}, your order {1} will be processed soon.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals(pattern, format.toPattern());
        assertNull(format.getRegistry());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testCreateExtendedMessageFormatWithLocaleAndRegistry() {
        final String pattern = "Hello {0}, your order {1} will be processed soon.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(registry, format.getRegistry());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testApplyPatternWithNoCustomFormats() {
        final String pattern = "Hello {0}, your order {1} will be processed on {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
        assertEquals("Hello {0}, your order {1} will be processed on {2}", format.toPattern());
        assertNull(format.getRegistry());
    }

    @Test
    public void testApplyPatternWithCustomFormats() {
        final String pattern = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        assertEquals("Hello {0}, your order {1} will be processed on {date}", format.toPattern());
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        final String pattern = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Date date = new Date();
        SimpleDateFormat dateFormat = (SimpleDateFormat) format.getFormatByArgumentIndex(1);
        dateFormat.applyPattern("dd/MM/yyyy");
        Object[] args = new Object[]{"John Doe", date};
        String result = format.format(args);
        assertEquals("Hello John Doe, your order will be processed on " + date.toString(), result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        final String pattern = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        format.setFormat(1, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        final String pattern = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Format[] formats = new Format[]{new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd/MM/yyyy")};
        format.setFormatsByArgumentIndex(Collections.singletonList(formats));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        final String pattern = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Format[] formats = new Format[]{new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd/MM/yyyy")};
        format.setFormats(formats);
    }

    @Test
    public void testEquals() {
        final String pattern = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        final String pattern2 = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2, registry);
        format2.applyPattern(pattern2);
        assertTrue(format.equals(format2));
    }

    @Test
    public void testHashCode() {
        final String pattern = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        final String pattern2 = "Hello {0}, your order {1} will be processed on {date,date,dd/MM/yyyy}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", (format, name, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2, registry);
        format2.applyPattern(pattern2);
        assertEquals(format.hashCode(), format2.hashCode());
    }
}