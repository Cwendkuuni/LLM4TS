package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FieldPropertyTest {

    @Test
    public void testBasicDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FieldProperty fieldProperty = new FieldProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedField.class)
        );

        JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)))).thenReturn(1);

        fieldProperty.setValueDeserializer(deserializer);

        String json = "{\"field\": 1}";
        Object result = mapper.readValue(json, FieldProperty.class.getDeclaredField("_field").getType());

        Assert.assertEquals(1, result);
        verify(deserializer).deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)));
    }

    @Test
    public void testNullValueDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FieldProperty fieldProperty = new FieldProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedField.class)
        );

        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(any(DeserializationContext.class))).thenReturn(0);

        fieldProperty.setNullProvider(nullValueProvider);

        String json = "{}";
        Object result = mapper.readValue(json, FieldProperty.class.getDeclaredField("_field").getType());

        Assert.assertEquals(0, result);
        verify(nullValueProvider).getNullValue(any(DeserializationContext.class));
    }

    @Test
    public void testDeserializationWithCustomTypeDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FieldProperty fieldProperty = new FieldProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedField.class)
        );

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)), any()))
                .thenReturn(new StdSerializer<>(String.class));

        fieldProperty.setValueTypeDeserializer(typeDeserializer);

        String json = "{\"field\": \"hello\"}";
        Object result = mapper.readValue(json, FieldProperty.class.getDeclaredField("_field").getType());

        Assert.assertInstanceOf(StdSerializer.class, result);
        verify(typeDeserializer).deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)), any());
    }

    @Test
    public void testSettingValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object target = new Object();
        FieldProperty fieldProperty = new FieldProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedField.class)
        );
        when(fieldProperty.getField()).thenReturn(target.getClass().getField("field"));

        String json = "{\"field\": 1}";
        mapper.readValue(json, FieldProperty.class).setAndReturn(target, 1);

        Assert.assertEquals(1, target.getClass().getField("field").get(target));
    }

    @Test(expected = IOException.class)
    public void testFailingDeserialization() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FieldProperty fieldProperty = new FieldProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedField.class)
        );

        JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class))))
                .thenThrow(new IOException("Error deserializing"));

        fieldProperty.setValueDeserializer(deserializer);

        String json = "{\"field\": 1}";
        mapper.readValue(json, FieldProperty.class);

        verify(deserializer).deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)));
    }
}