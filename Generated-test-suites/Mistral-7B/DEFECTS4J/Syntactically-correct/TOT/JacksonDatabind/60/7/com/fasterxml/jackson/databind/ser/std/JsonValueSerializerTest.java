package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSimpleValueSerialization() throws JsonMappingException, JsonProcessingException {
 Class<MyClass> myClassType = MyClass.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myClassType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyClass.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyClass.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = new MyClass();
 String expectedOutput = "expectedValue";
 MyClass myClassInstance = new MyClass(expectedOutput);
 assertEquals(expectedOutput, serializer.getValue(myClassInstance));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider prov = mapper.getSerializerProvider();
 serializer.serialize(myClassInstance, gen, prov);
 assertEquals(expectedOutput, gen.getOutputContentAsString());
 gen.close();
 }

 @Test
 public void testNullValueSerialization() throws JsonMappingException, JsonProcessingException {
 Class<MyClass> myClassType = MyClass.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myClassType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyClass.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyClass.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = new MyClass();
 assertNull(serializer.getValue(bean));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider prov = mapper.getSerializerProvider();
 serializer.serialize(bean, gen, prov);
 assertNull(gen.getOutputContentAsString());
 gen.close();
 }

 @Test
 public void testExceptionDuringValueMethodCall() throws JsonMappingException, JsonProcessingException {
 Class<MyClass> myClassType = MyClass.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myClassType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyClass.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyClass.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = new MyClass();
 try {
 serializer.getValue(bean);
 } catch (Exception e) {
 // expected
 }
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider prov = mapper.getSerializerProvider();
 try {
 serializer.serialize(bean, gen, prov);
 } catch (IOException e) {
 // expected - serialization should throw an IOException when value method call fails
 }
 gen.close();
 }

 @Test
 public void testSchemaAwareSerialization() throws JsonMappingException, JsonProcessingException, JsonSchema.FormatSchemaMismatchException {
 Class<MyClass> myClassType = MyClass.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myClassType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyClass.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyClass.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = new MyClass("testValue");
 JsonNode schema = mapper.getFactory().getSchemaFactory().getSchema(mapper, myClassType);
 assertEquals(JsonSchema.Type.STRING, schema.get("type"));
 assertNotNull(serializer.getSchema(mapper.getSerializerProvider(), myClassType));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider prov = mapper.getSerializerProvider();
 serializer.serialize(bean, gen, prov);
 assertEquals("testValue", gen.getOutputContentAsString());
 gen.close();
 }

 @Test
 public void testEnumSerialization() throws JsonMappingException, JsonProcessingException {
 Class<MyEnum> myEnumType = MyEnum.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myEnumType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyEnum.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyEnum.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = MyEnum.MY_VALUE;
 String expectedOutput = "MY_VALUE";
 assertEquals(expectedOutput, serializer.getValue(bean));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider prov = mapper.getSerializerProvider();
 serializer.serialize(bean, gen, prov);
 assertEquals(expectedOutput, gen.getOutputContentAsString());
 gen.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testEnumSerializationWithInvalidValue() throws JsonMappingException, JsonProcessingException {
 Class<MyEnum> myEnumType = MyEnum.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myEnumType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyEnum.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyEnum.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = new Object(); // not a valid MyEnum instance
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider prov = mapper.getSerializerProvider();
 serializer.serialize(bean, gen, prov);
 }

 @Test
 public void testContextualSerialization() throws JsonMappingException, JsonProcessingException {
 Class<MyClass> myClassType = MyClass.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myClassType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyClass.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyClass.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = new MyClass("contextualValue");
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
 JsonValueSerializer contextualSerializer = serializer.withResolved(null, valueSerializer, false);
 assertNotNull(contextualSerializer);

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider prov = mapper.getSerializerProvider();
 contextualSerializer.serialize(bean, gen, prov);
 assertEquals("contextualValue", gen.getOutputContentAsString());
 gen.close();
 }

 @Test
 public void testContextualSerializationWithForceTypeInformation() throws JsonMappingException, JsonProcessingException {
 Class<MyClass> myClassType = MyClass.class;
 AnnotatedClass ac = mapper.getTypeFactory().constructType(myClassType);
 AnnotatedMethod valueMethod = ac.findAnnotatedMember(MyClass.class.getMethod("getValue"));
 JsonValueSerializer serializer = (JsonValueSerializer) mapper.findValueSerializer(MyClass.class, null);
 assertNotNull(serializer);
 assertNotNull(valueMethod);

 Object bean = new MyClass("contextualValueWithTypeInformation");
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
 JsonValueSerializer contextualSerializer = serializer.withResolved(null, valueSerializer, true);
 assertNotNull(contextualSerializer);

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 SerializerProvider prov = mapper.getSerializerProvider();
 contextualSerializer.serializeWithType(bean, gen, prov, typeSerializer);
 assertEquals("contextualValueWithTypeInformation", gen.getOutputContentAsString());
 gen.close();
 }

}