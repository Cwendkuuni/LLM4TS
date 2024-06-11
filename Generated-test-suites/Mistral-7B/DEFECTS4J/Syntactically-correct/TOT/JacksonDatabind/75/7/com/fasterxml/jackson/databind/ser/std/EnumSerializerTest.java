package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link EnumSerializer}.
 */
public class EnumSerializerTest {

    @Test
    public void testSerializeEnumAsString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
        Enum testEnum = Enum.values()[0];
        ObjectNode node = mapper.createObjectNode();
        node.put("myEnum", testEnum);

        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        serializer.serialize(testEnum, gen, null);
        gen.flush();
        StringWriter writer = (StringWriter) gen.asJavaType(StringWriter.class);
        String expected = testEnum.name();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testSerializeEnumAsIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
        Enum testEnum = Enum.values()[0];
        ObjectNode node = mapper.createObjectNode();
        node.put("myEnum", testEnum);

        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        serializer.serialize(testEnum, gen, null);
        gen.flush();
        StringWriter writer = (StringWriter) gen.asJavaType(StringWriter.class);
        String expected = Integer.toString(testEnum.ordinal());
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testSerializeEnumWithJsonFormatAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
        Enum testEnum = Enum.values()[0];
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        ObjectNode node = mapper.createObjectNode();
        node.put("myEnum", testEnum);

        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        serializer.serialize(testEnum, gen, null);
        gen.flush();
        StringWriter writer = (StringWriter) gen.asJavaType(StringWriter.class);
        String expected = Integer.toString(testEnum.ordinal());
        assertEquals(expected, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeEnumWithInvalidJsonFormatAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
        Enum testEnum = Enum.values()[0];
        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        ObjectNode node = mapper.createObjectNode();
        node.put("myEnum", testEnum);

        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        try {
            serializer.serialize(testEnum, gen, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}