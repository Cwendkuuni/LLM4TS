package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private DeserializationContext context;
    private JsonDeserializer<String> stringDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        context = objectMapper.getDeserializationContext();
        stringDeserializer = objectMapper.findValueDeserializer(String.class);
    }

    @Test
    public void testEmptyIterator() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("[]");
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testSingleElementIterator() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[\"Test\"]";
        JsonParser parser = factory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);
        String nextElement = iterator.next();
        assertEquals("Test", nextElement);
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testMultipleElementsIterator() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[\"Test1\", \"Test2\", \"Test3\"]";
        JsonParser parser = factory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);
        List<String> resultList = new ArrayList<>();
        while (iterator.hasNext()) {
            resultList.add(iterator.next());
        }
        iterator.close();
        assertEquals(3, resultList.size());
        assertEquals("Test1", resultList.get(0));
        assertEquals("Test2", resultList.get(1));
        assertEquals("Test3", resultList.get(2));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[\"Test1\", \"Test2\"]";
        JsonParser parser = factory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);
        iterator.next();
        iterator.next();
        iterator.next(); // this one will throw NoSuchElementException
    }

    @Test(expected = IOException.class)
    public void testIOException() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[]";
        JsonParser parser = factory.createParser(jsonString);
        parser.nextToken(); // parse valid JSON token
        parser.setErrorHandler(new ThrowingErrorHandler()); // set custom error handler
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);
        iterator.next(); // should throw IOException
    }

    @Test
    public void testCloseParser() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[\"Test\"]";
        JsonParser parser = factory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, true, null);
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAll() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[\"Test1\", \"Test2\", \"Test3\"]";
        JsonParser parser = factory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);

        List<String> resultList = iterator.readAll();
        assertEquals(3, resultList.size());
        assertEquals("Test1", resultList.get(0));
        assertEquals("Test2", resultList.get(1));
        assertEquals("Test3", resultList.get(2));
        iterator.close();
    }

    @Test
    public void testReadAllCollection() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[\"Test1\", \"Test2\", \"Test3\"]";
        JsonParser parser = factory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);

        ArrayList<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("Test1", resultList.get(0));
        assertEquals("Test2", resultList.get(1));
        assertEquals("Test3", resultList.get(2));
        iterator.close();
    }

    @Test
    public void testEmptyCollectionReadAll() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        JsonParser parser = factory.createParser("[]");
        MappingIterator<String> iterator = new MappingIterator<String>(
                context.constructType(List.class), parser, context, stringDeserializer, false, null);
        iterator.readAll(new ArrayList<>());
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.close();
    }

    @Test
    public void testUpdateValue() throws Exception {
        JsonFactory factory = objectMapper.getFactory();
        String jsonString = "[1, 2, 3]";
        JsonParser parser = factory.createParser(jsonString);
        ObjectNode node = factory.createObjectNode();
        node.put("value", 10);
        MappingIterator<Number> iterator = new MappingIterator<Number>(
                context.constructType(List.class), parser, context, new NumberDeserializer(), false, node);

        Number nextValue = iterator.nextValue();
        assertEquals(1, nextValue.intValue());

        node.put("value", 20);
        nextValue = iterator.nextValue();
        assertEquals(20, nextValue.intValue());

        iterator.close();
    }

    static class NumberDeserializer extends JsonDeserializer<Number> {
        @Override
        public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Number number = null;
            if (p.isExpectedNumberToken()) {
                number = NumberUtils.parseNumber(p.getText());
            } else {
                throw new JsonMappingException(ctxt, p, "Expected Number but found " + p.getCurrentToken());
            }
            return number;
        }
    }
}