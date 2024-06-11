package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testDefaultInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);

        Object result = instantiator.createUsingDefault(mapper.getDeserializationContext().forRoot(), null);
        assertNull(result);
    }

    @Test
    public void testStringInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);

        String input = "{\"value\": \"Hello World\"}";
        ObjectNode node = mapper.readTree(input);
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node.path("value") }, null);
        assertEquals("Hello World", result);
    }

    @Test
    public void testNullInstantiatorForString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);

        ObjectNode node = mapper.readTree("{}");
        try {
            instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] {}, node);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("No String-argument constructor/factory method", e.getMessage());
        }
    }

    @Test
    public void testStringInstantiatorWithEmptyInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);

        ObjectNode node = mapper.readTree("{}");
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), null, node);
        assertNull(result);
    }

    @Test
    public void testBooleanInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);

        Object result = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertTrue(result);

        result = instantiator.createFromBoolean(mapper.getDeserializationContext(), false);
        assertFalse(result);
    }

    @Test
    public void testBooleanInstantiatorWithMissingArgument() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);

        ObjectNode node = mapper.readTree("{}");
        try {
            instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] {}, node);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("No boolean-argument constructor/factory method to deserialize from Object value", e.getMessage());
        }
    }

    @JsonDeserialize(using = customValueInstantiator.class)
    static class CustomValue {
        private String value;

        public CustomValue() {}

        public CustomValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static CustomValue createFromJson(ObjectNode node) {
            return new CustomValue(node.path("value").asText());
        }
    }

    static class customValueInstantiator extends ValueInstantiator {
        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args, PropertyName name) throws IOException {
            ObjectNode node = (ObjectNode) args[0];
            return new CustomValue(node.path("value").asText());
        }
    }

    @Test
    public void testCustomInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(CustomValue.class);

        ObjectNode node = mapper.readTree("{\"value\": \"Hello World\"}");
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node }, null);
        CustomValue expected = new CustomValue("Hello World");
        assertEquals(expected.getValue(), result.getClass().getMethod("getValue").invoke(result));
    }
}