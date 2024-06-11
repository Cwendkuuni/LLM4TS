package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;
 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() throws JsonMappingException {
 objectMapper = new ObjectMapper();
 config = objectMapper.getSerializationConfig();
 beanDesc = objectMapper.getTypeFactory().constructType(Person.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 /**
 * Test for building BeanPropertyWriter with default values.
 */
 @Test
 public void testDefaultValues() throws JsonProcessingException {
 ObjectWriter objectWriter = objectMapper.writerWithRootValueSerializer(new PersonSerializer());
 Person person = new Person();
 person.setName("John Doe");
 person.setAge(30);
 objectWriter.writeValueAsString(person);

 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 beanDesc.findPropertyDefinition("name"),
 beanDesc.getType(),
 null,
 null,
 null,
 null,
 true);
 assertEquals("John Doe", propertyWriter.getValueHandler().getSerializableValue(propertyWriter, null));
 }

 /**
 * Test for building BeanPropertyWriter with custom type serializer.
 */
 @Test
 public void testCustomTypeSerializer() throws JsonProcessingException {
 ObjectWriter objectWriter = objectMapper.writerWithRootValueSerializer(new PersonSerializer());
 Person person = new Person();
 person.setName("John Doe");
 person.setAge(30);
 person.setColor("red");
 objectWriter.writeValueAsString(person);

 TypeSerializer colorSerializer = new ColorSerializer();
 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 beanDesc.findPropertyDefinition("color"),
 beanDesc.getType(),
 null,
 null,
 null,
 null,
 true,
 null,
 colorSerializer);
 assertEquals("red", propertyWriter.getValueHandler().getSerializableValue(propertyWriter, null));
 }

 /**
 * Test for building BeanPropertyWriter with null values suppressed.
 */
 @Test
 public void testNullValuesSuppressed() throws JsonProcessingException {
 ObjectWriter objectWriter = objectMapper.writerWithRootValueSerializer(new PersonSerializer());
 Person person = new Person();
 person.setName(null);
 person.setAge(null);
 objectWriter.writeValueAsString(person);

 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 beanDesc.findPropertyDefinition("name"),
 beanDesc.getType(),
 null,
 null,
 null,
 null,
 true,
 BeanPropertyWriter.MARKER_FOR_EMPTY);
 assertNull(propertyWriter.getValueHandler().getSerializableValue(propertyWriter, null));
 }

 /**
 * Test for building BeanPropertyWriter with empty collections suppressed.
 */
 @Test
 public void testEmptyCollectionsSuppressed() throws JsonProcessingException {
 ObjectWriter objectWriter = objectMapper.writerWithRootValueSerializer(new PersonSerializer());
 Person person = new Person();
 List<String> list = new ArrayList<>();
 person.setList(list);
 objectWriter.writeValueAsString(person);

 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 beanDesc.findPropertyDefinition("list"),
 beanDesc.getType(),
 null,
 null,
 null,
 null,
 true,
 BeanPropertyWriter.MARKER_FOR_EMPTY);
 assertTrue(propertyWriter.getValueHandler().isEmpty());
 }

 /**
 * Test for building BeanPropertyWriter with custom null serializer.
 */
 @Test
 public void testCustomNullSerializer() throws JsonProcessingException {
 ObjectWriter objectWriter = objectMapper.writerWithRootValueSerializer(new PersonSerializer());
 Person person = new Person();
 person.setName(null);
 person.setAge(30);
 objectWriter.writeValueAsString(person);

 ObjectNullSerializer nullSerializer = new ObjectNullSerializer();
 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 beanDesc.findPropertyDefinition("name"),
 beanDesc.getType(),
 null,
 null,
 null,
 null,
 true,
 null,
 nullSerializer);
 assertNull(propertyWriter.getValueHandler().getSerializableValue(propertyWriter, null));
 }

 /**
 * Test for building BeanPropertyWriter with unwrapping name transformer.
 */
 @Test
 public void testUnwrappingNameTransformer() throws JsonProcessingException {
 ObjectWriter objectWriter = objectMapper.writerWithRootValueSerializer(new PersonSerializer());
 Person person = new Person();
 person.setName("John Doe");
 person.setLastName("Doe");
 objectWriter.writeValueAsString(person);

 NameTransformer nameTransformer = new NameTransformer();
 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 beanDesc.findPropertyDefinition("lastName"),
 beanDesc.getType(),
 null,
 null,
 null,
 null,
 true,
 nameTransformer);
 assertEquals("Doe", propertyWriter.getValueHandler().getSerializableValue(propertyWriter, null));
 }
}

class Person {
 @JsonInclude(JsonInclude.Include.NON_NULL)
 private String name;
 @JsonInclude(JsonInclude.Include.NON_NULL)
 private Integer age;
 @JsonInclude(JsonInclude.Include.NON_EMPTY)
 private List<String> list;
 @JsonSerialize(using = ColorSerializer.class)
 private String color;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public Integer getAge() {
 return age;
 }

 public void setAge(Integer age) {
 this.age = age;
 }

 public List<String> getList() {
 return list;
 }

 public void setList(List<String> list) {
 this.list = list;
 }

 public String getColor() {
 return color;
 }

 public void setColor(String color) {
 this.color = color;
 }
}

class ColorSerializer extends StdSerializer<String> {
 public ColorSerializer() {
 this(null);
 }

 protected ColorSerializer(Class<String> t) {
 super(t);
 }

 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("color_"+value);
 }
}

class ObjectNullSerializer extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNull();
 }
}

class NameTransformer implements BeanPropertyNameTransformer {
 @Override
 public String transformPropertyName(String name, JavaType propertyType, BeanDescription beanDesc) {
 return "lastName";
 }
}