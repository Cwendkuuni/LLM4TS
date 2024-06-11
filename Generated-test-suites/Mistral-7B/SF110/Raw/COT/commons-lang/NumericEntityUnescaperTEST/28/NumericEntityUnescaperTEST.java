package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperConstruction() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertNotNull(unescaper.options);
        assertEquals(EnumSet.of(NumericEntityUnescaper.OPTION.semiColonRequired), unescaper.options);

        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertNotNull(unescaperWithOptions.options);
        assertEquals(EnumSet.of(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon), unescaperWithOptions.options);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

        NumericEntityUnescaper unescaperAllOptions = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertTrue(unescaperAllOptions.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue(unescaperAllOptions.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaperAllOptions.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate_SimpleCase() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#13; &#171; &#8212; &#160; &#240;&#241;&#242;&#243; &#151; &#162; &#163; &#164; &#165; &#166; &#167; &#168; &#174; &#65533; &#55353; &#x1f600; &#xFFFD;";
        unescaper.translate(new StringReader(testInput), 0, out);
        assertEquals("This is a test --- Ĵĵ---------Ȣ-----\uD83C\uDFFF-\uD83D\uDCA0---------\uD83C\uDF80", out.toString());
    }

    @Test
    public void testTranslate_EntityWithSemiColon() throws Exception {
        NumericEntityUnescaper unescaperWithSemiColonRequired = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#13;&#171; &#8212;&#160; &#240;&#241;&#242;&#243; &#151;&#162;&#163;&#164;&#165;&#166;&#167;&#168;&#174;&#65533;&#55353;&#x1f600;&#xFFFD;";
        unescaperWithSemiColonRequired.translate(new StringReader(testInput), 0, out);
        assertEquals("This is a test --- Ĵĵ---------Ȣ-----\uD83C\uDFFF-\uD83D\uDCA0---------\uD83C\uDF80\r\n", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_EntityWithoutSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#13;&#171; &#8212;&#160; &#240;&#241;&#242;&#243; &#151;&#162;&#163;&#164;&#165;&#166;&#167;&#168;&#174;&#65533;&#55353;&#x1f600;&#xFFFD;";
        unescaper.translate(new StringReader(testInput), 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_EntityWithInvalidCharacter() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#13;&#171; &#8212;&#160; &#240&#241;&#242&#243; &#151;&#162;&#163;&#164;&#165;&#166;&#167;&#168;&#174;&#65533;&#55353;&#x1f600&#xFFFD;";
        unescaper.translate(new StringReader(testInput), 0, out);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_EntityWithInvalidDecimalValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#xyz;";
        unescaper.translate(new StringReader(testInput), 0, out);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_EntityWithInvalidHexValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#xzx";
        unescaper.translate(new StringReader(testInput), 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_EntityWithInvalidHexValueLength() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#x";
        unescaper.translate(new StringReader(testInput), 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_EntityWithInvalidValueLength() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#1234567890";
        unescaper.translate(new StringReader(testInput), 0, out);
    }

    @Test
    public void testTranslate_EntityWithLargeValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#65536";
        unescaper.translate(new StringReader(testInput), 0, out);
        assertEquals("This is a test ƒ", out.toString());
    }

    @Test
    public void testTranslate_EntityWithLargeValueWithSemiColon() throws Exception {
        NumericEntityUnescaper unescaperWithSemiColonRequired = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String testInput = "This is a test &#65536;";
        unescaperWithSemiColonRequired.translate(new StringReader(testInput), 0, out);
        assertEquals("This is a test ƒ\r\n", out.toString());
    }
}