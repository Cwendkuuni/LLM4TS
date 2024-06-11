import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.TDoubleShortMapImpl;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap<Double, Short> createMap() {
 return new TDoubleShortMapImpl<>();
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator<>();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 Assert.assertNotNull(decorator);
 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

 Double key = 1.0;
 Short value = (short) 1;
 Short existingValue = decorator.put(key, value);
 Assert.assertEquals(null, existingValue);
 Assert.assertEquals(value, decorator.get(key));

 existingValue = (Short) 2;
 decorator.put(key, existingValue);
 Assert.assertEquals(existingValue, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(null, (Short) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short) 1);

 Double key = 1.0;
 Short value = decorator.get(key);
 Assert.assertEquals(Short.valueOf((short) 1), value);
 }

 @Test
 public void testGetWithNullKey() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

 Double key = null;
 Short value = decorator.get(key);
 Assert.assertNull(value);
 }

 @Test
 public void testClear() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short) 1);
 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short) 1);

 Double key = 1.0;
 Short value = decorator.remove(key);
 Assert.assertEquals(Short.valueOf((short) 1), value);
 Assert.assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 TDoubleShortIterator iterator = map.iterator();
 int i = 0;
 for (Entry<Double, Short> entry : entrySet) {
 Double key = entry.getKey();
 Short value = entry.getValue();
 Double mapKey = iterator.key();
 Short mapValue = iterator.value();
 Assert.assertEquals(key, mapKey);
 Assert.assertEquals(value, mapValue);
 i++;
 iterator.advance();
 }
 Assert.assertEquals(2, i);
 }

 @Test
 public void testContainsValue() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short) 1);

 Short value = (short) 1;
 boolean containsValue = decorator.containsValue(value);
 Assert.assertTrue(containsValue);
 }

 @Test
 public void testContainsKey() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short) 1);

 Double key = 1.0;
 boolean containsKey = decorator.containsKey(key);
 Assert.assertTrue(containsKey);
 }

 @Test
 public void testSize() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short) 1);

 int size = decorator.size();
 Assert.assertEquals(1, size);
 }

 @Test
 public void testIsEmpty() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

 boolean isEmpty = decorator.isEmpty();
 Assert.assertTrue(isEmpty);
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMap<Double, Short> anotherMap = new TDoubleShortMapImpl<>();
 anotherMap.put(1.0, (short) 1);
 anotherMap.put(2.0, (short) 2);
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

 decorator.putAll(anotherMap);

 Double key1 = 1.0;
 Double key2 = 2.0;
 Short value1 = (short) 1;
 Short value2 = (short) 2;

 Assert.assertEquals(value1, decorator.get(key1));
 Assert.assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 TDoubleShortMap<Double, Short> map = createMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 objectOutputStream.writeObject(decorator);
 byte[] bytes = byteArrayOutputStream.toByteArray();

 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)objectInputStream.readObject();

 Assert.assertNotNull(deserializedDecorator);
 Assert.assertEquals(decorator.size(), deserializedDecorator.size());

 for (Object key : decorator.keySet()) {
 Double keyDouble = (Double)key;
 Short value = decorator.get(keyDouble);
 Short deserializedValue = deserializedDecorator.get(keyDouble);
 Assert.assertEquals(value, deserializedValue);
 }
 }
}