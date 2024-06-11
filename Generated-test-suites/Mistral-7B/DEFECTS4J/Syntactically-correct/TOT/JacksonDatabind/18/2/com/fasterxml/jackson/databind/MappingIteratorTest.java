package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    @Test
    public void testMappingIteratorNormalUsage() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<Integer> list = new ArrayList<>();
        mapper.writeValueAsString(list).trim().replaceAll("\\s+", "");
        JsonParser parser = new JsonFactory().createParser(mapper.readTree(mapper.writeValueAsString(list)));
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new StdDeserializer<Integer>(Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                parser, ctxt, deserializer, true, null);

        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        Assert.assertEquals(list, resultList);
        iterator.close();
        parser.close();
    }

    @Test
    public void testMappingIteratorNullInput() {
        ObjectMapper mapper = new ObjectMapper();
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            List<Object> resultList = new ArrayList<>();
            iterator.readAll(resultList);
            Assert.fail("Expected NoSuchElementException");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertFalse(iterator.hasNextValue());
    }

    @Test(expected = RuntimeJsonMappingException.class)
    public void testMappingIteratorInvalidInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":[]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new StdDeserializer<Integer>(Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                parser, ctxt, deserializer, true, null);
        iterator.nextValue();
        iterator.nextValue();
        iterator.nextValue();
        iterator.nextValue();
        ObjectNode tree = mapper.readTree(parser);
        parser.close();
        Assert.assertFalse(iterator.hasNextValue());
        iterator.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testMappingIteratorExceptionInDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":[100]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new ThrowingDeserializer();
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                parser, ctxt, deserializer, true, null);
        iterator.nextValue();
        iterator.nextValue();
        iterator.nextValue();
        iterator.close();
    }

    @Test
    public void testMappingIteratorCloseParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":[1, 2, 3]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new StdDeserializer<Integer>(Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                parser, ctxt, deserializer, false, null);
        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        Assert.assertEquals(3, resultList.size());
        iterator.close();
        Assert.assertFalse(iterator.hasNextValue());
        Assert.assertFalse(parser.isOpen());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static class ThrowingDeserializer extends StdDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonMappingException {
            throw new JsonMappingException("Test exception during deserialization");
        }
    }
}