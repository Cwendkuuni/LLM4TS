import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodPropertyTest {

 private ObjectMapper mapper;
 private SettableBeanProperty property;
 private Method testMethod;
 private Object testInstance;

 @Before
 public void setup() {
 mapper = JsonMapperFactory.getMapper();
 testMethod = Mockito.mock(Method.class);
 testInstance = new Object();
 try {
 property = new MethodProperty(
 new BeanPropertyDefinition(testMethod.getName(), testMethod.getGenericReturnType(), null),
 mapper.getTypeFactory().constructRawType(testInstance.getClass()),
 null,
 Annotations.empty(),
 testMethod);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @After
 public void teardown() {
 mapper = null;
 property = null;
 testInstance = null;
 testMethod = null;
 }

 @Test
 public void testSetAndGet() throws Exception {
 Object value = new Object();
 property.set(testInstance, value);
 assertEquals(value, testInstance.getClass().getDeclaredMethod(testMethod.getName()).invoke(testInstance));
 }

 @Test
 public void testSetNull() throws Exception {
 property.set(testInstance, null);
 assertNull(testInstance.getClass().getDeclaredMethod(testMethod.getName()).invoke(testInstance));
 }

 @Test
 public void testSetAndReturnNull() throws Exception {
 when(testMethod.invoke(testInstance, any())).then(invocation -> null);
 assertNull(property.setAndReturn(testInstance, new Object()));
 }

 @Test
 public void testSetAndReturnNonnull() throws Exception {
 Object returnedValue = new Object();
 when(testMethod.invoke(testInstance, any())).thenReturn(returnedValue);
 assertEquals(returnedValue, property.setAndReturn(testInstance, new Object()));
 }

 @Test
 public void testDeserializeNull() throws Exception {
 JsonParser parser = mapper.getFactory().createParser(new StringReader("null"));
 DeserializationContext context = mock(DeserializationContext.class);
 property.deserializeAndSet(parser, context, testInstance);
 assertNull(testInstance.getClass().getDeclaredField("_value").get(testInstance));
 }

 @Test
 public void testDeserializeValue() throws Exception {
 ObjectMapper mapperWithSetters = mapper.copy();
 mapperWithSetters.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapperWithSetters.setSerializationInclusion(JsonInclude.Include.ALWAYS);
 mapperWithSetters.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
 mapperWithSetters.setNameTransformer(new NameTransformer() {
 @Override
 public String transformSerializationKey(String key) {
 return "test_" + key;
 }

 @Override
 public String transformDeserializationPropertyName(String name) {
 return "test_" + name;
 }
 });
 JsonParser parser = mapperWithSetters.getFactory().createParser(
 new ByteArrayInputStream(("{\"test_" + testMethod.getName() + "\": 123}").getBytes()));
 DeserializationContext context = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(context.findBeanDeserializer(testInstance.getClass())).thenReturn(null);
 when(context.findValueDeserializer(mapperWithSetters.getTypeFactory()
 .constructType(int.class), testInstance.getClass()))
 .thenReturn(mock(JsonDeserializer.class));
 when(context.findValueDeserializer(mapperWithSetters.getTypeFactory()
 .constructType(String.class), testInstance.getClass()))
 .thenReturn(mapperWithSetters.getFactory().findValueDeserializer(JsonToken.VALUE_STRING, String.class));
 when(typeDeserializer.deserialize(parser, context)).thenReturn(123);
 property.setValueDeserializer(mapperWithSetters.getFactory().findValueDeserializer(JsonToken.VALUE_NUMBER_INT, int.class));
 property.deserializeAndSet(parser, context, testInstance);
 assertEquals(123, testInstance.getClass().getDeclaredField("_value").get(testInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidValue() throws Exception {
 JsonParser parser = mapper.getFactory().createParser(new StringReader("{\"test\": \"invalid\"}"));
 DeserializationContext context = mock(DeserializationContext.class);
 property.deserializeAndSet(parser, context, testInstance);
 }
}