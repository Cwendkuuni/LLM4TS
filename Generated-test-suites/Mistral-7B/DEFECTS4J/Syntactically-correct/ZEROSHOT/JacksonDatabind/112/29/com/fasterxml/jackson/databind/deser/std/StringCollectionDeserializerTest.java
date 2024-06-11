package com.yourpackage.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test class for {@link StringCollectionDeserializer}
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private StringCollectionDeserializer stringCollectionDeserializer;
    private JsonDeserializer<String> stringDeserializer;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        stringCollectionDeserializer = new StringCollectionDeserializer(objectMapper.getTypeFactory()
                .constructCollectionType(List.class, String.class), null, null, null, null, null);
        stringDeserializer = mock(JsonDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        when(valueInstantiator.createUsingDefault(Mockito.any(DeserializationContext.class)))
                .thenReturn(new ArrayList<>());
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeSimpleArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0));
        assertEquals("test2", result.get(1));
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        when(stringDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
                .then((InvocationOnMock invocation) -> invocation.getArgumentAt(0, JsonParser.class).nextTextValue());
        stringCollectionDeserializer = new StringCollectionDeserializer(objectMapper.getTypeFactory()
                .constructCollectionType(List.class, String.class), null, null, stringDeserializer, null, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0));
        assertEquals("test2", result.get(1));
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws IOException {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(Mockito.any(DeserializationContext.class)))
                .thenReturn("nullValue");
        stringCollectionDeserializer = new StringCollectionDeserializer(objectMapper.getTypeFactory()
                .constructCollectionType(List.class, String.class), null, null, null, nullValueProvider, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[null]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(1, result.size());
        assertEquals("nullValue", result.get(0));
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(1, result.size());
        assertEquals("", result.get(0));
    }

    @Test
    public void testDeserializeWithValueInstantiator() throws IOException {
        when(valueInstantiator.createUsingDelegate(Mockito.any(DeserializationContext.class), Mockito.any(Object.class)))
                .thenReturn(new ArrayList<>());
        stringCollectionDeserializer = new StringCollectionDeserializer(objectMapper.getTypeFactory()
                .constructCollectionType(List.class, String.class), valueInstantiator, null, null, null, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0));
        assertEquals("test2", result.get(1));
    }

    @Test
    public void testDeserializeWithValueInstantiatorAndDelegateDeserializer() throws IOException {
        when(valueInstantiator.createUsingDelegate(Mockito.any(DeserializationContext.class), Mockito.any(Object.class)))
                .thenReturn(new ArrayList<>());
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        when(delegateDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
                .thenReturn(new ObjectNode(objectMapper.getFactory()));
        stringCollectionDeserializer = new StringCollectionDeserializer(objectMapper.getTypeFactory()
                .constructCollectionType(List.class, String.class), valueInstantiator, delegateDeserializer, null, null, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0));
        assertEquals("test2", result.get(1));
    }

    @Test
    public void testDeserializeWithCustomDeserializerAndDelegateDeserializer() throws IOException {
        when(stringDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
                .then((InvocationOnMock invocation) -> invocation.getArgumentAt(0, JsonParser.class).nextTextValue());
        when(valueInstantiator.createUsingDelegate(Mockito.any(DeserializationContext.class), Mockito.any(Object.class)))
                .thenReturn(new ArrayList<>());
        when(delegateDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
                .thenReturn(new ObjectNode(objectMapper.getFactory()));
        stringCollectionDeserializer = new StringCollectionDeserializer(objectMapper.getTypeFactory()
                .constructCollectionType(List.class, String.class), null, null, stringDeserializer, valueInstantiator, delegateDeserializer);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0));
        assertEquals("test2", result.get(1));

        ObjectNode objectNode = (ObjectNode) result.get(1);
        assertEquals("test2", objectNode.getTextValue());
    }

    @Test
    public void testIsCachable() {
        assertEquals(false, stringCollectionDeserializer.isCachable());
    }
}