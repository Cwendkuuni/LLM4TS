package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.bean.MapTestBean;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    @org.junit.BeforeClass
    public static void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter anyGetterWriter = mapper.getTypeFactory().constructValueType(AnyGetterWriter.class, MapSerializer.class, BeanProperty.class, AnnotatedMember.class);
        anyGetterWriter = (AnyGetterWriter) mapper.configure(MapperFeature.AUTO_DETECT_SERIALIZERS, false)
                .getDefaultSerializerProvider().findValueSerializer(Map.class, null, null);
        testInstance = new MapTestBean();
    }

    private static MapTestBean testInstance;

    @org.junit.Test
    public void testGetAndSerialize() throws JsonMappingException, JsonProcessingException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator gen = jsonFactory.createGenerator(System.out);

        Object bean = testInstance;
        AnyGetterWriter writer = new AnyGetterWriter(testProperty, testAccessor, _mapSerializer);
        writer.getAndSerialize(bean, gen, new DefaultSerializerProvider());

        gen.flush();
        gen.close();

        JsonFactory referenceJsonFactory = new JsonFactory();
        JsonParser referenceParser = referenceJsonFactory.createParser(AnyGetterWriterTest.class.getResourceAsStream("/expected_output.json"));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> expected = mapper.readValue(referenceParser, Map.class);
        Map<String, Object> actual = new HashMap<>();
        mapper.readValue(jsonFactory, Map.class, actual);

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetAndFilter() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleBeanPropertyFilter filter = SimpleFilterProvider.simpleFilter("testFilterKey", SimpleFilterProvider.Filter.EXCLUDE);
        AnyGetterWriter writer = new AnyGetterWriter(testProperty, testAccessor, _mapSerializer);

        Object bean = testInstance;
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(System.out);
            writer.getAndFilter(bean, gen, new DefaultSerializerProvider(), filter);
            gen.flush();
        } finally {
            if (gen != null) {
                gen.close();
            }
        }

        assertTrue(Collections.isEmpty(testInstance.getMap().entrySet()));
    }

    @org.junit.Test
    public void testResolve() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter writer = new AnyGetterWriter(testProperty, testAccessor, null);

        ObjectMapper testedMapper = mapper.configure(MapperFeature.AUTO_DETECT_SERIALIZERS, false)
                .getDefaultSerializerProvider().findValueSerializer(Map.class, null, null);
        assertEquals(writer, testedMapper);
    }

    @org.junit.Test
    public void testNullValue() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter writer = new AnyGetterWriter(testProperty, testAccessor, _mapSerializer);
        Object bean = null;

        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(System.out);
            writer.getAndSerialize(bean, gen, new DefaultSerializerProvider());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        } finally {
            if (gen != null) {
                gen.close();
            }
        }
    }

    @org.junit.Test
    public void testNonMapValueType() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter writer = new AnyGetterWriter(testProperty, testAccessor, _mapSerializer);

        Object bean = new Object();
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(System.out);
            writer.getAndSerialize(bean, gen, new DefaultSerializerProvider());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        } finally {
            if (gen != null) {
                gen.close();
            }
        }
    }

    @org.junit.Test
    public void testNullMapValue() throws JsonMappingException, JsonProcessingException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator gen = jsonFactory.createGenerator(System.out);

        Object bean = testInstance;
        AnyGetterWriter writer = new AnyGetterWriter(testProperty, testAccessor, _mapSerializer);
        writer.getAndSerialize(bean, gen, new DefaultSerializerProvider());

        Object value = testInstance.getMap().get("testKey");
        testInstance.getMap().put("testKey", null);

        writer.getAndSerialize(bean, gen, new DefaultSerializerProvider());

        gen.flush();
        gen.close();

        JsonFactory referenceJsonFactory = new JsonFactory();
        JsonParser referenceParser = referenceJsonFactory.createParser(AnyGetterWriterTest.class.getResourceAsStream("/expected_output_null_value.json"));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> expected = mapper.readValue(referenceParser, Map.class);
        Map<String, Object> actual = new HashMap<>();
        mapper.readValue(jsonFactory, Map.class, actual);

        assertNotEquals(expected, actual);
    }

    private AnnotatedMember testAccessor;
    private BeanProperty testProperty;
    private MapSerializer _mapSerializer;

    static {
        ObjectMapper mapper = new ObjectMapper();
        SerializedString serializationInclusion = mapper.getSerializationConfig().getDefaultSerializationConfig().getSerializerProvider()
                .findValueSerializer(SerializedString.class, null, null);
        _mapSerializer = mapper.getTypeFactory().constructValueType(MapSerializer.class, SerializerProvider.StdSerializer.class,
                BeanProperty.class, AnnotatedMember.class, serializationInclusion.getClass());

        AnnotatedMember testAccessor = mapper.getTypeFactory().constructValueType(AnnotatedMember.class, JsonProperty.class,
                BeanProperty.class, Class.class,
                JsonProperty.PropertyAccessor.FIELD.getName(), "testAccessor");
        AnnotatedMember testProperty = mapper.getTypeFactory().constructValueType(BeanProperty.class, JsonProperty.class, String.class,
                Class.class, "testProperty");

        testAccessors = new AnnotatedMember[] { testAccessor };
        testProperties = new BeanProperty[] { testProperty };
    }

    private AnnotatedMember[] testAccessors;
    private BeanProperty[] testProperties;
}