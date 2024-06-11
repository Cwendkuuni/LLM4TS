package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        try {
            format.applyPattern("{}");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, {0}!", format.toPattern());
        Object[] args = {"John"};
        String result = format.format(args);
        assertEquals("Hello, John!", result);
    }

    @org.junit.Test
    public void testLocaleConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.FRENCH);
        assertEquals("Hello, {0}!", format.toPattern());
        Object[] args = {"Jean"};
        String result = format.format(args);
        assertEquals("Hello, Jean!", result);
    }

    @org.junit.Test
    public void testRegistryConstructor() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {date,date}!", registry);
        assertEquals("Hello, {date,date}!", format.toPattern());
        Object[] args = {new Date()};
        String result = format.format(args);
        try {
            ParsePosition pos = new ParsePosition(0);
            MessageFormat mf = new MessageFormat(format.toPattern());
            mf.applyPattern(format.toPattern());
            mf.format(args, pos);
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testRegistryConstructorWithFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {date,date,short,en_US}!", registry);
        assertEquals("Hello, {date,date,short,en_US}!", format.toPattern());
        Object[] args = {new Date()};
        String result = format.format(args);
        assertEquals("Hello, Jan 30, 2023", result);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormats(new Format[0]);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatsByArgumentIndex(new Format[0]);
    }

    @org.junit.Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello, {0}, {1}!");
        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(format3));
    }

    @org.junit.Test
    public void testHashcode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello, {0}, {1}!");
        int hashcode1 = format1.hashCode();
        int hashcode2 = format1.hashCode();
        int hashcode3 = format3.hashCode();
        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
    }

    private static class DateFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String pattern, Locale locale) {
            return new SimpleDateFormat(pattern, locale);
        }

    }

}