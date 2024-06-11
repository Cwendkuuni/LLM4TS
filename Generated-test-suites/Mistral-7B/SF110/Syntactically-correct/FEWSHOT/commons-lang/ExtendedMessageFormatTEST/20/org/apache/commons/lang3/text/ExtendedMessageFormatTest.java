package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.MessageFormatFactory;
import org.apache.commons.lang3.text.MessageFormatMessage;
import org.apache.commons.lang3.text.MessageFormatParsePosition;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.text.SimpleMessageFormat;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.Validate;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstance() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testNewInstanceWithLocale() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testNewInstanceWithRegistry() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("MM/dd/yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}");
        assertEquals("{format date 'dd-MM-yyyy'}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testNewInstanceWithLocaleAndRegistry() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("MM/dd/yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.US, registry);
        assertEquals("{format date 'dd-MM-yyyy'}", fmt.toPattern());
        assertEquals(Locale.US, fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testApplyPatternEmpty() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault());
        fmt.applyPattern("");
        assertEquals("", fmt.toPattern());
    }

    @Test
    public void testApplyPatternSimple() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        fmt.applyPattern("{0} {1}");
        assertEquals("{0} {1}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testApplyPatternCustom() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        fmt.applyPattern("{format date 'dd-MM-yyyy'}");
        assertEquals("{format date 'dd-MM-yyyy'}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testApplyPatternFormatIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        fmt.applyPattern("{1} {0}");
        assertEquals("{1} {0}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertNull(fmt.getRegistry());
    }

    @Test
    public void testApplyPatternCustomFormatIndex() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        fmt.applyPattern("{format date 'yyyy-MM-dd'}");
        assertEquals("{format date 'yyyy-MM-dd'}", fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @Test
    public void testApplyPatternArgumentIndexOutOfRange() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        assertThrows(IllegalArgumentException.class, () -> fmt.applyPattern("{3} {2}"));
    }

    @Test
    public void testApplyPatternCustomArgumentIndexOutOfRange() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        assertThrows(IllegalArgumentException.class, () -> fmt.applyPattern("{2} {3}"));
    }

    @Test
    public void testGetFormat() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        MessageFormat result = fmt.getFormat("date");
        assertEquals(new SimpleDateFormat("dd-MM-yyyy"), result);
        assertNull(fmt.getFormat("unknown"));
    }

    @Test
    public void testGetFormats() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        MessageFormat[] result = fmt.getFormats();
        assertEquals(1, result.length);
        assertEquals(new SimpleDateFormat("dd-MM-yyyy"), result[0]);
        assertNull(fmt.getFormats());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormat(0, new SimpleMessageFormat()));
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormatByArgumentIndex(0, new SimpleMessageFormat()));
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormats(new MessageFormat[0]));
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormatsByArgumentIndex(new MessageFormat[0]));
    }

    @Test
    public void testFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        Object[] args = { "Hello", "World" };
        String result = fmt.format(args);
        assertEquals("Hello World", result);
    }

    @Test
    public void testFormatCustom() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        Object[] args = { new Date() };
        String result = fmt.format(args);
        assertEquals(StringUtils.dateToString(new Date(), "dd-MM-yyyy"), result);
    }

    @Test
    public void testFormatNullArguments() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        Object[] args = { null, null };
        String result = fmt.format(args);
        assertNull(result);
    }

    @Test
    public void testFormatCustomNullArguments() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        Object[] args = { null };
        String result = fmt.format(args);
        assertNull(result);
    }

    @Test
    public void testFormatInvalidPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        Object[] args = { "Hello", "World" };
        assertThrows(MessageFormatException.class, () -> fmt.format(args, new ParsePosition(0)));
    }

    @Test
    public void testFormatCustomInvalidPattern() {
        Map<String, MessageFormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{format date 'dd-MM-yyyy'}", Locale.getDefault(), registry);
        Object[] args = { new Date() };
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> fmt.format(args, pos));
    }

}