package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.core.JsonToken.*;

/**
 * Tests for {@link StringCollectionDeserializer}.
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testEmptyCollection() throws Exception {
        String json = "[]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleValueCollection() throws Exception {
        String json = "[\"Hello\"]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(Arrays.asList("Hello"), result);
    }

    @Test
    public void testCollectionWithMultiValue() throws Exception {
        String json = "[\"Hello\", \"World\"]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(Arrays.asList("Hello", "World"), result);
    }

    @Test
    public void testCollectionWithNullValue() throws Exception {
        String json = "[null, \"World\"]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(Arrays.asList(null, "World"), result);
    }

    @Test
    public void testCollectionWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = new MyStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, null, null, null) {
            @Override
            protected JsonDeserializer<String> _valueDeserializer = stringDeserializer;
        };

        String json = "[\"Hello\", \"World\"]";
        Collection<String> result = mapper.readValue(json, deserializer.getClass());
        Assert.assertEquals(Arrays.asList("Hello", "World"), result);
    }

    @Test
    public void testCollectionWithEmptyArray() throws Exception {
        String json = "[[]]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCollectionWithSingleNullValueInArray() throws Exception {
        String json = "[null]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testCollectionWithEmptyArrayInArray() throws Exception {
        String json = "[[], \"World\"]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(Arrays.asList(Arrays.asList(), "World"), result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws Exception {
        String json = "{\"invalid_json\"}";
        mapper.readValue(json, StringCollectionDeserializer.class);
    }

    @Test(expected = IOException.class)
    public void testUnexpectedToken() throws Exception {
        String json = "{\"key\": \"value\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        mapper.readValue(parser, StringCollectionDeserializer.class);
    }

    @Test
    public void testDelegateDeserializer() throws Exception {
        JsonDeserializer<List<String>> delegateDeserializer = new DefaultListDeserializer(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, delegateDeserializer, null, null, null);

        String json = "[\"Hello\", \"World\"]";
        Collection<String> result = mapper.readValue(json, deserializer.getClass());
        Assert.assertEquals(Arrays.asList("Hello", "World"), result);
    }

    @Test
    public void testValueInstantiator() throws Exception {
        JsonDeserializer<String> stringDeserializer = new MyStringDeserializer();
        ValueInstantiator instantiator = new MyValueInstantiator(String.class, mapper);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, instantiator, null, null, null, null);

        String json = "[\"Hello\", \"World\"]";
        Collection<String> result = mapper.readValue(json, deserializer.getClass());
        Assert.assertEquals(Arrays.asList("Hello", "World"), result);
    }

    // Add any other test cases you would like to see here
    // For example, testing edge cases with null deserializer or
    // null value deserializer.

    private static class MyStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return p.getValueAsString();
        }
    }

    private static class MyValueInstantiator implements ValueInstantiator {
        private final Class<String> type;
        private final ObjectMapper mapper;

        public MyValueInstantiator(Class<String> type, ObjectMapper mapper) {
            this.type = type;
            this.mapper = mapper;
        }

        @Override
        public Object createInstance(DeserializationContext ctxt) throws IOException {
            return mapper.readValue(ctxt.getParser(), type);
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt)
                throws IOException {
            return mapper.readValue(ctxt.getParser(), type);
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, JsonDeserializer<?> delegate)
                throws IOException {
            return delegate.deserialize(ctxt.getParser(), ctxt);
        }
    }
}