package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.text.StrMatcher;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor_EmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNotNull(format);
        assertEquals("", format.toPattern());
    }

    @Test
    public void testConstructor_DefaultLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        assertNotNull(format);
        assertEquals("{0} {1}", format.toPattern());
    }

    @Test
    public void testConstructor_CustomLocale() {
        Locale locale = new Locale("fr", "FR");
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", locale);
        assertNotNull(format);
        assertEquals("{0} {1}", format.toPattern());
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testConstructor_CustomRegistry() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new Format() {};
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", registry);
        assertNotNull(format);
        assertEquals("{0}{1}", format.toPattern());
    }

    @Test
    public void testConstructor_CustomRegistryWithLocale() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new Format() {};
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", Locale.US, registry);
        assertNotNull(format);
        assertEquals("{0}{1}", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        assertEquals("{0} {1}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat_FormatElementIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.setFormat(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex_Format() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats_Formats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.setFormats(new Format[] {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex_Formats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.setFormatsByArgumentIndex(new Format[] {});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0} {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0} {1}");
        assertTrue(format1.equals(format2));

        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0} {1}", Locale.US);
        assertTrue(format1.equals(format3));

        MessageFormat format4 = new MessageFormat("{0} {1}");
        assertFalse(format1.equals(format4));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0} {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0} {1}");
        assertEquals(format1.hashCode(), format2.hashCode());

        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0} {1}", Locale.US);
        assertEquals(format1.hashCode(), format3.hashCode());

        MessageFormat format4 = new MessageFormat("{0} {1}");
        assertNotEquals(format1.hashCode(), format4.hashCode());
    }

    @Test
    public void testGetFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new Format() {};
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", registry);
        Format result = format.getFormat("CustomFormat");
        assertNotNull(result);
    }

    @Test
    public void testGetFormat_NullName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", registry);
        Format result = format.getFormat(null);
        assertNull(result);
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "{0}{1}";
        ParsePosition pos = new ParsePosition(0);
        int result = new ExtendedMessageFormat(pattern).readArgumentIndex(pattern, pos);
        assertEquals(0, result);

        pos.setIndex(6);
        result = new ExtendedMessageFormat(pattern).readArgumentIndex(pattern, pos);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndex_InvalidFormatArgumentIndex() {
        String pattern = "{0}{1}";
        ParsePosition pos = new ParsePosition(0);
        new ExtendedMessageFormat(pattern).readArgumentIndex(pattern, pos);
        pos.setIndex(11);
        throw new AssertionError();
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "{0} {{1}}";
        ParsePosition pos = new ParsePosition(0);
        String result = new ExtendedMessageFormat(pattern).parseFormatDescription(pattern, pos);
        assertEquals("{1}", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescription_UnterminatedFormatElement() {
        String pattern = "{0} {{1}";
        ParsePosition pos = new ParsePosition(0);
        new ExtendedMessageFormat(pattern).parseFormatDescription(pattern, pos);
    }

    @Test
    public void testInsertFormats() {
        String pattern = "{0} {{1}}";
        ArrayList<String> customPatterns = new ArrayList<>(Arrays.asList("{format1}", "{format2}"));
        String result = new ExtendedMessageFormat(pattern).insertFormats(pattern, customPatterns);
        assertEquals("{0} {{1}}{format1}{format2}", result);
    }

    @Test
    public void testInsertFormats_EmptyCustomPatterns() {
        String pattern = "{0} {{1}}";
        ArrayList<String> customPatterns = new ArrayList<>();
        String result = new ExtendedMessageFormat(pattern).insertFormats(pattern, customPatterns);
        assertEquals("{0} {{1}}", result);
    }

    @Test
    public void testSeekNonWs() {
        String pattern = "{0} {{1}}";
        ParsePosition pos = new ParsePosition(0);
        new ExtendedMessageFormat(pattern).seekNonWs(pattern, pos);
        assertEquals(0, pos.getIndex());

        pos.setIndex(5);
        new ExtendedMessageFormat(pattern).seekNonWs(pattern, pos);
        assertEquals(5, pos.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeekNonWs_InvalidPattern() {
        String pattern = "{0} {{1}";
        ParsePosition pos = new ParsePosition(0);
        new ExtendedMessageFormat(pattern).seekNonWs(pattern, pos);
    }

    @Test
    public void testNext() {
        String pattern = "{0} {{1}}";
        ParsePosition pos = new ParsePosition(5);
        new ExtendedMessageFormat(pattern).next(pos);
        assertEquals(6, pos.getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        String pattern = "{0} '{{1}}'";
        StringBuilder appendTo = new StringBuilder();
        new ExtendedMessageFormat(pattern).appendQuotedString(pattern, new ParsePosition(0), appendTo, false);
        assertEquals("", appendTo.toString());

        appendTo.setLength(0);
        pattern = "{0} '{{1}}'";
        pos.setIndex(11);
        new ExtendedMessageFormat(pattern).appendQuotedString(pattern, pos, appendTo, false);
        assertEquals("1", appendTo.toString());

        appendTo.setLength(0);
        pattern = "{0} '{{1}}'";
        pos.setIndex(11);
        new ExtendedMessageFormat(pattern).appendQuotedString(pattern, pos, appendTo, true);
        assertEquals("1'", appendTo.toString());

        appendTo.setLength(0);
        pattern = "{0} '{{1}}'";
        pos.setIndex(11);
        new ExtendedMessageFormat(pattern).appendQuotedString(pattern, pos, appendTo, true);
        assertEquals("1''", appendTo.toString());

        appendTo.setLength(0);
        pattern = "{0} '{{1}}'";
        pos.setIndex(11);
        new ExtendedMessageFormat(pattern).appendQuotedString(pattern, pos, appendTo, true);
        assertEquals("1''", appendTo.toString());
    }

    @Test
    public void testGetQuotedString() {
        String pattern = "{0} '{{{1}}}'";
        ParsePosition pos = new ParsePosition(0);
        new ExtendedMessageFormat(pattern).getQuotedString(pattern, pos, false);

        pos.setIndex(11);
        String result = new ExtendedMessageFormat(pattern).getQuotedString(pattern, pos, false).toString();
        assertEquals("", result);

        pos.setIndex(16);
        result = new ExtendedMessageFormat(pattern).getQuotedString(pattern, pos, true).toString();
        assertEquals("}", result);

        pos.setIndex(16);
        result = new ExtendedMessageFormat(pattern).getQuotedString(pattern, pos, false).toString();
        assertEquals("}", result);

        pos.setIndex(16);
        result = new ExtendedMessageFormat(pattern).getQuotedString(pattern, pos, false).toString();
        assertEquals("}", result);

        pos.setIndex(16);
        result = new ExtendedMessageFormat(pattern).getQuotedString(pattern, pos, true).toString();
        assertEquals("}", result);
    }

    @Test
    public void testContainsElements() {
        ArrayList<String> coll = new ArrayList<>();
        assertTrue(new ExtendedMessageFormat("").containsElements(coll));

        coll.add("CustomFormat");
        assertTrue(new ExtendedMessageFormat("{0}{1}").containsElements(coll));

        coll.add(null);
        assertFalse(new ExtendedMessageFormat("{0}{1}").containsElements(coll));
    }

}