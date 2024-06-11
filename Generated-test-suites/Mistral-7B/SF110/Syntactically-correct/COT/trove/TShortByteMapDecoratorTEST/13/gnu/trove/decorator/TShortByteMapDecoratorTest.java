import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
 TShortByteMap actualMap = decorator.getMap();
 Assert.assertSame(_map, actualMap);
 }

 @Test
 public void testPut() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);

 Byte result = decorator.put(key, value);
 Assert.assertSame(value, result);
 Assert.assertTrue(decorator.containsKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, Byte.valueOf((byte)1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(Short.valueOf((short)1), null);
 }

 @Test
 public void testGet() {
 Byte expectedValue = Byte.valueOf((byte)1);
 Short key = Short.valueOf((short)1);
 decorator.put(key, expectedValue);

 Byte actualValue = decorator.get(key);
 Assert.assertSame(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetWithNonExistentKey() {
 decorator.get((short)-1);
 }

 @Test
 public void testClear() {
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 decorator.remove(key);
 Assert.assertFalse(decorator.containsKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveWithNonExistentKey() {
 decorator.remove((short)-1);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 decorator.put(Short.valueOf((short)2), Byte.valueOf((byte)2));

 Entry<Short, Byte> entry1 = iterator.next();
 Assert.assertSame(Short.valueOf((short)1), entry1.getKey());
 Assert.assertSame(Byte.valueOf((byte)1), entry1.getValue());

 Entry<Short, Byte> entry2 = iterator.next();
 Assert.assertSame(Short.valueOf((short)2), entry2.getKey());
 Assert.assertSame(Byte.valueOf((byte)2), entry2.getValue());

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testContainsValue() {
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 decorator.put(Short.valueOf((short)2), Byte.valueOf((byte)2));

 Assert.assertTrue(decorator.containsValue(Byte.valueOf((byte)1)));
 Assert.assertTrue(decorator.containsValue(Byte.valueOf((byte)2)));
 Assert.assertFalse(decorator.containsValue((byte)0));
 }

 @Test
 public void testContainsKey() {
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 decorator.put(Short.valueOf((short)2), Byte.valueOf((byte)2));

 Assert.assertTrue(decorator.containsKey(Short.valueOf((short)1)));
 Assert.assertTrue(decorator.containsKey(Short.valueOf((short)2)));
 Assert.assertFalse(decorator.containsKey((short)-1));
 }

 @Test
 public void testSize() {
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 decorator.put(Short.valueOf((short)2), Byte.valueOf((byte)2));

 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 TShortByteMapDecorator empty = new TShortByteMapDecorator();

 Assert.assertTrue(empty.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 map.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
 decorator.putAll(map);

 Short[] keys = {Short.valueOf((short)1), Short.valueOf((short)2)};
 Byte[] values = {Byte.valueOf((byte)1), Byte.valueOf((byte)2)};

 Assert.assertArrayEquals(keys, decorator.keySet().toArray(new Short[0]));
 Assert.assertArrayEquals(values, decorator.values().toArray(new Byte[0]));
 }

 @Test
 public void testWrapKey() {
 Short testKey = Short.valueOf((short)1);
 Short expectedKey = Short.valueOf((short)1);

 Assert.assertSame(expectedKey, decorator.wrapKey(testKey.shortValue()));
 }

 @Test
 public void testUnwrapKey() {
 Short testKey = Short.valueOf((short)1);
 short expectedKey = testKey.shortValue();

 Assert.assertSame(expectedKey, decorator.unwrapKey(testKey));
 }

 @Test
 public void testWrapValue() {
 Byte testValue = Byte.valueOf((byte)1);
 Byte expectedValue = testValue;

 Assert.assertSame(expectedValue, decorator.wrapValue(testValue.byteValue()));
 }

 @Test
 public void testUnwrapValue() {
 Byte testValue = Byte.valueOf((byte)1);
 byte expectedValue = testValue.byteValue();

 Assert.assertSame(expectedValue, decorator.unwrapValue(testValue));
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(bos);
 decorator.writeExternal(out);

 ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
 ObjectInputStream in = new ObjectInputStream(bis);
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)in.readObject();

 Assert.assertNotNull(deserializedDecorator);
 }
}