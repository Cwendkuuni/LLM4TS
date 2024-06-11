import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import java.io.IOException;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 TypeDeserializerBaseType type = new TypeDeserializerBaseType(
 Map.class, mapper.getTypeFactory(), "_type", true, String.class);
 typeDeserializerBase = (TypeDeserializerBase) type;
 }

 @Test
 public void testForProperty() throws IOException {
 BeanProperty property = new BeanProperty("_type", String.class, null);
 TypeDeserializerBase forProperty = typeDeserializerBase.forProperty(property);
 assertNotNull(forProperty);
 assertEquals(String.class, forProperty.getType());
 }

 @Test
 public void testGetTypeInclusion() {
 JsonTypeInfo.As inclusion = typeDeserializerBase.getTypeInclusion();
 assertNull(inclusion);
 }

 @Test
 public void testBaseTypeName() {
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertEquals("class java.util.Map", baseTypeName);
 }

 @Test
 public void testPropertyName() {
 String propertyName = typeDeserializerBase.getPropertyName();
 assertEquals("_type", propertyName);
 }

 @Test
 public void testTypeIdResolver() {
 TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
 assertNotNull(typeIdResolver);
 }

 @Test
 public void testDefaultImpl() {
 Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
 assertEquals(String.class, defaultImpl.getName());
 }

 @Test
 public void testBaseType() {
 JavaType baseType = typeDeserializerBase.baseType();
 assertEquals(Map.class, baseType);
 }

 @Test
 public void testToString() {
 String toString = typeDeserializerBase.toString();
 assertTrue(toString.contains("BaseType"));
 assertTrue(toString.contains("id-resolver:"));
 }

 @Test
 public void testFindDeserializer() throws JsonProcessingException, IOException {
 String typeId = "StringMapKeyValueSerializer";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
 assertNotNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNullTypeId() throws JsonProcessingException, IOException {
 typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), null);
 fail("Expected IOException");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws JsonProcessingException, IOException {
 JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertNotNull(defaultImplDeserializer);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws JsonProcessingException, IOException {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"_type\": \"MapStringIntegerSerializer\", \"foo\": 1}");
 Object deserialize = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
 assertNotNull(deserialize);
 }

 @Test
 public void testDeserializeWithUnknownTypeId() throws JsonProcessingException, IOException {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"_type\": \"UnknownTypeSerializer\", \"foo\": 1}");
 try {
 Object deserialize = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testHandleUnknownTypeId() throws IOException {
 String typeId = "UnknownTypeSerializer";
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), typeId);
 assertNull(javaType);
 }

 @Test
 public void testHandleMissingTypeId() throws IOException {
 String extraDesc = "_type property is missing";
 JavaType javaType = typeDeserializerBase._handleMissingTypeId(mapper.getDeserializationContext(), extraDesc);
 assertNull(javaType);
 }
}

class TypeDeserializerBaseType extends TypeDeserializerBase<Map<String, Integer>> {

 public TypeDeserializerBaseType(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }
}