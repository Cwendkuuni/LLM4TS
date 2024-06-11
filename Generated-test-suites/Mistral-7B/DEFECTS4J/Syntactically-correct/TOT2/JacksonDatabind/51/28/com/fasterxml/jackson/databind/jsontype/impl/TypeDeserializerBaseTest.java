package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    private static final TypeIdResolver idResolver = new StandardTypeIdResolver();
    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        JavaType baseType = objectMapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", true, null);
    }

    /**
     * Test basic functionality of {@link TypeDeserializerBase#forProperty(BeanProperty)} method.
     * @throws IOException
     */
    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty("testProperty", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
    }

    /**
     * Test default implementation of {@link TypeDeserializerBase#_handleUnknownTypeId(DeserializationContext, String, TypeIdResolver, JavaType)} method.
     * @throws IOException
     */
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String jsonString = "{\"typeId\": \"UnknownTypeId\"}";
        ObjectReader reader = objectMapper.readerForValue(jsonString);
        JsonParser parser = reader.parse(new StringReader(jsonString));
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext()));
    }

    /**
     * Test handling of null deserializer in {@link TypeDeserializerBase#_findDeserializer(DeserializationContext, String)} method.
     * @throws IOException
     */
    @Test
    public void testFindDeserializerNull() throws IOException {
        objectMapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        String jsonString = "{\"typeId\": \"NullType\"}";
        ObjectReader reader = objectMapper.readerForValue(jsonString);
        JsonParser parser = reader.parse(new StringReader(jsonString));
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "NullType");
        assertNull(deserializer);
    }

    /**
     * Test handling of custom deserializer in {@link TypeDeserializerBase#_findDeserializer(DeserializationContext, String)} method.
     * @throws IOException
     */
    @Test
    public void testFindDeserializerCustom() throws IOException {
        JsonDeserializer<String> customDeserializer = new StubStringDeserializer();
        objectMapper.registerDeserializer(String.class, customDeserializer);
        String jsonString = "{\"typeId\": \"StringType\"}";
        ObjectReader reader = objectMapper.readerForValue(jsonString);
        JsonParser parser = reader.parse(new StringReader(jsonString));
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "StringType");
        assertEquals(customDeserializer.getClass(), deserializer.getClass());
    }

    /**
     * Test handling of missing deserializer in {@link TypeDeserializerBase#_findDeserializer(DeserializationContext, String)} method.
     * @throws IOException
     */
    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerMissing() throws IOException {
        String jsonString = "{\"typeId\": \"MissingType\"}";
        ObjectReader reader = objectMapper.readerForValue(jsonString);
        JsonParser parser = reader.parse(new StringReader(jsonString));
        typeDeserializerBase._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext());
    }

    /**
     * Test handling of missing default implementation in {@link TypeDeserializerBase#getDefaultImpl()} method.
     * @throws IOException
     */
    @Test
    public void testGetDefaultImplNull() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, "", true, null);
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    /**
     * Test handling of default implementation in {@link TypeDeserializerBase#getDefaultImpl()} method.
     * @throws IOException
     */
    @Test
    public void testGetDefaultImpl() throws IOException {
        JavaType defaultImpl = objectMapper.getTypeFactory().constructType(Object.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, "", true, defaultImpl);
        assertEquals(Object.class, typeDeserializerBase.getDefaultImpl().getRawClass());
    }

    /**
     * Test handling of null {@link TypeIdResolver} in {@link TypeDeserializerBase} constructor.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTypeIdResolver() throws IOException {
        new TypeDeserializerBase(null, null, "", true, null);
    }

    /**
     * Test handling of null {@link JavaType} in {@link TypeDeserializerBase} constructor.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBaseType() throws IOException {
        new TypeDeserializerBase(null, idResolver, "", true, null);
    }

    /**
     * Test handling of null {@link String} in {@link TypeDeserializerBase} constructor.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTypePropertyName() throws IOException {
        new TypeDeserializerBase(null, idResolver, null, true, null);
    }

    /**
     * Test handling of null {@link JavaType} in {@link TypeDeserializerBase#forProperty(BeanProperty)} method.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullBaseType() throws IOException {
        BeanProperty beanProperty = new BeanProperty("testProperty", String.class);
        typeDeserializerBase.forProperty(beanProperty);
    }

    /**
     * Test handling of null {@link JavaType} in {@link TypeDeserializerBase#getTypeIdResolver()} method.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testgetTypeIdResolverNull() throws IOException {
        typeDeserializerBase.getTypeIdResolver();
    }

    /**
     * Test handling of null {@link JsonDeserializer} in {@link TypeDeserializerBase#_deserializers} map.
     * @throws IOException
     */
    @Test
    public void testDeserializersNull() throws IOException {
        Map<String, JsonDeserializer<Object>> deserializers = typeDeserializerBase._deserializers;
        deserializers.put(null, null);
    }

    static class StubStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            return "StubStringDeserializer";
        }
    }
}