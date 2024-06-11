package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper(new JsonMapperBuilder()
                .findAndRegisterModules()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build());
    }

    @Test
    public void testSerializingSimpleDelegatedType() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter<>(),
                TypeFactory.defaultInstance.constructType(String.class),
                new StringSerializer());
        Object source = "Hello World";
        byte[] resultBytes = objectMapper.writeValueAsBytes(source, serializer);
        String resultString = new String(resultBytes);
        assertEquals("\"Hello World\"", resultString);
    }

    @Test
    public void testSerializingComplexDelegatedType() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new ComplexObjectConverter(),
                TypeFactory.defaultInstance.constructType(ComplexObject.class),
                new ComplexObjectSerializer());

        ComplexObject source = new ComplexObject("Hello World", 42);
        byte[] resultBytes = objectMapper.writeValueAsBytes(source, serializer);
        String resultString = new String(resultBytes);
        ComplexObject deserializedObject = objectMapper.readValue(resultString, ComplexObject.class);
        assertEquals(source.getStringValue(), deserializedObject.getStringValue());
        assertEquals(source.getIntegerValue(), deserializedObject.getIntegerValue());
    }

    @Test
    public void testSerializingNullDelegateValue() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter<>(),
                TypeFactory.defaultInstance.constructType(String.class),
                new StringSerializer());
        Object source = null;
        byte[] resultBytes = objectMapper.writeValueAsBytes(source, serializer);
        assertEquals("null", new String(resultBytes));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingDelegateTypeThatIsTheSameAsInputType() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter<>(),
                TypeFactory.defaultInstance.constructType(String.class),
                new StringSerializer());
        String source = "Hello World";
        objectMapper.writeValueAsBytes(source, serializer);
    }
}

class ComplexObject {
    private final String stringValue;
    private final int integerValue;

    public ComplexObject(String stringValue, int integerValue) {
        this.stringValue = stringValue;
        this.integerValue = integerValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getIntegerValue() {
        return integerValue;
    }
}

class ComplexObjectSerializer extends StdSerializer<ComplexObject> {
    private static final long serialVersionUID = 1L;

    public ComplexObjectSerializer() {
        this(null);
    }

    public ComplexObjectSerializer(SerializerProvider serializerProvider) {
        super(ComplexObject.class);
    }

    @Override
    public void serialize(ComplexObject complexObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("stringValue", complexObject.getStringValue());
        jsonGenerator.writeNumberField("integerValue", complexObject.getIntegerValue());
        jsonGenerator.writeEndObject();
    }
}

class ComplexObjectConverter implements Converter<ComplexObject, Object> {
    @Override
    public Object convert(ComplexObject complexObject) {
        return complexObject;
    }

    @Override
    public ComplexObject convert(Object obj, Class<ComplexObject> clazz) {
        if (obj == null || !(obj instanceof ComplexObject)) {
            throw new IllegalArgumentException();
        }
        return (ComplexObject) obj;
    }
}

class IdentityConverter<T> implements Converter<T, T> {
    @Override
    public T convert(T t) {
        return t;
    }

    @Override
    public T convert(Object obj, Class<T> clazz) {
        if (obj == null || !clazz.isInstance(obj)) {
            throw new IllegalArgumentException();
        }
        return clazz.cast(obj);
    }
}