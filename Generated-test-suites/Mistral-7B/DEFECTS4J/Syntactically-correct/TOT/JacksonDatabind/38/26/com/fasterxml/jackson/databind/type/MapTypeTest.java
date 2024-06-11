import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
 assertEquals(mapType, mapTypeInstance.getRawClass());
 assertEquals(keyType, mapTypeInstance.getKeyType());
 assertEquals(valueType, mapTypeInstance.getValueType());
 }

 @Test
 public void testConstructDeprecatedWithValidArguments() {
 Class<Map<String, Integer>> mapType = Map.class;
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapType mapTypeInstance = MapType.construct(mapType, keyType, valueType);
 assertEquals(mapType, mapTypeInstance.getRawClass());
 assertEquals(keyType, mapTypeInstance.getKeyType());
 assertEquals(valueType, mapTypeInstance.getValueType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidSuperclass() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = String.class; // invalid superclass
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapType mapTypeInstance = null;
 try {
 mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
 } catch (IllegalArgumentException e) {
 // expected exception
 fail("IllegalArgumentException not thrown");
 }
 assertNull(mapTypeInstance);
 }

 @Test
 public void testWithTypeHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 Object valueHandler = new Object();
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType mapTypeWithValueHandler = mapTypeInstance.withTypeHandler(valueHandler);
 assertEquals(mapTypeInstance.getRawClass(), mapTypeWithValueHandler.getRawClass());
 assertEquals(mapTypeInstance.getKeyType(), mapTypeWithValueHandler.getKeyType());
 assertEquals(mapTypeInstance.getValueType(), mapTypeWithValueHandler.getValueType());
 assertEquals(valueHandler, mapTypeWithValueHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 JavaType contentTypeHandler = new JavaType(String[].class);
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType mapTypeWithContentTypeHandler = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
 assertEquals(mapTypeInstance.getRawClass(), mapTypeWithContentTypeHandler.getRawClass());
 assertEquals(mapTypeInstance.getKeyType(), mapTypeWithContentTypeHandler.getKeyType());
 assertEquals(mapTypeWithContentTypeHandler.getValueType(), contentTypeHandler);
 }

 @Test
 public void testWithValueHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 Object valueHandler = new Object();
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType mapTypeWithValueHandler = mapTypeInstance.withValueHandler(valueHandler);
 assertEquals(mapTypeInstance.getRawClass(), mapTypeWithValueHandler.getRawClass());
 assertEquals(mapTypeInstance.getKeyType(), mapTypeWithValueHandler.getKeyType());
 assertEquals(mapTypeInstance.getValueType(), mapTypeWithValueHandler.getValueType());
 assertEquals(valueHandler, mapTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 Object contentValueHandler = new Object();
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType mapTypeWithContentValueHandler = mapTypeInstance.withContentValueHandler(contentValueHandler);
 assertEquals(mapTypeInstance.getRawClass(), mapTypeWithContentValueHandler.getRawClass());
 assertEquals(mapTypeInstance.getKeyType(), mapTypeWithContentValueHandler.getKeyType());
 assertEquals(mapTypeInstance.getValueType(), mapTypeWithContentValueHandler.getValueType());
 assertEquals(contentValueHandler, mapTypeWithContentValueHandler.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class.getType(); // static typing for key type
 JavaType valueType = Integer.class.getType(); // static typing for value type
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType mapTypeWithStaticTyping = mapTypeInstance.withStaticTyping();
 assertEquals(mapTypeInstance.getRawClass(), mapTypeWithStaticTyping.getRawClass());
 assertTrue(mapTypeWithStaticTyping.isStatic());
 }

 @Test
 public void testWithContentType() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 JavaType contentType = new JavaType(String[].class);
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType mapTypeWithContentType = mapTypeInstance.withContentType(contentType);
 assertEquals(mapTypeInstance.getRawClass(), mapTypeWithContentType.getRawClass());
 assertEquals(mapTypeInstance.getKeyType(), mapTypeWithContentType.getKeyType());
 assertEquals(contentType, mapTypeWithContentType.getValueType());
 }

 @Test
 public void testWithKeyType() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 JavaType newKeyType = StringBuffer.class;
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType mapTypeWithNewKeyType = mapTypeInstance.withKeyType(newKeyType);
 assertEquals(mapTypeInstance.getRawClass(), mapTypeWithNewKeyType.getRawClass());
 assertEquals(newKeyType, mapTypeWithNewKeyType.getKeyType());
 assertEquals(mapTypeInstance.getValueType(), mapTypeWithNewKeyType.getValueType());
 }

 @Test
 public void testRefine() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 Class<Map<String, Long>> refinedMapType = Map.class; // subclass of Map<String, Integer>
 TypeBindings newBindings = new TypeBindings();
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 JavaType refinedMapTypeInstance = mapTypeInstance.refine(refinedMapType.getType(), newBindings, superClass, superInts);
 assertEquals(refinedMapType, refinedMapTypeInstance.getRawClass());
 assertEquals(keyType, refinedMapTypeInstance.getKeyType());
 assertEquals(valueType, refinedMapTypeInstance.getValueType());
 }

 @Test
 public void testToString() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
 assertEquals("[map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer]", mapTypeInstance.toString());
 }
}