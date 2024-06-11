import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;

 @Before
 public void setUp() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 testee = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 }

 @Test
 public void testForProperty() throws JsonProcessingException {
 BeanProperty prop = new BeanProperty.Std("testProperty", String.class);
 TypeDeserializer forProperty = testee.forProperty(prop);
 assertNotNull(forProperty);
 assertEquals(String.class, forProperty.getClass());
 }

 @Test
 public void testGetTypeInclusion() {
 JsonTypeInfo.As inclusion = testee.getTypeInclusion();
 assertEquals(JsonTypeInfo.As.WRAP_ONLY, inclusion);
 }

 @Test
 public void testToString() {
 String actual = testee.toString();
 String expected = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@27d46056]";
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testForPropertyNullProperty() {
 BeanProperty prop = null;
 TypeDeserializer forProperty = testee.forProperty(prop);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyInvalidPropertyType() {
 BeanProperty prop = new BeanProperty.Std("testProperty", Integer.TYPE);
 TypeDeserializer forProperty = testee.forProperty(prop);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNullContext() throws JsonProcessingException {
 DeserializationContext context = null;
 JsonDeserializer<Object> deserializer = testee._findDeserializer(context, "_testTypeId");
 fail("Should have thrown IOException");
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerUnknownTypeId() throws JsonProcessingException {
 DeserializationContext context = new TestContext();
 JsonDeserializer<Object> deserializer = testee._findDeserializer(context, "_unknownTypeId");
 fail("Should have thrown IOException");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws JsonProcessingException {
 DeserializationContext context = new TestContext();
 JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(context);
 assertNotNull(deserializer);
 }

 @Test(expected = NullPointerException.class)
 public void testFindDefaultImplDeserializerNullContext() throws JsonProcessingException {
 DeserializationContext context = null;
 JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(context);
 fail("Should have thrown NullPointerException");
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"TestType\",\"value\":\"TestValue\"}");
 Object result = testee._deserializeWithNativeTypeId(jp, new TestContext(), "_TestType");
 assertEquals("TestValue", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNativeTypeIdUnknownTypeId() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"UnknownType\",\"value\":\"TestValue\"}");
 testee._deserializeWithNativeTypeId(jp, new TestContext(), "_UnknownType");
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testHandleUnknownTypeId() throws JsonProcessingException {
 DeserializationContext context = new TestContext();
 JavaType actual = testee._handleUnknownTypeId(context, "_testTypeId");
 assertNull(actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleMissingTypeId() throws JsonProcessingException {
 DeserializationContext context = new TestContext();
 JavaType actual = testee._handleMissingTypeId(context, "_testMissingTypeId");
 fail("Should have thrown JsonMappingException");
 }
}

class TestContext extends DeserializationContext {
 @Override
 public JsonDeserializer<?> findContextualValueDeserializer(JavaType type, BeanProperty property)
 throws IOException {
 return null;
 }

 @Override
 public JavaType typeFromId(JsonParser p, String id) throws IOException {
 return null;
 }
}