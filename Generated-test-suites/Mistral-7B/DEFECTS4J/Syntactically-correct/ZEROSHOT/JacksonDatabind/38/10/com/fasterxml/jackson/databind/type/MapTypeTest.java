import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class MapTypeTest {

 @Test
 public void testConstruct() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = Mockito.mock(TypeBindings.class);
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);

 assertEquals(Map.class, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 assertNotNull(mapType.getSuperClass());
 assertNotNull(mapType.getSuperInterfaces());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = Map.class;
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = MapType.construct(rawType, keyType, valueType);

 assertEquals(Map.class, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 assertNotNull(mapType.getSuperClass());
 assertNotNull(mapType.getSuperInterfaces());
 }

 @Test
 public void testNarrow() {
 MapType parentMapType = MapType.construct(HashMap.class, null, null, null,
 String.class, Integer.class);
 MapType childMapType = parentMapType.narrow(LinkedHashMap.class);

 assertEquals(LinkedHashMap.class, childMapType.getRawClass());
 assertEquals(parentMapType.getKeyType(), childMapType.getKeyType());
 assertEquals(parentMapType.getValueType(), childMapType.getValueType());
 assertNotNull(childMapType.getSuperClass());
 assertNotNull(childMapType.getSuperInterfaces());
 }

 @Test
 public void testWithTypeHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = Mockito.mock(Object.class);
 MapType mapTypeWithHandler = mapType.withTypeHandler(handler);

 assertEquals(mapType.getRawClass(), mapTypeWithHandler.getRawClass());
 assertEquals(mapType.getKeyType(), mapTypeWithHandler.getKeyType());
 assertEquals(mapType.getValueType(), mapTypeWithHandler.getValueType());
 assertEquals(handler, mapTypeWithHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = Mockito.mock(Object.class);
 JavaType contentType = TypeFactory.defaultInstance().constructType(Long.class);
 MapType mapTypeWithContentTypeHandler = mapType.withContentTypeHandler(handler);
 MapType mapTypeWithContentType = mapTypeWithContentTypeHandler.withContentType(contentType);

 assertEquals(mapType.getRawClass(), mapTypeWithContentType.getRawClass());
 assertEquals(mapType.getKeyType(), mapTypeWithContentType.getKeyType());
 assertEquals(contentType, mapTypeWithContentType.getValueType());
 assertEquals(handler, mapTypeWithContentType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = Mockito.mock(Object.class);
 MapType mapTypeWithHandler = mapType.withValueHandler(handler);

 assertEquals(mapType.getRawClass(), mapTypeWithHandler.getRawClass());
 assertEquals(mapType.getKeyType(), mapTypeWithHandler.getKeyType());
 assertEquals(mapType.getValueType(), mapTypeWithHandler.getValueType());
 assertEquals(handler, mapTypeWithHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = Mockito.mock(Object.class);
 JavaType valueTypeWithHandler = mapType.getValueType().withValueHandler(handler);
 MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(handler);

 assertEquals(mapType.getRawClass(), mapTypeWithContentValueHandler.getRawClass());
 assertEquals(mapType.getKeyType(), mapTypeWithContentValueHandler.getKeyType());
 assertEquals(valueTypeWithHandler, mapTypeWithContentValueHandler.getValueType());
 assertEquals(handler, mapTypeWithContentValueHandler.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 MapType mapTypeWithStaticTyping = mapType.withStaticTyping();

 assertEquals(mapType.getRawClass(), mapTypeWithStaticTyping.getRawClass());
 assertEquals(mapType.getKeyType().isStatic(), mapTypeWithStaticTyping.getKeyType().isStatic());
 assertEquals(mapType.getValueType().isStatic(), mapTypeWithStaticTyping.getValueType().isStatic());
 assertTrue(mapTypeWithStaticTyping.isStatic());
 }

 @Test
 public void testWithContentType() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 JavaType contentType = TypeFactory.defaultInstance().constructType(Long.class);
 MapType mapTypeWithContentType = mapType.withContentType(contentType);

 assertEquals(mapType.getRawClass(), mapTypeWithContentType.getRawClass());
 assertEquals(mapType.getKeyType(), mapTypeWithContentType.getKeyType());
 assertEquals(contentType, mapTypeWithContentType.getValueType());
 }

 @Test
 public void testWithKeyType() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 JavaType keyType = TypeFactory.defaultInstance().constructType(Long.class);
 MapType mapTypeWithKeyType = mapType.withKeyType(keyType);

 assertEquals(mapType.getRawClass(), mapTypeWithKeyType.getRawClass());
 assertEquals(keyType, mapTypeWithKeyType.getKeyType());
 assertEquals(mapType.getValueType(), mapTypeWithKeyType.getValueType());
 }

 @Test
 public void testRefine() {
 JavaType javaClass = TypeFactory.defaultInstance().constructType(HashMap.class);
 MapType mapType = MapType.construct(Map.class, null, javaClass, null,
 String.class, Integer.class);
 MapType refinedMapType = mapType.refine(LinkedHashMap.class, null, null, null);

 assertEquals(LinkedHashMap.class, refinedMapType.getRawClass());
 assertEquals(mapType.getKeyType(), refinedMapType.getKeyType());
 assertEquals(mapType.getValueType(), refinedMapType.getValueType());
 }

 @Test
 public void testWithKeyTypeHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = Mockito.mock(Object.class);
 MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(handler);

 assertEquals(mapType.getRawClass(), mapTypeWithKeyTypeHandler.getRawClass());
 assertEquals(mapType.getKeyType(), mapTypeWithKeyTypeHandler.getKeyType());
 assertEquals(mapType.getValueType(), mapTypeWithKeyTypeHandler.getValueType());
 assertEquals(handler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
 }

 @Test
 public void testWithKeyValueHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = Mockito.mock(Object.class);
 MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(handler);

 assertEquals(mapType.getRawClass(), mapTypeWithKeyValueHandler.getRawClass());
 assertEquals(mapType.getKeyType(), mapTypeWithKeyValueHandler.getKeyType());
 assertEquals(mapType.getValueType(), mapTypeWithKeyValueHandler.getValueType());
 assertEquals(handler, mapTypeWithKeyValueHandler.getKeyValueHandler());
 }
}