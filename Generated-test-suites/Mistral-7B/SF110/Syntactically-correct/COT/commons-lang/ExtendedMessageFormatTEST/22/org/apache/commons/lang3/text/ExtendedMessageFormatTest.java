package org.apache.commons.lang3.text; // Make sure you import the required classes

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

@RunWith(Parameterized.class)
public class ExtendedMessageFormatTest {
    private static final String DUMMY_PATTERN = "";
    private static final String ESCAPED_QUOTE = "''";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE = '{';
    private static final char QUOTE = '\'';

    private ExtendedMessageFormat extendedMessageFormat;
    private MessageFormat messageFormat;
    private Map<String, FormatFactory> registry;

    public ExtendedMessageFormatTest(Map<String, FormatFactory> registry) {
        this.registry = registry;
    }

    @Before
    public void setUp() {
        this.messageFormat = new MessageFormat("");
        this.extendedMessageFormat = new ExtendedMessageFormat("", Locale.getDefault(), this.registry);
    }

    @Test
    public void testConstructorWithEmptyPattern() {
        Assert.assertNotNull(this.extendedMessageFormat);
    }

    @Test
    public void testConstructorWithPatternAndDefaultLocale() {
        Assert.assertNotNull(this.extendedMessageFormat);
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        Assert.assertNotNull(this.extendedMessageFormat);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Assert.assertNotNull(this.extendedMessageFormat);
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        Assert.assertNotNull(this.extendedMessageFormat);
    }

    @Test
    public void testGetToPattern() {
        Assert.assertNotNull(this.extendedMessageFormat.toPattern());
        Assert.assertEquals("", this.extendedMessageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupportedOperationException() {
        this.extendedMessageFormat.setFormat(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupportedOperationException() {
        this.extendedMessageFormat.setFormatByArgumentIndex(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupportedOperationException() {
        Format[] origFormats = new Format[1];
        this.extendedMessageFormat.setFormats(origFormats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupportedOperationException() {
        Format[] origFormats = new Format[1];
        this.extendedMessageFormat.setFormatsByArgumentIndex(origFormats);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("", Locale.getDefault(), this.registry);
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("", Locale.getDefault(), this.registry);
        ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("", Locale.getDefault(), null);

        Assert.assertEquals(extendedMessageFormat1, extendedMessageFormat1);
        Assert.assertEquals(extendedMessageFormat1, extendedMessageFormat2);
        Assert.assertNotEquals(extendedMessageFormat1, extendedMessageFormat3);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("", Locale.getDefault(), this.registry);
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("", Locale.getDefault(), this.registry);
        ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("", Locale.getDefault(), null);

        Assert.assertEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat1.hashCode());
        Assert.assertEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat2.hashCode());
        Assert.assertNotEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat3.hashCode());
    }

    @Test
    public void testGetFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("yyyy-MM-dd");
            }
        };
        this.registry.put("date", formatFactory);

        Assert.assertNull(this.extendedMessageFormat.getFormat(0, null));
        Assert.assertNull(this.extendedMessageFormat.getFormat("EmptyName", null));
        Format format = this.extendedMessageFormat.getFormat("date", null);
        Assert.assertNotNull(format);
        Assert.assertEquals("yyyy-MM-dd", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexIllegalArgumentException() {
        String pattern = "{0,}" + START_FMT + "{1}" + START_FMT + "}";
        ParsePosition pos = new ParsePosition(0);

        this.extendedMessageFormat.applyPattern(pattern);
        this.extendedMessageFormat.readArgumentIndex(pattern, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionIllegalArgumentException() {
        String pattern = "{0," + START_FE + "{1}" + END_FE + "}";
        ParsePosition pos = new ParsePosition(0);

        this.extendedMessageFormat.applyPattern(pattern);
        this.extendedMessageFormat.parseFormatDescription(pattern, pos);
    }

    @Test
    public void testToString() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("yyyy-MM-dd");
            }
        });
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{0,date}", Locale.getDefault(), registry);

        String expected = "ExtendedMessageFormat[pattern={0,date}, locale=en_US, formats={}]";
        Assert.assertEquals(expected, extendedMessageFormat.toString());
    }

    @Parameters
    public static Collection<Object> dataProviderRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("yyyy-MM-dd");
            }
        });
        return Arrays.asList(registry);
    }
}