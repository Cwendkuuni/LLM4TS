package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.junit.Assert;
import org.junit.Test;

import java.text.Format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    

    @Test
    public void testExtendedMessageFormatConstructorWithPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Assert.assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        Assert.assertEquals(pattern, format.toPattern());
        Assert.assertEquals(locale, format.getLocale());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        Assert.assertEquals(pattern, format.toPattern());
        Assert.assertNull(format.getLocale());
        Assert.assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocaleAndRegistry() {
        String pattern = "Hello {0}, your age is {1}.";
        Locale locale = Locale.US;
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        Assert.assertEquals(pattern, format.toPattern());
        Assert.assertEquals(locale, format.getLocale());
        Assert.assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testToPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Assert.assertEquals(pattern, format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormat(0, new MessageFormat(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatByArgumentIndex(0, new MessageFormat(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = new Format[1];
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = new Format[1];
        format.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testEquals() {
        String pattern1 = "Hello {0}, your age is {1}.";
        String pattern2 = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern1);
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertEquals(format1, format1);
        Assert.assertEquals(format1, format3);
        Assert.assertEquals(format1.hashCode(), format3.hashCode());
        Assert.assertNotEquals(format1, obj1);
        Assert.assertNotEquals(format1, obj2);
    }

    @Test
    public void testGetFormat() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new MessageFormat(arg);
            }
        };
        registry.put("date", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        Assert.assertNull(format.getFormat("invalidName"));
        Format dateFormat = format.getFormat("date");
        Assert.assertEquals("{0}", dateFormat.getPattern());
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "{0},{1},{2}";
        ParsePosition pos = new ParsePosition(0);
        int argIndex = 0;
        Assert.assertEquals(argIndex, format.readArgumentIndex(pattern, pos));
        pos.setIndex(3);
        Assert.assertEquals(1, format.readArgumentIndex(pattern, pos));
        pos.setIndex(8);
        Assert.assertEquals(2, format.readArgumentIndex(pattern, pos));
        pos.setIndex(10);
        Assert.assertEquals(-1, format.readArgumentIndex(pattern, pos));
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "{0,date} {1,number,number}";
        ParsePosition pos = new ParsePosition(0);
        String formatDesc = "date";
        Assert.assertEquals(formatDesc, format.parseFormatDescription(pattern, pos));
        pos.setIndex(5);
        formatDesc = "number,number";
        Assert.assertEquals(formatDesc, format.parseFormatDescription(pattern, pos));
    }

    @Test
    public void testInsertFormats() {
        String pattern = "{0} {1}";
        ArrayList<String> customPatterns = new ArrayList<>(Arrays.asList("{0,time}", "{1,date}"));
        String formattedPattern = format.insertFormats(pattern, customPatterns);
        Assert.assertEquals("{0} {1,time} {1,date}", formattedPattern);
    }

    @Test
    public void testSeekNonWs() {
        String pattern = "{0} {1}";
        ParsePosition pos = new ParsePosition(0);
        int index = 0;
        format.seekNonWs(pattern, pos);
        Assert.assertEquals(0, pos.getIndex());
        format.next(pos);
        Assert.assertEquals(1, pos.getIndex());
        format.next(pos);
        Assert.assertEquals(2, pos.getIndex());
        format.next(pos);
        Assert.assertEquals(3, pos.getIndex());
        format.next(pos);
        index = pos.getIndex();
        Assert.assertEquals(5, index);
    }

    @Test
    public void testNext() {
        String pattern = "{0} {1}";
        ParsePosition pos = new ParsePosition(0);
        format.next(pos);
        Assert.assertEquals(1, pos.getIndex());
        format.next(pos);
        Assert.assertEquals(2, pos.getIndex());
        format.next(pos);
        Assert.assertEquals(3, pos.getIndex());
        format.next(pos);
        Assert.assertEquals(4, pos.getIndex());
        format.next(pos);
        Assert.assertEquals(5, pos.getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        String pattern = "{'Hello'}";
        StringBuilder sb = new StringBuilder();
        format.appendQuotedString(pattern, new ParsePosition(0), sb, false);
        Assert.assertEquals("Hello", sb.toString());

        pattern = "{'Hello'{'World'}}";
        sb = new StringBuilder();
        format.appendQuotedString(pattern, new ParsePosition(0), sb, false);
        Assert.assertEquals("HelloWorld", sb.toString());
    }

    @Test
    public void testGetQuotedString() {
        String pattern = "{'Hello'}";
        String result = null;
        format.getQuotedString(pattern, new ParsePosition(0), false);
        Assert.assertEquals("Hello", result);

        pattern = "{'Hello'{'World'}}";
        result = null;
        format.getQuotedString(pattern, new ParsePosition(0), false);
        Assert.assertEquals("HelloWorld", result);
    }

    @Test
    public void testContainsElements() {
        Collection<String> coll = Arrays.asList("date", "time");
        Assert.assertTrue(format.containsElements(coll));

        coll = Arrays.asList("invalidName", "date");
        Assert.assertFalse(format.containsElements(coll));

        coll = null;
        Assert.assertFalse(format.containsElements(coll));
    }

    
}