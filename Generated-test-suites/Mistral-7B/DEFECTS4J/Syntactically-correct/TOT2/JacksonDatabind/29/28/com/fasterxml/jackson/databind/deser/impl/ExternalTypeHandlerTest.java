import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private ObjectBean bean;

 @Before
 public void setup() throws IOException {
 JsonFactory factory = new JsonFactory();
 parser = factory.createParser("{\"property1\": \"value1\", \"property2\": \"value2\"}");
 ObjectMapper mapper = new ObjectMapper();
 bean = mapper.readValue(parser, ObjectBean.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("property1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("property2", Integer.class), new IntegerTypeDeserializer())
 .build());
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 handler.handleTypePropertyValue(parser, new TestingDeserializationContext(), "property1", bean);
 assertEquals("value1", handler.getTokens()[0].getText());
 handler.handleTypePropertyValue(parser, new TestingDeserializationContext(), "property2", bean);
 assertEquals("value2", handler.getTokens()[1].getText());
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 handler.handlePropertyValue(parser, new TestingDeserializationContext(), "property1", bean);
 assertEquals("value1", handler.getTypeIds()[0]);
 handler.handlePropertyValue(parser, new TestingDeserializationContext(), "property2", bean);
 assertEquals("value2", handler.getTypeIds()[1]);
 }

 @Test
 public void testComplete() throws IOException {
 handler.complete(parser, new TestingDeserializationContext(), bean);
 assertNotNull(bean.getProperty1());
 assertEquals("value1", bean.getProperty1());
 assertNotNull(bean.getProperty2());
 assertEquals(1, (int) bean.getProperty2());
 }

 @Test(expected = IOException.class)
 public void testMissingProperty() throws IOException {
 handler.handlePropertyValue(parser, new TestingDeserializationContext(), "nonexistentProperty", bean);
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingTypeId() throws IOException {
 handler.handlePropertyValue(parser, new TestingDeserializationContext(), "property1", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingBean() throws IOException {
 handler.handlePropertyValue(parser, new TestingDeserializationContext(), "property1", null);
 }

 @Test
 public void testNaturalType() throws IOException {
 parser.nextToken(); // consume START_OBJECT
 handler.handlePropertyValue(parser, new TestingDeserializationContext(), "property1", bean);
 assertEquals(1.0, handler.getTypeIds()[0], 0.01);
 }

 @Test
 public void testCompleteWithCreator() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 PropertyBasedCreator creator = new MyCreator();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("property1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("property2", Integer.class), new IntegerTypeDeserializer())
 .build());
 Object result = handler.complete(parser, new TestingDeserializationContext(), creator);
 assertNotNull(result);
 assertEquals("value1", ((MyBean) result).getProperty1());
 assertEquals(1, ((MyBean) result).getProperty2());
 }

 private static class TestingDeserializationContext extends DeserializationContext {
 @Override public Object findValueInContext(String name, Class<?> type) { return null; }
 }

 private static class MyBean {
 private String property1;
 private int property2;

 public String getProperty1() { return property1; }
 public void setProperty1(String value) { property1 = value; }
 public int getProperty2() { return property2; }
 public void setProperty2(int value) { property2 = value; }
 }

 private static class MyCreator implements PropertyBasedCreator {
 @Override public Object createUsingDefaults() { return new MyBean(); }
 @Override public Object createFrom(PropertyValueBuffer buffer, DeserializationContext ctxt) { return null; }
 @Override public PropertyDescriptor[] getPropertyDescriptors() { return new PropertyDescriptor[0]; }
 }

 private static class StringTypeDeserializer extends StdTypeDeserializer {
 @Override public SettableBeanProperty getProperty() { return new SettableBeanProperty("property1", String.class); }
 }

 private static class IntegerTypeDeserializer extends StdTypeDeserializer {
 @Override public SettableBeanProperty getProperty() { return new SettableBeanProperty("property2", Integer.class); }
 }

}