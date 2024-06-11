import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext ctxt;
 private Object bean;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 handler = mapper.getFactory().createExternalTypeHandler(new PropertyName("testProperty"));
 parser = mapper.getFactory().createParser("<{\"testProperty\":\"String\"}");
 ctxt = mapper.getDeserializationContext();
 bean = new Object();
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String typeId = "StringType";
 handler.handleTypePropertyValue(parser, ctxt, "testProperty", bean);
 assertEquals("StringType", handler.getTypeIds()[0]);
 }

 @Test
 public void testHandleTypePropertyValue_NullBean() throws Exception {
 handler.handleTypePropertyValue(parser, ctxt, "testProperty", null);
 assertNull(handler.getTypeIds());
 }

 @Test
 public void testHandleTypePropertyValue_NoMatchingProperty() throws Exception {
 handler = mapper.getFactory().createExternalTypeHandler(new PropertyName("nonexistentProperty"));
 handler.handleTypePropertyValue(parser, ctxt, "testProperty", bean);
 assertFalse(handler.hasHandledProperty());
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 String value = "testValue";
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 assertEquals(value, handler.getProperty().get(bean));
 }

 @Test
 public void testHandlePropertyValue_NullBean() throws Exception {
 handler.handlePropertyValue(parser, ctxt, "testProperty", null);
 assertNull(handler.getProperty());
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_EmptyJson() throws Exception {
 parser.nextToken();
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_InvalidJson() throws Exception {
 parser.nextToken();
 parser.nextToken(); // skip START_OBJECT
 parser.nextToken(); // skip PROPERTY_NAME
 parser.nextToken(); // skip COLON
 parser.nextToken(); // skip VALUE_STRING
 parser.nextToken(); // skip COMMA
 parser.nextToken(); // skip KEYWORD_FALSE
 parser.nextToken(); // skip END_OBJECT
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 fail("Expected IOException");
 }

 @Test
 public void testComplete() throws Exception {
 String propertyValue = "propertyValue";
 String typeId = "TypeId";
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);
 handler.complete(parser, ctxt, bean);
 assertEquals(propertyValue, handler.getProperty().get(bean));
 assertEquals(typeId, handler.getTypeIds()[0]);
 }

 @Test(expected = IOException.class)
 public void testComplete_EmptyJson() throws Exception {
 parser.nextToken();
 parser.nextToken(); // skip START_OBJECT
 parser.nextToken(); // skip END_OBJECT
 handler.complete(parser, ctxt, bean);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingTypeId() throws Exception {
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 handler.complete(parser, ctxt, bean);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingProperty() throws Exception {
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);
 handler.complete(parser, ctxt, bean);
 fail("Expected IOException");
 }

 @Test
 public void testComplete_NaturalType() throws Exception {
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 handler.handlePropertyValue(parser, ctxt, "123", bean);
 handler.complete(parser, ctxt, bean);
 assertEquals(123, handler.getProperty().get(bean));
 }

 @Test
 public void testComplete_DefaultType() throws Exception {
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 handler.complete(parser, ctxt, bean);
 SettableBeanProperty property = handler.getProperty();
 assertEquals("defaultValue", property.getDefaultValue());
 assertEquals("DefaultType", property.getType().getName());
 }

 @Test
 public void testComplete_CreatorProperties() throws Exception {
 ObjectCreator creator = new MapObjectCreator(Object.class);
 handler = mapper.getFactory().createExternalTypeHandler(new PropertyName("testProperty"));
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);
 handler.handlePropertyValue(parser, ctxt, "creatorProperty", bean);
 Object result = handler.complete(parser, ctxt, null, creator);
 assertEquals(result.getClass(), Object.class);
 }

 @Test
 public void testComplete_CreatorTypeId() throws Exception {
 ObjectCreator creator = new MapObjectCreator(Object.class);
 handler = mapper.getFactory().createExternalTypeHandler(new PropertyName("testProperty"));
 handler.handlePropertyValue(parser, ctxt, "testProperty", bean);
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);
 handler.handlePropertyValue(parser, ctxt, "creatorTypeId", bean);
 Object result = handler.complete(parser, ctxt, null, creator);
 assertEquals(result.getClass(), String.class);
 }

}