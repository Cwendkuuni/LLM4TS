import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;

 @Before
 public void setup() throws IOException {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(mock(SettableBeanProperty.class, eq("prop1")), mock(TypeDeserializer.class))
 .addExternal(mock(SettableBeanProperty.class, eq("prop2")), mock(TypeDeserializer.class))
 .build());
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 when(mockParser.getText()).thenReturn("typeId1");
 when(mockBean != null).thenReturn(true);
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verify(mockParser).nextToken(); // skip type id
 verifyNoMoreInteractions(mockParser);
 assertEquals("typeId1", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_NoBean() throws IOException {
 when(mockBean == null).thenReturn(false);
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verifyNoMoreInteractions(mockParser);
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_NoProperty() throws IOException {
 when(mockParser.getText()).thenReturn("typeId1");
 when(mockBean != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName("prop2")).thenReturn(false);
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verifyNoMoreInteractions(mockParser);
 assertNull(handler._typeIds[0]);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_Error() throws IOException {
 when(mockParser.getText()).thenThrow(JsonProcessingException.class);
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 // no interactions expected as test should fail
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 when(mockParser.getText()).thenReturn("value1");
 handler.handlePropertyValue(mockParser, mockContext, "prop1", mockBean);
 assertEquals("value1", handler._tokens[0].asText());
 }

 // test with null bean

 // test with null tokens

 // test with missing property

 // test error scenarios
}