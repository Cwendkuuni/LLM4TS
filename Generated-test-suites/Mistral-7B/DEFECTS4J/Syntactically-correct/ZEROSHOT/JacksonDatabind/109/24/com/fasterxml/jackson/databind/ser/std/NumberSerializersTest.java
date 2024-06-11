package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test class for NumberSerializers
 */
public class NumberSerializersTest {

    @Test
    public void testNumberSerializers() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> serializers = mapper.getSerializationConfig().getDefaultSerializers();

        // Testing IntegerSerializer
        JsonSerializer<Integer> integerSerializer = serializers.get(Integer.class.getName());
        Integer number1 = 123;
        String json1 = mapper.writeValueAsString(number1);
        Assert.assertEquals(json1, "123");

        // Testing IntegerSerializer with JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Integer numberWithJsonFormat1 = 456;
        String jsonWithJsonFormat1 = mapper.writeValueAsString(numberWithJsonFormat1);
        Assert.assertEquals(jsonWithJsonFormat1, "'456'");

        // Testing IntegerSerializer with ContextualSerializer
        Integer contextualNumber1 = 789;
        String jsonContextualNumber1 = mapper.writeValueAsString(contextualNumber1);
        Assert.assertEquals(jsonContextualNumber1, "789");

        // Testing IntLikeSerializer
        JsonSerializer<Number> intLikeSerializer = serializers.get(Number.class.getName());
        Number number2 = 567;
        String jsonNumber2 = mapper.writeValueAsString(number2);
        Assert.assertEquals(jsonNumber2, "567");

        // Testing LongSerializer
        JsonSerializer<Long> longSerializer = serializers.get(Long.class.getName());
        Long number3 = 9876543210L;
        String jsonNumber3 = mapper.writeValueAsString(number3);
        Assert.assertEquals(jsonNumber3, "9876543210");

        // Testing LongSerializer with JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Long numberWithJsonFormat3 = 123456789012L;
        String jsonWithJsonFormat3 = mapper.writeValueAsString(numberWithJsonFormat3);
        Assert.assertEquals(jsonWithJsonFormat3, "'123456789012'");

        // Testing LongSerializer with ContextualSerializer
        Long contextualNumber3 = 32109876543210L;
        String jsonContextualNumber3 = mapper.writeValueAsString(contextualNumber3);
        Assert.assertEquals(jsonContextualNumber3, "32109876543210");

        // Testing FloatSerializer
        JsonSerializer<Float> floatSerializer = serializers.get(Float.class.getName());
        Float number4 = 123.45f;
        String jsonNumber4 = mapper.writeValueAsString(number4);
        Assert.assertEquals(jsonNumber4, "123.45");

        // Testing FloatSerializer with JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Float numberWithJsonFormat4 = 456.78f;
        String jsonWithJsonFormat4 = mapper.writeValueAsString(numberWithJsonFormat4);
        Assert.assertEquals(jsonWithJsonFormat4, "'456.78'");

        // Testing FloatSerializer with ContextualSerializer
        Float contextualNumber4 = 789.1234f;
        String jsonContextualNumber4 = mapper.writeValueAsString(contextualNumber4);
        Assert.assertEquals(jsonContextualNumber4, "789.1234");

        // Testing DoubleSerializer
        JsonSerializer<Double> doubleSerializer = serializers.get(Double.class.getName());
        Double number5 = 123.45d;
        String jsonNumber5 = mapper.writeValueAsString(number5);
        Assert.assertEquals(jsonNumber5, "123.45");

        // Testing DoubleSerializer with JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Double numberWithJsonFormat5 = 456.78d;
        String jsonWithJsonFormat5 = mapper.writeValueAsString(numberWithJsonFormat5);
        Assert.assertEquals(jsonWithJsonFormat5, "'456.78'");

        // Testing DoubleSerializer with ContextualSerializer
        Double contextualNumber5 = 789.12345d;
        String jsonContextualNumber5 = mapper.writeValueAsString(contextualNumber5);
        Assert.assertEquals(jsonContextualNumber5, "789.12345");
    }

    @Test
    public void testNumberSerializersAddAll() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> serializers = mapper.getSerializationConfig().getDefaultSerializers();
        Map<String, JsonSerializer<?>> expectedSerializers = new java.util.HashMap<>();

        expectedSerializers.put(Integer.class.getName(), new IntegerSerializer(Integer.class));
        expectedSerializers.put(Integer.TYPE.getName(), new IntegerSerializer(Integer.TYPE));
        expectedSerializers.put(Long.class.getName(), new LongSerializer(Long.class));
        expectedSerializers.put(Long.TYPE.getName(), new LongSerializer(Long.TYPE));

        expectedSerializers.put(Byte.class.getName(), IntLikeSerializer.instance);
        expectedSerializers.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        expectedSerializers.put(Short.class.getName(), ShortSerializer.instance);
        expectedSerializers.put(Short.TYPE.getName(), ShortSerializer.instance);

        expectedSerializers.put(Double.class.getName(), new DoubleSerializer(Double.class));
        expectedSerializers.put(Double.TYPE.getName(), new DoubleSerializer(Double.TYPE));
        expectedSerializers.put(Float.class.getName(), FloatSerializer.instance);
        expectedSerializers.put(Float.TYPE.getName(), FloatSerializer.instance);

        NumberSerializers.addAll(expectedSerializers);
        Map<String, JsonSerializer<?>> actualSerializers = mapper.getSerializationConfig().getDefaultSerializers();
        Assert.assertEquals(expectedSerializers, actualSerializers);
    }
}