import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

 // Test case for constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInts, JavaType keyT, JavaType valueT
 @Test
 public void testConstruct() throws Exception {
 MapType mapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Integer.class), null,
 JavaType.construct(String.class), JavaType.construct(Integer.class));

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapType.toString());
 }

 // Test case for constructor with TypeBase, JavaType keyT, JavaType valueT
 @Test
 public void testConstructWithTypeBase() throws Exception {
 TypeBase mapBase = TypeBase.construct(Map.class, String.class, Object.class);
 MapType mapType = new MapType(mapBase, JavaType.construct(String.class), JavaType.construct(Integer.class));

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapType.toString());
 }

 // Test case for withTypeHandler method
 @Test
 public void testWithTypeHandler() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 MapType stringMapTypeWithHandler = stringMapType.withTypeHandler(new StringTypeDescriptor());

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Object", mapTypeWithHandler.toString());
 }

 // Test case for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 MapType stringMapTypeWithContentHandler = stringMapType.withContentTypeHandler(new StringTypeParser());

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Object", mapTypeWithContentHandler.toString());
 }

 // Test case for withValueHandler method
 @Test
 public void testWithValueHandler() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 MapType stringMapTypeWithHandler = stringMapType.withValueHandler(new IntegerValueHandler());

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithHandler.toString());
 }

 // Test case for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 MapType stringMapTypeWithContentHandler = stringMapType.withContentValueHandler(new IntegerContentValueHandler());

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithContentHandler.toString());
 }

 // Test case for withStaticTyping method
 @Test
 public void testWithStaticTyping() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 MapType stringMapTypeStatic = stringMapType.withStaticTyping();

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Object", mapTypeStatic.toString());
 }

 // Test case for withContentType method
 @Test
 public void testWithContentType() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 JavaType stringIntegerMapType = stringMapType.withContentType(JavaType.construct(Integer.class));

 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithContentType.toString());
 }

 // Test case for withKeyType method
 @Test
 public void testWithKeyType() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 JavaType integerMapType = stringMapType.withKeyType(JavaType.construct(Integer.class));

 assertEquals("map type; class class java.util.Map, java.lang.Integer -> java.lang.Object", mapTypeWithKeyType.toString());
 }

 // Test case for refine method
 @Test
 public void testRefine() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 JavaType integerMapType = stringMapType.refine(IntegerMap.class, new TypeBindings(), null, null);

 assertEquals("map type; class class java.util.Map, java.lang.Integer -> java.lang.Object", mapTypeWithRefine.toString());
 }

 // Test case for withKeyTypeHandler method
 @Test
 public void testWithKeyTypeHandler() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 MapType stringMapTypeWithKeyHandler = stringMapType.withKeyTypeHandler(new IntegerTypeHandler());

 assertEquals("map type; class class java.util.Map, java.lang.Integer -> java.lang.Object", mapTypeWithKeyHandler.toString());
 }

 // Test case for withKeyValueHandler method
 @Test
 public void testWithKeyValueHandler() throws Exception {
 JavaType stringMapType = MapType.construct(Map.class, new TypeBindings(),
 JavaType.construct(HashMap.class, String.class, Object.class), null,
 JavaType.construct(String.class), JavaType.construct(Object.class));
 MapType stringMapTypeWithKeyValueHandler = stringMapType.withKeyValueHandler(new IntegerKeyValueHandler());

 assertEquals("map type; class class java.util.Map, java.lang.Integer -> java.lang.Object", mapTypeWithKeyValueHandler.toString());
 }
}