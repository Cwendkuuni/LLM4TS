package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private TypeFactory typeFactory;
    private TypeDeserializer typeDeserializer;
    private TypeSerializer typeSerializer;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        JsonFactory jsonFactory = new JsonFactoryBuilder().build();
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setFactory(jsonFactory);
        typeFactory = mapper.getTypeFactory();
        typeDeserializer = mapper.getTypeDeserializer();
        typeSerializer = mapper.getTypeSerializer();
        reader = mapper.readerForListOf(String.class);
    }

    @Test
    public void testBasicUsage() throws IOException {
        String json = "[\"abc\", \"def\", \"ghi\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        List<String> result = reader.readValues(jsonParser);

        MappingIterator<String> mappingIterator = result.iterator();
        assertTrue(mappingIterator.hasNext());
        assertEquals("abc", mappingIterator.next());
        assertTrue(mappingIterator.hasNext());
        assertEquals("def", mappingIterator.next());
        assertTrue(mappingIterator.hasNext());
        assertEquals("ghi", mappingIterator.next());
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testIOException() throws IOException {
        String json = "{\"error\":\"Invalid JSON\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        List<String> result = reader.readValues(jsonParser);

        MappingIterator<String> mappingIterator = result.iterator();
        assertFalse(mappingIterator.hasNext());
        mappingIterator.close();

        JsonMappingException mappingException = expectThrows(JsonMappingException.class, () -> mappingIterator.next());
        String message = mappingException.getMessage();
        assertTrue(message.contains("Invalid JSON"));
    }

    @Test
    public void testJsonMappingException() throws IOException {
        String json = "[1, null, 3, [\"abc\", null]]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        List<Number> result = reader.readValues(jsonParser);

        MappingIterator<Number> mappingIterator = result.iterator();
        assertTrue(mappingIterator.hasNext());
        assertEquals(1, mappingIterator.next().intValue());
        assertTrue(mappingIterator.hasNext());
        assertNull(mappingIterator.next());
        assertTrue(mappingIterator.hasNext());
        assertEquals(3, mappingIterator.next().intValue());
        assertTrue(mappingIterator.hasNext());

        JsonMappingException mappingException = expectThrows(JsonMappingException.class, () -> mappingIterator.next());
        String message = mappingException.getMessage();
        assertTrue(message.contains("Cannot deserialize value of type [simple type, class java.util.ArrayList] from JsonString \"[\"abc\", null]\""));
    }

    @Test
    public void testCloseParser() throws IOException {
        String json = "[\"abc\", \"def\", \"ghi\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        List<String> result = reader.readValues(jsonParser);

        MappingIterator<String> mappingIterator = result.iterator();
        assertTrue(mappingIterator.hasNext());
        assertEquals("abc", mappingIterator.next());

        mappingIterator.close();
        assertFalse(mappingIterator.hasNext());

        jsonParser.close();
    }

    @Test
    public void testReadAll() throws IOException {
        String json = "[\"abc\", \"def\", \"ghi\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        List<String> result = reader.readValues(jsonParser);

        MappingIterator<String> mappingIterator = result.iterator();
        List<String> all = mappingIterator.readAll();

        assertEquals(Arrays.asList("abc", "def", "ghi"), all);
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        String json = "[\"abc\", \"def\", \"ghi\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);

        List<String> expected = new ArrayList<>();
        expected.add("abc");
        expected.add("def");
        expected.add("ghi");

        MappingIterator<String> mappingIterator = reader.iterator(jsonParser);
        mappingIterator.readAll(expected);

        assertEquals(expected, expected);
    }

    @Test
    public void testGetParser() {
        JsonParser jsonParser = mapper.getFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
        MappingIterator<String> mappingIterator = reader.iterator(jsonParser);

        JsonParser parser = mappingIterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParserSchema() {
        JsonParser jsonParser = mapper.getFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
        MappingIterator<String> mappingIterator = reader.iterator(jsonParser);

        FormatSchema schema = mappingIterator.getParserSchema();
        assertNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        String json = "[\"abc\", \"def\", \"ghi\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        MappingIterator<String> mappingIterator = reader.iterator(jsonParser);

        JsonLocation currentLocation = mappingIterator.getCurrentLocation();
        assertNotNull(currentLocation);
        assertEquals(jsonParser.getCurrentLocation(), currentLocation);
    }
}