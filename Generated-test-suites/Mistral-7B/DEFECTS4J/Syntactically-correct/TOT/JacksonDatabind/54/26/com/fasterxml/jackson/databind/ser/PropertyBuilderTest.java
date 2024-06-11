package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 @BeforeClass
 public static void setUp() {
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mapper.getSerializationConfig();
 BeanFactory beanFactory = new BeanFactory(config);
 BeanDescription beanDesc = BeanDescription.createDescription(TestPojo.class, beanFactory);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testMissingAnnotation() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, TestPojo.class.getType(), null, null, null, null, false);
 assertNull(writer);
 }

 @Test
 public void testMissingProperty() throws JsonMappingException {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("nonExistentProp", "nonExistentProp", null, null, null, null, TestPojo.class.getType());
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, false);
 assertNull(writer);
 }

 @Test
 public void testEmptyListSerialization() throws JsonMappingException {
 TestPojo pojo = new TestPojo();
 pojo.setList(new ArrayList<>());
 ObjectWriter writer = mapper.writerWithView(MyView.class).with(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS).writeValueAsString(pojo);
 String json = writer.getValueAsString();
 assertTrue(json.isEmpty());
 }

 @Test
 public void testEmptyMapSerialization() throws JsonMappingException {
 TestPojo pojo = new TestPojo();
 Map<String, Object> map = new HashMap<>();
 pojo.setMap(map);
 ObjectWriter writer = mapper.writerWithView(MyView.class).with(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS).writeValueAsString(pojo);
 String json = writer.getValueAsString();
 assertTrue(json.isEmpty());
 }

 @Test
 public void testNullSerialization() throws JsonMappingException {
 TestPojo pojo = new TestPojo();
 pojo.setString("test");
 pojo.setNullValue(null);
 ObjectWriter writer = mapper.writerWithView(MyView.class).writeValueAsString(pojo);
 String json = writer.getValueAsString();
 assertEquals("{\"string\":\"test\",\"nullValue\":null}", json);
 }

 @Test
 public void testNonDefaultSerialization() throws JsonMappingException {
 TestPojo pojo = new TestPojo();
 pojo.setString("test");
 String defaultValue = "defaultValue";
 ObjectWriter writer = mapper.writerWithView(MyView.class).writeValueAsString(pojo);
 String json = writer.getValueAsString();
 assertEquals("{\"string\":\"test\"}", json);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("string", null, TestPojo.class.getType());
 assertNull(valueToSuppress);

 TestPojo pojoWithDefault = new TestPojo();
 ObjectWriter writerWithDefault = mapper.writerWithView(MyView.class).writeValueAsString(pojoWithDefault);
 String jsonWithDefault = writerWithDefault.getValueAsString();
 assertEquals("{\"string\":\""+defaultValue+"\"}", jsonWithDefault);
 }

 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 static class TestPojo {
 private List<Object> list;
 private Map<String, Object> map;
 private String string;
 private Object nullValue;

 public List<Object> getList() {
 return list;
 }

 public void setList(List<Object> list) {
 this.list = list;
 }

 public Map<String, Object> getMap() {
 return map;
 }

 public void setMap(Map<String, Object> map) {
 this.map = map;
 }

 public String getString() {
 return string;
 }

 public void setString(String string) {
 this.string = string;
 }

 public Object getNullValue() {
 return nullValue;
 }

 public void setNullValue(Object nullValue) {
 this.nullValue = nullValue;
 }
 }

 static class MyView extends BaseJsonView {
}

}