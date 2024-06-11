package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        String pattern = "Hello, {0} {1} {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        String pattern = "Hello, {0} {1} {2}.";
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        String pattern = "Hello, {0} {1} {2}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertNull(format.getLocale());
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        String pattern = "Hello, {0} {1} {2}.";
        Locale locale = Locale.US;
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0} {1} {2}.");
        format.setFormat(0, new MessageFormat("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0} {1} {2}.");
        format.setFormatByArgumentIndex(0, new MessageFormat("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0} {1} {2}.");
        Format[] formats = {new MessageFormat("test1"), new MessageFormat("test2")};
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0} {1} {2}.");
        Format[] formats = {new MessageFormat("test1"), new MessageFormat("test2")};
        format.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testApplyPatternWithSimplePattern() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String result = format.format(new Object[]{"Test"});
        assertEquals("Hello, Test.", result);
    }

    @Test
    public void testApplyPatternWithComplexPattern() {
        String pattern = "Hello, {0}, {1} ({2}) {3}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", "Doe", "New York", "age: 30"};
        String result = format.format(args);
        assertEquals("Hello, John, Doe (New York) age: 30.", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatArgumentIndex() {
        String pattern = "Hello, {0}, {1} ({2}) {3}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", "Doe", "New York", "age: 30"};
        format.applyPattern("Hello, {4}, {1} ({2}) {3}.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatDescription() {
        String pattern = "Hello, {0}, {1} ({2}) {3}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", "Doe", "New York", "age: 30"};
        format.applyPattern("Hello, {0}, {1} ({2}) {4}.");
    }

    @Test
    public void testGetFormatNull() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}.");
        assertNull(format.getFormat(null));
    }

    @Test
    public void testGetFormatWithValidDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0} {1}.");
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String description, Locale locale, MessageFormat messageFormat) {
                return new MessageFormat(description);
            }
        };
        format.setFormatFactory(new HashMap<String, FormatFactory>().put("test", factory));
        Format result = format.getFormat("test");
        assertEquals(MessageFormat.class, result.getClass());
    }

    @Test
    public void testGetFormatWithInvalidDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}.");
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String description, Locale locale, MessageFormat messageFormat) {
                throw new UnsupportedOperationException();
            }
        };
        format.setFormatFactory(new HashMap<String, FormatFactory>().put("test", factory));
        assertNull(format.getFormat("invalid"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexInvalidFormatArgumentIndex() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        format.readArgumentIndex(pattern, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionInvalidFormatDescription() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription(pattern, pos);
    }

    @Test(expected = ParseException.class)
    public void testInsertFormatsInvalidPattern() {
        String pattern = "Hello, {0}.";
        ArrayList<String> customPatterns = new ArrayList<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.insertFormats(pattern, customPatterns);
    }

    @Test
    public void testInsertFormatsValidPattern() {
        String pattern = "Hello, {0}.";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("test: {1}");
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String result = format.format(new Object[]{"Test", "test"});
        assertEquals("Hello, Test{1}", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeekNonWsInvalidPattern() {
        String pattern = "Hello, {0}.";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat.seekNonWs(pattern, pos);
    }

    @Test
    public void testNextPosition() {
        String pattern = "Hello, {0}.";
        ParsePosition pos = new ParsePosition(0);
        assertEquals(1, ExtendedMessageFormat.next(pos).getIndex());
    }

    @Test
    public void testAppendQuotedStringWithoutEscaping() {
        String pattern = "Hello, {0}.{1}.{2}.";
        StringBuilder appendTo = new StringBuilder();
        ExtendedMessageFormat.appendQuotedString(pattern, new ParsePosition(0), appendTo, false);
        assertEquals("", appendTo.toString());

        pattern = "Hello, '''{0}'''.{1}.{2}.";
        appendTo = new StringBuilder();
        ExtendedMessageFormat.appendQuotedString(pattern, new ParsePosition(0), appendTo, false);
        assertEquals("", appendTo.toString());

        pattern = "Hello, {0}.''.{1}.{2}.";
        appendTo = new StringBuilder();
        ExtendedMessageFormat.appendQuotedString(pattern, new ParsePosition(0), appendTo, false);
        assertEquals("", appendTo.toString());

        pattern = "Hello, {0}.{1}.''.{2}.";
        appendTo = new StringBuilder();
        ExtendedMessageFormat.appendQuotedString(pattern, new ParsePosition(0), appendTo, false);
        assertEquals("", appendTo.toString());

        pattern = "Hello, {0}.'{1}'.{2}.";
        appendTo = new StringBuilder();
        ExtendedMessageFormat.appendQuotedString(pattern, new ParsePosition(0), appendTo, false);
        assertEquals("", appendTo.toString());
    }

    @Test
    public void testAppendQuotedStringWithEscaping() {
        String pattern = "Hello, {0}" + "'" + ".'{1}'" + ".'{2}'.{3}.";
        StringBuilder appendTo = new StringBuilder();
        ExtendedMessageFormat.appendQuotedString(pattern, new ParsePosition(0), appendTo, true);
        assertEquals("Hello, {0}'{1}'{2}'", appendTo.toString());
    }

    @Test
    public void testGetQuotedStringWithoutEscaping() {
        String pattern = "Hello, {0}." + "'" + ".'{1}'" + ".'{2}'.{3}.";
        ParsePosition pos = new ParsePosition(0);
        StringBuilder result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, false);
        assertEquals("", result.toString());

        pattern = "Hello, {0}'" + ".'{1}'" + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, false);
        assertEquals("", result.toString());

        pattern = "Hello, {0}.''." + "'{1}''.'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, false);
        assertEquals("", result.toString());

        pattern = "Hello, {0}'." + "'{1}'." + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, false);
        assertEquals("", result.toString());

        pattern = "Hello, {0}'." + ".'{1}'." + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, false);
        assertEquals("", result.toString());
    }

    @Test
    public void testGetQuotedStringWithEscaping() {
        String pattern = "Hello, {0}" + "'" + ".'{1}'" + ".'{2}'.{3}.";
        ParsePosition pos = new ParsePosition(0);
        StringBuilder result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, true);
        assertEquals("'", result.toString());

        pattern = "Hello, {0}'" + ".'{1}'" + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, true);
        assertEquals("", result.toString());

        pattern = "Hello, {0}.''." + "'{1}''.'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, true);
        assertEquals("", result.toString());

        pattern = "Hello, {0}'." + "'{1}'." + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, true);
        assertEquals("", result.toString());

        pattern = "Hello, {0}'." + ".'{1}'." + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, true);
        assertEquals("", result.toString());

        pattern = "Hello, {0}'" + ".'{1}'" + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, true);
        assertEquals("''", result.toString());

        pattern = "Hello, {0}'" + ".'{1}'" + ".'{2}'.{3}.";
        result = new StringBuilder();
        ExtendedMessageFormat.getQuotedString(pattern, pos, true);
        assertEquals("", result.toString());
    }
}