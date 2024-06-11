import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testForPropertyMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 BeanProperty beanProperty = new BeanProperty.Std("propertyName", String.class);
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(beanProperty);
 assertNotNull(forProperty);
 assertEquals(String.class, forProperty.getType(null));
 }

 @Test
 public void testGetTypeInclusionMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeNameMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testGetPropertyNameMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertEquals("propertyName", typeDeserializerBase.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolverMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNotNull(typeDeserializerBase.getTypeIdResolver());
 }

 @Test
 public void testGetDefaultImplMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase.getDefaultImpl());
 }

 @Test
 public void testToStringMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverImpl; ]";
 assertEquals(expected, typeDeserializerBase.toString());
 }

 @Test
 public void testFindDeserializerMethodHappyPath() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 String typeId = "com.example.MyClass";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new TestContext(), typeId);
 assertNotNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerMethodNullTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new TestContext(), null);
 fail("Expected IOException but got " + deserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerMethodUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 String typeId = "com.unknown.UnknownClass";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new TestContext(), typeId);
 fail("Expected IOException but got " + deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializerMethodHappyPath() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 Class<?> defaultImpl = typeDeserializerBase._defaultImpl.getRawClass();
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(new TestContext());
 assertNotNull(deserializer);
 assertSame(defaultImpl, deserializer.getParameterType(0));
 }

 @Test(expected = IOException.class)
 public void testFindDefaultImplDeserializerMethodNullDefaultImpl() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(new TestContext());
 fail("Expected IOException but got " + deserializer);
 }

 @Test
 public void testHandleUnknownTypeIdMethodHappyPath() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 DeserializationContext ctxt = new TestContext();
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 String typeId = "com.example.MyOtherClass";
 JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, typeDeserializerBase.getTypeIdResolver(), javaType);
 assertNotNull(actual);
 assertEquals(String[].class, actual.getRawClass());
 JsonDeserializer<Object> deserializer = ctxt.findContextualValueDeserializer(actual, new BeanProperty.Std("propertyName", String.class));
 assertNotNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdMethodNullTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 DeserializationContext ctxt = new TestContext();
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, null, typeDeserializerBase.getTypeIdResolver(), javaType);
 fail("Expected IOException but got " + actual);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdMethodUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 DeserializationContext ctxt = new TestContext();
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 String typeId = "com.unknown.UnknownClass";
 JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, typeDeserializerBase.getTypeIdResolver(), javaType);
 fail("Expected IOException but got " + actual);
 }

 private class MyTypeDeserializerBase extends TypeDeserializerBase {
 // Empty implementation
 }

 private class TestContext extends DeserializationContext {
 @Override
 public void findValueDeserializer(Class<?> paramType, JsonDeserializer<?> valueDeserializer) {
 // Empty implementation
 }

 @Override
 public void findContextualValueDeserializer(JavaType targetType, BeanProperty property) throws IOException {
 JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(targetType, property.getName());
 if (deserializer != null) {
 super.findContextualValueDeserializer(targetType, property);
 }
 return deserializer;
 }

 @Override
 public void handleUnknownTypeId(JavaType baseType, String typeId, TypeIdResolver idResolver, String extraDesc)
 throws IOException {
 super.handleUnknownTypeId(baseType, typeId, idResolver, extraDesc);
 fail("Should not reach this point");
 }
 }
}