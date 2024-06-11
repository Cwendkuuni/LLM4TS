package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;

/**
 * Unit tests for {@link AsWrapperTypeDeserializer} class.
 */
public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

    private static final String CLASS_NAME = AsWrapperTypeDeserializer.class.getName();

    public void testDeserializeTypedFromObject() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, typeIdResolver, "_class", true, String.class);

        String jsonString = "{\"_class\":\"java.lang.String\",\"value\":\"test\"}";
        Object result = mapper.readValue(jsonString, deserializer);
        assertEquals("test", result);
    }

    public void testDeserializeTypedFromArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructArrayType(Integer.class);
        TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, typeIdResolver, "_class", true, Integer[].class);

        String jsonString = "[{\"_class\":\"java.lang.Integer\",\"value\":42}]";
        Object result = mapper.readValue(jsonString, deserializer);
        assertArrayEquals(new Integer[]{42}, (Integer[]) result);
    }

    public void testDeserializeTypedFromScalar() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, typeIdResolver, "_class", true, Integer.class);

        String jsonString = "42";
        Object result = mapper.readValue(jsonString, deserializer);
        assertEquals(42, result);
    }

    public void testDeserializeTypedFromAny() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Number.class);
        TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, typeIdResolver, "_class", true, Number.class);

        String jsonString1 = "{\"_class\":\"java.lang.Integer\",\"value\":42}";
        Object result1 = mapper.readValue(jsonString1, deserializer);
        assertEquals(42, result1);

        String jsonString2 = "{\"_class\":\"java.lang.Double\",\"value\":3.14}";
        Object result2 = mapper.readValue(jsonString2, deserializer);
        assertEquals(3.14, result2);
    }

    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, typeIdResolver, "_class", true, String.class);

        String jsonString = "{\"_class\":\"java.lang.String\",\"value\":\"test\"}";
        ObjectIdReader objectIdReader = mapper.getObjectIdReader();
        ObjectIdReader.ObjectIdReaderContext idReaderContext = new ObjectIdReader.ObjectIdReaderContext();
        ObjectIdReader.ObjectId id = objectIdReader.readId(new CharArrayReader("java.lang.String"), idReaderContext);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
        jsonParser.nextToken();
        jsonParser.nextToken(); // skip "_class" field
        jsonParser.nextToken(); // read typeId
        JsonParser.TokenType tokenType = jsonParser.nextToken(); // read ":" separator
        jsonParser.nextToken(); // read value field name
        jsonParser.nextToken(); // read ":" separator
        JsonParser.Token token = jsonParser.nextToken(); // read value
        JsonToken expectedToken = JsonToken.VALUE_STRING;
        assertEquals(expectedToken, token);
        assertEquals("test", jsonParser.getValueAsString());
        Object result = deserializer.deserializeTypedFromObject(jsonParser, mapper.getDeserializationContext());
        assertEquals("test", result);
    }
}