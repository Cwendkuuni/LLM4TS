package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    public NumberSerializersTest() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        Integer intValue = 123;
        ObjectNode jsonNode = objectMapper.writeValueAsObject(intValue);
        Assert.assertEquals(Integer.toString(intValue), jsonNode.get("intValue").asText());

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);
        Integer deserializedInt = jsonParser.readValueAs(Integer.class);
        Assert.assertEquals(intValue, deserializedInt);

        // Edge case: negative integer
        intValue = -123;
        jsonNode = objectMapper.writeValueAsObject(intValue);
        Assert.assertEquals(Integer.toString(intValue), jsonNode.get("intValue").asText());

        deserializedInt = jsonParser.readValueAs(Integer.class);
        Assert.assertEquals(intValue, deserializedInt);

        // Error case: non-integer value
        jsonNode.put("intValue", "abc");
        try {
            deserializedInt = jsonParser.readValueAs(Integer.class);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIntegerSerializerWithCustomFormat() throws Exception {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Integer intValue = 123;
        ObjectNode jsonNode = objectMapper.writeValueAsObject(intValue);
        Assert.assertEquals("123", jsonNode.get("intValue").asText());

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);
        Integer deserializedInt = jsonParser.readValueAs(Integer.class);
        Assert.assertEquals(intValue, deserializedInt);

        // Edge case: negative integer
        intValue = -123;
        jsonNode = objectMapper.writeValueAsObject(intValue);
        Assert.assertEquals("123", jsonNode.get("intValue").asText());

        deserializedInt = jsonParser.readValueAs(Integer.class);
        Assert.assertEquals(intValue, deserializedInt);

        // Error case: non-integer value
        jsonNode.put("intValue", "abc");
        try {
            deserializedInt = jsonParser.readValueAs(Integer.class);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Test other serializers similarly: LongSerializer, ShortSerializer, IntLikeSerializer, FloatSerializer, DoubleSerializer

    @Test
    public void testBaseClassBehavior() throws Exception {
        // Test getSchema method
        JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Byte.class);
        ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializerProvider(), null);
        Assert.assertEquals("number", schemaNode.get("type").asText());

        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getAnnotationIntrospector().findAnnotation(serializer.getClass(), JsonFormat.class);
        JsonNumberFormatVisitor numberFormatVisitor = visitor.expectNumberFormat(null);
        Assert.assertNotNull(numberFormatVisitor);
        numberFormatVisitor.numberType(JsonParser.NumberType.INT);

        // Test createContextual method
        BeanProperty property = new BeanProperty("propertyName", Object.class, null);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        serializer = serializer.createContextual(objectMapper.getSerializerProvider(), property);
        Assert.assertEquals(ToStringSerializer.instance, serializer);
    }
}