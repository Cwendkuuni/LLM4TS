import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

 // Test case for construct method
 @Test
 public void testConstruct() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
 assertEquals("Test for MapType construct method with valid arguments", mapType.getClass(), mapClass);
 assertEquals("Test for MapType keyType", mapType.getKeyType(), keyType);
 assertEquals("Test for MapType valueType", mapType.getValueType(), valueType);
 }

 // Test case for construct deprecated method
 @Test(expected = UnsupportedOperationException.class)
 public void testConstructDeprecated() {
 Class<Map<String, Integer>> mapClass = Map.class;
 JavaType keyType = JavaType.constructType(String.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapType mapType = MapType.construct(mapClass, null, null, null, keyType, valueType);
 fail("Test for MapType construct deprecated method should not execute");
 }

 // Test case for _narrow method
 @Test
 public void testNarrow() {
 Class<HashMap<String, Integer>> hashMapClass = HashMap.class;
 MapType mapType = new MapType(Map.class, null, null, null, JavaType.constructType(String.class), JavaType.constructType(Integer.class));
 JavaType narrowedType = mapType._narrow(hashMapClass);
 assertEquals("Test for MapType _narrow method with valid arguments", narrowedType.getClass(), MapType.class);
 assertEquals("Test for narrowed type keyType", narrowedType.getKeyType(), mapType.getKeyType());
 assertEquals("Test for narrowed type valueType", narrowedType.getValueType(), mapType.getValueType());
 }

 // Test case for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, valueType, null, null, false);
 Object typeHandler = new MyMapTypeHandler();
 MapType newMapType = mapType.withTypeHandler(typeHandler);
 assertEquals("Test for MapType withTypeHandler method with valid arguments", newMapType.getTypeHandler(), typeHandler);
 assertEquals("Test for MapType keyType", newMapType.getKeyType(), keyType);
 assertEquals("Test for MapType valueType", newMapType.getValueType(), valueType);
 }

 // Test case for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 JavaType contentTypeHandler = JavaType.constructType(MyContentTypeHandler.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
 assertEquals("Test for MapType withContentTypeHandler method with valid arguments", newMapType.getContentTypeHandler(), contentTypeHandler);
 assertEquals("Test for MapType keyType", newMapType.getKeyType(), keyType);
 assertEquals("Test for MapType valueType", newMapType.getValueType(), valueType);
 }

 // Test case for withValueHandler method
 @Test
 public void testWithValueHandler() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, valueType, null, null, false);
 Object valueHandler = new MyValueHandler();
 MapType newMapType = mapType.withValueHandler(valueHandler);
 assertEquals("Test for MapType withValueHandler method with valid arguments", newMapType.getValueHandler(), valueHandler);
 assertEquals("Test for MapType keyType", newMapType.getKeyType(), keyType);
 assertEquals("Test for MapType valueType", newMapType.getValueType(), valueType);
 }

 // Test case for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 JavaType contentValueHandler = JavaType.constructType(MyContentValueHandler.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withContentValueHandler(contentValueHandler);
 assertEquals("Test for MapType withContentValueHandler method with valid arguments", newMapType.getContentValueHandler(), contentValueHandler);
 assertEquals("Test for MapType keyType", newMapType.getKeyType(), keyType);
 assertEquals("Test for MapType valueType", newMapType.getValueType(), valueType);
 }

 // Test case for withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, valueType, null, null, false);
 MapType staticMapType = mapType.withStaticTyping();
 assertTrue("Test for MapType withStaticTyping method with valid arguments", staticMapType.isStatic());
 assertEquals("Test for staticMapType keyType", staticMapType.getKeyType(), keyType);
 assertEquals("Test for staticMapType valueType", staticMapType.getValueType(), valueType);
 }

 // Test case for withContentType method
 @Test
 public void testWithContentType() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType contentType = JavaType.constructType(String.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, JavaType.constructType(Integer.class), null, null, false);
 MapType newMapType = mapType.withContentType(contentType);
 assertEquals("Test for MapType withContentType method with valid arguments", newMapType.getValueType(), contentType);
 assertEquals("Test for newMapType keyType", newMapType.getKeyType(), keyType);
 }

 // Test case for withKeyType method
 @Test
 public void testWithKeyType() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType oldKeyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType newKeyType = JavaType.constructCollectionType(Map.class, Integer.class, String.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, oldKeyType, JavaType.constructType(Integer.class), null, null, false);
 MapType newMapType = mapType.withKeyType(newKeyType);
 assertEquals("Test for MapType withKeyType method with valid arguments", newMapType.getKeyType(), newKeyType);
 assertEquals("Test for newMapType valueType", newMapType.getValueType(), mapType.getValueType());
 }

 // Test case for refine method
 @Test
 public void testRefine() {
 Class<HashMap<Integer, String>> hashMapClass = HashMap.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, Integer.class, String.class);
 JavaType valueType = JavaType.constructType(String.class);
 MapType mapType = new MapType(Map.class, bindings, null, null, keyType, valueType, null, null, false);
 JavaType refinedType = mapType.refine(hashMapClass, bindings, null, null);
 assertEquals("Test for MapType refine method with valid arguments", refinedType.getClass(), MapType.class);
 assertEquals("Test for refinedType keyType", refinedType.getKeyType(), keyType);
 assertEquals("Test for refinedType valueType", refinedType.getValueType(), valueType);
 }

 // Test case for withKeyTypeHandler method
 @Test
 public void testWithKeyTypeHandler() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, valueType, null, null, false);
 Object keyTypeHandler = new MyKeyTypeHandler();
 MapType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
 assertEquals("Test for MapType withKeyTypeHandler method with valid arguments", newMapType.getKeyTypeHandler(), keyTypeHandler);
 assertEquals("Test for newMapType keyType", newMapType.getKeyType(), keyType);
 assertEquals("Test for newMapType valueType", newMapType.getValueType(), valueType);
 }

 // Test case for withKeyValueHandler method
 @Test
 public void testWithKeyValueHandler() {
 Class<Map<String, Integer>> mapClass = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapType mapType = new MapType(mapClass, bindings, null, null, keyType, valueType, null, null, false);
 Object keyValueHandler = new MyKeyValueHandler();
 MapType newMapType = mapType.withKeyValueHandler(keyValueHandler);
 assertEquals("Test for MapType withKeyValueHandler method with valid arguments", newMapType.getKeyValueHandler(), keyValueHandler);
 assertEquals("Test for newMapType keyType", newMapType.getKeyType(), keyType);
 assertEquals("Test for newMapType valueType", newMapType.getValueType(), valueType);
 }
}