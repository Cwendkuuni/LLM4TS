package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

/**
 * Test class for CollectionDeserializer.
 */
@JacksonStdImpl
public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeSingleValue() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("5");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(1, result.size());
        assertEquals((Integer) 5, result.get(0));
    }

    @Test
    public void testDeserializeArrayWithValues() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(3, result.size());
        assertEquals((Integer) 1, result.get(0));
        assertEquals((Integer) 2, result.get(1));
        assertEquals((Integer) 3, result.get(2));
    }

    @Test
    public void testDeserializeArrayWithNullValue() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[null]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    public void testDeserializeArrayWithEmptyString() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, null);
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("5");
        jsonParser.nextToken(); // consume INT token
        collectionDeserializer.deserialize(jsonParser, null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class, String.class);
        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory()
                .constructValueTypeDeserializer(String.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, typeDeserializer, null, null, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"apple\", \"banana\", \"orange\"]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("orange", result.get(2));
    }
}