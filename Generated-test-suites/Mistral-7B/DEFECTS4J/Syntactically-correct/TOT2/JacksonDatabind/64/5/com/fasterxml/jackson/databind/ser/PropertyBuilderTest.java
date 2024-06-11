import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), null);
 objectMapper = mapper;
 }

 @Test
 public void testBuildWriter_NullSer() {
 try {
 propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 } catch (JsonMappingException e) {
 assertEquals("Cannot construct instance of null serializer", e.getMessage());
 }
 }

 @Test
 public void testBuildWriter_Valid() {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null);
 JavaType declaredType = null;
 try {
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, propDef, declaredType, null, null, null, null, false);
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testFindSerializationType_Valid() {
 Class<MyClass> clazz = MyClass.class;
 Annotated a = new AnnotatedClass(clazz);
 JavaType declaredType = null;
 try {
 JavaType secondary = propertyBuilder.findSerializationType(a, true, declaredType);
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testFindSerializationType_NotSuperType() {
 Class<MySubClass> subClass = MySubClass.class;
 Class<MyClass> superClass = MyClass.class;
 Annotated a = new AnnotatedClass(subClass);
 JavaType declaredType = null;
 try {
 JavaType secondary = propertyBuilder.findSerializationType(a, true, declaredType);
 } catch (JsonMappingException e) {
 assertEquals("Illegal concrete-type annotation for method '': class com.example.MySubClass not a super-type of (declared) class com.example.MyClass", e.getMessage());
 }
 }

 @Test
 public void testGetDefaultBean_Null() {
 // This test case is for when _defaultBean is null
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 assertEquals(null, propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetDefaultBean_DefaultInstance() {
 MyClass defaultBean = new MyClass();
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 propertyBuilder._defaultBean = defaultBean;
 assertEquals(defaultBean, propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetDefaultBean_NoDefaultConstructor() {
 MyClassNoDefaultConstructor classToTest = new MyClassNoDefaultConstructor();
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 try {
 propertyBuilder._defaultBean = classToTest.newInstance();
 } catch (Exception e) {
 // expected
 }
 assertEquals(PropertyBuilder.NO_DEFAULT_MARKER, propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetPropertyDefaultValue_Valid() {
 MyClass myClass = new MyClass();
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(null, null, null, null, null, null, null, false);
 assertEquals(myClass.getDefaultValue(), beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_NonDefaultEmpty() {
 MyClassWithEmptyList myClass = new MyClassWithEmptyList();
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(null, null, null, null, null, null, null, false);
 assertEquals(JsonInclude.Include.NON_EMPTY, beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_Null() {
 MyClassWithNull myClass = new MyClassWithNull();
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(null, null, null, null, null, null, null, false);
 assertEquals(JsonInclude.Include.NON_NULL, beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_Array() {
 MyClassWithArray myClass = new MyClassWithArray();
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(null, null, null, null, null, null, null, false);
 assertEquals(Arrays.asList(new int[]{}), beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_String() {
 MyClassWithEmptyString myClass = new MyClassWithEmptyString();
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(null, null, null, null, null, null, null, false);
 assertEquals("", beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_Number() {
 MyClassWithNumber myClass = new MyClassWithNumber();
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(null, null, null, null, null, null, null, false);
 assertEquals(0, beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_Object() {
 MyClassWithObject myClass = new MyClassWithObject();
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(null, null, null, null, null, null, null, false);
 assertEquals(null, beanPropertyWriter.getValueToSuppress());
 }

 // Add more test cases as needed for other methods and edge cases

 @Test
 public void testSerializeUsingDefaultValues() throws JsonProcessingException {
 MyClassWithDefaultValues myClass = new MyClassWithDefaultValues();
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{},{}", json);
 }

 @Test
 public void testSerializeUsingExplicitValues() throws JsonProcessingException {
 MyClassWithExplicitValues myClass = new MyClassWithExplicitValues();
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{\"a\":0,\"b\":\"\"}", json);
 }

 @Test
 public void testSerializeUsingNullValues() throws JsonProcessingException {
 MyClassWithNullValues myClass = new MyClassWithNullValues();
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{}", json);
 }

 @Test
 public void testSerializeUsingEmptyListValues() throws JsonProcessingException {
 MyClassWithEmptyListValues myClass = new MyClassWithEmptyListValues();
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{},[]", json);
 }

 @Test
 public void testSerializeUsingEmptyArrayValues() throws JsonProcessingException {
 MyClassWithEmptyArrayValues myClass = new MyClassWithEmptyArrayValues();
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{},[]", json);
 }

 @Test
 public void testSerializeUsingEmptyStringValues() throws JsonProcessingException {
 MyClassWithEmptyStringValues myClass = new MyClassWithEmptyStringValues();
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{},{}", json);
 }

 @Test
 public void testSerializeUsingNullSerializationType() throws JsonProcessingException {
 MyClassWithList myClass = new MyClassWithList();
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{\"list\":null}", json);
 }

 @Test
 public void testSerializeUsingEmptySerializationType() throws JsonProcessingException {
 MyClassWithList myClass = new MyClassWithList();
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{\"list\":[]}", json);
 }

 @Test
 public void testSerializeUsingEmptySerializationTypeWithValue() throws JsonProcessingException {
 MyClassWithListWithValue myClass = new MyClassWithListWithValue();
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{\"list\":[null]}", json);
 }

 @Test
 public void testSerializeUsingEmptySerializationTypeWithValueAndArray() throws JsonProcessingException {
 MyClassWithListAndArray myClass = new MyClassWithListAndArray();
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
 String json = mapper.writeValueAsString(myClass);
 assertEquals("{\"list\":[],\"array\":[]}", json);
 }
}