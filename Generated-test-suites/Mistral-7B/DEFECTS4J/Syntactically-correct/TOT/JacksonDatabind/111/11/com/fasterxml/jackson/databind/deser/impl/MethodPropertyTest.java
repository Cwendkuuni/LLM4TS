import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MethodPropertyTest {

 private ObjectMapper objectMapper;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private MethodProperty methodProperty;

 @BeforeAll
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 jsonParser = objectMapper.getFactory().createParser("{}");
 deserializationContext = Mockito.mock(DeserializationContext.class);
 MethodProperty.NullValueProvider nullValueProvider = new MethodProperty.NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return "nullValue";
 }
 };
 methodProperty = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedClass(String.class), new PropertyName("testProperty"), null),
 javaTypeOf(String.class), null, Annotations.empty(),
 Mockito.mock(AnnotatedMethod.class), nullValueProvider);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object instance = new Object();
 String expectedValue = "expectedValue";
 jsonParser.nextValue();
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals(expectedValue, instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object instance = new Object();
 String expectedValue = "expectedValue";
 jsonParser.nextValue();
 Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertEquals(expectedValue, result);
 assertEquals(expectedValue, instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testNullValue() throws Exception {
 Object instance = new Object();
 methodProperty._skipNulls = true;
 jsonParser.nextValue();
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertNull(instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testDeserializeWithType() throws Exception {
 ObjectMapper objectMapperWithType = new ObjectMapper();
 TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
 ObjectNode expectedNode = objectMapperWithType.createObjectNode();
 expectedNode.put("type", "MyType");
 methodProperty = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedClass(String.class), new PropertyName("testProperty"), null),
 javaTypeOf(String.class), null, Annotations.empty(),
 Mockito.mock(AnnotatedMethod.class), nullValueProvider, typeDeserializer);
 JsonParser jsonParserWithType = objectMapperWithType.getFactory().createParser(expectedNode.toString());
 Object instance = new Object();
 String expectedValue = "expectedValue";
 when(typeDeserializer.deserialize(jsonParserWithType, deserializationContext, javaTypeOf(MyType.class)))
 .thenReturn(new MyType());
 methodProperty.deserializeAndSet(jsonParserWithType, deserializationContext, instance);
 assertEquals(new MyType(), instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testSet() throws Exception {
 Object instance = new Object();
 String expectedValue = "expectedValue";
 methodProperty.set(instance, expectedValue);
 assertEquals(expectedValue, instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 String expectedValue = "expectedValue";
 Object result = methodProperty.setAndReturn(instance, expectedValue);
 assertEquals(expectedValue, result);
 assertEquals(expectedValue, instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testValueDeserializer() throws Exception {
 JsonDeserializer<String> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 methodProperty = new MethodProperty(methodProperty, jsonDeserializer, nullValueProvider);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("returnValue");
 Object instance = new Object();
 String expectedValue = "expectedValue";
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals("returnValue", instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testValueDeserializerWithType() throws Exception {
 TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
 JsonDeserializer<String> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 methodProperty = new MethodProperty(methodProperty, jsonDeserializer, nullValueProvider, typeDeserializer);
 when(jsonDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer)).thenReturn("returnValue");
 Object instance = new Object();
 String expectedValue = "expectedValue";
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals("returnValue", instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testFixAccess() throws Exception {
 methodProperty.fixAccess(objectMapper.getDeserializationConfig());
 assertTrue(methodProperty.getMember().isAccessible());
 }

 @Test
 public void testAnnotatedMethodGetAnnotations() throws Exception {
 Annotation[] expectedAnnotations = new Annotation[] {Mockito.mock(Annotation.class)};
 AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
 when(annotatedMethod.getAnnotations()).thenReturn(expectedAnnotations);
 methodProperty = new MethodProperty(methodProperty, annotatedMethod);
 assertEquals(expectedAnnotations, methodProperty.getMember().getAnnotations());
 }

 @Test
 public void testInvoke() throws Exception {
 Object instance = new Object();
 String expectedValue = "expectedValue";
 methodProperty.set(instance, expectedValue);
 Method setter = methodProperty.getSetter();
 assertEquals(expectedValue, setter.invoke(instance, null));
 }

 @Test
 public void testInvokeWithType() throws Exception {
 Object instance = new Object();
 MyType expectedValue = new MyType();
 methodProperty.set(instance, expectedValue);
 Method setter = methodProperty.getSetter();
 Type setterType = setter.getParameterTypes()[0].getType();
 assertEquals(expectedValue, setter.invoke(instance, javaTypeOf(MyType.class)));
 }

 @Test
 public void testInvokeWithArgumentException() throws Exception {
 Object instance = new Object();
 methodProperty.set(instance, new Object());
 Method setter = methodProperty.getSetter();
 assertThrows(IllegalArgumentException.class, () -> setter.invoke(instance, new Object()[0]));
 }

 @After
 public void teardown() throws Exception {
 jsonParser.close();
 deserializationContext = null;
 objectMapper = null;
 methodProperty = null;
 }
}