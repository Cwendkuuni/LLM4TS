import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper mapper;
 private TypeDeserializerBase typeDeserializerBase;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 typeDeserializerBase = new TypeDeserializerBaseBase(
 Class.forName("com.test.MyBaseClass"),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Class.forName("com.test.MyDefaultImpl")));
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty.Std("propertyName", String.class);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
 assertEquals("TypeDeserializerBaseSubclass", typeDeserializer.getClass().getSimpleName());
 }

 @Test
 public void testTypeInclusionGetTypeInclusion() {
 JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
 assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeInclusion);
 }

 @Test
 public void testBaseTypeName() throws Exception {
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertEquals("com.test.MyBaseClass", baseTypeName);
 }

 @Test
 public void testPropertyName() throws Exception {
 String propertyName = typeDeserializerBase.getPropertyName();
 assertEquals("typePropertyName", propertyName);
 }

 @Test
 public void testTypeIdResolverGetTypeIdResolver() throws Exception {
 TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
 assertNotNull(typeIdResolver);
 }

 @Test
 public void testDefaultImpl() throws Exception {
 Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
 assertEquals(Class.forName("com.test.MyDefaultImpl"), defaultImpl);
 }

 @Test
 public void testToString() throws Exception {
 String toString = typeDeserializerBase.toString();
 assertTrue(toString.contains("TypeDeserializerBase"));
 assertTrue(toString.contains("base-type:com.test.MyBaseClass"));
 assertTrue(toString.contains("id-resolver:"));
 }

 @Test
 public void testFindDeserializer() throws Exception {
 String typeId = "com.test.MySubclass";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
 assertEquals(TypeDeserializerSubclass.class, deserializer.getClass());
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerUnknownTypeId() throws Exception {
 String unknownTypeId = "UnknownTypeId";
 typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), unknownTypeId);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 String extraDesc = "Test for unknown type id handling";
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), "UnknownTypeId");
 assertNotNull(javaType);
 assertTrue(javaType.getClass().getName().contains("UnknownTypeIdHandler"));
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertNotNull(defaultImplDeserializer);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 String json = "{\"typeId\":\"com.test.MySubclass\"}";
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 Object deserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
 assertEquals(new MySubclass(), deserializedObject);
 }

 @Test
 public void testDeserializeWithNullTypeId() throws Exception {
 String json = "{}";
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 Object deserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
 assertNull(deserializedObject);
 }
}