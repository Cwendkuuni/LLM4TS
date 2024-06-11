package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.fasterxml.jackson.databind.JsonMapper.DefaultTyping;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertInstanceOf;
import static org.testng.Assert.assertNull;

@Test
public class StringCollectionDeserializerTest extends BaseMapTest {

    @Test
    public void testEmptyCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), null, null);

        JsonParser parser = jsonFactory.createParser("[ ]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(result, new ArrayList<>());
    }

    @Test
    public void testSingleElementCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), valueInstantiator, null, null, null, null);

        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test");
        when(valueInstantiator.createUsingDelegate(any(DeserializationContext.class), any(JsonDeserializer.class))).thenReturn(mock(Object.class));

        JsonParser parser = jsonFactory.createParser("[\"test\"]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(result, new ArrayList<>());
        assertEquals(result.get(0), "test");
    }

    @Test
    public void testNullCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), null, null);

        JsonParser parser = jsonFactory.createParser("null");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testNullValueCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), null, null);

        JsonParser parser = jsonFactory.createParser("[null]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(result, new ArrayList<>());
    }

    @Test
    public void testEmptyStringArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), null, null);

        JsonParser parser = jsonFactory.createParser("[]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(result, new ArrayList<>());
    }

    @Test
    public void testStringArrayWithSingleElement() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), valueDeserializer, null, null, null, null);

        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test");

        JsonParser parser = jsonFactory.createParser("[\"test\"]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(result, new ArrayList<>());
        assertEquals(result.get(0), "test");
    }

    @Test
    public void testStringArrayWithMultipleElements() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), valueDeserializer, null, null, null, null);

        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test1");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test2");

        JsonParser parser = jsonFactory.createParser("[\"test1\", \"test2\"]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(result, new ArrayList<>());
        assertEquals(result.get(0), "test1");
        assertEquals(result.get(1), "test2");
    }

    @Test
    public void testStringArrayWithEmptyElement() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class), valueDeserializer, null, null, null, null);

        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test1");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("");

        JsonParser parser = jsonFactory.createParser("[\"test1\", \"\"]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(result, new ArrayList<>());
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), "test1");
        assertEquals(result.get(1), "");
    }
}