package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test cases for {@link EnumSerializer}.
 */
public class EnumSerializerTest {

    // Test case for the constructor
    @Test
    public void testEnumSerializerConstruction() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromString(String.class), null);
        assertNotNull(serializer);
    }

    // Test case for static factory method
    @Test
    public void testConstruct() throws Exception {
        Class<MyEnum> enumClass = MyEnum.class;
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = EnumSerializer.construct(enumClass, mapper.getSerializationConfig(), null, null);
        assertNotNull(serializer);
        assertEquals(MyEnum.class, serializer.handledType());
    }

    // Test case for createContextual method
    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyEnum propertyEnum = MyEnum.ONE;
        BeanProperty property = new BeanProperty("property", MyEnum.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), property);
        assertNotNull(contextualSerializer);
        assertEquals(MyEnum.class, contextualSerializer.handledType());
        assertNotSame(serializer, contextualSerializer);
    }

    // Test case for serialize method with index serialization
    @Test
    public void testSerializeIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), Boolean.TRUE);

        MyEnum enumValue = MyEnum.ONE;
        ObjectWriter writer = mapper.writer().withRootValueSerializer(serializer);
        JsonNode rootNode = writer.writeValueAsTree(enumValue);
        JsonNode node = rootNode.get(0);
        assertEquals(Integer.valueOf(enumValue.ordinal()), node);
    }

    // Test case for serialize method with string serialization
    @Test
    public void testSerializeString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        MyEnum enumValue = MyEnum.ONE;
        ObjectWriter writer = mapper.writer().withRootValueSerializer(serializer);
        JsonNode rootNode = writer.writeValueAsTree(enumValue);
        JsonNode node = rootNode.get(0);
        assertEquals("ONE", node.asText());
    }

    // Test case for serialize method with toString() serialization
    @Test
    public void testSerializeToString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        MyEnum enumValue = MyEnum.ONE;
        ObjectWriter writer = mapper.writer().withRootValueSerializer(serializer);
        JsonNode rootNode = writer.writeValueAsTree(enumValue);
        JsonNode node = rootNode.get(0);
        assertEquals("ONE", node.asText());
    }

    // Test case for serialize method with custom values
    @Test
    public void testSerializeCustomValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromString(MyEnumCustomValues.class), null);

        MyEnumCustomValues enumValue = MyEnumCustomValues.TWO;
        ObjectWriter writer = mapper.writer().withRootValueSerializer(serializer);
        JsonNode rootNode = writer.writeValueAsTree(enumValue);
        JsonNode node = rootNode.get(0);
        assertEquals("TWO", node.asText());
    }

    // Test case for getSchema method with index serialization
    @Test
    public void testGetSchemaIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), Boolean.TRUE);

        JsonNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), null);
        JsonNode schema = schemaNode.getDefaultValue();
        assertEquals("integer", schema.get("type").asText());
    }

    // Test case for getSchema method with string serialization
    @Test
    public void testGetSchemaString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        JsonNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), null);
        JsonNode schema = schemaNode.getDefaultValue();
        assertEquals("string", schema.get("type").asText());
    }

    // Test case for acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFactory()
                .constructJsonFormatVisitor(SerializerProvider.StandardSerializers.NULL_VALUE);
        serializer.acceptJsonFormatVisitor(visitor, null);
        JsonNode schemaNode = visitor.getSchemaProvider().getSchema(null);
        JsonNode schema = schemaNode.getDefaultValue();
        assertEquals("string", schema.get("type").asText());
    }

    // Test case for _isShapeWrittenUsingIndex method
    @Test
    public void testIsShapeWrittenUsingIndex() {
        Class<MyEnum> enumClass = MyEnum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
        assertNull(result);

        format.setShape(JsonFormat.Shape.INT);
        result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
        assertTrue(result);

        format.setShape(JsonFormat.Shape.STRING);
        result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
        assertFalse(result);

        format.setShape(JsonFormat.Shape.NATURAL);
        result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
        assertFalse(result);

        format.setShape(JsonFormat.Shape.ARRAY);
        result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
        assertTrue(result);

        format.setShape(JsonFormat.Shape.OBJECT);
        try {
            EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test case for _serializeAsIndex method
    @Test
    public void testSerializeAsIndex() {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        assertTrue(serializer._serializeAsIndex(mapper.getSerializerProvider()));
        serializer._serializeAsIndex = Boolean.FALSE;
        assertFalse(serializer._serializeAsIndex(mapper.getSerializerProvider()));
        serializer._serializeAsIndex = null;
        assertTrue(serializer._serializeAsIndex(mapper.getSerializerProvider()));
    }

    // Enum classes used for testing
    enum MyEnum { ONE, TWO, THREE }

    enum MyEnumCustomValues { ONE, TWO }
}