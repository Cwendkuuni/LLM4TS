package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        typeIdResolver = new TestTypeIdResolver();
        JavaType baseType = JavaType.construct(String.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, null, false, null);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertNotNull(typeDeserializer);
        assertEquals(String.class, typeDeserializer.getType());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver:com.fasterxml.jackson.databind.TestTypeIdResolver@6846c755]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "com.example:MyClass";
        JsonDeserializer<Object> deserializer = new TestJsonDeserializer();
        typeDeserializerBase._deserializers.put(typeId, deserializer);

        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(deserializer, foundDeserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerMissing() throws Exception {
        String typeId = "com.example:MissingClass";
        DeserializationContext context = objectMapper.getDeserializationContext();
        typeDeserializerBase._findDeserializer(context, typeId);
        fail("Expected IOException was not thrown");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType defaultImpl = JavaType.construct(String[].class);
        typeDeserializerBase._defaultImpl = defaultImpl;

        synchronized (typeDeserializerBase._defaultImpl) {
            JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._defaultImplDeserializer;
            assertNotNull(defaultImplDeserializer);
            assertEquals(defaultImpl, defaultImplDeserializer.getParameterTypes()[0]);
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"propertyName\":\"value\",\"@type\":\"com.example:MyClass\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext(), "com.example:MyClass");
        assertInstanceOf(TestJsonDeserializer.class, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithUnknownTypeId() throws Exception {
        String json = "{\"propertyName\":\"value\",\"@type\":\"com.example:UnknownClass\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "com.example:UnknownClass", typeIdResolver, JavaType.construct(String.class));
        fail("Expected IOException was not thrown");
    }

    private static class TestJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class TestTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            if ("com.example:MyClass".equals(typeId)) {
                return JavaType.construct(TestJsonDeserializer.class);
            }
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext ctxt, Object value) {
            return null;
        }

        @Override
        public Map<String, String> getPropertyNameMapping() {
            return Arrays.asList(new BeanProperty("propertyName", String.class)).stream()
                    .collect(Converter.toMap(BeanProperty::getName, BeanProperty::getType));
        }
    }
}