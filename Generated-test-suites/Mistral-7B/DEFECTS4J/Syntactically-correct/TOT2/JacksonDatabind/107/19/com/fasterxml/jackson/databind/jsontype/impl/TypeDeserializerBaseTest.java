import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 testee = mapper.getTypeFactory().constructValueType(TypeDeserializerBase.class, null);
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty(TestClass.class, "testProperty");
 TypeDeserializer expected = mapper.getTypeFactory().constructType(SubType1.class);
 TypeDeserializer actual = testee.forProperty(property);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetTypeInclusion() {
 JsonTypeInfo.As inclusion = testee.getTypeInclusion();
 assertEquals(JsonTypeInfo.As.WRAP_ONLY, inclusion);
 }

 @Test
 public void testBaseTypeName() throws Exception {
 String result = testee.baseTypeName();
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", result);
 }

 @Test
 public void testPropertyName() throws Exception {
 String result = testee.getPropertyName();
 assertEquals("", result);
 }

 @Test
 public void testTypeIdResolver() throws Exception {
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 testee = new TypeDeserializerBase(mapper.getTypeFactory().constructType(TypeDeserializerBase.class, null), idResolver, "", true, mapper.getTypeFactory().constructType(DefaultImplType.class));
 TypeDeserializer actual = testee.forProperty(new BeanProperty(TestClass.class, "testProperty"));
 assertNotNull(actual);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerWithNullTypeId() throws Exception {
 testee._findDeserializer(mapper.getDeserializationContext(), null);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerWithInvalidTypeId() throws Exception {
 testee._findDeserializer(mapper.getDeserializationContext(), "InvalidTypeId");
 fail("Expected IOException");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> actual = testee._findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertNotNull(actual);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"@type\":\"SubType1\"}]");
 Object result = testee._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(new SubType1(), result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdWithInvalidTypeId() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"@type\":\"InvalidTypeId\"}]");
 testee._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeWithNativeTypeIdWithNullTypeId() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{}]");
 Object result = testee._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 DeserializationContext context = mapper.getDeserializationContext();
 String result = testee._handleUnknownTypeId(context, "UnknownTypeId").getClassName();
 assertEquals("com.fasterxml.jackson.core.json.UnknownTypeIdException", result);
 }

 @Test
 public void testHandleMissingTypeId() throws Exception {
 DeserializationContext context = mapper.getDeserializationContext();
 String result = testee._handleMissingTypeId(context, "MissingTypeId").getClassName();
 assertEquals("com.fasterxml.jackson.databind.JsonMappingException", result);
 }
}