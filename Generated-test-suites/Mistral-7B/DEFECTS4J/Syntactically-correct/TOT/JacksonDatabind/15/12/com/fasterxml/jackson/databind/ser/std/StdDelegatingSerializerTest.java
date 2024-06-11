package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.test.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdDelegatingSerializerTest extends BaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private Converter<Object,?> converter;
    private JsonSerializer<Object> delegateSerializer;
    private JavaType delegateType;
    private StdDelegatingSerializer serializerUnderTest;

    @BeforeMethod
    public void setUp() {
        converter = mock(Converter.class);
        delegateSerializer = mock(JsonSerializer.class);
        delegateType = mock(JavaType.class);

        serializerUnderTest = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerializedValueWithDelegateSerializer() throws Exception {
        when(converter.convert(any(Object.class))).thenReturn("testValue");
        when(delegateSerializer.serializeValue(any(JsonGenerator.class), any(SerializerProvider.class), any(Object.class))).thenAnswer(invocation -> invocation.getArgument(2));

        String result = serializerUnderTest.serializeWithType(null, null, MAPPER.getSerializerProvider(), MAPPER.getTypeFactory().constructType(String.class));

        assertEquals(result, "testValue");
    }

    @Test
    public void testSerializedValueWithNullDelegateValue() throws Exception {
        when(converter.convert(any(Object.class))).thenReturn(null);
        when(delegateSerializer.serializeValue(any(JsonGenerator.class), any(SerializerProvider.class), any(Object.class))).thenThrow(new JsonGenerationException());

        assertNull(serializerUnderTest.serializeWithType(null, null, MAPPER.getSerializerProvider(), MAPPER.getTypeFactory().constructType(String.class)));
    }

    @Test
    public void testSerializedValueWithNullDelegateSerializer() throws Exception {
        when(converter.convert(any(Object.class))).thenReturn("testValue");
        when(delegateSerializer.serializeValue(any(JsonGenerator.class), any(SerializerProvider.class), any(Object.class))).thenReturn(null);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> serializerUnderTest.serializeWithType(null, null, MAPPER.getSerializerProvider(), MAPPER.getTypeFactory().constructType(String.class)));

        assertEquals(exception.getMessage(), "Delegate serializer returned null");
    }

    @Test
    public void testSerializedValueWithTypeMismatch() throws Exception {
        when(converter.convert(any(Object.class))).thenReturn(123);
        when(delegateSerializer.serializeValue(any(JsonGenerator.class), any(SerializerProvider.class), any(Object.class))).thenReturn("testStringValue");
        when(delegateSerializer.isEmpty(any(SerializerProvider.class))).thenReturn(false);

        String result = serializerUnderTest.serializeWithType(null, null, MAPPER.getSerializerProvider(), MAPPER.getTypeFactory().constructType(String.class));

        assertEquals(result, "testStringValue");

        TypeFactory typeFactory = MAPPER.getTypeFactory();
        Type wrongType = typeFactory.constructType(int.class);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> serializerUnderTest.serializeWithType(null, null, MAPPER.getSerializerProvider(), wrongType));

        assertEquals(exception.getMessage(), "Types do not match expected type: int but was: java.lang.String");
    }
}