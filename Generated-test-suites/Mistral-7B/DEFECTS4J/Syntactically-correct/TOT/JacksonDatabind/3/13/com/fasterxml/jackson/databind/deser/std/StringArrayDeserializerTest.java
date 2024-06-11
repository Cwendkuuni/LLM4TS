package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testStandardDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper(provider);

        String[] expected = {"test1", "test2", "test3"};
        JsonNode node = mapper.readTree("[\"" + String.join("\"\",\"", expected) + "\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(node.asArray(), new TestContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializationWithCustomElementDeserializer() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper(provider);

        JsonDeserializer<Integer> customDeserializer = new StdDeserializer<Integer>(Integer.class) {
            @Override
            public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsInt();
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] expected = {1, 2, 3};
        JsonNode node = mapper.readTree("[\"" + String.join("\"", expected) + "\"]");
        String[] result = deserializer.deserialize(node.asArray(), new TestContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEmptyArrayDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper(provider);

        JsonNode node = mapper.readTree("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(node.asArray(), new TestContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testNullDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper(provider);

        JsonNode node = mapper.readTree("null");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(node, new TestContext());
        assertNull(result);
    }

    @Test
    public void testInvalidInputDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper(provider);

        JsonNode node = mapper.readTree("invalidJson");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserialize(node, new TestContext());
            fail("Expected exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializationWithTypeHandler() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper(provider);

        TypeFactory typeFactory = mapper.getTypeFactory();
        ObjectType stringArrayType = typeFactory.constructArrayType(String.class);
        TypeDeserializer typeDeserializer = mapper.getPolymorphicTypeDeserializer(stringArrayType);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"test1", "test2", "test3"};
        JsonNode node = mapper.readTree("[\"" + String.join("\"", expected) + "\"]");
        String[] result = (String[]) deserializer.deserializeWithType(node.asArray(), new TestContext(), typeDeserializer);
        assertArrayEquals(expected, result);
    }

    /**
     * Test context for deserialization tests.
     */
    public static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getConfig() {
            return null;
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }

        @Override
        public void findAndBindValue(Object value, BeanProperty property) throws JsonMappingException {
            // No-op
        }

        @Override
        public void findAndRegisterValueDeserializer(DeserializerProvider provider, BeanProperty property, TypeDeserializer typeDeserializer) throws JsonMappingException {
            // No-op
        }

        @Override
        public void findAndRegisterValueDeserializer(DeserializerProvider provider, TypeDeserializer typeDeserializer) throws JsonMappingException {
            // No-op
        }

        @Override
        public void handleNonArray(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            // No-op
        }

        @Override
        public void handleNonContextualDeserialization(DeserializationContext ctxt, BeanProperty property, JsonDeserializer<?> deserializer) throws JsonMappingException {
            // No-op
        }

        @Override
        public void handleMissingValueDeserializer(DeserializationContext ctxt, BeanProperty property, TypeDeserializer typeDeserializer) throws JsonMappingException {
            // No-op
        }

        @Override
        public void handleUnknownProperty(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            // No-op
        }
    }

    /**
     * Custom JsonProvider for testing.
     */
    public static class StringJsonProvider extends StandardJsonProvider {
        @Override
        public ObjectReader getFactory() {
            return null;
        }
    }

}