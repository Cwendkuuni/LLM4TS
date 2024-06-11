package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        IOContext ioContext = new IOContext();
        int features = Feature.AUTO_CLOSE_TARGET.enabledIn(0);
        ObjectCodec objectCodec = new SimpleObjectCodec();
        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, features, objectCodec);
    }

    @Test
    public void testDefaultConstruction() {
        assertNull(jsonGeneratorImpl._characterEscapes);
        assertEquals(sOutputEscapes, jsonGeneratorImpl._outputEscapes);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new SimpleCharacterEscapes();
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes.getEscapeCodesForAscii(), jsonGeneratorImpl._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes characterEscapes = new SimpleCharacterEscapes();
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGeneratorImpl.getCharacterEscapes());
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedCharNegative() {
        jsonGeneratorImpl.setHighestNonEscapedChar(-128);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("rootValueSeparator");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl._rootValueSeparator);
    }

    @Test
    public void testEnableQuoteFieldNames() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testDisableQuoteFieldNames() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES); // just to make sure it is enabled
        jsonGeneratorImpl.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testVersion() {
        assertEquals(Version.unknownVersion(), jsonGeneratorImpl.version());
    }
}

class SimpleObjectCodec extends ObjectCodec {
    @Override
    public JsonParser createParser(String contents) throws IOException {
        return null;
    }

    @Override
    public JsonParser createParser(FileValueSource valueSource) throws IOException {
        return null;
    }

    @Override
    public JsonParser createParser(InputStream valueSource) throws IOException {
        return null;
    }

    @Override
    public JsonParser createParser(byte[] valueSource) throws IOException {
        return null;
    }

    @Override
    public JsonParser createParser(char[] valueSource) throws IOException {
        return null;
    }

    @Override
    public String getFactoryName() {
        return null;
    }
}

class SimpleCharacterEscapes extends CharacterEscapes {
    @Override
    public int[] getEscapeCodesForAscii() {
        return new int[]{22, 23, 25, 26, 27, 32, 127}; // custom set of escape codes
    }
}