package org.apache.commons.lang3.text; // Make sure to import necessary classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.Format;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    

    @Test
    public void testExtendedMessageFormatConstructorWithPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1}");
        assertNotNull(messageFormat);
        assertEquals("", messageFormat.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertNotNull(messageFormat);
        assertEquals("", messageFormat.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1}", registry);
        assertNotNull(messageFormat);
        assertEquals("", messageFormat.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1}", Locale.US, registry);
        assertNotNull(messageFormat);
        assertEquals("", messageFormat.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("{0}, {1}", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat("{0}, {1}", new HashMap<>());
        assertEquals("{0}, {1}", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat("{0}, {1}", Locale.US, new HashMap<>());
        assertEquals("{0}, {1}", messageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1}");
        String[] args = {"Hello", "World"};
        Object[] results = messageFormat.format(args);
        assertEquals(2, results.length);
        assertEquals("Hello, World", results[0].toString());

        messageFormat.applyPattern("{1}, {0}");
        results = messageFormat.format(args);
        assertEquals(2, results.length);
        assertEquals("World, Hello", results[0].toString());

        messageFormat.applyPattern("{0}, {1}, {2}");
        args = new String[]{"Hello", "World", "commas"};
        results = messageFormat.format(args);
        assertEquals(3, results.length);
        assertEquals("Hello, World, commas", results[0].toString());
    }

    @Test
    public void testUnsupportedOperations() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}, {1}");
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormat(1, new SimpleDateFormat()));
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormatByArgumentIndex(1, new SimpleDateFormat()));
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormats(new Format[0]));
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormatsByArgumentIndex(new Format[0]));
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd/MM/yyyy"));
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{dd/MM/yyyy}", Locale.US, registry);
        Format result = messageFormat.getFormat("date");
        assertNotNull(result);
        assertTrue(result instanceof SimpleDateFormat);
        assertEquals("dd/MM/yyyy", result.toPattern());

        try {
            messageFormat.getFormat("nonExistentFormat");
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "{0, number, integer}{1, number, integer}";
        ParsePosition pos = new ParsePosition(0);
        int index = ExtendedMessageFormat.class.getDeclaredMethod("readArgumentIndex", String.class, ParsePosition.class).invoke(null, pattern, pos);
        assertEquals(2, index);

        pattern = "{0, number, integer}";
        pos = new ParsePosition(0);
        index = ExtendedMessageFormat.class.getDeclaredMethod("readArgumentIndex", String.class, ParsePosition.class).invoke(null, pattern, pos);
        assertEquals(1, index);

        pattern = "{}";
        pos = new ParsePosition(0);
        assertThrows(IllegalArgumentException.class, () -> ExtendedMessageFormat.class.getDeclaredMethod("readArgumentIndex", String.class, ParsePosition.class).invoke(null, pattern, pos));
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "{0, number, integer, date}";
        ParsePosition pos = new ParsePosition(0);

        String customPattern = ExtendedMessageFormat.class.getDeclaredMethod("parseFormatDescription", String.class, ParsePosition.class).invoke(null, pattern, pos).toString();
        assertEquals("date", customPattern);

        pattern = "{0, number, integer}";
        customPattern = (String) ExtendedMessageFormat.class.getDeclaredMethod("parseFormatDescription", String.class, ParsePosition.class).invoke(null, pattern, pos);
        assertEquals(null, customPattern);
    }

    @Test
    public void testInsertFormats() {
        String pattern = "{0}, {1}";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{dd/MM/yyyy}");
        String formattedPattern = ExtendedMessageFormat.class.getDeclaredMethod("insertFormats", String.class, ArrayList.class).invoke(null, pattern, customPatterns);
        assertEquals("{0}, {1}, {dd/MM/yyyy}", formattedPattern);

        pattern = "{0}, {1}";
        customPatterns = new ArrayList<>();
        formattedPattern = (String) ExtendedMessageFormat.class.getDeclaredMethod("insertFormats", String.class, ArrayList.class).invoke(null, pattern, customPatterns);
        assertEquals(pattern, formattedPattern);
    }

    @Test
    public void testSeekNonWs() {
        String pattern = "{0, number, integer}, {1, number, integer}";
        ParsePosition pos = new ParsePosition(0);
        int index = 0;

        index = ExtendedMessageFormat.class.getDeclaredMethod("seekNonWs", String.class, ParsePosition.class).invoke(null, pattern, pos).hashCode();
        assertEquals(0, index);

        pattern = "{0, number, integer}, {1, number, integer}";
        pos = new ParsePosition(2);
        index = ExtendedMessageFormat.class.getDeclaredMethod("seekNonWs", String.class, ParsePosition.class).invoke(null, pattern, pos).hashCode();
        assertEquals(5, index);

        pattern = "{0, number, integer}, {1, number, integer}";
        pos = new ParsePosition(3);
        assertThrows(IllegalArgumentException.class, () -> ExtendedMessageFormat.class.getDeclaredMethod("seekNonWs", String.class, ParsePosition.class).invoke(null, pattern, pos));
    }

    @Test
    public void testNext() {
        String pattern = "{0, number, integer}, {1, number, integer}";
        ParsePosition pos = new ParsePosition(0);

        int index = ExtendedMessageFormat.class.getDeclaredMethod("next", ParsePosition.class).invoke(null, pos).hashCode();
        assertEquals(1, index);

        pattern = "{0, number, integer}, {1, number, integer}";
        pos = new ParsePosition(1);
        index = ExtendedMessageFormat.class.getDeclaredMethod("next", ParsePosition.class).invoke(null, pos).hashCode();
        assertEquals(3, index);

        pattern = "{0, number, integer}, {1, number, integer}";
        pos = new ParsePosition(5);
        assertThrows(ParseException.class, () -> ExtendedMessageFormat.class.getDeclaredMethod("next", ParsePosition.class).invoke(null, pos));
    }

    @Test
    public void testAppendQuotedString() {
        String pattern = "{'Hello''}";
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();

        ExtendedMessageFormat.class.getDeclaredMethod("appendQuotedString", String.class, ParsePosition.class, StringBuilder.class).invoke(null, pattern, pos, appendTo);
        assertEquals("Hello", appendTo.toString());

        pattern = "{'Hello'''}";
        appendTo = new StringBuilder();
        ExtendedMessageFormat.class.getDeclaredMethod("appendQuotedString", String.class, ParsePosition.class, StringBuilder.class).invoke(null, pattern, pos, appendTo);
        assertEquals("Hello''", appendTo.toString());

        pattern = "{'{}'}";
        appendTo = new StringBuilder();
        ExtendedMessageFormat.class.getDeclaredMethod("appendQuotedString", String.class, ParsePosition.class, StringBuilder.class).invoke(null, pattern, pos, appendTo);
        assertEquals("{}", appendTo.toString());
    }

    @Test
    public void testGetQuotedString() {
        String pattern = "{'Hello''}";
        ParsePosition pos = new ParsePosition(0);

        String result = ExtendedMessageFormat.class.getDeclaredMethod("getQuotedString", String.class, ParsePosition.class, boolean.class).invoke(null, pattern, pos, false).toString();
        assertEquals("Hello", result);

        pattern = "{'{}'}";
        result = (String) ExtendedMessageFormat.class.getDeclaredMethod("getQuotedString", String.class, ParsePosition.class, boolean.class).invoke(null, pattern, pos, false);
        assertEquals("{}", result);

        pattern = "{'Hello'''}";
        result = (String) ExtendedMessageFormat.class.getDeclaredMethod("getQuotedString", String.class, ParsePosition.class, boolean.class).invoke(null, pattern, pos, false);
        assertEquals("Hello''", result);
    }

    @Test
    public void testContainsElements() {
        Collection<String> collection = new ArrayList<>();
        assertTrue(ExtendedMessageFormat.class.getDeclaredMethod("containsElements", Collection.class).invoke(null, null));
        assertTrue(ExtendedMessageFormat.class.getDeclaredMethod("containsElements", Collection.class).invoke(null, collection));
        assertFalse(ExtendedMessageFormat.class.getDeclaredMethod("containsElements", Collection.class).invoke(null, new String[]{"Hello", "World"}));
    }

    
}