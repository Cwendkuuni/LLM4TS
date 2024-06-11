import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 @Test
 public void testConstruct() {
 MapType mapType = MapType.construct(Map.class, null, String.class, Integer.class);
 JavaType keyType = mapType.getKeyType();
 JavaType valueType = mapType.getValueType();
 assertNotNull(mapType);
 assertNotNull(keyType);
 assertNotNull(valueType);
 assertEquals(Map.class, mapType.getRawClass());
 }

 @Test
 public void testConstructWithBindings() {
 TypeBindings bindings = new TypeBindings();
 MapType mapType = MapType.construct(Map.class, bindings, String.class, Integer.class);
 JavaType keyType = mapType.getKeyType();
 JavaType valueType = mapType.getValueType();
 assertNotNull(mapType);
 assertNotNull(keyType);
 assertNotNull(valueType);
 assertEquals(Map.class, mapType.getRawClass());
 }

 @Test
 public void testConstructDeprecated() {
 JavaType keyType = JavaType.constructType(String.class, Map.class);
 JavaType valueType = JavaType.constructType(Integer.class, Map.class);
 MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
 JavaType refinedMapType = mapType.refine(HashMap.class, null, null, null);
 assertNotNull(mapType);
 assertNotNull(refinedMapType);
 assertEquals(HashMap.class, refinedMapType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidSuperClass() {
 JavaType keyType = JavaType.constructType(String.class, Map.class);
 JavaType valueType = JavaType.constructType(Integer.class, Map.class);
 try {
 MapType mapType = new MapType(List.class, null, null, null, keyType, valueType);
 fail("Should not create MapType with an invalid superclass");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testWithTypeHandler() {
 MapType mapType = new MapType(Map.class, null, String.class, Integer.class);
 Object handler = new MapHandler();
 MapType newMapType = mapType.withTypeHandler(handler);
 assertNotNull(newMapType);
 assertSame(handler, newMapType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 MapType mapType = new MapType(Map.class, null, String.class, Integer.class);
 Object handler = new ValueHandler();
 MapType newMapType = mapType.withValueHandler(handler);
 assertNotNull(newMapType);
 assertSame(handler, newMapType.getValueHandler());
 }
}