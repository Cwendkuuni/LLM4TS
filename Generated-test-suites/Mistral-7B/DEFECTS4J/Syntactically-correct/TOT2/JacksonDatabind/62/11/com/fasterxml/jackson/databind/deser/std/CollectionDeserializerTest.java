package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for {@link CollectionDeserializer} class.
 */
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[\"abc\",\"def\",\"ghi\"]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        assertEquals(3, collection.size());
        assertEquals("abc", collection.get(0));
        assertEquals("def", collection.get(1));
        assertEquals("ghi", collection.get(2));
    }

    @Test
    public void testCollectionDeserializationWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(MyClass.class);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(MyClass.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, typeDeserializer, null, null, null);

        String jsonString = "[{\"name\":\"Alice\",\"age\":30},{\"name\":\"Bob\",\"age\":25},{\"name\":\"Charlie\",\"age\":35}]";
        List<MyClass> collection = mapper.readValue(jsonString, javaType);
        assertEquals(3, collection.size());
        assertEquals("Alice", collection.get(0).getName());
        assertEquals(30, collection.get(0).getAge());
        assertEquals("Bob", collection.get(1).getName());
        assertEquals(25, collection.get(1).getAge());
        assertEquals("Charlie", collection.get(2).getName());
        assertEquals(35, collection.get(2).getAge());
    }

    @Test
    public void testCollectionDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        JsonDeserializer<Object> delegateDeserializer = new IntDeserializer();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, delegateDeserializer, null);

        String jsonString = "[1,2,3]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        assertEquals(3, collection.size());
        assertEquals("1", collection.get(0));
        assertEquals("2", collection.get(1));
        assertEquals("3", collection.get(2));
    }

    @Test
    public void testCollectionDeserializationWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[ ]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testCollectionDeserializationWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[null]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        assertTrue(collection.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[abc]"; // missing '[' at the beginning
        List<String> collection = mapper.readValue(jsonString, javaType);
        fail("Expected JsonMappingException but got " + collection.getClass().getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithInvalidType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(HashMap.class, String.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[\"abc\",\"def\",\"ghi\"]";
        List<Map.Entry<String, String>> collection = mapper.readValue(jsonString, javaType);
        fail("Expected JsonMappingException but got " + collection.getClass().getName());
    }

    @Test
    public void testCollectionDeserializationWithEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[ ]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testCollectionDeserializationWithSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[\"abc\"]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        assertEquals(1, collection.size());
        assertEquals("abc", collection.get(0));
    }

    @Test
    public void testCollectionDeserializationWithSingleElementArrayWrappedInArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[[\"abc\"]]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        assertEquals(1, collection.size());
        assertEquals("abc", collection.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithMultipleRoots() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[\"abc\",{\"name\":\"Alice\",\"age\":30}]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        fail("Expected JsonMappingException but got " + collection.getClass().getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithInvalidElementType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null, null, null);

        String jsonString = "[\"abc\",1,2,3]";
        List<Integer> collection = mapper.readValue(jsonString, javaType);
        fail("Expected JsonMappingException but got " + collection.getClass().getName());
    }
}