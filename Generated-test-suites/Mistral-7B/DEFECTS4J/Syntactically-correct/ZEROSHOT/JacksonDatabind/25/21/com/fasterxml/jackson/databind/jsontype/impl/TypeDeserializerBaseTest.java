package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest extends BaseTest {

    private static final String TEST_CLASS_NAME = TypeDeserializerBase.class.getName();
    private static final String BASE_TYPE_NAME = "java.lang.String";
    private static final String PROPERTY_NAME = "_property";
    private static final String TYPE_PROPERTY_NAME = "_typePropertyName";
    private static final boolean TYPE_ID_VISIBLE = true;
    private static final Class<?> DEFAULT_IMPL = String[].class;

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase<Object> createTestInstance(Class<? extends TypeDeserializerBase<Object>> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        return (TypeDeserializerBase<Object>) clazz.getConstructor(JsonType.Id.class, TypeIdResolver.class, STRING, BOOLEAN, DEFAULT_IMPL)
                .newInstance(JsonType.Id.class, idResolver, TYPE_PROPERTY_NAME, TYPE_ID_VISIBLE, DEFAULT_IMPL);
    }

    @Test
    public void testTypeDeserializerBaseConstruction() throws IOException {
        TypeDeserializerBase<Object> instance = createTestInstance(TypeDeserializerBase.class);
        assertNotNull(instance);
        assertEquals(BASE_TYPE_NAME, instance.baseTypeName());
        assertEquals(TYPE_PROPERTY_NAME, instance.getPropertyName());
        assertSame(idResolver, instance.getTypeIdResolver());
        assertNull(instance._property);
        assertNull(instance._defaultImplDeserializer);
    }

    @Test
    public void testTypeDeserializerBaseForProperty() throws IOException {
        TypeDeserializerBase<Object> instance = createTestInstance(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty("testProperty", Object.class);
        TypeDeserializerBase<Object> instanceForProperty = instance.forProperty(property);
        assertNotNull(instanceForProperty);
        assertNotSame(instance, instanceForProperty);
        assertEquals(BASE_TYPE_NAME, instanceForProperty.baseTypeName());
        assertEquals(TYPE_PROPERTY_NAME, instanceForProperty.getPropertyName());
        assertEquals(idResolver, instanceForProperty.getTypeIdResolver());
        assertNull(instanceForProperty._property);
        assertNull(instanceForProperty._defaultImplDeserializer);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase<Object> instance = createTestInstance(TypeDeserializerBase.class);
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "typeId1";
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer<>();
        instance._deserializers.put(typeId, deserializer);

        JsonDeserializer<Object> result = instance._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase<Object> instance = createTestInstance(TypeDeserializerBase.class);
        DeserializationContext ctxt = new TestDeserializationContext();

        JsonDeserializer<Object> result = instance._findDefaultImplDeserializer(ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase<Object> instance = createTestInstance(TypeDeserializerBase.class);
        JsonParser jp = createContextualTestParser(instance, new ObjectMapper());
        String typeId = "typeId1";
        jp.nextTokenId();
        jp.setValue(typeId);

        Object result = instance._deserializeWithNativeTypeId(jp, new TestDeserializationContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase<Object> instance = createTestInstance(TypeDeserializerBase.class);
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "unknownTypeId";

        instance._handleUnknownTypeId(ctxt, typeId, idResolver, _defaultImplType);
        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testToString() throws IOException {
        TypeDeserializerBase<Object> instance = createTestInstance(TypeDeserializerBase.class);
        String result = instance.toString();
        assertTrue(result.contains(TEST_CLASS_NAME));
        assertTrue(result.contains(BASE_TYPE_NAME));
        assertTrue(result.contains("id-resolver:" + idResolver));
    }

    private static class TestDeserializationContext extends DeserializationContext {
        public TestDeserializationContext() {
            super(new TestAnnotationIntrospector(), new ObjectMapper());
        }

        @Override
        public Object readValue(Class<?> clazz, JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object findContextualValueDeserializer(Class<?> deserializerType, BeanProperty property)
                throws IOException {
            return null;
        }
    }

    private static class StubJsonDeserializer<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static JavaType _defaultImplType = TypeFactory.defaultInstance().constructType(Object[].class);
}