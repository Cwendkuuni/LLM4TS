package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private static final String TEST_BASE_TYPE_NAME = "com.test.BaseType";
    private static final String TEST_DEFAULT_IMPL_TYPE_NAME = "com.test.DefaultImplType";
    private static final String TEST_SUB_TYPE_1_TYPE_NAME = "com.test.SubType1";
    private static final String TEST_SUB_TYPE_2_TYPE_NAME = "com.test.SubType2";

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws Exception {
        ObjectMapper.Feature[] features = new ObjectMapper.Feature[]{
                ObjectMapper.Feature.FAIL_ON_EMPTY_BEANS,
                ObjectMapper.Feature.FAIL_ON_INVALID_SUBTYPE,
                ObjectMapper.Feature.USE_STANDARD_JSON_PARSER
        };
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        objectMapper.configure(Feature.USE_STANDARD_PARSER, false);
        objectMapper.setSerializationInclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        objectMapper.setSerializationInclusion(JsonTypeInfo.As.WRAP_ROOT_VALUE);
        JavaType baseType = objectMapper.getTypeFactory().constructType(TEST_BASE_TYPE_NAME);
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
                if (id.equals("subType1")) {
                    return objectMapper.getTypeFactory().constructType(TEST_SUB_TYPE_1_TYPE_NAME);
                } else if (id.equals("subType2")) {
                    return objectMapper.getTypeFactory().constructType(TEST_SUB_TYPE_2_TYPE_NAME);
                } else {
                    return null;
                }
            }
        };
        JsonTypeInfo.As typeInclusion = JsonTypeInfo.As.WRAP_ROOT_VALUE;
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "type", false, TEST_DEFAULT_IMPL_TYPE_NAME) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return typeInclusion;
            }
        };
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = new BeanProperty("type", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(typeDeserializerBase, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws IOException {
        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws IOException {
        assertEquals(TEST_BASE_TYPE_NAME, typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testPropertyName() throws IOException {
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() throws IOException {
        assertEquals(typeDeserializerBase.getTypeIdResolver(), typeDeserializerBase._idResolver);
    }

    @Test
    public void testDefaultImpl() throws IOException {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        String json = "{}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), null);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeIdSubType1() throws IOException {
        String json = "{\"type\":\"subType1\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "subType1");
        Object result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(TEST_SUB_TYPE_1_TYPE_NAME, result.getClass().getName());
    }

    @Test
    public void testFindDeserializerWithTypeIdSubType2() throws IOException {
        String json = "{\"type\":\"subType2\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "subType2");
        Object result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(TEST_SUB_TYPE_2_TYPE_NAME, result.getClass().getName());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        String json = "{\"type\":\"unknownType\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "unknownType");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
        assertNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        String json = "{}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithNativeTypeIdSubType1() throws IOException {
        String json = "{\"type\":\"subType1\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "subType1");
        assertEquals(TEST_SUB_TYPE_1_TYPE_NAME, result.getClass().getName());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdSubType2() throws IOException {
        String json = "{\"type\":\"subType2\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "subType2");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeIdWithIdResolver() throws IOException {
        String json = "{\"type\":\"unknownType\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "unknownType");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String json = "{\"type\":\"unknownType\"}";
        Map<String, JsonDeserializer<Object>> deserializers = new HashMap<>();
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
                return null;
            }
        };
        Object result = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "unknownType", idResolver, null);
        assertNull(result);
    }
}