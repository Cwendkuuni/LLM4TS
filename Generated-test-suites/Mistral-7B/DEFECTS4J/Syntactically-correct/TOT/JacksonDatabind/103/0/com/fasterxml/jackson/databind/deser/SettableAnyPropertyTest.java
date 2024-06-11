package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private ObjectMapper mapper;
    private SettableAnyProperty settableAnyProperty;

    @BeforeClass
    public void setUp() throws IOException {
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        settableAnyProperty = mapper.findSpecialValueDeserializer(SettableAnyProperty.class, Map.class, String.class);
    }

    @Test
    public void testSettableAnyPropertySimple() throws IOException {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        ObjectNode jsonNode = JsonFactory.instance.createObjectNode();
        jsonNode.put("anyProperty", input);

        Map<String, Object> result = mapper.readValue(jsonNode, Map.class);
        Assert.assertEquals(result.get("name"), "John Doe");
        Assert.assertNotNull(result.get(_settableAnyProperty.getProperty().getName()));
    }

    @Test
    public void testSettableAnyPropertyNullValue() throws IOException {
        Map<String, Object> input = new HashMap<>();
        ObjectNode jsonNode = JsonFactory.instance.createObjectNode();
        jsonNode.put("anyProperty", null);

        Map<String, Object> result = mapper.readValue(jsonNode, Map.class);
        Assert.assertNull(result.get(_settableAnyProperty.getProperty().getName()));
    }

    @Test
    public void testSettableAnyPropertyComplex() throws IOException {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("age", 30);
        ObjectNode jsonNode = JsonFactory.instance.createObjectNode();
        jsonNode.put("anyProperty", input);

        Map<String, Object> result = mapper.readValue(jsonNode, Map.class);
        Assert.assertEquals(result.get("name"), "John Doe");
        Assert.assertEquals(result.get("age"), 30);
        Assert.assertNotNull(result.get(_settableAnyProperty.getProperty().getName()));
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyPropertyInvalidType() throws IOException {
        ObjectNode jsonNode = JsonFactory.instance.createObjectNode();
        jsonNode.put("anyProperty", 123);

        Map<String, Object> result = mapper.readValue(jsonNode, Map.class);
        Assert.fail("Should throw JsonMappingException");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyPropertyMissingProperty() throws IOException {
        ObjectNode jsonNode = JsonFactory.instance.createObjectNode();

        Map<String, Object> result = mapper.readValue(jsonNode, new TypeReference<Map<String, Object>>() {});
        Assert.fail("Should throw JsonMappingException");
    }

    @Test
    public void testSettableAnyPropertyCustomTypeDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
                return stringDeserializer.deserialize(p, ctxt);
            }

            @Override
            public JsonDeserializer<?> getDeserializer(DeserializationContext ctxt, BeanProperty property, Type descriptor) {
                return stringDeserializer;
            }
        };

        SettableAnyProperty settableAnyPropertyWithTypeDeserializer = mapper.findSpecialValueDeserializer(SettableAnyProperty.class, Map.class, String[].class, typeDeserializer);

        ObjectNode jsonNode = JsonFactory.instance.createObjectNode();
        jsonNode.put("anyProperty", new String[]{"Hello", "World"});

        Map<String, Object> result = mapper.readValue(jsonNode, Map.class);
        Assert.assertNotNull(result.get(_settableAnyProperty.getProperty().getName()));
        Assert.assertEquals(result.get(_settableAnyProperty.getProperty().getName()), new String[]{"Hello", "World"});
    }
}