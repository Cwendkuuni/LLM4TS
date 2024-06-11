package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 @BeforeClass
 public static void setup() {
 SerializationConfig config = new SerializationConfig();
 config.setMapperFactory(new AnnotationIntrospectorMapperFactory(new StandardAnnotationIntrospector()));
 BeanDescription beanDesc = new BeanDescription(MyPojo.class, config);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testPropertyWriterCreation() throws JsonMappingException {
 MyPojo myPojo = new MyPojo();
 myPojo.setName("Test Name");
 myPojo.setAge(25);
 MyPojo.Inner innner = new MyPojo.Inner();
 innner.setValue("Test Inner Value");
 myPojo.setInner(innner);

 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, true);
 assertNotNull(writer);
 }

 @Test
 public void testSerializationWithDefaultValues() throws JsonProcessingException {
 MyPojo myPojo = new MyPojo();
 ObjectWriter writer = Json.createObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(myPojo);
 JsonParser parser = Json.createObjectMapper().getFactory().createParser(json);
 MyPojo deserializedPojo = parser.readValueAs(MyPojo.class);

 assertTrue(deserializedPojo.getName().isEmpty());
 assertEquals(0, deserializedPojo.getAge());
 assertNull(deserializedPojo.getInner());
 }

 @Test
 public void testSerializationWithNonDefaultValues() throws JsonMappingException, JsonProcessingException {
 MyPojo myPojo = new MyPojo();
 myPojo.setName("Test Name");
 myPojo.setAge(25);
 MyPojo.Inner innner = new MyPojo.Inner();
 innner.setValue("Test Inner Value");
 myPojo.setInner(innner);

 ObjectWriter writer = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, true).getSerializerProvider().getSerializer(MyPojo.Inner.class);
 writer.setNullSerializer(null); // Disable null serialization
 String json = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, true).writeValueAsString(myPojo);
 JsonParser parser = Json.createObjectMapper().getFactory().createParser(json);
 MyPojo deserializedPojo = parser.readValueAs(MyPojo.class);

 assertEquals("Test Name", deserializedPojo.getName());
 assertEquals(25, deserializedPojo.getAge());
 assertEquals("Test Inner Value", deserializedPojo.getInner().getValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidType() throws JsonMappingException {
 MyPojo myPojo = new MyPojo();
 myPojo.setName("Test Name");
 myPojo.setAge(25);
 myPojo.setInner(new Object()); // Inner type is not MyPojo.Inner

 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, true);
 writer.writeValueAsString(myPojo);
 }

 @Test
 public void testSerializationWithCustomNullSerializer() throws JsonMappingException, JsonProcessingException {
 MyPojo myPojo = new MyPojo();
 myPojo.setName("Test Name");
 myPojo.setAge(25);
 MyPojo.Inner innner = new MyPojo.Inner();
 innner.setValue("Test Inner Value");
 myPojo.setInner(innner);

 JsonSerializer<MyPojo.Inner> customNullSerializer = new JsonSerializer<MyPojo.Inner>() {
 @Override
 public void serialize(MyPojo.Inner value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString("null");
 }
 };

 writer.assignNullSerializer(customNullSerializer);
 String json = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, true).writeValueAsString(myPojo);
 JsonParser parser = Json.createObjectMapper().getFactory().createParser(json);
 MyPojo deserializedPojo = parser.readValueAs(MyPojo.class);

 assertEquals("Test Name", deserializedPojo.getName());
 assertEquals(25, deserializedPojo.getAge());
 assertEquals("null", deserializedPojo.getInner().getValue());
 }

 @Test
 public void testSerializationWithUnwrappingNameTransformer() throws JsonMappingException, JsonProcessingException {
 MyPojo myPojo = new MyPojo();
 myPojo.setName("Test Name");
 myPojo.setAge(25);
 MyPojo.Inner innner = new MyPojo.Inner();
 innner.setValue("Test Inner Value");
 myPojo.setInner(innner);
 NameTransformer nameTransformer = new NameTransformer() {
 @Override
 public String transformDescription(PropertyName propertyName) {
 return propertyName.getShortName();
 }
 };

 writer.unwrappingWriter(nameTransformer);
 String json = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, true).writeValueAsString(myPojo);
 JsonParser parser = Json.createObjectMapper().getFactory().createParser(json);
 MyPojo deserializedPojo = parser.readValueAs(MyPojo.class);

 assertEquals("Name", deserializedPojo.getName());
 assertEquals("Age", Integer.class.getName(), deserializedPojo.propertyName("age").getName());
 assertEquals("Inner", MyPojo.Inner.class.getName(), deserializedPojo.propertyName("inner").getName());
 }
}