import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private SerializationConfig config;
 private BeanDescription beanDesc;

 @Before
 public void setup() {
 // Assuming you can create a mock SerializationConfig and BeanDescription for testing
 config = new SerializationConfig();
 beanDesc = new BeanDescription(null);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testBuildWriter_shouldCreateBeanPropertyWriter() throws JsonMappingException {
 // Test basic functionality
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", new JavaType(String.class), null);
 JavaType declaredType = new JavaType(String.class);
 JsonSerializer<?> ser = new JsonSerializer<String>() {};
 TypeSerializer typeSer = new TypeSerializer();
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(new SerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(bpw);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriter_throwsExceptionWhenPropertyNameIsNull() throws JsonMappingException {
 BeanPropertyDefinition propDef = null;
 JavaType declaredType = new JavaType(String.class);
 JsonSerializer<?> ser = new JsonSerializer<String>() {};
 TypeSerializer typeSer = new TypeSerializer();
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 propertyBuilder.buildWriter(new SerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 }

 @Test
 public void testFindSerializationType_returnsExpectedTypeWhenAnnotationIsPresent() throws JsonMappingException {
 // Test finding serialization type based on annotations
 Annotated a = new Annotated() {};
 JavaType declaredType = new JavaType(String[].class);
 propertyBuilder.findSerializationType(a, true, declaredType);
 // Check if returned type is correct
 JavaType expectedType = new JavaType(String[].class);
 assertEquals(expectedType, propertyBuilder.findSerializationType(a, true, declaredType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationType_throwsExceptionWhenTypeIsNotAssignableFromDeclaredType() throws JsonMappingException {
 Annotated a = new Annotated() {};
 JavaType declaredType = new JavaType(String.class);
 JavaType secondary = new JavaType(Integer.class);
 propertyBuilder.findSerializationType(a, true, declaredType);
 // Type is not assignable from declared type, so an exception should be thrown
 }

 // Add more test cases here for other methods and edge cases

}