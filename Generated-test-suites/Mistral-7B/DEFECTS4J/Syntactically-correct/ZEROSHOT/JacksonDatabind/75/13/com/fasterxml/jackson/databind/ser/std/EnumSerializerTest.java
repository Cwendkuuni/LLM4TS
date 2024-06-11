package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

/**
 * Test class for {@link EnumSerializer}
 */
public class EnumSerializerTest {

    @JacksonStdImpl
    public static class TestEnumSerializer extends EnumSerializer {
        public TestEnumSerializer(EnumValues v, Boolean serializeAsIndex) {
            super(v, serializeAsIndex);
        }
    }

    @Test
    public void testSerializationWithIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(Shape.class), true);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("shape", Shape.ARRAY.toString());
        JsonNode result = mapper.readTree(mapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(Shape.values()));
        ArrayNode expected = mapper.createArrayNode();
        expected.addAll(Arrays.asList(Shape.values().ordinal()));
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testSerializationWithToString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(Shape.class), false);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("shape", Shape.ARRAY.toString());
        JsonNode result = mapper.readTree(mapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(Shape.values()));
        ArrayNode expected = mapper.createArrayNode();
        for (Shape s : Shape.values()) {
            expected.add(s.toString());
        }
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testSerializationWithNames() throws Exception {
        EnumSerializer serializer = new TestEnumSerializer(EnumSet.allOf(Shape.class), false);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(Shape.ARRAY);
        JsonNode result = mapper.readTree(mapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(Shape.values(), format));
        ArrayNode expected = mapper.createArrayNode();
        for (Shape s : Shape.values()) {
            expected.add(s.name());
        }
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testSerializationWithCustomValues() throws Exception {
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), false);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormat format = new JsonFormat(Shape.ARRAY);
        JsonNode result = mapper.readTree(mapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(Shape.values(), format));
        ArrayNode expected = mapper.createArrayNode();
        for (SerializableString value : serializer.getEnumValues().values()) {
            expected.add(value.getValue());
        }
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testSerializationWithCustomValuesAndToString() throws Exception {
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), false);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("writeEnumUsingToString", true);
        JsonNode result = mapper.readTree(mapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(Shape.values()));
        ArrayNode expected = mapper.createArrayNode();
        for (Enum<?> e : EnumSet.allOf(Shape.class)) {
            expected.add(e.toString());
        }
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testSerializationWithCustomValuesAndIndex() throws Exception {
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), true);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("writeEnumUsingToString", true);
        JsonNode result = mapper.readTree(mapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(Shape.values()));
        ArrayNode expected = mapper.createArrayNode();
        for (Enum<?> e : EnumSet.allOf(Shape.class)) {
            expected.add(Integer.toString(e.ordinal()));
        }
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testSerializationWithCustomValuesAndIndexWithFormat() throws Exception {
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), true);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormat format = new JsonFormat(Shape.ARRAY, true);
        JsonNode result = mapper.readTree(mapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(Shape.values(), format));
        ArrayNode expected = mapper.createArrayNode();
        for (Enum<?> e : EnumSet.allOf(Shape.class)) {
            expected.add(Integer.toString(e.ordinal()));
        }
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializationWithIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("shape", Shape.ARRAY.toString());
        String json = mapper.writerWithRootValue(node).writeValueAsString(Arrays.asList(0, 1, 2));
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(Shape.class), true);
        Shape[] result = mapper.readerForArray(Shape[].class).readValue(json);
        assertArrayEquals(Shape.values(), result);
    }

    @Test
    public void testDeserializationWithToString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("shape", Shape.ARRAY.toString());
        String json = mapper.writerWithRootValue(node).writeValueAsString(Arrays.asList("STRING", "ARRAY", "NUMBER"));
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(Shape.class), false);
        Shape[] result = mapper.readerForArray(Shape[].class).readValue(json);
        assertArrayEquals(Shape.values(), result);
    }

    @Test
    public void testDeserializationWithNames() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormat format = new JsonFormat(Shape.ARRAY);
        String json = mapper.writerWithRootValue(node).writeValueAsString(mapper.writerWithRootValueSerializer(new TestEnumSerializer(EnumValues.construct(Shape.class), false))
                .writeValueAsString(Arrays.asList(Shape.values())));
        Shape[] result = mapper.readerForArray(Shape[].class).readValue(json);
        assertArrayEquals(Shape.values(), result);
    }

    @Test
    public void testDeserializationWithCustomValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormat format = new JsonFormat(Shape.ARRAY);
        String json = mapper.writerWithRootValue(node).writeValueAsString(mapper.writerWithRootValueSerializer(new TestEnumSerializer(EnumValues.construct(Shape.class), false))
                .writeValueAsString(Arrays.asList(Shape.values())));
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), false);
        Shape[] result = mapper.readerForArray(Shape[].class).readValue(json);
        assertArrayEquals(Shape.values(), result);
    }

    @Test
    public void testDeserializationWithCustomValuesAndToString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("writeEnumUsingToString", true);
        String json = mapper.writerWithRootValue(node).writeValueAsString(mapper.writerWithRootValueSerializer(new TestEnumSerializer(EnumValues.construct(Shape.class), false))
                .writeValueAsString(Arrays.asList(Shape.values())));
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), false);
        Shape[] result = mapper.readerForArray(Shape[].class).readValue(json);
        assertArrayEquals(Shape.values(), result);
    }

    @Test
    public void testDeserializationWithCustomValuesAndIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("writeEnumUsingToString", true);
        String json = mapper.writerWithRootValue(node).writeValueAsString(mapper.writerWithRootValueSerializer(new TestEnumSerializer(EnumValues.construct(Shape.class), true))
                .writeValueAsString(Arrays.asList(Shape.values())));
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), true);
        Shape[] result = mapper.readerForArray(Shape[].class).readValue(json);
        assertArrayEquals(Shape.values(), result);
    }

    @Test
    public void testDeserializationWithCustomValuesAndIndexWithFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("writeEnumUsingToString", true);
        JsonFormat format = new JsonFormat(Shape.ARRAY, true);
        String json = mapper.writerWithRootValue(node).writeValueAsString(mapper.writerWithRootValueSerializer(new TestEnumSerializer(EnumValues.construct(Shape.class), true))
                .writeValueAsString(Arrays.asList(Shape.values())));
        EnumSerializer serializer = new TestEnumSerializer(EnumValues.construct(Shape.class), true);
        Shape[] result = mapper.readerForArray(Shape[].class).readValue(json);
        assertArrayEquals(Shape.values(), result);
    }

    @Test
    public void testSchema() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(Shape.class), false);
        JsonNode schema = serializer.getSchema(new SerializerProvider(), null);
        ObjectNode expected = (ObjectNode) JsonTestUtils.readValue(getClass().getResourceAsStream("/testEnumSerializerSchema.json"), ObjectNode.class);
        assertEquals(expected, schema);
    }

}