import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for constructor with Class, TypeBindings, JavaType[], JavaType, JavaType
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals("java.util.Map", mapLikeType.getCanonicalName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", mapLikeType.getClass().getName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for isMapLikeType method
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test case for getKeyType method
 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(String.class, mapLikeType.getKeyType());
 }

 // Test case for getValueType method
 @Test
 public void testGetValueType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for isContainerType method
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test case for isTrueMapType method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for withKeyType method
 @Test
 public void testWithKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(Double.class);
 assertNotEquals(mapLikeType, newMapLikeType);
 assertEquals(Double.class, newMapLikeType.getKeyType());
 }

 // Test case for withContentType method
 @Test
 public void testWithContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withContentType(Boolean.class);
 assertNotEquals(mapLikeType, newMapLikeType);
 assertEquals(Boolean.class, newMapLikeType.getValueType());
 }

 // Test case for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 mapLikeType = mapLikeType.withTypeHandler(new MyTypeHandler());
 assertNotNull(mapLikeType.getTypeHandler());
 }

 // Test case for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 mapLikeType = mapLikeType.withContentTypeHandler(new MyContentTypeHandler());
 assertNotNull(mapLikeType.getContentTypeHandler());
 }

 // Test case for withValueHandler method
 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 mapLikeType = mapLikeType.withValueHandler(new MyValueHandler());
 assertNotNull(mapLikeType.getValueHandler());
 }

 // Test case for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 mapLikeType = mapLikeType.withContentValueHandler(new MyContentValueHandler());
 assertNotNull(mapLikeType.getContentValueHandler());
 }

 // Test case for withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertNotEquals(mapLikeType, newMapLikeType);
 assertTrue(newMapLikeType.isStaticTyping());
 }
}