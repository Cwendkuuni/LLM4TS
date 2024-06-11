import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private CreatorProperty property;
 private ObjectBean context;
 private Object beanInstance;

 @Before
 public void setup() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 property = new CreatorProperty(PropertyName.construct("testProperty"),
 JavaType.valueOf(String.class), null, null, null, null, 0, null, PropertyMetadata.STD_OBJ);
 context = mapper.getDeserializationContext();
 beanInstance = new Object();
 }

 @Test
 public void testBasicFunctionality() throws IOException {
 String expectedValue = "testValue";
 property.setValueDeserializer(new StandardDeserializer<>());
 property.deserializeAndSet(context.createParser(expectedValue), context, beanInstance);
 assertEquals(expectedValue, property.get(beanInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectableValueId() throws IOException {
 property.markAsIgnorable();
 property.inject(context, beanInstance);
 }

 @Test
 public void testInjectValue() throws IOException {
 String injectedValue = "injectedValue";
 Object injectableValueId = new Object();
 property.setInjectableValueId(injectableValueId);
 when(context.findInjectableValue(any(), any(), any())).thenReturn(injectedValue);
 property.inject(context, beanInstance);
 assertEquals(injectedValue, property.get(beanInstance));
 }

 @Test(expected = IOException.class)
 public void testDeserializationFailure() throws IOException {
 property.setValueDeserializer(new FailingDeserializer());
 property.deserializeAndSet(context.createParser("failedDeserialization"), context, beanInstance);
 }

 @Test
 public void testSetValue() throws IOException {
 String valueToSet = "valueToSet";
 property.set(beanInstance, valueToSet);
 assertEquals(valueToSet, property.get(beanInstance));
 }

 @Test
 public void testSetValueAndReturn() throws IOException {
 String valueToSetAndReturn = "valueToSetAndReturn";
 Object returnedValue = new Object();
 when(property.setAndReturn(beanInstance, valueToSetAndReturn)).thenReturn(returnedValue);
 assertEquals(returnedValue, property.setAndReturn(beanInstance, valueToSetAndReturn));
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingFallbackSetter() throws IOException {
 property.markAsIgnorable();
 property.setFallbackSetter(null);
 property.inject(context, beanInstance);
 }

 @Test
 public void testFallbackSetterFunctionality() throws IOException {
 String fallbackValue = "fallbackValue";
 SettableBeanProperty fallbackSetter = new SettableBeanProperty(PropertyName.construct("fallbackProperty"),
 JavaType.valueOf(String.class), null, null, null, null, 0, null, PropertyMetadata.STD_OBJ);
 property.setFallbackSetter(fallbackSetter);
 fallbackSetter.set(beanInstance, fallbackValue);
 property.inject(context, beanInstance);
 assertEquals(fallbackValue, property.get(beanInstance));
 }
}

class FailingDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new IOException("Deserialization failure");
 }
}