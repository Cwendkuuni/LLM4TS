package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;
    private TokenBuffer tokenBuffer;
    private AsPropertyTypeDeserializer deserializer;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        jsonParser = jsonFactory.createParser("{}");
        tokenBuffer = new TokenBuffer(jsonParser, objectMapper.getDeserializationContext());
    }

    @After
    public void tearDown() throws Exception {
        jsonParser.close();
        tokenBuffer = null;
        jsonParser = null;
        jsonFactory = null;
        objectMapper = null;
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, null, As.PROPERTY);

        // When
        jsonParser.nextToken(); // [START_OBJECT]
        jsonParser.nextToken(); // [FIELD_NAME] "type"
        jsonParser.nextToken(); // [VALUE_STRING] "StringType"
        jsonParser.nextToken(); // [START_OBJECT]
        jsonParser.nextToken(); // [FIELD_NAME] "data"
        jsonParser.nextToken(); // [VALUE_STRING] "Hello, World!"
        jsonParser.nextToken(); // [END_OBJECT]

        Object result = deserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());

        // Then
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithoutNativeTypeId() throws Exception {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, null, As.PROPERTY);

        // When
        jsonParser.nextToken(); // [START_OBJECT]
        jsonParser.nextToken(); // [FIELD_NAME] "data"
        jsonParser.nextToken(); // [VALUE_STRING] "Hello, World!"

        // Then
        try {
            deserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithPropertyName() throws Exception {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        String propertyName = "stringData";
        deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, null, As.PROPERTY);

        // When
        jsonParser.nextToken(); // [START_OBJECT]
        jsonParser.nextToken(); // [FIELD_NAME] "type"
        jsonParser.nextToken(); // [VALUE_STRING] "StringType"
        jsonParser.nextToken(); // [FIELD_NAME] "stringData"
        jsonParser.nextToken(); // [VALUE_STRING] "Hello, World!"
        jsonParser.nextToken(); // [END_OBJECT]

        Object result = deserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());

        // Then
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        String defaultImplTypeName = "DefaultImplType";
        deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, objectMapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        // When
        jsonParser.nextToken(); // [START_OBJECT]
        jsonParser.nextToken(); // [FIELD_NAME] "type"
        jsonParser.nextToken(); // [VALUE_STRING] "DefaultImplType"

        Object result = deserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());

        // Then
        assertEquals(new DefaultImplType(), result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyTokenBuffer() throws Exception {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, null, As.PROPERTY);

        // When
        Object result = deserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());

        // Then
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, null, As.PROPERTY);

        // When
        jsonParser.nextToken(); // [START_ARRAY]
        jsonParser.nextToken(); // [VALUE_NULL]
        jsonParser.nextToken(); // [VALUE_NULL]
        jsonParser.nextToken(); // [VALUE_STRING] "Hello, World!"
        jsonParser.nextToken(); // [END_ARRAY]

        Object result = deserializer.deserializeTypedFromAny(jsonParser, objectMapper.getDeserializationContext());

        // Then
        assertEquals(new ArrayList<>(Arrays.asList("Hello, World!")), result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(Boolean.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, null, As.PROPERTY);

        // When
        jsonParser.nextToken(); // [VALUE_TRUE]

        Object result = deserializer.deserializeTypedFromScalar(jsonParser, objectMapper.getDeserializationContext());

        // Then
        assertTrue(result);
    }

    /**
     * Helper class for testing.
     */
    public static class DefaultImplType {
    }

    /**
     * Helper class for testing.
     */
    public static class SimpleTypeIdResolver implements TypeIdResolver {
        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> valueType) {
            return null;
        }
    }
}