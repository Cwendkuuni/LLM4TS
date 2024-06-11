import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.view.TObjectIntMapView;

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
 assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMap map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);

 Byte returnedValue = decorator.put(key, value);
 assertEquals(value, returnedValue);
 assertEquals(key, decorator.keySet().iterator().next());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, Byte.valueOf((byte)1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(Short.valueOf((short)1), null);
 }

 @Test(expected = ClassCastException.class)
 public void testPutWithInvalidKeyType() {
 decorator.put(1, Byte.valueOf((byte)1));
 }

 @Test(expected = ClassCastException.class)
 public void testPutWithInvalidValueType() {
 decorator.put(Short.valueOf((short)1), "invalid value");
 }

 @Test
 public void testGet() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);

 Byte returnedValue = decorator.get(key);
 assertEquals(value, returnedValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithInvalidKeyType() {
 decorator.get(1);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 Byte returnedValue = decorator.remove(key);
 assertEquals(value, returnedValue);
 assertFalse(decorator.containsKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveWithInvalidKeyType() {
 decorator.remove(1);
 }

 @Test
 public void testEntrySet() {
 TShortByteMapView view = decorator.entrySet().asMap();
 assertEquals(decorator.size(), view.size());
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put(Short.valueOf((short)1), value);
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short)1);
 decorator.put(key, (byte)1);
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 map.put(key1, value1);
 map.put(key2, value2);

 TObjectIntMapView<Short, Integer> mapView = new TObjectIntHashMap<Short, Integer>(map.size());
 decorator.putAll(mapView);

 assertEquals(map.size(), decorator.size());
 assertEquals(value1, decorator.get(key1));
 assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testWrapKey() {
 assertEquals((short)1, decorator.wrapKey((short)1));
 }

 @Test(expected = ClassCastException.class)
 public void testWrapKeyWithInvalidType() {
 decorator.wrapKey(1);
 }

 @Test
 public void testUnwrapKey() {
 Short key = Short.valueOf((short)1);
 assertEquals((short)1, decorator.unwrapKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testUnwrapKeyWithNullObject() {
 decorator.unwrapKey(null);
 }

 @Test(expected = ClassCastException.class)
 public void testUnwrapKeyWithInvalidType() {
 decorator.unwrapKey(1);
 }

 @Test
 public void testWrapValue() {
 assertEquals((byte)1, decorator.wrapValue((byte)1));
 }

 @Test(expected = NullPointerException.class)
 public void testWrapValueWithNullObject() {
 decorator.wrapValue(null);
 }

 @Test(expected = ClassCastException.class)
 public void testWrapValueWithInvalidType() {
 decorator.wrapValue(1);
 }

 @Test
 public void testUnwrapValue() {
 Byte value = Byte.valueOf((byte)1);
 assertEquals((byte)1, decorator.unwrapValue(value));
 }

 @Test(expected = NullPointerException.class)
 public void testUnwrapValueWithNullObject() {
 decorator.unwrapValue(null);
 }

 @Test(expected = ClassCastException.class)
 public void testUnwrapValueWithInvalidType() {
 decorator.unwrapValue(1);
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 decorator.writeExternal(inputStream);
 inputStream.reset();
 TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
 deserializedDecorator.readExternal(inputStream);
 assertEquals(decorator.size(), deserializedDecorator.size());
 }

 @Test
 public void testCloneable() {
 try {
 TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();
 assertEquals(decorator.size(), clonedDecorator.size());
 } catch (CloneNotSupportedException e) {
 Assert.fail("Cloneable class should not throw CloneNotSupportedException.");
 }
 }

 @Test(expected = ClassCastException.class)
 public void testIterator() {
 Iterator<Map.Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
 assertEquals(decorator.size(), iterator.next().getValueCount());
 }

 @Test
 public void testIterable() {
 Iterator<Map.Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
 Set<Map.Entry<Short, Byte>> entries = new HashSet<>();
 while (iterator.hasNext()) {
 entries.add(iterator.next());
 }
 assertEquals(decorator.size(), entries.size());
 }

}