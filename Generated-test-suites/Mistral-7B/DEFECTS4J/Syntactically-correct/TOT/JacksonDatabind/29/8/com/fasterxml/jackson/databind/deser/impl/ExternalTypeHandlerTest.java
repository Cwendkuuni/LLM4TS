import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind.deser.StandardDeserializationContext.instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext context;
 private Object testBean;

 @Before
 public void setup() throws IOException {
 testBean = new Object();
 parser = mock(JsonParser.class);
 context = instance();

 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class)));
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
 new HashMap<>(), null, null);
 }

 @Test
 public void testHandleTypePropertyValueWithValidTypeId() throws IOException {
 // Given
 String typeId = "MyTypeId";
 when(parser.getText()).thenReturn(typeId);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("propertyName");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("value");

 // When
 handler.handleTypePropertyValue(parser, context, "propertyName", testBean);

 // Then
 // Check that typeId has been stored
 String expectedTypeId = typeId;
 String actualTypeId = handler._typeIds[0];
 assertEquals(expectedTypeId, actualTypeId);

 // Check that value has been deserialized and set
 String expectedValue = "value";
 Object actualValue = handler._properties[0].getProperty().get(testBean);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testHandleTypePropertyValueWithMissingProperty() throws IOException {
 // Given
 when(parser.getText()).thenReturn("missingPropertyName");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("missingPropertyName");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 // When
 handler.handleTypePropertyValue(parser, context, "missingPropertyName", testBean);

 // Then
 // Check that no typeId or value has been stored
 assertNull(handler._typeIds[0]);
 assertNull(handler._tokens[0]);
 }

 @Test
 public void testHandlePropertyValueWithValidValue() throws IOException {
 // Given
 when(parser.getText()).thenReturn("propertyName");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("propertyName");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("value");

 // When
 handler.handlePropertyValue(parser, context, "propertyName", testBean);

 // Then
 // Check that value has been buffered
 TokenBuffer expectedTokens = new TokenBuffer(parser);
 TokenBuffer actualTokens = handler._tokens[0];
 assertEquals(expectedTokens, actualTokens);

 // Check that no typeId has been stored
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testCompleteWithValidTypeIds() throws IOException {
 // Given
 String typeId1 = "Type1";
 String typeId2 = "Type2";
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn(typeId1);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("property2");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn(typeId2);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // When
 handler.complete(parser, context, testBean);

 // Then
 // Check that both typeIds have been deserialized and set
 String[] expectedTypeIds = {typeId1, typeId2};
 String[] actualTypeIds = handler._typeIds;
 assertArrayEquals(expectedTypeIds, actualTypeIds);

 // Check that both values have been deserialized and set
 Object[] expectedValues = {new Object(), new Object()};
 Object[] actualValues = new Object[2];
 for (int i = 0; i < 2; i++) {
 SettableBeanProperty property = handler._properties[i].getProperty();
 actualValues[i] = property.get(testBean);
 }
 assertArrayEquals(expectedValues, actualValues);
 }

 @Test(expected = MappingException.class)
 public void testCompleteWithMissingTypeId() throws IOException {
 // Given
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("Type1");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("property2");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // When
 // Throws MappingException as expected
 handler.complete(parser, context, testBean);
 }

 @Test(expected = MappingException.class)
 public void testCompleteWithMissingProperty() throws IOException {
 // Given
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("Type1");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("missingProperty");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // When
 // Throws MappingException as expected
 handler.complete(parser, context, testBean);
 }
}