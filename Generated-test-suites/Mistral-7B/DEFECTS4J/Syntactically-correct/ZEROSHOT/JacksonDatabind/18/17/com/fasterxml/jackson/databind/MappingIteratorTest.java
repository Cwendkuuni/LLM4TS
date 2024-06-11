package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.json.JsonParser.Feature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;
    private JsonParser parser;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonFactory = mapper.getFactory();
        parser = jsonFactory.createParser("[]");
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void testEmptyIterator() throws Exception {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());
    }

    @Test
    public void testHasNextAndNextWithDefaultConfiguration() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<String>(
                jsonFactory.getConstructorArgs()[0], parser, mapper.getContext(parser), mapper.findValueDeserializer(String.class), true, null
        );

        assertTrue(iterator.hasNext());
        String next = iterator.next();
        assertNull(next);

        // Check that no value was read from the parser
        assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());
    }

    @Test
    public void testHasNextAndNextWithCustomConfiguration() throws Exception {
        // Create a custom deserializer that simply returns a constant value
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForParametricType(MappingIterator.class, String.class);
        JsonDeserializer<MappingIterator<String>> mappingIteratorDeserializer = new JsonDeserializer<MappingIterator<String>>(MappingIterator.class) {
            @Override
            public MappingIterator<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
                List<Object> constructorArgs = new ArrayList<>(5);
                constructorArgs.add(jsonFactory.getConstructorArgs()[0]);
                constructorArgs.add(parser);
                constructorArgs.add(ctxt);
                constructorArgs.add(stringSerializer);
                constructorArgs.add(true);
                constructorArgs.add(null);
                return new MappingIterator<String>(
                        javaType, parser, ctxt, typeDeserializer, true, null
                );
            }
        };

        MappingIterator<String> iterator = mapper.readValue(parser, mappingIteratorDeserializer);

        assertTrue(iterator.hasNext());
        String next = iterator.next();
        assertEquals("test", next);

        // Check that a value was read from the parser
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals("test", next);

        // Check that a second value was read from the parser
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());

        // Check that there are no more values to read
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextAndNextWithCloseParser() throws Exception {
        // Create a custom deserializer that simply returns a constant value
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForParametricType(MappingIterator.class, String.class);
        JsonDeserializer<MappingIterator<String>> mappingIteratorDeserializer = new JsonDeserializer<MappingIterator<String>>(MappingIterator.class) {
            @Override
            public MappingIterator<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
                List<Object> constructorArgs = new ArrayList<>(6);
                constructorArgs.add(jsonFactory.getConstructorArgs()[0]);
                constructorArgs.add(parser);
                constructorArgs.add(ctxt);
                constructorArgs.add(stringSerializer);
                constructorArgs.add(true);
                constructorArgs.add(null);
                constructorArgs.add(true);
                return new MappingIterator<String>(
                        javaType, parser, ctxt, typeDeserializer, true, null
                );
            }
        };

        MappingIterator<String> iterator = mapper.readValue(parser, mappingIteratorDeserializer);

        assertTrue(iterator.hasNext());
        String next = iterator.next();
        assertEquals("test", next);

        // Check that a value was read from the parser
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());

        // Check that there is no next value
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        // Check that the parser was closed
        assertNull(iterator.getParser());
    }

    @Test
    public void testReadAll() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<String>(
                jsonFactory.getConstructorArgs()[0], parser, mapper.getContext(parser), mapper.findValueDeserializer(String.class), true, null
        );

        List<String> resultList = new ArrayList<String>();
        iterator.readAll(resultList);

        List<String> expectedList = Collections.singletonList("test");
        assertEquals(expectedList, resultList);

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNull(next);

        // Check that no more values can be read
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        // Check that the parser was closed
        assertNull(iterator.getParser());
    }

    @Test
    public void testReadAllWithPreallocatedCollection() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<String>(
                jsonFactory.getConstructorArgs()[0], parser, mapper.getContext(parser), mapper.findValueDeserializer(String.class), true, null
        );

        Collection<String> results = new ArrayList<String>(2);
        iterator.readAll(results);

        assertEquals(Collections.singletonList("test"), results);

        assertTrue(iterator.hasNext());
        String next = iterator.next();
        assertNull(next);

        // Check that no more values can be read
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        // Check that the parser was closed
        assertNull(iterator.getParser());
    }
}