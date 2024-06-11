package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;

    @BeforeMethod
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectReader = objectMapper.getReader();
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jsonParser = createParserFor("[]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonParser jsonParser = createParserFor("null");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testDeserializeSingleElementArray() throws IOException {
        JsonParser jsonParser = createParserFor("[1]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), 1);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        JsonParser jsonParser = createParserFor("[]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>(), Collections.emptyList());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testDeserializeSingleElementString() throws IOException {
        JsonParser jsonParser = createParserFor("[\"1\"]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>(), Collections.singletonList(String.class));
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), "1");
    }

    @Test
    public void testDeserializeMultipleElementsArray() throws IOException {
        JsonParser jsonParser = createParserFor("[1, 2, 3]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>());
        Assert.assertEquals(result.size(), 3);
        Assert.assertEquals(result.get(0), 1);
        Assert.assertEquals(result.get(1), 2);
        Assert.assertEquals(result.get(2), 3);
    }

    @Test
    public void testDeserializeMultipleElementsString() throws IOException {
        JsonParser jsonParser = createParserFor("[\"1\", \"2\", \"3\"]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>(), Arrays.asList(String.class));
        Assert.assertEquals(result.size(), 3);
        Assert.assertEquals(result.get(0), "1");
        Assert.assertEquals(result.get(1), "2");
        Assert.assertEquals(result.get(2), "3");
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        TypeDeserializer typeDeserializer = new StdSerializer<List<String>>(List.class) {
            @Override
            public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return ctxt.readValueAs(ArrayList.class);
            }
        };

        JsonParser jsonParser = createParserFor("[\"1\", \"2\", \"3\"]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), typeDeserializer);
        Assert.assertTrue(result instanceof List);
        Assert.assertEquals(((List) result).size(), 3);
        Assert.assertEquals(((List) result).get(0), "1");
        Assert.assertEquals(((List) result).get(1), "2");
        Assert.assertEquals(((List) result).get(2), "3");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeInvalidArray() throws IOException {
        JsonParser jsonParser = createParserFor("{1, 2}");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>());
        // should throw JsonMappingException
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeInvalidTypeArray() throws IOException {
        JsonParser jsonParser = createParserFor("[true, false]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>());
        // should throw JsonMappingException
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeInvalidStringArray() throws IOException {
        JsonParser jsonParser = createParserFor("[ab, cd]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>());
        // should throw JsonMappingException
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeInvalidStringElements() throws IOException {
        JsonParser jsonParser = createParserFor("[\"ab\", \"cd\"]");
        Collection<Object> result = CollectionDeserializer.deserialize(jsonParser, new DeserializationContext(objectMapper), new ArrayList<>(), Collections.singletonList(String.class));
        // should throw JsonMappingException
    }

    private JsonParser createParserFor(String jsonString) throws IOException {
        ObjectNode node = objectMapper.readTree(jsonString);
        JsonParser jsonParser = JsonFactory.instance.createParser(node.traverse());
        jsonParser.nextToken();
        return jsonParser;
    }
}