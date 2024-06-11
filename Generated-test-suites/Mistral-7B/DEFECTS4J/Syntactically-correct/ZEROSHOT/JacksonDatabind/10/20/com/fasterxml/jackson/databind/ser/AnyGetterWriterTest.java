package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest {

    @Test
    public void testAnyGetterWriter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test class with any getter
        class TestClass {
            @JsonAnyGetter
            Map<String, Object> mapProperty;

            public Map<String, Object> getMapProperty() {
                return mapProperty;
            }
        }

        TestClass testClass = new TestClass();
        testClass.mapProperty = new HashMap<>();
        testClass.mapProperty.put("key1", "value1");
        testClass.mapProperty.put("key2", "value2");

        BeanPropertyDefinition propertyDefinition = new AnnotationIntrospector()
                .findPropertyDefinition(TestClass.class, "mapProperty");

        // Test constructor
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(propertyDefinition.getBeanProperty(),
                propertyDefinition.getMember(), new MapSerializer(mapper.getTypeFactory()
                        .constructMapType(Map.class, String.class, Object.class)));

        // Test getAndSerialize method
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        anyGetterWriter.getAndSerialize(testClass, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        // Expected output:
        // {
        // "key1": "value1",
        // "key2": "value2"
        // }

        // Test getAndFilter method
        @JsonInclude(JsonInclude.Include.NON_NULL)
        class FilterTestClass {
            @JsonAnyGetter
            Map<String, Object> mapProperty;
        }

        FilterTestClass filterTestClass = new FilterTestClass();
        filterTestClass.mapProperty = new HashMap<>();
        filterTestClass.mapProperty.put("key1", "value1");
        filterTestClass.mapProperty.put("key2", null);
        filterTestClass.mapProperty.put("key3", "value3");

        anyGetterWriter.getAndFilter(filterTestClass, jsonGenerator, serializerProvider, null);
        jsonGenerator.flush();

        // Expected output:
        // {
        // "key1": "value1",
        // "key3": "value3"
        // }

        // Test resolve method
        SerializerProvider provider = mapper.getSerializerProvider();
        anyGetterWriter.resolve(provider);
        assertNotNull(anyGetterWriter._mapSerializer);
        assertEquals(StdSerializer.class, anyGetterWriter._mapSerializer.getClass());
    }
}