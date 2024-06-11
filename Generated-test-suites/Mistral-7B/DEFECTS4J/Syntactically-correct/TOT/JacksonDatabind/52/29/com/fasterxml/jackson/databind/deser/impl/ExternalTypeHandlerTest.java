import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExternalTypeHandlerTest {

 private ObjectMapper mapper;
 private ExternalTypeHandler handler;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 Builder builder = new Builder();
 SettableBeanProperty prop = new SettableBeanProperty("name", String.class);
 TypeDeserializer td = mapper.getTypeDeserializer(String.class);
 builder.addExternal(prop, td);
 handler = builder.build();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "StringType");
 JsonParser p = mapper.treeAsTokens(node);
 DeserializationContext context = mapper.getDeserializationContext();
 Object bean = new Object();
 Object result = handler.handleTypePropertyValue(p, context, "typeId", bean);
 assertEquals("StringType", result);
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "John Doe");
 JsonParser p = mapper.treeAsTokens(node);
 DeserializationContext context = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, context, "name", bean);
 assertEquals("John Doe", handler.getProperties()[0].getProperty().get(bean));
 }

 @Test
 public void testHandleMissingPropertyValue() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 JsonParser p = mapper.treeAsTokens(node);
 DeserializationContext context = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, context, "nonExistentProperty", bean);
 assertNull(handler.getProperties()[0].getProperty().get(bean));
 }

 @Test
 public void testHandleMissingTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "John Doe");
 JsonParser p = mapper.treeAsTokens(node);
 DeserializationContext context = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, context, "typeId", bean);
 assertNull(handler.getProperties()[0].getProperty().get(bean));
 }

 @Test
 public void testComplete() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "StringType");
 node.put("name", "John Doe");
 JsonParser p = mapper.treeAsTokens(node);
 DeserializationContext context = mapper.getDeserializationContext();
 Object bean = new Object();
 Object result = handler.complete(p, context, bean);
 assertEquals("John Doe", handler.getProperties()[0].getProperty().get(bean));
 assertEquals("StringType", handler.getProperties()[0].getTypePropertyName());
 }

 @Test
 public void testCompleteWithCreator() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "CustomType");
 node.put("creatorProperty", "CreatorObject");
 JsonParser p = mapper.treeAsTokens(node);
 DeserializationContext context = mapper.getDeserializationContext();
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override public Object createUsingGuid(DeserializationContext ctxt, PropertyName propertyName) {
 return "CreatedObject";
 }};
 Object result = handler.complete(p, context, buffer, creator);
 assertEquals("CreatedObject", handler.getProperties()[0].getProperty().get(result));
 assertEquals("CustomType", handler.getProperties()[0].getTypePropertyName());
 }

 @Test
 public void testCompleteWithMissingTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "John Doe");
 JsonParser p = mapper.treeAsTokens(node);
 DeserializationContext context = mapper.getDeserializationContext();
 Object bean = new Object();
 try {
 handler.complete(p, context, bean);
 fail("Expected MappingException");
 } catch (MappingException e) {
 assertEquals("Missing external type id property 'typeId'", e.getMessage());
 }
 }
}