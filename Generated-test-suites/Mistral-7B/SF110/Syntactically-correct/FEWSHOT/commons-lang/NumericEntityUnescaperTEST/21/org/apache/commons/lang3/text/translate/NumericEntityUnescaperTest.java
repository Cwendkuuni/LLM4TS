package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperConstruction() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired);
        NumericEntityUnescaper unescaperWithEmptyOptions = new NumericEntityUnescaper(new OPTION[0]);

        assertEquals(1, unescaper.options.size());
        assertEquals(OPTION.semiColonRequired, unescaperWithOptions.options.get(0));
        assertNull(unescaperWithEmptyOptions.options);
    }

    @Test
    public void testIsSet_semiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet_semiColonOptional() {
        NumericEntityUnescaper unescaperWithSemiColonOptional = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertFalse(unescaperWithSemiColonOptional.isSet(OPTION.semiColonRequired));
        assertTrue(unescaperWithSemiColonOptional.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testIsSet_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaperWithErrorIfNoSemiColon = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertFalse(unescaperWithErrorIfNoSemiColon.isSet(OPTION.semiColonRequired));
        assertFalse(unescaperWithErrorIfNoSemiColon.isSet(OPTION.semiColonOptional));
        assertTrue(unescaperWithErrorIfNoSemiColon.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslationWithSemiColonRequiredButNoSemiColon() {
        NumericEntityUnescaper unescaperWithSemiColonRequired = new NumericEntityUnescaper(OPTION.semiColonRequired);
        unescaperWithSemiColonRequired.translate("&amp;#123;", 0, new StringWriter(new StringBuilder()).getBuffer());
    }

    @Test
    public void testTranslationWithSemiColonOptional() {
        NumericEntityUnescaper unescaperWithSemiColonOptional = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter writer = new StringWriter(new StringBuilder()).getBuffer();
        assertEquals("€", unescaperWithSemiColonOptional.translate("&euro;", 0, writer));
        assertEquals("€", writer.toString());
    }

    @Test
    public void testTranslationWithErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaperWithErrorIfNoSemiColon = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter(new StringBuilder()).getBuffer();
        assertEquals("&amp;#123;", unescaperWithErrorIfNoSemiColon.translate("&euro;", 0, writer));
        assertSame("Semi-colon required at end of numeric entity", new IllegalArgumentException("").getMessage(), writer.toString());
    }

    @Test
    public void testTranslationHexValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter(new StringBuilder()).getBuffer();
        assertEquals("\u00a9", unescaper.translate("&copy;", 0, writer));
        assertEquals("\u00a9", writer.toString());
    }

    @Test
    public void testTranslationDecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter(new StringBuilder()).getBuffer();
        assertEquals("©", unescaper.translate("&copy;", 0, writer));
        assertEquals("©", writer.toString());
    }

    @Test
    public void testTranslationInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter(new StringBuilder()).getBuffer();
        assertEquals("", unescaper.translate("&invalid;", 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslationLargeDecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter(new StringBuilder()).getBuffer();
        assertEquals("\uD800\uDC00", unescaper.translate("&H010001;", 0, writer));
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslationLargeHexValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter(new StringBuilder()).getBuffer();
        assertEquals("\uD83C\uDF80\uD83C\uDF81", unescaper.translate("&H210001;", 0, writer));
        assertEquals("\uD83C\uDF80\uD83C\uDF81", writer.toString());
    }
}