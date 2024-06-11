package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SettableAnyPropertyTest {

    private ObjectMapper objectMapper;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private KeyDeserializer keyDeserializer;
    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        valueDeserializer = mock(JsonDeserializer.class);
        valueTypeDeserializer = mock(TypeDeserializer.class);
        keyDeserializer = mock(KeyDeserializer.class);
        PropertyNameIdResolver idResolver = new PropertyNameIdResolver();
        when(valueDeserializer.getNullValue(any(DeserializationContext.class))).thenReturn(null);
        when(valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[1].getValueDeserializer().deserialize(invocationOnMock.getArguments()[0], invocationOnMock.getArguments()[1]));
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[1].getValueDeserializer().deserialize(invocationOnMock.getArguments()[0], invocationOnMock.getArguments()[1]));
        when(valueTypeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[1].getValueDeserializer().deserialize(invocationOnMock.getArguments()[0], invocationOnMock.getArguments()[1]));
        when(keyDeserializer.deserializeKey(anyString(), any(DeserializationContext.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        objectMapper.configure(MapperFeature.FAIL_ON_EMPTY_BEANS, false);
        settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), Object.class, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws Exception {
        Object testInstance = new Object();
        String propName = "testProp";
        JsonParser jsonParser = objectMapper.getFactory().createParser("null");
        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance, propName);
        Object actualValue = ((Map<?, ?>) ((AnnotatedField) settableAnyProperty._setter).getValue(testInstance)).get(propName);
        assertEquals(null, actualValue);
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializer() throws Exception {
        Object testInstance = new Object();
        String propName = "testProp";
        Object expectedValue = "testValue";
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProp\":\"testValue\"}");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(expectedValue);
        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance, propName);
        Object actualValue = ((Map<?, ?>) ((AnnotatedField) settableAnyProperty._setter).getValue(testInstance)).get(propName);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializer() throws Exception {
        Object testInstance = new Object();
        String propName = "testProp";
        Class<String> expectedType = String.class;
        Object expectedValue = "testValue";
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProp\":\"testValue\"}");
        when(valueDeserializer.getNullValue(any(DeserializationContext.class))).thenReturn(null);
        when(valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class))).thenAnswer(invocationOnMock -> {
            TypeDeserializer typeDeserializer = invocationOnMock.getArguments()[2];
            when(typeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(expectedValue);
            return expectedValue;
        });
        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance, propName);
        Object actualValue = ((AnnotatedMethod) settableAnyProperty._setter).call(testInstance, propName, null);
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_InvalidValueType() throws Exception {
        Object testInstance = new Object();
        String propName = "testProp";
        Object expectedValue = new Object();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProp\":\"testValue\"}");
        when(valueDeserializer.getNullValue(any(DeserializationContext.class))).thenReturn(null);
        when(valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class))).thenReturn(expectedValue);
        when(valueTypeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(Integer.class);
        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance, propName);
    }

    @Test
    public void testSet_SetFieldValue() throws Exception {
        Object testInstance = new Object();
        String propName = "testProp";
        Object expectedValue = "testValue";
        Map<Object, Object> fieldValueMap = new HashMap<>();
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(expectedValue);
        settableAnyProperty._setter = mock(AnnotatedField.class);
        settableAnyProperty._setterIsField = true;
        when(((AnnotatedField) settableAnyProperty._setter).getValue(testInstance)).thenReturn(fieldValueMap);
        settableAnyProperty.set(testInstance, propName, null);
        Object actualValue = fieldValueMap.get(propName);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSet_SetMethodValue() throws Exception {
        Object testInstance = new Object();
        String propName = "testProp";
        Object expectedValue = "testValue";
        settableAnyProperty._setter = mock(AnnotatedMethod.class);
        settableAnyProperty._setterIsField = false;
        when(((AnnotatedMethod) settableAnyProperty._setter).call(testInstance, anyString(), anyObject())).thenAnswer(invocationOnMock -> {
            Object[] arguments = invocationOnMock.getArguments();
            assertEquals(propName, arguments[0]);
            assertEquals(expectedValue, arguments[1]);
            return null;
        });
        settableAnyProperty.set(testInstance, propName, null);
    }
}