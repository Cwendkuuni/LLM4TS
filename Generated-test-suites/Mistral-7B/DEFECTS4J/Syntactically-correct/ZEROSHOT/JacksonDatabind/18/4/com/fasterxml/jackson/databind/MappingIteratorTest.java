package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest extends BaseTest {

    public void testMappingIteratorEmpty() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(List.class);
        MappingIterator<Object> iterator = reader.emptyIterator();

        assertFalse(iterator.hasNext());
    }

    public void testMappingIteratorBasic() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(List.class);
        JsonNode node = mapper.readTree("[[1, 2], [3, 4]]");
        JsonParser parser = node.traversingParser();
        MappingIterator<Object> iterator = reader.readValues(parser);

        assertEquals(1, iterator.next().getClass().getSimpleName());
        assertEquals(2, iterator.next().getClass().getSimpleName());
        assertEquals(3, iterator.next().getClass().getSimpleName());
        assertEquals(4, iterator.next().getClass().getSimpleName());
        assertFalse(iterator.hasNext());

        parser.close();
    }

    public void testMappingIteratorException() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(List.class);
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [\"error\"]]");
        JsonParser parser = node.traversingParser();
        MappingIterator<Object> iterator = reader.readValues(parser);

        assertEquals(1, iterator.next().getClass().getSimpleName());
        assertEquals(2, iterator.next().getClass().getSimpleName());

        JsonMappingException exception = expectThrows(JsonMappingException.class, () -> iterator.next());
        assertEquals("Failed to read value of type: java.lang.String from JSON String: error", exception.getMessage());
        assertFalse(iterator.hasNext());

        parser.close();
    }

    public void testMappingIteratorCustomDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReader reader = mapper.readerFor(Map.class);
        JsonNode node = mapper.readTree("{\"foo\": [1, 2], \"bar\": [3, 4]}");
        JsonParser parser = node.traversingParser();

        reader.setDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        MapType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        reader = reader.withValueDeserializer(new JsonDeserializer<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Map<String, Integer> result = new HashMap<>();
                while (p.nextToken() != JsonToken.END_OBJECT) {
                    String name = p.getText();
                    result.put(name, p.nextIntValue());
                }
                return result;
            }
        });

        MappingIterator<Map<String, Integer>> iterator = reader.readValues(parser);
        Map<String, Integer> map = iterator.next();
        assertEquals(2, map.size());
        assertEquals("foo", map.keySet().iterator().next());
        assertEquals(1, map.get("foo"));
        assertEquals("bar", map.keySet().iterator().next());
        assertEquals(3, map.get("bar"));
        assertFalse(iterator.hasNext());

        parser.close();
    }

    public void testMappingIteratorNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(List.class);
        JsonNode node = mapper.readTree("[[null], [null]]");
        JsonParser parser = node.traversingParser();
        MappingIterator<Object> iterator = reader.readValues(parser);

        assertNull(iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());

        parser.close();
    }

    public void testMappingIteratorCloseParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(List.class);
        JsonNode node = mapper.readTree("[[1, 2], [3, 4]]");
        JsonParser parser = node.traversingParser();
        MappingIterator<Object> iterator = reader.readValues(parser);

        iterator.next();
        iterator.next();
        iterator.close();

        assertFalse(iterator.hasNext());
        assertFalse(parser.hasNextToken());
    }

    public void testMappingIteratorUpdateValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(List.class);
        JsonNode node = mapper.readTree("[[1, 2], [3, 4]]");
        JsonParser parser = node.traversingParser();

        ObjectNode factory = new JsonFactory().getFactory().createObjectNode();
        factory.put("firstName", "John");
        factory.put("lastName", "Doe");

        reader.setDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_WRAPPER_ARRAY);
        MappingIterator<ObjectNode> iterator = reader.readValues(parser);

        ObjectNode node1 = iterator.next();
        ObjectNode node2 = iterator.next();

        assertEquals(2, node1.size());
        assertEquals("firstName", node1.get(0).asText());
        assertEquals("1", node1.get(1).asText());

        node1.set("firstName", "Jane");

        assertEquals("Jane", node1.get(0).asText());

        iterator.next();

        ObjectNode node3 = iterator.next();
        ObjectNode node4 = iterator.next();

        assertEquals("lastName", node2.get(0).asText());
        assertEquals("Doe", node2.get(1).asText());

        node2.set("lastName", "Smith");

        assertEquals("Smith", node2.get(0).asText());

        assertFalse(iterator.hasNext());

        parser.close();
    }

    public void testMappingIteratorListReadAll() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(List.class);
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [5, 6]]");
        JsonParser parser = node.traversingParser();

        List<Object> list = iteratorToList(reader.readValues(parser));

        assertEquals(6, list.size());
        assertEquals(1, list.get(0).getClass().getSimpleName());
        assertEquals(2, list.get(1).getClass().getSimpleName());
        assertEquals(3, list.get(2).getClass().getSimpleName());
        assertEquals(4, list.get(3).getClass().getSimpleName());
        assertEquals(5, list.get(4).getClass().getSimpleName());
        assertEquals(6, list.get(5).getClass().getSimpleName());
    }

    public void testMappingIteratorCollectionReadAll() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(ArrayList.class);
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [5, 6]]");
        JsonParser parser = node.traversingParser();

        ArrayList<Object> list = iteratorToCollection(reader.readValues(parser));

        assertEquals(6, list.size());
        assertEquals(1, list.get(0).getClass().getSimpleName());
        assertEquals(2, list.get(1).getClass().getSimpleName());
        assertEquals(3, list.get(2).getClass().getSimpleName());
        assertEquals(4, list.get(3).getClass().getSimpleName());
        assertEquals(5, list.get(4).getClass().getSimpleName());
        assertEquals(6, list.get(5).getClass().getSimpleName());
    }

    private <T> List<T> iteratorToList(MappingIterator<T> iterator) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNextValue()) {
            list.add(iterator.nextValue());
        }
        return list;
    }

    private <T> Collection<T> iteratorToCollection(MappingIterator<T> iterator) {
        ArrayList<T> list = new ArrayList<>();
        while (iterator.hasNextValue()) {
            list.add(iterator.nextValue());
        }
        return list;
    }
}