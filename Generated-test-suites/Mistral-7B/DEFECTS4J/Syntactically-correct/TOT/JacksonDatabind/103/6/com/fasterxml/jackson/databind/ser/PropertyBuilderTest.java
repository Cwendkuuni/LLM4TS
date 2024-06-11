package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig _config;
 private BeanDescription _beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 _config = mapper.getSerializationConfig();
 _beanDesc = mapper.findBeanDescription(MyPojo.class);
 propertyBuilder = new PropertyBuilder(_config, _beanDesc);
 }

 @Test
 public void testBuildWriter_DefaultValues() throws Exception {
 // Test default values for properties without any custom inclusion or exclusion
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriter_ExcludedByAnnotation() throws Exception {
 // Test exclusion of properties with "NON_DEFAULT" inclusion annotation
 MyPojo pojo = new MyPojo();
 pojo.setExcludedProperty("excludedValue");
 ObjectMapper mapper = new ObjectMapper(_config);
 mapper.setAnnotationIntrospector(new TestAnnotationIntrospector());
 ObjectWriter writer = mapper.writerWithView(MyPojoView.class);
 String json = writer.writeValueAsString(pojo);
 assertTrue(json.isEmpty());
 }

 @Test
 public void testBuildWriter_IncludeByAnnotation() throws Exception {
 // Test inclusion of properties with "Include.ALWAYS" annotation
 MyPojo pojo = new MyPojo();
 pojo.setIncludedProperty("includedValue");
 ObjectMapper mapper = new ObjectMapper(_config);
 ObjectWriter writer = mapper.writerWithView(MyPojoView.class);
 String json = writer.writeValueAsString(pojo);
 assertEquals("{\"includedProperty\":\"includedValue\"}", json);
 }

 @Test
 public void testBuildWriter_IncludeByType() throws Exception {
 // Test inclusion of properties with "Include.NON_EMPTY" type annotation
 MyPojo pojo = new MyPojo();
 pojo.setEmptyList(Collections.emptyList());
 ObjectMapper mapper = new ObjectMapper(_config);
 ObjectWriter writer = mapper.writerWithView(MyPojoView.class);
 String json = writer.writeValueAsString(pojo);
 assertTrue(json.isEmpty());

 // Test inclusion of properties with "Include.NON_EMPTY" custom filter annotation
 MyPojo pojoWithFilter = new MyPojo();
 pojoWithFilter.setEmptyList(Collections.singletonList(new MyNestedPojo()));
 ObjectMapper mapperWithFilter = new ObjectMapper(_config);
 ObjectWriter writerWithFilter = mapperWithFilter.writerWithView(MyPojoView.class)
 .withSerializationConfig(mapperWithFilter.getSerializationConfig()
 .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS));
 String jsonWithFilter = writerWithFilter.writeValueAsString(pojoWithFilter);
 assertEquals("{\"emptyList\":[{\"nestedProperty\":null}]}", jsonWithFilter);
 }

 @Test
 public void testBuildWriter_ExcludeByType() throws Exception {
 // Test exclusion of properties with "Include.NON_NULL" type annotation
 MyPojo pojo = new MyPojo();
 pojo.setNullProperty(null);
 ObjectMapper mapper = new ObjectMapper(_config);
 ObjectWriter writer = mapper.writerWithView(MyPojoView.class);
 String json = writer.writeValueAsString(pojo);
 assertTrue(json.isEmpty());
 }

 @Test
 public void testBuildWriter_ExcludeByDefaultValue() throws Exception {
 // Test exclusion of properties with default value and "Include.NON_DEFAULT" inclusion
 MyPojo pojo = new MyPojo();
 Object defaultBean = propertyBuilder.getDefaultBean();
 pojo.setDefaultValueProperty(defaultBean);
 ObjectMapper mapper = new ObjectMapper(_config);
 ObjectWriter writer = mapper.writerWithView(MyPojoView.class);
 String json = writer.writeValueAsString(pojo);
 assertTrue(json.isEmpty());
 }

 @Test
 public void testBuildWriter_ExcludeByDefaultValueAndAnnotation() throws Exception {
 // Test exclusion of properties with default value and "Include.NON_DEFAULT" inclusion and custom annotation
 MyPojo pojo = new MyPojo();
 Object defaultBean = propertyBuilder.getDefaultBean();
 pojo.setDefaultValueAndAnnotationProperty(defaultBean);
 ObjectMapper mapper = new ObjectMapper(_config);
 ObjectWriter writer = mapper.writerWithView(MyPojoView.class);
 String json = writer.writeValueAsString(pojo);
 assertTrue(json.isEmpty());
 }

 private static class MyPojo {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private String excludedProperty;

 private String includedProperty;

 @JsonSerialize(using = ToStringSerializer.class)
 private List<MyNestedPojo> emptyList;

 private MyNestedPojo nullProperty;

 @JsonInclude(JsonInclude.Include.NON_NULL)
 private MyNestedPojo nullPropertyWithAnnotation;

 @JsonInclude(JsonInclude.Include.NON_EMPTY)
 private List<MyNestedPojo> nonEmptyList;

 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private Object defaultValueProperty;

 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private Object defaultValueAndAnnotationProperty;

 public MyPojo() {}

 public String getExcludedProperty() {
 return excludedProperty;
 }

 public void setExcludedProperty(String value) {
 this.excludedProperty = value;
 }

 public String getIncludedProperty() {
 return includedProperty;
 }

 public void setIncludedProperty(String value) {
 this.includedProperty = value;
 }

 public List<MyNestedPojo> getEmptyList() {
 return emptyList;
 }

 public void setEmptyList(List<MyNestedPojo> value) {
 this.emptyList = value;
 }

 public MyNestedPojo getNullProperty() {
 return nullProperty;
 }

 public void setNullProperty(MyNestedPojo value) {
 this.nullProperty = value;
 }

 public MyNestedPojo getNullPropertyWithAnnotation() {
 return nullPropertyWithAnnotation;
 }

 public void setNullPropertyWithAnnotation(MyNestedPojo value) {
 this.nullPropertyWithAnnotation = value;
 }

 public List<MyNestedPojo> getNonEmptyList() {
 return nonEmptyList;
 }

 public void setNonEmptyList(List<MyNestedPojo> value) {
 this.nonEmptyList = value;
 }

 public Object getDefaultValueProperty() {
 return defaultValueProperty;
 }

 public void setDefaultValueProperty(Object value) {
 this.defaultValueProperty = value;
 }

 public Object getDefaultValueAndAnnotationProperty() {
 return defaultValueAndAnnotationProperty;
 }

 public void setDefaultValueAndAnnotationProperty(Object value) {
 this.defaultValueAndAnnotationProperty = value;
 }
 }

 private static class MyNestedPojo {
 private String nestedProperty;

 public MyNestedPojo() {}

 public String getNestedProperty() {
 return nestedProperty;
 }

 public void setNestedProperty(String value) {
 this.nestedProperty = value;
 }
 }

 private static class MyPojoView {
 }

 private static class TestAnnotationIntrospector extends AnnotationIntrospector {
 @Override
 public JsonSerializer<?> findNullSerializer(AnnotatedMember am) {
 return null;
 }
 }
}