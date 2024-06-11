package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.MessageFormatException;
import org.junit.Test;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ExtendedMessageFormatTest {

    private static final String PATTERN_1 = "Hello, {0}! Your age is {1}";
    private static final String PATTERN_2 = "The date is {0, date, short}:{1}";
    private static final String PATTERN_3 = "The format '{0}' is not supported";

    private static final FormatFactory DATE_FORMAT_FACTORY = new FormatFactory() {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            try {
                return new SimpleDateFormat(arguments, locale);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Test
    public void testDefaultFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_1);
        Object[] args = {"John", 25};
        String result = format.format(args);
        assertEquals("Hello, John! Your age is 25", result);
    }

    @Test
    public void testLocaleFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_1, Locale.US);
        Object[] args = {"John", 25};
        String result = format.format(args);
        assertEquals("Hello, John! Your age is twenty-five", result);
    }

    @Test
    public void testCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", DATE_FORMAT_FACTORY);
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_2, null, registry);
        Object[] args = {new Date(), "AM"};
        String result = format.format(args);
        assertEquals("The date is Sep 05, 2023:AM", result);
    }

    @Test
    public void testInvalidFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_1);
        assertThrows(MessageFormatException.class, () -> format.applyPattern("InvalidPattern"));
    }

    @Test
    public void testMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_3, null, registry);
        assertNull(format.getFormat("date"));
        assertThrows(MessageFormatException.class, () -> format.applyPattern(PATTERN_3, "date"));
    }

    @Test
    public void testEscapedCharacter() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_1);
        Object[] args = {"O'Reilly", 42};
        String result = format.format(args);
        assertEquals("Hello, O'Reilly! Your age is 42", result);
    }

    @Test
    public void testQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_1);
        Object[] args = {"O''Reilly", 42};
        String result = format.format(args);
        assertEquals("Hello, O''Reilly! Your age is 42", result);
    }
}