import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleShortMapView;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.Collections;
import java.util.Map;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testGetMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertNull(decorator.getMap());

 TDoubleShortMapDecorator decoratorWithMap = new TDoubleShortMapDecorator(map);
 assertEquals(map, decoratorWithMap.getMap());
 }

 @Test
 public void testPut() {
 double key = 1.0;
 short value = 1;

 Short expectedValue = decorator.put(key, value);
 assertEquals(value, expectedValue);

 Short actualValue = decorator.get(key);
 assertEquals(value, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 double key = null;
 short value = 1;

 Short expectedValue = decorator.put(key, value);
 fail("Should not be able to put a null key");
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 double key = 1.0;
 short value = null;

 Short expectedValue = decorator.put(key, value);
 fail("Should not be able to put a null value");
 }

 @Test
 public void testGet() {
 double key = 1.0;
 short value = 1;

 decorator.put(key, value);

 Short actualValue = decorator.get(key);
 assertEquals(value, actualValue);

 actualValue = decorator.get(2.0);
 assertNull(actualValue);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 double key = 1.0;
 short value = 1;

 decorator.put(key, value);
 Short actualValue = decorator.remove(key);
 assertEquals(value, actualValue);

 actualValue = decorator.get(key);
 assertNull(actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 double key = null;

 Short actualValue = decorator.remove(key);
 fail("Should not be able to remove a null key");
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertNotNull(entrySet);
 }

 @Test
 public void testContainsValue() {
 short value = 1;
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 double key = 1.0;
 assertFalse(decorator.containsKey(key));

 decorator.put(key, (short)1);
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());

 decorator.put(1.0, (short)1);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());

 decorator.put(1.0, (short)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, (short)2);
 anotherMap.put(2.0, (short)3);

 decorator.putAll(anotherMap);

 Map<Double, Short> decoratorMap = decorator.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
 Map<Double, Short> anotherMapCopy = new TDoubleShortHashMapCopy(anotherMap);

 assertEquals(decoratorMap, anotherMapCopy);
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);

 decorator.writeExternal(objStream);
 byte[] bytes = byteStream.toByteArray();

 TDoubleShortMapDecorator deserializer = new TDoubleShortMapDecorator();
 deserializer.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

 assertEquals(decorator, deserializer);
 }

 @Test
 public void testCloneable() {
 TDoubleShortMapDecorator clone = decorator.clone();
 assertEquals(decorator, clone);
 }
}