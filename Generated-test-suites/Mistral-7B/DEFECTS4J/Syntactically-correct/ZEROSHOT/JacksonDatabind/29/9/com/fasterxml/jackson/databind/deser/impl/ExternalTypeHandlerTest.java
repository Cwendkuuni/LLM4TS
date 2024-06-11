import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext context;
 private Object rootObject;

 @Before
 public void setUp() throws Exception {
 parser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 rootObject = new Object();
 }

 @After
 public void tearDown() throws Exception {
 handler = null;
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 ExtTypedProperty property1 = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 ExtTypedProperty property2 = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(property1);
 properties.add(property2);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 nameToPropertyIndex.put("property2", 1);
 String[] typeIds = new String[2];
 TokenBuffer[] tokens = new TokenBuffer[2];
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, typeIds, tokens);

 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("typeId1");

 handler.handleTypePropertyValue(parser, context, "property1", rootObject);
 assertEquals("typeId1", typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_missingProperty() throws Exception {
 ExtTypedProperty property = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(property);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 String[] typeIds = new String[1];
 TokenBuffer[] tokens = new TokenBuffer[1];
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, typeIds, tokens);

 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenReturn("nonexistentProperty");

 handler.handleTypePropertyValue(parser, context, "nonexistentProperty", rootObject);
 assertEquals(0, typeIds.length);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 ExtTypedProperty property1 = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 ExtTypedProperty property2 = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(property1);
 properties.add(property2);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 nameToPropertyIndex.put("property2", 1);
 String[] typeIds = new String[2];
 TokenBuffer[] tokens = new TokenBuffer[2];
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, typeIds, tokens);

 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("typeId1");

 handler.handlePropertyValue(parser, context, "property1", rootObject);
 assertEquals("typeId1", typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue_missingTypeId() throws Exception {
 ExtTypedProperty property = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(property);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 String[] typeIds = new String[1];
 TokenBuffer[] tokens = new TokenBuffer[1];
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, typeIds, tokens);

 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 handler.handlePropertyValue(parser, context, "property1", rootObject);
 assertEquals(0, typeIds.length);
 }

 @Test
 public void testComplete() throws Exception {
 ExtTypedProperty property1 = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 ExtTypedProperty property2 = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(property1);
 properties.add(property2);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 nameToPropertyIndex.put("property2", 1);
 String[] typeIds = new String[2];
 TokenBuffer[] tokens = new TokenBuffer[2];
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, typeIds, tokens);

 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("typeId1");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenReturn("property2");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(parser, context, rootObject);
 assertEquals("typeId1", typeIds[0]);
 assertEquals(null, typeIds[1]);
 }

 @Test(expected = JsonMappingException.class)
 public void testComplete_missingTypeId_withType() throws Exception {
 ExtTypedProperty property = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(property);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 String[] typeIds = new String[1];
 TokenBuffer[] tokens = new TokenBuffer[1];
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, typeIds, tokens);

 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.getText()).thenReturn("property1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 handler.complete(parser, context, rootObject);
 }

 @Test(expected = JsonMappingException.class)
 public void testComplete_missingProperty() throws Exception {
 ExtTypedProperty property = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(property);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 String[] typeIds = new String[1];
 TokenBuffer[] tokens = new TokenBuffer[1];
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, typeIds, tokens);

 when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("typeId1");

 handler.complete(parser, context, rootObject);
 }
}