package com.fasterxml.jackson.databind.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.JsonMapping.FromContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory FACTORY = MAPPER.getFactory();
    private static final TypeFactory TYPE_FACTORY = MAPPER.getTypeFactory();

    @Before
    public void setUp() throws IOException {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @After
    public void tearDown() throws IOException {
        MAPPER = null;
    }

    @Test
    public void testEmptyIterator() throws IOException {
        JsonParser parser = FACTORY.createParser("[]");
        ObjectReader reader = MAPPER.readerForElementValue(String.class);
        MappingIterator<String> iterator = reader.readValues(parser, String.class);

        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIteratorWithOneValue() throws IOException {
        JsonParser parser = FACTORY.createParser("\"hello\"");
        ObjectReader reader = MAPPER.readerForElementValue(String.class);
        MappingIterator<String> iterator = reader.readValues(parser, String.class);

        assertTrue(iterator.hasNext());
        assertEquals("hello", iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIteratorWithListOfValues() throws IOException {
        JsonParser parser = FACTORY.createParser("[\"hello\", \"world\"]");
        ObjectReader reader = MAPPER.readerForValueType(Collections.class, new TypeReference<List<String>>(){});
        MappingIterator<List<String>> iterator = reader.readValues(parser, List.class);

        assertTrue(iterator.hasNext());
        List<String> firstValue = iterator.next();
        assertEquals(2, firstValue.size());
        assertEquals("hello", firstValue.get(0));
        assertEquals("world", firstValue.get(1));
        assertTrue(iterator.hasNext());
        List<String> secondValue = iterator.next();
        assertEquals(2, secondValue.size());
        assertNotSame(firstValue, secondValue);
        assertEquals("hello", secondValue.get(0));
        assertEquals("world", secondValue.get(1));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIteratorWithArrayOfValues() throws IOException {
        JsonParser parser = FACTORY.createParser("[\"hello\", \"world\"]");
        ObjectReader reader = MAPPER.readerForValueType(ArrayNode.class);
        MappingIterator<ArrayNode> iterator = reader.readValues(parser, ArrayNode.class);

        assertTrue(iterator.hasNext());
        ArrayNode firstValue = iterator.next();
        assertEquals(2, firstValue.size());
        assertEquals("hello", firstValue.get(0).asText());
        assertEquals("world", firstValue.get(1).asText());
        assertTrue(iterator.hasNext());
        ArrayNode secondValue = iterator.next();
        assertEquals(2, secondValue.size());
        assertNotSame(firstValue, secondValue);
        assertEquals("hello", secondValue.get(0).asText());
        assertEquals("world", secondValue.get(1).asText());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIteratorWithCustomDeserializer() throws IOException {
        JsonParser parser = FACTORY.createParser("[1, 2, 3]");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(Integer.class, new JsonDeserializer<Integer>() {
                    @Override
                    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsInt();
                    }
                })
        );
        ObjectReader reader = mapper.readerForValueType(List.class, new TypeReference<List<Integer>>(){});
        MappingIterator<List<Integer>> iterator = reader.readValues(parser, List.class);

        assertTrue(iterator.hasNext());
        List<Integer> firstValue = iterator.next();
        assertEquals(3, firstValue.size());
        assertEquals(1, firstValue.get(0));
        assertEquals(2, firstValue.get(1));
        assertEquals(3, firstValue.get(2));
        assertTrue(iterator.hasNext());
        List<Integer> secondValue = iterator.next();
        assertEquals(3, secondValue.size());
        assertNotSame(firstValue, secondValue);
        assertEquals(1, secondValue.get(0));
        assertEquals(2, secondValue.get(1));
        assertEquals(3, secondValue.get(2));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIteratorWithUpdateValue() throws IOException {
        JsonParser parser = FACTORY.createParser("[\"hello\", \"world\"]");
        ObjectReader reader = MAPPER.readerForValueType(String.class);
        String initialValue = "initial";
        MappingIterator<String> iterator = reader.readValues(parser, String.class, initialValue);

        assertTrue(iterator.hasNext());
        assertEquals("hello", iterator.next());
        assertEquals("initial", iterator.next());
        assertEquals("world", iterator.next());
        assertFalse(iterator.hasNext());

        ObjectNode node = FACTORY.createObjectNode();
        node.put("updated", "value");

        iterator = reader.readValues(parser, String.class, node);

        assertTrue(iterator.hasNext());
        assertEquals("hello", iterator.next());
        assertEquals("value", iterator.next());
        assertEquals("world", iterator.next());
        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testIteratorWithCloseParser() throws IOException {
        JsonParser parser = FACTORY.createParser("[\"hello\", \"world\"]");
        ObjectReader reader = MAPPER.readerForValueType(String.class);

        MappingIterator<String> iterator1 = reader.readValues(parser, String.class);
        assertTrue(iterator1.hasNext());
        iterator1.next();

        MappingIterator<String> iterator2 = reader.readValues(parser, String.class);
        assertFalse(iterator2.hasNext());

        parser.close();

        MappingIterator<String> iterator3 = reader.readValues(parser, String.class);
        assertFalse(iterator3.hasNext());
    }

    @Test
    public void testIteratorWithTypeDeserializer() throws IOException {
        JsonParser parser = FACTORY.createParser("[{\"name\": \"John\", \"age\": 30}, {\"name\": \"Mary\", \"age\": 25}]");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReader reader = mapper.readerForValueType(List.class, new TypeReference<List<Person>>(){});
        reader.setAnnotationIntrospector(new StandardAnnotationIntrospector());

        MappingIterator<List<Person>> iterator = reader.readValues(parser, List.class);
        assertEquals(2, iterator.next().size());

        Person john = iterator.next();
        assertEquals("John", john.getName());
        assertEquals(30, john.getAge());

        Person mary = iterator.next();
        assertEquals("Mary", mary.getName());
        assertEquals(25, mary.getAge());

        assertFalse(iterator.hasNext());
        iterator.close();
    }

    static class Person {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}