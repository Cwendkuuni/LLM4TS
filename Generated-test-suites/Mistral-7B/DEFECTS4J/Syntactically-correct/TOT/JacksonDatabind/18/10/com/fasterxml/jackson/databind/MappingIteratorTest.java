package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class MappingIteratorTest extends BaseMapTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeMethod
    public void setUp() throws Exception {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // Disable automatic empty arrays/lists
        MAPPER.setSerializationInclusion(JsonInclude.Selection.NON_EMPTY);
    }

    @Test
    public void testIteration() throws Exception {
        JsonFactory jsonFactory = new JsonFactoryBuilder().build();
        JsonParser jsonParser = jsonFactory.createParser("[]");
        ObjectReader reader = MAPPER.readerForArrayOf(Integer.class);
        MappingIterator<Integer> iterator = reader.readValues(jsonParser);
        List<Integer> values = new ArrayList<>();
        assertEquals(iterator.readAll(values).size(), 0);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), Integer.valueOf(0));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testIterationWithUpdateValue() throws Exception {
        JsonFactory jsonFactory = new JsonFactoryBuilder().build();
        JsonParser jsonParser = jsonFactory.createParser("[1,2,3]");
        ObjectReader reader = MAPPER.readerForValueType(Integer[].class);
        MappingIterator<Integer[]> iterator = reader.readValues(jsonParser);
        Integer[] values = new Integer[3];
        values[0] = 4;
        assertEquals(iterator.readAll(values).length, 3);
        assertEquals(iterator.nextValue()[0], Integer.valueOf(4));
        assertEquals(iterator.nextValue()[0], Integer.valueOf(1));
        assertEquals(iterator.nextValue()[0], Integer.valueOf(2));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testIterationWithMappingException() throws Exception {
        JsonFactory jsonFactory = new JsonFactoryBuilder().build();
        JsonParser jsonParser = jsonFactory.createParser("{\"value\":[1]}");
        ObjectReader reader = MAPPER.readerForArrayOf(Integer.class);
        MappingIterator<Integer> iterator = reader.readValues(jsonParser);
        List<Integer> values = new ArrayList<>();
        assertEquals(iterator.readAll(values).size(), 0);
        assertTrue(iterator.hasNext());
        iterator.next();
        // Trigger JsonMappingException
        iterator.next();
    }

    @Test(expectedExceptions = IOException.class)
    public void testIterationWithIOException() throws Exception {
        JsonFactory jsonFactory = new JsonFactoryBuilder().build();
        JsonParser jsonParser = jsonFactory.createParser("{\"value\":[1]}");
        ObjectReader reader = MAPPER.readerForArrayOf(Integer.class);
        MappingIterator<Integer> iterator = reader.readValues(jsonParser);
        List<Integer> values = new ArrayList<>();
        assertEquals(iterator.readAll(values).size(), 0);
        assertTrue(iterator.hasNext());
        iterator.next();
        // Trigger IOException
        iterator.next();
        iterator.next();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        JsonFactory jsonFactory = new JsonFactoryBuilder().build();
        JsonParser jsonParser = jsonFactory.createParser("[1,2,3]");
        ObjectReader reader = MAPPER.readerForArrayOf(Integer.class);
        MappingIterator<Integer> iterator = reader.readValues(jsonParser);
        iterator.remove();
    }
}