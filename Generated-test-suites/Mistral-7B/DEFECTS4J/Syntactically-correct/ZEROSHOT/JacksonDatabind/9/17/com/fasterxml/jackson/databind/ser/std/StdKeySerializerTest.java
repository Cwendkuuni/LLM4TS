package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

/**
 * Test class for StdKeySerializer
 */
public class StdKeySerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeDate() throws IOException {
        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, jsonGenerator, new SerializerProvider().builder().build());
        jsonGenerator.flush();
        System.out.flush();

        String actual = System.out.getBytes()[0] > 0 ? new String(System.out.getBytes(), 0, (int) System.out.size()) : "";
        String expected = ISODateFormat.DEFAULT_DATE_FORMAT.format(date);

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeString() throws IOException {
        String key = "test_key";
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(key, jsonGenerator, new SerializerProvider().builder().build());
        jsonGenerator.flush();
        System.out.flush();

        String actual = System.out.getBytes()[0] > 0 ? new String(System.out.getBytes(), 0, (int) System.out.size()) : "";
        String expected = key;

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(new SerializerProvider().builder().build(), StdKeySerializer.class);

        String expected = "string";
        String actual = schema.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = objectMapper.schema().findSchemaForType(String.class);

        serializer.acceptJsonFormatVisitor(visitor, StdKeySerializer.class);

        String expected = "string";
        String actual = visitor.getFormatName();

        assertEquals(expected, actual);
    }
}