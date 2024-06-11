package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationTestBase.createObjectMapper;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    private ObjectMapper objectMapper;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private KeyDeserializer keyDeserializer;

    @BeforeMethod
    public void setUp() {
        valueDeserializer = mock(JsonDeserializer.class);
        valueTypeDeserializer = mock(TypeDeserializer.class);
        keyDeserializer = mock(KeyDeserializer.class);

        objectMapper = createObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSettableAnyProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        JavaType type = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectNode input = ObjectMapperTestUtils.createObjectNode("{\"key\":\"value\"}");

        Object pojo = new Object() {};

        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), pojo, "key");

        verify(setter, times(1)).callOnWith(pojo, "value", argThat(instanceOf(Object.class)));
    }

    @Test
    public void testSettableAnyPropertyValueDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        JavaType type = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object() {};

        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");

        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), pojo, "key");

        verify(setter, times(1)).callOnWith(pojo, "value", eq("value"));
    }

    @Test
    public void testSettableAnyPropertyValueDeserializerWithType() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        JavaType type = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object() {};

        when(valueTypeDeserializer.deserializeTypedObject(any(JsonParser.class), any(DeserializationContext.class), eq(String.class))).thenReturn("value");

        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), pojo, "key");

        verify(setter, times(1)).callOnWith(pojo, "value", eq("value"));
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyPropertyNullValueDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        JavaType type = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, null, valueTypeDeserializer);

        Object pojo = new Object() {};

        ObjectNode input = ObjectMapperTestUtils.createObjectNode("{\"key\":null}");

        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), pojo, "key");

        verifyNoMoreInteractions(setter);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyPropertyInvalidType() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        JavaType type = objectMapper.getTypeFactory().constructValueType(String.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object() {};

        ObjectNode input = ObjectMapperTestUtils.createObjectNode("{\"key\":\"value\"}");

        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), pojo, "key");

        verifyNoMoreInteractions(setter);
    }
}