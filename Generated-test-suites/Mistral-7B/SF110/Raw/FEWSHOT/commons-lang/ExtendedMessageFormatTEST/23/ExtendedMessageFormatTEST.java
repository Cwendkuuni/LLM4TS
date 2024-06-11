package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstruction() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(null, format.getRegistry());
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(name);
            }
        };
        registry.put("date", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        String pattern = "Hello {0, }.";
        new ExtendedMessageFormat(pattern);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John Doe", 30};
        String formatted = format.format(args);
        assertEquals("Hello John Doe, your age is 30", formatted);
    }

    @Test
    public void testApplyPatternWithRegistry() throws ParseException {
        String pattern = "Hello {0, date(MMMM dd, yyyy)}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat(arguments);
                }
                return null;
            }
        };
        registry.put("date", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John Doe", new Date()};
        String formatted = format.format(args);
        Date date = new Date();
        assertEquals("Hello January 21, 2023, your age is John Doe", formatted);
        assertEquals("January 21, 2023", date.toString());
    }

    @Test
    public void testInsertFormats() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{format.date(MMMM dd, yyyy)}");
        String formatted = format.format(new Object[]{"John Doe"}, new ParsePosition(0), customPatterns);
        assertEquals("Hello January 21, 2023, your age is John Doe", formatted);
    }

    @Test
    public void testGetFormat() {
        String pattern = "Hello {0, format('date(MMMM dd, yyyy)')}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat(arguments);
                }
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("format", formatFactory);
        format.setRegistry(registry);
        Object[] args = {"John Doe", new Date()};
        String formatted = format.format(args);
        Date date = new Date();
        assertEquals("Hello January 21, 2023, your age is John Doe", formatted);
        assertEquals("January 21, 2023", date.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormat(0, new SimpleDateFormat(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        Format[] formats = new Format[0];
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        Format[] formats = new Format[0];
        format.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
        assertEquals(format1, format2);

        format1.setRegistry(new HashMap<>());
        format2.setRegistry(new HashMap<>());
        assertEquals(format1, format2);

        format1.setLocale(Locale.US);
        format2.setLocale(Locale.US);
        assertEquals(format1, format2);

        format1.setToPattern("{0}");
        format2.setToPattern("{0}");
        assertEquals(format1, format2);

        format1.setRegistry(new HashMap<>());
        format1.setLocale(Locale.US);
        format1.setToPattern("{0}");
        format2.setRegistry(new HashMap<>());
        format2.setLocale(Locale.US);
        format2.setToPattern("{0}");
        assertEquals(format1, format2);

        format1.setRegistry(new HashMap<>());
        format1.setLocale(Locale.US);
        format1.setToPattern("{0}");
        format2.setRegistry(new HashMap<>());
        format2.setLocale(Locale.CANADA);
        format2.setToPattern("{0}");
        assertFalse(format1.equals(format2));

        format1.setRegistry(new HashMap<>());
        format1.setLocale(Locale.US);
        format1.setToPattern("{0}");
        format2.setRegistry(new HashMap<>());
        format2.setLocale(Locale.US);
        format2.setToPattern("{1}");
        assertFalse(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
        assertEquals(format1.hashCode(), format2.hashCode());

        format1.setRegistry(new HashMap<>());
        format2.setRegistry(new HashMap<>());
        assertEquals(format1.hashCode(), format2.hashCode());

        format1.setLocale(Locale.US);
        format2.setLocale(Locale.US);
        assertEquals(format1.hashCode(), format2.hashCode());

        format1.setToPattern("{0}");
        format2.setToPattern("{0}");
        assertEquals(format1.hashCode(), format2.hashCode());

        format1.setRegistry(new HashMap<>());
        format1.setLocale(Locale.US);
        format1.setToPattern("{0}");
        format2.setRegistry(new HashMap<>());
        format2.setLocale(Locale.CANADA);
        format2.setToPattern("{0}");
        assertNotEquals(format1.hashCode(), format2.hashCode());

        format1.setRegistry(new HashMap<>());
        format1.setLocale(Locale.US);
        format1.setToPattern("{0}");
        format2.setRegistry(new HashMap<>());
        format2.setLocale(Locale.US);
        format2.setToPattern("{1}");
        assertNotEquals(format1.hashCode(), format2.hashCode());
    }
}