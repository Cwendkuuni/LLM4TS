import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testObject;

 @Before
 public void setUp() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ExtTypedProperty prop1 = new SimplePOJO.StdSetter("prop1");
 ExtTypedProperty prop2 = new SimplePOJO.StdSetter("prop2");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(SimplePOJO.class);
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(prop1, typeDeserializer);
 builder.addExternal(prop2, typeDeserializer);
 handler = builder.build();
 testObject = new SimplePOJO();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 String propertyName = "prop1";
 JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"StringType\"}");
 Object bean = handler.start();
 handler.handleTypePropertyValue(parser, null, propertyName, bean);
 Assert.assertEquals("StringType", handler.getExtTypedProperty(propertyName).getProperty().getName());
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 String propertyName = "prop1";
 JsonParser parser = mapper.getFactory().createParser("{\"prop1\":\"value1\"}");
 Object bean = handler.start();
 handler.handlePropertyValue(parser, null, propertyName, bean);
 Assert.assertEquals("value1", handler.getExtTypedProperty(propertyName).getProperty().getValue(bean));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeIdProperty() throws IOException {
 String propertyName = "prop3";
 JsonParser parser = mapper.getFactory().createParser("{\"prop1\":\"value1\", \"prop2\":\"value2\"}");
 Object bean = handler.start();
 handler.handlePropertyValue(parser, null, propertyName, bean);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingPropertyForTypeId() throws IOException {
 String typeId = "MissingTypeId";
 JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}");
 Object bean = handler.start();
 handler.handleTypePropertyValue(parser, null, "prop1", bean);
 }

 @Test
 public void testComplete() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{" +
 "{\"prop1\":\"value1\"}," +
 "{\"prop2\":\"value2\"}" +
 "}");
 Object bean = handler.start();
 handler.complete(parser, null, testObject);
 Assert.assertEquals("value1", handler.getExtTypedProperty("prop1").getProperty().getValue(testObject));
 Assert.assertEquals("value2", handler.getExtTypedProperty("prop2").getProperty().getValue(testObject));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingExternalTypeId() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{" +
 "{\"prop1\":\"value1\"}" +
 "}");
 Object bean = handler.start();
 handler.complete(parser, null, testObject);
 }

 @Test
 public void testCompleteWithNullValue() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{" +
 "{\"prop1\":null}" +
 "}");
 Object bean = handler.start();
 handler.complete(parser, null, testObject);
 Assert.assertNull(handler.getExtTypedProperty("prop1").getProperty().getValue(testObject));
 }

 private ExtTypedProperty getExtTypedProperty(String propertyName) {
 for (ExtTypedProperty prop : handler._properties) {
 if (prop.getProperty().getName().equals(propertyName)) {
 return prop;
 }
 }
 return null;
 }
}