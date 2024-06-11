package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Test;

import java.text.*;
import java.util.*;

public class ExtendedMessageFormatTest {
    @Test
    public void testConstructors() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(null, format.getRegistry());

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
        assertEquals("Hello {0}!", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(null, format.getRegistry());

        format = new ExtendedMessageFormat("Hello {0}!", null);
        assertEquals("Hello {0}!", format.toPattern());
        assertEquals(null, format.getLocale());
        assertEquals(null, format.getRegistry());

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), null);
        assertEquals("Hello {0}!", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(null, format.getRegistry());

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        assertEquals("Hello {date,date,yyyy-MM-dd}!", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertNotNull(format.getRegistry());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        assertEquals("", format.toPattern());

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
        assertEquals("Hello {0}!", format.toPattern());

        format = new ExtendedMessageFormat("Hello {0}!", null);
        assertEquals("Hello {0}!", format.toPattern());

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), null);
        assertEquals("Hello {0}!", format.toPattern());

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        assertEquals("Hello {date,date,yyyy-MM-dd}!", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        String result = format.format(new Object[]{"John"});
        assertEquals("Hello John!", result);

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
        result = format.format(new Object[]{"John"}, Locale.US);
        assertEquals("Hello John!", result);

        format = new ExtendedMessageFormat("Hello {0}!", null);
        result = format.format(new Object[]{"John"});
        assertEquals("Hello John!", result);

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), null);
        result = format.format(new Object[]{"John"}, Locale.US);
        assertEquals("Hello John!", result);

        format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        Date date = new Date();
        result = format.format(new Object[]{"John", date}, Locale.US);
        assertEquals("Hello John!" + " " + DateFormat.format(new SimpleDateFormat("yyyy-MM-dd").format(date), Locale.US), result);

        format = new ExtendedMessageFormat("Hello {0}! {1,date,date}", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        result = format.format(new Object[]{"John", new Date()}, Locale.US);
        assertEquals("Hello John!" + " " + DateFormat.format(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), Locale.US), result);
    }

    @Test
    public void testSetFormat() {
        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
            format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
            format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", null);
            format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), null);
            format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
                put("date", new SimpleDateFormat("yyyy-MM-dd"));
            }});
            format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}!");
        assertTrue(format1.equals(format2));

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
        format2 = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        assertFalse(format1.equals(format2));

        format1 = new ExtendedMessageFormat("Hello {0}!", null);
        format2 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
        assertFalse(format1.equals(format2));

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), null);
        format2 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        assertFalse(format1.equals(format2));

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        format2 = new ExtendedMessageFormat("Hello {1}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        assertFalse(format1.equals(format2));

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        format2 = new MessageFormat("Hello {0}!");
        assertFalse(format1.equals(format2));

        format1 = new MessageFormat("Hello {0}!");
        format2 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        assertFalse(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}!");
        assertEquals(format1.hashCode(), format2.hashCode());

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
        format2 = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        assertNotSame(format1.hashCode(), format2.hashCode());

        format1 = new ExtendedMessageFormat("Hello {0}!", null);
        format2 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault());
        assertNotSame(format1.hashCode(), format2.hashCode());

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), null);
        format2 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        assertNotSame(format1.hashCode(), format2.hashCode());

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        format2 = new ExtendedMessageFormat("Hello {1}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        assertNotSame(format1.hashCode(), format2.hashCode());

        format1 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        FormatFactory factory = new SimpleDateFormat("yyyy-MM-dd");
        FormatFactory factory2 = new SimpleDateFormat("dd-MM-yyyy");
        HashMap<String, FormatFactory> map = new HashMap<String, FormatFactory>() {{
            put("date", factory);
        }};
        HashMap<String, FormatFactory> map2 = new HashMap<String, FormatFactory>() {{
            put("date", factory2);
        }};
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), map);
        assertEquals(format1.hashCode(), format3.hashCode());
        assertEquals(format1.hashCode(), map.hashCode());
        assertEquals(map.hashCode(), factory.hashCode());
        assertNotSame(format1.hashCode(), map2.hashCode());
        assertNotSame(map.hashCode(), map2.hashCode());
        assertNotSame(format1.hashCode(), factory2.hashCode());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormat("yyyy-MM-dd"));
        }});
        FormatFactory factory = format.getFormat("date");
        assertEquals(new SimpleDateFormat("yyyy-MM-dd"), factory);
        assertNull(format.getFormat("unknown"));
    }
}