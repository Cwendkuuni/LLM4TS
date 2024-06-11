package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(JUnit4.class)
public class ObjectResultTest {

    private ObjectResult or;

    @Before
    public void setUp() {
        or = new ObjectResult("schema", "name", "type");
        or.setCount(new AtomicLong(10));
        or.setTotal(new AtomicLong(20));
    }

    @Test
    public void testObjectResult() {
        assertNotNull(or);
        assertEquals("schema", or.getSchema());
        assertEquals("name", or.getName());
        assertEquals("type", or.getType());
        assertEquals(10L, or.getCount().get());
        assertEquals(20L, or.getTotal().get());
        assertNull(or.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult result = ObjectResult.of(that);
        assertNotNull(result);
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals(null, result.getType());
        assertEquals(10L, result.getCount().get());
        assertEquals(20L, result.getTotal().get());
        assertNotSame(that, result);
    }

    @Test
    public void testGetSummary() {
        assertEquals("schema.name[type]", or.getSummary());
    }

    @Test
    public void testExportPaths() throws MalformedURLException {
        URL url1 = new URL("http://example.com/1");
        URL url2 = new URL("http://example.com/2");

        List<URL> list = new ArrayList<>();
        list.add(url1);
        list.add(url2);

        or.setExportPaths(list);

        assertEquals(list, or.getExportPaths());
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(or);

        ObjectResult deserialized = mapper.readValue(json, ObjectResult.class);

        assertEquals(or.getSchema(), deserialized.getSchema());
        assertEquals(or.getName(), deserialized.getName());
        assertEquals(or.getType(), deserialized.getType());
        assertEquals(or.getCount().get(), deserialized.getCount().get());
        assertEquals(or.getTotal().get(), deserialized.getTotal().get());
        assertNull(deserialized.getExportPaths());
    }

    @Test
    public void testSerializationWithExportPaths() throws JsonProcessingException, MalformedURLException {
        ObjectResult orWithPaths = new ObjectResult("schema", "name", "type");
        orWithPaths.setCount(new AtomicLong(10));
        orWithPaths.setTotal(new AtomicLong(20));

        URL url1 = new URL("http://example.com/1");
        URL url2 = new URL("http://example.com/2");

        List<URL> list = new ArrayList<>();
        list.add(url1);
        list.add(url2);

        orWithPaths.setExportPaths(list);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(orWithPaths);

        ObjectResult deserialized = mapper.readValue(json, ObjectResult.class);

        assertEquals(orWithPaths.getSchema(), deserialized.getSchema());
        assertEquals(orWithPaths.getName(), deserialized.getName());
        assertEquals(orWithPaths.getType(), deserialized.getType());
        assertEquals(orWithPaths.getCount().get(), deserialized.getCount().get());
        assertEquals(orWithPaths.getTotal().get(), deserialized.getTotal().get());
        assertEquals(orWithPaths.getExportPaths(), deserialized.getExportPaths());
    }

    @Test
    public void testSerializationNull() throws JsonProcessingException {
        or.setSchema(null);
        or.setName(null);
        or.setType(null);
        or.setCount(null);
        or.setTotal(null);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(or);

        ObjectResult deserialized = mapper.readValue(json, ObjectResult.class);

        assertNull(deserialized.getSchema());
        assertNull(deserialized.getName());
        assertNull(deserialized.getType());
        assertNull(deserialized.getCount());
        assertNull(deserialized.getTotal());
    }

    @Test
    public void testDeserializationMissingSchema() throws JsonProcessingException {
        String json = "{\"name\": \"name\", \"type\": \"type\", \"count\": 10, \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () ->
                mapper.readValue(json, ObjectResult.class)
        );

        assertEquals("Failed to deserialize a JSON object into `com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult`: missing property 'schema'",
                exception.getMessage());
    }

    @Test
    public void testDeserializationMissingName() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"type\": \"type\", \"count\": 10, \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () ->
                mapper.readValue(json, ObjectResult.class)
        );

        assertEquals("Failed to deserialize a JSON object into `com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult`: missing property 'name'",
                exception.getMessage());
    }

    @Test
    public void testDeserializationMissingType() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"name\": \"name\", \"count\": 10, \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        ObjectResult deserialized = mapper.readValue(json, ObjectResult.class);

        assertEquals("schema", deserialized.getSchema());
        assertEquals("name", deserialized.getName());
        assertNull(deserialized.getType());
        assertEquals(10L, deserialized.getCount().get());
        assertEquals(20L, deserialized.getTotal().get());
    }

    @Test
    public void testDeserializationMissingCount() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"name\": \"name\", \"type\": \"type\", \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        ObjectResult deserialized = mapper.readValue(json, ObjectResult.class);

        assertEquals("schema", deserialized.getSchema());
        assertEquals("name", deserialized.getName());
        assertEquals("type", deserialized.getType());
        assertNull(deserialized.getCount());
        assertEquals(20L, deserialized.getTotal().get());
    }

    @Test
    public void testDeserializationMissingTotal() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"name\": \"name\", \"type\": \"type\", \"count\": 10}";

        ObjectMapper mapper = new ObjectMapper();
        ObjectResult deserialized = mapper.readValue(json, ObjectResult.class);

        assertEquals("schema", deserialized.getSchema());
        assertEquals("name", deserialized.getName());
        assertEquals("type", deserialized.getType());
        assertEquals(10L, deserialized.getCount().get());
        assertNull(deserialized.getTotal());
    }

    @Test
    public void testDeserializationInvalidSchema() throws JsonProcessingException {
        String json = "{\"schema\": 123, \"name\": \"name\", \"type\": \"type\", \"count\": 10, \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () ->
                mapper.readValue(json, ObjectResult.class)
        );

        assertEquals("Can not deserialize value of type `java.lang.String` from Object value (token `JsonToken` of type `VALUE_NUMBER_INT`)",
                exception.getMessage());
    }

    @Test
    public void testDeserializationInvalidName() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"name\": 123, \"type\": \"type\", \"count\": 10, \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () ->
                mapper.readValue(json, ObjectResult.class)
        );

        assertEquals("Can not deserialize value of type `java.lang.String` from Object value (token `JsonToken` of type `VALUE_NUMBER_INT`)",
                exception.getMessage());
    }

    @Test
    public void testDeserializationInvalidType() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"name\": \"name\", \"type\": 123, \"count\": 10, \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () ->
                mapper.readValue(json, ObjectResult.class)
        );

        assertEquals("Can not deserialize value of type `java.lang.String` from Object value (token `JsonToken` of type `VALUE_NUMBER_INT`)",
                exception.getMessage());
    }

    @Test
    public void testDeserializationInvalidCount() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"name\": \"name\", \"type\": \"type\", \"count\": \"10\", \"total\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () ->
                mapper.readValue(json, ObjectResult.class)
        );

        assertEquals("Can not deserialize value of type `java.lang.Long` from String \"10\": not a valid Long value",
                exception.getMessage());
    }

    @Test
    public void testDeserializationInvalidTotal() throws JsonProcessingException {
        String json = "{\"schema\": \"schema\", \"name\": \"name\", \"type\": \"type\", \"count\": 10, \"total\": \"20\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () ->
                mapper.readValue(json, ObjectResult.class)
        );

        assertEquals("Can not deserialize value of type `java.lang.Long` from String \"20\": not a valid Long value",
                exception.getMessage());
    }

}

