package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, new StandardTypeIdResolver(), "", false, null);

        assertEquals(javaIntType, deserializer.getBaseType());
        assertEquals(StandardTypeIdResolver.class, deserializer.getTypeIdResolver().getClass());
        assertEquals("", deserializer.getPropertyName());
        assertFalse(deserializer.isTypeIdVisible());
        assertNull(deserializer.getDefaultImpl());
        assertNull(deserializer.getDefaultImplDeserializer());
        assertTrue(deserializer.getClass().isInstance(deserializer));
        assertNotNull(deserializer.toString());
    }

    /**
     * Test {@link #forProperty(BeanProperty)}.
     */
    @Test
    public void testForProperty() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        BeanProperty property = new BeanProperty("test", intType);
        TypeDeserializerBase copy = deserializer.forProperty(property);

        assertNotSame(deserializer, copy);
        assertEquals(javaIntType, copy.getBaseType());
        assertEquals(idResolver, copy.getTypeIdResolver());
        assertEquals(typePropertyName, copy.getPropertyName());
        assertEquals(typeIdVisible, copy.isTypeIdVisible());
        assertEquals(defaultImpl, copy.getDefaultImpl());
        assertEquals(deserializer.getDefaultImplDeserializer(), copy.getDefaultImplDeserializer());
    }

    /**
     * Test {@link #getTypeInclusion()}.
     */
    @Test
    public void testgetTypeInclusion() {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, new StandardTypeIdResolver(), "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, deserializer.getTypeInclusion());

        deserializer = new TypeDeserializerBase(
                javaIntType, new StandardTypeIdResolver(), "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.WRAP_ROOT_VALUE;
            }
        };
        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, deserializer.getTypeInclusion());
    }

    /**
     * Test {@link #baseTypeName()}.
     */
    @Test
    public void testBaseTypeName() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, new StandardTypeIdResolver(), "", false, null);
        assertEquals("class java.lang.Integer", deserializer.baseTypeName());
    }

    /**
     * Test {@link #toString()}.
     */
    @Test
    public void testToString() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, new StandardTypeIdResolver(), "", false, null);
        String expected = "[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;"
                + " base-type:class java.lang.Integer; id-resolver:"
                + " class com.fasterxml.jackson.databind.TypeIdResolver;]";
        assertEquals(expected, deserializer.toString());
    }

    /**
     * Test {@link #_findDeserializer(DeserializationContext, String)}.
     */
    @Test
    public void testFindDeserializer() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, "intTypeId");
        assertNotNull(deser);
        assertEquals(IntegerDeserializer.class, deser.getClass());

        deser = deserializer._findDeserializer(ctxt, null);
        assertNull(deser);

        deser = deserializer._findDeserializer(ctxt, "unknownTypeId");
        assertNull(deser);
    }

    /**
     * Test {@link #_findDefaultImplDeserializer(DeserializationContext)}.
     */
    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(deser);
        assertEquals(StringDeserializer.class, deser.getClass());

        deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, null);

        deser = deserializer._findDefaultImplDeserializer(ctxt);
        assertNull(deser);
    }

    /**
     * Test {@link #_deserializeWithNativeTypeId(JsonParser, DeserializationContext)} with valid type id.
     */
    @Test
    public void testDeserializeWithNativeTypeIdValidTypeId() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectReader reader = mapper.readerForElement("intTypeId", JsonToken.START_OBJECT);
        JsonParser parser = reader.getParser();

        Object result = deserializer._deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals(Integer.class, result.getClass());

        parser = reader.getParser();
        parser.nextToken(); // consume START_OBJECT
        parser.nextValue(); // consume null value

        result = deserializer._deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
        assertNull(result);
    }

    /**
     * Test {@link #_deserializeWithNativeTypeId(JsonParser, DeserializationContext)} with invalid type id.
     */
    @Test
    public void testDeserializeWithNativeTypeIdInvalidTypeId() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectReader reader = mapper.readerForElement("intTypeId", JsonToken.START_OBJECT);
        JsonParser parser = reader.getParser();

        parser.nextToken(); // consume START_OBJECT
        parser.nextValue(); // consume null value

        parser.setValue("unknownTypeId");

        try {
            deserializer._deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    /**
     * Test {@link #_handleUnknownTypeId(DeserializationContext, String, TypeIdResolver, JavaType)} with
     * known type id resolver.
     */
    @Test
    public void testHandleUnknownTypeIdKnownTypeIdResolver() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        JavaType mapType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if ("intTypeId".equals(typeId)) {
                    return javaIntType;
                } else if ("mapTypeId".equals(typeId)) {
                    return mapType;
                } else {
                    return null;
                }
            }

            @Override
            public String idFromValue(DeserializationContext ctxt, Object value) throws IOException {
                return null;
            }
        };
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectReader reader = mapper.readerForElement("typeId", JsonToken.START_OBJECT);
        JsonParser parser = reader.getParser();

        Object result = deserializer._handleUnknownTypeId(parser, mapper.getDeserializationContext(), idResolver, javaIntType);
        assertNotNull(result);
        assertEquals(Integer.class, result.getClass());

        parser.setValue("mapTypeId");

        result = deserializer._handleUnknownTypeId(parser, mapper.getDeserializationContext(), idResolver, javaIntType);
        assertInstanceOf(Map.class, result);
    }

    /**
     * Test {@link #_handleUnknownTypeId(DeserializationContext, String, TypeIdResolver, JavaType)} with unknown
     * type id resolver.
     */
    @Test
    public void testHandleUnknownTypeIdUnknownTypeIdResolver() throws Exception {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(intType);
        JavaType mapType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                javaIntType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectReader reader = mapper.readerForElement("typeId", JsonToken.START_OBJECT);
        JsonParser parser = reader.getParser();

        try {
            deserializer._handleUnknownTypeId(parser, mapper.getDeserializationContext(), idResolver, javaIntType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}