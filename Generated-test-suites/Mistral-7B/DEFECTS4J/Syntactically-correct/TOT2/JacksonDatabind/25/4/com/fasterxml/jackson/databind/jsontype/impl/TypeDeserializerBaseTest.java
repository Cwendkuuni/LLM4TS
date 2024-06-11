import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 typeDeserializerBase = new TypeDeserializerBaseTestType(
 Object.class, null, null, false, String.class
 );
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty.Std("testProperty", String.class);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
 assertEquals(String.class, typeDeserializer.getType(null));
 }

 @Test
 public void testGetTypeInclusion() {
 assertNull(typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("baseTypeName", "java.lang.String");
 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 Object result = typeDeserializerBase.deserialize(jsonParser, null);
 assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testToString() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("getClass", TypeDeserializerBase.class.getName());
 rootNode.put("baseType", "java.lang.String");
 rootNode.put("idResolver", "com.fasterxml.jackson.databind.DefaultTypeIdResolver@1b6d7482");
 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 Object result = typeDeserializerBase.deserialize(jsonParser, null);
 assertEquals("[TypeDeserializerBase; base-type:java.lang.String; id-resolver:com.fasterxml.jackson.databind.DefaultTypeIdResolver@1b6d7482]", typeDeserializerBase.toString());
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNullContext() throws Exception {
 String typeId = "com.example.MyClass";
 typeDeserializerBase._findDeserializer(null, typeId);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNullTypeId() throws Exception {
 DeserializationContext context = objectMapper.getFactory().createContext();
 typeDeserializerBase._findDeserializer(context, null);
 }

 @Test
 public void testFindDeserializerWithDeserializer() throws Exception {
 String typeId = "com.example.MyClass";
 JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
 typeDeserializerBase._deserializers.put(typeId, deserializer);
 DeserializationContext context = objectMapper.getFactory().createContext();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 Object result = typeDeserializerBase._findDeserializer(context, typeId).deserialize(jsonParser, context);
 assertTrue(result instanceof StubObject);
 }

 @Test
 public void testFindDefaultImplDeserializerNullContext() throws Exception {
 DeserializationContext context = objectMapper.getFactory().createContext();
 assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
 }

 @Test
 public void testFindDefaultImplDeserializerWithDeserializer() throws Exception {
 DeserializationContext context = objectMapper.getFactory().createContext();
 JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
 typeDeserializerBase._defaultImplDeserializer = deserializer;
 Object result = typeDeserializerBase._findDefaultImplDeserializer(context).deserialize(null, context);
 assertTrue(result instanceof StubObject);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, null);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdNullParser() throws Exception {
 typeDeserializerBase._deserializeWithNativeTypeId(null, null);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 String typeId = "com.example.MyClass";
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("typeId", typeId);
 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, null);
 assertTrue(result instanceof StubObject);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdNullTypeIdResolver() throws Exception {
 String typeId = "com.example.MyClass";
 typeDeserializerBase._handleUnknownTypeId(null, typeId, null, null);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdNullBaseType() throws Exception {
 String typeId = "com.example.MyClass";
 typeDeserializerBase._handleUnknownTypeId(null, typeId, null, null);
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 String typeId = "com.example.MyClass";
 typeDeserializerBase._handleUnknownTypeId(objectMapper.getFactory().createContext(), typeId, new DefaultTypeIdResolver(), null);
 }
}

class StubJsonDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
 return new StubObject();
 }
}

class StubObject {
}