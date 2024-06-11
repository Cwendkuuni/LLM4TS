package com.example.junit;

import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
        Date date = new Date();
        serializer.serialize(date, jgen, null);
        jgen.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(new ByteArrayInputStream(jgen.getOutputBytes()));
        String expected = "\"\""; // Quotes are used for JSON representation of Date objects
        String actual = jsonNode.get(0).asText();
        assertEquals(expected, actual);
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeNull() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(null, jgen, null);
    }

    @Test
    public void testSerializeNonStringNonDate() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
        int value = 123;
        serializer.serialize(value, jgen, null);
        jgen.close();

        String actual = jgen.getOutputStr();
        String expected = "123";
        assertEquals(expected, actual);
    }

    // Test cases for other methods can be added here
}