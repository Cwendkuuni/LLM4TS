package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setup() {
        IOContext ioContext = new IOContext();
        int features = JsonManifest.StandardFeatures.DEFAULT_MASK;
        ObjectCodec codec = new SimpleObjectMapper().getFactory().getCodec();
        jsonGenerator = new JsonGeneratorImpl(ioContext, features, codec);
    }

    @Test
    public void testDefaultConstruction() {
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
        assertNull(_characterEscapes);
        assertFalse(_cfgUnqNames);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testFeatureEnabled() {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(_cfgUnqNames);

        jsonGenerator.enable(Feature.ESCAPE_NON_ASCII);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator._maximumNonEscapedChar);

        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new StandardCharacterEscapes();
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = "|";
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }
}