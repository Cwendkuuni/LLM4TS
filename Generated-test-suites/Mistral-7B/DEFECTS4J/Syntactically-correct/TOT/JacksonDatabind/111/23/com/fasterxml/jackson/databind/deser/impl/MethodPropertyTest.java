import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

 private ObjectInstanceFactory instanceFactory;
 private ObjectMapper objectMapper;
 private MethodProperty property;

 @Before
 public void setup() throws Exception {
 Object testInstance = new TestClass();
 instanceFactory = new ObjectMapper().findValueSerializer(TestClass.class);
 objectMapper = new ObjectMapper();
 property = new MethodProperty(new BeanPropertyDefinition(TestClass.class, "testProperty"),
 ObjectTypeFactories.defaultInstance(), null, null, new AnnotatedMethod(TestClass.class.getMethod("setTestProperty")));
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser parser = objectMapper.getFactory().createParser("{\"testProperty\":1}");
 Object instance = instanceFactory.findValue(parser, TestClass.class);
 property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), instance);
 assertEquals(1, instance.getClass().getMethod("getTestProperty").invoke(instance));
 }

 @Test
 public void testDeserializeAndSetNull() throws Exception {
 JsonParser parser = objectMapper.getFactory().createParser("{\"testProperty\":null}");
 Object instance = instanceFactory.findValue(parser, TestClass.class);
 property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), instance);
 assertNull(instance.getClass().getMethod("getTestProperty").invoke(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 JsonParser parser = objectMapper.getFactory().createParser("{\"testProperty\":1}");
 Object originalInstance = instanceFactory.findValue(parser, TestClass.class);
 Object result = property.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), originalInstance);
 assertEquals(result, originalInstance);
 assertEquals(1, result.getClass().getMethod("getTestProperty").invoke(result));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws Exception {
 JsonParser parser = objectMapper.getFactory().createParser("{\"testProperty\":null}");
 Object originalInstance = instanceFactory.findValue(parser, TestClass.class);
 Object result = property.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), originalInstance);
 assertNotNull(result);
 assertNull(result.getClass().getMethod("getTestProperty").invoke(result));
 assertEquals(originalInstance, instanceFactory.findValue(parser, TestClass.class));
 }

 @Test
 public void testSet() throws Exception {
 Object instance = new TestClass();
 property.set(instance, 1);
 assertEquals(1, instance.getClass().getMethod("getTestProperty").invoke(instance));
 }

 @Test
 public void testSetNull() throws Exception {
 Object instance = new TestClass();
 property.set(instance, null);
 assertNull(instance.getClass().getMethod("getTestProperty").invoke(instance));
 }

 @Test
 public void testFixAccess() throws Exception {
 AnnotatedMember annotatedMember = new AnnotatedMethod(TestClass.class.getDeclaredMethod("setTestProperty"));
 annotatedMember.setAccessible(true);
 MethodProperty property = new MethodProperty(new BeanPropertyDefinition(TestClass.class, "testProperty"),
 ObjectTypeFactories.defaultInstance(), null, null, annotatedMember);
 property.fixAccess(objectMapper.getDeserializationConfig());
 assertFalse(annotatedMember.isAccessible());
 }

 @Test
 public void testGetAnnotation() {
 MethodProperty property = new MethodProperty(new BeanPropertyDefinition(TestClass.class, "testProperty"),
 ObjectTypeFactories.defaultInstance(), null, null, new AnnotatedMethod(TestClass.class.getMethod("setTestProperty"))) {
 @Override
 public <A extends Annotation> A getAnnotation(Class<A> acls) {
 return (A) new TestAnnotation();
 }
 };
 assertTrue(property.getAnnotation(TestAnnotation.class) instanceof TestAnnotation);
 }

 @Test
 public void testInvokeSetterWithNullValueDeserializer() throws Exception {
 Object instance = new TestClass();
 JsonParser parser = objectMapper.getFactory().createParser("null");
 property = new MethodProperty(new BeanPropertyDefinition(TestClass.class, "testProperty"),
 ObjectTypeFactories.defaultInstance(), null, null, new AnnotatedMethod(TestClass.class.getMethod("setTestProperty")));
 property.withValueDeserializer(null);
 property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), instance);
 assertNull(instance.getClass().getMethod("getTestProperty").invoke(instance));
 }

 @Test(expected = IOException.class)
 public void testInvokeSetterWithNullValue() throws Exception {
 Object instance = new TestClass();
 property = new MethodProperty(new BeanPropertyDefinition(TestClass.class, "testProperty"),
 ObjectTypeFactories.defaultInstance(), null, null, new AnnotatedMethod(TestClass.class.getMethod("setTestProperty")));
 property.deserializeAndSet(objectMapper.getFactory().createParser("null"), objectMapper.getDeserializationContext(), instance);
 }

 @Test
 public void testInvokeSetterWithTypeDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 JsonParser parser = objectMapper.getFactory().createParser("1");
 property = new MethodProperty(new BeanPropertyDefinition(TestClass.class, "testProperty"),
 ObjectTypeFactories.defaultInstance(), typeDeserializer, null, new AnnotatedMethod(TestClass.class.getMethod("setTestProperty")));
 property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new TestClass());
 assertEquals(1, new TestClass().getClass().getMethod("getTestProperty").invoke(new TestClass()));
 }

 @Test
 public void testInvokeSetterWithEmptyJsonParser() throws Exception {
 property = new MethodProperty(new BeanPropertyDefinition(TestClass.class, "testProperty"),
 ObjectTypeFactories.defaultInstance(), null, null, new AnnotatedMethod(TestClass.class.getMethod("setTestProperty")));
 property.deserializeAndSet(objectMapper.getFactory().createParser(""), objectMapper.getDeserializationContext(), new TestClass());
 assertNull(new TestClass().getClass().getMethod("getTestProperty").invoke(new TestClass()));
 }
}

class TestAnnotation {}
class TestClass {
 private int testProperty;
 public int getTestProperty() {
 return testProperty;
 }
 public void setTestProperty(int testProperty) {
 this.testProperty = testProperty;
 }
}