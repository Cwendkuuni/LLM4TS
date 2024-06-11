package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultSerializer() throws JsonMappingException, JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        String value = "Hello World!";
        ObjectNode result = mapper.writeValueAsObject(value, new StdSerializer<String>(String.class), provider);
        assertNotNull(result);
        assertEquals("{\"type\":\"string\"}", result.toString());

        JsonSchema schema = serializer.getSchema(mapper.getSerializerProvider(), String.class);
        assertEquals("string", schema.getSchemaName());
    }

    @Test
    public void testJsonFormatVisitor() throws JsonMappingException, JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        String value = "Hello World!";
        SerializerProvider provider = new DefaultSerializerProvider();

        PropertyWriter<String> writer = mapper.findValueSerializer(String.class);
        BeanPropertyWriter<String> propertyWriter = mapper.writerForValueType(String.class);

        ObjectNode result = mapper.writerWithView(JsonFormatVisitorProvider.Std.class)
                .writeValueAsObject(value, serializer, provider, propertyWriter);

        assertEquals("{\"type\":\"string\"}", result.toString());
        JsonNode schema = serializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().getActiveContext().getJsonFormatVisitorWrapper(), String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testHandledType() throws JsonMappingException, JsonProcessingException {
        StdSerializer<Map<String, Integer>> serializer = new StdSerializer<Map<String, Integer>>(Map.class);

        Map<String, Integer> value = new HashMap<>();
        value.put("key1", 1);
        value.put("key2", 2);

        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNode result = mapper.writeValueAsObject(value, serializer, provider);

        assertEquals("{\"type\":\"object\"}", result.toString());
        assertEquals(2, result.size());

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Map.class);
        JsonNode schemaType = schema.get("type");
        assertEquals("object", schemaType.asText());

        JsonNode schemaProperties = schema.get("properties");
        assertEquals(2, schemaProperties.size());

        JsonNode property1 = schemaProperties.get(0);
        assertEquals("key1", property1.get("propertyName").asText());
        JsonNode propertyType1 = property1.get("type");
        assertEquals("integer", propertyType1.asText());

        JsonNode property2 = schemaProperties.get(1);
        assertEquals("key2", property2.get("propertyName").asText());
        JsonNode propertyType2 = property2.get("type");
        assertEquals("integer", propertyType2.asText());
    }

    @Test
    public void testSerialization() throws JsonMappingException, JsonProcessingException, IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        String value = "Hello World!";
        SerializerProvider provider = new DefaultSerializerProvider();

        JsonFactory jsonFactory = mapper.getFactory();
        JsonGenerator jgen = jsonFactory.createGenerator(new ByteArrayOutputStream(), jsonFactory.getDefaultSerializerProvider());

        serializer.serialize(value, jgen, provider);
        jgen.flush();
        byte[] result = ((ByteArrayOutputStream) jgen.getOutputStream()).toByteArray();

        assertEquals("Hello World!\n", new String(result));
    }

    @Test(expected = JsonMappingException.class)
    public void testExceptionHandling() throws JsonMappingException, JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        String value = null;
        SerializerProvider provider = new DefaultSerializerProvider();

        try {
            serializer.serialize(value, mapper.getFactory().createGenerator(new ByteArrayOutputStream()), provider);
        } catch (IOException e) {
            serializer.wrapAndThrow(provider, e, null, "test");
        }
    }
}