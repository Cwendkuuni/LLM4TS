import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper mapper;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 JsonFactory jsonFactory = mapper.getFactory();
 String jsonString = "{\"name\": \"John\", \"typeId\": \"Person\", \"age\": 30}";
 ObjectNode rootNode = mapper.readValue(jsonFactory.createParser(jsonString), ObjectNode.class);
 handler = new ExternalTypeHandler(mapper.readerForMap(rootNode), null, null, null);
 }

 /**
 * Testing handleTypePropertyValue method with valid type id and property
 */
 @Test
 public void testHandleTypePropertyValue_Valid() throws IOException {
 String propName = "typeId";
 String expectedTypeId = "Person";
 Object bean = new Object();
 boolean result = handler.handleTypePropertyValue(mapper.getFactory().createParser("{\"typeId\": \"Person\"}"), mapper, propName, bean);
 Assert.assertTrue(result);
 Assert.assertEquals(expectedTypeId, handler.getTypeIds()[0]);
 }

 /**
 * Testing handleTypePropertyValue method with invalid type id and property
 */
 @Test
 public void testHandleTypePropertyValue_Invalid() throws IOException {
 String propName = "age";
 Object bean = new Object();
 boolean result = handler.handleTypePropertyValue(mapper.getFactory().createParser("{\"typeId\": \"Invalid\"}"), mapper, propName, bean);
 Assert.assertFalse(result);
 }

 /**
 * Testing handlePropertyValue method with valid property
 */
 @Test
 public void testHandlePropertyValue_Valid() throws IOException {
 String propName = "name";
 Object bean = new Object();
 String expectedValue = "John";
 handler.handlePropertyValue(mapper.getFactory().createParser("{\"name\": \"John\"}"), mapper, propName, bean);
 Assert.assertEquals(expectedValue, handler.getTokens()[0].getText());
 }

 /**
 * Testing handlePropertyValue method with invalid property
 */
 @Test
 public void testHandlePropertyValue_Invalid() throws IOException {
 String propName = "typeId";
 Object bean = new Object();
 handler.handlePropertyValue(mapper.getFactory().createParser("{\"name\": \"John\"}"), mapper, propName, bean);
 Assert.assertNull(handler.getTypeIds()[0]);
 }

 /**
 * Testing complete method with valid type ids and properties
 */
 @Test
 public void testComplete_Valid() throws IOException {
 Object bean = new Object();
 handler.complete(mapper.getFactory().createParser("{}"), mapper, bean);
 String expectedName = "John";
 String expectedTypeId = "Person";
 String expectedAge = "30";
 Assert.assertEquals(expectedName, handler.getProperties()[0].getProperty().getName());
 Assert.assertEquals(expectedTypeId, handler.getTypeIds()[0]);
 Assert.assertEquals(expectedAge, handler.getTokens()[0].getText());
 }

 /**
 * Testing complete method with missing type ids
 */
 @Test(expected = JsonMappingException.class)
 public void testComplete_MissingTypeIds() throws IOException {
 Object bean = new Object();
 handler.complete(mapper.getFactory().createParser("{}"), mapper, bean);
 }

 /**
 * Testing complete method with missing properties
 */
 @Test(expected = JsonMappingException.class)
 public void testComplete_MissingProperties() throws IOException {
 Object bean = new Object();
 handler.complete(mapper.getFactory().createParser("{\"name\": \"John\"}"), mapper, bean);
 }
}