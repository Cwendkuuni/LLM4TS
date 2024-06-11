import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.js.JsonMappingError;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Converter;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @SuppressWarnings("unchecked")
    private static final Converter<Integer, String> INT_TO_STRING_CONVERTER =
            new Converter<Integer, String>() {
                @Override
                public String convert(Integer source) {
                    return source.toString();
                }
            };

    @SuppressWarnings("unchecked")
    private static final Converter<Double, Integer> DOUBLE_TO_INT_CONVERTER =
            new Converter<Double, Integer>() {
                @Override
                public Integer convert(Double source) {
                    return (int) source;
                }
            };

    @SuppressWarnings("unchecked")
    private static final Converter<String, Integer> STRING_TO_INT_CONVERTER =
            new Converter<String, Integer>() {
                @Override
                public Integer convert(String source) {
                    return Integer.parseInt(source);
                }
            };

    @SuppressWarnings("unchecked")
    private static final JsonSerializer<Integer> INT_SERIALIZER = mock(JsonSerializer.class);

    @SuppressWarnings("unchecked")
    private static final JsonSerializer<String> STRING_SERIALIZER = mock(JsonSerializer.class);

    @SuppressWarnings("unchecked")
    private static final JsonSerializer<Double> DOUBLE_SERIALIZER = mock(JsonSerializer.class);

    @SuppressWarnings("unchecked")
    private static final StdDelegatingSerializer<Object> INT_TO_STRING_CONVERTER_SERIALIZER =
            new StdDelegatingSerializer<Object>(INT_TO_STRING_CONVERTER,
                    TypeFactory.defaultInstance.constructType(String.class), STRING_SERIALIZER);

    @SuppressWarnings("unchecked")
    private static final StdDelegatingSerializer<Object> DOUBLE_TO_INT_CONVERTER_SERIALIZER =
            new StdDelegatingSerializer<Object>(DOUBLE_TO_INT_CONVERTER,
                    TypeFactory.defaultInstance.constructType(Integer.class), INT_SERIALIZER);

    @SuppressWarnings("unchecked")
    private static final StdDelegatingSerializer<Object> STRING_TO_INT_CONVERTER_SERIALIZER =
            new StdDelegatingSerializer<Object>(STRING_TO_INT_CONVERTER,
                    TypeFactory.defaultInstance.constructType(Integer.class), INT_SERIALIZER);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Test
    public void testIntToString() throws JsonGenerationException, JsonMappingException, IOException {
        when(STRING_SERIALIZER.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(i -> i.getArguments()[0].toString());

        ObjectNode result = (ObjectNode) MAPPER.writeValueAsTree(1, INT_TO_STRING_CONVERTER_SERIALIZER);
        assertEquals("1", result.get("1").asText());

        verify(STRING_SERIALIZER, times(1)).serialize(eq(1L), any(JsonGenerator.class), any(SerializerProvider.class));
        verifyNoMoreInteractions(STRING_SERIALIZER);
    }

    @org.junit.Test
    public void testDoubleToInt() throws JsonGenerationException, JsonMappingException, IOException {
        when(INT_SERIALIZER.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(d -> String.valueOf((int) d.getArguments()[0]));

        ObjectNode result = (ObjectNode) MAPPER.writeValueAsTree(3.14, DOUBLE_TO_INT_CONVERTER_SERIALIZER);
        assertEquals("3", result.get("3.14").asText());

        verify(INT_SERIALIZER, times(1)).serialize(eq(3), any(JsonGenerator.class), any(SerializerProvider.class));
        verifyNoMoreInteractions(INT_SERIALIZER);
    }

    @org.junit.Test
    public void testStringToInt() throws JsonGenerationException, JsonMappingException, IOException {
        when(INT_SERIALIZER.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(i -> String.valueOf(i.getArguments()[0].intValue()));

        ObjectNode result = (ObjectNode) MAPPER.writeValueAsTree("3", STRING_TO_INT_CONVERTER_SERIALIZER);
        assertEquals("3", result.get("3").asText());

        verify(INT_SERIALIZER, times(1)).serialize(eq(3), any(JsonGenerator.class), any(SerializerProvider.class));
        verifyNoMoreInteractions(INT_SERIALIZER);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testCannotSerializeSameType() throws JsonGenerationException, JsonMappingException, IOException {
        when(INT_SERIALIZER.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(i -> i.getArguments()[0].toString());

        StdDelegatingSerializer<Object> intToIntConverterSerializer =
                new StdDelegatingSerializer<Object>(INT_TO_STRING_CONVERTER,
                        TypeFactory.defaultInstance.constructType(Integer.class), INT_SERIALIZER);

        MAPPER.writeValueAsTree(1, intToIntConverterSerializer);
    }

    @org.junit.Test
    public void testNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        when(STRING_SERIALIZER.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(i -> null);

        ObjectNode result = (ObjectNode) MAPPER.writeValueAsTree(null, INT_TO_STRING_CONVERTER_SERIALIZER);
        assertNull(result.get("1"));

        verify(STRING_SERIALIZER, never()).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        verifyNoMoreInteractions(STRING_SERIALIZER);
    }

    @org.junit.Test
    public void testEmptyList() throws JsonGenerationException, JsonMappingException, IOException {
        when(STRING_SERIALIZER.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(i -> null);

        JsonSerializer<List<Integer>> intListSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer<Object> intToIntListConverterSerializer =
                new StdDelegatingSerializer<Object>(Function.identity(),
                        TypeFactory.defaultInstance.constructCollectionType(List.class, Integer.class),
                        intListSerializer);

        ObjectNode result = (ObjectNode) MAPPER.writeValueAsTree(Collections.emptyList(), intToIntListConverterSerializer);
        assertNull(result.get("[]"));

        verify(STRING_SERIALIZER, never()).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        verify(intListSerializer, never()).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        verifyNoMoreInteractions(STRING_SERIALIZER);
        verifyNoMoreInteractions(intListSerializer);
    }

    @org.junit.Test
    public void testEmptyArray() throws JsonGenerationException, JsonMappingException, IOException {
        when(STRING_SERIALIZER.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(i -> null);

        JsonSerializer<Object[]> intArraySerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer<Object> intToIntArrayConverterSerializer =
                new StdDelegatingSerializer<Object>(Function.identity(),
                        TypeFactory.defaultInstance.constructArrayType(Integer.class), intArraySerializer);

        ObjectNode result = (ObjectNode) MAPPER.writeValueAsTree(new Object[0], intToIntArrayConverterSerializer);
        assertNull(result.get("[]"));

        verify(STRING_SERIALIZER, never()).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        verify(intArraySerializer, never()).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        verifyNoMoreInteractions(STRING_SERIALIZER);
        verifyNoMoreInteractions(intArraySerializer);
    }

    @org.junit.Test
    public void testConvertValue() throws JsonGenerationException, JsonMappingException, IOException {
        assertEquals("1", INT_TO_STRING_CONVERTER.convert(1));
        assertEquals("3.14", DOUBLE_TO_INT_CONVERTER.convert(3.14));
        assertEquals("3", STRING_TO_INT_CONVERTER.convert("3"));
    }
}