package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonMappingException_withParser() throws Exception {
        String json = "{\"name\":\"John\", \"age\":\"30\"}";
        JsonMappingException e = JsonMappingException.from(mapper.getFactory().createParser(json), "Unexpected token");
        assertNotNull(e.getProcessor());
        assertEquals("Unexpected token (through reference chain: root->name)", e.getMessage());
        assertEquals(JsonMappingException.class, e.getClass());
    }

    @Test
    public void testJsonMappingException_withGenerator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Object());
        JsonMappingException e = JsonMappingException.from(mapper.getFactory().createGenerator(new StringWriter()), "Internal Error");
        assertNotNull(e.getProcessor());
        assertEquals("Internal Error (through reference chain: com.fasterxml.jackson.databind.util.StreamWriteContext[StreamRootElementWriter@715c2f78]->JsonGenerator)", e.getMessage());
        assertEquals(JsonMappingException.class, e.getClass());
    }

    @Test
    public void testJsonMappingException_withDeserializationContext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = JsonMappingException.from(mapper.getDeserializationContext().getParser("{invalidJson}"), "Unexpected token");
        assertNotNull(e.getProcessor());
        assertEquals("Unexpected token (through reference chain: com.fasterxml.jackson.databind.JsonDeserializationContext[JsonParser@427d9a62]->JsonParser)", e.getMessage());
        assertEquals(JsonMappingException.class, e.getClass());
    }

    @Test
    public void testJsonMappingException_withSerializerProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = JsonMappingException.from(mapper.getSerializerProvider(), "Internal Error");
        assertNotNull(e.getProcessor());
        assertEquals("Internal Error (through reference chain: com.fasterxml.jackson.databind.JsonSerializerProvider[JsonGenerator@39b2e469]->JsonGenerator)", e.getMessage());
        assertEquals(JsonMappingException.class, e.getClass());
    }

    @Test
    public void testJsonMappingException_withReference() throws Exception {
        String json = "{\"name\":\"John\", \"objects\":[{\"age\":1}, {\"age\":2}, {\"age\":3}]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = mapper.readValue(json, JsonMappingException.class);
        assertEquals(JsonMappingException.class, e.getClass());
        assertEquals("(through reference chain: root->objects[2]->age)", e.getMessage());
        assertEquals(JsonMappingException.class, e.getClass());
        Reference ref = e.getPath().getLast();
        assertNotNull(ref.getFrom());
        assertEquals("root", ref.getFieldName());
        assertEquals(Integer.class, ref.getFrom().getClass());
        assertEquals(2, ref.getIndex());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException_withInvalidJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue("{invalidJson}", Object.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException_withInvalidJson_withPath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = JsonMappingException.from(mapper.getFactory().createParser("{invalidJson}"), "Unexpected token");
        assertEquals("Unexpected token (through reference chain: com.fasterxml.jackson.databind.JsonDeserializationContext[JsonParser@3c958151]->JsonParser)", e.getMessage());
    }
}