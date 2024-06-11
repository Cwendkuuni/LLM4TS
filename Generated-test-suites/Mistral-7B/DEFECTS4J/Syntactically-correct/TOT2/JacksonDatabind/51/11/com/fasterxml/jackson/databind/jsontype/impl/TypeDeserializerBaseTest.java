import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 testee = new MyTypeDeserializerBase();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty.Std(new String[]{"testProperty"}, String.class);
 TypeDeserializer deserializer = testee.forProperty(property);
 assertEquals(String.class, deserializer.getType(null));
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, testee.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 String expected = "com.example.MyClass";
 assertEquals(expected, testee.baseTypeName());
 }

 @Test
 public void testPropertyName() throws Exception {
 String expected = "testProperty";
 assertEquals(expected, testee.getPropertyName());
 }

 @Test
 public void testTypeIdResolver() throws Exception {
 TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
 testee = new TypeDeserializerBase(null, idResolver, null, false, null);
 Object type = testee.getTypeIdResolver();
 assertEquals(idResolver, type);
 }

 @Test
 public void testDefaultImpl() throws Exception {
 JavaType expected = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
 assertEquals(expected, testee.getDefaultImpl());
 }

 @Test
 public void testToString() throws Exception {
 String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class com.example.MyClass; id-resolver:null]";
 assertEquals(expected, testee.toString());
 }

 @Test
 public void testFindDeserializerNull() throws Exception {
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 assertNull(testee._findDeserializer(ctxt, null));
 }

 @Test
 public void testFindDeserializerInvalidTypeId() throws Exception {
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String typeId = "invalidTypeId";
 assertNull(testee._findDeserializer(ctxt, typeId));
 }

 @Test
 public void testFindDeserializerValidTypeId() throws Exception {
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String typeId = "com.example.MySubclass";
 JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
 when(ctxt.findContextualValueDeserializer(eq(Object.class), any(BeanProperty.class))).thenReturn(deserializer);
 when(testee._idResolver.typeFromId(ctxt, typeId)).thenReturn(ObjectMapper.getDefaultInstance().getTypeFactory().constructType(MySubclass.class));
 JsonDeserializer<Object> result = testee._findDeserializer(ctxt, typeId);
 assertEquals(deserializer, result);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerUnknownType() throws Exception {
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String typeId = "unknownType";
 testee._findDeserializer(ctxt, typeId);
 }

 @Test
 public void testFindDefaultImplDeserializerNull() throws Exception {
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 assertNull(testee._findDefaultImplDeserializer(ctxt));
 }

 @Test
 public void testFindDefaultImplDeserializerNotNull() throws Exception {
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String propertyName = "testProperty";
 BeanProperty property = new BeanProperty.Std(new String[]{propertyName}, String.class);
 JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
 when(ctxt.findContextualValueDeserializer(eq(String.class), any(BeanProperty.class))).thenReturn(deserializer);
 testee = new TypeDeserializerBase(null, null, propertyName, false, null);
 JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(ctxt);
 assertEquals(deserializer, result);
 }

 @Test
 public void testDeserializeWithNativeTypeIdNull() throws Exception {
 JsonParser jp = Mockito.mock(JsonParser.class);
 assertNull(testee._deserializeWithNativeTypeId(jp, null));
 }

 @Test
 public void testDeserializeWithNativeTypeIdValidTypeId() throws Exception {
 String typeId = "MyClass";
 JsonParser jp = Mockito.mock(JsonParser.class);
 when(jp.getTypeId()).thenReturn(typeId);
 Object result = testee._deserializeWithNativeTypeId(jp, null);
 assertNotNull(result);
 }

 @Test
 public void testDeserializeWithNativeTypeIdInvalidTypeId() throws Exception {
 String typeId = "InvalidType";
 JsonParser jp = Mockito.mock(JsonParser.class);
 when(jp.getTypeId()).thenReturn(typeId);
 try {
 testee._deserializeWithNativeTypeId(jp, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}

class MyTypeDeserializerBase extends TypeDeserializerBase {
 // empty implementation
}

class MySubclass extends MyClass {
 // empty implementation
}