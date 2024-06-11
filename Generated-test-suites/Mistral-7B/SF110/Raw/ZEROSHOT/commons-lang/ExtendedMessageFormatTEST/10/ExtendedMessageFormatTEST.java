package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstruction() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        assertNull(fmt.getFORMAT());
        assertNotNull(fmt.getLocale());
        assertNull(fmt.getRegistry());
        assertEquals("", fmt.toPattern());

        fmt = new ExtendedMessageFormat("{0}, {1}", Locale.ENGLISH);
        assertNotNull(fmt.getFORMAT());
        assertSame(Locale.ENGLISH, fmt.getLocale());
        assertNull(fmt.getRegistry());
        assertEquals("", fmt.toPattern());

        fmt = new ExtendedMessageFormat("{0}, {1}", null, Collections.emptyMap());
        assertNotNull(fmt.getFORMAT());
        assertSame(Locale.getDefault(), fmt.getLocale());
        assertNotNull(fmt.getRegistry());
        assertEquals("", fmt.toPattern());

        fmt = new ExtendedMessageFormat("{0}, {1}", null, new HashMap<>());
        assertNotNull(fmt.getFORMAT());
        assertSame(Locale.getDefault(), fmt.getLocale());
        assertNotNull(fmt.getRegistry());
        assertEquals("", fmt.toPattern());

        fmt = new ExtendedMessageFormat("{0}, {1}", null, new HashMap<>(){
            {
                put("date", new SimpleDateFormat("dd/MM/yyyy"));
            }
        });
        assertNotNull(fmt.getFORMAT());
        assertSame(Locale.getDefault(), fmt.getLocale());
        assertNotNull(fmt.getRegistry());
        assertEquals("", fmt.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        assertNotNull(fmt.applyPattern("{0}, {1}"));
        assertSame(fmt, fmt.applyPattern("{0}, {1}"));

        fmt.applyPattern("{0}, {1,date,\"dd/MM/yyyy\"}, {2}");
        assertEquals("{0}, {1,date,\"dd/MM/yyyy\"}, {2}", fmt.toPattern());
        assertNotNull(fmt.getRegistry());
        assertNotNull(fmt.getFormat("date"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormat(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormatByArgumentIndex(1, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormats(new Format[]{new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd/MM/yyyy")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1}");
        fmt.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd/MM/yyyy")});
    }

    @Test
    public void testEqualsAndHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat fmt3 = new ExtendedMessageFormat("{0}, {1,date,\"dd/MM/yyyy\"}");

        assertEquals(fmt1, fmt1);
        assertEquals(fmt1, fmt2);
        assertEquals(fmt1.hashCode(), fmt2.hashCode());
        assertNotEquals(fmt1, fmt3);
        assertFalse(fmt1.equals(fmt3));
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1,date,\"dd/MM/yyyy\"}");
        Format dateFormat = fmt.getFormat("date");
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy", dateFormat.toPattern());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFormatNullName() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}, {1,date,\"dd/MM/yyyy\"}");
        fmt.getFormat(null);
    }

}