package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.ser.std.StdSerializer.constructIndexedSerializer;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonFactory factory;

    @BeforeEach
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        factory = mapper.getFactory();
        reader = mapper.readerForListOf(String.class);
    }

    @Test
    public void testIterationOverEmptyList() throws IOException {
        JsonParser parser = factory.createParser(new StringReader("[]"));
        MappingIterator<String> emptyIterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, false, null);

        assertTrue(emptyIterator.hasNext());
        assertFalse(emptyIterator.hasNext());
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testIterationOverSingleElementList() throws IOException {
        String jsonString = "[\"Hello World\"]";
        JsonParser parser = factory.createParser(new StringReader(jsonString));

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, false, null);

        assertEquals("Hello World", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterationOverMultipleElementList() throws IOException {
        String jsonString = "[\"Hello World\", \"Jackson\", \"Gson\"]";
        JsonParser parser = factory.createParser(new StringReader(jsonString));

        List<String> expectedList = Arrays.asList("Hello World", "Jackson", "Gson");
        List<String> list = new ArrayList<>();

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, false, null);

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        assertEquals(expectedList, list);
    }

    @Test
    public void testIterationOverEmptyArray() throws IOException {
        JsonParser parser = factory.createParser(new StringReader("[]"));
        MappingIterator<String> emptyIterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, true, null);

        assertTrue(emptyIterator.hasNext());
        assertFalse(emptyIterator.hasNext());
        assertFalse(emptyIterator.hasNext());
        emptyIterator.close();
    }

    @Test
    public void testIterationOverSingleElementArray() throws IOException {
        String jsonString = "[\"Hello World\"]";
        JsonParser parser = factory.createParser(new StringReader(jsonString));

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, true, null);

        assertEquals("Hello World", iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIterationOverMultipleElementArray() throws IOException {
        String jsonString = "[\"Hello World\", \"Jackson\", \"Gson\"]";
        JsonParser parser = factory.createParser(new StringReader(jsonString));

        List<String> expectedList = Arrays.asList("Hello World", "Jackson", "Gson");
        List<String> list = new ArrayList<>();

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, true, null);

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        assertEquals(expectedList, list);
        iterator.close();
    }

    @Test
    public void testIterationOverNullArray() throws IOException {
        JsonParser parser = factory.createParser(new StringReader("null"));

        List<String> expectedList = Collections.emptyList();
        List<String> list = new ArrayList<>();

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, true, null);

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        assertEquals(expectedList, list);
        iterator.close();
    }

    @Test
    public void testIterationOverNullList() throws IOException {
        JsonParser parser = factory.createParser(new StringReader("null"));

        List<String> expectedList = Collections.emptyList();
        List<String> list = new ArrayList<>();

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, false, null);

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        assertEquals(expectedList, list);
        iterator.close();
    }

    @Test
    public void testIterationOverEmptyObject() throws IOException {
        JsonParser parser = factory.createParser(new StringReader("{}"));
        MappingIterator<String> emptyIterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, true, null);

        assertTrue(emptyIterator.hasNext());
        assertFalse(emptyIterator.hasNext());
        assertFalse(emptyIterator.hasNext());
        emptyIterator.close();
    }

    @Test
    public void testIterationOverSingleElementObject() throws IOException {
        String jsonString = "{\"name\": \"Hello World\"}";
        JsonParser parser = factory.createParser(new StringReader(jsonString));

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, true, null);

        assertEquals("Hello World", iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIterationOverMultipleElementObject() throws IOException {
        String jsonString = "{\"data\": [{\"name\": \"Hello World\", \"age\": 42}, {\"name\": \"Jackson\", \"age\": 37}, {\"name\": \"Gson\", \"age\": 21}]}";
        JsonParser parser = factory.createParser(new StringReader(jsonString));

        List<Map<String, Object>> expectedList = Arrays.asList(
                Map.of("name", "Hello World", "age", 42),
                Map.of("name", "Jackson", "age", 37),
                Map.of("name", "Gson", "age", 21)
        );
        List<Map<String, Object>> list = new ArrayList<>();

        MappingIterator<Map<String, Object>> iterator = new MappingIterator<>(reader.getType(parser), parser, mapper.getDeserializationContext(), null, true, null);

        Converter<Object, Map<String, Object>> objectToMapConverter = mapper.getFactory().getValueFactory().findValueDeserializer(Map.class).getValueDeserializer();
        TypeDeserializer mapTypeDeserializer = mapper.getTypeFactory().constructType(Map.class);
        TypeSerializer mapTypeSerializer = mapper.getTypeFactory().constructTypeSerializer(Map.class);

        while (iterator.hasNext()) {
            list.add((Map<String, Object>) iterator.next());
        }

        assertEquals(expectedList, list);
        iterator.close();
    }

    @Test
    public void testIterationOverInvalidJson() throws IOException {
        JsonParser parser = factory.createParser(new StringReader("{"));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, objectMapper.getDeserializationContext(), null, true, null);
            iterator.next();
            fail();
        } catch (JsonMappingException e) {
            assertTrue(true);
        }

        parser.close();
    }

    @Test
    public void testIterationOverInvalidJsonWithCustomExceptionHandler() throws IOException {
        JsonParser parser = factory.createParser(new StringReader("{"));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = objectMapper.getTypeFactory().getDefaultTypingResolver();
        ObjectMapper.DefaultTypingResolver.DelegatingResolver delegratingTypingResolver = new ObjectMapper.DefaultTypingResolver.DelegatingResolver(defaultTypingResolver);

        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.setDefaultTypingResolver(delegratingTypingResolver);
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        customObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        customObjectMapper.setFactory(new JsonMapperFactory(false, customObjectMapper.getFactory().getConfig(), customObjectMapper.getConfig()));

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser), parser, customObjectMapper.getDeserializationContext(), null, true, null);

        try {
            iterator.next();
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Cannot construct instance of \"java.util.HashMap\": no suitable constructor found", e.getMessage());
        }

        parser.close();
    }
}