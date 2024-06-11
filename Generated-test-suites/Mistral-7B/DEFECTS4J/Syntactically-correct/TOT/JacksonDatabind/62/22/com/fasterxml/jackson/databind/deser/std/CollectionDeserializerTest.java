package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.testng.annotations.*;

import java.io.*;
import java.util.*;

import static org.testng.Assert.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;
import static com.fasterxml.jackson.databind.JsonMapping.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionDeserializerTest {

    private ObjectMapper mapper;

    @BeforeClass
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(Feature.FAIL_ON_EMPTY_CREATOR_ARRAYS, false);
    }

    @Test
    public void testEmptyCollection() throws IOException {
        JsonNode json = mapper.readTree("[]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(result.size(), 0);
    }

    @Test
    public void testCollectionWithStrings() throws IOException {
        JsonNode json = mapper.readTree("[\"foo\", \"bar\", \"baz\"]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                new StringDeserializer(), null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(result.size(), 3);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).toString(), String.valueOf(i + 1));
        }
    }

    @Test
    public void testCollectionWithObjects() throws IOException {
        JsonNode json = mapper.readTree("[1, 2, 3]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                new NumberDeserializer(NumberFactory.instance, null), null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(result.size(), 3);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getClass(), Number.class);
            assertEquals(((Number) result.get(i)).intValue(), i);
        }
    }

    @Test
    public void testCollectionWithEmptyString() throws IOException {
        JsonNode json = mapper.readTree("[\"\"]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).toString(), "");
    }

    @Test
    public void testCollectionWithNull() throws IOException {
        JsonNode json = mapper.readTree("[null]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), null);
    }

    @Test
    public void testCollectionWithNestedObject() throws IOException {
        JsonNode json = mapper.readTree("[{\"foo\": 1, \"bar\": \"test\"}]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Map.class),
                new MapDeserializer(new HashMapSerializer(true), null), null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(result.size(), 1);
        Object obj = result.get(0);
        assertEquals(obj.getClass(), Map.class);
        Map<?, ?> map = (Map<?, ?>) obj;
        assertEquals(map.size(), 2);
        assertEquals(map.get("foo"), 1);
        assertEquals(map.get("bar"), "test");
    }

    @Test
    public void testCollectionWithNestedArray() throws IOException {
        JsonNode json = mapper.readTree("[1, [2, 3]]");;
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Object.class),
                null, null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getClass(), Number.class);
        assertEquals(((Number) result.get(0)).intValue(), 1);
        assertEquals(result.get(1).getClass(), List.class);
        List<?> nestedList = (List<?>) result.get(1);
        assertEquals(nestedList.size(), 2);
        for (int i = 0; i < nestedList.size(); i++) {
            assertEquals(nestedList.get(i).getClass(), Number.class);
            assertEquals(((Number) nestedList.get(i)).intValue(), i + 1);
        }
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCollectionWithInvalidJson() throws IOException {
        JsonNode json = mapper.readTree("{foo: [1, 2, 3]}");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCollectionWithNullContentType() throws IOException {
        JsonNode json = mapper.readTree("[null]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                null, null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCollectionWithNullValueDeserializer() throws IOException {
        JsonNode json = mapper.readTree("[null]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCollectionWithNullValueTypeDeserializer() throws IOException {
        JsonNode json = mapper.readTree("[1]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCollectionWithNullValueInstantiator() throws IOException {
        JsonNode json = mapper.readTree("[1]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCollectionWithNullDelegateDeserializer() throws IOException {
        JsonNode json = mapper.readTree("[1]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expectedExceptions = IOException.class)
    public void testCollectionWithNullReader() throws IOException {
        JsonNode json = mapper.readTree("[1]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null);
        deserializer.deserialize(null, null);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCollectionWithUnwrappableSingleValue() throws IOException {
        JsonNode json = mapper.readTree("[\"foo\"]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class),
                new StringDeserializer(), null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

}