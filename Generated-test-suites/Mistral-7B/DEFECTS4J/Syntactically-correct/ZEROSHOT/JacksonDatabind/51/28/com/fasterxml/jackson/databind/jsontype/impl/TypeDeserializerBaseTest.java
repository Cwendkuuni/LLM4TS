package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeDeserializerBase testee;
    private static final JavaType baseType;
    private static final TypeIdResolver idResolver;
    private static final String typePropertyName = "typePropertyName";
    private static final boolean typeIdVisible = true;
    private static final JavaType defaultImpl;

    static {
        testee = new TestTypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
            public static class TestClass1 {
            }

            @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
            public static class TestClass2 {
                @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
                public static class InnerClass1 {
                }

                public String type;
            }
        };

        baseType = JavaType.constructType(TestTypeDeserializerBase.class);
        idResolver = new StubTypeIdResolver();
        defaultImpl = mapper.getTypeFactory().constructType(String.class);
    }

    @Before
    public void setUp() throws Exception {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        BeanProperty prop = new BeanProperty("testProperty", String.class);
        TypeDeserializer deserializer = testee.forProperty(prop);
        assertEquals(testee.getClass(), deserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.PROPERTY, testee.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws IOException, JsonProcessingException {
        assertEquals(TypeDeserializerBase.class.getName(), testee.baseTypeName());
    }

    @Test
    public void testPropertyName() {
        assertEquals(typePropertyName, testee.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() {
        assertEquals(idResolver, testee.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() throws IOException, JsonProcessingException {
        assertEquals(String.class, testee.getDefaultImpl().getRawClass());
    }

    @Test
    public void testToString() throws IOException, JsonProcessingException {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver:class com.fasterxml.jackson.databind.jsontype.impl.StubTypeIdResolver]";
        assertEquals(expected, testee.toString());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId1 = "com.fasterxml.jackson.databind.jsontype.impl.TestClass1";
        JsonDeserializer<Object> deserializer1 = mapper.findValueDeserializer(testee, typeId1);
        assertEquals(TestClass1.class, deserializer1.getValueClass());

        String typeId2 = "TestClass2";
        JsonDeserializer<Object> deserializer2 = testee._findDeserializer(mapper.getDeserializationContext(), typeId2);
        assertEquals(TestClass2.class, deserializer2.getValueClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        testee._findDeserializer(mapper.getDeserializationContext(), null);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertEquals(String.class, deserializer.getValueClass());
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNull() throws IOException {
        testee._findDefaultImplDeserializer(mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"TestClass2\"}");
        Object result = testee._deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
        assertEquals(TestClass2.class, result.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("{}");
        testee._deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException, JsonProcessingException {
        String unknownTypeId = "UnknownType";
        JavaType result = testee._handleUnknownTypeId(mapper.getDeserializationContext(), unknownTypeId, idResolver, baseType);
        assertNotNull(result);

        // StubTypeIdResolver returns MapType for unknown types, but that should not be the case here
        assertFalse(result.isContainerType());
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {
        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            return mapper.findValueDeserializer(this, typeId);
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return mapper.findValueDeserializer(this, defaultImpl.getName());
        }

        @Override
        protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return mapper.handleUnknownTypeId(baseType, typeId, idResolver, null);
        }
    }

    private static class StubTypeIdResolver extends TypeIdResolverBase {
        @Override
        public JavaType resolveTypeId(DeserializationContext ctxt, String typeId) throws IOException {
            if (typeId.equals("TestClass1")) {
                return mapper.getTypeFactory().constructType(TestTypeDeserializerBase.TestClass1.class);
            }
            if (typeId.equals("TestClass2")) {
                return mapper.getTypeFactory().constructType(TestTypeDeserializerBase.TestClass2.class);
            }
            if (typeId.equals("InnerClass1")) {
                return mapper.getTypeFactory().constructType(TestTypeDeserializerBase.TestClass2.InnerClass1.class);
            }
            throw new IOException("Unknown type id: " + typeId);
        }
    }
}