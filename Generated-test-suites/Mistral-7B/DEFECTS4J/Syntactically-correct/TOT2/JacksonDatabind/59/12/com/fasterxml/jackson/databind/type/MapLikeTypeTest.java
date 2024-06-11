import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test constructor with valid arguments
 @Test
 public void testConstructValidArgs() {
 Class<?> mapType = Map.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(mapType);
 JavaType[] superInts = null;
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass, superInts, keyType, valueType);
 assertEquals(mapType, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 // Test constructor with invalid arguments
 @Test(expected = IllegalArgumentException.class)
 public void testConstructInvalidArgs() {
 Class<?> mapType = Map.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(mapType);
 JavaType[] superInts = new JavaType[] { String.class };
 JavaType keyType = null;
 JavaType valueType = Integer.class;
 new MapLikeType(mapType, bindings, superClass, superInts, keyType, valueType);
 }

 // Test upgradeFrom method with valid arguments
 @Test
 public void testUpgradeFromValidArgs() {
 JavaType baseType = new ClassType(Map.class);
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 // Test upgradeFrom method with invalid arguments
 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidArgs() {
 JavaType baseType = new ArrayListType(Integer.class);
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 }

 // Test isContainerType method
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test isMapLikeType method
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test getKeyType method
 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 assertEquals(String.class, mapLikeType.getKeyType());
 }

 // Test getContentType method
 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 assertEquals(Integer.class, mapLikeType.getContentType());
 }

 // Test withKeyType method
 @Test
 public void testWithKeyType() {
 JavaType keyType = Integer.class;
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(keyType);
 assertEquals(keyType, newMapLikeType.getKeyType());
 }

 // Test withContentType method
 @Test
 public void testWithContentType() {
 JavaType valueType = Double.class;
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withContentType(valueType);
 assertEquals(valueType, newMapLikeType.getContentType());
 }

 // Test withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Object valueHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(valueHandler);
 assertEquals(valueHandler, newMapLikeType.getValueHandler());
 }

 // Test withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 Object contentTypeHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(contentTypeHandler);
 assertEquals(contentTypeHandler, newMapLikeType.getTypeHandler());
 }

 // Test withKeyTypeHandler method
 @Test
 public void testWithKeyTypeHandler() {
 Object keyTypeHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(keyTypeHandler);
 assertEquals(keyTypeHandler, newMapLikeType.getKeyTypeHandler());
 }

 // Test withKeyValueHandler method
 @Test
 public void testWithKeyValueHandler() {
 Object keyValueHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(keyValueHandler);
 assertEquals(keyValueHandler, newMapLikeType.getKeyValueHandler());
 }

 // Test isTrueMapType method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test toString method
 @Test
 public void testToString() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapLikeType.toString());
 }

 // Test equals method
 @Test
 public void testEquals() {
 MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null,
 String.class, Integer.class);
 MapLikeType mapLikeType3 = new MapLikeType(List.class, null, null, null,
 String.class, Integer.class);
 assertTrue(mapLikeType1.equals(mapLikeType2));
 assertFalse(mapLikeType1.equals(mapLikeType3));
 }
}