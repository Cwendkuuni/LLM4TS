import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteView;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(new TShortByteMap());
 Assert.assertNotNull(decorator);
 Assert.assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
 short key = 1;
 byte value = 1;
 byte expectedValue = value;
 byte actualValue = decorator.put(key, value);
 Assert.assertEquals(expectedValue, actualValue);
 short actualKey = decorator.unwrapKey(key);
 Short wrappedKey = decorator.wrapKey(actualKey);
 Entry<Short, Byte> entry = new AbstractMap.SimpleEntry<>(wrappedKey, value);
 TShortByteMap map = decorator.getMap();
 Assert.assertTrue(map.containsKey(actualKey));
 Assert.assertEquals(entry, decorator.entrySet().iterator().next());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (byte)0);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put((short)0, null);
 }

 @Test
 public void testGet() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 byte actualValue = decorator.get(key);
 Assert.assertEquals(value, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test
 public void testClear() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 decorator.clear();
 Assert.assertFalse(decorator.containsKey(key));
 }

 @Test
 public void testRemove() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 byte actualValue = decorator.remove(key);
 Assert.assertEquals(value, actualValue);
 Assert.assertFalse(decorator.containsKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> expectedSet = new HashSet<Entry<Short, Byte>>(Arrays.asList(new AbstractMap.SimpleEntry<Short, Byte>(Short.valueOf((short)1), Byte.valueOf((byte)1))));
 Set<Entry<Short, Byte>> actualSet = decorator.entrySet();
 Assert.assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testContainsValue() {
 byte value = 1;
 decorator.put((short)0, value);
 boolean containsValue = decorator.containsValue(Byte.valueOf(value));
 Assert.assertTrue(containsValue);
 }

 @Test
 public void testContainsKey() {
 short key = 1;
 decorator.put(key, (byte)0);
 boolean containsKey = decorator.containsKey(key);
 Assert.assertTrue(containsKey);
 }

 @Test
 public void testSize() {
 short key1 = 1;
 short key2 = 2;
 decorator.put(key1, (byte)1);
 decorator.put(key2, (byte)2);
 int size = decorator.size();
 Assert.assertEquals(2, size);
 }

 @Test
 public void testIsEmpty() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 boolean isEmpty = decorator.isEmpty();
 Assert.assertTrue(isEmpty);
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);
 decorator.putAll(map);
 Set<Entry<Short, Byte>> expectedSet = new HashSet<Entry<Short, Byte>>(Arrays.asList(new AbstractMap.SimpleEntry<Short, Byte>(Short.valueOf((short)1), Byte.valueOf((byte)1)), new AbstractMap.SimpleEntry<Short, Byte>(Short.valueOf((short)2), Byte.valueOf((byte)2))));
 Set<Entry<Short, Byte>> actualSet = decorator.entrySet();
 Assert.assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testWrapKey() {
 short key = 1;
 Short wrappedKey = decorator.wrapKey(key);
 Assert.assertEquals(key, wrappedKey.shortValue());
 }

 @Test
 public void testUnwrapKey() {
 Short key = Short.valueOf((short)1);
 short unwrappedKey = decorator.unwrapKey(key);
 Assert.assertEquals(key.shortValue(), unwrappedKey);
 }

 @Test
 public void testWrapValue() {
 byte value = 1;
 Byte wrappedValue = decorator.wrapValue(value);
 Assert.assertEquals(value, wrappedValue.byteValue());
 }

 @Test
 public void testUnwrapValue() {
 Byte value = Byte.valueOf((byte)1);
 byte unwrappedValue = decorator.unwrapValue(value);
 Assert.assertEquals(value.byteValue(), unwrappedValue);
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 DataOutput out = new DataOutputStream(byteStream);
 decorator.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 DataInput in = new DataInputStream(new ByteArrayInputStream(bytes));
 decorator2.readExternal(in);
 Set<Entry<Short, Byte>> expectedSet = decorator.entrySet();
 Set<Entry<Short, Byte>> actualSet = decorator2.entrySet();
 Assert.assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator original = decorator;
 TShortByteMapDecorator clone = (TShortByteMapDecorator)original.clone();
 Assert.assertNotSame(original, clone);
 Set<Entry<Short, Byte>> expectedSet = decorator.entrySet();
 Set<Entry<Short, Byte>> actualSet = clone.entrySet();
 Assert.assertEquals(expectedSet, actualSet);
 }

}