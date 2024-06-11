package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonFactory jsonFactory = new JsonFactoryBuilder().build();
    private static final TypeFactory typeFactory = mapper.getTypeFactory();

    private ObjectReader reader;
    private MappingIterator<String> iterator;
    private String[] inputValues = {"one", "two", "three"};

    @Before
    public void setUp() throws Exception {
        reader = mapper.reader(typeFactory.constructCollectionType(List.class, String.class));

        JsonParser inputParser = jsonFactory.createParser(getClass().getResourceAsStream("/mapping-iterator.json"));
        iterator = reader.readValues(String.class, inputParser);
    }

    @After
    public void tearDown() throws Exception {
        if (iterator != null) {
            iterator.close();
        }
    }

    @Test
    public void testHasNextAndNextWithNoValues() throws Exception {
        iterator = reader.readValues(String.class, JsonParser.emptyParser());

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextAndNextWithValues() throws Exception {
        while (iterator.hasNext()) {
            String value = iterator.next();
            assertEquals(value, inputValues[0]);
            inputValues = new String[]{inputValues[1], inputValues[2]};
        }

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        while (iterator.hasNext()) {
            iterator.next();
        }

        iterator.remove();
    }

    @Test
    public void testClose() throws Exception {
        while (iterator.hasNext()) {
            iterator.next();
        }

        iterator.close();

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextValueWithNoValues() throws Exception {
        iterator = reader.readValues(String.class, JsonParser.emptyParser());

        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWithValues() throws Exception {
        while (iterator.hasNextValue()) {
            String value = iterator.nextValue();
            assertEquals(value, inputValues[0]);
            inputValues = new String[]{inputValues[1], inputValues[2]};
        }

        assertFalse(iterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws Exception {
        while (iterator.hasNextValue()) {
            iterator.nextValue();
        }

        iterator.nextValue();
    }

    @Test
    public void testReadAll() throws Exception {
        List<String> result = iterator.readAll();

        for (int i = 0; i < inputValues.length; i++) {
            assertEquals(inputValues[i], result.get(i));
        }
    }

    @Test
    public void testReadAllCollection() throws Exception {
        ArrayList<String> result = new ArrayList<>();

        iterator.readAll(result);

        for (int i = 0; i < inputValues.length; i++) {
            assertEquals(inputValues[i], result.get(i));
        }
    }

    @Test
    public void testGetParser() throws Exception {
        JsonParser parser = jsonFactory.createParser(getClass().getResourceAsStream("/mapping-iterator.json"));
        MappingIterator<String> iterator = reader.readValues(String.class, parser);

        JsonParser resultParser = iterator.getParser();
        assertSame(parser, resultParser);
    }

    @Test
    public void testGetParserSchema() throws Exception {
        JsonParser parser = jsonFactory.createParser(getClass().getResourceAsStream("/mapping-iterator.json"));
        MappingIterator<String> iterator = reader.readValues(String.class, parser);

        FormatSchema schema = iterator.getParserSchema();
        assertNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        JsonParser parser = jsonFactory.createParser(getClass().getResourceAsStream("/mapping-iterator.json"));
        MappingIterator<String> iterator = reader.readValues(String.class, parser);

        JsonLocation location = iterator.getCurrentLocation();
        assertNull(location);
    }

    @Test(expected = JsonMappingException.class)
    public void testMappingException() throws Exception {
        StdDeserializer deserializer = new StdDeserializer();
        JsonParser parser = jsonFactory.createParser(getClass().getResourceAsStream("/mapping-iterator-mapping-exception.json"));
        MappingIterator<String> iterator = reader.readValues(String.class, parser, deserializer);

        iterator.nextValue();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws Exception {
        JsonParser parser = jsonFactory.createParser(getClass().getResourceAsStream("/mapping-iterator-io-exception.json"));
        MappingIterator<String> iterator = reader.readValues(String.class, parser);

        iterator.nextValue();
    }
}