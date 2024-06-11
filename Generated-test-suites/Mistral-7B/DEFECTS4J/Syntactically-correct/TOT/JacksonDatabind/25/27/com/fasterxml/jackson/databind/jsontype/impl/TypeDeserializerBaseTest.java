package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private DeserializationContext ctxt;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;

        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };

        BeanProperty property = new BeanProperty("name", String.class);
        TypeDeserializer deserializerForProperty = deserializer.forProperty(property);
        assertNull(deserializerForProperty);
    }

    @Test
    public void testFindDeserializerNullTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();

        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            public JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };

        Object result = deserializer._findDeserializer(ctxt, null);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerKnownTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = new StubDeserializer();
        idResolver.setValueIdMapper(new StandardTypeIdResolver.StdTypeIdMapper() {
            @Override
            public String idFromValue(Class<?> cls) {
                return typeId;
            }
        });
        idResolver.putValueIdForClass(Class.forName(typeId), deserializer);

        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            public JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return super._findDeserializer(ctxt, typeId);
            }
        };

        Object result = deserializerBase._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerUnknownTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typeId = "com.example.UnknownClass";

        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            public JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return super._findDeserializer(ctxt, typeId);
            }
        };

        Object result = deserializerBase._findDeserializer(ctxt, typeId);
        fail("Expected JsonMappingException but got " + result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        Class<? > defaultImpl = String.class;

        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, defaultImpl) {
            @Override
            public JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Object result = deserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializerNullType() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        Class<? > defaultImpl = String.class;

        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, defaultImpl) {
            @Override
            public JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        JsonDeserializer<Object> defaultImplDeserializer = mapper.findContextualValueDeserializer(defaultImpl, null);
        result = deserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(defaultImplDeserializer, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        TypeIdResolver baseTypeIdResolver = mapper.getTypeIdResolver();
        String typeId = "baseType";

        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            public Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }
        };

        ObjectMapper mapperWithTypeIdHandler = new ObjectMapper();
        mapperWithTypeIdHandler.setTypeIdHandler(new TypeIdHandlerBase() {
            @Override
            public String idFromValue(Object value) {
                if (value instanceof String) {
                    return typeId;
                }
                return null;
            }
        });

        String json = "{\"name\":\"John\", \"type\":\"baseType\"}";
        Object result = mapperWithTypeIdHandler.readValue(new ByteArrayInputStream(json.getBytes()), baseType, deserializerBase);
        assertNull(result);

        ctxt = mapperWithTypeIdHandler.getDeserializationContext();
        result = deserializerBase._deserializeWithNativeTypeId(mapperWithTypeIdHandler.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), ctxt, typeId);
        assertNull(result);

        idResolver.setValueIdMapper(new StandardTypeIdResolver.StdTypeIdMapper() {
            @Override
            public String idFromValue(Class<?> cls) {
                return "baseType";
            }
        });

        result = deserializerBase._deserializeWithNativeTypeId(mapperWithTypeIdHandler.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), ctxt, null);
        assertNull(result);

        idResolver.putValueIdForClass(baseType.getRawClass(), new StubDeserializer());

        result = deserializerBase._deserializeWithNativeTypeId(mapperWithTypeIdHandler.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), ctxt, null);
        assertNotNull(result);
    }

    private static class StubDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "Stub";
        }
    }
}