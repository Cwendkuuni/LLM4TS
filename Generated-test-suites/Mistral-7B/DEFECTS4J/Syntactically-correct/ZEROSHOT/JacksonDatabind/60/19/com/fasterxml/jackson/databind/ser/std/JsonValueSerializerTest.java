package com.yourpackage.name.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Test class for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    private static final Class<JsonValueSerializer> JsonValueSerializerClass =
            (Class<JsonValueSerializer>) Class.forName("com.fasterxml.jackson.databind.ser.std.JsonValueSerializer");

    private static final Object MOCK_VALUE = new Object() {
        @JsonValue
        public String value() {
            return "test";
        }
    };

    private static final Class<?> MOCK_VALUE_TYPE = MOCK_VALUE.getClass();

    @SuppressWarnings("rawtypes")
    private static final JsonSerializer MOCK_VALUE_SERIALIZER = mock(JsonSerializer.class);

    private static final BeanProperty MOCK_PROPERTY = mock(BeanProperty.class);

    private static final SerializerProvider MOCK_PROVIDER = mock(SerializerProvider.class);

    private static final TypeSerializer MOCK_TYPE_SERIALIZER = mock(TypeSerializer.class);

    private static final JsonFormatVisitorWrapper MOCK_VISITOR = mock(JsonFormatVisitorWrapper.class);

    private static final JavaType MOCK_TYPE_HINT = mock(JavaType.class);

    /**
     * Test constructor with explicit serializer.
     */
    @Test
    public void testConstructorWithExplicitSerializer() throws Exception {
        Method valueMethod = mockMethod(JsonValueSerializerClass, "valueMethod");
        when(valueMethod.getType()).thenReturn(MOCK_VALUE_TYPE);

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, MOCK_VALUE_SERIALIZER);
        assertEquals(MOCK_VALUE_TYPE, serializer.handleType(null, null));
        assertEquals(MOCK_VALUE_SERIALIZER, serializer.getValueSerializer());
    }

    /**
     * Test constructor with contextual serializer.
     */
    @Test
    public void testConstructorWithContextualSerializer() throws Exception {
        Method valueMethod = mockMethod(JsonValueSerializerClass, "valueMethod");
        when(valueMethod.getType()).thenReturn(MOCK_VALUE_TYPE);

        Object value = MOCK_VALUE;
        when(valueMethod.invoke(value)).thenReturn(value);

        BeanProperty property = mock(BeanProperty.class);
        when(property.getName()).thenReturn("propertyName");
        when(property.getType()).thenReturn(MOCK_VALUE_TYPE);

        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findPrimaryPropertySerializer(MOCK_TYPE_HINT, property)).thenReturn(valueSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, property, valueSerializer, true);
        assertEquals(MOCK_VALUE_TYPE, serializer.handleType(null, null));
        assertEquals(valueSerializer, serializer.getValueSerializer());
    }

    /**
     * Test serialization with explicit serializer.
     */
    @Test
    public void testSerializationWithExplicitSerializer() throws Exception {
        Method valueMethod = mockMethod(JsonValueSerializerClass, "valueMethod");
        when(valueMethod.getType()).thenReturn(MOCK_VALUE_TYPE);

        Object value = MOCK_VALUE;
        when(valueMethod.invoke(value)).thenReturn(value);

        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(valueSerializer.serialize(value, eq(null), eq(null))).thenReturn(MOCK_VALUE);

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer);

        Object testObject = new Object() {
            @JsonSerialize(using = JsonValueSerializer.class)
            public Method valueMethod() {
                return valueMethod;
            }
        };

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(testObject, generator, provider);
        verify(generator).writeString("test");
    }

    /**
     * Test serialization with contextual serializer.
     */
    @Test
    public void testSerializationWithContextualSerializer() throws Exception {
        Method valueMethod = mockMethod(JsonValueSerializerClass, "valueMethod");
        when(valueMethod.getType()).thenReturn(MOCK_VALUE_TYPE);

        Object value = MOCK_VALUE;
        when(valueMethod.invoke(value)).thenReturn(value);

        BeanProperty property = mock(BeanProperty.class);
        when(property.getName()).thenReturn("propertyName");
        when(property.getType()).thenReturn(MOCK_VALUE_TYPE);

        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findPrimaryPropertySerializer(MOCK_TYPE_HINT, property)).thenReturn(valueSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, property, valueSerializer, true);

        Object testObject = new Object() {
            @JsonSerialize(using = JsonValueSerializer.class)
            public BeanProperty property() {
                return property;
            }
        };

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(testObject, generator, provider);
        verify(generator).writeString("test");
    }

    /**
     * Test serialization with type information.
     */
    @Test
    public void testSerializationWithTypeInformation() throws Exception {
        Method valueMethod = mockMethod(JsonValueSerializerClass, "valueMethod");
        when(valueMethod.getType()).thenReturn(MOCK_VALUE_TYPE);

        Object value = MOCK_VALUE;
        when(valueMethod.invoke(value)).thenReturn(value);

        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(valueSerializer.serializeWithType(value, eq(null), eq(MOCK_PROVIDER), eq(MOCK_TYPE_SERIALIZER))).thenReturn(MOCK_VALUE);

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, property, valueSerializer, true);

        Object testObject = new Object() {
            @JsonSerialize(using = JsonValueSerializer.class)
            public Method valueMethod() {
                return valueMethod;
            }
        };

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        serializer.serializeWithType(testObject, generator, provider, typeSerializer);
        verify(generator).writeStartObject();
        verify(generator).writeFieldName("@class");
        verify(generator).writeString(MOCK_VALUE_TYPE.getName());
        verify(generator).writeString("test");
        verify(generator).writeEndObject();
    }

    /**
     * Test schema generation.
     */
    @Test
    public void testSchemaGeneration() throws Exception {
        Method valueMethod = mockMethod(JsonValueSerializerClass, "valueMethod");
        when(valueMethod.getType()).thenReturn(MOCK_VALUE_TYPE);

        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(valueSerializer.getSchema(eq(MOCK_PROVIDER), anyObject())).thenReturn(JsonSchema.getDefaultSchemaNode());

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, property, valueSerializer, true);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(MOCK_PROVIDER);

        serializer.acceptJsonFormatVisitor(visitor, MOCK_TYPE_HINT);

        verify(visitor).expectAnyFormat(MOCK_TYPE_HINT);
        verify(valueSerializer).getSchema(eq(MOCK_PROVIDER), anyObject());
    }

    /**
     * Test schema generation for enum types.
     */
    @Test
    public void testSchemaGenerationForEnumTypes() throws Exception {
        Class<?> enumType = Enum.class;

        Method valueMethod = mockMethod(JsonValueSerializerClass, "valueMethod");
        when(valueMethod.getType()).thenReturn(enumType);

        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(valueSerializer.getSchema(eq(MOCK_PROVIDER), anyObject())).thenReturn(JsonSchema.getDefaultSchemaNode());

        when(MOCK_VISITOR.expectStringFormat(anyObject())).thenReturn(mock(JsonStringFormatVisitor.class));

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, property, valueSerializer, true);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(MOCK_PROVIDER);
        when(visitor.expectStringFormat(anyObject())).thenReturn(mock(JsonStringFormatVisitor.class));

        serializer.acceptJsonFormatVisitor(visitor, MOCK_TYPE_HINT);

        verify(MOCK_VISITOR).enumTypes(anyCollection());
        verify(valueSerializer).getSchema(eq(MOCK_PROVIDER), anyObject());
    }

    /**
     * Test that natural types are serialized with default serializer.
     */
    @Test
    public void testSerializationOfNaturalTypesWithDefaultSerializer() throws Exception {
        Class<?> intType = Integer.TYPE;
        JsonSerializer<Object> intSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findValueSerializer(intType, property)).thenReturn(intSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(mockMethod(JsonValueSerializerClass, "valueMethod"), property, null, false);

        int testInt = 42;

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(testInt, generator, provider);
        verify(intSerializer).serialize(testInt, generator, provider);
    }

    /**
     * Test that natural types are not serialized with default serializer if one is explicitly provided.
     */
    @Test
    public void testSerializationOfNaturalTypesWithExplicitSerializer() throws Exception {
        Class<?> intType = Integer.TYPE;
        JsonSerializer<Object> intSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findValueSerializer(intType, property)).thenReturn(intSerializer);

        JsonSerializer<Integer> intValueSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findPrimaryTypeSerializer(intType, property)).thenReturn(intValueSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(mockMethod(JsonValueSerializerClass, "valueMethod"), property, intValueSerializer, false);

        int testInt = 42;

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(testInt, generator, provider);
        verify(intValueSerializer).serialize(testInt, generator, provider);
    }

    /**
     * Test that natural strings are serialized with StringSerializer.
     */
    @Test
    public void testSerializationOfNaturalStringsWithStringSerializer() throws Exception {
        Class<?> stringType = String.class;
        JsonSerializer<Object> stringSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findValueSerializer(stringType, property)).thenReturn(stringSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(mockMethod(JsonValueSerializerClass, "valueMethod"), property, null, false);

        String testString = "test";

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(testString, generator, provider);
        verify(stringSerializer).serialize(testString, generator, provider);
    }

    /**
     * Test that natural booleans are serialized with BooleanSerializer.
     */
    @Test
    public void testSerializationOfNaturalBooleansWithBooleanSerializer() throws Exception {
        Class<?> booleanType = Boolean.TYPE;
        JsonSerializer<Object> booleanSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findValueSerializer(booleanType, property)).thenReturn(booleanSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(mockMethod(JsonValueSerializerClass, "valueMethod"), property, null, false);

        boolean testBoolean = true;

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(testBoolean, generator, provider);
        verify(booleanSerializer).serialize(testBoolean, generator, provider);
    }

    /**
     * Test that natural doubles are serialized with NumberSerializer.
     */
    @Test
    public void testSerializationOfNaturalDoublesWithNumberSerializer() throws Exception {
        Class<?> doubleType = Double.TYPE;
        JsonSerializer<Object> numberSerializer = mock(JsonSerializer.class);
        when(MOCK_PROVIDER.findValueSerializer(doubleType, property)).thenReturn(numberSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(mockMethod(JsonValueSerializerClass, "valueMethod"), property, null, false);

        double testDouble = 3.14;

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(testDouble, generator, provider);
        verify(numberSerializer).serialize(testDouble, generator, provider);
    }

    private Method mockMethod(Class<?> clazz, String name) throws NoSuchMethodException {
        return ReflectionTestUtils.findMethod(clazz, name);
    }
}