package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.SimpleDateFormat;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private static final String DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm:ss.SSS";
    private static final String DATE_FORMAT_PATTERN_WITH_CUSTOM = "dd-MM-{0,date,yyyy} HH:mm:ss.SSS";
    private static final String EMPTY_PATTERN = "";
    private static final String INVALID_PATTERN = "invalidPattern{";
    private static final String INVALID_FORMAT_DESCRIPTION = "invalidFormatDescription,dd";
    private static final String MISSING_FORMAT_DESCRIPTION = "{0,date}";

    private FormatFactory dateFormatFactory;
    private ExtendedMessageFormat extendedMessageFormat;
    private MessageFormat messageFormat;

    @Before
    public void setup() {
        dateFormatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("date")) {
                    return new SimpleDateFormat(DATE_FORMAT_PATTERN, locale);
                }
                return null;
            }
        };
    }

    @Test
    public void testConstructorWithEmptyPattern() {
        extendedMessageFormat = new ExtendedMessageFormat(EMPTY_PATTERN);
        assertEquals(EMPTY_PATTERN, extendedMessageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        assertEquals("{0,date} {1}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", dateFormatFactory);
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault(), registry);
        assertEquals("{0,date} {1}", extendedMessageFormat.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithNullPattern() {
        extendedMessageFormat = new ExtendedMessageFormat(null);
        extendedMessageFormat.applyPattern("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        extendedMessageFormat = new ExtendedMessageFormat(INVALID_PATTERN, Locale.getDefault());
        extendedMessageFormat.applyPattern("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatDescription() {
        extendedMessageFormat = new ExtendedMessageFormat(MISSING_FORMAT_DESCRIPTION, Locale.getDefault());
        extendedMessageFormat.applyPattern("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatDescription() {
        extendedMessageFormat = new ExtendedMessageFormat(INVALID_FORMAT_DESCRIPTION, Locale.getDefault());
        extendedMessageFormat.applyPattern("");
    }

    @Test
    public void testSetLocale() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.US);
        assertEquals("{0,date} {1}", extendedMessageFormat.toPattern());
        extendedMessageFormat.setLocale(Locale.CHINA);
        assertEquals("{0,date} {1}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testToPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        assertEquals("{0,date} {1}", extendedMessageFormat.toPattern());
        extendedMessageFormat.applyPattern(DATE_FORMAT_PATTERN_WITH_CUSTOM);
        assertEquals("dd-MM-{0,date,yyyy} HH:mm:ss.SSS {1}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", dateFormatFactory);
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault(), registry);
        extendedMessageFormat.applyPattern(EMPTY_PATTERN);
        assertEquals("{0,date} {1}", extendedMessageFormat.toPattern());
        extendedMessageFormat.applyPattern(DATE_FORMAT_PATTERN_WITH_CUSTOM);
        assertEquals("dd-MM-{0,date,yyyy} HH:mm:ss.SSS {1}", extendedMessageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        extendedMessageFormat.setFormat(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        extendedMessageFormat.setFormatByArgumentIndex(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        Format[] newFormats = new Format[1];
        extendedMessageFormat.setFormats(newFormats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        Format[] newFormats = new Format[1];
        extendedMessageFormat.setFormatsByArgumentIndex(newFormats);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("{0,date} {1}", Locale.US);
        assertEquals(extendedMessageFormat1, extendedMessageFormat1);
        assertEquals(extendedMessageFormat1, extendedMessageFormat2);
        assertNotEquals(extendedMessageFormat1, extendedMessageFormat3);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("{0,date} {1}", Locale.US);
        int hashCode1 = extendedMessageFormat1.hashCode();
        int hashCode2 = extendedMessageFormat2.hashCode();
        int hashCode3 = extendedMessageFormat3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testGetFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        assertNull(extendedMessageFormat.getFormat(null));
        assertNull(extendedMessageFormat.getFormat(""));
        assertNull(extendedMessageFormat.getFormat("date"));
        assertEquals(dateFormatFactory.getFormat("date", null, Locale.getDefault()), extendedMessageFormat.getFormat("date"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithInvalidPattern() {
        extendedMessageFormat = new ExtendedMessageFormat(INVALID_PATTERN, Locale.getDefault());
        extendedMessageFormat.applyPattern("");
        int index = extendedMessageFormat.readArgumentIndex(INVALID_PATTERN, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithInvalidIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        int index = extendedMessageFormat.readArgumentIndex(INVALID_PATTERN, pos);
    }

    @Test
    public void testParseFormatDescriptionWithValidFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        String formatDescription = "date";
        assertEquals(formatDescription, extendedMessageFormat.parseFormatDescription(DATE_FORMAT_PATTERN, new ParsePosition(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        String formatDescription = "invalidFormatDescription";
        ParsePosition pos = new ParsePosition(0);
        extendedMessageFormat.parseFormatDescription(INVALID_PATTERN, pos);
    }

    @Test
    public void testInsertFormats() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        String customPattern = "customPattern";
        ArrayList<String> customPatterns = new ArrayList<>(Collections.singletonList(customPattern));
        String result = extendedMessageFormat.insertFormats(DATE_FORMAT_PATTERN, customPatterns);
        assertEquals("dd-MM-{0,date,yyyy} HH:mm:ss.SSS {1}", result);
    }

    @Test
    public void testInsertFormatsWithEmptyCustomPatterns() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1}", Locale.getDefault());
        ArrayList<String> customPatterns = new ArrayList<>();
        String result = extendedMessageFormat.insertFormats(DATE_FORMAT_PATTERN, customPatterns);
        assertEquals("{0,date} {1}", result);
    }

    @Test
    public void testContainsElements() {
        ArrayList<String> customPatterns = new ArrayList<>();
        assertTrue(extendedMessageFormat.containsElements(customPatterns));
        customPatterns.clear();
        customPatterns.add("date");
        customPatterns.add("customPattern");
        assertTrue(extendedMessageFormat.containsElements(customPatterns));
        customPatterns.clear();
        customPatterns.add(null);
        assertFalse(extendedMessageFormat.containsElements(customPatterns));
    }
}