import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExternalTypeHandlerTest {

 @BeforeEach
 public void setup() {
 // Set up any required objects or mocks here
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 // Given
 ObjectMock bean = new ObjectMock();
 JsonParser mockParser = createMockParser();
 DeserializationContext context = createMockContext();
 ExtTypedProperty property = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "testProperty"), new SimpleTypeDeserializer(String.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("testProperty", 0);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(property, property.getTypeDeserializer())
 .build()
 );
 // When
 boolean result = handler.handleTypePropertyValue(mockParser, context, "testProperty", bean);
 // Then
 assertTrue(result);
 assertEquals("testTypeId", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_MissingProperty() throws IOException {
 // Given
 ObjectMock bean = new ObjectMock();
 JsonParser mockParser = createMockParser();
 DeserializationContext context = createMockContext();
 ExtTypedProperty property = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "missingProperty"), new SimpleTypeDeserializer(String.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("missingProperty", 0);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(property, property.getTypeDeserializer())
 .build()
 );
 // When
 boolean result = handler.handleTypePropertyValue(mockParser, context, "missingProperty", bean);
 // Then
 assertFalse(result);
 }

 @Test
 public void testHandleTypePropertyValue_InvalidTypeId() throws IOException {
 // Given
 ObjectMock bean = new ObjectMock();
 JsonParser mockParser = createMockParser();
 DeserializationContext context = createMockContext();
 ExtTypedProperty property = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "testProperty"), new SimpleTypeDeserializer(String.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("testProperty", 0);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(property, property.getTypeDeserializer())
 .build()
 );
 // When
 mockParser.setText("{\"testProperty\":\"invalidTypeId\"}");
 boolean result = handler.handleTypePropertyValue(mockParser, context, "testProperty", bean);
 // Then
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 // Given
 ObjectMock bean = new ObjectMock();
 JsonParser mockParser = createMockParser();
 DeserializationContext context = createMockContext();
 ExtTypedProperty property = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "testProperty"), new SimpleTypeDeserializer(String.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("testProperty", 0);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(property, property.getTypeDeserializer())
 .build()
 );
 // When
 handler.handlePropertyValue(mockParser, context, "testProperty", bean);
 // Then
 assertNull(handler._typeIds[0]);
 assertNotNull(handler._tokens[0]);
 }

 @Test
 public void testComplete() throws IOException {
 // Given
 ObjectMock bean = new ObjectMock();
 JsonParser mockParser = createMockParser();
 DeserializationContext context = createMockContext();
 ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "property1"), new SimpleTypeDeserializer(String.class));
 ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "property2"), new SimpleTypeDeserializer(Integer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 nameToPropertyIndex.put("property2", 1);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(property1, property1.getTypeDeserializer())
 .addExternal(property2, property2.getTypeDeserializer())
 .build()
 );
 // When
 handler.start();
 mockParser.nextToken(); // [START_OBJECT]
 handler.handlePropertyValue(mockParser, context, "property1", bean);
 mockParser.nextToken(); // "property1": "typeId1"
 handler.handlePropertyValue(mockParser, context, null, bean); // null property
 mockParser.nextToken(); // ","
 handler.handlePropertyValue(mockParser, context, "property2", bean);
 mockParser.nextToken(); // "property2": 42
 handler.handlePropertyValue(mockParser, context, null, bean); // null property
 mockParser.nextToken(); // ","
 mockParser.nextToken(); // "]"
 handler.complete(mockParser, context, bean);
 // Then
 assertEquals("typeId1", bean.getClass().getField("property1").get(bean));
 assertEquals(42, bean.getClass().getField("property2").get(bean));
 }

 @Test
 public void testComplete_MissingTypeId() throws IOException {
 // Given
 ObjectMock bean = new ObjectMock();
 JsonParser mockParser = createMockParser();
 DeserializationContext context = createMockContext();
 ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "property1"), new SimpleTypeDeserializer(String.class));
 ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "property2"), new SimpleTypeDeserializer(Integer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 nameToPropertyIndex.put("property2", 1);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(property1, property1.getTypeDeserializer())
 .addExternal(property2, property2.getTypeDeserializer())
 .build()
 );
 // When
 handler.start();
 mockParser.nextToken(); // [START_OBJECT]
 handler.handlePropertyValue(mockParser, context, "property1", bean);
 mockParser.nextToken(); // "property1": "typeId1"
 handler.handlePropertyValue(mockParser, context, "property2", bean);
 handler.handlePropertyValue(mockParser, context, null, bean); // null property
 mockParser.nextToken(); // ","
 handler.handlePropertyValue(mockParser, context, null, bean); // missing type id
 mockParser.nextToken(); // "]"
 // Then
 assertThrows(MappingException.class, () -> handler.complete(mockParser, context, bean));
 }

 @Test
 public void testComplete_MissingProperty() throws IOException {
 // Given
 ObjectMock bean = new ObjectMock();
 JsonParser mockParser = createMockParser();
 DeserializationContext context = createMockContext();
 ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "property1"), new SimpleTypeDeserializer(String.class));
 ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty(ObjectMock.class, "property2"), new SimpleTypeDeserializer(Integer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("property1", 0);
 nameToPropertyIndex.put("property2", 1);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(property1, property1.getTypeDeserializer())
 .addExternal(property2, property2.getTypeDeserializer())
 .build()
 );
 // When
 handler.start();
 mockParser.nextToken(); // [START_OBJECT]
 handler.handlePropertyValue(mockParser, context, "property1", bean);
 handler.handlePropertyValue(mockParser, context, "property1", bean); // duplicate property
 handler.handlePropertyValue(mockParser, context, null, bean); // null property
 mockParser.nextToken(); // ","
 handler.handlePropertyValue(mockParser, context, null, bean); // missing property
 mockParser.nextToken(); // "]"
 // Then
 assertThrows(MappingException.class, () -> handler.complete(mockParser, context, bean));
 }

 @AfterEach
 public void teardown() {
 // Clean up any mocks or objects here
 }

 // Helper methods go here
}