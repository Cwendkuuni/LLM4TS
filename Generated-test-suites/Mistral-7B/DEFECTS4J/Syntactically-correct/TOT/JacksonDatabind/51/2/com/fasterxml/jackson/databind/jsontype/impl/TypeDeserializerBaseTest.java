package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolverBase typeIdResolver;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        typeDeserializerBase = new TypeDeserializerBaseForString() {
            @Override
            protected Map<String, JsonDeserializer<Object>> createDeserializers() {
                return new HashMap<>();
            }
        };
        typeIdResolver = new TypeIdResolverBase();
    }

    @Test
    public void testBaseTypeDeserialization() throws IOException {
        String json = "{\"foo\":\"bar\"}";
        Map<String, Object> result = objectMapper.readValue(json, Map.class, typeDeserializerBase, typeIdResolver);
        Assert.assertEquals("bar", result.get("foo"));
    }

    @Test
    public void testTypeIdDeserialization() throws IOException {
        String json = "{\"typeId\":\"StringClass\",\"foo\":\"bar\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerSubtypes(new StringClass1(), new StringClass2());
        Map<String, Object> result = objectMapper.readValue(json, Map.class, typeDeserializerBase, typeIdResolver);
        Assert.assertTrue(result.get("foo") instanceof StringClass1);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeIdDeserialization() throws IOException {
        String json = "{\"typeId\":\"UnknownTypeId\",\"foo\":\"bar\"}";
        Map<String, Object> result = objectMapper.readValue(json, Map.class, typeDeserializerBase, typeIdResolver);
    }

    @Test
    public void testMissingTypeIdDeserialization() throws IOException {
        String json = "{\"foo\":\"bar\"}";
        Map<String, Object> result = objectMapper.readValue(json, Map.class, typeDeserializerBase, typeIdResolver);
        Assert.assertNull(result.get("foo"));
    }

    @Test
    public void testDefaultImplDeserialization() throws IOException {
        String json = "null";
        Map<String, Object> result = objectMapper.readValue(json, Map.class, typeDeserializerBase, typeIdResolver);
        Assert.assertNull(result.get("foo"));
    }

    @Test
    public void testTypeIdVisibleDeserialization() throws IOException {
        JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
                .use(JsonTypeInfo.Id.NAME)
                .property("typePropertyName")
                .build();

        String json = "{\"typeId\":\"StringClass\",\"foo\":\"bar\",\"typePropertyName\":\"typePropertyName\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        objectMapper.registerSubtypes(new StringClass1(), new StringClass2());
        Map<String, Object> result = objectMapper.readValue(json, Map.class, typeDeserializerBase, typeIdResolver, jsonTypeInfo);
        Assert.assertTrue(result.get("foo") instanceof StringClass1);
    }

    @Test
    public void testTypeIdVisibleDeserializationWithMissingTypeId() throws IOException {
        JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
                .use(JsonTypeInfo.Id.NAME)
                .property("typePropertyName")
                .build();

        String json = "{\"foo\":\"bar\",\"typePropertyName\":\"typePropertyName\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        Map<String, Object> result = objectMapper.readValue(json, Map.class, typeDeserializerBase, typeIdResolver, jsonTypeInfo);
        Assert.assertNull(result.get("foo"));
    }
}

class StringClass1 {
    private String value = "StringClass1";
}

class StringClass2 {
    private String value = "StringClass2";
}