package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testSimpleFormatting() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0,choice,0#No|1#Yes}!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        String[] args = {"No"};
        Object[] objects = {args[0]};

        String result = fmt.format(objects, new Object[]{}, Locale.getDefault());
        assertEquals("Hello No!", result);

        args = new String[]{"Yes"};
        objects = new Object[]{args[0]};

        result = fmt.format(objects, new Object[]{}, Locale.getDefault());
        assertEquals("Hello Yes!", result);

        // test with empty arguments
        args = new String[0];
        objects = new Object[0];
        result = fmt.format(objects, new Object[]{}, Locale.getDefault());
        assertEquals("", result);

        // test with too many arguments
        args = new String[]{"No", "Yes"};
        objects = new Object[]{args[0], args[1]};
        try {
            fmt.format(objects, new Object[]{}, Locale.getDefault());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test with null arguments
        args = new String[]{"No", null};
        objects = new Object[]{args[0], null};
        try {
            fmt.format(objects, new Object[]{}, Locale.getDefault());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEmptyFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();

        String pattern = "";
        try {
            ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0,choice,0#No|1#Yes} ({2,choice,0#None|1#One})!";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        String[] args = {"No", "One"};
        Object[] objects = {args[0], args[1], null};

        String result = fmt.format(objects, new Object[]{}, Locale.getDefault());
        assertEquals("Hello No (One)!", result);

        args = new String[]{"Yes", "None"};
        objects = new Object[]{args[0], args[1], null};

        result = fmt.format(objects, new Object[]{}, Locale.getDefault());
        assertEquals("Hello Yes (None)!", result);
    }

    @Test
    public void testFormatWithEmptyFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0,choice,0#No|1#Yes} ({2,choice,0#None|1#One}) {}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        String[] args = {"No", "One"};
        Object[] objects = {args[0], args[1], null};

        String result = fmt.format(objects, new Object[]{}, Locale.getDefault());
        assertEquals("Hello No (One) {}", result);

        args = new String[]{"Yes", "None"};
        objects = new Object[]{args[0], args[1], null};

        result = fmt.format(objects, new Object[]{}, Locate.getDefault());
        assertEquals("Hello Yes (None) {}", result);

        // test with null format
        registry.remove("customFormat");
        try {
            ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(pattern, registry);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatWithEmptyRegistry() {
        String pattern = "Hello {0,choice,0#No|1#Yes} ({2,choice,0#None|1#One}) {}";
        try {
            ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Custom format factory for testing.
     */
    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String arguments, Locale locale) {
            if (formatName.equals("customFormat")) {
                return new CustomFormat();
            }
            return null;
        }
    }

    /**
     * Custom format for testing.
     */
    private static class CustomFormat extends Format {
        @Override
        public String format(Object obj, Locale locale, ParsePosition parsePosition) {
            Validate.notNull(obj, "Object cannot be null.");
            String str = obj.toString();
            parsePosition.setIndex(parsePosition.getIndex() + str.length());
            return str;
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition) {
            throw new UnsupportedOperationException();
        }
    }
}