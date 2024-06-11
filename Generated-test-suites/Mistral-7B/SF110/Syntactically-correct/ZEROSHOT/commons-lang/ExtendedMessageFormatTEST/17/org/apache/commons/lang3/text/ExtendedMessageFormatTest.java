package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.Validate;

public class ExtendedMessageFormatTest {

    private static final String TEST_PATTERN = "Hello {0}, your age is {1,number, integer}.";

    @org.junit.Test
    public void testExtendedMessageFormat() throws ParseException {
        // Test default constructor
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(TEST_PATTERN);
        String result1 = format1.format(new Object[]{"John", 25});
        assertEquals("Hello John, your age is 25.", result1);

        // Test constructor with locale
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH);
        String result2 = format2.format(new Object[]{"John", 25});
        assertEquals("Hello John, your age is 25.", result2);

        // Test constructor with registry
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageMessageFormat format3 = new ExtendedMessageFormat(TEST_PATTERN, null, registry);
        String result3 = format3.format(new Object[]{"John", 25});
        assertEquals("Hello John, your age is Two Five.", result3); // NumberFormatFactory changes number to "Two Five"

        // Test toPattern method
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(TEST_PATTERN);
        String pattern4 = format4.toPattern();
        assertEquals(TEST_PATTERN, pattern4);

        // Test applyPattern method with no custom formats
        ExtendedMessageFormat format5 = new ExtendedMessageFormat(TEST_PATTERN);
        format5.applyPattern(TEST_PATTERN);
        String result5 = format5.format(new Object[]{"John", 25});
        assertEquals("Hello John, your age is 25.", result5);

        // Test applyPattern method with custom formats
        ExtendedMessageFormat format6 = new ExtendedMessageFormat(TEST_PATTERN);
        format6.applyPattern(TEST_PATTERN + "{2,customNumber}");
        Map<String, FormatFactory> customFormats = new HashMap<>();
        customFormats.put("customNumber", new CustomNumberFormatFactory());
        format6.setRegistry(customFormats);
        format6.applyPattern(TEST_PATTERN + "{2,customNumber}");
        String result6 = format6.format(new Object[]{"John", 25, "Sixteen"});
        assertEquals("Hello John, your age is Sixteen.", result6); // CustomNumberFormatFactory changes number to "Sixteen"

        // Test setFormat method
        try {
            format6.setFormat(0, new MessageFormat());
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {}

        // Test setFormatByArgumentIndex method
        try {
            format6.setFormatByArgumentIndex(0, new MessageFormat());
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {}

        // Test setFormats method
        try {
            format6.setFormats(new MessageFormat[0]);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {}

        // Test setFormatsByArgumentIndex method
        try {
            format6.setFormatsByArgumentIndex(new MessageFormat[0]);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {}

        // Test equals method
        ExtendedMessageFormat format7 = new ExtendedMessageFormat(TEST_PATTERN);
        ExtendedMessageFormat format8 = new ExtendedMessageFormat(TEST_PATTERN);
        assertTrue(format7.equals(format8));

        ExtendedMessageFormat format9 = new ExtendedMessageFormat(TEST_PATTERN, Locale.US);
        assertFalse(format7.equals(format9));

        // Test hashCode method
        ExtendedMessageFormat format10 = new ExtendedMessageFormat(TEST_PATTERN);
        ExtendedMessageFormat format11 = new ExtendedMessageFormat(TEST_PATTERN);
        assertEquals(format10.hashCode(), format11.hashCode());

        ExtendedMessageFormat format12 = new ExtendedMessageFormat(TEST_PATTERN, Locale.US);
        assertNotEquals(format10.hashCode(), format12.hashCode());
    }

    private static class NumberFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new DecimalFormat("#,##0");
        }

    }

    private static class CustomNumberFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new DecimalFormat("Two {0}");
        }

    }

}