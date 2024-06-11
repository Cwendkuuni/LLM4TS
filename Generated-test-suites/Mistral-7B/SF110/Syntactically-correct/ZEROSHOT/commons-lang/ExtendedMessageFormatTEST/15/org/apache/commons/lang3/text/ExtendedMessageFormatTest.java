package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatEmpty() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.toPattern());
        assertEquals("", format.format(new Object[0]));
    }

    @Test
    public void testExtendedMessageFormatDefault() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        assertEquals("{0}", format.toPattern());
        Object[] args = {"test"};
        String result = format.format(args);
        assertEquals("test", result);
    }

    @Test
    public void testExtendedMessageFormatWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        assertEquals("{0},{1}", format.toPattern());
        Object[] args = {"test1", "test2"};
        String result = format.format(args);
        assertEquals("test1,test2", result);
    }

    @Test
    public void testExtendedMessageFormatWithCustomFormatRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat,number,#}", registry);
        assertEquals("{customFormat,number,#}", format.toPattern());
        Object[] args = {123};
        String result = format.format(args);
        assertEquals("#123", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormat(0, new MessageFormat("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormatByArgumentIndex(0, new MessageFormat("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("test");
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("test");
        format.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testEqualsAndHashcode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}"){};
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}"){};
        assertTrue(format1.equals(format2));
        assertTrue(format1.hashCode() == format2.hashCode());

        Object obj = new Object();
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}"){};
        assertFalse(format1.equals(obj));
        assertFalse(format1.hashCode() == obj.hashCode());

        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0}", Locale.US);
        assertFalse(format1.equals(format4));
        assertFalse(format1.hashCode() == format4.hashCode());

        ExtendedMessageFormat format5 = new ExtendedMessageFormat("{0}", new HashMap<>());
        assertFalse(format1.equals(format5));
        assertFalse(format1.hashCode() == format5.hashCode());
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat,date,short}", registry);
        Format result = format.getFormat("customFormat");
        assertNotNull(result);
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "{0,number,integer}{1}";
        ParsePosition pos = new ParsePosition(0);
        int result = ExtendedMessageFormat.this.readArgumentIndex(pattern, pos);
        assertEquals(0, result);

        pos.setIndex(5);
        result = ExtendedMessageFormat.this.readArgumentIndex(pattern, pos);
        assertEquals(1, result);

        pos.setIndex(12);
        try {
            result = ExtendedMessageFormat.this.readArgumentIndex(pattern, pos);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format argument index at position 5: ", e.getMessage().substring(0, e.getMessage().indexOf(":")));
        }
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "{customFormat,date,short}";
        ParsePosition pos = new ParsePosition(0);
        String result = ExtendedMessageFormat.this.parseFormatDescription(pattern, pos);
        assertEquals("customFormat", result);

        pos.setIndex(13);
        try {
            result = ExtendedMessageFormat.this.parseFormatDescription(pattern, pos);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Unterminated format element at position 5", e.getMessage());
        }
    }

    @Test
    public void testInsertFormats() {
        String pattern = "{0,number,integer}{customFormat,date,short}";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("customFormat");
        String result = ExtendedMessageFormat.this.insertFormats(pattern, customPatterns);
        assertEquals("{0,number,integer}{customFormat,date,short}", result);
    }

    @Test
    public void testSeekNonWs() {
        String pattern = "test{0}test";
        ParsePosition pos = new ParsePosition(0);
        int posIndex = ExtendedMessageFormat.this.seekNonWs(pattern, pos);
        assertEquals(0, posIndex);

        posIndex = ExtendedMessageFormat.this.seekNonWs(pattern, pos);
        assertEquals(1, posIndex);

        pos.setIndex(3);
        posIndex = ExtendedMessageFormat.this.seekNonWs(pattern, pos);
        assertEquals(4, posIndex);

        pos.setIndex(6);
        try {
            posIndex = ExtendedMessageFormat.this.seekNonWs(pattern, pos);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Unreadable format element at position 3", e.getMessage());
        }
    }

    @Test
    public void testNext() {
        String pattern = "test{0}test";
        ParsePosition pos = new ParsePosition(0);
        int posIndex = ExtendedMessageFormat.this.next(pos).getIndex();
        assertEquals(1, posIndex);

        posIndex = ExtendedMessageFormat.this.next(pos).getIndex();
        assertEquals(5, posIndex);

        posIndex = ExtendedMessageFormat.this.next(pos).getIndex();
        assertEquals(6, posIndex);

        posIndex = ExtendedMessageFormat.this.next(pos).getIndex();
        assertEquals(7, posIndex);
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatPattern, Locale locale) {
            if (formatName.equals("customFormat")) {
                return new SimpleDateFormat(formatPattern, locale);
            }
            return null;
        }
    }
}