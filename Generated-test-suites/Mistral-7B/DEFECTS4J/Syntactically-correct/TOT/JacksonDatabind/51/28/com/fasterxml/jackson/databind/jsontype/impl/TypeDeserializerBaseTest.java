package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase deserializer;
    private TypeIdResolver idResolver;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        idResolver = new TypeIdResolverBase();
        deserializer = new TypeDeserializerBaseBase(mapper.getTypeFactory().constructType(Object.class), idResolver, "", false, null);
    }

    @Test
    public void testDeserializeWithBaseType() throws IOException {
        String json = "{\"name\": \"John\", \"type\": \"Person\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonDeserializer<Object> deserializer = deserializer.forProperty(new BeanProperty("name"));
        Object result = deserializer.deserialize(node, new DeserializationContext(mapper, null, null));
        assertEquals("John", result);
    }

    @Test
    public void testDeserializeWithTypeId() throws IOException {
        String json = "{\"name\": \"John\", \"type\": \"Person:JohnDoe\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonDeserializer<Object> deserializer = deserializer.forProperty(new BeanProperty("name"));
        Object result = deserializer.deserialize(node, new DeserializationContext(mapper, idResolver, null));
        assertEquals("JohnDoe", result.getClass().getSimpleName());
    }

    @Test
    public void testDeserializeWithMissingTypeId() throws IOException {
        String json = "{\"name\": \"John\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonDeserializer<Object> deserializer = deserializer.forProperty(new BeanProperty("name"));
        Object result = deserializer.deserialize(node, new DeserializationContext(mapper, idResolver, null));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithInvalidTypeId() throws IOException {
        String json = "{\"name\": \"John\", \"type\": \"InvalidType\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonDeserializer<Object> deserializer = deserializer.forProperty(new BeanProperty("name"));
        try {
            deserializer.deserialize(node, new DeserializationContext(mapper, idResolver, null));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Could not resolve type id 'InvalidType' for type 'class java.lang.Object' ", e.getMessage());
        }
    }

    @Test
    public void testDeserializeWithDefaultImpl() throws IOException {
        String json = "{\"name\": null}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = deserializer.forProperty(new BeanProperty("name")).deserialize(node, new DeserializationContext(mapper, idResolver, null));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        JavaType customType = mapper.getTypeFactory().constructType(HashMap.class);
        JsonDeserializer<Object> customDeserializer = new StdSerializer<Map<String, String>>(HashMap.class) {};
        deserializer = new TypeDeserializerBaseBase(mapper.getTypeFactory().constructType(Object.class), idResolver, "", false, customType) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return customDeserializer;
            }
        };
        String json = "{\"type\": \"Map\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = deserializer.forProperty(new BeanProperty("type")).deserialize(node, new DeserializationContext(mapper, idResolver, null));
        assertEquals(HashMap.class, result.getClass());
    }
}