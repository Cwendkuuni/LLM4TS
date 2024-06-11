package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testExtendedMessageFormat_DefaultConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertNull(fmt.toPattern());
        assertNull(fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @org.junit.Test
    public void testExtendedMessageFormat_StringConstructor() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, fmt.toPattern());
        assertNotNull(fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @org.junit.Test
    public void testExtendedMessageFormat_StringConstructor_Locale() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals(pattern, fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @org.junit.Test
    public void testExtendedMessageFormat_StringConstructor_Registry() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("age", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @org.junit.Test
    public void testExtendedMessageFormat_StringConstructor_Locale_Registry() {
        String pattern = "Hello {0}, your age is {1}.";
        Locale locale = Locale.US;
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("age", new DateFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(pattern, fmt.toPattern());
        assertEquals(locale, fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormat_IntIndex_Format() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormat(0, new MessageFormat("{0}"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex_IntIndex_Format() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormatByArgumentIndex(0, new MessageFormat("{0}"));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormats_FormatArray() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormats(new MessageFormat[0]);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex_FormatArray() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormatsByArgumentIndex(new MessageFormat[0]);
    }

    @org.junit.Test
    public void testApplyPattern_ValidPattern() {
        String pattern = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern(pattern);
        assertNotNull(fmt.getFormats());
        assertEquals(2, fmt.getFormats().length);
        assertEquals("date", fmt.getFormats()[1].getClass().getSimpleName());
        assertNotNull(fmt.getFormat("date"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_InvalidPatternFormat() {
        String pattern = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern("Hello {0}, your age is {1,date,Short,thisisinvalid}.");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_MissingFormatName() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern("Hello {0}, your age is {1}.");
        fmt.applyPattern("Hello {0}, your {2} is {1}.");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_InvalidFormatArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern("Hello {0}, your age is {2}.");
    }

    @org.junit.Test
    public void testGetFormat_ValidFormatName() {
        String pattern = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern(pattern);
        Format format = fmt.getFormat("date");
        assertNotNull(format);
        assertEquals("date", format.getClass().getSimpleName());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testGetFormat_NullFormatName() {
        String pattern = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern(pattern);
        fmt.getFormat(null);
    }

    @org.junit.Test
    public void testEquals_SameObject() {
        String pattern = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(pattern);
        ExtendedMessageFormat fmt2 = fmt1;
        assertTrue(fmt1.equals(fmt2));
    }

    @org.junit.Test
    public void testEquals_DifferentObjects() {
        String pattern1 = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(pattern1);
        String pattern2 = "Hello {0}, your name is {1,date,Short}.";
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(pattern2);
        assertFalse(fmt1.equals(fmt2));
    }

    @org.junit.Test
    public void testHashCode_SameObjects() {
        String pattern1 = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat fmt2 = fmt1;
        assertEquals(fmt1.hashCode(), fmt2.hashCode());
    }

    @org.junit.Test
    public void testHashCode_DifferentObjects() {
        String pattern1 = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(pattern1);
        String pattern2 = "Hello {0}, your name is {1,date,Short}.";
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(pattern2);
        assertNotEquals(fmt1.hashCode(), fmt2.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        String pattern = "Hello {0}, your age is {1,date,Short}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern(pattern);
        String expected = "ExtendedMessageFormat[pattern=Hello {0}, your age is {1,date,Short}.locale=java.util.Locale[language=en,country=US],registry={}]";
        assertEquals(expected, fmt.toString());
    }
}