package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
    }

    @Test
    public void testDefaultValues() {
        assertEquals("default root value separator", DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator._rootValueSeparator);
        assertArrayEquals("default output escapes", CharTypes.get7BitOutputEscapes(), jsonGenerator._outputEscapes);
        assertEquals("default maximum non-escaped char", 127, jsonGenerator.getHighestEscapedChar());
        assertNull("default character escapes", jsonGenerator.getCharacterEscapes());
        assertFalse("default unquoted field names", jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testConfiguration() {
        // set root value separator
        SerializableString rootValueSeparator = new SerializableString(":");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals("custom root value separator", rootValueSeparator, jsonGenerator._rootValueSeparator);

        // set output escapes
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 0x1, 0x2, 0x3 }; // custom escapes
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertNotNull("custom character escapes", jsonGenerator.getCharacterEscapes());
        assertNotArrayEquals("custom output escapes", CharTypes.get7BitOutputEscapes(), jsonGenerator._outputEscapes);
        assertArrayEquals("custom output escapes", new int[] { 0x1, 0x2, 0x3 }, jsonGenerator._outputEscapes);

        // set maximum non-escaped char
        jsonGenerator.setHighestNonEscapedChar(0x400);
        assertEquals("custom maximum non-escaped char", 0x400, jsonGenerator.getHighestEscapedChar());

        // set unquoted field names
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue("quoted field names", jsonGenerator._cfgUnqNames);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse("unquoted field names", jsonGenerator._cfgUnqNames);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaximumNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCharacterEscapes() {
        jsonGenerator.setCharacterEscapes(null);
    }
}