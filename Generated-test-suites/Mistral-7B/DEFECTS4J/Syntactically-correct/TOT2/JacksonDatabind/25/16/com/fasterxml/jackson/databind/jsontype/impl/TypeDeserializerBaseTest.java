import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest extends TestCase {

 private TypeDeserializerBase typeDeserializerBase;
 private DeserializationContext context;
 private JsonParser parser;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 typeDeserializerBase = new TypeDeserializerBase(
 ObjectType.class, mock(TypeIdResolver.class), "typePropertyName", true, null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 };
 }

 @Test
 public void testForProperty() throws JsonProcessingException {
 BeanProperty beanProperty = mock(BeanProperty.class);
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(beanProperty);
 assertEquals(TypeDeserializerBase.class, forProperty.getClass());
 }

 @Test
 public void testTypeInclusion() {
 assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() {
 assertEquals("ObjectType", typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testPropertyName() {
 assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
 }

 @Test
 public void testTypeIdResolver() {
 assertEquals(mock(TypeIdResolver.class), typeDeserializerBase.getTypeIdResolver());
 }

 @Test
 public void testDefaultImpl() {
 assertNull(typeDeserializerBase.getDefaultImpl());
 }

 @Test
 public void testToString() {
 String toString = typeDeserializerBase.toString();
 assertTrue(toString.contains("[TypeDeserializerBase"));
 assertTrue(toString.contains("; base-type:ObjectType"));
 assertTrue(toString.contains("; id-resolver:"));
 }

 @Test
 public void testFindDeserializer() throws JsonProcessingException {
 String typeId = "TypeId1";
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(context.findContextualValueDeserializer(TypeA.class, any(BeanProperty.class))).thenReturn(deserializer);
 when(context.findContextualValueDeserializer(TypeB.class, any(BeanProperty.class))).thenThrow(new JsonMappingException(" expected"));
 when(context.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

 typeDeserializerBase._deserializers.put(typeId, null);
 JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(context, typeId);
 assertEquals(deserializer, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerNullTypeId() throws JsonProcessingException {
 typeDeserializerBase._deserializers.clear();
 JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(context, null);
 fail("should throw exception");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerUnknownTypeId() throws JsonProcessingException {
 typeDeserializerBase._deserializers.clear();
 when(context.getTypeIdResolver().typeFromId(context, "UnknownTypeId")).thenReturn(null);
 JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(context, "UnknownTypeId");
 fail("should throw exception");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerUnsupportedType() throws JsonProcessingException {
 typeDeserializerBase._deserializers.clear();
 when(context.getTypeIdResolver().typeFromId(context, "TypeId1"))
 .thenReturn(TypeC.class);
 when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
 JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(context, "TypeId1");
 fail("should throw exception");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws JsonProcessingException {
 Class<?> defaultImpl = Object.class;
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(context.findContextualValueDeserializer(defaultImpl, any(BeanProperty.class))).thenReturn(deserializer);
 when(context.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
 JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(context);
 assertEquals(deserializer, actual);
 }

 @Test
 public void testFindDefaultImplDeserializerNull() throws JsonProcessingException {
 when(context.findContextualValueDeserializer(Object.class, any(BeanProperty.class))).thenReturn(null);
 JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(context);
 assertNull(actual);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws JsonProcessingException {
 String json = "{\"typeId\":\"TypeId1\"}";
 Object actual = typeDeserializerBase._deserializeWithNativeTypeId(parser, context, "TypeId1");
 assertEquals(TypeA.class, actual.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNativeTypeIdNullTypeId() throws JsonProcessingException {
 String json = "{}";
 typeDeserializerBase._deserializeWithNativeTypeId(parser, context, null);
 fail("should throw exception");
 }
}