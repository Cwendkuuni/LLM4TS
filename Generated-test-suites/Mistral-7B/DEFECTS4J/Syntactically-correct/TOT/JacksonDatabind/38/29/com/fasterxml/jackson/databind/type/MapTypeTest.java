import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 @Test
 public void testConstructWithValidTypes() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = BasicTypeBindings.StringType;
 JavaType valueType = BasicTypeBindings.IntegerType;
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertEquals(Map.class, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 @Test
 public void testConstructWithBogusSuperClass() {
 Class<Map<String, Integer>> rawType = Map.class;
 JavaType keyType = BasicTypeBindings.StringType;
 JavaType valueType = BasicTypeBindings.IntegerType;
 MapType mapType = MapType.construct(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType);
 assertNull(mapType.getSuperClass());
 }

 @Test
 public void testNarrowWithSubclass() {
 Class<Map<String, Integer>> subclass = HashMap.class;
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 MapType narrowedMapType = mapType.narrow(subclass);
 assertEquals(HashMap.class, narrowedMapType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidClass() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 mapType.narrow(String.class);
 }

 @Test
 public void testWithTypeHandler() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 Object typeHandler = new MapTypeTestTypeHandler();
 MapType withTypeHandler = mapType.withTypeHandler(typeHandler);
 assertEquals(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 Object contentTypeHandler = new MapTypeTestContentTypeHandler();
 MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
 assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 Object valueHandler = new MapTypeTestValueHandler();
 MapType withValueHandler = mapType.withValueHandler(valueHandler);
 assertEquals(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 Object contentValueHandler = new MapTypeTestContentValueHandler();
 MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
 assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 MapType withStaticTyping = mapType.withStaticTyping();
 assertTrue(withStaticTyping.isStatic());
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = BasicTypeBindings.StringType;
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 MapType withContentType = mapType.withContentType(contentType);
 assertEquals(contentType, withContentType.getValueType());
 }

 @Test
 public void testWithKeyType() {
 JavaType keyType = BasicTypeBindings.StringType.withStaticTyping();
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 MapType withKeyType = mapType.withKeyType(keyType);
 assertEquals(keyType, withKeyType.getKeyType());
 }

 @Test
 public void testRefine() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInterfaces = null;
 JavaType keyType = BasicTypeBindings.StringType;
 JavaType valueType = BasicTypeBindings.IntegerType;
 MapType mapType = MapType.construct(rawType, bindings, superClass, superInterfaces, keyType, valueType);
 MapType refinedMapType = mapType.refine(HashMap.class, bindings, null, null);
 assertEquals(HashMap.class, refinedMapType.getRawClass());
 }

 @Test
 public void testWithKeyTypeHandler() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 Object keyTypeHandler = new MapTypeTestKeyTypeHandler();
 MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
 assertEquals(keyTypeHandler, withKeyTypeHandler.getKeyTypeHandler());
 }

 @Test
 public void testWithKeyValueHandler() {
 Class<Map<String, Integer>> mapType = MapType.construct(Map.class, null, null, null,
 BasicTypeBindings.StringType, BasicTypeBindings.IntegerType);
 Object keyValueHandler = new MapTypeTestKeyValueHandler();
 MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
 assertEquals(keyValueHandler, withKeyValueHandler.getKeyValueHandler());
 }

 private static Class<?> _bogusSuperClass(Class<?> clazz) {
 throw new UnsupportedOperationException("Cannot instantiate bogus superclass for: " + clazz.getName());
 }
}