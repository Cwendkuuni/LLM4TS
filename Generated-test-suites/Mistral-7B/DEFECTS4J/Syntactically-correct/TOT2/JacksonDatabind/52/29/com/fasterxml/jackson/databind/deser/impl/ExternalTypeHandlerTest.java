import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.instance;
import static com.fasterxml.jackson.databind.node.TextNode.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectNode node;
 private JsonParser parser;
 private DeserializationContext ctxt;

 @Before
 public void setup() throws Exception {
 ctxt = mock(DeserializationContext.class);
 node = instance.objectNode();
 parser = new JsonParser(node);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(
 mock(SettableBeanProperty.class), mock(TypeDeserializer.class)).build());
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String typeId = "someTypeId";
 String propName = "typePropertyName";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 Object bean = new Object();
 when(property.getName()).thenReturn(propName);
 when(property.getType()).thenReturn(String.class);
 when(property.getPropertyName()).thenReturn("typePropertyName");
 when(property.hasTypePropertyName(propName)).thenReturn(true);
 handler.handleTypePropertyValue(parser, ctxt, propName, bean);
 verify(ctxt).reportMappingException(eq("Missing property 'typePropertyName' for external type id 'someTypeId'"));

 // happy path
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(
 property, mock(TypeDeserializer.class)).build());
 handler.handleTypePropertyValue(parser, ctxt, propName, bean);
 verifyNoMoreInteractions(ctxt);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 String propName = "propertyName";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 Object bean = new Object();
 when(property.getName()).thenReturn(propName);
 when(property.getType()).thenReturn(String.class);
 handler.handlePropertyValue(parser, ctxt, propName, bean);
 verify(ctxt).reportMappingException(eq("Missing property 'propertyName' for external type id 'null'"));

 // happy path
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(
 property, mock(TypeDeserializer.class)).build());
 String typeId = "someTypeId";
 parser.nextToken(JsonToken.START_OBJECT);
 parser.nextToken(JsonToken.FIELD_NAME);
 parser.nextToken(JsonToken.VALUE_STRING);
 parser.nextToken(JsonToken.VALUE_NULL);
 parser.nextToken(JsonToken.END_OBJECT);
 handler.handlePropertyValue(parser, ctxt, propName, bean);
 verifyNoMoreInteractions(ctxt);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWhenParserThrowsException() throws Exception {
 String propName = "propertyName";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 Object bean = new Object();
 when(property.getName()).thenReturn(propName);
 when(property.getType()).thenReturn(String.class);
 parser.nextToken(JsonToken.START_OBJECT);
 parser.nextToken(JsonToken.FIELD_NAME);
 parser.nextToken(JsonToken.VALUE_STRING);
 parser.nextToken(JsonToken.VALUE_NULL);
 parser.nextToken(JsonToken.FIELD_NAME);
 parser.nextToken(JsonToken.VALUE_NULL);
 parser.nextToken(JsonToken.END_OBJECT);
 parser.nextToken(JsonToken.VALUE_STRING);
 handler.handlePropertyValue(parser, ctxt, propName, bean);
 }

 @Test
 public void testComplete() throws Exception {
 Object bean = new Object();
 handler.complete(parser, ctxt, bean);
 // verify no interactions with ctxt
 }

 @Test
 public void testCompleteWithTypeId() throws Exception {
 Object bean = new Object();
 String typeId = "someTypeId";
 node.put("typeId", typeId);
 parser.setCurrentLocation(parser.getCurrentLocation());
 parser.nextToken(JsonToken.START_OBJECT);
 handler.complete(parser, ctxt, bean);
 // verify interactions with ctxt
 verify(ctxt).reportMappingException(eq("Missing property 'typePropertyName' for external type id 'someTypeId'"));
 }

 @Test(expected = IOException.class)
 public void testCompleteWhenParserThrowsException() throws Exception {
 Object bean = new Object();
 parser.nextToken(JsonToken.START_OBJECT);
 handler.complete(parser, ctxt, bean);
 }

 @Test
 public void testCompleteWithNullBean() throws Exception {
 handler.complete(parser, ctxt, null);
 // no interactions with ctxt
 }

 @Test
 public void testCompleteWithNullTypeId() throws Exception {
 Object bean = new Object();
 handler.complete(parser, ctxt, bean);
 // no interactions with ctxt
 }

 @Test
 public void testCompleteWithNullTokens() throws Exception {
 Object bean = new Object();
 String typeId = "someTypeId";
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(
 mock(SettableBeanProperty.class), mock(TypeDeserializer.class)).build());
 handler.complete(parser, ctxt, bean);
 // verify no interactions with ctxt
 }

 @Test
 public void testCompleteWithMissingProperty() throws Exception {
 Object bean = new Object();
 String typeId = "someTypeId";
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(
 mock(SettableBeanProperty.class), mock(TypeDeserializer.class)).build());
 parser.nextToken(JsonToken.START_OBJECT);
 parser.nextToken(JsonToken.FIELD_NAME);
 parser.nextToken(JsonToken.VALUE_STRING);
 parser.nextToken(JsonToken.VALUE_NULL);
 parser.nextToken(JsonToken.END_OBJECT);
 handler.complete(parser, ctxt, bean);
 // verify interactions with ctxt
 verify(ctxt).reportMappingException(eq("Missing property 'propertyName' for external type id 'null'"));
 }

 @Test
 public void testCompleteWithNullValue() throws Exception {
 Object bean = new Object();
 String typeId = "someTypeId";
 node.put("typeId", typeId);
 parser.setCurrentLocation(parser.getCurrentLocation());
 parser.nextToken(JsonToken.START_OBJECT);
 parser.nextToken(JsonToken.FIELD_NAME);
 parser.nextToken(JsonToken.VALUE_STRING);
 parser.nextToken(JsonToken.VALUE_NULL);
 parser.nextToken(JsonToken.END_OBJECT);
 handler.complete(parser, ctxt, bean);
 // verify no interactions with ctxt
 }
}