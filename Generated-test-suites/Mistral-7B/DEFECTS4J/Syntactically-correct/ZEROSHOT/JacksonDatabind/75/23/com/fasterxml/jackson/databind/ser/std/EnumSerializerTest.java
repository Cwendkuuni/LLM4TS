package com.example.jackson; // replace with your test package if needed

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializeEnumAsText() throws JsonProcessingException {
        EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        Enum<?> enumInstance = MyEnum.VALUE_ONE;
        serializer.serialize(enumInstance, gen, null);
        String result = gen.getOutputContent();
        assertEquals("VALUE_ONE", result);
    }

    @Test
    public void testSerializeEnumAsIndex() throws JsonProcessingException {
        JsonFormat.Shape shape = JsonFormat.Shape.INT;
        EnumSerializer serializer = new EnumSerializer(MyEnumValues.getInstance(), true, shape);
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        Enum<?> enumInstance = MyEnum.VALUE_ONE;
        serializer.serialize(enumInstance, gen, null);
        String result = gen.getOutputContent();
        assertEquals("0", result);
    }

    @Test
    public void testSerializeEnumAsTextWithCustomValues() throws JsonProcessingException {
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        EnumSerializer serializer = new EnumSerializer(MyEnumValuesWithCustomValues.getInstance(), shape);
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        Enum<?> enumInstance = MyEnum.VALUE_ONE;
        serializer.serialize(enumInstance, gen, null);
        String result = gen.getOutputContent();
        assertEquals("CUSTOM_VALUE_ONE", result);
    }

    @Test
    public void testSerializeEnumAsIndexWithCustomValues() throws JsonProcessingException {
        JsonFormat.Shape shape = JsonFormat.Shape.INT;
        EnumSerializer serializer = new EnumSerializer(MyEnumValuesWithCustomValues.getInstance(), true, shape);
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        Enum<?> enumInstance = MyEnum.VALUE_ONE;
        serializer.serialize(enumInstance, gen, null);
        String result = gen.getOutputContent();
        assertEquals("0", result);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class);
        ObjectNode schemaNode = serializer.getSchema(mapper.getSerializationConfig(), null);
        String expectedSchema = "{\"type\":\"string\"}";
        assertEquals(expectedSchema, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectNode expectedNode = mapper.createObjectNode();
        expectedNode.put("type", "string");
        expectedNode.put("enum", mapper.createArrayNode());
        expectedNode.get("enum").add("VALUE_ONE");
        expectedNode.get("enum").add("VALUE_TWO");

        EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class);
        serializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().getSerializerProvider().getRootValueSerializer(), null);

        ObjectNode resultNode = (ObjectNode) serializer.getSchema(mapper.getSerializationConfig(), null);
        assertEquals(expectedNode, resultNode);
    }

    @JsonFormat(shape = JsonFormat.Shape.INT)
    enum MyEnum {
        VALUE_ONE,
        VALUE_TWO
    }

    enum MyEnumValues implements EnumValues {
        INSTANCE;

        @Override
        public Enum<?>[] values() {
            return MyEnum.values();
        }

        @Override
        public SerializableString serializedValueFor(Enum<?> value) {
            if (value == MyEnum.VALUE_ONE) {
                return new SimpleSerializableString("VALUE_ONE");
            }
            if (value == MyEnum.VALUE_TWO) {
                return new SimpleSerializableString("VALUE_TWO");
            }
            throw new AssertionError();
        }
    }

    enum MyEnumValuesWithCustomValues implements EnumValues {
        INSTANCE;

        @Override
        public Enum<?>[] values() {
            return MyEnum.values();
        }

        @Override
        public SerializableString serializedValueFor(Enum<?> value) {
            if (value == MyEnum.VALUE_ONE) {
                return new SimpleSerializableString("CUSTOM_VALUE_ONE");
            }
            if (value == MyEnum.VALUE_TWO) {
                return new SimpleSerializableString("CUSTOM_VALUE_TWO");
            }
            throw new AssertionError();
        }
    }
}