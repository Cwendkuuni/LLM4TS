package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyNameIDResolver;
import com.fasterxml.jackson.databind.jsontype.SimpleTypeDescriptor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    private Object testInstance;
    private Map<Object, Object> testMap;

    @Before
    public void setup() {
        testInstance = new Object();
        testMap = new java.util.HashMap<>();
    }

    @Test
    public void testSettableAnyPropertyConstruction() {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(testInstance)).thenReturn(testMap);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);

        JavaType javaType = new SimpleTypeDescriptor<String>("java.lang.String").getType();
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(TestClass.class), "testProperty"),
                annotatedField, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        assertEquals("testProperty", settableAnyProperty.getProperty().getName());
        assertEquals(TestClass.class, settableAnyProperty.getProperty().getMember().getDeclaringClass());
        assertEquals(javaType, settableAnyProperty.getType());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(typeDeserializer, settableAnyProperty.getValueTypeDeserializer());
        assertEquals(false, settableAnyProperty.isSetterIsField());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(testInstance)).thenReturn(testMap);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);

        JavaType javaType = new SimpleTypeDescriptor<String>("java.lang.String").getType();
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(TestClass.class), "testProperty"),
                annotatedField, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);

        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newValueDeserializer);

        assertEquals(newValueDeserializer, newSettableAnyProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(testInstance)).thenReturn(testMap);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        JavaType javaType = new SimpleTypeDescriptor<String>("java.lang.String").getType();
        KeyDeserializer keyDeserializer = new PropertyNameIDResolver();
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(TestClass.class), "testProperty"),
                annotatedField, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object key = "testKey";
        String testValue = "testValue";

        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(testValue);

        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance, key);

        assertEquals(testValue, testMap.get(key));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithError() throws IOException {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(testInstance)).thenReturn(testMap);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        JavaType javaType = new SimpleTypeDescriptor<String>("java.lang.String").getType();
        KeyDeserializer keyDeserializer = new PropertyNameIDResolver();
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(TestClass.class), "testProperty"),
                annotatedField, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object key = "testKey";

        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenThrow(new IOException());

        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance, key);
    }

    @Test
    public void testDeserialize() throws IOException {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(testInstance)).thenReturn(testMap);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        JavaType javaType = new SimpleTypeDescriptor<String>("java.lang.String").getType();
        KeyDeserializer keyDeserializer = new PropertyNameIDResolver();
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(TestClass.class), "testProperty"),
                annotatedField, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        String testValue = "testValue";

        when(jsonParser.getText()).thenReturn(testValue);

        Object result = settableAnyProperty.deserialize(jsonParser, deserializationContext);

        assertEquals(testValue, result);
    }

    @Test
    public void testSet() throws IOException {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(testInstance)).thenReturn(testMap);

        Object testKey = "testKey";
        Object testValue = "testValue";

        settableAnyProperty.set(testInstance, testKey, testValue);

        assertEquals(testValue, testMap.get(testKey));
    }
}

class TestClass {
    @SuppressWarnings("unused")
    private Map<Object, Object> testProperty;
}