import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new TestBeanProperty1(), new TestTypeDeserializer1())
 .addExternal(new TestBeanProperty2(), new TestTypeDeserializer2())
 .build());
 }

 /**
 * Test method for {@link ExternalTypeHandler#start()}
 */
 @Test
 public void testStart() {
 ExternalTypeHandler startHandler = handler.start();
 assertNotNull(startHandler);
 }

 /**
 * Test method for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
 */
 @Test
 public void testHandleTypePropertyValue() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"typeId\":\"Type1\"}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object bean = new TestBean();
 boolean handled = handler.handleTypePropertyValue(parser, ctxt, "typeId", bean);
 assertTrue(handled);
 assertEquals("Type1", handler.getTypeIds()[0]);
 }

 /**
 * Test method for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
 */
 @Test
 public void testHandlePropertyValue() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"property1\":1}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object bean = new TestBean();
 handler.handlePropertyValue(parser, ctxt, "property1", bean);
 assertEquals(1, handler.getBeanProperty1().get(bean));
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)}
 */
 @Test
 public void testComplete() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"property1\":1,\"typeId\":\"Type1\"}");
 Object bean = new TestBean();
 handler.handlePropertyValue(parser, objectMapper.getDeserializationContext(), "property1", bean);
 handler.handleTypePropertyValue(parser, objectMapper.getDeserializationContext(), "typeId", bean);
 Object result = handler.complete(parser, objectMapper.getDeserializationContext(), bean);
 assertEquals(1, ((TestBean) result).getProperty1());
 assertEquals("Type1", handler.getTypeIds()[0]);
 }

 /**
 * Test method for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
 * with missing property
 */
 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueMissingProperty() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"property1\":1}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object bean = new TestBean();
 boolean handled = handler.handleTypePropertyValue(parser, ctxt, "typeId", bean);
 fail("Should throw IOException when property 'typeId' is missing");
 }

 /**
 * Test method for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
 * with missing type id
 */
 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueMissingTypeId() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"typeId\":\"Type1\"}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object bean = new TestBean();
 boolean handled = handler.handleTypePropertyValue(parser, ctxt, "property1", bean);
 fail("Should throw IOException when property 'property1' is missing");
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)}
 * with missing type id
 */
 @Test(expected = IOException.class)
 public void testCompleteMissingTypeId() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"property1\":1}");
 Object bean = new TestBean();
 handler.handlePropertyValue(parser, objectMapper.getDeserializationContext(), "property1", bean);
 Object result = handler.complete(parser, objectMapper.getDeserializationContext(), bean);
 fail("Should throw IOException when 'typeId' is missing");
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object, PropertyValueBuffer, PropertyBasedCreator)}
 */
 @Test
 public void testCompleteWithCreator() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"property1\":1,\"typeId\":{\"type\":\"String\"}}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 TestCreator creator = new TestCreator();
 Object result = handler.complete(parser, objectMapper.getDeserializationContext(), buffer, creator);
 assertEquals("1", ((TestBean) result).getProperty1());
 assertEquals("String", handler.getTypeIds()[0]);
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object, PropertyValueBuffer, PropertyBasedCreator)}
 * with missing type id
 */
 @Test(expected = IOException.class)
 public void testCompleteWithCreatorMissingTypeId() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"property1\":1,\"typeId\":null}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 TestCreator creator = new TestCreator();
 Object result = handler.complete(parser, objectMapper.getDeserializationContext(), buffer, creator);
 fail("Should throw IOException when 'typeId' is missing");
 }

 /**
 * Test method for {@link ExternalTypeHandler#_deserialize(JsonParser, DeserializationContext, int, String)}
 */
 @Test
 public void testDeserialize() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"typeId\":\"Type1\",\"value\":1}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object bean = new TestBean();
 Object result = handler._deserialize(parser, ctxt, 0, "Type1");
 assertEquals(1, ((TestBean) bean).getProperty1().get(result));
 }

 /**
 * Test method for {@link ExternalTypeHandler#_deserializeAndSet(JsonParser, DeserializationContext, Object, int, String)}
 */
 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"typeId\":\"Type1\",\"value\":1}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object bean = new TestBean();
 handler._deserializeAndSet(parser, ctxt, bean, 0, "Type1");
 assertEquals(1, ((TestBean) bean).getProperty1().get(0));
 }

}

class TestBean {
 private List<Object> typeIds = new ArrayList<>();
 private int property1;

 public List<Object> getTypeIds() {
 return typeIds;
 }

 public void setProperty1(int property1) {
 this.property1 = property1;
 }

 public int getProperty1() {
 return property1;
 }
}

class TestBeanProperty1 {
 public static final SettableBeanProperty PROPERTY = new AutoPropertyGenerator().getSettableBeanProperty(TestBean.class, "property1");
}

class TestTypeDeserializer1 extends TypeDeserializer {
 public static final String PROPERTY_NAME = "typeId";

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return ctxt.readValueAs(String.class);
 }

 @Override
 public String getPropertyName() {
 return PROPERTY_NAME;
 }
}

class TestTypeDeserializer2 extends TypeDeserializer {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }

 @Override
 public String getPropertyName() {
 return "typeId";
 }
}

class TestCreator implements PropertyBasedCreator {
 @Override
 public Object createInstanceWithHandledProperties(DeserializationContext ctxt, PropertyValueBuffer valueBuffer) throws IOException {
 return new TestBean();
 }
}