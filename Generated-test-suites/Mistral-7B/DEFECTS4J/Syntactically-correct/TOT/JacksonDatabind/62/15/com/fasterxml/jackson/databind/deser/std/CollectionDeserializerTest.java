package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ListDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testEmptyArrayDeserialization() throws JsonProcessingException, IOException {
        String json = "[]";
        Collection<Object> expected = new ArrayList<>();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collections.singletonList(Object.class), JsonToken.START_ARRAY);
        Collection<Object> result = deserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSingleObjectDeserialization() throws JsonProcessingException, IOException {
        String json = "[1]";
        Collection<Object> expected = new ArrayList<>();
        expected.add(1);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collections.singletonList(Object.class), JsonToken.START_ARRAY);
        Collection<Object> result = deserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSingleObjectDeserializationWithTypeDeserializer() throws JsonProcessingException, IOException {
        String json = "[\"string\"]";
        Collection<Object> expected = new ArrayList<>();
        expected.add("string");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collections.singletonList(String.class), JsonToken.START_ARRAY);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(String.class);
        Collection<Object> result = deserializer.deserializeWithType(mapper.readerForRawValue(json), null, typeDeserializer);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEmptyMapDeserialization() throws JsonProcessingException, IOException {
        String json = "{}";
        HashMap<String, Object> expected = new HashMap<>();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Map.class, JsonToken.START_OBJECT);
        Collection<Object> result = deserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testListDeserialization() throws JsonProcessingException, IOException {
        String json = "[1, 2, 3]";
        List<Object> expected = Arrays.asList(1, 2, 3);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        Collection<Object> result = deserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testListDeserializationWithTypeDeserializer() throws JsonProcessingException, IOException {
        String json = "[1, \"string\", 3.0]";
        List<Object> expected = Arrays.asList(1, "string", 3.0);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        Collection<Object> result = deserializer.deserializeWithType(mapper.readerForRawValue(json), null, mapper.getTypeFactory().constructTypeDeserializer(List.class));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testObjectDeserializationWithJsonDeserializer() throws JsonProcessingException, IOException {
        String json = "[{\"name\": \"John\", \"age\": 30}]";
        User expected = new User();
        expected.setName("John");
        expected.setAge(30);
        ObjectIdReader objectIdReader = mapper.getFactory().getObjectIdReader();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(User.class, JsonToken.START_ARRAY);
        deserializer.setObjectIdReader(objectIdReader);
        Collection<Object> result = deserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result.iterator().next());
    }

    @Test
    public void testObjectDeserializationWithTypeDeserializer() throws JsonProcessingException, IOException {
        String json = "[{\"@type\": \"com.example.User\", \"name\": \"John\", \"age\": 30}]";
        User expected = new User();
        expected.setName("John");
        expected.setAge(30);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(User.class, JsonToken.START_ARRAY);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(User.class);
        deserializer.setTypeDeserializer(typeDeserializer);
        Collection<Object> result = deserializer.deserializeWithType(mapper.readerForRawValue(json), null, typeDeserializer);
        Assert.assertEquals(expected, result.iterator().next());
    }

    @Test
    public void testInvalidJsonDeserialization() throws JsonProcessingException, IOException {
        String json = "[]]"; // malformed JSON
        Collection<Object> result;
        try {
            CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
            result = deserializer.deserialize(mapper.readerForRawValue(json), null);
        } catch (JsonProcessingException e) {
            result = null;
        }
        Assert.assertNull(result);
    }

    @Test
    public void testPolymorphicDeserialization() throws JsonProcessingException, IOException {
        String json = "[1, \"string\", 3.0]";
        Collection<Object> expected = Arrays.asList(1, "string", 3.0);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(List.class);
        Collection<Object> result = deserializer.deserializeWithType(mapper.readerForRawValue(json), null, typeDeserializer);
        Assert.assertEquals(expected, result);

        ListDeserializer listDeserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        result = listDeserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPolymorphicDeserializationWithCustomDeserializer() throws JsonProcessingException, IOException {
        String json = "[1, \"string\", 3.0]";
        Collection<Object> expected = Arrays.asList(1, "string", 3.0);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(List.class);
        deserializer.setContentDeserializer(mapper.findValueDeserializer(Number.class, JsonToken.VALUE_NUMBER));
        Collection<Object> result = deserializer.deserializeWithType(mapper.readerForRawValue(json), null, typeDeserializer);
        Assert.assertEquals(expected, result);

        ListDeserializer listDeserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        listDeserializer.setContentDeserializer(mapper.findValueDeserializer(Number.class, JsonToken.VALUE_NUMBER));
        result = listDeserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPolymorphicDeserializationWithJsonDeserializer() throws JsonProcessingException, IOException {
        String json = "[1, \"string\", 3.0]";
        Collection<Object> expected = Arrays.asList(1, "string", 3.0);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        deserializer.setContentDeserializer(mapper.findValueDeserializer(String.class, JsonToken.VALUE_STRING));
        deserializer.setContentDeserializer(mapper.findValueDeserializer(Number.class, JsonToken.VALUE_NUMBER));
        Collection<Object> result = deserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);

        ListDeserializer listDeserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        listDeserializer.setContentDeserializer(mapper.findValueDeserializer(String.class, JsonToken.VALUE_STRING));
        listDeserializer.setContentDeserializer(mapper.findValueDeserializer(Number.class, JsonToken.VALUE_NUMBER));
        result = listDeserializer.deserialize(mapper.readerForRawValue(json), null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPolymorphicDeserializationWithTypeDeserializer() throws JsonProcessingException, IOException {
        String json = "[1, \"string\", 3.0]";
        Collection<Object> expected = Arrays.asList(1, "string", 3.0);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(List.class);
        deserializer.setContentDeserializer(mapper.findValueDeserializer(Number.class, JsonToken.VALUE_NUMBER));
        deserializer.setTypeDeserializer(mapper.getTypeFactory().constructTypeDeserializer(List.class));
        Collection<Object> result = deserializer.deserializeWithType(mapper.readerForRawValue(json), null, typeDeserializer);
        Assert.assertEquals(expected, result);

        ListDeserializer listDeserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        listDeserializer.setContentDeserializer(mapper.findValueDeserializer(Number.class, JsonToken.VALUE_NUMBER));
        listDeserializer.setTypeDeserializer(mapper.getTypeFactory().constructTypeDeserializer(List.class));
        result = listDeserializer.deserializeWithType(mapper.readerForRawValue(json), null, typeDeserializer);
        Assert.assertEquals(expected, result);
    }
}