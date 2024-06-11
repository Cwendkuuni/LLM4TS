package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    private static final String DUMMY_PATTERN = "";
    private static final String ESCAPED_QUOTE = "''";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE_CHAR = '{';
    private static final char QUOTE_CHAR = '\'';

    private static final String EMPTY_REGISTRY = "";
    private static final String REGISTRY_WITH_ONE_FORMAT = "NumberFormat,NumberFormatStyle";

    private ExtendedMessageFormat format;

    @Before
    public void setUp() {
        // The following registry maps format names to simple instance of NumberFormat
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("NumberFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new DecimalFormat("0");
            }
        });
        format = new ExtendedMessageFormat("{0,format(NumberFormat,Number)}",
                Locale.getDefault(), registry);
    }

    @Test
    public void testNewInstance() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault());
        assertEquals("", instance.toPattern());
    }

    @Test
    public void testNewInstanceWithRegistry() {
        assertEquals("{}", format.toPattern());
    }

    @Test
    public void testNewInstanceWithPatternAndRegistry() {
        assertEquals("{}", new ExtendedMessageFormat("{}", Locale.getDefault(), registry).toPattern());
    }

    @Test
    public void testSetLocale() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault());
        assertEquals(Locale.getDefault(), instance.getLocale());
        instance.setLocale(Locale.US);
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{}", Locale.getDefault());
        instance.applyPattern("{0}");
        assertEquals("{}", instance.toPattern());

        instance = new ExtendedMessageFormat("{}", Locale.getDefault(), registry);
        instance.applyPattern("{0,format(NumberFormat)}");
        assertEquals("{0,format(NumberFormat)}", instance.toPattern());

        instance.applyPattern("{0,format(NumberFormat,Number)}");
        assertEquals("{0,format(NumberFormat,Number)}", instance.toPattern());

        instance.applyPattern("{0,format(NumberFormat,Number),format(DateFormat,Date)}");
        assertEquals("{0,format(NumberFormat,Number),format(DateFormat,Date)}",
                instance.toPattern());
    }

    @Test
    public void testSetFormat() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByName() {
        format.setFormatByName("NumberFormat", new DecimalFormat("0"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetFormatsByName() {
        Format[] formats = new Format[1];
        formats[0] = new DecimalFormat("0");
        format.setFormatsByName("NumberFormat", formats);
        assertEquals(1, format.getFormats().length);
        assertSame(formats[0], format.getFormats()[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testFormat() {
        Object[] args = new Object[1];
        args[0] = new Integer(123);
        String formatted = format.format(args);
        assertEquals("123", formatted);
    }

    @Test
    public void testFormatWithRegistry() {
        Object[] args = new Object[1];
        args[0] = new Integer(123);
        format.applyPattern("{0,format(NumberFormat)}");
        String formatted = format.format(args);
        assertEquals("123", formatted);
    }

    @Test
    public void testFormatWithRegistryAndFormatStyle() {
        Object[] args = new Object[1];
        args[0] = new Integer(123);
        format.applyPattern("{0,format(NumberFormat,Percent)}");
        String formatted = format.format(args);
        assertEquals("123%", formatted);
    }

    @Test
    public void testFormatWithManyFormatsAndStyles() {
        Object[] args = new Object[2];
        args[0] = new Integer(123);
        args[1] = new Float(123.456);
        format.applyPattern("{0,format(NumberFormat,Number)}{1,format(NumberFormat,Percent)}");
        String formatted = format.format(args);
        assertEquals("123{0}123.456%", formatted);
    }

    @Test
    public void testFormatWithEmptyRegistry() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault());
        instance.applyPattern("{0,format(NumberFormat)}");
        try {
            instance.format(new Object[0]);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'NumberFormat' not found", e.getMessage());
        }
    }

    @Test
    public void testFormatWithWrongFormatName() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault(), registry);
        instance.applyPattern("{0,format(UnknownFormat)}");
        try {
            instance.format(new Object[0]);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'UnknownFormat' not found", e.getMessage());
        }
    }

    @Test
    public void testFormatWithWrongFormatStyle() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault(), registry);
        instance.applyPattern("{0,format(NumberFormat,UnknownStyle)}");
        try {
            instance.format(new Object[0]);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format style 'UnknownStyle' not found for format 'NumberFormat'",
                    e.getMessage());
        }
    }

    @Test
    public void testParsePattern() {
        String pattern = "{0,format(NumberFormat,Number)}{1,format(NumberFormat,Percent)}";
        ExtendedMessageFormat instance = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        assertEquals(pattern, instance.toPattern());
        Object[] args = new Object[2];
        args[0] = new Integer(123);
        args[1] = new Float(123.456);
        assertEquals("123{0}123.456%", instance.format(args));
    }

    @Test
    public void testParsePatternWithEmptyRegistry() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault());
        instance.applyPattern("{0,format(NumberFormat)}");
        try {
            instance.parse(new Object[] {}, new ParsePosition(0));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'NumberFormat' not found", e.getMessage());
        }
    }

    @Test
    public void testParsePatternWithWrongFormatName() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault(), registry);
        instance.applyPattern("{0,format(NumberFormat)}");
        try {
            instance.parse(new Object[] {"foo"}, new ParsePosition(0));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'foo' not found", e.getMessage());
        }
    }

    @Test
    public void testParsePatternWithManyFormatsAndStyles() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0,format(NumberFormat,Number)}{1,format(NumberFormat,Percent)}",
                Locale.getDefault(), registry);
        Object[] args = new Object[2];
        args[0] = new Integer(123);
        args[1] = new Float(123.456);
        ParsePosition pos = new ParsePosition(0);
        instance.parse(args, pos);
        assertEquals(2, args.length);
        assertEquals(123, args[0]);
        assertEquals(123.456, args[1]);
        assertTrue(pos.getIndex() >= 0);
        assertEquals("", instance.format(args));
    }

    @Test
    public void testToString() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0}", Locale.getDefault());
        instance.applyPattern("{0,format(StringFormat)}");
        String expected = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        String actual = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithRegistry() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0,format(NumberFormat)}", Locale.getDefault(), registry);
        String expected = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        String actual = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithFormatStyle() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0,format(NumberFormat,Percent)}", Locale.getDefault(), registry);
        String expected = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        String actual = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithManyFormatsAndStyles() {
        ExtendedMessageFormat instance = new ExtendedMessageFormat("{0,format(NumberFormat,Number)}{1,format(NumberFormat,Percent)}",
                Locale.getDefault(), registry);
        String expected = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        String actual = ToStringBuilder.reflectionToString(instance,
                ToStringStyle.SHORT_PREFIX_STYLE);
        assertEquals(expected, actual);
    }

    /**
     * Tests format factory interface.
     */
    private static interface FormatFactory {

        Format getFormat(String formatName, String formatStyle, Locale locale);

    }

    /**
     * A simple implementation of FormatFactory interface.
     */
    private static class SimpleFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            if (StringUtils.equalsIgnoreCase(formatName, "NumberFormat")) {
                return new DecimalFormat("0");
            }
            throw new IllegalArgumentException("Invalid format name: " + formatName);
        }

    }

    /**
     * A simple implementation of FormatFactory interface.
     */
    private static class DateFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            if (StringUtils.equalsIgnoreCase(formatName, "DateFormat")) {
                if (StringUtils.equalsIgnoreCase(formatStyle, "Date")) {
                    return new SimpleDateFormat("yyyy-MM-dd");
                } else if (StringUtils.equalsIgnoreCase(formatStyle, "Time")) {
                    return new SimpleDateFormat("HH:mm:ss");
                }
            }
            throw new IllegalArgumentException("Invalid format name: " + formatName);
        }

    }

    /**
     * A simple implementation of FormatFactory interface.
     */
    private static class StringFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            return new MessageFormat(formatName);
        }

    }

    /**
     * A simple implementation of FormatFactory interface.
     */
    static class CustomFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            if (StringUtils.equalsIgnoreCase(formatName, "CustomFormat")) {
                return new CustomFormat();
            }
            throw new IllegalArgumentException("Invalid format name: " + formatName);
        }

    }

    /**
     * A simple implementation of FormatFactory interface for testing.
     */
    static class CustomFormat implements Format {

        @Override
        public Object format(Object obj, Locale locale, String format, ParsePosition newPos) {
            return String.format("CustomFormat: %s", obj);
        }

        @Override
        public String format(Object obj, Locale locale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFormatName() {
            return "CustomFormat";
        }

        @Override
        public Class<Object> getParameterTypes() {
            return null;
        }

        @Override
        public void setErrorMessage(String arg0) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setParameter(String key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAllParameters(Map<String,?> parameters) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void parse(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "CustomFormat";
        }

    }

}