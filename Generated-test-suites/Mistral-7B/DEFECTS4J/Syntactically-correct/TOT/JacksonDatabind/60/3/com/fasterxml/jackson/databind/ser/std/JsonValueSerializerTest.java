package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperConfig;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.schema.SchemaFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;
    private SchemaFactory schemaFactory;

    @Before
    public void setup() throws Exception {
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = new ObjectMapper.DefaultTypingResolver();
        JsonMapperConfig config = new JsonMapperConfig(defaultTypingResolver, new SchemaFactory());
        objectMapper = new ObjectMapper(config);
        serializerProvider = mock(SerializerProvider.class);
        schemaFactory = mock(SchemaFactory.class);
    }

    @Test
    public void testJsonValueSerializer() throws JsonProcessingException {
        // Test serialization with an explicit serializer
        AnnotatedMethod accessorMethod = mock(AnnotatedMethod.class);
        when(accessorMethod.getType()).thenReturn(String.class);
        when(accessorMethod.getValue(anyObject())).thenReturn("Hello World");

        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(String.class, null)).thenReturn(stringSerializer);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, stringSerializer, null, false);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(null, jsonGenerator, serializerProvider);
        assertEquals("Hello World", jsonGenerator.getOutputValueAsString());

        // Test serialization with a default serializer
        when(accessorMethod.getValue(anyObject())).thenReturn(42);

        jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, null, false);
        JsonSerializer<Number> numberSerializer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(Number.class, null)).thenReturn(numberSerializer);

        jsonGenerator = mock(JsonGenerator.class);
        jsonValueSerializer.serialize(null, jsonGenerator, serializerProvider);
        assertEquals(42.0, jsonGenerator.getOutputValueAsNumber().doubleValue());

        // Test serialization with a contextual serializer
        when(accessorMethod.getValue(anyObject())).thenReturn(new MyClass());
        MyClassSerializer myClassSerializer = mock(MyClassSerializer.class);
        when(serializerProvider.handlePrimaryContextualization(myClassSerializer, null)).thenReturn(myClassSerializer);

        jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, myClassSerializer, false);
        jsonGenerator = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(null, jsonGenerator, serializerProvider);
        assertEquals(myClassSerializer, jsonValueSerializer.createContextual(serializerProvider, null));
    }

    @Test
    public void testJsonValueSerializerWithSchema() throws JsonProcessingException {
        AnnotatedMethod accessorMethod = mock(AnnotatedMethod.class);
        when(accessorMethod.getType()).thenReturn(String.class);
        when(accessorMethod.getValue(anyObject())).thenReturn("Hello World");

        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(String.class, null)).thenReturn(stringSerializer);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, stringSerializer, null, false);

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JsonSchema jsonSchema = mock(JsonSchema.class);
        when(schemaFactory.getSchema(any(), any())).thenReturn(jsonSchema);
        when(jsonFormatVisitorWrapper.expectStringFormat(any())).thenReturn(mock(JsonStringFormatVisitor.class));

        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);

        // Test schema with a default serializer
        when(accessorMethod.getValue(anyObject())).thenReturn(42);

        jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, null, false);

        jsonGenerator = mock(JsonGenerator.class);
        when(jsonFormatVisitorWrapper.expectAnyFormat(any())).thenReturn(mock(JsonFormatVisitorWrapper.class));

        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);

        // Test schema with a contextual serializer
        when(accessorMethod.getValue(anyObject())).thenReturn(new MyClass());

        MyClassSerializer myClassSerializer = mock(MyClassSerializer.class);
        when(serializerProvider.findValueSerializer(MyClass.class, null)).thenReturn(myClassSerializer);

        jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, myClassSerializer, false);

        jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        jsonSchema = mock(JsonSchema.class);
        when(jsonFormatVisitorWrapper.expectStringFormat(any())).thenReturn(mock(JsonStringFormatVisitor.class));

        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }

    @Test
    public void testJsonValueSerializerWithNullValue() throws JsonProcessingException {
        AnnotatedMethod accessorMethod = mock(AnnotatedMethod.class);
        when(accessorMethod.getType()).thenReturn(String.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, null, false);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(null, jsonGenerator, serializerProvider);
        assertNull(jsonGenerator.getOutputValueAsString());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonValueSerializerWithException() throws JsonProcessingException {
        AnnotatedMethod accessorMethod = mock(AnnotatedMethod.class);
        when(accessorMethod.getType()).thenReturn(String.class);
        when(accessorMethod.getValue(anyObject())).thenCallRealMethod();

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, null, false);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(null, jsonGenerator, serializerProvider);
    }

    static class MyClass {

    }

    static class MyClassSerializer extends StdSerializer<MyClass> {

    }
}