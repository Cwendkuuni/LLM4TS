import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializer() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        assertEquals(NumberSerializer.class, serializer.getClass());
    }

    @Test
    public void testInstance() {
        assertEquals(NumberSerializer.instance, NumberSerializer.instance);
    }

    @Test
    public void testIsInt() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        boolean isInt = serializer.isInt;
        assertTrue(isInt);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat format = new JsonFormat();
        when(provider.findFormatOverrides(any(), eq(property), eq(BigInteger.class)))
                .thenReturn(format);

        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(ToStringSerializer.class, result.getClass());
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Number number = new BigDecimal("123.45");
        when(generator.writeNumber(number)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(number, generator, provider);

        verify(generator).writeNumber(number);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Number number = new BigDecimal("1.23E-5");
        when(generator.writeNumber(number)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(number, generator, provider);

        verify(generator).writeNumber(number);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Number number = new BigInteger("12345678901234567890");
        when(generator.writeNumber(number)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(number, generator, provider);

        verify(generator).writeNumber(number);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullNumber() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(null, generator, provider);

        fail("Expected JsonMappingException but got none.");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeOutOfRangeNumber() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Number number = new Long("-9223372036854775809L");
        when(generator.writeNumber(number)).thenThrow(new IOException());

        NumberSerializer serializer = new NumberSerializer(Number.class);
        try {
            serializer.serialize(number, generator, provider);
        } catch (JsonMappingException e) {
            throw e;
        }

        fail("Expected IOException but got JsonMappingException.");
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode result = new NumberSerializer(Number.class).getSchema(provider, Number.class);

        assertEquals("number", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        new NumberSerializer(Number.class).acceptJsonFormatVisitor(visitor, typeHint);

        // Verify that the visitor methods were called, but we don't know what they should return, so we won't assert on that.
    }
}