package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws Exception {
 Class<?> testClass = TestBean.class;
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.readValue(getClass().getResource("/testBean.json"), ObjectNode.class);
 BeanDescriptionFactory beanDescFactory = mapper.getFactory().findBeanDescription(testClass, node);
 config = mapper.getSerializationConfig();
 beanDesc = beanDescFactory;
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testDefaultInclusion() throws Exception {
 JsonInclude.Value defaultInclusion = propertyBuilder.getDefaultInclusion();
 assertEquals(JsonInclude.Include.NON_DEFAULT, defaultInclusion.getValueInclusion());
 }

 @Test
 public void testBuildWriterWithNullContentTypeSerializer() throws Exception {
 SerializerProvider prov = mapper.getSerializationConfig().getDefaultSerializerProvider();
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 JavaType declaredType = beanDesc.getType();
 JsonSerializer<?> ser = mapper.findValueSerializer(propDef.getName(), null, null);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, null, null, false);
 assertNotNull(writer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWriterWithNullContentType() throws Exception {
 SerializerProvider prov = mapper.getSerializationConfig().getDefaultSerializerProvider();
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 JavaType declaredType = beanDesc.getType();
 JsonSerializer<?> ser = mapper.findValueSerializer(propDef.getName(), null, null);
 TypeSerializer typeSer = null;
 BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, null, null, false);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testBuildWriterWithDefaultValue() throws Exception {
 Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("name", null, beanDesc.getType());
 assertNull(valueToSuppress);

 boolean suppressNulls = true;
 JsonInclude.Include inclusion = JsonInclude.Include.NON_NULL;
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, beanDesc.getType(), null, null, null, null, suppressNulls);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriterWithDefaultBean() throws Exception {
 TestBean testBean = propertyBuilder.getDefaultBean();
 assertNotNull(testBean);
 }

 @Test
 public void testFindSerializationType() throws Exception {
 Annotated member = mock(Annotated.class);
 JavaType declaredType = beanDesc.getType();
 Object serDef = mock(JsonSerializer.class);
 JavaType serializationType = propertyBuilder.findSerializationType(member, false, declaredType);
 assertEquals(declaredType, serializationType);

 when(member.isAnnotationPresent(JsonSerialize.class)).thenReturn(true);
 when(member.getAnnotation(JsonSerialize.class)).thenReturn(serDef);
 serializationType = propertyBuilder.findSerializationType(member, true, declaredType);
 assertNotNull(serializationType);
 }

 @Test
 public void testFindSerializationTypeWithSubclass() throws Exception {
 Annotated member = mock(Annotated.class);
 JavaType declaredType = beanDesc.getType();
 JavaType secondaryType = TypeFactory.type(MemberSubclass.class, declaredType);
 when(member.isAnnotationPresent(JsonSerialize.class)).thenReturn(false);
 when(member.getAnnotation(JsonSerialize.class)).thenReturn(null);
 when(member.getGenericType()).thenReturn(secondaryType.getType());
 JsonSerialize ser = mock(JsonSerializer.class);
 when(ser.getType()).thenReturn(secondaryType);
 JavaType serializationType = propertyBuilder.findSerializationType(member, false, declaredType);
 assertEquals(secondaryType, serializationType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithMismatchedTypes() throws Exception {
 Annotated member = mock(Annotated.class);
 JavaType declaredType = beanDesc.getType();
 JavaType secondaryType = TypeFactory.type(String.class, declaredType);
 when(member.isAnnotationPresent(JsonSerialize.class)).thenReturn(false);
 when(member.getAnnotation(JsonSerialize.class)).thenReturn(null);
 when(member.getGenericType()).thenReturn(secondaryType.getType());
 JsonSerializer ser = mock(JsonSerializer.class);
 when(ser.getType()).thenReturn(declaredType);
 JavaType serializationType = propertyBuilder.findSerializationType(member, false, declaredType);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetDefaultBean() throws Exception {
 TestBean testBean = propertyBuilder.getDefaultBean();
 assertNotNull(testBean);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDefaultBeanWithNoDefaultConstructor() throws Exception {
 TestBeanWithNoDefaultConstructor testBean = new TestBeanWithNoDefaultConstructor();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object defaultBean = propertyBuilder.getDefaultBean();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDefaultValueWithPrimitiveType() throws Exception {
 JavaType intType = TypeFactory.type(int.class);
 Object defaultValue = propertyBuilder.getDefaultValue(intType);
 assertEquals(0, defaultValue);
 }

 @Test
 public void testGetDefaultValueWithStringType() throws Exception {
 JavaType stringType = TypeFactory.type(String.class);
 Object defaultValue = propertyBuilder.getDefaultValue(stringType);
 assertEquals("", defaultValue);
 }

 @Test
 public void testGetDefaultValueWithContainerType() throws Exception {
 JavaType listType = TypeFactory.typeCollectionType(List.class, int.class);
 Object defaultValue = propertyBuilder.getDefaultValue(listType);
 assertNotNull(defaultValue);
 assertTrue(defaultValue instanceof ObjectNode);
 }

 @Test
 public void testGetDefaultValueWithReferenceType() throws Exception {
 JavaType testBeanType = beanDesc.getType();
 Object defaultValue = propertyBuilder.getDefaultValue(testBeanType);
 assertNull(defaultValue);
 }

 @Test
 public void testGetDefaultValueWithArrayType() throws Exception {
 JavaType intArrayType = TypeFactory.arrayType(int[].class);
 Object defaultValue = propertyBuilder.getDefaultValue(intArrayType);
 assertNull(defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithNullMember() throws Exception {
 assertNull(propertyBuilder.getPropertyDefaultValue("nonExistentProperty", null, beanDesc.getType()));
 }

 @Test
 public void testGetPropertyDefaultValueWithStringProperty() throws Exception {
 TestBean testBean = new TestBean();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("name", null, beanDesc.getType());
 assertNull(defaultValue);

 TestBean testBeanWithDefaultValue = new TestBean("DefaultValue");
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 defaultValue = propertyBuilder.getPropertyDefaultValue("name", null, beanDesc.getType());
 assertEquals("DefaultValue", defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithIntProperty() throws Exception {
 TestBean testBean = new TestBean();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("age", null, beanDesc.getType());
 assertEquals(0, defaultValue);

 TestBean testBeanWithDefaultValue = new TestBean(21);
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 defaultValue = propertyBuilder.getPropertyDefaultValue("age", null, beanDesc.getType());
 assertEquals(21, defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithListProperty() throws Exception {
 TestBean testBean = new TestBean();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("numbers", null, beanDesc.getType());
 assertNotNull(defaultValue);
 assertTrue(defaultValue instanceof ObjectNode);
 ObjectNode defaultNode = (ObjectNode) defaultValue;
 assertEquals(0, defaultNode.size());

 TestBean testBeanWithDefaultValue = new TestBean(Arrays.asList(1, 2, 3));
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 defaultValue = propertyBuilder.getPropertyDefaultValue("numbers", null, beanDesc.getType());
 ObjectNode defaultNode2 = (ObjectNode) defaultValue;
 assertEquals(3, defaultNode2.size());
 }

 @Test
 public void testGetPropertyDefaultValueWithMapProperty() throws Exception {
 TestBean testBean = new TestBean();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("map", null, beanDesc.getType());
 assertNotNull(defaultValue);
 assertTrue(defaultValue instanceof ObjectNode);
 ObjectNode defaultNode = (ObjectNode) defaultValue;
 assertEquals(0, defaultNode.size());

 TestBean testBeanWithDefaultValue = new TestBean(Collections.singletonMap("key", "value"));
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 defaultValue = propertyBuilder.getPropertyDefaultValue("map", null, beanDesc.getType());
 ObjectNode defaultNode2 = (ObjectNode) defaultValue;
 assertEquals(1, defaultNode2.size());
 assertEquals("value", defaultNode2.get("key"));
 }
}

 class TestBean {
 private String name;
 private int age;
 private List<Integer> numbers;
 private Map<String, String> map;

 public TestBean() {
 }

 public TestBean(String name) {
 this();
 this.name = name;
 }

 public TestBean(int age) {
 this();
 this.age = age;
 }

 public TestBean(List<Integer> numbers) {
 this();
 this.numbers = numbers;
 }

 public TestBean(Map<String, String> map) {
 this();
 this.map = map;
 }
}

 class TestBeanWithNoDefaultConstructor {
 private TestBeanWithNoDefaultConstructor() {
 }
}