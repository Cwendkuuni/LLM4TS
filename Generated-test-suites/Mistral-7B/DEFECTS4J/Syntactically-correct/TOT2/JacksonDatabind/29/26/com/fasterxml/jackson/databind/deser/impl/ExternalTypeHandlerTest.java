import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private static Object mapperSetup() throws JsonProcessingException {
 JsonFactory factory = new JsonFactory();
 ObjectMapper objectMapper = new ObjectMapper(factory);
 return objectMapper;
 }

 @Test
 public void testHandleTypePropertyValueSuccess() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), new StringTypeDeserializer(null))},
 new HashMap<>(),
 new String[]{},
 new TokenBuffer[0]);
 String json = "{\"testProperty\":\"StringType\"}";
 JsonParser jp = mapper.getFactory().createParser(json);
 TestClass testClass = new TestClass();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handleTypePropertyValue(jp, ctxt, "testProperty", testClass);
 assertEquals("StringType", testClass.testProperty);
 }

 @Test
 public void testHandleTypePropertyValueMissingProperty() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), new StringTypeDeserializer(null))},
 new HashMap<>(),
 new String[]{},
 new TokenBuffer[0]);
 String json = "{\"testProperty2\":\"StringType\"}";
 JsonParser jp = mapper.getFactory().createParser(json);
 TestClass testClass = new TestClass();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handleTypePropertyValue(jp, ctxt, "testProperty2", testClass);
 assertNull(testClass.testProperty);
 }

 @Test
 public void testHandlePropertyValueSuccess() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), null)},
 new HashMap<>(),
 new String[]{},
 new TokenBuffer[0]);
 String json = "{\"testProperty\":\"TestValue\"}";
 JsonParser jp = mapper.getFactory().createParser(json);
 TestClass testClass = new TestClass();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handlePropertyValue(jp, ctxt, "testProperty", testClass);
 assertEquals("TestValue", testClass.testProperty);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueMissingProperty() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), null)},
 new HashMap<>(),
 new String[]{},
 new TokenBuffer[0]);
 String json = "{\"testProperty2\":\"TestValue\"}";
 JsonParser jp = mapper.getFactory().createParser(json);
 TestClass testClass = new TestClass();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handlePropertyValue(jp, ctxt, "testProperty2", testClass);
 }

 @Test
 public void testCompleteSuccess() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), null)
 },
 new HashMap<>(),
 new String[]{"StringType"},
 new TokenBuffer[]{new TokenBuffer()});
 String json = "{\"testProperty\":\"StringType\"}";
 TestClass testClass = new TestClass();
 JsonParser jp = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.complete(jp, ctxt, testClass);
 assertEquals("StringType", testClass.testProperty);
 }

 @Test
 public void testCompleteMissingTypeId() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), null)
 },
 new HashMap<>(),
 new String[0],
 new TokenBuffer[0]);
 String json = "{\"testProperty\":\"TestValue\"}";
 TestClass testClass = new TestClass();
 JsonParser jp = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 handler.complete(jp, ctxt, testClass);
 fail("Expected missing type id exception");
 } catch (MappingException e) {
 assertEquals("Missing external type id property 'testProperty'", e.getMessage());
 }
 }

 @Test
 public void testCompleteMissingProperty() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), new StringTypeDeserializer(null))
 },
 new HashMap<>(),
 new String[]{"StringType"},
 new TokenBuffer[]{new TokenBuffer()});
 String json = "{\"testProperty2\":\"TestValue\"}";
 TestClass testClass = new TestClass();
 JsonParser jp = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 handler.complete(jp, ctxt, testClass);
 fail("Expected missing property exception");
 } catch (MappingException e) {
 assertEquals("Missing property 'testProperty2' for external type id 'testProperty'", e.getMessage());
 }
 }

 @Test
 public void testCompleteNullValue() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"), null)
 },
 new HashMap<>(),
 new String[]{"StringType"},
 new TokenBuffer[]{new TokenBuffer()});
 String json = "{\"testProperty\":null}";
 TestClass testClass = new TestClass();
 JsonParser jp = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.complete(jp, ctxt, testClass);
 assertNull(testClass.testProperty);
 }

 @Test
 public void testCompleteWithDefaultType() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
 new ExtTypedProperty(new SettableBeanProperty(TestClass.class, "testProperty"),
 new SimpleTypeDeserializer(SimpleType.constructUnboundType(Integer.class)))
 },
 new HashMap<>(),
 new String[]{},
 new TokenBuffer[0]);
 String json = "{\"testProperty\":null}";
 TestClass testClass = new TestClass();
 JsonParser jp = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.complete(jp, ctxt, testClass);
 assertEquals(0, testClass.testProperty);
 }

 @Test
 public void testBuilder() {
 ExternalTypeHandler handler = new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty(TestClass.class, "testProperty"), new StringTypeDeserializer(null))
 .build();
 }

}

class TestClass {
 private String testProperty;
 // getters and setters
}

class StringTypeDeserializer extends StdDeserializer<String> {
 StringTypeDeserializer() { this(null); }
 StringTypeDeserializer(Class<?> vc) { super(vc); }
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString();
 }
}

class SimpleTypeDeserializer extends StdDeserializer<Integer> {
 SimpleTypeDeserializer() { this(null); }
 SimpleTypeDeserializer(Class<?> vc) { super(vc); }
 @Override
 public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getIntValue();
 }
}