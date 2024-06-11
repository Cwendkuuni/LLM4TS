package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceEventArgs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectReader.BaseJsonReaderFactory;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.JsonDeserializer.Provider;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MappingIteratorTest extends TestCase {

    public void testEmptyIterator() throws Exception {
        JavaType type = MapperTest.mapper().getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iter = new MappingIterator<String>(type, null, null, null, false, null);

        assertFalse("Iterator should not have next value", iter.hasNext());
        iter.close();
    }

    public void testIteratorWithValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("[\"foo\", \"bar\"]");

        JavaType type = MapperTest.mapper().getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iter = new MappingIterator<String>(type, parser, mapper.getDeserializationContext(), null, true, null);

        // Test hasNext and next methods
        assertTrue("Iterator should have next value", iter.hasNext());
        assertEquals("Iterator should return 'foo'", iter.next());
        assertTrue("Iterator should have next value", iter.hasNext());
        assertEquals("Iterator should return 'bar'", iter.next());
        assertFalse("Iterator should not have next value", iter.hasNext());

        // Test close method
        iter.close();
        assertFalse("Iterator should not have next value", iter.hasNext());

        // Test readAll method
        List<String> resultList = new ArrayList<>();
        iter.readAll(resultList);
        assertEquals(Arrays.asList("foo", "bar"), resultList);

        // Test readAll with custom Collection
        List<String> customList = new ArrayList<>();
        iter.readAll(customList);
        assertEquals(Collections.singletonList("foo"), customList);
        assertEquals(Collections.singletonList("bar"), Arrays.asList(iter.nextValue(), iter.nextValue()));
        assertFalse("Iterator should not have next value", iter.hasNext());

        // Test getParser method
        JsonParser parserFromIterator = iter.getParser();
        JsonToken token = parserFromIterator.getCurrentToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parserFromIterator.close();

        // Test getParserSchema method
        // Note: this test depends on the version of Jackson being used and the availability of FormatSchema
        //FormatSchema schema = iter.getParserSchema();
        //assertTrue(schema != null);

        // Test getCurrentLocation method
        JsonLocation location = iter.getCurrentLocation();
        assertEquals(1, location.getLineNumber());
        assertEquals(1, location.getColumnNumber());
    }

    public void testIteratorWithException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("{ \"foo\": 1 }");

        JavaType type = MapperTest.mapper().getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iter = new MappingIterator<String>(type, parser, mapper.getDeserializationContext(), null, true, null);

        // Test hasNext and next methods with JsonMappingException
        try {
            assertTrue("Iterator should have next value", iter.hasNext());
            String nextValue = (String) iter.next();
            fail("Should have thrown JsonMappingException");
        } catch (JsonMappingException e) {
            String nextReference = ((ReferenceEventArgs) e.getReference()).getPath();
            assertEquals("/foo", nextReference);
            // Test hasNext method after JsonMappingException
            assertTrue("Iterator should have next value", iter.hasNext());
        }

        // Test hasNext and next methods with IOException
        try {
            assertTrue("Iterator should have next value", iter.hasNext());
            String nextValue = (String) iter.next();
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // Test hasNext method after IOException
            assertFalse("Iterator should not have next value", iter.hasNext());
        }

        // Test close method
        iter.close();
        assertFalse("Iterator should not have next value", iter.hasNext());

        // Test readAll method with JsonMappingException
        List<String> resultList = new ArrayList<>();
        try {
            iter.readAll(resultList);
            fail("Should have thrown JsonMappingException");
        } catch (JsonMappingException e) {
            fail("Should have thrown JsonMappingException but got " + e.getClass().getName());
        }

        // Test readAll method with IOException
        try {
            iter.readAll(resultList);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            fail("Should have thrown IOException but got " + e.getClass().getName());
        }
    }

    public void testIteratorWithNullValueProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("[null]");

        JavaType type = MapperTest.mapper().getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iter = new MappingIterator<String>(type, parser, mapper.getDeserializationContext(), null, true, null);

        // Test nextValue method with null value
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt, JavaType type) {
                return null;
            }
        };
        JsonDeserializer<String> deserializer = new ContextualDeserializer<>(nullValueProvider);
        MappingIterator<String> iterWithNullValueProvider = new MappingIterator<String>(type, parser, mapper.getDeserializationContext(), deserializer, true, null);
        assertEquals(null, iterWithNullValueProvider.nextValue());

        // Test readAll method with null value
        List<String> resultList = new ArrayList<>();
        iterWithNullValueProvider.readAll(resultList);
        assertEquals(Collections.singletonList(null), resultList);

        // Test iterator with null value and close method
        iterWithNullValueProvider.close();
        assertFalse("Iterator should not have next value", iterWithNullValueProvider.hasNext());
    }

    public void testIteratorWithCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("[1, 2.5]");

        JavaType intType = MapperTest.mapper().getTypeFactory().constructCollectionType(List.class, Integer.class);
        JavaType doubleType = MapperTest.mapper().getTypeFactory().constructCollectionType(List.class, Double.class);

        MappingIterator<Integer> intIterator = new MappingIterator<Integer>(intType, parser, mapper.getDeserializationContext(), new IntDeserializer(), true, null);
        MappingIterator<Double> doubleIterator = new MappingIterator<Double>(doubleType, parser, mapper.getDeserializationContext(), new DoubleDeserializer(), true, null);

        // Test hasNext and next methods
        assertTrue("Iterator should have next value", intIterator.hasNext());
        assertEquals(1, intIterator.next().intValue());
        assertTrue("Iterator should have next value", intIterator.hasNext());
        assertEquals(2, intIterator.next().intValue());
        assertFalse("Iterator should not have next value", intIterator.hasNext());

        assertTrue("Iterator should have next value", doubleIterator.hasNext());
        assertEquals(1.0, doubleIterator.next().doubleValue(), 0.0);
        assertTrue("Iterator should have next value", doubleIterator.hasNext());
        assertEquals(2.5, doubleIterator.next().doubleValue(), 0.0);
        assertFalse("Iterator should not have next value", doubleIterator.hasNext());

        // Test close methods
        intIterator.close();
        assertFalse("Iterator should not have next value", intIterator.hasNext());
        doubleIterator.close();
        assertFalse("Iterator should not have next value", doubleIterator.hasNext());

        // Test readAll method
        List<Integer> intResultList = new ArrayList<>();
        intIterator.readAll(intResultList);
        assertEquals(Arrays.asList(1, 2), intResultList);

        List<Double> doubleResultList = new ArrayList<>();
        doubleIterator.readAll(doubleResultList);
        assertEquals(Arrays.asList(1.0, 2.5), doubleResultList);
    }
}

class IntDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsInt();
    }
}

class DoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsDouble();
    }
}