package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.Enum;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotate.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class EnumSerializerTest {

    @InjectMocks
    private EnumSerializer enumSerializer;

    @Test
    public void testSerializeEnumUsingIndex() throws IOException, JsonProcessingException {
        // Given
        Class<MyEnum> enumClass = MyEnum.class;
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);

        // When
        enumSerializer.serialize(MyEnum.FIRST, gen, serializers);

        // Then
        Mockito.verify(gen).writeNumber(0);
    }

    @Test
    public void testSerializeEnumUsingToString() throws IOException, JsonProcessingException {
        // Given
        Class<MyEnum> enumClass = MyEnum.class;
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        SerializerProvider serializersWithFeature = Mockito.spy(serializers);
        Mockito.doReturn(objectMapper.getSerializerProvider()).when(serializersWithFeature)
                .findValueSerializer(enumClass);

        // When
        enumSerializer.serialize(MyEnum.SECOND, gen, serializersWithFeature);

        // Then
        Mockito.verify(gen).writeString("SECOND");
    }

    @Test
    public void testSerializeEnumUsingName() throws IOException, JsonProcessingException {
        // Given
        Class<MyEnum> enumClass = MyEnum.class;
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);

        // When
        enumSerializer.serialize(MyEnum.THIRD, gen, serializers);

        // Then
        Mockito.verify(gen).writeString("THIRD");
    }

    @Test
    public void testCreateContextualSerializerForProperty() throws IOException, JsonProcessingException {
        // Given
        Class<MyEnumProperty> enumClass = MyEnumProperty.class;
        BeanProperty property = Mockito.mock(BeanProperty.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        // When
        JsonSerializer<?> serializer = enumSerializer.createContextual(serializers, property);

        // Then
        assertNotNull(serializer);
        assertInstanceOf(EnumSerializer.class, serializer);
        EnumSerializer enumSerializer = (EnumSerializer) serializer;
        assertEquals(EnumValues.constructFromName(serializers, enumClass), enumSerializer.getEnumValues());
    }

    @Test
    public void testCreateContextualSerializerForClass() throws IOException, JsonProcessingException {
        // Given
        Class<MyEnumClass> enumClass = MyEnumClass.class;
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);

        // When
        JsonSerializer<?> serializer = enumSerializer.construct(enumClass, serializers, beanDescription,
                format);

        // Then
        assertNotNull(serializer);
        assertInstanceOf(EnumSerializer.class, serializer);
        EnumSerializer enumSerializer = (EnumSerializer) serializer;
        assertEquals(EnumValues.constructFromName(serializers, enumClass), enumSerializer.getEnumValues());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Given
        Class<MyJsonFormatVisitorEnum> enumClass = MyJsonFormatVisitorEnum.class;
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        Type typeHint = new ClassType(JsonNode.class);

        // When
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Then
        Mockito.verify(visitor).expectStringFormat(typeHint);

        // Given
        JsonStringFormatVisitor jsonStringFormatVisitor = Mockito.mock(JsonStringFormatVisitor.class);
        Mockito.when(visitor.expectStringFormat(typeHint)).thenReturn(jsonStringFormatVisitor);

        // When
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Then
        Mockito.verify(jsonStringFormatVisitor).enumTypes(Collections.singleton("FIRST"));
        Mockito.verify(jsonStringFormatVisitor).enumTypes(Arrays.asList("SECOND", "THIRD"));
    }

    @Test
    public void testGetSchema() throws IOException {
        // Given
        Class<MyEnum> enumClass = MyEnum.class;
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Type typeHint = new ClassType(JsonNode.class);

        // When
        JsonNode schema = enumSerializer.getSchema(serializers, typeHint);

        // Then
        assertNotNull(schema);
        assertInstanceOf(ObjectNode.class, schema);
        ObjectNode objectNode = (ObjectNode) schema;
        assertEquals("string", objectNode.get("type").asText());
        ArrayNode enumNode = objectNode.get("enum");
        assertEquals(3, enumNode.size());
        assertEquals("FIRST", enumNode.get(0).asText());
        assertEquals("SECOND", enumNode.get(1).asText());
        assertEquals("THIRD", enumNode.get(2).asText());
    }

    private enum MyEnum {
        FIRST,
        SECOND,
        THIRD
    }

    @JsonSerialize(using = EnumSerializer.class)
    private enum MyEnumProperty {
        FIRST,
        SECOND,
        THIRD
    }

    @JsonFormat(shape = Shape.ARRAY)
    private enum MyEnumClass {
        FIRST,
        SECOND,
        THIRD
    }

    private enum MyJsonFormatVisitorEnum {
        FIRST,
        @JsonSerialize(using = JsonSerializer.None.class)
        SECOND,
        THIRD
    }
}