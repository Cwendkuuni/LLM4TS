package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrMatcher;
import org.junit.Test;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}");
        assertEquals("", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}", Locale.ENGLISH);
        assertEquals("{0} {1} {2}", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        assertEquals("{0} {1} {2}", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date} {1} {2}", registry);
        assertEquals("{0, date} {1} {2}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        format.setFormat(0, new DateFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        format.setFormatByArgumentIndex(0, new DateFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        format.setFormats(new Format[]{new DateFormat(), new NumberFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        format.setFormatsByArgumentIndex(new Format[]{new DateFormat(), new NumberFormat()});
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0} {1, number, integer} {2, date, short, 'yyyy-MM-dd'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Collections.emptyMap());
        Object[] args = {"John", 123, new Date(System.currentTimeMillis())};
        String formatted = format.format(args);
        String expected = String.format("%s %d %s", "John", 123, StringUtils.DATE_FORMAT_SHORT.format(new Date(System.currentTimeMillis())));
        assertEquals(expected, formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnreadableFormatElement() {
        String pattern = "{0} {1, unknown} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnterminatedFormatElement() {
        String pattern = "{0} {1,";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Collections.emptyMap());
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date}", registry);
        Format dateFormat = format.getFormat("date");
        assertTrue(dateFormat instanceof DateFormat);
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "{0} {1, 3} {2, integer, 5}";
        ParsePosition pos = new ParsePosition(0);
        int index1 = formatPattern("{0}", pattern, pos).readArgumentIndex(pattern, pos);
        assertEquals(0, index1);
        int index2 = formatPattern("{1, 3}", pattern, pos).readArgumentIndex(pattern, pos);
        assertEquals(1, index2);
        int index3 = formatPattern("{2, integer, 5}", pattern, pos).readArgumentIndex(pattern, pos);
        assertEquals(2, index3);
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "{0} {1, date, short, 'yyyy-MM-dd'}";
        ParsePosition pos = new ParsePosition(0);
        String description1 = formatPattern("{0}", pattern, pos).parseFormatDescription(pattern, pos);
        assertEquals("", description1);
        String description2 = formatPattern("{1, date, short, 'yyyy-MM-dd'}", pattern, pos).parseFormatDescription(pattern, pos);
        assertEquals("date, short, 'yyyy-MM-dd'", description2);
    }

    @Test
    public void testInsertFormats() {
        String pattern = "{0} {1} {2}";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{3, date, medium, 'MM-dd-yyyy'}");
        String formatted = formatPattern(pattern, customPatterns).insertFormats(pattern, customPatterns);
        assertEquals("{0} {1} {2} {3, date, medium, 'MM-dd-yyyy'}", formatted);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0} {1} {3}", Collections.emptyMap());
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(format3));
        assertFalse(format1.equals(format4));
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        format5.setLocale(Locale.ENGLISH);
        assertFalse(format1.equals(format5));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0} {1} {3}", Collections.emptyMap());
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("{0} {1} {2}", Collections.emptyMap());
        format5.setLocale(Locale.ENGLISH);
        int code1 = format1.hashCode();
        int code2 = format2.hashCode();
        int code3 = format3.hashCode();
        int code4 = format4.hashCode();
        int code5 = format5.hashCode();
        assertEquals(code1, code2);
        assertNotEquals(code1, code3);
        assertNotEquals(code1, code4);
        assertNotEquals(code1, code5);
    }

    private ExtendedMessageFormat formatPattern(String pattern, ArrayList<String> customPatterns) {
        if (customPatterns == null) {
            return new ExtendedMessageFormat(pattern);
        }
        Map<String, FormatFactory> registry = new HashMap<>();
        for (String description : customPatterns) {
            registry.put(description.substring(0, description.indexOf(',')), new FormatFactoryStub());
        }
        return new ExtendedMessageFormat(pattern, registry);
    }

    private static class FormatFactoryStub implements FormatFactory {
        @Override
        public Format getFormat(String description, Locale locale) {
            return new MessageFormat("", locale);
        }
    }
}