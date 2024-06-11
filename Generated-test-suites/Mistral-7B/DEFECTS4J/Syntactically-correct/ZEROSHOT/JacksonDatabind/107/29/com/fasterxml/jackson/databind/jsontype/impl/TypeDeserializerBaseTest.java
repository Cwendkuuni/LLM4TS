package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    private static final String TYPE_PROPERTY_NAME = "typePropertyName";
    private static final String DEFAULT_IMPL_CLASS_NAME = "com.example.DefaultImpl";

    private TypeDeserializerBase testSubject;
    private TypeIdResolver typeIdResolver;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        typeIdResolver = mock(TypeIdResolver.class);
        deserializationContext = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);

        JavaType baseType = getTypeFactory().constructType(Object.class);
        testSubject = new TypeDeserializerBase(baseType, typeIdResolver, TYPE_PROPERTY_NAME, true,
                getTypeFactory().constructType(DEFAULT_IMPL_CLASS_NAME));
    }

    @org.junit.Test
    public void testForProperty() throws IOException, JsonProcessingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializer expected = mock(TypeDeserializer.class);

        when(typeIdResolver.typeFromId(deserializationContext, "testTypeId")).thenReturn(getTypeFactory()
                .constructType(String.class));
        when(typeIdResolver.resolveVisibleType(deserializationContext, baseType, beanProperty))
                .thenReturn(getTypeFactory().constructType(Integer.class));
        when(typeIdResolver.isUnknownId(deserializationContext, "testTypeId")).thenReturn(false);

        TypeDeserializer result = testSubject.forProperty(beanProperty);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFindDeserializerWithKnownTypeId() throws IOException, JsonProcessingException {
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);

        when(typeIdResolver.typeFromId(deserializationContext, "knownTypeId"))
                .thenReturn(getTypeFactory().constructType(String.class));
        when(typeIdResolver.isUnknownId(deserializationContext, "knownTypeId"))
                .thenReturn(false);
        when(typeIdResolver.resolveClass(deserializationContext, getTypeFactory().constructType(String.class)))
                .thenReturn(String.class);
        when(jsonDeserializer.getNullValue()).thenReturn(null);
        when(typeIdResolver.findDeserializer(deserializationContext, String.class))
                .thenReturn(jsonDeserializer);

        JsonDeserializer<Object> result = testSubject._findDeserializer(deserializationContext, "knownTypeId");

        assertEquals(jsonDeserializer, result);
    }

    @org.junit.Test
    public void testFindDeserializerWithUnknownTypeId() throws IOException, JsonProcessingException {
        when(typeIdResolver.typeFromId(deserializationContext, "unknownTypeId"))
                .thenReturn(null);

        JsonDeserializer<Object> result = testSubject._findDeserializer(deserializationContext, "unknownTypeId");

        assertNull(result);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws IOException, JsonProcessingException {
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);

        when(typeIdResolver.isUnknownId(deserializationContext, null)).thenReturn(true);
        when(typeIdResolver.resolveClass(deserializationContext, _baseType)).thenReturn(Object.class);
        when(deserializationContext.findContextualValueDeserializer(Object.class, null)).thenReturn(jsonDeserializer);

        JsonDeserializer<Object> result = testSubject._findDefaultImplDeserializer(deserializationContext);

        assertEquals(jsonDeserializer, result);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);

        when(typeIdResolver.typeFromId(jsonParser, "customTypeId"))
                .thenReturn(getTypeFactory().constructType(String.class));
        when(typeIdResolver.isUnknownId(jsonParser, "customTypeId")).thenReturn(false);
        when(typeIdResolver.resolveClass(deserializationContext, getTypeFactory().constructType(String.class)))
                .thenReturn(String.class);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("testValue");
        when(jsonParser.getText()).thenReturn("testValue");
        when(jsonDeserializer.getNullValue()).thenReturn(null);
        when(typeIdResolver.findDeserializer(deserializationContext, String.class))
                .thenReturn(jsonDeserializer);

        Object result = testSubject._deserializeWithNativeTypeId(jsonParser, deserializationContext);

        assertEquals("testValue", result);
    }
}