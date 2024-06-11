package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    private Object testInstance;
    private AnnotatedField testSetter;
    private BeanProperty testProperty;
    private JavaType testType;
    private JsonDeserializer<Object> testValueDeserializer;
    private TypeDeserializer testValueTypeDeserializer;
    private KeyDeserializer testKeyDeserializer;

    @Before
    public void setUp() throws Exception {
        testInstance = mock(Object.class);
        testSetter = mock(AnnotatedField.class);
        testProperty = mock(BeanProperty.class);
        testType = mock(JavaType.class);
        testValueDeserializer = mock(JsonDeserializer.class);
        testValueTypeDeserializer = mock(TypeDeserializer.class);
        testKeyDeserializer = mock(KeyDeserializer.class);
    }

    @Test
    public void testSettableAnyPropertyConstruction() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(testProperty, testSetter, testType, testKeyDeserializer, testValueDeserializer, testValueTypeDeserializer);
        assertEquals(testProperty, settableAnyProperty.getProperty());
        assertEquals(testSetter, settableAnyProperty._setter);
        assertEquals(testType, settableAnyProperty._type);
        assertEquals(testKeyDeserializer, settableAnyProperty._keyDeserializer);
        assertEquals(testValueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(testValueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
    }

    @Test
    public void testWithValueDeserializer() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(testProperty, testSetter, testType, testKeyDeserializer, testValueDeserializer, testValueTypeDeserializer);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(new JsonDeserializer<>());
        assertEquals(testProperty, newSettableAnyProperty.getProperty());
        assertEquals(testSetter, newSettableAnyProperty._setter);
        assertEquals(testType, newSettableAnyProperty._type);
        assertEquals(testKeyDeserializer, newSettableAnyProperty._keyDeserializer);
        assertNotSame(testValueDeserializer, newSettableAnyProperty._valueDeserializer);
        assertEquals(testValueTypeDeserializer, newSettableAnyProperty._valueTypeDeserializer);
    }

    @Test
    public void testFixAccess() throws Exception {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(testProperty, testSetter, testType, testKeyDeserializer, testValueDeserializer, testValueTypeDeserializer);
        settableAnyProperty.fixAccess(mock(DeserializationConfig.class));
        verify(testSetter).fixAccess(true);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object propName = "testPropName";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(testSetter.getValue(testInstance)).thenReturn(new HashMap<>());

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(testProperty, testSetter, testType, testKeyDeserializer, testValueDeserializer, testValueTypeDeserializer);

        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance, propName);

        Object value = testValueDeserializer.deserialize(jsonParser, deserializationContext);

        verify(testSetter).callOnWith(testInstance, propName, value);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(testValueDeserializer.deserialize(jsonParser, deserializationContext))
                .thenReturn("testValue");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(testProperty, testSetter, testType, testKeyDeserializer, testValueDeserializer, testValueTypeDeserializer);

        assertEquals("testValue", settableAnyProperty.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object() {};
        Object propName = "testPropName";
        Object value = "testValue";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(testProperty, testSetter, testType, testKeyDeserializer, testValueDeserializer, testValueTypeDeserializer);

        settableAnyProperty.set(instance, propName, value);

        if (testSetter instanceof AnnotatedField) {
            AnnotatedField annotatedField = (AnnotatedField) testSetter;
            Map<Object,Object> map = (Map<Object,Object>) annotatedField.getValue(instance);
            assertEquals(value, map.get(propName));
        } else {
            verify(testSetter).callOnWith(instance, propName, value);
        }
    }
}