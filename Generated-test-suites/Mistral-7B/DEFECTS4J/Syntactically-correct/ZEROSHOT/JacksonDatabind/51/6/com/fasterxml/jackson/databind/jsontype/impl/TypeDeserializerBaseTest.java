package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private static Object mapperSetup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        return mapper;
    }

    @org.junit.Test
    public void testForProperty() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, null, null, null, null, false, null);
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializerBase forProperty = base.forProperty(property);
        assertNotNull(forProperty);
        assertEquals(String.class, forProperty._baseType.getRawClass());
        assertEquals("testProperty", forProperty._typePropertyName);
        assertEquals(false, forProperty._typeIdVisible);
        assertEquals(ConcurrentHashMap.class, forProperty._deserializers.getClass());
        assertEquals(Map.class, forProperty._defaultImpl.getRawClass());

        TypeDeserializerBase copy = new TypeDeserializerBase(forProperty, property);
        assertEquals(forProperty._baseType, copy._baseType);
        assertEquals(forProperty._idResolver, copy._idResolver);
        assertEquals(forProperty._typePropertyName, copy._typePropertyName);
        assertEquals(forProperty._typeIdVisible, copy._typeIdVisible);
        assertEquals(forProperty._deserializers, copy._deserializers);
        assertEquals(forProperty._defaultImpl, copy._defaultImpl);
        assertEquals(forProperty._property, copy._property);
    }

    @org.junit.Test
    public void testGetTypeInclusion() {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, null, null, null, null, false, null);
        assertNull(base.getTypeInclusion());
    }

    @org.junit.Test
    public void testBaseTypeName() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, null, null, null, null, false, null);
        assertEquals("java.lang.String", base.baseTypeName());
    }

    @org.junit.Test
    public void testGetPropertyName() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, null, null, null, null, false, null);
        assertEquals("", base.getPropertyName());
    }

    @org.junit.Test
    public void testGetTypeIdResolver() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, null, null, null, null, false, null);
        assertNotNull(base.getTypeIdResolver());
    }

    @org.junit.Test
    public void testGetDefaultImpl() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, null, null, null, null, false, null);
        assertNull(base.getDefaultImpl());
    }

    @org.junit.Test
    public void testToString() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, null, null, null, null, false, null);
        String expected = "[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class java.lang.String; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@58666661; ]";
        assertEquals(expected, base.toString());
    }

    @org.junit.Test
    public void testFindDeserializer() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, idResolver, null, null, null, null, false, null);
        Object typeId = "MyClass";
        JsonDeserializer<Object> deser = base._findDeserializer(mapper.getDeserializationContext(), (String) typeId);
        assertNull(deser);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, idResolver, null, null, null, null, false, null);
        Object typeId = null;
        JsonDeserializer<Object> deser = base._findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNull(deser);
    }

    @org.junit.Test
    public void testFindDeserializerWithTypeId() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>() {};
        idResolver.addValueDeserializer(String.class, stringDeserializer);
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, idResolver, null, null, null, null, false, null);
        Object typeId = "java.lang.String";
        JsonDeserializer<Object> deser = base._findDeserializer(mapper.getDeserializationContext(), (String) typeId);
        assertEquals(stringDeserializer, deser);
    }

    @org.junit.Test
    public void testFindDeserializerWithCustomTypeId() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        JsonDeserializer<MyClass> myClassDeserializer = new StdDeserializer<MyClass>() {};
        idResolver.addValueDeserializer(MyClass.class, myClassDeserializer);
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, MyClass.class, idResolver, null, null, null, null, false, null);
        Object typeId = "MyID";
        JsonDeserializer<Object> deser = base._findDeserializer(mapper.getDeserializationContext(), (String) typeId);
        assertEquals(myClassDeserializer, deser);
    }

    @org.junit.Test
    public void testHandleUnknownTypeId() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, Object.class, idResolver, null, null, null, null, false, null);
        Object typeId = "MyClass";
        JavaType result = base._handleUnknownTypeId(mapper.getDeserializationContext(), (String) typeId, idResolver, Object.class);
        assertNull(result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdWithTypeIdResolver() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, Object.class, idResolver, null, null, null, null, false, null);
        Object typeId = "MyClass";
        JavaType result = base._handleUnknownTypeId(mapper.getDeserializationContext(), (String) typeId, idResolver, Object.class);
        assertNull(result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolver() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = new DefaultTypeIdResolver() {
            @Override
            public JavaType resolveTypeId(DeserializationContext ctxt, Object beanProperty, Type id) {
                if (id == null) {
                    return mapper.getTypeFactory().constructValueType(String.class);
                }
                return mapper.getTypeFactory().constructType(id.getClassLoader(), id.toString());
            }
        };
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, Object.class, idResolver, null, null, null, null, false, null);
        Object typeId = "MyClass";
        JavaType result = base._handleUnknownTypeId(mapper.getDeserializationContext(), (String) typeId, idResolver, Object.class);
        assertEquals(String.class, result.getRawClass());
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolverAndKnownTypeIds() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = new DefaultTypeIdResolver() {
            @Override
            public JavaType resolveTypeId(DeserializationContext ctxt, Object beanProperty, Type id) {
                if (id == null) {
                    return mapper.getTypeFactory().constructValueType(String.class);
                }
                if ("MyClass".equals(id.toString())) {
                    return mapper.getTypeFactory().constructType(MyClass.class);
                }
                return mapper.getTypeFactory().constructType(id.getClassLoader(), id.toString());
            }

            @Override
            public String getDescForKnownTypeIds() {
                return "MyClass";
            }
        };
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, Object.class, idResolver, null, null, null, null, false, null);
        Object typeId = "MyClass";
        JavaType result = base._handleUnknownTypeId(mapper.getDeserializationContext(), (String) typeId, idResolver, Object.class);
        assertEquals(MyClass.class, result.getRawClass());
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolverAndUnknownTypeIds() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = new DefaultTypeIdResolver() {
            @Override
            public JavaType resolveTypeId(DeserializationContext ctxt, Object beanProperty, Type id) {
                if (id == null) {
                    return mapper.getTypeFactory().constructValueType(String.class);
                }
                if ("MyClass".equals(id.toString())) {
                    return mapper.getTypeFactory().constructType(MyClass.class);
                }
                return mapper.getTypeFactory().constructType(id.getClassLoader(), id.toString());
            }
        };
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, Object.class, idResolver, null, null, null, null, false, null);
        Object typeId = "UnknownTypeID";
        JavaType result = base._handleUnknownTypeId(mapper.getDeserializationContext(), (String) typeId, idResolver, Object.class);
        assertNull(result);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        JsonDeserializer<MyClass> myClassDeserializer = new StdDeserializer<MyClass>() {};
        idResolver.addValueDeserializer(MyClass.class, myClassDeserializer);
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, MyClass.class, idResolver, null, null, null, null, false, null);
        String json = "{\"typeId\":\"MyClass\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = base._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
        assertEquals(MyClass.class, result.getClass());
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeIdAndCustomTypeIdResolver() throws JsonProcessingException, IOException {
        ObjectMapper mapper = (ObjectMapper) mapperSetup();
        TypeIdResolver idResolver = new DefaultTypeIdResolver() {
            @Override
            public JavaType resolveTypeId(DeserializationContext ctxt, Object beanProperty, Type id) {
                if (id == null) {
                    return mapper.getTypeFactory().constructValueType(String.class);
                }
                if ("MyClass".equals(id.toString())) {
                    return mapper.getTypeFactory().constructType(MyClass.class);
                }
                return mapper.getTypeFactory().constructType(id.getClassLoader(), id.toString());
            }
        };
        JsonDeserializer<MyClass> myClassDeserializer = new StdDeserializer<MyClass>() {};
        idResolver.addValueDeserializer(MyClass.class, myClassDeserializer);
        TypeDeserializerBase base = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, MyClass.class, idResolver, null, null, null, null, false, null);
        String json = "{\"typeId\":\"MyClass\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = base._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
        assertEquals(MyClass.class, result.getClass());
    }

    private static class MyClass {
    }

    private static class StdDeserializer<T> extends JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
}