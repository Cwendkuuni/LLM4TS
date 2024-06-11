package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private static final Object SOURCE_VALUE = new Object() {
        @Override
        public String toString() {
            return "sourceValue";
        }
    };

    private static final Type SOURCE_TYPE = SOURCE_VALUE.getClass();
    private static final Type DELEGATE_TYPE = String.class;

    private Converter<Object, String> converter;
    private StdDelegatingSerializer delegatingSerializer;
    private JsonSerializer<String> delegateSerializer;

    @BeforeClass
    public void setUp() {
        converter = new SimpleStringConverter();
        delegateSerializer = new StringSerializer();
        delegatingSerializer = new StdDelegatingSerializer(converter, DELEGATE_TYPE, delegateSerializer);
    }

    @Test
    public void testSerialization() throws JsonProcessingException, JsonGenerationException {
        String expectedResult = "delegateValue";
        Object delegateValue = "delegateValue";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        delegatingSerializer.serialize(SOURCE_VALUE, gen, new ObjectMapper().getSerializerProvider());
        Assert.assertEquals(gen.getOutputBuffer().toString(), "\"delegateValue\"");

        gen.reset();
        delegatingSerializer.serializeWithType(SOURCE_VALUE, gen, new ObjectMapper().getSerializerProvider(), new SimpleTypeSerializer());
        Assert.assertEquals(gen.getOutputBuffer().toString(), "{\"@class\":\"java.lang.String\",\"value\":\"delegateValue\"}");

        gen.reset();
        delegatingSerializer.serialize(delegateValue, gen, new ObjectMapper().getSerializerProvider());
        Assert.assertEquals(gen.getOutputBuffer().toString(), "\"delegateValue\"");

        gen.reset();
        delegatingSerializer.serializeWithType(delegateValue, gen, new ObjectMapper().getSerializerProvider(), new SimpleTypeSerializer());
        Assert.assertEquals(gen.getOutputBuffer().toString(), "\"delegateValue\"");
    }

    @Test
    public void testContextualization() throws JsonProcessingException, JsonGenerationException {
        String expectedContextualSerializerResult = "contextualSerializerResult";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        JsonSerializer<String> contextualSerializer = new ContextualStringSerializer();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSerializer(SOURCE_TYPE, contextualSerializer);

        delegatingSerializer = (StdDelegatingSerializer) mapper.findValueSerializer(SOURCE_TYPE);
        delegatingSerializer.resolve(mapper.getSerializerProvider());

        delegatingSerializer.serialize(SOURCE_VALUE, gen, mapper.getSerializerProvider());
        Assert.assertEquals(gen.getOutputBuffer().toString(), "\"contextualSerializerResult\"");

        gen.reset();
        delegatingSerializer.serializeWithType(SOURCE_VALUE, gen, mapper.getSerializerProvider(), new SimpleTypeSerializer());
        Assert.assertEquals(gen.getOutputBuffer().toString(), "{\"@class\":\"java.lang.String\",\"value\":\"contextualSerializerResult\"}");
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        JsonNode expectedSchema = new NumericJsonSchema(SchemaVersion.VERSION_3, false);

        JsonNode schema = delegatingSerializer.getSchema(new ObjectMapper().getSerializerProvider(), SOURCE_TYPE);
        Assert.assertEquals(schema.toString(), expectedSchema.toString());
    }

    @Test
    public void testJsonFormatVisitor() throws JsonProcessingException {
        JsonNode expectedJsonFormatVisitorResult = new ObjectNode(new ObjectMapper().getFactory());

        delegatingSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper("root"), SOURCE_TYPE);

        JsonNode jsonFormatVisitorResult = delegatingSerializer.getSchema(new ObjectMapper().getSerializerProvider(), SOURCE_TYPE);
        Assert.assertEquals(jsonFormatVisitorResult.toString(), expectedJsonFormatVisitorResult.toString());
    }

    private static class SimpleStringConverter implements Converter<Object, String> {
        @Override
        public String convert(Object input) {
            return "delegateValue";
        }

        @Override
        public Object fromText(String text) {
            return "sourceValue";
        }
    }

    private static class SimpleTypeSerializer extends TypeSerializer {
        @Override
        public void serialize(Type type, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            generator.writeStartObject();
            generator.writeFieldName("@class");
            generator.writeValue(type.getClass().getName());
            generator.writeEndObject();
        }

        @Override
        public Type deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Class<?> clazz = null;
            try {
                JsonNode node = p.readValueAsTree();
                clazz = Class.forName(node.get("@class").asText());
            } catch (ClassNotFoundException e) {
                throw ctxt.mappingException(e);
            }
            return ctxt.constructType(clazz);
        }
    }

    private static class ContextualStringSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString("contextualSerializerResult");
        }
    }
}