package com.example.json; // replace with your package name

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;
    private JsonSerializer<Object> valueSerializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        valueSerializer = new JsonValueSerializer(null, null, stringSerializer, false);
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(provider.defaultSerializeNull(generator)).thenCallRealMethod();

        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        serializer.serialize(null, generator, provider);

        verify(generator).writeNull();
    }

    @Test
    public void testSerializeValue() throws JsonProcessingException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();

        when(provider.findValueSerializer(value.getClass(), null)).thenReturn(valueSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        serializer.serialize(new Object[]{value}, generator, provider);

        verify(valueSerializer).serialize(value, generator, provider);
    }

    @Test
    public void testSerializeWithType() throws JsonProcessingException {
        ObjectNode expectedTypeIdNode = objectMapper.createObjectNode();
        expectedTypeIdNode.put("type", "String");

        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer typeSerializerSpy = spy(typeSerializer);

        when(provider.findValueSerializer(Object.class, null)).thenReturn(valueSerializer);
        when(typeSerializerSpy.writeTypePrefixForScalar(any(), generator)).thenCallRealMethod();
        when(typeSerializerSpy.writeTypeSuffixForScalar(any(), generator)).thenCallRealMethod();

        Object value = new Object();

        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, true);
        serializer.serializeWithType(value, generator, provider, typeSerializerSpy);

        verify(typeSerializerSpy).writeTypePrefixForScalar(value, generator);
        verify(valueSerializer).serializeWithType(value, generator, provider, typeSerializerSpy);
        verify(typeSerializerSpy).writeTypeSuffixForScalar(value, generator);

        ObjectNode actualTypeIdNode = generator.getOutput();
        Assert.assertEquals(expectedTypeIdNode, actualTypeIdNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = null;

        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectAnyFormat(null);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = null;
        Class<?> enumType = Enum.class;

        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        boolean result = serializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);

        verify(visitor).expectStringFormat(typeHint);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(int.class, valueSerializer));
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(boolean.class, valueSerializer));
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(double.class, valueSerializer));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, valueSerializer));
    }
}