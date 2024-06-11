package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private static final String TYPE_ID = "testTypeId";
    private static final JavaType BASE_TYPE = TypeFactory.defaultInstance().constructType(Object.class);
    private static final JavaType DEFAULT_IMPL = TypeFactory.defaultInstance().constructType(String.class);
    private static final TypeIdResolver idResolver = new TypeIdResolverBase();
    private static final TypeDeserializerBase deserializerBase;

    static {
        deserializerBase = new TypeDeserializerBase(BASE_TYPE, idResolver, "typePropertyName", false, DEFAULT_IMPL) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                // no-op
                return this;
            }
        };
    }

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
    }

    @Test
    public void testDeserializeWithValidTypeId() throws IOException {
        String json = "{\"typeId\":\"" + TYPE_ID + "\", \"value\":\"testStringValue\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        Object result = deserializerBase.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals("testStringValue", result);
    }

    @Test
    public void testDeserializeWithMissingTypeId() throws IOException {
        String json = "{\"value\":\"testStringValue\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        Object result = deserializerBase.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(DEFAULT_IMPL.getDefaultConstructor().invoke(), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws IOException {
        String json = "{\"typeId\":\"unknownTypeId\", \"value\":\"testStringValue\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        deserializerBase.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        deserializerBase._deserializers.put(TYPE_ID, stringDeserializer);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"typeId\":\"" + TYPE_ID + "\", \"value\":\"testStringValue\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        Object result = deserializerBase.findValueDeserializer(jsonParser, objectMapper.getDeserializationContext(), null);
        Assert.assertEquals(stringDeserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object result = deserializerBase.findDefaultImplDeserializer(objectMapper.getDeserializationContext());
        Assert.assertEquals(DEFAULT_IMPL, result.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String json = "{\"typeId\":\"unknownTypeId\", \"value\":\"testStringValue\"}";
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jsonParser = jsonFactory.createParser(json);
        TypeDeserializer baseType = deserializerBase.handleUnknownTypeId(jsonParser, ctxt, null);
        JsonDeserializer<Object> deserializer = ctxt.findContextualValueDeserializer(baseType.baseType(), null);
        Object result = deserializer.deserialize(jsonParser, ctxt);
        Assert.assertEquals(DEFAULT_IMPL, result.getClass());
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        String json = "{\"value\":\"testStringValue\"}";
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jsonParser = jsonFactory.createParser(json);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(BASE_TYPE, idResolver, "typePropertyName", false, DEFAULT_IMPL) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                // no-op
                return this;
            }
        };
        TypeDeserializer baseType = deserializerBase.handleMissingTypeId(jsonParser, ctxt, null);
        JsonDeserializer<Object> deserializer = ctxt.findContextualValueDeserializer(baseType.baseType(), null);
        Object result = deserializer.deserialize(jsonParser, ctxt);
        Assert.assertEquals(DEFAULT_IMPL, result.getClass());
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}