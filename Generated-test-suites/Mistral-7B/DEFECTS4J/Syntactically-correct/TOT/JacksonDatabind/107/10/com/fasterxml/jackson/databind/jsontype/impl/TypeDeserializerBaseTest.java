import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testTypeDeserializerBaseForProperty() throws IOException {
 // Given
 Object testInstance = new TestClass();
 BeanProperty property = ObjectMapper.getDefaultInstance().getSerializationConfig()
 .getIntrospector().findBeanProperty(TestClass.class, "testProperty");
 TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(
 ObjectTypeFactory.defaultInstance, null, null, true, null);
 TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(property);

 // When
 // Assume that the test property is of type Integer
 JavaType javaType = objectMapper.getTypeFactory().constructType(Integer.class);
 String typeId = "TestIntegerTypeId";
 Object deserializedObject = actualTypeDeserializer.deserialize(
 objectMapper.getFactory(), null, new SimpleBeanPropertyFilterFinder(property), null, jsonParser -> {
 JsonParser.CurrentContext ctxt = jsonParser.getCurrentContext();
 ctxt.findAndBindValue("typeId", typeId);
 jsonParser.nextToken(); // READ_VALUE_AS_EQUALS
 return null;
 }, javaType, null, null, null);

 // Then
 assertEquals(42, deserializedObject);
 }

 @Test
 public void testTypeDeserializerBaseWithMissingTypeId() throws IOException {
 // Given
 Object testInstance = new TestClass();
 BeanProperty property = ObjectMapper.getDefaultInstance().getSerializationConfig()
 .getIntrospector().findBeanProperty(TestClass.class, "testProperty");
 TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(
 ObjectTypeFactory.defaultInstance, null, null, true, null);
 TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(property);

 // When
 // Assume that no type id is provided
 Object deserializedObject = actualTypeDeserializer.deserialize(
 objectMapper.getFactory(), null, new SimpleBeanPropertyFilterFinder(property), null, jsonParser -> {
JsonParser.CurrentContext ctxt = jsonParser.getCurrentContext();
 ctxt.findAndBindValue("value", 42);
 jsonParser.nextToken(); // READ_VALUE_AS_EQUALS
 return null;
 }, null, null, null, null);

 // Then
 assertNotNull(deserializedObject);
 // The actual type of deserializedObject is not important, as long as it is not null
 }

 @Test(expected = JsonMappingException.class)
 public void testTypeDeserializerBaseWithInvalidTypeId() throws IOException {
 // Given
 Object testInstance = new TestClass();
 BeanProperty property = ObjectMapper.getDefaultInstance().getSerializationConfig()
 .getIntrospector().findBeanProperty(TestClass.class, "testProperty");
 TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(
 ObjectTypeFactory.defaultInstance, null, null, true, null);
 TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(property);

 // When
 // Assume that an invalid type id is provided
 String invalidTypeId = "InvalidTypeId";
 Object deserializedObject = actualTypeDeserializer.deserialize(
 objectMapper.getFactory(), null, new SimpleBeanPropertyFilterFinder(property), null, jsonParser -> {
 JsonParser.CurrentContext ctxt = jsonParser.getCurrentContext();
 ctxt.findAndBindValue("typeId", invalidTypeId);
 jsonParser.nextToken(); // READ_VALUE_AS_EQUALS
 return null;
 }, null, null, null, null);

 // Then
 fail("Expected JsonMappingException but got " + deserializedObject.getClass().getName());
 }

 @Test
 public void testTypeDeserializerBaseWithNullTypeDeserializer() throws IOException {
 // Given
 Object testInstance = new TestClass();
 BeanProperty property = ObjectMapper.getDefaultInstance().getSerializationConfig()
 .getIntrospector().findBeanProperty(TestClass.class, "testProperty");
 TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(
 ObjectTypeFactory.defaultInstance, null, null, true, null);
 TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(property);

 // When
 actualTypeDeserializer = null;
 Object deserializedObject = actualTypeDeserializer.deserialize(
 objectMapper.getFactory(), null, new SimpleBeanPropertyFilterFinder(property), null, jsonParser -> {
 JsonParser.CurrentContext ctxt = jsonParser.getCurrentContext();
 ctxt.findAndBindValue("value", 42);
 jsonParser.nextToken(); // READ_VALUE_AS_EQUALS
 return null;
 }, null, null, null, null);

 // Then
 assertNull(deserializedObject);
 }
}

class TestClass {
 int testProperty;
}

class TestTypeDeserializerBase extends TypeDeserializerBase {
 // Empty constructor for testing
 TestTypeDeserializerBase() {
 super(null, null, null, true, null);
 }
}
