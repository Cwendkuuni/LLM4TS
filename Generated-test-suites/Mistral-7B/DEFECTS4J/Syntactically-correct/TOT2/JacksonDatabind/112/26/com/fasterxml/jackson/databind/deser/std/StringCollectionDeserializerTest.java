package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StringCollectionDeserializerTest {

    private static final ObjectMapper mapper;
    private static final JsonFactory jsonFactory;

    static {
        mapper = new ObjectMapper();
        jsonFactory = mapper.getFactory();
    }

    /**
     * Test for default constructor
     */
    @Test
    public void testDefaultConstructor() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertEquals(Collection.class, deserializer.getInputType());
        assertNull(deserializer._valueDeserializer);
        assertNull(deserializer._valueInstantiator);
        assertNull(deserializer._delegateDeserializer);
    }

    /**
     * Test for constructor with valueDeserializer
     */
    @Test
    public void testConstructorWithValueDeserializer() throws Exception {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, valueDeserializer, valueInstantiator);
        assertEquals(Collection.class, deserializer.getInputType());
        assertEquals(valueDeserializer, deserializer._valueDeserializer);
        assertEquals(valueInstantiator, deserializer._valueInstantiator);
        assertNull(deserializer._delegateDeserializer);
    }

    /**
     * Test for constructor with delegateDeserializer
     */
    @Test
    public void testConstructorWithDelegateDeserializer() throws Exception {
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, delegateDeserializer, null, valueDeserializer, null);
        assertEquals(Collection.class, deserializer.getInputType());
        assertNull(deserializer._valueDeserializer);
        assertEquals(delegateDeserializer, deserializer._delegateDeserializer);
        assertNull(deserializer._valueInstantiator);
    }

    /**
     * Test for deserialize method with null input
     */
    @Test(expectedExceptions = JsonProcessingException.class)
    public void testDeserializeNullInput() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer.deserialize(jsonFactory.createParser(new ByteArrayInputStream(new byte[0])), new TestContext());
    }

    /**
     * Test for deserialize method with empty JSON array
     */
    @Test
    public void testDeserializeEmptyJsonArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("[ ]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertTrue(result.isEmpty());
    }

    /**
     * Test for deserialize method with single JSON string
     */
    @Test
    public void testDeserializeSingleJsonString() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("[\"hello\" ]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(1, result.size());
        assertEquals("hello", result.iterator().next());
    }

    /**
     * Test for deserialize method with multiple JSON strings
     */
    @Test
    public void testDeserializeMultipleJsonStrings() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("[\"hello\", \"world\" ]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("hello", "world")));
    }

    /**
     * Test for deserialize method with null value in JSON array
     */
    @Test
    public void testDeserializeNullValueInJsonArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("[null, \"hello\" ]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("null", "hello")));
    }

    /**
     * Test for deserialize method with JSON array containing different types
     */
    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeJsonArrayContainingDifferentTypes() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("[1, \"hello\" ]");
        deserializer.deserialize(parser, new TestContext());
    }

    /**
     * Test for deserialize method with JSON object instead of array
     */
    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeJsonObjectInsteadOfArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("{\"name\": \"hello\" }");
        deserializer.deserialize(parser, new TestContext());
    }

    /**
     * Test for deserialize method with JSON array containing JSON objects
     */
    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeJsonArrayContainingJsonObjects() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("[{\"name\": \"hello\" }]");
        deserializer.deserialize(parser, new TestContext());
    }

    /**
     * Test for deserialize method with empty JSON object
     */
    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeEmptyJsonObject() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = jsonFactory.createParser("{}");
        deserializer.deserialize(parser, new TestContext());
    }

    /**
     * Test for isCachable method
     */
    @Test
    public void testIsCachable() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertFalse(deserializer.isCachable());
    }

    /**
     * Test for createContextual method with no delegate deserializer
     */
    @Test
    public void testCreateContextualNoDelegateDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<Object> delegateDeserializer = null;
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nuller = mock(NullValueProvider.class);
        Boolean unwrapSingle = null;
        JsonDeserializer<?> result = deserializer.createContextual(context, property);
        assertEquals(deserializer, result);
        verify(context, times(1)).findContextualValueDeserializer(Collection.class, property);
        verify(context, never()).findValueDeserializer(String.class, property);
    }

    /**
     * Test for createContextual method with delegate deserializer
     */
    @Test
    public void testCreateContextualWithDelegateDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nuller = mock(NullValueProvider.class);
        Boolean unwrapSingle = null;
        JsonDeserializer<?> result = deserializer.createContextual(context, property);
        assertNotNull(result);
        assertFalse(result instanceof StringCollectionDeserializer);
        assertEquals(delegateDeserializer, result._delegateDeserializer);
        verify(context, times(1)).findValueDeserializer(String.class, property);
        verify(context, never()).findContentDeserializer(Collection.class, property);
    }

    /**
     * Test for deserializeWithType method
     */
    @Test
    public void testDeserializeWithType() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<Collection<String>> typeDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializerType = mock(TypeDeserializer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(typeDeserializer.deserializeTypedFromArray(parser, context)).thenReturn(new ArrayList<>());
        Collection<String> result = deserializer.deserializeWithType(parser, context, typeDeserializerType);
        assertEquals(new ArrayList<>(), result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findValueDeserializer(Class<?> type, BeanProperty property) {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(Class<?> type, BeanProperty property) {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findContentDeserializer(Class<Collection<?>> collectionType) {
            return null;
        }

        @Override
        public ObjectReader createContextualObjectMapper(DeserializationConfig config) {
            return null;
        }

        @Override
        public ObjectReader createContextualValueFactory(DeserializationConfig config) {
            return null;
        }

        @Override
        public NullValueProvider getNullValueProvider() {
            return null;
        }
    }
}