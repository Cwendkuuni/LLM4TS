package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[]");
        StringCollectionDeserializer deserializer = mapper.findValue("elements", node, StringCollectionDeserializer.class);
        Assert.assertEquals(Collections.emptyList(), deserializer.getValue());
    }

    @Test
    public void testSingleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"element\"]");
        StringCollectionDeserializer deserializer = mapper.findValue("elements", node, StringCollectionDeserializer.class);
        List<String> result = deserializer.getValue();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("element", result.get(0));
    }

    @Test
    public void testMultipleElementsCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"element1\", \"element2\", \"element3\"]");
        StringCollectionDeserializer deserializer = mapper.findValue("elements", node, StringCollectionDeserializer.class);
        List<String> result = deserializer.getValue();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("element1", result.get(0));
        Assert.assertEquals("element2", result.get(1));
        Assert.assertEquals("element3", result.get(2));
    }

    @Test
    public void testNullValuesCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[null]");
        StringCollectionDeserializer deserializer = mapper.findValue("elements", node, StringCollectionDeserializer.class);
        List<String> result = deserializer.getValue();
        Assert.assertEquals(1, result.size());
        Assert.assertNull(result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": \"element\"}");
        StringCollectionDeserializer deserializer = mapper.findValue("elements", node, StringCollectionDeserializer.class);
        deserializer.deserialize(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerFailure() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[{\"value\": \"invalid\"}]");
        Object valueDeserializer = new InvalidJsonDeserializer(); // Custom deserializer that throws an exception
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null) {
            @Override
            protected JsonDeserializer<Object> getDelegateDeserializer() {
                return (JsonDeserializer<Object>) valueDeserializer;
            }
        };
        deserializer.deserialize(null, null);
    }
}