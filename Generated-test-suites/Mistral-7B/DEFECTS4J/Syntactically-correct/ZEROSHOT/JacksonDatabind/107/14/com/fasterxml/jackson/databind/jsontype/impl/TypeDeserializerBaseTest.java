package com.example.jackson.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Test
    public void testTypeDeserializerBase() throws JsonProcessingException, IOException {
        // Create test object
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.construct(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        // Test forProperty method
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer<?, ?> result = typeDeserializerBase.forProperty(property);
        assertNotNull(result);
        assertInstanceOf(TypeDeserializer.class, result);

        // Test getTypeInclusion method
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNotNull(typeInclusion);

        // Test baseTypeName method
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(baseType.getRawClass().getName(), baseTypeName);

        // Test getPropertyName method
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals(typePropertyName, propertyName);

        // Test getTypeIdResolver method
        TypeIdResolver resultIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(idResolver, resultIdResolver);

        // Test defaultImpl method
        Class<?> defaultImplClass = typeDeserializerBase.getDefaultImpl().getRawClass();
        assertEquals(Integer.class, defaultImplClass);

        // Test toString method
        String toString = typeDeserializerBase.toString();
        String expectedToString = "[TypeDeserializerBase; base-type:java.lang.String; id-resolver:mock.TypeIdResolver]";
        assertEquals(expectedToString, toString);

        // Test _findDeserializer method with known type id
        String typeId = "myTypeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.deserializerFromId(eq(typeId), any(DeserializationContext.class))).thenReturn(deserializer);
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(mock(JavaType.class));

        deserializer = typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), typeId);
        assertEquals(deserializer, typeDeserializerBase._deserializers.get(typeId));

        // Test _findDeserializer method with unknown type id
        String unknownTypeId = "unknownTypeId";
        JavaType unknownType = mock(JavaType.class);
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(unknownTypeId))).thenReturn(unknownType);

        assertNull(typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), unknownTypeId));

        // Test _findDefaultImplDeserializer method
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(idResolver.deserializerFromId(eq(JsonTypeInfo.Id.NONE), any(DeserializationContext.class))).thenReturn(defaultImplDeserializer);

        defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));
        assertEquals(defaultImplDeserializer, typeDeserializerBase._defaultImplDeserializer);

        // Test _deserializeWithNativeTypeId method
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object resultObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
        assertNull(resultObject);

        when(jsonParser.getTypeId()).thenReturn(typeId);
        Object expectedResultObject = "testObject";
        when(deserializationContext.findContextualValueDeserializer(eq(JavaType.construct(String.class)), any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));

        resultObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);
        assertEquals(expectedResultObject, resultObject);

        // Test _handleUnknownTypeId method
        String extraDesc = "Type id is not statically known (for POJO property 'typeProperty')";
        JavaType resultType = typeDeserializerBase._handleUnknownTypeId(deserializationContext, extraDesc);
        assertNull(resultType);
    }

    private void assertInstanceOf(Class<?> expectedType, Object actualObject) {
        assertTrue("Expected instance of: " + expectedType.getName() + ", but got: " + actualObject.getClass().getName(), expectedType.isInstance(actualObject));
    }

    private void assertEquals(Object expectedObject, Object actualObject) {
        assertEquals(expectedObject.getClass(), actualObject.getClass());
        assertEquals(expectedObject, actualObject);
    }

    private void assertEquals(String expectedString, String actualString) {
        assertEquals(expectedString, actualString);
    }

    private void assertEquals(long expectedLong, long actualLong) {
        assertEquals(expectedLong, actualLong);
    }
}