package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    /**
     * Test deserialize method with default deserializer
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDefaultDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Hello\",\"World\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer serializer = StringArrayDeserializer.instance;
        String[] result = serializer.deserialize(parser, ctxt);
        assertEquals("Hello", result[0]);
        assertEquals("World", result[1]);
    }

    /**
     * Test deserialize method with custom deserializer
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @Test
    public void testCustomDeserializer() throws IOException, JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Hello\",\"World\"]";
        ObjectReader reader = mapper.readerForArrayOf(String.class);
        String[] expected = new String[] {"Hello", "World"};
        StringArrayDeserializer serializer = new StringArrayDeserializer(mapper.getFactory().getValueDeserializer(String.class));
        String[] result = serializer.deserialize(mapper.createParser(json), mapper.getDeserializationContext());
        assertArrayEquals(expected, result);
    }

    /**
     * Test handleNonArray method
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @Test
    public void testHandleNonArray() throws IOException, JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"Hello World\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer serializer = StringArrayDeserializer.instance;
        String[] result = serializer.handleNonArray(parser, ctxt);
        assertEquals(null, result);
    }

    /**
     * Test createContextual method
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @Test
    public void testCreateContextual() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = new BeanProperty("test", String[].class);
        JsonDeserializer<String> valueDeserializer = mapper.getFactory().getValueDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(valueDeserializer);
        JsonDeserializer<?> result = deserializer.createContextual(mapper.getDeserializationContext(), property);
        assertTrue(result instanceof StringArrayDeserializer);
    }
}