package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MappingIteratorTest extends TestBase {

    private static final Class<MappingIterator> MAPPING_ITERATOR_CLASS =
            MappingIterator.class;

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;

    @BeforeMethod
    public void setUp() throws Exception {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReader reader = objectMapper.readerForListOf(String.class);
        this.objectReader = objectReader = reader.with(new StdDeserializer.NullValueProvider());
    }

    @Test
    public void testNormalUseCase() throws Exception {
        String json = "[\"Hello\", \"World\"]";
        Iterator<String> iterator = objectReader.iterateThroughValues(json);
        assertEquals("Hello", iterator.next());
        assertEquals("World", iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testEmptyIteration() throws Exception {
        String json = "[]";
        Iterator<String> iterator = objectReader.iterateThroughValues(json);
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testDeserializationException() throws Exception {
        ObjectReader reader = objectMapper.readerForListOf(String.class);
        JsonParser parser = jsonFactory.createParser("[{\"invalid\": true}]");
        reader._context = mock(DeserializationContext.class);

        MappingIterator<String> iterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                parser, objectMapper.getDeserializationContext(), reader.getDeserializer(),
                false, null);

        JsonMappingException mappingException = expectThrows(JsonMappingException.class,
                () -> iterator.nextValue());
        assertEquals("Text 'invalid' contained an empty string (length=0) whereas an array element was expected",
                mappingException.getMessage());

        verify(objectMapper.getDeserializationContext(), times(1)).handleUnexpectedValue(
                mappingException.getOriginalPosition(), mappingException.getCause());

        iterator.close();
    }

    @Test
    public void testIOException() throws Exception {
        ObjectReader reader = objectMapper.readerForListOf(String.class);
        JsonParser parser = jsonFactory.createParser("[\"Hello\"];"); // malformed json
        reader._context = mock(DeserializationContext.class);

        MappingIterator<String> iterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                parser, objectMapper.getDeserializationContext(), reader.getDeserializer(),
                false, null);

        IOException ioException = expectThrows(IOException.class,
                () -> iterator.nextValue());
        assertEquals("Unexpected character (34) : JSON_STRING_UNESCAPED_CONTROL_CHARACTER",
                ioException.getMessage());

        verify(objectMapper.getDeserializationContext(), times(1)).handleUnexpectedValue(
                parser.getCurrentLocation(), ioException);

        iterator.close();
    }

    @Test
    public void testCloseParser() throws Exception {
        String json = "[\"Hello\", \"World\"]";
        ObjectReader reader = objectMapper.readerForListOf(String.class);
        JsonParser parser = jsonFactory.createParser(json);
        reader._context = mock(DeserializationContext.class);

        MappingIterator<String> iterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                parser, objectMapper.getDeserializationContext(), reader.getDeserializer(),
                true, null);

        assertEquals("Hello", iterator.next());
        assertEquals("World", iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testUpdateValue() throws Exception {
        String json = "[{\"name\": \"John\", \"age\": 30}, {\"name\": \"Jane\", \"age\": 25}]";
        ObjectReader reader = objectMapper.readerForListOf(Map.class);
        JsonParser parser = jsonFactory.createParser(json);
        reader._context = mock(DeserializationContext.class);

        MappingIterator<Map<String, Object>> iterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class),
                parser, objectMapper.getDeserializationContext(), reader.getDeserializer(),
                false, new HashMap<>());

        Map<String, Object> map = iterator.nextValue();
        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
        map.put("status", "active");
        assertEquals("active", iterator.nextValue().get("status"));

        iterator.close();
    }
}