package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.ENGLISH);
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new SimpleDateFormat(argument);
            }
        };
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{date, date, 'dd-MM-yyyy'}, {0}, {1}", registry);
        assertEquals("{date, date, 'dd-MM-yyyy'}, {0}, {1}", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new SimpleDateFormat(argument, locale);
            }
        };
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{date, date, 'dd-MM-yyyy'}, {0}, {1}", Locale.ENGLISH, registry);
        assertEquals("{date, date, 'dd-MM-yyyy'}, {0}, {1}", format.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testApplyPattern() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        String[] args = new String[]{"Hello", "World"};
        String formattedMessage = format.format(args);
        assertEquals("Hello, World", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPattern() {
        new ExtendedMessageFormat("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPattern() {
        try {
            new ExtendedMessageFormat(null);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Pattern must not be null", e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElementAtPosition() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, ({1})");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElementAtPosition() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, {1}, {2}}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndexAtPosition() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{invalidIndex}, {0}");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperations() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormat(0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLocale() {
        new ExtendedMessageFormat("{0}, {1}", null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put(null, null);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new SimpleDateFormat(argument);
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("{date, null}", registry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatFactory() {
        Map<String, FormatFactory> registry = Collections.emptyMap();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{date}", registry);
    }
}