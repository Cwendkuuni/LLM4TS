package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.Format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExtendedMessageFormatTest {

    private static final String BASE_PATTERN = "{0}, {1}{2}";
    private static final String FORMAT_PATTERN = "{}";
    private static final Locale LOCALE = Locale.ENGLISH;
    private static final Map<String, FormatFactory> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("DATE_FORMAT", new DateFormatFactory());
    }

    private String pattern;
    private Locale locale;
    private Map<String, FormatFactory> registry;

    public ExtendedMessageFormatTest(String pattern, Locale locale, Map<String, FormatFactory> registry) {
        this.pattern = pattern;
        this.locale = locale;
        this.registry = registry;
    }

    @Parameters
    public static Object[][] data() {
        return new Object[][]{
            { BASE_PATTERN, LOCALE, REGISTRY },
            { "", LOCALE, REGISTRY },
            { null, LOCALE, REGISTRY },
            { null, null, REGISTRY }
        };
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
        assertNull(format.getFormat(0));
        assertNull(format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
        assertNull(format.getFormat(0));
        assertNull(format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        format.setFormat(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(pattern, format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithNullPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(null, locale, registry);
        try {
            format.applyPattern(pattern);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        format.applyPattern(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        String invalidPattern = pattern + "X";
        try {
            format.applyPattern(invalidPattern);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        format.applyPattern(pattern);
        Validate.isNotNull(format.getFormat(0));
    }

    @Test
    public void testGetFormatWithNullDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        format.applyPattern(pattern);
        assertNull(format.getFormat(null));
    }

    @Test
    public void testGetFormatWithEmptyDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        format.applyPattern(pattern);
        assertNull(format.getFormat(""));
    }

    @Test
    public void testGetFormatWithInvalidFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        format.applyPattern(pattern);
        assertNull(format.getFormat("FORMAT"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithNullPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(null, locale, registry);
        ParsePosition pos = new ParsePosition(0);
        try {
            format.readArgumentIndex(null, pos);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", locale, registry);
        ParsePosition pos = new ParsePosition(0);
        try {
            format.readArgumentIndex(pattern, pos);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseFormatDescriptionWithNullPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(null, locale, registry);
        ParsePosition pos = new ParsePosition(0);
        try {
            format.parseFormatDescription(null, pos);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        ParsePosition pos = new ParsePosition(0);
        String formatDescription = format.parseFormatDescription(pattern, pos);
        assertEquals(FORMAT_PATTERN, formatDescription);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        ParsePosition pos = new ParsePosition(0);
        String invalidPattern = pattern + "X";
        try {
            format.parseFormatDescription(invalidPattern, pos);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        ArrayList<String> customPatterns = new ArrayList<>();
        String customPattern = "DATE_FORMAT";
        customPatterns.add(customPattern);
        assertEquals(pattern, format.insertFormats(pattern, customPatterns));
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(BASE_PATTERN, locale, registry);
        format2.applyPattern(BASE_PATTERN);
        assertEquals(pattern, format2.toPattern());
        Format dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        REGISTRY.put(customPattern, new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return dateFormat;
            }
        });
        format2 = new ExtendedMessageFormat(BASE_PATTERN, locale, registry);
        format2.applyPattern(BASE_PATTERN);
        assertEquals( pattern.replaceAll("{}", "dd-MM-yyyy"), format2.format(new Object[] { "2022-09-01" }));
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern, locale, registry);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern, locale, registry);
        assertTrue(format1.equals(format2));
        format2.applyPattern(FORMAT_PATTERN);
        assertFalse(format1.equals(format2));
        format1.applyPattern(FORMAT_PATTERN);
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern, locale, registry);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern, locale, registry);
        int hashcode1 = format1.hashCode();
        format2.applyPattern(FORMAT_PATTERN);
        assertNotSame(hashcode1, format2.hashCode());
        format1.applyPattern(FORMAT_PATTERN);
        assertEquals(hashcode1, format1.hashCode());
    }
}

class FormatFactory {
    @SuppressWarnings("unchecked")
    public Format getFormat(String name, String argument, Locale locale) {
        if ("DATE_FORMAT".equals(name)) {
            return new SimpleDateFormat("dd-MM-yyyy");
        }
        throw new UnsupportedOperationException();
    }
}