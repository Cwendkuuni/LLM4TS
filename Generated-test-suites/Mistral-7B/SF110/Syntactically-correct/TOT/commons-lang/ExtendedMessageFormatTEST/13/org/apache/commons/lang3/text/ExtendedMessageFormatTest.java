package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testSimpleFormat() throws ParseException {
        final FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };

        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        format.setLocale(Locale.ENGLISH);
        format.setFormatFactory(formatFactory);

        final Object[] args = new Object[]{new java.util.Date()};
        final String result = format.format(args);
        Assert.assertEquals("Day IX", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        format.applyPattern("{0, date, d 'Day {0, number, roman}'} {1, date, d 'Day {1, number, roman}'}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormat() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        format.applyPattern("{2, date, d 'Day {0, number, roman}'}");
    }

    @Test
    public void testFormatWithArguments() throws ParseException {
        final FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };

        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'} {1, number, number}");
        format.setLocale(Locale.ENGLISH);
        format.setFormatFactory(formatFactory);

        final Object[] args = new Object[]{new java.util.Date(), 20};
        final String result = format.format(args);
        Assert.assertEquals("Day IX 20", result);
    }

    @Test
    public void testEmptyFormat() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("");
        final Object[] args = new Object[]{};
        final String result = format.format(args);
        Assert.assertEquals("", result);
    }

    @Test
    public void testNullFormat() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat(null);
        final Object[] args = new Object[]{};
        try {
            format.format(args);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testNullArguments() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        final String result = format.format((Object[])null);
        Assert.assertEquals("", result);
    }

    @Test
    public void testLocale() {
        final FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };

        final ExtendedMessageFormat formatEN = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}", Locale.ENGLISH);
        formatEN.setFormatFactory(formatFactory);

        final ExtendedMessageFormat formatDE = new ExtendedMessageFormat("{0, date, d 'Tag {0, number, roman}'}", Locale.GERMAN);
        formatDE.setFormatFactory(formatFactory);

        final Object[] args = new Object[]{new java.util.Date()};
        final Object[] results = new Object[]{formatEN.format(args), formatDE.format(args)};
        Assert.assertArrayEquals(new String[]{"Day IX", "Tag IX"}, Arrays.asList(results).toArray());
    }

    @Test
    public void testRegistry() {
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        });

        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}", null, registry);
        final Object[] args = new Object[]{new java.util.Date()};
        final String result = format.format(args);
        Assert.assertEquals("Day IX", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        format.setFormat(0, new SimpleDateFormat("dd.MM.yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd.MM.yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        final Format[] newFormats = new Format[1];
        newFormats[0] = new SimpleDateFormat("dd.MM.yyyy");
        format.setFormats(newFormats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        final Format[] newFormats = new Format[1];
        newFormats[0] = new SimpleDateFormat("dd.MM.yyyy");
        format.setFormatsByArgumentIndex(newFormats);
    }

    @Test
    public void testEquals() {
        final ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        final ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        final ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}", Locale.ENGLISH);
        final Object[] args = new Object[]{new java.util.Date()};

        Assert.assertTrue(format1.equals(format1));
        Assert.assertTrue(format1.equals(format2));
        Assert.assertFalse(format1.equals(format3));
        Assert.assertFalse(format1.equals(""));
        Assert.assertFalse(format1.equals(null));

        final ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}", Locale.ENGLISH, Arrays.asList(formatFactory));
        format4.setLocale(Locale.GERMAN);
        Assert.assertFalse(format1.equals(format4));

        final Object[] args2 = new Object[]{new java.util.Date(), new java.util.Date()};
        format1.applyPattern("{0, date, d 'Day {0, number, roman}'} {1, date, d 'Day {1, number, roman}'}");
        Assert.assertFalse(format1.equals(format1));
        Assert.assertFalse(format1.equals(format2));
        Assert.assertFalse(format1.equals(args));
        Assert.assertFalse(format1.equals(args2));
    }

    @Test
    public void testHashCode() {
        final ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        final ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}");
        final ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}", Locale.ENGLISH);

        Assert.assertEquals(format1.hashCode(), format1.hashCode());
        Assert.assertEquals(format1.hashCode(), format2.hashCode());
        Assert.assertFalse(format1.hashCode() == format3.hashCode());
        Assert.assertFalse(format1.hashCode() == 0);

        final ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0, date, d 'Day {0, number, roman}'}", Locale.ENGLISH, Arrays.asList(formatFactory));
        format4.setLocale(Locale.GERMAN);
        Assert.assertFalse(format1.hashCode() == format4.hashCode());

        final Object[] args = new Object[]{new java.util.Date()};
        format1.applyPattern("{0, date, d 'Day {0, number, roman}'} {1, date, d 'Day {1, number, roman}'}");
        Assert.assertFalse(format1.hashCode() == format1.hashCode());
        Assert.assertFalse(format1.hashCode() == format2.hashCode());
        Assert.assertFalse(format1.hashCode() == args.hashCode());
    }
}