import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.TShortByteMap.Mapping;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNotNull(decorator);
     Assert.assertSame(_map, decorator.getMap());
 }

 @Test
 public void testPutMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     Byte putValue = decorator.put(key, value);
     Assert.assertNull(putValue);
     Assert.assertTrue(_map.containsKey(key.shortValue()));
     Assert.assertEquals(value, _map.get((short) 1));
 }

 @Test
 public void testPutMethodWithNullKey() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Byte putValue = decorator.put((Short) null, (Byte) 10);
     Assert.assertNull(putValue);
     Assert.assertFalse(_map.containsKey(Short.MIN_VALUE));
 }

 @Test
 public void testPutMethodWithNullValue() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte putValue = decorator.put(key, (Byte) null);
     Assert.assertNull(putValue);
     Assert.assertEquals(Byte.MIN_VALUE, _map.get(key.shortValue()));
 }

 @Test
 public void testGetMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);
     Byte getValue = decorator.get(key);
     Assert.assertEquals(value, getValue);
 }

 @Test
 public void testGetMethodWithNullKey() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);
     Byte getValue = decorator.get((Short) null);
     Assert.assertNull(getValue);
 }

 @Test
 public void testGetMethodWithNonExistentKey() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Byte getValue = decorator.get((Short) null);
     Assert.assertNull(getValue);
     Assert.assertFalse(_map.containsKey(Short.MIN_VALUE));
 }

 @Test
 public void testClearMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);
     decorator.clear();

     Assert.assertTrue(_map.isEmpty());
     Assert.assertFalse(decorator.containsKey(key));
 }

 @Test
 public void testRemoveMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);
     Byte removedValue = decorator.remove(key);
     Assert.assertEquals(value, removedValue);
     Assert.assertFalse(_map.containsKey(key.shortValue()));
     Assert.assertFalse(decorator.containsKey(key));
 }

 @Test
 public void testRemoveMethodWithNullKey() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Byte removedValue = decorator.remove((Short) null);
     Assert.assertNull(removedValue);
     Assert.assertFalse(_map.isEmpty());
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testRemoveMethodWithNonExistentKey() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Byte removedValue = decorator.remove((Short) null);
     Assert.assertNull(removedValue);
     Assert.assertFalse(_map.isEmpty());
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testEntrySetMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key1 = Short.valueOf((short) 1);
     Byte value1 = Byte.valueOf((byte) 10);
     Short key2 = Short.valueOf((short) 2);
     Byte value2 = Byte.valueOf((byte) 20);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
     int size = entrySet.size();
     Entry<Short, Byte> entry = null;

     for (int i = 0; i < size; i++) {
         entry = entrySet.iterator().next();
         if (entry.getKey().equals(key1)) {
             Assert.assertEquals(value1, entry.getValue());
             break;
         }
         if (entry.getKey().equals(key2)) {
             Assert.assertEquals(value2, entry.getValue());
             break;
         }
     }

     Assert.assertNotNull(entry);
 }

 @Test
 public void testContainsValueMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);

     Assert.assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKeyMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);

     Assert.assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testContainsKeyMethodWithNullKey() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Assert.assertFalse(decorator.containsKey((Short) null));
 }

 @Test
 public void testSizeMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key1 = Short.valueOf((short) 1);
     Byte value1 = Byte.valueOf((byte) 10);
     Short key2 = Short.valueOf((short) 2);
     Byte value2 = Byte.valueOf((byte) 20);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmptyMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Assert.assertTrue(decorator.isEmpty());

     Short key = Short.valueOf((short) 1);
     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);

     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAllMethod() {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key1 = Short.valueOf((short) 1);
     Byte value1 = Byte.valueOf((byte) 10);
     Short key2 = Short.valueOf((short) 2);
     Byte value2 = Byte.valueOf((byte) 20);

     TShortByteMap tempMap = new TShortByteMap(Mapping.DEFAULT, 10);
     tempMap.put(key1, value1);
     tempMap.put(key2, value2);

     decorator.putAll(tempMap);

     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals(value1, decorator.get(key1));
     Assert.assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testWrapKeyUnwrapKeyMethods() throws Exception {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     Short key = Short.valueOf((short) 1);
     byte unwrappedKey = key.shortValue();

     Byte value = Byte.valueOf((byte) 10);

     decorator.put(key, value);

     Short decKey = decorator.wrapKey(unwrappedKey);
     Assert.assertEquals(key, decKey);

     Assert.assertEquals(value, decorator.get(decKey));
 }

 @Test
 public void testExternalizableMethods() throws IOException, ClassNotFoundException {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     ByteArrayOutputStream out = new ByteArrayOutputStream();
     ObjectOutput objOut = new ObjectOutputStream(out);

     decorator.writeExternal(objOut);
     objOut.close();
     byte[] bytes = out.toByteArray();

     ByteArrayInputStream in = new ByteArrayInputStream(bytes);
     ObjectInput objIn = new ObjectInputStream(in);
     TShortByteMapDecorator newDecorator = (TShortByteMapDecorator) objIn.readObject();
     objIn.close();

     Assert.assertNotNull(newDecorator);
     Assert.assertSame(_map, newDecorator.getMap());
 }

 @Test
 public void testClonableMethod() throws CloneNotSupportedException {
     _map = new TShortByteMap(Mapping.DEFAULT, 10);
     decorator = new TShortByteMapDecorator(_map);

     TShortByteMapDecorator clone = (TShortByteMapDecorator) decorator.clone();

     Assert.assertNotNull(clone);
     Assert.assertSame(_map, clone.getMap());
 }
}