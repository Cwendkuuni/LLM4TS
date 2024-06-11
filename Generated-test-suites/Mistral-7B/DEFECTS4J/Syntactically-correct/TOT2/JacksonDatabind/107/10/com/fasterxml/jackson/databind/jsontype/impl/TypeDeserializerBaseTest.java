import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testForProperty() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 assertNotNull(typeDeserializer);
 assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNullProperty() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 }

 @Test
 public void testGetTypeInclusionDefault() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testGetTypeInclusionVisible() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest(true);
 assertEquals(JsonTypeInfo.As.PROPERTY, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testGetPropertyName() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertNotNull(typeDeserializerBase.getTypeIdResolver());
 }

 @Test
 public void testDefaultImpl() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertNotNull(typeDeserializerBase.getDefaultImpl());
 }

 @Test
 public void testFindDeserializerNullContext() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertNull(typeDeserializerBase._findDeserializer(null, "typeId"));
 }

 @Test
 public void testFindDeserializerEmptyDeserializers() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 typeDeserializerBase._deserializers.clear();
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(MAPPER.getContext(), "typeId");
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerValidDeserializer() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 JsonDeserializer<Object> deserializer = new StubDeserializer();
 typeDeserializerBase._deserializers.put("typeId", deserializer);
 JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(MAPPER.getContext(), "typeId");
 assertEquals(deserializer, foundDeserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerMissingDeserializer() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 JsonDeserializer<Object> deserializer = new StubDeserializer();
 typeDeserializerBase._deserializers.put("typeId", deserializer);
 typeDeserializerBase._deserializers.remove("typeId");
 JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(MAPPER.getContext(), "typeId");
 fail("Expected IOException");
 }

 @Test
 public void testFindDefaultImplDeserializerNullContext() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
 }

 @Test
 public void testFindDefaultImplDeserializerDefaultImplPresent() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 JsonDeserializer<Object> deserializer = new StubDeserializer();
 synchronized (typeDeserializerBase._defaultImpl) {
 typeDeserializerBase._defaultImplDeserializer = deserializer;
 }
 JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDefaultImplDeserializer(MAPPER.getContext());
 assertEquals(deserializer, foundDeserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDefaultImplDeserializerDefaultImplAbsent() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 JsonDeserializer<Object> deserializer = new StubDeserializer();
 synchronized (typeDeserializerBase._defaultImpl) {
 typeDeserializerBase._defaultImplDeserializer = null;
 }
 JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDefaultImplDeserializer(MAPPER.getContext());
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 assertNull(typeDeserializerBase._deserializeWithNativeTypeId(MAPPER.createParser("{}"), MAPPER.getContext()));
 }

 @Test
 public void testDeserializeWithNativeTypeIdValidTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(StubType.class);
 JsonParser parser = reader.getParser();
 parser.nextValue();
 parser.nextValue(); // skip "typeId" property
 String typeId = parser.nextTextValue();
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, MAPPER.getContext(), typeId);
 assertEquals("Result from deserialization", result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdInvalidTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(StubType.class);
 JsonParser parser = reader.getParser();
 parser.nextValue();
 parser.nextValue(); // skip "typeId" property
 parser.nextTextValue("InvalidTypeId");
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypeResolverBuilder builder = mapper.getTypeFactory().builder();
 builder.acceptCaseInsensitive(true);
 JavaType javaType = builder.constructType(TypeDeserializerBase.class);
 Object result = typeDeserializerBase._handleUnknownTypeId(MAPPER.getContext(), "InvalidTypeId");
 fail("Expected IOException");
 }

 @Test
 public void testHandleMissingTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseForTest();
 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypeResolverBuilder builder = mapper.getTypeFactory().builder();
 builder.acceptCaseInsensitive(true);
 JavaType javaType = builder.constructType(TypeDeserializerBase.class);
 Object result = typeDeserializerBase._handleMissingTypeId(MAPPER.getContext(), "MissingTypeId");
 fail("Expected IOException");
 }
}