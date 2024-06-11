import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.StdDeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext ctx;
 private Object bean;

 @Before
 public void setup() throws Exception {
 ExtTypedProperty prop1 = new ExtTypedProperty(new SettableBeanProperty(new Object(), "prop1"), new TypeDeserializer() {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "value1";
 }

 @Override
 public String getPropertyName() {
 return "type1";
 }
 });
 ExtTypedProperty prop2 = new ExtTypedProperty(new SettableBeanProperty(new Object(), "prop2"), new TypeDeserializer() {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new IOException("Test exception");
 }

 @Override
 public String getPropertyName() {
 return "type2";
 }
 });

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop1, prop2 }, new HashMap<>(), null, null);
 parser = mock(JsonParser.class);
 ctx = mock(DeserializationContext.class);
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenAnswer(invocation -> invocation.getArgument(0).getText());
 when(ctx.findBeanProperty(prop1.getTypePropertyName())).thenReturn(prop1.getProperty());
 when(ctx.findBeanProperty(prop2.getTypePropertyName())).thenReturn(prop2.getProperty());
 when(ctx.findCreator(prop1.getTypePropertyName())).thenReturn(null);
 bean = new Object();
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 when(parser.getText()).thenReturn("type1");
 handler.handleTypePropertyValue(parser, ctx, "prop1", bean);
 assertEquals("value1", prop1.getProperty().get(bean));
 assertNull(parser.getCurrentName());
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueWithException() throws Exception {
 when(parser.getText()).thenReturn("type2");
 handler.handleTypePropertyValue(parser, ctx, "prop2", bean);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 when(parser.getText()).thenReturn("value1");
 handler.handlePropertyValue(parser, ctx, "prop1", bean);
 assertEquals("value1", prop1.getProperty().get(bean));
 assertNull(parser.getCurrentName());
 }

 @Test
 public void testHandlePropertyValueWithTypeId() throws Exception {
 when(parser.getText()).thenReturn("value2");
 handler.handlePropertyValue(parser, ctx, "prop1", bean);
 assertNull(parser.getText());
 assertEquals("value2", prop1.getProperty().get(bean));
 }

 @Test
 public void testHandlePropertyValueWithCreator() throws Exception {
 PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
 when(ctx.findCreator(prop1.getTypePropertyName())).thenReturn(creator);
 when(creator.build(ctx, null)).thenReturn(new Object());
 handler.handlePropertyValue(parser, ctx, "prop1", bean);
 assertEquals(new Object(), bean);
 }

 @Test
 public void testComplete() throws Exception {
 when(parser.getText()).thenReturn("type1:value1");
 handler.complete(parser, ctx, bean);
 assertEquals("value1", prop1.getProperty().get(bean));
 }

 @Test
 public void testCompleteWithNullTypeId() throws Exception {
 when(parser.getText()).thenReturn("null");
 handler.complete(parser, ctx, bean);
 // no assertions here: test allows null values for some properties
 }

 @Test
 public void testCompleteWithException() throws Exception {
 when(parser.getText()).thenReturn("type2");
 handler.complete(parser, ctx, bean);
 assertNull(parser.getCurrentName());
 }
}