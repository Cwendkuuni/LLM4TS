package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest_Expert1 {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = jsonFactory.createParser("\"C:\\test.txt\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("C:\\test.txt"), deserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = jsonFactory.createParser("\"http://example.com\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://example.com"), deserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserializeInvalidString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = jsonFactory.createParser("\"invalid_string\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNull(deserializer.deserialize(jp, ctxt));
    }
}