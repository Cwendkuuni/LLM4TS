import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.decorator.TShortByteMapDecorator;
import java.util.Collections;
import java.util.Iterator;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setup() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 }

 @Test
 public void testConstructor() {
 TShortByteMapDecorator emptyDecorator = new TShortByteMapDecorator();
 Assert.assertNotNull(emptyDecorator);
 }

 @Test
 public void testConstructorWithMap() {
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
 TShortByteMap actualMap = decorator.getMap();
 Assert.assertSame(map, actualMap);
 }

 @Test
 public void testPut() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 Byte result = decorator.put(key, value);
 Assert.assertEquals(value, result);
 byte actualValue = map.get(1);
 Assert.assertEquals(value.byteValue(), actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (Byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testGet() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 Byte actualValue = decorator.get(key);
 Assert.assertEquals(value, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test
 public void testClear() {
 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 Byte removedValue = decorator.remove(key);
 Assert.assertEquals(value, removedValue);
 Assert.assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 Assert.assertNotNull(entrySet);
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put((Short)1, value);
 Assert.assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short)1);
 decorator.put(key, (Byte)1);
 Assert.assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 decorator.put((Short)1, (Byte)1);
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(decorator.isEmpty());
 decorator.put((Short)1, (Byte)1);
 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map2 = new TShortByteMap();
 map2.put((short)1, (byte)1);
 map2.put((short)2, (byte)2);
 decorator.putAll(Collections.singletonMap((Short)1, (Byte)1));
 decorator.putAll(map2);
 TShortByteIterator it = decorator.entrySet().iterator();
 // check if all entries are present in the decorator
 short expectedKey;int expectedValue;
 while (it.hasNext()) {
 expectedKey = it.key();
 expectedValue = it.value();
 Assert.assertEquals(expectedKey, it.getKey());
 Assert.assertEquals(expectedValue, it.getValue());
 }
 }

 @Test
 public void testWrapKey() {
 Assert.assertEquals(Short.valueOf((short)1), decorator.wrapKey((short)1));
 }

 @Test
 public void testUnwrapKey() {
Short key = Short.valueOf((short)1);
Assert.assertEquals((short)1, decorator.unwrapKey(key));
}

 @Test
 public void testWrapValue() {
 Assert.assertEquals(Byte.valueOf((byte)1), decorator.wrapValue((byte)1));
 }

 @Test
 public void testUnwrapValue() {
 Byte value = Byte.valueOf((byte)1);
 Assert.assertEquals((byte)1, decorator.unwrapValue(value));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 decorator.writeExternal(outputStream);
 TShortByteMapDecorator actualDecorator = new TShortByteMapDecorator();
 actualDecorator.readExternal(inputStream);
 TShortByteMap actualMap = actualDecorator.getMap();
 Assert.assertSame(map, actualMap);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 decorator.writeExternal(outputStream);
 byte[] serializedData = outputStream.toByteArray();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
 TShortByteMapDecorator actualDecorator = new TShortByteMapDecorator();
 actualDecorator.readExternal(inputStream);
 TShortByteMap actualMap = actualDecorator.getMap();
 Assert.assertSame(map, actualMap);
 }
}