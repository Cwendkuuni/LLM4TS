import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private JsonParser parser;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 parser = mapper.getFactory().createParser("{\"typeId1\":\"Type1\", \"prop1\":null, \"typeId2\":\"Type2\", \"prop2\":{\"nestedProp\":123}}");
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 ExternalTypeHandler handler = new ExternalTypeHandler.Builder()
 .addExternal(new TestBean().getClass().getDeclaredField("typeId1"), mapper.getTypeFactory().constructType(Type1.class))
 .addExternal(new TestBean().getClass().getDeclaredField("prop1"), String.class)
 .build();
 handler.handleTypePropertyValue(parser, new StandardDeserializationContext(), "typeId1", new TestBean());
 assertEquals("Type1", handler.getProperties()[0].getProperty().get(new TestBean()));
 }

 @Test
 public void testHandleTypePropertyValueWithNullBean() throws Exception {
 ExternalTypeHandler handler = new ExternalTypeHandler.Builder()
 .addExternal(new TestBean().getClass().getDeclaredField("typeId1"), mapper.getTypeFactory().constructType(Type1.class))
 .addExternal(new TestBean().getClass().getDeclaredField("prop1"), String.class)
 .build();
 handler.handleTypePropertyValue(parser, new StandardDeserializationContext(), "typeId1", null);
 assertNull(handler.getProperties()[0].getProperty());
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 ExternalTypeHandler handler = new ExternalTypeHandler.Builder()
 .addExternal(new TestBean().getClass().getDeclaredField("prop1"), String.class)
 .build();
 handler.handlePropertyValue(parser, new StandardDeserializationContext(), "prop1", new TestBean());
 assertEquals("null", handler.getProperties()[0].getProperty().get(new TestBean()));
 }

 @Test
 public void testCompleteWithValidData() throws Exception {
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExternalTypeHandler.Builder()
 .addExternal(new TestBean().getClass().getDeclaredField("typeId1"), mapper.getTypeFactory().constructType(Type1.class))
 .addExternal(new TestBean().getClass().getDeclaredField("prop1"), String.class)
 .addExternal(new TestBean().getClass().getDeclaredField("typeId2"), mapper.getTypeFactory().constructType(Type2.class))
 .addExternal(new TestBean().getClass().getDeclaredField("prop2.nestedProp"), Integer.class)
 .build().getProperties());
 TestBean bean = (TestBean) handler.complete(parser, new StandardDeserializationContext(), null);
 assertEquals("Type1", handler.getProperties()[0].getProperty().get(bean));
 assertEquals(123, handler.getProperties()[2].getProperty().get(bean));
 }

 @Test
 public void testCompleteWithMissingData() throws Exception {
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExternalTypeHandler.Builder()
 .addExternal(new TestBean().getClass().getDeclaredField("prop1"), String.class)
 .addExternal(new TestBean().getClass().getDeclaredField("typeId2"), mapper.getTypeFactory().constructType(Type2.class))
 .build().getProperties());
 TestBean bean = (TestBean) handler.complete(parser, new StandardDeserializationContext(), null);
 assertNull(handler.getProperties()[0].getProperty());
 assertEquals(null, handler.getProperties()[1].getProperty());
 }

 @Test(expected = IOException.class)
 public void testCompleteWithInvalidData() throws Exception {
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExternalTypeHandler.Builder()
 .addExternal(new TestBean().getClass().getDeclaredField("prop1"), String.class)
 .addExternal(new TestBean().getClass().getDeclaredField("typeId2"), mapper.getTypeFactory().constructType(Type2.class))
 .build().getProperties());
 TestBean bean = (TestBean) handler.complete(parser, new StandardDeserializationContext(), null);
 // complete should throw an IOException if any property is missing
 parser.nextToken(); // consume "{"
 parser.nextToken(); // consume "typeId1"
 parser.nextToken(); // consume ":"
 parser.nextIntValue(); // consume int
 parser.nextToken(); // consume ","
 parser.nextToken(); // consume "prop2"
 parser.nextToken(); // consume ":"
 parser.nextValueAsString("invalidType"); // consume invalid type
 assertFalse(handler.getProperties()[2].getProperty() instanceof Number);
 }

 private Object[] getProperties(ExternalTypeHandler handler) {
 return handler.getProperties();
 }
}

class TestBean {
 private Type1 typeId1;
 private String prop1;
 private Type2 prop2;
 // getters and setters omitted for brevity
}

class Type1 {}
class Type2 {
 private int nestedProp;
 // getters and setters omitted for brevity
}