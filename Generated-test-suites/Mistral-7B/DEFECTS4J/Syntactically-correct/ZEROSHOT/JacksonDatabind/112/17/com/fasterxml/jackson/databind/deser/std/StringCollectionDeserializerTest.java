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
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.core.JsonToken.*;

/**
 * Test file for StringCollectionDeserializer class
 */
@JacksonStdImpl
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private StringCollectionDeserializer stringCollectionDeserializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        stringCollectionDeserializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, null, null, null, null);
    }

    @Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        Collection<String> collection = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(0, collection.size());
    }

    @Test
    public void testStringCollectionDeserializationWithNullValueProvider() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[null]");
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext context) {
                return "nullValue";
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, null, null, nullValueProvider, null);
        Collection<String> collection = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(1, collection.size());
        Assert.assertEquals("nullValue", collection.get(0));
    }

    @Test
    public void testStringCollectionDeserializationWithValueDeserializer() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"Hello World\"]");
        ContextualDeserializer<Object> contextualDeserializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, null, contextualDeserializer, null, null);
        Collection<String> collection = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(1, collection.size());
        Assert.assertEquals("Hello World", collection.get(0));
    }

    @Test
    public void testStringCollectionDeserializationWithDelegateDeserializer() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"name\": \"Hello World\"}]");
        ContextualDeserializer<Object> contextualDeserializer = new StdDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonNode node = p.readValueAsTree();
                return node.get("name").asText();
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, contextualDeserializer, null, null, null);
        Collection<String> collection = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(1, collection.size());
        Assert.assertEquals("Hello World", collection.get(0));
    }

    @Test
    public void testStringCollectionDeserializationWithNullValues() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[null, \"Hello World\", null]");
        Collection<String> collection = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(3, collection.size());
        Assert.assertNull(collection.get(0));
        Assert.assertEquals("Hello World", collection.get(1));
        Assert.assertNull(collection.get(2));
    }

    @Test
    public void testStringCollectionDeserializationWithEmptyValue() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"\", \"Hello World\"]");
        Collection<String> collection = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(2, collection.size());
        Assert.assertEquals("", collection.get(0));
        Assert.assertEquals("Hello World", collection.get(1));
    }

    @Test
    public void testStringCollectionDeserializationWithValueAndArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"Hello World\", [\"Apple\", \"Banana\"]]");
        Collection<String> collection = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(3, collection.size());
        Assert.assertEquals("Hello World", collection.get(0));
        List<String> subList = (List<String>) collection.subList(1, collection.size());
        Assert.assertEquals(2, subList.size());
        Assert.assertEquals("Apple", subList.get(0));
        Assert.assertEquals("Banana", subList.get(1));
    }

    @Test(expected = IOException.class)
    public void testStringCollectionDeserializationWithInvalidJson() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("invalidJson");
        stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializationWithInvalidJsonWithPath() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("invalidJson");
        jsonParser.setCurrentLocation(new TextNode("testPath"));
        stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }
}