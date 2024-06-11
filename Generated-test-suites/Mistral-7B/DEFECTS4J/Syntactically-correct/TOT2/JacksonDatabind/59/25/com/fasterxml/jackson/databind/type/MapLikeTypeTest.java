import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for the constructor with Class, TypeBindings, JavaType, JavaType, Object, Object, boolean
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals("java.util.Map", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 assertFalse(mapLikeType.isStatic());
 }

 // Test case for the upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = new TypeFactory().constructType(Map.class, HashSet.class, String.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
 assertEquals(Map.class, mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for the isContainerType() method
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test case for the isMapLikeType() method
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test case for the getKeyType() method
 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals(String.class, mapLikeType.getKeyType());
 }

 // Test case for the getContentType() method
 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for the hasHandlers() method
 @Test
 public void testHasHandlers() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.hasHandlers());
 }

 // Test case for the isTrueMapType() method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for the withKeyType() method
 @Test
 public void testWithKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(Double.class);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(Double.class, newMapLikeType.getKeyType());
 }

 // Test case for the withContentType() method
 @Test
 public void testWithContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withContentType(Long.class);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(Long.class, newMapLikeType.getValueType());
 }

 // Test case for the withTypeHandler() method
 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(new MyMapTypeHandler());
 assertNotSame(mapLikeType, newMapLikeType);
 }

 // Test case for the withContentTypeHandler() method
 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(new MyMapContentTypeHandler());
 assertNotSame(mapLikeType, newMapLikeType);
 }

 // Test case for the withValueHandler() method
 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(new MyMapValueHandler());
 assertNotSame(mapLikeType, newMapLikeType);
 }

 // Test case for the withContentValueHandler() method
 @Test
 public void testWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(new MyMapContentValueHandler());
 assertNotSame(mapLikeType, newMapLikeType);
 }

 // Test case for the withStaticTyping() method
 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertNotSame(mapLikeType, newMapLikeType);
 assertTrue(newMapLikeType.isStatic());
 }

 // Test case for the refine method with Class, TypeBindings, JavaType, JavaType[], JavaType, boolean
 @Test
 public void testRefine() {
 JavaType javaType = new TypeFactory().constructType(Map.class, HashSet.class, String.class);
 MapLikeType mapLikeType = new MapLikeType(javaType, TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, null, null, null, null, false);
 MapLikeType refinedMapLikeType = mapLikeType.refine(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null);
 assertEquals(HashMap.class, refinedMapLikeType.getClassName());
 }

 // Test case for the buildCanonicalName method
 @Test
 public void testBuildCanonicalName() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals("[map-like type; class java.util.Map, Ljava/lang/String;Ljava/lang/Integer;>;", mapLikeType.buildCanonicalName().toString());
 }
}