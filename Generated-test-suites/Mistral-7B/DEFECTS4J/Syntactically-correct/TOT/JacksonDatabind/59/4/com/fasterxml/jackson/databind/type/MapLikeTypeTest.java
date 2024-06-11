import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 @Test
 public void testMapLikeTypeCreation() {
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(String[].class, String.class, Integer.class);
 assertEquals("java.lang.Class<java.lang.String[]>, java.lang.String, java.lang.Integer", mapLikeType.toString());
 }

 @Test
 public void testMapLikeTypeWithKeyType() {
 JavaType javaType = new SimpleModule().addTypeHandler(String.class, new StringTypeHandler()).build().getTypeFactory()
 .constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = (MapLikeType) javaType;
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.toString());
 mapLikeType = mapLikeType.withKeyType(Integer.class);
 assertEquals("java.util.Map<java.lang.Integer, java.lang.Integer>", mapLikeType.toString());
 }

 @Test
 public void testMapLikeTypeWithValueType() {
 JavaType javaType = new SimpleModule().addTypeHandler(String.class, new StringTypeHandler())
 .build().getTypeFactory().constructType(Map.class, String.class, Double.class);
 MapLikeType mapLikeType = (MapLikeType) javaType;
 assertEquals("java.util.Map<java.lang.String, java.lang.Double>", mapLikeType.toString());
 mapLikeType = mapLikeType.withContentType(Float.class);
 assertEquals("java.util.Map<java.lang.String, java.lang.Float>", mapLikeType.toString());
 }

 @Test
 public void testMapLikeTypeWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 mapLikeType = mapLikeType.withTypeHandler(new StringTypeHandler());
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>[typeHandler=com.example.StringTypeHandler]", mapLikeType.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapLikeTypeInvalidCreation() {
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(int[].class, String.class, Integer.class);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testMapLikeTypeEquals() {
 MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType1.equals(mapLikeType2));
 MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(), _bogusSuperClass(List.class), null, String.class, Integer.class);
 assertFalse(mapLikeType1.equals(mapLikeType3));
 }
}