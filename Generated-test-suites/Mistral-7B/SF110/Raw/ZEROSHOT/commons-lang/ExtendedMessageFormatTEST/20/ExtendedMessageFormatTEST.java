package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        assertNull(formatter.toPattern());
        assertEquals(Locale.getDefault(), formatter.getLocale());
        assertNull(formatter.getRegistry());

        try {
            formatter.applyPattern("");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testLocaleConstructor() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0,date,yyyy-MM-dd HH:mm:ss}", Locale.US);
        assertEquals("{0,date,yyyy-MM-dd HH:mm:ss}", formatter.toPattern());
        assertEquals(Locale.US, formatter.getLocale());
        assertNull(formatter.getRegistry());

        String pattern = formatter.toPattern();
        Formatter formatter1 = new MessageFormat(pattern, Locale.US);
        Object[] args = new Object[] {"2019-12-31 23:59:59"};
        assertEquals("2019-12-31 23:59:59", formatter1.format(args));
    }

    @org.junit.Test
    public void testRegistryConstructor() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0,date,yyyy-MM-dd HH:mm:ss}", Locale.US, registry);
        assertEquals("{0,date,yyyy-MM-dd HH:mm:ss}", formatter.toPattern());
        assertEquals(Locale.US, formatter.getLocale());
        assertEquals(registry, formatter.getRegistry());

        String pattern = formatter.toPattern();
        Formatter formatter1 = new MessageFormat(pattern, Locale.US);
        Object[] args = new Object[] {"2019-12-31 23:59:59"};
        assertEquals("2019-12-31 23:59:59", formatter1.format(args));
    }

    @org.junit.Test
    public void testRegistryConstructorWithNullPattern() {
        try {
            new ExtendedMessageFormat(null, Locale.US, Collections.emptyMap());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testApplyPattern() {
        String pattern = "{0,date,yyyy-MM-dd HH:mm:ss},{1,date,EEE hh:mm a}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);

        Object[] args = new Object[] {"2019-12-31 23:59:59", "Tuesday 11:00 PM"};
        String expected = "2019-12-31 23:59:59,Tuesday 11:00 PM";
        String result = formatter.applyPattern(pattern).format(args);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testApplyPatternWithInvalidArgumentIndex() {
        String pattern = "{0,date,yyyy-MM-dd HH:mm:ss},{1,date,EEE hh:mm a},{3,date,yyyy-MM-dd HH:mm:ss}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);

        Object[] args = new Object[] {"2019-12-31 23:59:59", "Tuesday 11:00 PM"};
        try {
            formatter.applyPattern(pattern);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Invalid format argument index"));
            assertTrue(message.contains("3"));
        }
    }

    @org.junit.Test
    public void testApplyPatternWithUnknownFormatDescription() {
        String pattern = "{0,unknownFormat,yyyy-MM-dd HH:mm:ss}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);

        try {
            formatter.applyPattern(pattern);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Unknown format description"));
            assertTrue(message.contains("unknownFormat"));
        }
    }

    @org.junit.Test
    public void testSetFormat() {
        String pattern = "{0,date,yyyy-MM-dd HH:mm:ss},{1,format:customDateFormat,EEE hh:mm a}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("format:customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);

        Object[] args = new Object[] {"2019-12-31 23:59:59", ""};
        String expected = "2019-12-31 23:59:59,Monday 11:00 PM";
        String result = formatter.applyPattern(pattern).format(args);
        assertEquals(expected, result);

        formatter.setFormat(1, new StringFormat("EEE hh:mm a"));
        result = formatter.applyPattern(pattern).format(args);
        assertEquals("2019-12-31 23:59:59,Monday 11:00 AM", result);
    }

    @org.junit.Test
    public void testSetFormatByArgumentIndex() {
        String pattern = "{0,date,yyyy-MM-dd HH:mm:ss},{1,format:customDateFormat,EEE hh:mm a}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("format:customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);

        Object[] args = new Object[] {"2019-12-31 23:59:59", ""};
        String expected = "2019-12-31 23:59:59,Monday 11:00 PM";
        String result = formatter.applyPattern(pattern).format(args);
        assertEquals(expected, result);

        formatter.setFormatByArgumentIndex(1, new StringFormat("EEE hh:mm a"));
        result = formatter.applyPattern(pattern).format(args);
        assertEquals("2019-12-31 23:59:59,Monday 11:00 AM", result);
    }

    @org.junit.Test
    public void testSetFormats() {
        String pattern = "{0,date,yyyy-MM-dd HH:mm:ss},{1,format:customDateFormat,EEE hh:mm a}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("format:customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);

        Object[] args = new Object[] {"2019-12-31 23:59:59", ""};
        String expected = "2019-12-31 23:59:59,Monday 11:00 PM";
        String result = formatter.applyPattern(pattern).format(args);
        assertEquals(expected, result);

        Formatter[] formats = new Formatter[] {new StringFormat("EEE hh:mm a"), new DateFormat()};
        formatter.setFormats(formats);
        result = formatter.applyPattern(pattern).format(args);
        assertEquals("2019-12-31 23:59:59,Monday 11:00 AM", result);
    }

    @org.junit.Test
    public void testSetFormatsByArgumentIndex() {
        String pattern = "{0,date,yyyy-MM-dd HH:mm:ss},{1,format:customDateFormat,EEE hh:mm a}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("format:customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);

        Object[] args = new Object[] {"2019-12-31 23:59:59", ""};
        String expected = "2019-12-31 23:59:59,Monday 11:00 PM";
        String result = formatter.applyPattern(pattern).format(args);
        assertEquals(expected, result);

        Formatter[] formats = new Formatter[] {new DateFormat(), new StringFormat("EEE hh:mm a")};
        formatter.setFormatsByArgumentIndex(new int[] {1}, formats);
        result = formatter.applyPattern(pattern).format(args);
        assertEquals("2019-12-31 23:59:59,Monday 11:00 AM", result);
    }

    @org.junit.Test
    public void testEquals() {
        String pattern1 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        String pattern2 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        ExtendedMessageFormat formatter1 = new ExtendedMessageFormat(pattern1, Locale.US, Collections.emptyMap());
        ExtendedMessageFormat formatter2 = new ExtendedMessageFormat(pattern2, Locale.US, Collections.emptyMap());
        assertTrue(formatter1.equals(formatter2));

        String pattern3 = "{0,date,yyyy-MM-dd HH:mm:ss},{1,date,EEE hh:mm a}";
        ExtendedMessageFormat formatter3 = new ExtendedMessageFormat(pattern3, Locale.US, Collections.emptyMap());
        assertFalse(formatter1.equals(formatter3));

        String pattern4 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        ExtendedMessageFormat formatter4 = new ExtendedMessageFormat(pattern4, Locale.US, null);
        assertFalse(formatter1.equals(formatter4));

        String pattern5 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        ExtendedMessageFormat formatter5 = new ExtendedMessageFormat(pattern5, Locale.US, new HashMap<>());
        assertFalse(formatter1.equals(formatter5));
    }

    @org.junit.Test
    public void testHashCode() {
        String pattern1 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        ExtendedMessageFormat formatter1 = new ExtendedMessageFormat(pattern1, Locale.US, Collections.emptyMap());
        int hashCode1 = formatter1.hashCode();

        String pattern2 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        ExtendedMessageFormat formatter2 = new ExtendedMessageFormat(pattern2, Locale.US, Collections.emptyMap());
        int hashCode2 = formatter2.hashCode();
        assertEquals(hashCode1, hashCode2);

        String pattern3 = "{0,date,yyyy-MM-dd HH:mm:ss},{1,date,EEE hh:mm a}";
        ExtendedMessageFormat formatter3 = new ExtendedMessageFormat(pattern3, Locale.US, Collections.emptyMap());
        int hashCode3 = formatter3.hashCode();
        assertNotEquals(hashCode1, hashCode3);

        String pattern4 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        ExtendedMessageFormat formatter4 = new ExtendedMessageFormat(pattern4, Locale.US, null);
        int hashCode4 = formatter4.hashCode();
        assertNotEquals(hashCode1, hashCode4);

        String pattern5 = "{0,date,yyyy-MM-dd HH:mm:ss}";
        ExtendedMessageFormat formatter5 = new ExtendedMessageFormat(pattern5, Locale.US, new HashMap<>());
        int hashCode5 = formatter5.hashCode();
        assertNotEquals(hashCode1, hashCode5);
    }

    private static class CustomDateFormatFactory implements FormatFactory {
        @Override
        public Formatter getFormat(String description, Locale locale, Format previous) {
            return new StringFormat("EEE hh:mm a");
        }
    }

    private static class StringFormat implements Formatter {
        private final String format;

        public StringFormat(String format) {
            this.format = format;
        }

        @Override
        public String buffer(MessageFormat formatter, Object obj, String buffer, int index, int len) {
            return Strings.leftPad(String.valueOf(obj), len, " ");
        }

        @Override
        public String format(Object obj, Locale locale) {
            return format;
        }
    }
}