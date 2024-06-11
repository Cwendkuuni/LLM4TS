package com.example;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    // Expert 1's test cases for deserialize and deserializeCustom methods

    // Expert 2's test cases for deserializeWithType method

    // Expert 3's test cases for handleNonArray method

    // Additional test cases for deserializeWithType method

    @Test
    public void testDeserializeWithTypeEmptyArray() throws IOException {
        StringJsonParser jp = new StringJsonParser("[]");
        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testDeserializeWithTypeEmptyString() throws IOException {
        StringJsonParser jp = new StringJsonParser("\"\"");
        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
            fail("Expected mapping exception");
        } catch (MappingException ex) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithTypeNullValues() throws IOException {
        StringJsonParser jp = new StringJsonParser("[null, null, null]");
        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
        assertArrayEquals(new String[]{"null", "null", "null"}, result);
    }

    @Test
    public void testDeserializeWithTypeUnexpectedToken() throws IOException {
        StringJsonParser jp = new StringJsonParser("{\"key\":\"value\"}");
        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
            fail("Expected mapping exception");
        } catch (MappingException ex) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithTypeNullInput() throws IOException {
        StringJsonParser jp = null;
        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
            fail("Expected mapping exception");
        } catch (MappingException ex) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithTypeNoTypeInformation() throws IOException {
        StringJsonParser jp = new StringJsonParser("[\"Hello\", \"World\", \"Java\"]");
        TypeDeserializer typeDeserializer = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
            fail("Expected mapping exception");
        } catch (MappingException ex) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithTypeIncorrectTypeInformation() throws IOException {
        StringJsonParser jp = new StringJsonParser("[\"Hello\", \"World\", \"Java\"]");
        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(Integer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
            fail("Expected mapping exception");
        } catch (MappingException ex) {
            // expected
        }
    }

}