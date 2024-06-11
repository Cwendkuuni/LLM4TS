package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testJsonNodeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        // Test null value
        JsonParser p = mapper.getFactory().createParser("null");
        JsonNode node = deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertNull(node.asText());

        // Test empty JSON Object
        p = mapper.getFactory().createParser("{}");
        node = deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), ObjectNode.class);

        // Test empty JSON Array
        p = mapper.getFactory().createParser("[]");
        node = deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), ArrayNode.class);

        // Test JSON Object with fields
        p = mapper.getFactory().createParser("{\"field1\": 1, \"field2\": [\"hello\", \"world\"]}");
        node = deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), ObjectNode.class);
        Assert.assertEquals(node.get("field1").asInt(), 1);
        Assert.assertEquals(node.get("field2").getClass(), ArrayNode.class);
        Assert.assertEquals(node.get("field2").size(), 2);
        Assert.assertEquals(node.get("field2").get(0).asText(), "hello");
        Assert.assertEquals(node.get("field2").get(1).asText(), "world");

        // Test JSON Array
        p = mapper.getFactory().createParser("[1, 2, 3.5, \"hello\", null, true]");
        node = deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), ArrayNode.class);
        Assert.assertEquals(node.size(), 7);
        Assert.assertEquals(node.get(0).asInt(), 1);
        Assert.assertEquals(node.get(1).asInt(), 2);
        Assert.assertEquals(node.get(2).asDouble(), 3.5);
        Assert.assertEquals(node.get(3).asText(), "hello");
        Assert.assertEquals(node.get(5).asBoolean(), true);
        Assert.assertNull(node.get(6));

        // Test JSON Object with fields and an array
        p = mapper.getFactory().createParser("{\"field1\": 1, \"field2\": [1, 2, 3.5], \"field3\": \"hello\"}");
        node = deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), ObjectNode.class);
        Assert.assertEquals(node.get("field1").asInt(), 1);
        Assert.assertEquals(node.get("field2").getClass(), ArrayNode.class);
        Assert.assertEquals(node.get("field2").size(), 3);
        Assert.assertEquals(node.get("field2").get(0).asInt(), 1);
        Assert.assertEquals(node.get("field2").get(1).asInt(), 2);
        Assert.assertEquals(node.get("field2").get(2).asDouble(), 3.5);
        Assert.assertEquals(node.get("field3").asText(), "hello");
    }

    @Test
    public void testObjectDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectDeserializer deserializer = (ObjectDeserializer) JsonNodeDeserializer.getDeserializer(ObjectNode.class);

        // Test empty JSON Object
        JsonParser p = mapper.getFactory().createParser("{}");
        ObjectNode node = (ObjectNode) deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), ObjectNode.class);
    }

    @Test
    public void testArrayDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayDeserializer deserializer = (ArrayDeserializer) JsonNodeDeserializer.getDeserializer(ArrayNode.class);

        // Test empty JSON Array
        JsonParser p = mapper.getFactory().createParser("[]");
        ArrayNode node = (ArrayNode) deserializer.deserialize(p, new TestContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), ArrayNode.class);
    }

    static class TestContext extends DeserializationContext {
        @Override
        public JsonDeserializer<?> findDeserializer(Class<?> deserializerClass) {
            return JsonNodeDeserializer.getDeserializer(deserializerClass);
        }

        @Override
        public JsonDeserializer<?> createContextualDeserializer(DeserializerProvider provider, BeanDescription beanDesc) throws JsonMappingException, IOException {
            return JsonNodeDeserializer.getDeserializer(beanDesc.getType());
        }

        @Override
        public ObjectFactory getFactory() {
            return mapper.getFactory();
        }

        @Override
        public DeserializationFeature[] getActiveFeatures() {
            return new DeserializationFeature[0];
        }
    }
}