import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("typeId1", "value1");
 rootNode.put("property1", "value2");
 rootNode.put("typeId2", null);
 handler = mapper.readerForUpdating(ExternalTypeHandler.class)
 .withValueToBeReadAs(ExternalTypeHandler.class)
 .readValue(rootNode);
 }

 @Test
 public void testStart() throws Exception {
 ExternalTypeHandler newHandler = handler.start();
 assertNotNull(newHandler);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String typeId = "testTypeId";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object bean = new Object();
 handler.handleTypePropertyValue(mockParser, mockContext, "property1", bean);
 assertEquals("value1", handler._typeIds[0]);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueMissingProperty() throws Exception {
 String propName = "missingProperty";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object bean = new Object();
 handler.handleTypePropertyValue(mockParser, mockContext, propName, bean);
 fail("Should have thrown an IOException for missing property");
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueMissingTypeId() throws Exception {
 String propName = "typeIdProperty";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object bean = new Object();
 handler.handleTypePropertyValue(mockParser, mockContext, propName, bean);
 fail("Should have thrown an IOException for missing type id");
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 String propName = "property1";
 String expectedTypeId = "testTypeId";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object bean = new Object();
 handler.handlePropertyValue(mockParser, mockContext, propName, bean);
 assertEquals(expectedTypeId, handler._typeIds[0]);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueMissingProperty() throws Exception {
 String propName = "missingProperty";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object bean = new Object();
 handler.handlePropertyValue(mockParser, mockContext, propName, bean);
 fail("Should have thrown an IOException for missing property");
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueMissingTypeId() throws Exception {
 String propName = "property1";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object bean = new Object();
 handler.handlePropertyValue(mockParser, mockContext, propName, bean);
 fail("Should have thrown an IOException for missing type id");
 }

 @Test
 public void testCompleteWithValidTypeIds() throws Exception {
 Object expectedBean = new Object();
 handler.complete(mockParser, mockContext, expectedBean);
 assertEquals(expectedBean, handler.start().complete(mockParser, mockContext, expectedBean));
 }

 @Test
 public void testCompleteWithNullTypeId() throws Exception {
 Object expectedBean = new Object();
 handler._typeIds = new String[2];
 handler._tokens = new TokenBuffer[2];
 handler.complete(mockParser, mockContext, expectedBean);
 assertNull(handler._typeIds[0]);
 assertEquals("value2", handler._properties[0].getProperty().getValue(expectedBean));
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingTypeId() throws Exception {
 Object expectedBean = new Object();
 handler.complete(mockParser, mockContext, expectedBean);
 SettableBeanProperty prop = handler._properties[0].getProperty();
 fail("Should have thrown an IOException for missing type id");
 }

 @Test
 public void testCompleteWithNaturalType() throws Exception {
 Object expectedBean = new Object();
 JsonParser parser = mapper.getFactory().createParser("[1]");
 handler.complete(parser, mockContext, expectedBean);
 assertEquals(1, handler._properties[0].getProperty().getValue(expectedBean));
 }

 @Test
 public void testCompleteWithCreatorProperties() throws Exception {
 Object expectedBean = new Object();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("typeId1", "StringTypeDeserializer");
 rootNode.put("creatorProperty1", "value1");
 rootNode.put("property1", "value2");
 rootNode.put("typeId2", null);
 handler = mapper.readerForUpdating(ExternalTypeHandler.class)
 .withValueToBeReadAs(ExternalTypeHandler.class)
 .readValue(rootNode);
 ObjectNode beanNode = mapper.createObjectNode();
 beanNode.put("typeId1", "StringDeserializer");
 StringDeserializer creator = mapper.getTypeFactory().constructValueType(StringDeserializer.class);
 ValueInstantiator instantiator = mapper.getFactory()
 .getValueInstantiator()
 .findValueInstantiator(StringDeserializer.class, mapper.getTypeFactory());
 TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
 Creator creatorAnnotation = creator.getClass().getAnnotation(Creator.class);
 PropertyBasedCreator creatorBuilder = new PropertyBasedCreator() {
 @Override
 public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer)
 throws IOException {
 return instantiator.createValueInstance(creator.getClass());
 }
 };
 creatorBuilder.setCreatorAnnotation(creatorAnnotation);
 handler = handler.linkCreatorProperties(creatorBuilder);
 handler.complete(mockParser, mockContext, beanNode, creatorBuilder);
 assertEquals(creator, handler._properties[0].getProperty().getValue(expectedBean));
 }

 @Test
 public void testLinkTypeProperty() throws Exception {
 handler = new ExternalTypeHandler(new ExternalTypeHandler(handler), null, null, null);
 handler._properties[0].linkTypeProperty(handler._properties[1]);
 assertEquals(handler._properties[1].getTypePropertyName(), handler._properties[0]._typePropertyName);
 }
}