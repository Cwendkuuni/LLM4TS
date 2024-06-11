package com.example.jackson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.impl.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testEnumSerializer() throws JsonMappingException, JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(Mockito.mock(EnumValues.class), null);

 // Test serialization
 Enum testEnum = MyEnum.VALUE1;
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeNumber(anyInt())).thenCallRealMethod();
 when(gen.writeString(anyString())).thenCallRealMethod();
 serializerProvider.expect(once()).method("findSerializationConfig").andReturn(null);
 serializerProvider.expect(once()).method("findFormatOverrides").andReturn(null);
 serializerProvider.expect(once()).method("isEnabled").withArgument(eq(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).andReturn(false);
 enumSerializer.serialize(testEnum, gen, serializerProvider);
 verify(gen, times(1)).writeString(_values.serializedValueFor(testEnum));

 // Test serialization with index
 serializerProvider.resetCalls();
 serializerProvider.expect(once()).method("isEnabled").withArgument(eq(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).andReturn(false);
 serializerProvider.expect(once()).method("isEnabled").withArgument(eq(SerializationFeature.WRITE_ENUMS_USING_INDEX)).andReturn(true);
 enumSerializer = new EnumSerializer(Mockito.mock(EnumValues.class), Boolean.TRUE);
 enumSerializer.serialize(testEnum, gen, serializerProvider);
 verify(gen, times(1)).writeNumber(0);

 // Test serialization with toString()
 serializerProvider.resetCalls();
 serializerProvider.expect(once()).method("isEnabled").withArgument(eq(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).andReturn(true);
 enumSerializer = new EnumSerializer(Mockito.mock(EnumValues.class), null);
 enumSerializer.serialize(testEnum, gen, serializerProvider);
 verify(gen, times(1)).writeString(testEnum.toString());
 }

 @Test
 public void testContextualSerialization() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 MyClass myClass = new MyClass();
 MyEnum field1 = MyEnum.VALUE1;
 MyEnum field2 = MyEnum.VALUE2;
 BeanDescription beanDescription = mapper.getTypeFactory().constructType(MyClass.class).getAnnotatedClass();
 AnnotatedMethod method = beanDescription.findProperty("field1");
 JsonFormat jsonFormat = method.getAnnotation(JsonFormat.class);
 MyClass serializedObject = mapper.readValue(mapper.writeValueAsString(myClass), MyClass.class);

 // Test serialization with default settings
 assertEquals(field1, serializedObject.getField1());
 assertEquals(field2, serializedObject.getField2());

 // Test serialization with @JsonFormat(Shape.STRING) on property level
 jsonFormat = new JsonFormat();
 jsonFormat.setShape(Shape.STRING);
 method.setAnnotation(jsonFormat);
 MyEnum field1WithStringFormat = MyEnum.VALUE1;
 MyClass objectWithStringFormat = new MyClass(field1WithStringFormat, field2);
 serializedObject = mapper.writeValueAsString(objectWithStringFormat);
 assertEquals(MyEnum.VALUE1.toString(), serializedObject.substring(1, serializedObject.length() - 1));
 assertEquals(MyEnum.VALUE2.toString(), serializedObject.substring(serializedObject.lastIndexOf(","), serializedObject.length()));

 // Test serialization with @JsonFormat(Shape.ARRAY) on property level
 jsonFormat = new JsonFormat();
 jsonFormat.setShape(Shape.ARRAY);
 method.setAnnotation(jsonFormat);
 MyClass objectWithArrayFormat = new MyClass(field1, field2);
 serializedObject = mapper.writeValueAsString(objectWithArrayFormat);
 assertEquals("[VALUE1, VALUE2]", serializedObject);
 }

 @Test
 public void testGetSchema() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Class<MyEnum> enumClass = MyEnum.class;
 JsonNode schema = new EnumSerializer(EnumValues.constructFromName(mapper.getSerializationConfig(), enumClass), null).getSchema(mapper.getSerializationConfig(), enumClass);
 assertEquals("string", schema.getClass().getSimpleName());
 ArrayNode enumNode = (ArrayNode) schema.get("enum");
 assertEquals(2, enumNode.size());
 assertEquals("VALUE1", enumNode.get(0).asText());
 assertEquals("VALUE2", enumNode.get(1).asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Class<MyEnum> enumClass = MyEnum.class;
 JsonFormat.Value format = new JsonFormat();
 JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().acceptJsonFormatVisitor(null, enumClass, format);
 new EnumSerializer(EnumValues.constructFromName(mapper.getSerializationConfig(), enumClass), null).acceptJsonFormatVisitor(visitor, enumClass);
 assertEquals(EnumSerializer.class, visitor.getCurrentValueSerializer().getClass());
 Set<String> enums = visitor.getCurrentValueSerializer().getEnumValues().getValuesAsStrings();
 assertEquals(Arrays.asList("VALUE1", "VALUE2"), enums);
 }

 private SerializerProvider serializerProvider = mock(SerializerProvider.class);
}

enum MyEnum {
 @JsonDeserialize(as = String.class)
 VALUE1,
 @JsonDeserialize(as = String.class)
 VALUE2
}

class MyClass {
 private MyEnum field1;
 private MyEnum field2;

 public MyClass(MyEnum field1, MyEnum field2) {
 this.field1 = field1;
 this.field2 = field2;
 }

 @JsonFormat(shape = Shape.STRING)
 public MyEnum getField1() {
 return field1;
 }

 public MyEnum getField2() {
 return field2;
 }
}