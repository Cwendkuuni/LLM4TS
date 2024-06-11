package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.junit.Test;

/**
 * Unit tests for {@link ExtendedMessageFormat} class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with default locale and empty pattern.
     */
    @Test
    public void testConstructorEmptyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertNotNull(fmt);
        assertEquals("", fmt.toPattern());
    }

    /**
     * Test constructor with default locale and simple pattern.
     */
    @Test
    public void testConstructorSimplePattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
        assertNotNull(fmt);
        assertEquals("{0} {1}", fmt.toPattern());
    }

    /**
     * Test constructor with default locale, custom pattern, and empty registry.
     */
    @Test
    public void testConstructorCustomPatternEmptyRegistry() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}");
        assertNotNull(fmt);
        assertEquals("{0}{1}", fmt.toPattern());
    }

    /**
     * Test constructor with default locale, custom pattern, and non-empty registry.
     */
    @Test
    public void testConstructorCustomPatternNonEmptyRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}{2}(date)", registry);
        assertNotNull(fmt);
        assertEquals("{0}{1}{2}(date)", fmt.toPattern());
    }

    /**
     * Test constructor with custom locale and empty pattern.
     */
    @Test
    public void testConstructorEmptyPatternCustomLocale() {
        Locale locale = Locale.ENGLISH;
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", locale);
        assertNotNull(fmt);
        assertEquals("", fmt.toPattern());
    }

    /**
     * Test constructor with custom locale and simple pattern.
     */
    @Test
    public void testConstructorSimplePatternCustomLocale() {
        Locale locale = Locale.ENGLISH;
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", locale);
        assertNotNull(fmt);
        assertEquals("{0}", fmt.toPattern());
    }

    /**
     * Test constructor with custom locale, custom pattern, and empty registry.
     */
    @Test
    public void testConstructorCustomPatternEmptyRegistryCustomLocale() {
        Locale locale = Locale.ENGLISH;
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", locale);
        assertNotNull(fmt);
        assertEquals("{0}", fmt.toPattern());
    }

    /**
     * Test constructor with custom locale, custom pattern, and non-empty registry.
     */
    @Test
    public void testConstructorCustomPatternNonEmptyRegistryCustomLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        Locale locale = Locale.ENGLISH;
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date){1}", registry, locale);
        assertNotNull(fmt);
        assertEquals("{0}(date){1}", fmt.toPattern());
    }

    /**
     * Test constructor with custom pattern and null registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCustomPatternNullRegistry() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", null);
    }

    /**
     * Test constructor with empty pattern and non-empty registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyPatternNonEmptyRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", registry);
    }

    /**
     * Test constructor with custom pattern, empty registry, and no format elements.
     */
    @Test
    public void testConstructorCustomPatternEmptyRegistryNoFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Collections.emptyMap());
        assertNotNull(fmt);
        assertEquals("{0}", fmt.toPattern());
    }

    /**
     * Test constructor with custom pattern, empty registry, and one format element.
     */
    @Test
    public void testConstructorCustomPatternEmptyRegistryOneFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date)", registry);
        assertNotNull(fmt);
        assertEquals("{0}(date)", fmt.toPattern());
    }

    /**
     * Test constructor with custom pattern, empty registry, and multiple format elements.
     */
    @Test
    public void testConstructorCustomPatternEmptyRegistryMultipleFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        registry.put("number", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new DecimalFormat("#,##0.00");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date){1}(number)", registry);
        assertNotNull(fmt);
        assertEquals("{0}(date){1}(number)", fmt.toPattern());
    }

    /**
     * Test constructor with custom pattern, non-empty registry, and no format elements.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCustomPatternNonEmptyRegistryNoFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", registry);
    }

    /**
     * Test constructor with custom pattern, non-empty registry, and one format element.
     */
    @Test
    public void testConstructorCustomPatternNonEmptyRegistryOneFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date)", registry);
        assertNotNull(fmt);
        assertEquals("{0}(date)", fmt.toPattern());
    }

    /**
     * Test constructor with custom pattern, non-empty registry, and multiple format elements.
     */
    @Test
    public void testConstructorCustomPatternNonEmptyRegistryMultipleFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        registry.put("number", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new DecimalFormat("#,##0.00");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date){1}(number)", registry);
        assertNotNull(fmt);
        assertEquals("{0}(date){1}(number)", fmt.toPattern());
    }

    /**
     * Test {@link ExtendedMessageFormat#getFormat(int)} method with no format elements.
     */
    @Test
    public void testGetFormatNoFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Collections.emptyMap());
        assertNull(fmt.getFormat(0));
    }

    /**
     * Test {@link ExtendedMessageFormat#getFormat(int)} method with one format element.
     */
    @Test
    public void testGetFormatOneFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date)", registry);
        Format format = fmt.getFormat(0);
        assertNotNull(format);
        assertEquals(SimpleDateFormat.class, format.getClass());
    }

    /**
     * Test {@link ExtendedMessageFormat#getFormat(int)} method with multiple format elements.
     */
    @Test
    public void testGetFormatMultipleFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        registry.put("number", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new DecimalFormat("#,##0.00");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date){1}(number)", registry);
        Format dateFormat = fmt.getFormat(0);
        Format numberFormat = fmt.getFormat(1);

        assertNotNull(dateFormat);
        assertEquals(SimpleDateFormat.class, dateFormat.getClass());
        assertNotNull(numberFormat);
        assertEquals(DecimalFormat.class, numberFormat.getClass());
    }

    /**
     * Test {@link ExtendedMessageFormat#format(Object[], Locale)} method.
     */
    @Test
    public void testFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        registry.put("number", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new DecimalFormat("#,##0.00");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date){1}(number)", registry);
        Object[] args = new Object[]{new Date(), 123.45};
        String result = fmt.format(args, Locale.ENGLISH);
        assertEquals("11/12/2022, 123.45", result);
    }

    /**
     * Test {@link ExtendedMessageFormat#parse(String, ParsePosition)} method.
     */
    @Test
    public void testParse() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        registry.put("number", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new DecimalFormat("#,##0.00");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date){1}(number)", registry);
        String input = "11/12/2022, 123.45";
        ParsePosition pos = new ParsePosition(0);
        Object[] args = fmt.parse(input, pos);
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals(new Date(), args[0]);
        assertEquals(Double.valueOf(123.45), args[1]);
        assertEquals(input.length(), pos.getIndex());
    }

    /**
     * Test {@link ExtendedMessageFormat#parseObject(String)} method.
     */
    @Test
    public void testParseObject() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat("dd/MM/yyyy");
            }
        });

        registry.put("number", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new DecimalFormat("#,##0.00");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}(date){1}(number)", registry);
        String input = "11/12/2022, 123.45";
        Object[] args = fmt.parseObject(input);
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals(new Date(), args[0]);
        assertEquals(Double.valueOf(123.45), args[1]);
    }
}

class FormatFactory {
    @FunctionalInterface
    public interface Format {
    }

    public abstract Format getFormat(String name, String arguments, Locale locale);
}