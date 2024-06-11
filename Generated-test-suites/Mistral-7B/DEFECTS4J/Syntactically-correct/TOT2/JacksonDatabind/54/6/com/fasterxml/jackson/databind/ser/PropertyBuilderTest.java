import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 @Test
 public void testClassAnnotations() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), new BeanDescription(TestBean.class));
 Annotations classAnnotations = propertyBuilder.getClassAnnotations();
 assertTrue(classAnnotations instanceof AnnotatedClass);
 }

 @Test
 public void testBuildWriterBasicCase() throws Exception {
 SerializationConfig config = SerializationConfigFactory.getEmptyConfig();
 BeanDescription beanDesc = new BeanDescription(TestBean.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 ObjectMapper mapper = new ObjectMapper(config);
 PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriterWithNullSerializer() throws Exception {
 SerializationConfig config = SerializationConfigFactory.getEmptyConfig();
 BeanDescription beanDesc = new BeanDescription(TestBean.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 ObjectMapper mapper = new ObjectMapper(config);
 Serializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 JavaType declaredType = null;
 PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, ser, typeSer, contentTypeSer, am, false);
 assertNull(writer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWriterWithEmptyContentType() throws Exception {
 SerializationConfig config = SerializationConfigFactory.getEmptyConfig();
 BeanDescription beanDesc = new BeanDescription(TestBean.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 ObjectMapper mapper = new ObjectMapper(config);
 Serializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 JavaType declaredType = null;
 PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, ser, typeSer, contentTypeSer, am, false);
 }

 @Test
 public void testGetClassAnnotations() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), new BeanDescription(TestBean.class));
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testFindSerializationTypeWithStaticTyping() throws Exception {
 SerializationConfig config = SerializationConfigFactory.getEmptyConfig();
 BeanDescription beanDesc = new BeanDescription(TestBean.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Annotated a = new AnnotatedClass(TestBean.class);
 JavaType declaredType = null;
 boolean useStaticTyping = true;
 JavaType secondary = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
 assertTrue(secondary instanceof StaticTypedType);
 }

 @Test
 public void testFindSerializationTypeWithDynamicTyping() throws Exception {
 SerializationConfig config = SerializationConfigFactory.getEmptyConfig();
 BeanDescription beanDesc = new BeanDescription(TestBean.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Annotated a = new AnnotatedClass(TestBean.class);
 JavaType declaredType = null;
 boolean useStaticTyping = false;
 JavaType secondary = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
 assertNull(secondary);
 }

 @Test
 public void testGetPropertyDefaultValue() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), new BeanDescription(TestBean.class));
 String name = "name";
 AnnotatedMember member = new AnnotatedField(TestBean.class.getField("name"));
 JavaType type = JavaTypeFactory.valueType(String.class);
 Object defaultBean = propertyBuilder.getDefaultBean();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertEquals("", defaultValue);
 }

 @Test
 public void testGetDefaultValue() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), null);
 JavaType type = JavaTypeFactory.valueType(TestBean.class);
 Object defaultValue = propertyBuilder.getDefaultValue(type);
 assertNull(defaultValue);
 }

 @Test
 public void testGetDefaultValueForPrimitiveType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), null);
 JavaType type = JavaTypeFactory.valueType(int.class);
 Object defaultValue = propertyBuilder.getDefaultValue(type);
 assertEquals(0, defaultValue);
 }

 @Test
 public void testGetDefaultValueForStringType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), null);
 JavaType type = JavaTypeFactory.valueType(String.class);
 Object defaultValue = propertyBuilder.getDefaultValue(type);
 assertEquals("", defaultValue);
 }

 @Test
 public void testGetDefaultBean() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), new BeanDescription(TestBean.class));
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test
 public void testGetDefaultBeanForNullInstance() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(SerializationConfigFactory.getEmptyConfig(), null);
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }

}