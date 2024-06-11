package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest extends TestBase {

    public static class TestBean {
        private int value;

        public TestBean(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class TestDeserializer extends StdDeserializer<TestBean> {
        public TestDeserializer() {
            this(null);
        }

        public TestDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            int value = p.nextIntValue();
            return new TestBean(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> MappingIterator<T> createMappingIterator(ObjectMapper mapper, JsonParser parser, Class<T> valueType) throws IOException {
        JsonDeserializer<T> deserializer = mapper.findValueDeserializer(valueType, null);
        return new MappingIterator<>(mapper.getTypeFactory().constructType(valueType), parser, mapper.getDeserializationContext(), deserializer, true, null);
    }

    @org.junit.Test
    public void testMappingIteratorHasNext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", 1);
        node.put("bar", 2);
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        List<TestBean> values = new ArrayList<>();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            assertTrue(iterator.hasNext());
            values.add(iterator.next());
            assertTrue(iterator.hasNext());
            values.add(iterator.next());
            assertFalse(iterator.hasNext());
        }
        assertEquals(2, values.size());
    }

    @org.junit.Test
    public void testMappingIteratorClosesParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", 1);
        node.put("bar", 2);
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            assertTrue(iterator.hasNext());
            iterator.next();
            Iterator<TestBean> copy = iterator;
            assertTrue(iterator.hasNext());
            assertFalse(copy.hasNext());
        }
        // parser should be closed
        assertFalse(parser.isOpen());
    }

    @org.junit.Test
    public void testMappingIteratorHandlesEmptyJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            assertFalse(iterator.hasNext());
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesNullJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(null);
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            assertFalse(iterator.hasNext());
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "not a number");
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            assertTrue(iterator.hasNext());
            iterator.next();
            parser.nextToken(); // force exception
            assertNull(iterator.next());
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "not a TestBean");
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            assertTrue(iterator.hasNext());
            iterator.next();
            parser.nextToken(); // force exception
            assertNull(iterator.next());
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesUpdateValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", 1);
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        TestBean initialValue = new TestBean(1);
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            iterator.next();
            TestBean updatedValue = new TestBean(2);
            iterator.setUpdatedValue(updatedValue);
            TestBean nextValue = iterator.next();
            assertEquals(updatedValue, nextValue);
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        JsonSchema schema = JsonSchemaFactory.getSchema(TestBean.class);
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            JsonMappingException e = assertThrows(JsonMappingException.class, () -> iterator.next());
            JsonSchema expectedSchema = new JsonSchema.ReferenceSchema(new ObjectNode(mapper.createObjectNode()));
            expectedSchema.setSchema(schema);
            assertEquals(expectedSchema, iterator.getParserSchema());
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesEmptyList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        SimpleType emptyListType = mapper.getTypeFactory().constructCollectionType(List.class, TestBean.class);
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, emptyListType)) {
            assertFalse(iterator.hasNext());
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Collection<TestBean> emptyCollection = new ArrayList<>();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, null, TestBean.class)) {
            iterator.setUpdatedValue(emptyCollection);
            assertFalse(iterator.hasNext());
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesCollectionWithSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", new ObjectNode(mapper.createObjectNode()));
        JsonParser parser = mapper.treeToValues(node, TestBean.class)[0].getParser();
        List<TestBean> expectedValues = new ArrayList<>();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            TestBean firstValue = iterator.next();
            expectedValues.add(firstValue);
            TestBean secondValue = iterator.next();
            assertEquals(2, expectedValues.size());
            assertEquals(firstValue, secondValue);
        }
    }

    @org.junit.Test
    public void testMappingIteratorHandlesCollectionWithMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "[1,2,3]");
        JsonParser parser = mapper.treeToValues(node, TestBean[].class)[0].getParser();
        List<TestBean> expectedValues = new ArrayList<>();
        try (MappingIterator<TestBean> iterator = createMappingIterator(mapper, parser, TestBean.class)) {
            TestBean firstValue = iterator.next();
            assertEquals(1, firstValue.getValue());
            TestBean secondValue = iterator.next();
            assertEquals(2, secondValue.getValue());
            TestBean thirdValue = iterator.next();
            assertEquals(3, thirdValue.getValue());
            List<TestBean> actualValues = iterator.readAll();
            assertEquals(3, actualValues.size());
            assertEquals(expectedValues, actualValues);
        }
    }
}