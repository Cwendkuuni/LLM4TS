package com.example.jsonvalueserializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializerBase;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonValueSerializerTest {

    @Mock
    private JsonSerializer<Object> valueSerializer;

    @Mock
    private BeanProperty property;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private AnnotatedMethod accessorMethod;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private JsonMappingException jsonMappingException;

    @InjectMocks
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() throws Exception {
        when(accessorMethod.getType()).thenReturn(Integer.class);
        when(accessorMethod.getName()).thenReturn("getNumber");
        when(accessorMethod.getDeclaringClass()).thenReturn(TestClass.class);

        when(property.getName()).thenReturn("number");

        when(serializerProvider.isEnabled(MapperFeature.USE_STATIC_TYPING)).thenReturn(false);
        when(serializerProvider.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPES)).thenReturn(false);

        when(serializerProvider.findPrimaryPropertySerializer(Integer.class, property)).thenReturn(valueSerializer);
        when(serializerProvider.findValueSerializer(Integer.class, property)).thenReturn(valueSerializer);

        when(jsonGenerator.getOutputFactory().getSerializerProvider()).thenReturn(serializerProvider);
    }

    @Test
    public void testSerialize() throws Exception {
        when(valueSerializer.serialize(anyInt(), any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);

        when(valueSerializer.getClass()).thenReturn(IntSerializer.class);

        Object testInstance = new TestClass(1);

        jsonValueSerializer.serialize(testInstance, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(eq(1));
    }

    @Test
    public void testSerializeWithType() throws Exception {
        when(valueSerializer.serializeWithType(anyInt(), any(JsonGenerator.class), any(SerializerProvider.class), any(TypeSerializer.class)))
                .thenReturn(null);

        when(valueSerializer.getClass()).thenReturn(IntSerializer.class);

        Object testInstance = new TestClass(1);

        jsonValueSerializer.serializeWithType(testInstance, jsonGenerator, serializerProvider, typeSerializer);

        verify(jsonGenerator).writeNumber(eq(1), any(TypeSerializer.class));
    }

    @Test
    public void testCreateContextual() throws Exception {
        when(valueSerializer.getClass()).thenReturn(IntSerializer.class);

        JsonSerializer<Integer> expectedSerializer = mock(JsonSerializer.class);

        JsonValueSerializer jsonValueSerializer1 = new JsonValueSerializer(accessorMethod, valueSerializer);
        JsonValueSerializer jsonValueSerializer2 = jsonValueSerializer1.withResolved(property, expectedSerializer, false);

        JsonSerializer<?> actualSerializer = jsonValueSerializer2.createContextual(serializerProvider, property);

        assertEquals(expectedSerializer, actualSerializer);
    }

    @Test
    public void testGetSchema() throws Exception {
        when(valueSerializer.getSchema(any(SerializerProvider.class), any(Type.class))).thenReturn(new ObjectMapper().createSchemaNode());

        JsonNode actualSchema = jsonValueSerializer.getSchema(serializerProvider, null);

        assertEquals(new ObjectMapper().createSchemaNode(), actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        when(valueSerializer.acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), any(JavaType.class))).thenReturn(null);

        jsonValueSerializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), null);

        verify(valueSerializer).acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), any(JavaType.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerialize_with_null_value() throws Exception {
        when(valueSerializer.serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenThrow(new JsonMappingException(jsonMappingException));

        jsonValueSerializer.serialize(null, jsonGenerator, serializerProvider);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Integer.TYPE, null));
        assertFalse(jsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("(@JsonValue serializer for method TestClass.getNumber())", jsonValueSerializer.toString());
    }

    private static class IntSerializer extends StdSerializer<Integer> {}

    private static class TestClass {
        private int number;

        public TestClass(int number) {
            this.number = number;
        }

        @JsonSerialize(using = JsonValueSerializer.class, usingProperty = "number")
        public int getNumber() {
            return number;
        }
    }
}

class JsonValueSerializerTest$$JsonValueSerializer extends StdSerializer<Object>
        implements ContextualSerializer, JsonFormatVisitable, SchemaAware {

    private AnnotatedMethod _accessorMethod;
    private JsonSerializer<Object> _valueSerializer;
    private BeanProperty _property;
    private boolean _forceTypeInformation;

    public JsonValueSerializer(AnnotatedMethod valueMethod, JsonSerializer<?> ser) {
        super(valueMethod.getType());
        _accessorMethod = valueMethod;
        _valueSerializer = ser;
        _property = null;
        _forceTypeInformation = true;
    }

    public JsonValueSerializer withResolved(BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo) {
        return new JsonValueSerializer(this, property, ser, forceTypeInfo);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
            throws JsonMappingException {
        return this;
    }

    @Override
    public void serialize(Object bean, JsonGenerator gen, SerializerProvider prov) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer0)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
            throws JsonMappingException {
        throw new UnsupportedOperationException();
    }

    // Add other methods here if necessary
}