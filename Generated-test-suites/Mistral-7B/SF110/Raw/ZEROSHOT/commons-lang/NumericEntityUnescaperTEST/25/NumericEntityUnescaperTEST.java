package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaper() {
        // Test default constructor
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

        // Test constructor with no options
        NumericEntityUnescaper noOptionsUnescaper = new NumericEntityUnescaper(OPTION.values());
        assertFalse(noOptionsUnescaper.isSet(OPTION.semiColonRequired));
        assertFalse(noOptionsUnescaper.isSet(OPTION.semiColonOptional));
        assertTrue(noOptionsUnescaper.isSet(OPTION.errorIfNoSemiColon));

        // Test constructor with semiColonRequired option
        NumericEntityUnescaper semicolonRequiredUnescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(semicolonRequiredUnescaper.isSet(OPTION.semiColonRequired));
        assertFalse(semicolonRequiredUnescaper.isSet(OPTION.semiColonOptional));
        assertFalse(semicolonRequiredUnescaper.isSet(OPTION.errorIfNoSemiColon));

        // Test translate method with valid numeric entities and no options
        CharSequence input = "&amp;#160;&#32;&#171;&#x2013;";
        StringWriter out = new StringWriter();
        int index = 0;
        int len = unescaper.translate(input, index, out);
        assertEquals("", out.toString());
        index += len;
        assertEquals("\u00A0\u0020—", out.toString());

        // Test translate method with valid numeric entities and semiColonRequired option
        out = new StringWriter();
        len = semicolonRequiredUnescaper.translate(input, index, out);
        assertEquals("\u00A0\u0020—", out.toString());
        assertTrue(out.toString().endsWith("—"));

        // Test translate method with invalid numeric entity and no options
        input = "&amp;#abc";
        out = new StringWriter();
        len = unescaper.translate(input, 0, out);
        assertEquals("", out.toString());

        // Test translate method with invalid numeric entity and semiColonRequired option
        out = new StringWriter();
        len = semicolonRequiredUnescaper.translate(input, 0, out);
        assertEquals("", out.toString());
        assertFalse(out.toString().endsWith(";"));

        // Test translate method with valid hex numeric entity and no options
        input = "&amp;#x1F44D";
        out = new StringWriter();
        len = unescaper.translate(input, 0, out);
        assertEquals("\uD835\uDD4D", out.toString());

        // Test translate method with valid hex numeric entity and semiColonRequired option
        out = new StringWriter();
        len = semicolonRequiredUnescaper.translate(input, 0, out);
        assertEquals("\uD835\uDD4D;", out.toString());

        // Test translate method with valid decimal numeric entity and no options
        input = "&amp;#12345";
        out = new StringWriter();
        len = unescaper.translate(input, 0, out);
        assertEquals("\u00012345", out.toString());

        // Test translate method with valid decimal numeric entity and semiColonRequired option
        out = new StringWriter();
        len = semicolonRequiredUnescaper.translate(input, 0, out);
        assertEquals("\u00012345;", out.toString());

        // Test translate method with invalid numeric entity and errorIfNoSemiColon option
        out = new StringWriter();
        try {
            len = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon).translate(input, 0, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }

        // Test translate method with valid numeric entity and semiColonOptional option
        out = new StringWriter();
        len = new NumericEntityUnescaper(OPTION.semiColonOptional).translate(input, 0, out);
        assertEquals("\u00A0\u0020—", out.toString());
    }
}