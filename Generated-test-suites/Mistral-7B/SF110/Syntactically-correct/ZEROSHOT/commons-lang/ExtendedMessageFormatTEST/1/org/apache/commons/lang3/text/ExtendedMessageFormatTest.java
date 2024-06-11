package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat format;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        formatFactory = new TestFormatFactory();
    }

    @Test
    public void testDefaultConstructor() {
        try {
            format = new ExtendedMessageFormat("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorsWithPattern() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());
        assertEquals(pattern, format.getFormat());
    }

    @Test
    public void testConstructorsWithLocale() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals("", format.toPattern());
        assertEquals(pattern, format.getFormat());
    }

    @Test
    public void testConstructorsWithPatternAndRegistry() {
        String pattern = "Hello, {0}! Your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("IntegerFormat", formatFactory);
        format = new ExtendedMessageFormat(pattern, registry);
        assertEquals("", format.toPattern());
        assertEquals(pattern, format.getFormat());
        assertEquals(formatFactory, format.getFormat("IntegerFormat"));
    }

    @Test
    public void testConstructorsWithPatternLocaleAndRegistry() {
        String pattern = "Hello, {0}! Your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("IntegerFormat", formatFactory);
        format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals("", format.toPattern());
        assertEquals(pattern, format.getFormat());
        assertEquals(formatFactory, format.getFormat("IntegerFormat"));
    }

    @Test
    public void testToPattern() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello, {1}! Your age is {0}.");
        assertEquals("Hello, {1}! Your age is {0}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern);
        format.setFormat(0, new TestFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern);
        format.setFormatByArgumentIndex(1, new TestFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern);
        format.setFormats(new TestFormat[]{});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern);
        format.setFormatsByArgumentIndex(new TestFormat[]{});
    }

    @Test
    public void testEquals() {
        String pattern = "Hello, {0}! Your age is {1}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(pattern, Locale.US);
        ExtendedMessageFormat format5 = new ExtendedMessageFormat(pattern, null, Collections.singletonMap("IntegerFormat", formatFactory));

        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(format3));
        assertFalse(format1.equals(format4));
        assertFalse(format1.equals(format5));
    }

    @Test
    public void testHashCode() {
        String pattern = "Hello, {0}! Your age is {1}.";
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(pattern, Locale.US);
        ExtendedMessageFormat format5 = new ExtendedMessageFormat(pattern, null, Collections.singletonMap("IntegerFormat", formatFactory));

        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        int hashCode3 = format3.hashCode();
        int hashCode4 = format4.hashCode();
        int hashCode5 = format5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
    }

    @Test
    public void testGetFormat() {
        String pattern = "Hello, {0}! Your age is {1}.";
        format = new ExtendedMessageFormat(pattern, null, Collections.singletonMap("IntegerFormat", formatFactory));

        Format format0 = format.getFormat(0);
        Format format1 = format.getFormat(1);
        Format format2 = format.getFormat("IntegerFormat");

        assertNull(format0);
        assertNotNull(format1);
        assertNotNull(format2);
        assertEquals(formatFactory, format2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndex() {
        String pattern = "Hello, {0}! Your age is {1}.";
        ParsePosition pos = new ParsePosition(0);

        format.applyPattern(pattern);

        format.readArgumentIndex(pattern, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescription() {
        String pattern = "Hello, {0}! Your age is {1}.";
        ParsePosition pos = new ParsePosition(0);

        format.applyPattern(pattern);

        format.parseFormatDescription(pattern, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertFormats() {
        String pattern = "Hello, {0}! Your age is {1}.";
        ArrayList<String> customPatterns = new ArrayList<>();

        format = new ExtendedMessageFormat(pattern);
        String result = format.insertFormats(pattern, customPatterns);

        assertEquals(pattern, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeekNonWs() {
        String pattern = "Hello, {0}! Your age is {1}.";
        ParsePosition pos = new ParsePosition(0);

        format.applyPattern(pattern);

        format.seekNonWs(pattern, pos);
    }

    @Test(expected = ParseException.class)
    public void testNext() throws ParseException {
        String pattern = "Hello, {0}! Your age is {1}.";
        ParsePosition pos = new ParsePosition(0);

        format.applyPattern(pattern);

        format.next(pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendQuotedString() {
        String pattern = "Hello, {0}! Your age is {1}.";
        StringBuilder appendTo = new StringBuilder();

        format.applyPattern(pattern);

        format.appendQuotedString(pattern, new ParsePosition(0), appendTo, false);

        assertEquals("", appendTo.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetQuotedString() {
        String pattern = "Hello, {0}! Your age is {1}.";
        ParsePosition pos = new ParsePosition(0);

        format.applyPattern(pattern);

        format.getQuotedString(pattern, pos, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsElements() {
        Collection<String> coll = new ArrayList<>();

        format.containsElements(coll);
    }
}

class TestFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String argument, Locale locale) throws ParseException {
        if ("TestFormat".equals(name)) {
            return new TestFormat();
        }
        throw new ParseException("Format not found: " + name);
    }
}

class TestFormat implements Format {
    @Override
    public String format(Object obj, Locale locale) throws IllegalFormatException {
        return "Test";
    }
}