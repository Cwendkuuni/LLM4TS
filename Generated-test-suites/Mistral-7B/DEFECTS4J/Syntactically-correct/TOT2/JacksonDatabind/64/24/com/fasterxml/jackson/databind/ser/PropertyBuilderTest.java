import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectNode node;

 @Before
 public void setUp() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 this.propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(),
 mapper.findBeanDescription(SomeClass.class));
 }

 @Test
 public void testBuildWriter_PropertyValueEqualsDefault() {
 // Basic test case: call method with valid arguments and check correctness
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, false);
 assertTrue(writer != null);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_NullPropertyDefinition() {
 // Edge case: test null property definition
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, false);
 assertNull(writer);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_NullSerializer() {
 // Edge case: test null serializer
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, false);
 assertNull(writer);
 }

 @Test
 public void testBuildWriter_PropertyValueIsDefault() throws JsonProcessingException {
 // Test case: call method with default-value property and check correctness
 JsonInclude.Include inclusion = JsonInclude.Include.ALWAYS;
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, false, inclusion);
 ObjectNode defaultValueNode = node.objectNode();
 defaultValueNode.put("myProperty", 0);
 ObjectWriter writer1 = mapper.writerWithDefaultPrettyPrinter();
 String json1 = writer1.writeValueAsString(defaultValueNode);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(new BeanPropertyDefinition(
 "myProperty", IntType.class, null, null, null, null, null, null, null, null, null, null), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 assertEquals(expectedWriter, writer);
 }

 @Test
 public void testBuildWriter_PropertyValueNotEqualsDefault() throws JsonProcessingException {
 // Test case: call method with non-default-value property and check correctness
 JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
 ObjectNode nodeWithNonDefaultValue = node.objectNode();
 nodeWithNonDefaultValue.put("myProperty", 5);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, false, inclusion);
 ObjectWriter writer1 = mapper.writerWithDefaultPrettyPrinter();
 String json1 = writer1.writeValueAsString(nodeWithNonDefaultValue);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(new BeanPropertyDefinition(
 "myProperty", IntType.class, null, null, null, null, null, null, null, null, null, null), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 assertNotEquals(expectedWriter, writer);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_NonContainerTypeWithEmptyCollection() {
 // Edge case: test non-container type with empty collection
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, false, JsonInclude.Include.NON_EMPTY);
 fail("Unexpectedly returned a non-null writer");
 }

 @Test
 public void testBuildWriter_IgnoreNulls() throws JsonProcessingException {
 // Test case: call method with null property and check correctness
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, true, null);
 ObjectWriter writer1 = mapper.writerWithDefaultPrettyPrinter();
 ObjectNode nodeWithNullValue = node.objectNode();
 nodeWithNullValue.put("myProperty", null);
 String json1 = writer1.writeValueAsString(nodeWithNullValue);
 ObjectWriter writer2 = mapper.writerWithDefaultPrettyPrinter();
 ObjectNode nodeWithSuppressedNull = node.objectNode();
 nodeWithSuppressedNull.put("myProperty", null);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(new BeanPropertyDefinition(
 "myProperty", IntType.class, null, null, null, null, null, null, null, null, null, null), null, null, null, null, null, null, null, BeanPropertyWriter.MARKER_FOR_EMPTY, null, null, null, null, null, null);
 assertEquals(expectedWriter, writer);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_NullSerializerWithNonEmptyCollection() {
 // Edge case: test null serializer with non-empty collection
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, IntType.class, null, null, null, null, false, JsonInclude.Include.NON_EMPTY);
 assertNull(writer);
 }

 @Test
 public void testFindSerializationType_PropertyAnnotatedWithJsonSerialize() throws JsonMappingException {
 // Test case: call method with property annotated with @JsonSerialize
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMemberImpl(SomeClass.class, "myProperty"), false, IntType.class);
 assertNotNull(actualType);
 }

 @Test
 public void testFindSerializationType_PropertyNotAnnotatedWithJsonSerialize() throws JsonMappingException {
 // Test case: call method with property not annotated with @JsonSerialize
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMemberImpl(SomeClass.class, "someOtherProperty"), false, IntType.class);
 assertNull(actualType);
 }

 @Test
 public void testFindSerializationType_PropertyAnnotatedWithJsonSerializeAndStaticTyping() throws JsonMappingException {
 // Test case: call method with property annotated with @JsonSerialize and static typing
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMemberImpl(SomeClass.class, "myProperty"), true, IntType.class);
 assertNotNull(actualType);
 assertEquals(IntType.class, actualType.getRawClass());
 }

 @Test
 public void testFindSerializationType_PropertyNotAnnotatedWithJsonSerializeAndStaticTyping() throws JsonMappingException {
 // Test case: call method with property not annotated with @JsonSerialize and static typing
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMemberImpl(SomeClass.class, "someOtherProperty"), true, IntType.class);
 assertNull(actualType);
 }

 @Test
 public void testFindSerializationType_PropertyAnnotatedWithJsonSerializeAndDynamicTyping() throws JsonMappingException {
 // Test case: call method with property annotated with @JsonSerialize and dynamic typing
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMemberImpl(SomeClass.class, "myProperty"), false, StringType.class);
 assertNotNull(actualType);
 assertEquals(StringType.class, actualType.getRawClass());
 }

 @Test
 public void testFindSerializationType_PropertyNotAnnotatedWithJsonSerializeAndDynamicTyping() throws JsonMappingException {
 // Test case: call method with property not annotated with @JsonSerialize and dynamic typing
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMemberImpl(SomeClass.class, "someOtherProperty"), false, StringType.class);
 assertNull(actualType);
 }

 @Test
 public void testGetDefaultBean() throws JsonMappingException {
 // Test case: call method and check correctness
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetDefaultBean_WhenDefaultConstructorIsNotFound() throws JsonMappingException {
 // Edge case: test when default constructor is not found
 propertyBuilder = new PropertyBuilder(null, null);
 Object defaultBean = propertyBuilder.getDefaultBean();
 fail("Unexpectedly returned a non-null defaultBean");
 }

 @Test
 public void testGetPropertyDefaultValue_PropertyValueEqualsDefault() throws JsonMappingException {
 // Test case: call method with default-value property and check correctness
 String propertyName = "myProperty";
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, IntType.class);
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_PropertyValueIsDefault() throws JsonMappingException {
 // Test case: call method with default-value property and check correctness
 String propertyName = "myProperty";
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, new AnnotatedMemberImpl(SomeClass.class, "myProperty"), IntType.class);
 assertEquals(0, propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_PropertyValueIsNotNull() throws JsonMappingException {
 // Test case: call method with non-default-value property and check correctness
 String propertyName = "myProperty";
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, new AnnotatedMemberImpl(SomeClass.class, "myProperty"), StringType.class);
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_PropertyValueIsNull() throws JsonMappingException {
 // Test case: call method with null property and check correctness
 String propertyName = "myProperty";
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, new AnnotatedMemberImpl(SomeClass.class, "myProperty"), ObjectType.class);
 assertEquals(JsonInclude.Include.NON_DEFAULT, propertyDefaultValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_PropertyDoesNotExist() throws JsonMappingException {
 // Edge case: test when property does not exist
 String propertyName = "someOtherProperty";
 propertyBuilder.getPropertyDefaultValue(propertyName, null, IntType.class);
 fail("Unexpectedly did not throw an IllegalArgumentException");
 }
}