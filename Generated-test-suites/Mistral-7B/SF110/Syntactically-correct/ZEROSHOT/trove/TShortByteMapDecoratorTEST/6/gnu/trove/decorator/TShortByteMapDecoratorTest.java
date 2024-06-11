import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyDecorator() {
 decorator = new TShortByteMapDecorator();
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testDecoratorWithMap() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Assert.assertEquals(_map.size(), decorator.size());
 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPut() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Short key = Short.valueOf("1");
 Byte value = Byte.valueOf("2");

 Byte result = decorator.put(key, value);

 Assert.assertEquals(1, decorator.size());
 Assert.assertNotNull(result);
 Assert.assertEquals(value, result);
 Assert.assertEquals(value, decorator.get(key));

 key = Short.valueOf("3");
 value = Byte.valueOf("4");
 result = decorator.put(key, value);

 Assert.assertEquals(2, decorator.size());
 Assert.assertNotNull(result);
 Assert.assertEquals(value, result);
 Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyPut() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Byte value = Byte.valueOf("2");

 decorator.put(null, value);
 }

 @Test
 public void testGet() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Short key1 = Short.valueOf("1");
 Byte value1 = Byte.valueOf("2");
 Short key2 = Short.valueOf("3");
 Byte value2 = Byte.valueOf("4");

 decorator.put(key1, value1);
 decorator.put(key2, value2);

 Byte result1 = decorator.get(key1);
 Byte result2 = decorator.get(key2);
 Byte result3 = decorator.get(Short.valueOf("5"));

 Assert.assertEquals(value1, result1);
 Assert.assertEquals(value2, result2);
 Assert.assertEquals(null, result3);
 }

 @Test
 public void testClear() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Short key1 = Short.valueOf("1");
 Byte value1 = Byte.valueOf("2");
 Short key2 = Short.valueOf("3");
 Byte value2 = Byte.valueOf("4");

 decorator.put(key1, value1);
 decorator.put(key2, value2);

 decorator.clear();

 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Short key1 = Short.valueOf("1");
 Byte value1 = Byte.valueOf("2");
 Short key2 = Short.valueOf("3");
 Byte value2 = Byte.valueOf("4");

 decorator.put(key1, value1);
 decorator.put(key2, value2);

 Byte result1 = decorator.remove(key1);
 Byte result2 = decorator.remove(key2);
 Byte result3 = decorator.remove(Short.valueOf("5"));

 Assert.assertEquals(1, decorator.size());
 Assert.assertEquals(value1, result1);
 Assert.assertEquals(null, result2);
 Assert.assertEquals(null, result3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyRemove() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);Short key = Short.valueOf("1");
Byte value = Byte.valueOf("2");
 decorator.put(key, value);
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Short key1 = Short.valueOf("1");
 Byte value1 = Byte.valueOf("2");
 Short key2 = Short.valueOf("3");
 Byte value2 = Byte.valueOf("4");

 decorator.put(key1, value1);
 decorator.put(key2, value2);

 TShortByteMapView view = decorator.entrySet().asMap();

 Assert.assertEquals(decorator.size(), view.size());
 Assert.assertEquals(value1, view.get(key1));
 Assert.assertEquals(value2, view.get(key2));
 }

 @Test
 public void testContainsValue() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Byte value1 = Byte.valueOf("1");
 Byte value2 = Byte.valueOf("2");
 Byte value3 = Byte.valueOf("3");

 decorator.put(Short.valueOf("1"), value1);
 decorator.put(Short.valueOf("2"), value2);
 decorator.put(Short.valueOf("3"), value3);

 Assert.assertTrue(decorator.containsValue(value1));
 Assert.assertTrue(decorator.containsValue(value2));
 Assert.assertTrue(decorator.containsValue(value3));
 Assert.assertFalse(decorator.containsValue(Byte.valueOf("5")));
 }

 @Test
 public void testContainsKey() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Short key1 = Short.valueOf("1");
 Short key2 = Short.valueOf("2");
 Short key3 = Short.valueOf("3");

 decorator.put(key1, (byte) 1);
 decorator.put(key2, (byte) 2);
 decorator.put(key3, (byte) 3);

 Assert.assertTrue(decorator.containsKey(key1));
 Assert.assertTrue(decorator.containsKey(key2));
 Assert.assertTrue(decorator.containsKey(key3));
 Assert.assertFalse(decorator.containsKey(Short.valueOf("5")));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Byte value = Byte.valueOf("1");
 decorator.put(Short.valueOf("1"), value);
 decorator.put(Short.valueOf("2"), value);
 decorator.put(Short.valueOf("3"), value);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objStream);
 objStream.close();
 byte[] byteData = byteStream.toByteArray();

 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteData);
 ObjectInputStream objInput = new ObjectInputStream(byteInput);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)objInput.readObject();
 objInput.close();
 byteInput.close();

 Assert.assertEquals(decorator.size(), decorator2.size());
 for (Entry<Short, Byte> entry : decorator.entrySet()) {
 Short key = entry.getKey();
 Byte value2 = entry.getValue();
 Short key2 = decorator2.get(key);
 Byte value3 = decorator2.get(key2);
 Assert.assertEquals(key, key2);
 Assert.assertEquals(value2, value3);
 }
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Byte value = Byte.valueOf("1");
 decorator.put(Short.valueOf("1"), value);
 decorator.put(Short.valueOf("2"), value);
 decorator.put(Short.valueOf("3"), value);

 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)decorator.clone();

 Assert.assertEquals(decorator.size(), decorator2.size());
 for (Entry<Short, Byte> entry : decorator.entrySet()) {
 Short key = entry.getKey();
 Byte value2 = entry.getValue();
 Short key2 = decorator2.get(key);
 Byte value3 = decorator2.get(key2);
 Assert.assertEquals(key, key2);
 Assert.assertEquals(value2, value3);
 }
 }
}