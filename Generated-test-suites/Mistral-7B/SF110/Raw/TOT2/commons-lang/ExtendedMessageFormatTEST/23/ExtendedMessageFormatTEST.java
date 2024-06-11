package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ExtendedMessageFormatTest {
    @Test
    public void testBasicConstruction() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testLocaleConstruction() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testRegistryConstruction() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", (format, args, locale) -> new SimpleDateFormat("mm/dd/yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{custom,date,\"MM/dd/yyyy\"}, {1}", registry);
        assertEquals("{custom,date,\"MM/dd/yyyy\"}, {1}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testApplyPatternWithDefaultRegistry() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.applyPattern("{0},{1}");
        assertEquals("{0},{1}", fmt.toPattern());
    }

    @Test
    public void testApplyPatternWithCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", (format, args, locale) -> new SimpleDateFormat("mm/dd/yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{custom,date,\"MM/dd/yyyy\"}, {1}", registry);
        fmt.applyPattern("{0},{1},{custom,date,\"MM/dd/yyyy\"}");
        assertEquals("{0},{1},{custom,date,\"MM/dd/yyyy\"}", fmt.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormat(0, new SimpleDateFormat("mm/dd/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormatByArgumentIndex(0, new SimpleDateFormat("mm/dd/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        Format[] formats = {new SimpleDateFormat("mm/dd/yyyy"), new SimpleDateFormat("dd/MM/yyyy")};
        fmt.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        Format[] formats = {new SimpleDateFormat("mm/dd/yyyy"), new SimpleDateFormat("dd/MM/yyyy")};
        fmt.setFormatsByArgumentIndex(new Format[0], formats);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}, {1}");
        assertEquals(fmt1, fmt2);

        fmt1.setRegistry(new HashMap<>());
        assertNotEquals(fmt1, fmt2);

        fmt2.setRegistry(new HashMap<>());
        assertEquals(fmt1, fmt2);

        fmt1.setLocale(Locale.US);
        fmt2.setLocale(Locale.JAPAN);
        assertNotEquals(fmt1, fmt2);

        fmt1.setLocale(Locale.JAPAN);
        fmt2.setLocale(Locale.US);
        assertNotEquals(fmt1, fmt2);

        fmt1.setRegistry(new HashMap<>());
        fmt1.setLocale(Locale.US);
        fmt2.setRegistry(new HashMap<>());
        fmt2.setLocale(Locale.US);
        fmt2.setRegistry(new HashMap<>());
        fmt2.put("custom", (format, args, locale) -> new SimpleDateFormat("mm/dd/yyyy"));
        assertEquals(fmt1, fmt2);

        fmt2.put("custom", (format, args, locale) -> new SimpleDateFormat("dd/MM/yyyy"));
        assertNotEquals(fmt1, fmt2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}, {1}");

        assertEquals(System.identityHashCode(fmt1), System.identityHashCode(fmt1.hashCode()));
        assertEquals(System.identityHashCode(fmt1), fmt1.hashCode());

        assertEquals(System.identityHashCode(fmt2), System.identityHashCode(fmt2.hashCode()));
        assertEquals(System.identityHashCode(fmt2), fmt2.hashCode());

        fmt1.setRegistry(new HashMap<>());
        assertNotEquals(System.identityHashCode(fmt1), fmt1.hashCode());

        fmt2.setRegistry(new HashMap<>());
        assertNotEquals(System.identityHashCode(fmt1), fmt2.hashCode());

        fmt1.setLocale(Locale.US);
        fmt2.setLocale(Locale.JAPAN);
        assertNotEquals(System.identityHashCode(fmt1), fmt2.hashCode());

        fmt1.setLocale(Locale.JAPAN);
        fmt2.setLocale(Locale.US);
        assertNotEquals(System.identityHashCode(fmt1), fmt2.hashCode());

        fmt1.setRegistry(new HashMap<>());
        fmt1.setLocale(Locale.US);
        fmt2.setRegistry(new HashMap<>());
        fmt2.setLocale(Locale.US);
        fmt2.put("custom", (format, args, locale) -> new SimpleDateFormat("mm/dd/yyyy"));
        assertNotEquals(System.identityHashCode(fmt1), fmt2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.applyPattern("{2}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnreadableFormatElement() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0},{1},{2}");
        fmt.applyPattern("{0},{1,}{2}");
    }
}