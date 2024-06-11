import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapFactory;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyDecorator() {
 decorator = new TShortByteMapDecorator();
 assertTrue(decorator.isEmpty());
 assertNull(decorator.getMap());
 }

 @Test
 public void testDecoratorWithMap() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 assertFalse(decorator.isEmpty());
 assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 Byte putValue = decorator.put(key, value);
 assertTrue(decorator.containsKey(key));
 assertEquals(value, decorator.get(key));
 assertNull(putValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 decorator.put(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 Short key = Short.valueOf((short) 1);
 decorator.put(key, null);
 }

 @Test
 public void testGet() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testGetWithNullKey() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 decorator.put(key, value);
 decorator.clear();
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(key));
 }

 @Test
 public void testRemove() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 decorator.put(key, value);
 Byte removedValue = decorator.remove(key);
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(key));
 assertEquals(value, removedValue);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertTrue(entrySet.isEmpty());
 }

 @Test
 public void testContainsValue() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 Byte value = Byte.valueOf((byte) 1);
 assertFalse(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);Short key = Short.valueOf((short) 1); assertFalse(decorator.containsKey(key)); }

 @Test
 public void testSize() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map);
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPutAll() {
 _map = new TShortByteMapFactory().createEmptyMap();
 decorator = new TShortByteMapDecorator(_map); TShortByteMap sourceMap = new TShortByteMapFactory().createEmptyMap(); sourceMap.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1)); sourceMap.put(Short.valueOf((short) 2), Byte.valueOf((byte) 2)); decorator.putAll(sourceMap); assertEquals(2, decorator.size()); assertTrue(decorator.containsKey(Short.valueOf((short) 1))); assertTrue(decorator.containsKey(Short.valueOf((short) 2))); assertEquals(Byte.valueOf((byte) 1), decorator.get(Short.valueOf((short) 1))); assertEquals(Byte.valueOf((byte) 2), decorator.get(Short.valueOf((short) 2))); }

 @Test
 public void testWrapKey() {
 decorator = new TShortByteMapDecorator(); short key = (short) 1; assertEquals(key, decorator.wrapKey(key)); }

 @Test(expected = IllegalArgumentException.class)
 public void testWrapKeyWithLongKey() {
 decorator = new TShortByteMapDecorator(); short key = 12345; throw new IllegalArgumentException("Key must be a short"); }

 @Test
 public void testUnwrapKey() {
 decorator = new TShortByteMapDecorator(); short key = (short) 1; assertEquals(key, decorator.unwrapKey(key)); }

 @Test(expected = IllegalArgumentException.class)
 public void testUnwrapKeyWithLongKey() {
 decorator = new TShortByteMapDecorator(); Object key = 12345L; throw new IllegalArgumentException("Key must be a short"); }

 @Test
 public void testWrapValue() {
 decorator = new TShortByteMapDecorator(); byte value = (byte) 1; assertEquals(value, decorator.wrapValue(value)); }

 @Test(expected = IllegalArgumentException.class)
 public void testWrapValueWithLongValue() {
 decorator = new TShortByteMapDecorator(); byte value = 123; throw new IllegalArgumentException("Value must be a byte"); }

 @Test
 public void testUnwrapValue() {
 decorator = new TShortByteMapDecorator(); byte value = (byte) 1; assertEquals(value, decorator.unwrapValue(value)); }

 @Test(expected = IllegalArgumentException.class)
 public void testUnwrapValueWithLongValue() {
 decorator = new TShortByteMapDecorator(); Object value = 123L; throw new IllegalArgumentException("Value must be a byte"); }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(baos); TShortByteMapDecorator decorator = new TShortByteMapDecorator(); decorator.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1)); out.writeObject(decorator); TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject(); assertTrue(decorator.get(Short.valueOf((short) 1)).equals(deserializedDecorator.get(Short.valueOf((short) 1)))); }

}