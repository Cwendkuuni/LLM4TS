import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 @Test
 public void testConstruct() {
 Class<?> rawType = Map.class; // Java standard Map implementation
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType expectedMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertEquals(expectedMapType, actualMapType);
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType actualMapType = MapType.construct(rawType, keyType, valueType);
 MapType expectedMapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
 // Assuming _bogusSuperClass is replaced with a valid superclass for testing purposes
 assertEquals(expectedMapType, actualMapType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullKeyOrValueType() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = null;
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullSuperClass() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testWithTypeHandler() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 Object handler = new MyMapTypeHandler();
 MapType newMapType = mapType.withTypeHandler(handler);
 assertEquals(mapType.getTypeHandler(), null);
 assertEquals(newMapType.getTypeHandler(), handler);
 }

 @Test
 public void testWithContentTypeHandler() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, JavaType.construct(Long.class, null)));
 Object handler = new MyMapValueTypeHandler();
 MapType newMapType = mapType.withContentTypeHandler(handler);
 assertEquals(mapType.getContentTypeHandler(), null);
 assertEquals(newMapType.getContentTypeHandler(), handler);
 }

 @Test
 public void testWithValueHandler() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 Object handler = new MyMapValueHandler();
 MapType newMapType = mapType.withValueHandler(handler);
 assertEquals(mapType.getValueHandler(), null);
 assertEquals(newMapType.getValueHandler(), handler);
 }

 @Test
 public void testWithContentValueHandler() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, JavaType.construct(Long.class, null)));
 Object handler = new MyMapContentValueHandler();
 MapType newMapType = mapType.withContentValueHandler(handler);
 assertEquals(mapType.getContentValueHandler(), null);
 assertEquals(newMapType.getContentValueHandler(), handler);
 }

 @Test
 public void testWithStaticTyping() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 MapType newMapType = mapType.withStaticTyping();
 assertTrue(newMapType.isStatic());
 assertFalse(mapType.isStatic());
 }

 @Test
 public void testWithContentType() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 JavaType contentType = JavaType.construct(Float.class, null);
 MapType newMapType = mapType.withContentType(contentType);
 assertNotSame(mapType, newMapType);
 assertEquals(newMapType.getValueType(), contentType);
 }

 @Test
 public void testWithKeyType() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 JavaType newKeyType = JavaType.construct(Character.class, null);
 MapType newMapType = mapType.withKeyType(newKeyType);
 assertNotSame(mapType, newMapType);
 assertEquals(newMapType.getKeyType(), newKeyType);
 }

 @Test
 public void testRefine() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 Class<?> rawType = HashMap.class; // Java standard HashMap implementation
 TypeBindings newBindings = TypeBindings.empty();
 JavaType superClass = JavaType.construct(HashMap.class, null);
 JavaType[] superInterfaces = new JavaType[0];
 MapType newMapType = mapType.refine(rawType, newBindings, superClass, superInterfaces);
 assertEquals(newMapType.getRawType(), rawType);
 assertEquals(newMapType.getClass(), MapType.class);
 assertNotSame(mapType, newMapType);
 }

 @Test
 public void testKeyTypeHandler() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 Object handler = new MyMapKeyTypeHandler();
 MapType newMapType = mapType.withKeyTypeHandler(handler);
 assertEquals(mapType.getKeyTypeHandler(), null);
 assertEquals(newMapType.getKeyTypeHandler(), handler);
 }

 @Test
 public void testKeyValueHandler() {
 MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
 Object handler = new MyMapKeyValueHandler();
 MapType newMapType = mapType.withKeyValueHandler(handler);
 assertEquals(mapType.getKeyValueHandler(), null);
 assertEquals(newMapType.getKeyValueHandler(), handler);
 }
}