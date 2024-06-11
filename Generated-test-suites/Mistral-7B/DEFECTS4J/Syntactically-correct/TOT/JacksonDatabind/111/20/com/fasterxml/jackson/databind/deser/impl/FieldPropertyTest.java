package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class FieldPropertyTest {

    private ObjectNode objectNode;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Object instance;
    private Field field;
    private AnnotatedField annotatedField;
    private BeanPropertyDefinition beanPropertyDefinition;
    private FieldProperty fieldProperty;
    private JsonDeserializer<String> stringDeserializer;

    @Before
    public void setUp() throws Exception {
        objectNode = new ObjectMapper().createObjectNode();
        jsonParser = new JsonFactory().createParser(objectNode);
        deserializationContext = mock(DeserializationContext.class);
        instance = new Object();
        field = instance.getClass().getDeclaredField("testField");
        annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        when(beanPropertyDefinition.getBeanProperty()).thenReturn(fieldProperty = new FieldProperty(beanPropertyDefinition, ObjectType.INSTANCE, null, null, annotatedField));
        stringDeserializer = mock(JsonDeserializer.class);

        fieldProperty.overrideAccess(new DeserializationConfig());
    }

    @Test
    public void testDeserializeAndSetWithValidValue() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();

        // When
        objectNode.put("testField", "Test Value");
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Then
        assertEquals("Test Value", field.get(instance));
        verify(deserializationContext, times(1)).findContextualValueDeserializer(eq(String.class));
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();
        doReturn(null).when(fieldProperty).getNullProvider().getNullValue(deserializationContext);

        // When
        objectNode.put("testField", null);
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Then
        assertNull(field.get(instance));
        verify(deserializationContext, times(1)).findContextualValueDeserializer(eq(String.class));
    }

    @Test
    public void testDeserializeAndSetWithNullValueProvider() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(null).when(fieldProperty).getValueDeserializer();

        // When
        objectNode.put("testField", null);
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Then
        assertNull(field.get(instance));
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeAndSetWithInvalidJson() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();

        // When & Then
        assertThrows(JsonProcessingException.class, () -> fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance));
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(new TypeReference<MyClass>() {}.getType());
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();
        doReturn(new MyClassDeserializer()).when(fieldProperty).getValueTypeDeserializer();

        // When
        ObjectNode myClassNode = objectNode.putObject("testField", new ObjectMapper().readValue(objectNode.get("testField"), new TypeReference<MyClass>() {}.getType()));
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Then
        MyClass expectedMyClass = new MyClass();
        assertEquals(expectedMyClass, field.get(instance));
        verify(deserializationContext, times(1)).findContextualValueDeserializer(eq(MyClass.class));
    }

    @Test
    public void testDeserializeAndSetWithSkippedNullValues() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();
        doReturn(new NullValueProvider() {}).when(fieldProperty).getNullProvider();

        // When
        objectNode.put("testField", null);
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Then
        assertNull(field.get(instance));
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testSet() throws Exception {
        // Given
        String value = "Test Value";

        // When
        fieldProperty.set(instance, value);

        // Then
        assertEquals(value, field.get(instance));
    }

    @Test
    public void testSetWithNullValue() throws Exception {
        // Given

        // When
        fieldProperty.set(instance, null);

        // Then
        assertNull(field.get(instance));
    }

    @Test
    public void testSetWithInaccessibleField() throws Exception {
        // Given
        field.setAccessible(true);

        // When & Then
        assertThrows(IllegalAccessException.class, () -> fieldProperty.set(instance, "Test Value"));
    }

    @Test
    public void testDeserializeSetAndReturnWithValidValue() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();

        // When
        JsonParser jsonParser = new JsonFactory().createParser(objectNode);
        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Then
        assertEquals(instance, result);
        assertEquals("Test Value", field.get(instance));
        verify(deserializationContext, times(1)).findContextualValueDeserializer(eq(String.class));
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();
        doReturn(null).when(fieldProperty).getNullProvider().getNullValue(deserializationContext);

        // When
        JsonParser jsonParser = new JsonFactory().createParser(objectNode);
        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Then
        assertEquals(instance, result);
        assertNull(field.get(instance));
        verify(deserializationContext, times(1)).findContextualValueDeserializer(eq(String.class));
    }

    @Test
    public void testDeserializeSetAndReturnWithSkippedNullValues() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();
        doReturn(new NullValueProvider() {}).when(fieldProperty).getNullProvider();

        // When
        JsonParser jsonParser = new JsonFactory().createParser(objectNode);
        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Then
        assertEquals(instance, result);
        assertNull(field.get(instance));
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturnWithInvalidJson() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(String.class);
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();

        // When & Then
        assertThrows(JsonProcessingException.class, () -> fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturnWithTypeDeserializer() throws Exception {
        // Given
        when(beanPropertyDefinition.getValueType()).thenReturn(new TypeReference<MyClass>() {}.getType());
        doReturn(stringDeserializer).when(fieldProperty).getValueDeserializer();
        doReturn(new MyClassDeserializer()).when(fieldProperty).getValueTypeDeserializer();

        // When
        ObjectNode myClassNode = objectNode.putObject("testField", new ObjectMapper().readValue(objectNode.get("testField"), new TypeReference<MyClass>() {}.getType()));
        JsonParser jsonParser = new JsonFactory().createParser(myClassNode);
        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Then
        MyClass expectedMyClass = new MyClass();
        assertEquals(instance, result);
        assertEquals(expectedMyClass, field.get(instance));
        verify(deserializationContext, times(1)).findContextualValueDeserializer(eq(MyClass.class));
    }

    @Test
    public void testSetAndReturnWithValidValue() throws Exception {
        // Given
        String value = "Test Value";

        // When
        Object result = fieldProperty.setAndReturn(instance, value);

        // Then
        assertEquals(instance, result);
        assertEquals(value, field.get(instance));
    }

    @Test
    public void testSetAndReturnWithNullValue() throws Exception {
        // Given

        // When
        Object result = fieldProperty.setAndReturn(instance, null);

        // Then
        assertEquals(instance, result);
        assertNull(field.get(instance));
    }

    @Test
    public void testSetAndReturnWithInaccessibleField() throws Exception {
        // Given
        field.setAccessible(true);

        // When & Then
        assertThrows(IllegalAccessException.class, () -> fieldProperty.setAndReturn(instance, "Test Value"));
    }

    @Test
    public void testReadResolve() throws Exception {
        // Given
        Object resolvedObject = fieldProperty;

        // When
        Object result = resolvedObject.readResolve();

        // Then
        assertEquals(fieldProperty, result);
    }

    private static class MyClassDeserializer extends JsonDeserializer<MyClass> {
        @Override
        public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyClass();
        }
    }

    private static class MyClass {
        @JsonValue
        public String getValue() {
            return "Test Value";
        }
    }
}