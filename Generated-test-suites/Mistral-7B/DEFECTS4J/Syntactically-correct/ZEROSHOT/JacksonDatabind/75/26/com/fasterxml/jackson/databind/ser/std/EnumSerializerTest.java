package com.example.jackson; // replace with your test package name if needed

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;
    private EnumSerializer enumSerializer;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        enumSerializer = objectMapper.findValueSerializer(MyEnum.class);
    }

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException {
        JsonGenerator gen = objectMapper.getPolymorphicSerializer(MyEnum[].class).getFactory()
                .createGenerator();
        ObjectNode node = objectMapper.createObjectNode();
        JsonSerializer<?> serializer = enumSerializer.createContextual(null, null);

        // Test serialization as index with default configuration
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals(0, gen.getOutputValue().asInt());
        gen.flush();

        // Test serialization as index with enabled feature
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        serializer = enumSerializer.createContextual(null, null);
        gen = objectMapper.getPolymorphicSerializer(MyEnum[].class).getFactory()
                .createGenerator();
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals(0, gen.getOutputValue().asInt());
        gen.flush();

        // Test serialization as text with disabled feature
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        serializer = enumSerializer;
        gen = objectMapper.getPolymorphicSerializer(MyEnum[].class).getFactory()
                .createGenerator();
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals("ONE", gen.getOutputValueAsString());
        gen.flush();

        // Test serialization as text with enabled feature
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        serializer = enumSerializer;
        gen = objectMapper.getPolymorphicSerializer(MyEnum[].class).getFactory()
                .createGenerator();
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals("ONE", gen.getOutputValueAsString());
        gen.flush();

        // Test serialization as text with custom serialization format
        JsonFormat format = new JsonFormat();
        format.setShape(Shape.STRING);
        ObjectNode nodeWithFormat = objectMapper.treeToObject(node);
        nodeWithFormat.put("format", format);
        serializer = enumSerializer.createContextual(null, objectMapper.treeToValue(nodeWithFormat, Object.class));
        gen = objectMapper.getPolymorphicSerializer(MyEnum[].class).getFactory()
                .createGenerator();
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals("ONE", gen.getOutputValueAsString());
        gen.flush();

        // Test serialization as index with custom serialization format
        format.setShape(Shape.INT);
        nodeWithFormat = objectMapper.treeToObject(node);
        nodeWithFormat.put("format", format);
        serializer = enumSerializer.createContextual(null, objectMapper.treeToValue(nodeWithFormat, Object.class));
        gen = objectMapper.getPolymorphicSerializer(MyEnum[].class).getFactory()
                .createGenerator();
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals(0, gen.getOutputValue().asInt());
        gen.flush();

        assertNull(objectMapper.findValueSerializer(String.class));
    }

    enum MyEnum {
        ONE,
        TWO,
        THREE
    }
}