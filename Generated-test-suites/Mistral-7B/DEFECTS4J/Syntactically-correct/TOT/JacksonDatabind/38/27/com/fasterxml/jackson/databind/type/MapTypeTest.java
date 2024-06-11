import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertEquals(MapType.class, mapType.getClass());
 assertEquals(rawType, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 @Test
 public void testConstructWithDeprecatedMethod() {
 Class<?> rawType = Map.class;
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = MapType.construct(rawType, keyType, valueType);
 assertEquals(MapType.class, mapType.getClass());
 assertEquals(rawType, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType originalMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 Object handler = new Object() { /* Handler implementation */ };
 MapType mapTypeWithHandler = originalMapType.withTypeHandler(handler);
 assertNotSame(originalMapType, mapTypeWithHandler);
 assertEquals(handler, mapTypeWithHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType contentType = TypeFactory.defaultInstance().constructType(String[].class);
 MapType originalMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 Object handler = new Object() { /* Handler implementation */ };
 MapType mapTypeWithHandler = originalMapType.withContentTypeHandler(handler);
 assertNotSame(originalMapType, mapTypeWithHandler);
 assertEquals(handler, mapTypeWithHandler.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType originalMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 Object handler = new Object() { /* Handler implementation */ };
 MapType mapTypeWithHandler = originalMapType.withValueHandler(handler);
 assertNotSame(originalMapType, mapTypeWithHandler);
 assertEquals(handler, mapTypeWithHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType contentValueType = TypeFactory.defaultInstance().constructType(String.class);
 MapType originalMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 Object handler = new Object() { /* Handler implementation */ };
 MapType mapTypeWithHandler = originalMapType.withContentValueHandler(handler);
 assertNotSame(originalMapType, mapTypeWithHandler);
 assertEquals(handler, mapTypeWithHandler.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType originalMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 MapType mapTypeWithStaticTyping = originalMapType.withStaticTyping();
 assertNotSame(originalMapType, mapTypeWithStaticTyping);
 assertTrue(mapTypeWithStaticTyping.isStaticTyping());
 }

 @Test
 public void testWithContentType() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType originalValueType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType newValueType = TypeFactory.defaultInstance().constructType(Long.class);
 MapType originalMapType = MapType.construct(rawType, bindings, null, null, keyType, originalValueType);
 MapType mapTypeWithNewValueType = originalMapType.withContentType(newValueType);
 assertNotSame(originalMapType, mapTypeWithNewValueType);
 assertEquals(newValueType, mapTypeWithNewValueType.getValueType());
 }

 @Test
 public void testWithKeyType() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType originalKeyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType newKeyType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType originalMapType = MapType.construct(rawType, bindings, null, null, originalKeyType, TypeFactory.defaultInstance().constructType(Integer.class));
 MapType mapTypeWithNewKeyType = originalMapType.withKeyType(newKeyType);
 assertNotSame(originalMapType, mapTypeWithNewKeyType);
 assertEquals(newKeyType, mapTypeWithNewKeyType.getKeyType());
 }

 @Test
 public void testRefine() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType originalMapType = MapType.construct(rawType, bindings, superClass, superInterfaces, keyType, valueType);
 Class<?> newRawType = HashMap.class;
 TypeBindings newBindings = TypeBindings.defaultBindings();
 JavaType newSuperClass = JavaType.constructType(Map.class, newRawType, newBindings, null, null, null, null, null, false);
 JavaType[] newSuperInterfaces = new JavaType[0];
 MapType refinedMapType = originalMapType.refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
 assertNotSame(originalMapType, refinedMapType);
 assertEquals(newRawType, refinedMapType.getRawClass());
 assertEquals(newBindings, refinedMapType.getBindings());
 assertEquals(newSuperClass, refinedMapType.getSuperclass());
 assertEquals(newSuperInterfaces, refinedMapType.getSuperinterfaces());
 assertEquals(keyType, refinedMapType.getKeyType());
 assertEquals(valueType, refinedMapType.getValueType());
 }
}