package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link MappingIterator}.
 */
public class MappingIteratorTest extends TestBase {

    private JsonFactory _jsonFactory;
    private ObjectMapper _objectMapper;
    private ObjectReader _objectReader;
    private DeserializationContext _deserializationContext;
    private List<Integer> _values;

    @Before
    public void setUp() throws Exception {
        _jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_CREATOR_ARRAY)
                .build();
        _objectMapper = new JsonMapper(_jsonFactory);
        _objectReader = _objectMapper.readerForListOf(Integer.class);
        _deserializationContext = _objectMapper.getDeserializationContext();
        _values = new ArrayList<Integer>();
    }

    @After
    public void tearDown() throws Exception {
        _objectMapper = null;
        _objectReader = null;
        _deserializationContext = null;
        _jsonFactory = null;
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<Integer> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());
    }

    @Test
    public void testNormalOperation() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("[1,2,3,4]".getBytes());
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                _objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, _deserializationContext, null, false, null);
        List<Integer> result = new ArrayList<Integer>();
        while (mappingIterator.hasNext()) {
            result.add(mappingIterator.next());
        }
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    public void testIteratingThroughListMultipleTimes() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("[1,2,3,4]".getBytes());
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                _objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, _deserializationContext, null, false, null);
        List<Integer> result = new ArrayList<Integer>();
        while (mappingIterator.hasNext()) {
            result.add(mappingIterator.next());
        }
        assertEquals(Collections.singletonList(1), result);
        result.clear();
        while (mappingIterator.hasNext()) {
            result.add(mappingIterator.next());
        }
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    public void testIteratingThroughListMultipleTimesWithUpdateValue() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("[1,2,3,4]".getBytes());
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        Object valueToUpdate = 5;
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                _objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, _deserializationContext, null, false, valueToUpdate);
        assertEquals(5, mappingIterator.next());
        List<Integer> result = new ArrayList<Integer>();
        while (mappingIterator.hasNext()) {
            result.add(mappingIterator.next());
        }
        assertEquals(Collections.singletonList(1), result);
        result.clear();
        while (mappingIterator.hasNext()) {
            result.add(mappingIterator.next());
        }
        assertEquals(Collections.singletonList(1), result);
        assertSame(valueToUpdate, mappingIterator.next());
    }

    @Test
    public void testClosingParser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("[1,2,3,4]".getBytes());
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                _objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, _deserializationContext, null, true, null);
        List<Integer> result = new ArrayList<Integer>();
        while (mappingIterator.hasNext()) {
            result.add(mappingIterator.next());
        }
        assertEquals(Collections.singletonList(1), result);
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testEmptyList() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("[]".getBytes());
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                _objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, _deserializationContext, null, false, null);
        assertFalse(mappingIterator.hasNext());
        assertNull(mappingIterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("[1,2,3]".getBytes());
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                _objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, _deserializationContext, null, false, null);
        while (mappingIterator.hasNext()) {
            mappingIterator.next();
        }
        mappingIterator.next();
        assertNull(mappingIterator.next());
    }

    @Test(expected = JsonMappingException.class)
    public void testMappingException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("{invalid json}".getBytes());
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                _objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, _deserializationContext, null, false, null);
        while (mappingIterator.hasNext()) {
            mappingIterator.next();
        }
        fail("MappingException expected");
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("{invalid json}".getBytes());
        JsonMapper jsonMapper = new JsonMapper(_jsonFactory);
        ObjectReader objectReader = jsonMapper.readerForListOf(Integer.class);
        DeserializationContext deserializationContext = jsonMapper.getDeserializationContext();
        JsonParser jsonParser = _objectMapper.getFactory().createParser(inputStream);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(
                objectReader.getTypeFactory().constructType(Collections.list(Integer.class)),
                jsonParser, deserializationContext, null, false, null);
        mappingIterator.next();
        mappingIterator.next();
        fail("IOException expected");
    }
}