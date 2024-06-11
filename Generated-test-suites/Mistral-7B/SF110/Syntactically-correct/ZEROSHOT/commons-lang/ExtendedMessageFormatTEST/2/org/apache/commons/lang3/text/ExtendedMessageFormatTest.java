package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testConstructors() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertNull(fmt.getRegistry());

        pattern = "Hello {0,choice,0#Foo|1#Bar}!";
        fmt = new ExtendedMessageFormat(pattern, Locale.getDefault());
        assertEquals(pattern, fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertNull(fmt.getRegistry());

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("choice", new ChoiceFormatFactory());
        pattern = "Hello {0,choice,0#Foo|1#Bar}!";
        fmt = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        assertEquals(pattern, fmt.toPattern());
        assertEquals(Locale.getDefault(), fmt.getLocale());
        assertEquals(registry, fmt.getRegistry());
    }

    @org.junit.Test
    public void testApplyPattern() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Locale.getDefault());

        Object[] args = {"Alice"};
        String formatted = fmt.format(args);
        assertEquals("Hello Alice!", formatted);

        fmt.applyPattern("Hello {1} {0}!");
        args = new Object[]{"Bob", "Alice"};
        formatted = fmt.format(args);
        assertEquals("Hello Bob Alice!", formatted);

        fmt.applyPattern("Hello {1,number,#,##0} {0}!");
        args = new Object[]{"Bob", 12};
        formatted = fmt.format(args);
        assertEquals("Hello 12 Bob!", formatted);

        fmt.applyPattern("Hello {0,choice,0#Foo|1#Bar}! {1,choice,0#Apple|1#Banana}");
        args = new Object[]{"Bar", "Apple"};
        formatted = fmt.format(args);
        assertEquals("Hello Bar Apple!", formatted);

        // Test with custom format
        FormatFactory registry = new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                if (name.equals("custom")) {
                    return new CustomFormat();
                }
                return null;
            }
        };
        fmt.applyPattern("Hello {0} {1,custom}!");
        args = new Object[]{"Alice", "custom"};
        formatted = fmt.format(args);
        assertEquals("Hello Alice custom!", formatted);
    }

    @org.junit.Test
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Locale.getDefault());

        try {
            fmt.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testEqualsAndHashCode() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(pattern, Locale.getDefault());
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(pattern, Locale.getDefault());
        ExtendedMessageFormat fmt3 = new ExtendedMessageFormat("Hello {0}!", Locale.getDefault(), new HashMap<String, FormatFactory>());

        assertEquals(fmt1, fmt1);
        assertEquals(fmt1, fmt2);
        assertEquals(fmt1.hashCode(), fmt2.hashCode());
        assertEquals(fmt1, fmt3);
        assertEquals(fmt1.hashCode(), fmt3.hashCode());

        String pattern2 = "Hello {1} {0}!";
        ExtendedMessageFormat fmt4 = new ExtendedMessageFormat(pattern2, Locale.getDefault());
        assertNotEquals(fmt1, fmt4);
        assertNotEquals(fmt1.hashCode(), fmt4.hashCode());
    }

    private static class ChoiceFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String argument, Locale locale) {
            if (name.equals("choice")) {
                return new ChoiceFormat(argument);
            }
            return null;
        }
    }

    private static class CustomFormat implements Format {

        @Override
        public String format(Object object, Locale locale) {
            if (object instanceof String) {
                return "Custom: " + object;
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public Object parseObject(String text, ParsePosition pos) throws ParseException, IllegalArgumentException {
            throw new UnsupportedOperationException();
        }
    }

    private static class SimpleDateFormat extends Format {

        @Override
        public String format(Object object, Locale locale) {
            if (object != null) {
                return new SimpleDateFormat("yyyy-MM-dd").format((Date) object);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public Object parseObject(String text, ParsePosition pos) throws ParseException, IllegalArgumentException {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(text);
            } catch (ParseException e) {
                pos.setIndex(pos.getIndex() + text.length());
                throw e;
            }
        }
    }
}