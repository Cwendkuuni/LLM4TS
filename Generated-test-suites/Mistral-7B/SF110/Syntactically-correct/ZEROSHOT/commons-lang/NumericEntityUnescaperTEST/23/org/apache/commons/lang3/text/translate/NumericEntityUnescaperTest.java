package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testDefaultConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertNotNull(unescaper.getOptions());
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testConstructorWithOptions() {
        EnumSet<NumericEntityUnescaper.OPTION> options = EnumSet.of(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertEquals(options, unescaper.getOptions());
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

        EnumSet<NumericEntityUnescaper.OPTION> options = EnumSet.of(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(options.toArray(new NumericEntityUnescaper.OPTION[0]));
        assertTrue(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslateWithDefaultOptions() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);

        String testString = "Hello &copy; World! &psi; this is a &A1B1C1D1E1F1 test &#x10A1B1C1D1E1F1 &#x10AF &#12345 &#x543210AB &#x7856";
        int index = 0;
        while (index < testString.length()) {
            int length = unescaper.translate(new String(testString), index, writer);
            index += length;
        }

        assertEquals("Hello © World! ψ this is a ΑΒÇΔΕF test ΑΒÇΔΕF Π ₁₂₃₄₅ 五四三二一⺩ seven eight five six", writer.getBuffer().toString());
    }

    @Test
    public void testTranslateWithSemiColonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);

        String testString = "Hello &copy; World! &psi; this is a &A1B1C1D1E1F1 test &#x10A1B1C1D1E1F1 &#x10AF &#12345 &#x543210AB &#x7856";
        int index = 0;
        while (index < testString.length()) {
            int length = unescaper.translate(new String(testString), index, writer);
            index += length;
        }

        String result = output.toString();
        assertEquals("Hello © World! ψ this is a ΑΒÇΔΕF test ΑΒÇΔΕF Π ₁₂₃₄₅ 五四三二一⺩ seven eight five six\r\n", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);

        String testString = "Hello &copy; World! &psi; this is a &A1B1C1D1E1F1 test &#x10A1B1C1D1E1F1 &#x10AF &#12345 &#x543210AB &#x7856";
        int index = 0;
        while (index < testString.length() - 1) {
            int length = unescaper.translate(new String(testString), index, writer);
            index += length;
        }

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new String(testString), testString.length() - 1, writer));
    }

}