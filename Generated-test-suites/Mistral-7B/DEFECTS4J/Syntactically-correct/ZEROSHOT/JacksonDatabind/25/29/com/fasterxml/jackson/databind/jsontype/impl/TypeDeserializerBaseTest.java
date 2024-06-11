package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase INSTANCE = new TypeDeserializerBaseTestTypeDeserializer() {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }.forProperty(null);

    @org.junit.Test
    public void testConstructors() throws Exception {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase testInstance = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, testInstance._baseType);
        assertEquals(idResolver, testInstance._idResolver);
        assertEquals(typePropertyName, testInstance._typePropertyName);
        assertEquals(typeIdVisible, testInstance._typeIdVisible);
        assertNull(testInstance._property);
        assertEquals(16, testInstance._deserializers.size());
        assertEquals(null, testInstance._defaultImpl);

        TypeDeserializerBase testInstance2 = new TypeDeserializerBase(INSTANCE, null);
        assertEquals(INSTANCE._baseType, testInstance2._baseType);
        assertEquals(INSTANCE._idResolver, testInstance2._idResolver);
        assertEquals(INSTANCE._typePropertyName, testInstance2._typePropertyName);
        assertEquals(INSTANCE._typeIdVisible, testInstance2._typeIdVisible);
        assertEquals(INSTANCE._deserializers, testInstance2._deserializers);
        assertEquals(INSTANCE._defaultImpl, testInstance2._defaultImpl);
        assertEquals(INSTANCE._property, testInstance2._property);
    }

    @org.junit.Test
    public void testForProperty() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        TypeDeserializerBase testInstance2 = testInstance.forProperty(property);
        assertEquals(testInstance, testInstance2);
    }

    @org.junit.Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        }.forProperty(null);
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, testInstance.getTypeInclusion());
    }

    @org.junit.Test
    public void testBaseTypeName() throws Exception {
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        assertEquals("java.lang.Object", testInstance.baseTypeName());
    }

    @org.junit.Test
    public void testGetPropertyName() throws Exception {
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        assertNull(testInstance.getPropertyName());
    }

    @org.junit.Test
    public void testGetTypeIdResolver() throws Exception {
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        assertNull(testInstance.getTypeIdResolver());
    }

    @org.junit.Test
    public void testGetDefaultImpl() throws Exception {
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        assertNull(testInstance.getDefaultImpl());
    }

    @org.junit.Test
    public void testToString() throws Exception {
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver:null]", testInstance.toString());
    }

    @org.junit.Test
    public void testFindDeserializer() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "typeId";
        JavaType type = mock(JavaType.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(type, null)).thenReturn(deserializer);
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        Object result = testInstance._findDeserializer(context, typeId);
        assertEquals(deserializer, result);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(null, null)).thenReturn(deserializer);
        TypeDeserializerBase testInstance = new TypeDeserializerBaseTestTypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        Object result = testInstance._findDefaultImplDeserializer(context);
        assertEquals(deserializer, result);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        JsonParser parser = mock(JsonParser.class);
        Object result = INSTANCE._deserializeWithNativeTypeId(parser, context, null);
        assertNull(result);

        String typeId = "typeId";
        when(parser.getTypeId()).thenReturn(typeId);
        Object result2 = INSTANCE._deserializeWithNativeTypeId(parser, context, typeId);
        // testing implementation is not possible without knowing deserializer
        // for given typeId
        assertNull(result2);
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "typeId";
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(context, typeId)).thenReturn(null);
        JavaType baseType = mock(JavaType.class);
        TypeDeserializerBase testInstance = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        }.forProperty(null);
        testInstance._handleUnknownTypeId(context, typeId, idResolver, baseType);
    }

    private static class TypeDeserializerTestTypeDeserializer extends TypeDeserializerBase {
        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}