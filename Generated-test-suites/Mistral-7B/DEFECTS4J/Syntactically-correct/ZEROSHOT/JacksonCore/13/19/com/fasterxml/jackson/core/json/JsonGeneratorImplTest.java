package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link JsonGeneratorImpl}.
 */
public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setup() {
        IOContext ioContext = new IOContext();
        JsonFactory jsonFactory = new JsonFactory();
        jsonGenerator = jsonFactory.createGenerator(ioContext);
    }

    @Test
    public void testDefaultConstruction() {
        Assert.assertNotNull(jsonGenerator);
        Assert.assertNull(jsonGenerator._characterEscapes);
        Assert.assertArrayEq(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
        Assert.assertEquals(127, jsonGenerator.getHighestEscapedChar());
        Assert.assertFalse(jsonGenerator._cfgUnqNames);
        Assert.assertNull(jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {};
        jsonGenerator.setCharacterEscapes(characterEscapes);
        Assert.assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
        Assert.assertNotEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCharacterEscapesNull() {
        jsonGenerator.setCharacterEscapes(null);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(100);
        Assert.assertEquals(100, jsonGenerator.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedCharNegative() {
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("test");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        Assert.assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testEnableQuoteFieldNames() {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        Assert.assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testDisableQuoteFieldNames() {
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        Assert.assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testVersion() {
        Assert.assertNotNull(jsonGenerator.version());
    }

    private static void assertArrayEq(int[] expected, int[] actual) {
        Assert.assertArrayEquals(expected.getClass(), actual.getClass());
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], actual[i]);
        }
    }
}