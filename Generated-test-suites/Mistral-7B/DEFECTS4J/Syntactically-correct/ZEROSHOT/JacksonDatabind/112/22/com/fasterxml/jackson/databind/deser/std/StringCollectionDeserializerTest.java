package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testStringCollectionDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test default deserialization
        String jsonString1 = "[\"abc\",\"def\"]";
        JsonNode jsonNode1 = mapper.readTree(jsonString1);
        Collection<String> collection1 = mapper.readValue(jsonNode1, StringCollectionDeserializer.class);
        assertEquals(2, collection1.size());
        assertTrue(collection1.containsAll(Arrays.asList("abc", "def")));

        // Test custom value deserializer
        JsonDeserializer<String> customValueDeserializer = new StringUpperCaseDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                customValueDeserializer, null, null, null, null);
        String jsonString2 = "[\"abc\",\"def\"]";
        JsonNode jsonNode2 = mapper.readTree(jsonString2);
        Collection<String> collection2 = mapper.readerForListOf(String.class).readValue(jsonNode2);
        assertEquals(2, collection2.size());
        assertTrue(collection2.containsAll(Arrays.asList("ABC", "DEF")));

        // Test null value handling
        String jsonString3 = "[null, \"def\"]";
        JsonNode jsonNode3 = mapper.readTree(jsonString3);
        Collection<String> collection3 = mapper.readValue(jsonNode3, StringCollectionDeserializer.class);
        assertEquals(2, collection3.size());
        assertNull(collection3.get(0));
        assertTrue(collection3.get(1).equals("def"));

        // Test empty collection deserialization
        String jsonString4 = "[]";
        JsonNode jsonNode4 = mapper.readTree(jsonString4);
        Collection<String> collection4 = mapper.readValue(jsonNode4, StringCollectionDeserializer.class);
        assertTrue(collection4.isEmpty());

        // Test deserialization with delegate deserializer
        String jsonString5 = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Doe\",\"age\":25}]";
        JsonNode jsonNode5 = mapper.readTree(jsonString5);
        Collection<String> collection5 = mapper.readerForListOf(String.class).withResolved(
                mapper.readerForValue(Object.class),
                mapper.readerForValue(Object.class),
                null,
                null).readValue(jsonNode5);
        assertEquals(2, collection5.size());
        assertFalse(collection5.contains(null));
        for (String s : collection5) {
            ObjectNode node = (ObjectNode) mapper.readTree(s);
            assertEquals("name", node.fields().nextKeyValue().getKey());
            assertTrue(node.has("age"));
        }
    }

    static class StringUpperCaseDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString().toUpperCase();
        }
    }
}