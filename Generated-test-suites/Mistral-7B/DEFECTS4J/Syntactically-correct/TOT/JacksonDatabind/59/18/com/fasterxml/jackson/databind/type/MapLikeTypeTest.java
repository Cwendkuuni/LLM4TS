import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.MapTypeBindings;

public class MapLikeTypeTest {

 @Test
 public void testMapLikeTypeCreation() {
 MapTypeBindings bindings = TypeBindings.stdBindings().without(MapTypeBindings.Std.STANDARD_MAP);
 JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(keyType, keyType, valueType);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testMapLikeTypeWithKeyType() {
 JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 MapLikeType mapLikeType = new MapLikeType(TypeBase.STANDARD_INSTANCE, keyType, valueType);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testMapLikeTypeWithContentType() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, String.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testMapLikeTypeWithKeyTypeHandler() {
 JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 Object keyHandler = new TypeDeserializer.MapKeyDeserializer(keyType);
 MapLikeType mapLikeType = new MapLikeType(TypeBase.STANDARD_INSTANCE, keyType, valueType, keyHandler, null, false);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 assertEquals(keyHandler, mapLikeType.getValueHandler());
 }

 @Test
 public void testMapLikeTypeWithContentTypeHandler() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, String.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 Object contentTypeHandler = new TypeDeserializer.MapEmptyCollectionDeserializer(valueType);
 MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType, null, null, contentTypeHandler, false);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 assertEquals(contentTypeHandler, mapLikeType.getTypeHandler());
 }

 @Test
 public void testMapLikeTypeWithValueHandler() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, String.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 Object valueHandler = new TypeDeserializer.MapValueDeserializer(valueType);
 MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType, null, valueHandler, false);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 assertEquals(valueHandler, mapLikeType.getValueHandler());
 }

 @Test
 public void testMapLikeTypeWithContentValueHandler() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, String.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 Object contentValueHandler = new TypeDeserializer.MapValueDeserializer(valueType);
 MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType, null, null, null, contentValueHandler, false);
 assertEquals(MapLikeType.class, mapLikeType.getClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 assertEquals(contentValueHandler, mapLikeType.getContentValueHandler());
 }

 @Test
 public void testMapLikeTypeWithStaticTyping() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType, null, null, null, null, false);
 MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
 assertEquals(MapLikeType.class, staticMapLikeType.getClass());
 assertTrue(staticMapLikeType.isStaticTyping());
 }

 @Test
 public void testMapLikeTypeIsMapLikeType() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType, null, null, null, null, false);
 assertTrue(mapLikeType.isMapLikeType());
 }

 @Test
 public void testMapLikeTypeIsContainerType() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType, null, null, null, null, false);
 assertTrue(mapLikeType.isContainerType());
 }

 @Test
 public void testMapLikeTypeEquals() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType keyType1 = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType1 = JavaType.constructParametricType(String.class, Integer.class);
 JavaType keyType2 = JavaType.constructParametricType(String.class, Integer.class);
 JavaType valueType2 = JavaType.constructParametricType(Integer.class, Integer.class);
 MapLikeType mapLikeType1 = new MapLikeType(mapType, keyType1, valueType1);
 MapLikeType mapLikeType2 = new MapLikeType(mapType, keyType2, valueType2);
 MapLikeType mapLikeType3 = new MapLikeType(mapType, keyType1, valueType1);
 assertTrue(mapLikeType1.equals(mapLikeType3));
 assertFalse(mapLikeType1.equals(mapLikeType2));
 }

 @Test
 public void testMapLikeTypeHashCode() {
 JavaType mapType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
 JavaType keyType = JavaType.constructParametricType(String.class, Class.class);
 JavaType valueType = JavaType.constructParametricType(String.class, Integer.class);
 MapLikeType mapLikeType1 = new MapLikeType(mapType, keyType, valueType);
 MapLikeType mapLikeType2 = new MapLikeType(mapType, keyType, valueType);
 assertEquals(mapLikeType1.hashCode(), mapLikeType2.hashCode());
 }
}