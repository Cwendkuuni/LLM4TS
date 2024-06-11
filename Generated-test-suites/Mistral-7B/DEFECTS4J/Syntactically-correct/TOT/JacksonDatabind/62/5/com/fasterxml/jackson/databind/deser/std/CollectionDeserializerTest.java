package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CollectionDeserializer} class.
 */
public class CollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test deserialization of empty Array.
     */
    @Test
    public void testEmptyArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("[]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType, JsonToken.START_ARRAY);
        List<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Collections.emptyList(), result);
    }

    /**
     * Test deserialization of single value Array.
     */
    @Test
    public void testSingleValueArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonNode node = mapper.readTree("[1]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType, JsonToken.START_ARRAY);
        List<Integer> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Collections.singletonList(1), result);
    }

    /**
     * Test deserialization of Array with multiple values.
     */
    @Test
    public void testMultipleValuesArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonNode node = mapper.readTree("[1, 2, 3]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType, JsonToken.START_ARRAY);
        List<Integer> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    /**
     * Test deserialization of Array with null value.
     */
    @Test
    public void testNullValueArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("[null]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType, JsonToken.START_ARRAY);
        List<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertNull(result.get(0));
    }

    /**
     * Test deserialization of Array with empty String value.
     */
    @Test
    public void testEmptyStringValueArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("[\"\"]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType, JsonToken.START_ARRAY);
        List<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Collections.singletonList(""), result);
    }

    /**
     * Test deserialization of Array with non-Array value.
     */
    @Test(expected = JsonMappingException.class)
    public void testNonArrayValue() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("{\"foo\":\"bar\"}");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType, JsonToken.START_ARRAY);
        deserializer.deserialize(mapper.getFactory().createParser(node), null);
    }

    /**
     * Test deserialization of Array with unsupported type.
     */
    @Test(expected = JsonMappingException.class)
    public void testUnsupportedType() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(CollectionDeserializer.class);
        JsonNode node = mapper.readTree("[1, 2, 3]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType, JsonToken.START_ARRAY);
        deserializer.deserialize(mapper.getFactory().createParser(node), null);
    }

    /**
     * Test deserialization of Array with circular reference.
     */
    @Test
    public void testCircularReference() throws Exception {
        MapperConfig config = new MapperConfig(mapper.getConfiguration());
        config.setAnnotationIntrospector(new SimpleBeanPropertyDefinitionIntrospector());
        ObjectMapper circularMapper = new ObjectMapper(config);

        List<String> list1 = new ArrayList<>();
        list1.add("list1");
        List<String> list2 = new ArrayList<>();
        list2.add("list2");
        list2.add(list1);

        JsonNode node = mapper.writeValueAsTree(list2);

        CollectionDeserializer deserializer = mapper.findValueDeserializer(List.class, JsonToken.START_ARRAY);
        List<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(list2, result);
        assertTrue(result.get(0) instanceof Referring);
        assertTrue(((Referring) result.get(0)).getReferringObject() instanceof CollectionDeserializer);
    }
}