package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.testng.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private BeanSerializerFactory factory;

    @BeforeMethod
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_TYPES, false);
        mapper.configure(MapperFeature.AUTO_DETECT_SERIALIZERS, false);
        mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
        mapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        factory = mapper.getSerializationConfig().getDefaultSerializerProvider().getBeanSerializerFactory();
    }

    @Test
    public void testSimpleProperty() throws IOException {
        JsonInclude.Value inclusion = JsonInclude.Value.stdInclude();
        BeanPropertyWriter writer = buildWriter("test_simple_property", factory, SimplePojo.class, null, null, null, null, true);
        ObjectNode node = mapper.writeValueAsTree(writer);

        JsonNode expected = mapper.readTree("{\"name\":\"John\",\"age\":30}");
        JsonNode actual = mapper.readTree(node.toString());

        assertEquals(expected, actual);
    }

    @Test
    public void testExplicitSerializationType() throws IOException {
        JsonInclude.Value inclusion = JsonInclude.Value.stdInclude();
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        BeanPropertyWriter writer = buildWriter("test_explicit_serialization_type", factory, String[].class, null, null, null, null, true);

        ObjectNode node = mapper.writeValueAsTree(writer);

        JsonNode expected = mapper.readTree("[\"test\",\"test2\",\"test3\"]");
        JsonNode actual = mapper.readTree(node.toString());

        assertEquals(expected, actual);
    }

    @Test
    public void testNullSuppression() throws IOException {
        JsonInclude.Value inclusion = JsonInclude.Value.builder()
                .withInclude(JsonInclude.Include.NON_NULL)
                .build();

        BeanPropertyWriter writer = buildWriter("test_null_suppression", factory, NullPojo.class, null, null, null, null, true);

        ObjectNode node = mapper.writeValueAsTree(writer);

        JsonNode expected = mapper.readTree("{\"name\":\"John\",\"age\":30}");
        JsonNode actual = mapper.readTree(node.toString());

        assertTrue(actual.isMissingNode("nullField"));
    }

    @Test
    public void testCustomNullSerializer() throws IOException {
        JsonInclude.Value inclusion = JsonInclude.Value.builder()
                .withInclude(JsonInclude.Include.NON_NULL)
                .build();

        JsonSerialize customNullSerializer = new JsonSerialize() {
            @Override
            public void serialize(SerializerProvider provider, Object bean, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNull();
            }
        };

        BeanPropertyWriter writer = buildWriter("test_custom_null_serializer", factory, NullPojo.class, null, null, null, null, true);
        writer.assignNullSerializer(provider.serializerInstance(null, customNullSerializer));

        ObjectNode node = mapper.writeValueAsTree(writer);

        JsonNode expected = mapper.readTree("{\"name\":\"John\",\"age\":30,\"nullField\":null}");
        JsonNode actual = mapper.readTree(node.toString());

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultValues() throws IOException {
        JsonInclude.Value inclusion = JsonInclude.Value.builder()
                .withInclude(JsonInclude.Include.NON_DEFAULT)
                .build();

        BeanPropertyWriter writer = buildWriter("test_default_values", factory, DefaultPojo.class, null, null, null, null, true);

        ObjectNode node = mapper.writeValueAsTree(writer);

        JsonNode expected = mapper.readTree("{\"name\":\"defaultName\",\"age\":0,\"booleanField\":false}");
        JsonNode actual = mapper.readTree(node.toString());

        assertEquals(expected, actual);
    }

    @Test
    public void testCustomIncludeFilter() throws IOException {
        JsonInclude.Value inclusion = JsonInclude.Value.builder()
                .withInclude(JsonInclude.Include.CUSTOM)
                .withValueFilter(CustomIncludeFilter.class)
                .build();

        BeanPropertyWriter writer = buildWriter("test_custom_include_filter", factory, CustomIncludePojo.class, null, null, null, null, true);

        ObjectNode node = mapper.writeValueAsTree(writer);

        JsonNode expected = mapper.readTree("{\"stringField\":\"test\",\"intField\":1,\"booleanField\":true}");
        JsonNode actual = mapper.readTree(node.toString());

        assertEquals(expected, actual);
    }

    private BeanPropertyWriter buildWriter(String name, BeanSerializerFactory factory, Class<?> type,
                                           JsonSerializer<?> serializer, TypeSerializer contentTypeSerializer,
                                           TypeSerializer typeSerializer, AnnotatedMember am, boolean defaultUseStaticTyping) {
        try {
            return factory.buildWriter(null, null, type, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);
        } catch (JsonMappingException e) {
            throw new RuntimeException("Failed to build writer for property '"+name+"'", e);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class DefaultPojo {
        private String name = "defaultName";
        private int age = 0;
        private boolean booleanField = false;
    }

    static class SimplePojo {
        private String name = "John";
        private int age = 30;
    }

    static class NullPojo {
        private String name = "John";
        private String nullField = null;
    }

    static class CustomIncludePojo {
        private String stringField = "test";
        private int intField = 1;
        private boolean booleanField = true;
    }

    static class CustomIncludeFilter implements JsonFilter {
        @Override
        public boolean includes(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return p.getCurrentName().equals("intField");
        }
    }
}