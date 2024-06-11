package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        JsonNode rootNode = mapper.readTree(jsonInput);

        try {
            mapper.writeValueAsString(rootNode.get("cars").get(2).toString()); // index out of bounds
        } catch (JsonMappingException e) {
            assertEquals("Root path 'cars[2]' does not exist", e.getDescription());
            assertEquals("expected JSON String but found BINARY STRING", e.getMessage());
            assertNull(e.getProcessor());
            List<Reference> path = e.getPath();
            assertNotNull(path);
            assertEquals(2, path.size());
            Reference ref = path.get(0);
            assertNull(ref.getFrom());
            assertEquals("cars", ref.getFieldName());
            assertEquals("-1", String.valueOf(ref.getIndex()));
            ref = path.get(1);
            assertNull(ref.getFrom());
            assertEquals("2", String.valueOf(ref.getIndex()));
            assertEquals("[Ford, BMW, Fiat]", ref.getDescription());
        }
    }

    @Test
    public void testDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = "{\"name\": \"John\", \"age\": 30, \"cars\": [{\"make\": \"Ford\"}, {\"make\": \"BMW\"}, {\"make\": \"Fiat\"}]}";
        JsonNode rootNode = mapper.readTree(jsonInput);

        try {
            JsonNode node = mapper.readTree(rootNode.get("cars").get(2).toString()); // invalid JSON
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Can not construct instance of com.fasterxml.jackson.databind.JsonMappingException$Reference (no empty constructor)", e.getDescription());
            assertNull(e.getProcessor());
            List<Reference> path = e.getPath();
            assertNotNull(path);
            assertEquals(2, path.size());
            Reference ref = path.get(0);
            assertNull(ref.getFrom());
            assertEquals("cars", ref.getFieldName());
            assertEquals("-1", String.valueOf(ref.getIndex()));
            ref = path.get(1);
            assertNull(ref.getFrom());
            assertEquals("2", String.valueOf(ref.getIndex()));
            assertEquals("[\"Fiat\"]", ref.getDescription());
        }
    }

    @Test
    public void testCustomMessage() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        JsonNode rootNode = mapper.readTree(jsonInput);

        try {
            mapper.writerForTree(new File("output.txt")).writeValue(rootNode.get("cars").get(3)); // non-existent file
        } catch (JsonMappingException e) {
            assertEquals("Error writing value to file output.txt", e.getMessage());
            assertNull(e.getProcessor());
            List<Reference> path = e.getPath();
            assertNotNull(path);
            assertEquals(0, path.size());
            assertNull(e.getPath());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithPath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"data\": [1, 2, 3, 4]}");
        mapper.setConfiguration(JsonMapper.Feature.FAIL_ON_EMPTY_BEANS);

        try {
            mapper.writerForTree(new File("output.txt")).writeValue(rootNode.get(2)); // write to non-existent file
        } catch (JsonProcessingException e) {
            throw new JsonMappingException(e, new Reference(mapper, "output.txt", "writeValue"));
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithPath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = "{\"data\": [1, 2, null, 4]}";

        try {
            JsonNode rootNode = mapper.readTree(jsonInput);
            Object obj = mapper.readValue(rootNode.get(2).toString(), String.class);
        } catch (JsonMappingException e) {
            throw new JsonMappingException(e, new Reference(mapper, "data[2]", null));
        }
    }

    @Test
    public void testMaxRefsLimit() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = "[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100]";

        try {
            JsonNode node = mapper.readTree(jsonInput);
            JsonMappingException e = new JsonMappingException(null, "Test exception");
            for (int i = 0; i < 101; i++) {
                e.prependPath(node.get(i));
            }
            fail();
        } catch (JsonMappingException ex) {
            assertTrue(ex.getMessage().contains("Maximum reference chain size (1000) exceeded"));
        }
    }
}