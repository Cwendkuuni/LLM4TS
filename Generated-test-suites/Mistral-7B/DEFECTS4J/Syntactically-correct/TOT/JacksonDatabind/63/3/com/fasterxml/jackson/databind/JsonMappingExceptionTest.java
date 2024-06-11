package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonProcessingException exception = new JsonProcessingException("Invalid JSON");
        JsonMappingException jsonException = new JsonMappingException(exception);

        String json = mapper.writeValueAsString(jsonException);
        ObjectNode node = mapper.readTree(json);

        assertEquals("com.fasterxml.jackson.databind.JsonMappingException", node.getClass().getSimpleName());
        assertNotNull(node.get("path"));
    }

    @Test
    public void testJsonMappingExceptionDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"path\":[{\"from\":1,\"fieldName\":\"field1\"},{\"from\":2,\"index\":0}],\"message\":\"Invalid JSON\"}";
        JsonMappingException jsonException = mapper.readValue(json, JsonMappingException.class);

        assertNotNull(jsonException.getProcessor());
        assertEquals(2, jsonException.getPath().size());
        assertEquals(1, jsonException.getPath().get(0).getIndex());
        assertEquals("field1", jsonException.getPath().get(0).getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithPath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String jsonInput = "{\"name\":{\"age\":[1,2,3]}}";
        ObjectNode rootNode = mapper.readTree(jsonInput);

        ObjectMapper mapper2 = new ObjectMapper();
        JsonMappingException jsonException = mapper2.readValue(rootNode.get("name").toString(), JsonMappingException.class);

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(jsonInput.getBytes()));
        JsonMappingException expectedException = JsonMappingException.from(parser, "Unexpected value: [1, 2, 3] (use jsonProperty or jsonSetter to access this property)");
        expectedException.prependPath(parser, "name");

        assertEquals(expectedException, jsonException);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String jsonInput = "[1, 2, 3]";
        JsonMappingException jsonException = new JsonMappingException(jsonInput, JsonMappingException.FromContext.PARSER, "Unexpected value: [1, 2, 3]");

        JsonMapper.DefaultTypingResolver typeResolver = mapper.getTypeFactory().constructTyping(JsonMappingException.class, JsonMappingException.class);
        JsonNode node = mapper.readTree(jsonException.toString());

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(jsonInput.getBytes()));
        JsonMappingException expectedException = JsonMappingException.from(parser, "Unexpected value: [1, 2, 3]", JsonMappingException.Reference.withIndex(2));

        JsonMappingException deserializedException = mapper.readValue(node, typeResolver);

        assertEquals(expectedException, deserializedException);
    }
}