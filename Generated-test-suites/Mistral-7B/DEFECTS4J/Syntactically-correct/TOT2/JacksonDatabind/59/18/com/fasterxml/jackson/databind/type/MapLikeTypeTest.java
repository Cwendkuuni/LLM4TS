import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for the constructor with Class, TypeBindings, JavaType, JavaType, Object, Object, boolean
 @Test
 public void testMapLikeTypeConstructor() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals("java.util.Map", mapLikeType.getCanonicalName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for the upgradeFrom method
 @Test
 public void testUpgradeFromMethod() {
 JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
 assertEquals(javaType.getRawClass(), mapLikeType.getRawClass());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for the isTrueMapType method
 @Test
 public void testIsTrueMapTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for the getCanonicalName method
 @Test
 public void testGetCanonicalNameMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getCanonicalName());
 }

 // Test case for the getErasedSignature method
 @Test
 public void testGetErasedSignatureMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 StringBuilder erasedSignature = new StringBuilder();
 mapLikeType.getErasedSignature(erasedSignature);
 assertEquals("java.util.Map", erasedSignature.toString());
 }

 // Test case for the getGenericSignature method
 @Test
 public void testGetGenericSignatureMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 StringBuilder genericSignature = new StringBuilder();
 mapLikeType.getGenericSignature(genericSignature);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", genericSignature.toString());
 }

 // Test case for the isContainerType method
 @Test
 public void testIsContainerTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test case for the isMapLikeType method
 @Test
 public void testIsMapLikeTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test case for the getKeyType method
 @Test
 public void testGetKeyTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals(String.class, mapLikeType.getKeyType());
 }

 // Test case for the getValueType method
 @Test
 public void testGetValueTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for the hasHandlers method
 @Test
 public void testHasHandlersMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertFalse(mapLikeType.hasHandlers());
 }

 // Test case for the withKeyType method
 @Test
 public void testWithKeyTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(Double.class);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(Double.class, newMapLikeType.getKeyType());
 }

 // Test case for the withContentType method
 @Test
 public void testWithContentTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withContentType(Boolean.class);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(Boolean.class, newMapLikeType.getValueType());
 }

 // Test case for the withTypeHandler method
 @Test
 public void testWithTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(handler, newMapLikeType.getTypeHandler());
 }

 // Test case for the withContentTypeHandler method
 @Test
 public void testWithContentTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
 }

 // Test case for the withValueHandler method
 @Test
 public void testWithValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(handler, newMapLikeType.getValueHandler());
 }

 // Test case for the withContentValueHandler method
 @Test
 public void testWithContentValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
 assertNotSame(mapLikeType, newMapLikeType);
 assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
 }

 // Test case for the withStaticTyping method
 @Test
 public void testWithStaticTypingMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertNotSame(mapLikeType, newMapLikeType);
 assertTrue(newMapLikeType.isStaticTyping());
 }

 // Test case for the refine method
 @Test
 public void testRefineMethod() {
 JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = (MapLikeType) javaType.refine(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null);
 assertEquals(javaType.getRawClass(), mapLikeType.getRawClass());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }
}