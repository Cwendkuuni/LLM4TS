package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Class testing the functionality of AnyGetterWriter
 */
public class AnyGetterWriterTest {

    private ObjectMapper objectMapper;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        BeanProperty property = new BeanProperty("anyGetterMap", Map.class, null);
        AnnotatedMember accessor = BeanUtil.findAnnotated(Map.class, "getAnyGetterMap");
        MapSerializer mapSerializer = new StdSerializer<Map<String, Object>>(Map.class, null);
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws JsonProcessingException, JsonMappingException {
        Object testObject = new TestObject();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        anyGetterWriter.getAndSerialize(testObject, gen, objectMapper.getSerializerProvider());
        gen.flush();
        gen.close();

        // Test that the output matches the expected JSON
        String expectedJson = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
        String actualJson = objectMapper.writeValueAsString(testObject.getAnyGetterMap());
        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNull() throws JsonProcessingException, JsonMappingException {
        Object testObject = new TestObject();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        anyGetterWriter.getAndSerialize(testObject, gen, objectMapper.getSerializerProvider());
        gen.flush();
        gen.close();

        // Test that the null check is working
        TestObject testObject2 = new TestObject();
        anyGetterWriter.getAndSerialize(testObject2, gen, objectMapper.getSerializerProvider());
        fail();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNonMapType() throws JsonProcessingException, JsonMappingException {
        Object testObject = new TestObject();
        testObject.setAnyGetterString("test");

        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        anyGetterWriter.getAndSerialize(testObject, gen, objectMapper.getSerializerProvider());
        gen.flush();
        gen.close();

        // Test that the type check is working
        String testString = (String) testObject.getAnyGetterMap().get("test");
        fail();
    }

    @Test
    public void testGetAndFilter() throws JsonProcessingException, JsonMappingException {
        Object testObject = new TestObject();
        TestFilter filter = new TestFilter();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        anyGetterWriter.getAndFilter(testObject, gen, objectMapper.getSerializerProvider(), filter);
        gen.flush();
        gen.close();

        // Test that the filtering is working
        Map<String, Object> filteredMap = testObject.getAnyGetterMap();
        assertEquals(1, filteredMap.size());
        assertTrue(filteredMap.containsKey("value1"));
        assertFalse(filteredMap.containsKey("value2"));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterNull() throws JsonProcessingException, JsonMappingException {
        Object testObject = new TestObject();
        TestFilter filter = new TestFilter();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        anyGetterWriter.getAndFilter(testObject, gen, objectMapper.getSerializerProvider(), filter);
        gen.flush();
        gen.close();

        // Test that the null check is working
        TestObject testObject2 = new TestObject();
        anyGetterWriter.getAndFilter(testObject2, gen, objectMapper.getSerializerProvider(), filter);
        fail();
    }

    @Test
    public void testResolve() throws JsonProcessingException, JsonMappingException {
        Object testObject = new TestObject();
        ObjectMapper testMapper = new ObjectMapper();

        // Test that the resolver is working
        AnyGetterWriter resolvedWriter = (AnyGetterWriter) testMapper.getSerializerProvider().findValueSerializer(Map.class);
        assertEquals(anyGetterWriter.getClass(), resolvedWriter.getClass());
        assertEquals(anyGetterWriter._property, resolvedWriter._property);
        assertEquals(anyGetterWriter._accessor, resolvedWriter._accessor);
        assertEquals(anyGetterWriter._mapSerializer, resolvedWriter._mapSerializer);
    }

    private static class TestObject {
        private Map<String, Object> anyGetterMap;

        public Map<String, Object> getAnyGetterMap() {
            return anyGetterMap;
        }

        public void setAnyGetterMap(Map<String, Object> anyGetterMap) {
            this.anyGetterMap = anyGetterMap;
        }

        public void setAnyGetterString(String value) {
            this.anyGetterMap.put("value1", value);
            this.anyGetterMap.put("value2", "test2");
        }
    }

    private static class TestFilter implements PropertyFilter {
        @Override
        public void serializeFilteredFields(Map<String, Object> map, JsonGenerator gen, SerializerProvider provider, PropertyFilter pf, JsonSerializer serializer) throws IOException {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("value1")) {
                    gen.writeString(entry.getKey());
                    gen.writeString(":");
                    serializer.serializeValues(entry.getValue(), gen, provider);
                    gen.writeEndObject();
                }
            }
        }
    }
}