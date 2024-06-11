package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    private static final String TYPE_NAME = "com.test.MyClass";
    private static final String TYPE_ID = "MyClassTypeId";
    private static final String UNKNOWN_TYPE_ID = "UnknownTypeId";

    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setUp() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        typeIdResolver = new TypeIdResolverBase();
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "_typePropertyName", true, Object.class) {
            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                fail("_handleUnknownTypeId should not be called in tests");
                return null;
            }
        };
        typeDeserializerBase._defaultImplDeserializer = mapper.findValueDeserializer(Object.class);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = new BeanProperty("_propertyName", String.class, null);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(typeDeserializer);
        assertEquals(TypeDeserializer.None.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(Object.class.getName(), baseTypeName);
    }

    @Test
    public void testPropertyName() throws Exception {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        TypeId typeId = typeIdResolver.typeIdFromValue(mapper.createObjectCodec().readTree("{\"typeId\":\"" + TYPE_ID + "\"}"));
        assertEquals(TYPE_ID, typeId.getId());
        assertEquals(String.class, typeId.getClassId());

        typeId = typeIdResolver.typeIdFromValue(mapper.createObjectCodec().readTree("{\"typeId\":null}"));
        assertNull(typeId);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() throws Exception {
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.startsWith("[TypeDeserializerBase"));
    }

    @Test
    public void testFindDeserializerWithNullContext() throws Exception {
        assertNull(typeDeserializerBase._findDeserializer(null, null));
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        DeserializationContext context = mapper.getDeserializationContext();
        assertNull(typeDeserializerBase._findDeserializer(context, null));
    }

    @Test
    public void testFindDeserializerWithTypeId() throws Exception {
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(javaType);
        typeDeserializerBase._deserializers.put(TYPE_ID, deserializer);
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(context, TYPE_ID);
        assertEquals(deserializer, foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNotNull(foundDeserializer);
        assertEquals(NullifyingDeserializer.instance.getClass(), foundDeserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        DeserializationContext context = mapper.getDeserializationContext();
        String unknownTypeId = UNKNOWN_TYPE_ID;
        typeDeserializerBase._findDeserializer(context, unknownTypeId);
        fail("_findDeserializer should have thrown an IOException for unknown type id");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeId\":\"" + TYPE_ID + "\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
        assertInstanceOf(String.class, result);
    }
}