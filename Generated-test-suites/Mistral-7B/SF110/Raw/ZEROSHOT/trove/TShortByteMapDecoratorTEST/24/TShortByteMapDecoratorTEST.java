import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.iterator.TShortByteMapIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class TShortByteMapDecoratorTest {

 @Test
 public void testEmptyMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testWrapAndUnwrapKeysAndValues() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);

 Assert.assertEquals(key, decorator.get(key));
 Assert.assertEquals(value, decorator.get(key));
 }

 @Test
 public void testPut() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key2, value2);

 Assert.assertEquals(2, decorator.size());

 Byte value1Ret = decorator.get(key1);
 Assert.assertEquals(value1, value1Ret);
 Byte value2Ret = decorator.get(key2);
 Assert.assertEquals(value2, value2Ret);
 }

 @Test
 public void testClear() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key2, value2);

 Assert.assertEquals(2, decorator.size());
 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key2, value2);

 Assert.assertEquals(2, decorator.size());
 decorator.remove(key1);
 Assert.assertEquals(1, decorator.size());
 Assert.assertNull(decorator.get(key1));
 Assert.assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testEntrySet() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key2, value2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 Assert.assertEquals(2, entrySet.size());

 TShortByteMapDecorator.Entry entry1 = (TShortByteMapDecorator.Entry)entrySet.iterator().next();
 Assert.assertEquals(key1, entry1.getKey());
 Assert.assertEquals(value1, entry1.getValue());

 TShortByteMapDecorator.Entry entry2 = (TShortByteMapDecorator.Entry)entrySet.iterator().next();
 Assert.assertEquals(key2, entry2.getKey());
 Assert.assertEquals(value2, entry2.getValue());
 }

 @Test
 public void testContainsKeyAndValue() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key2, value2);

 Assert.assertTrue(decorator.containsKey(key1));
 Assert.assertTrue(decorator.containsKey(key2));
 Assert.assertTrue(decorator.containsValue(value1));
 Assert.assertTrue(decorator.containsValue(value2));
 }

 @Test
 public void testSize() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key2, value2);

 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Assert.assertTrue(decorator.isEmpty());

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);

 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map1 = new TShortByteMapImpl();
 TShortByteMap map2 = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map1);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);

 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 map2.put(key2, value2);

 decorator.putAll(map2);

 Assert.assertEquals(2, decorator.size());
 Assert.assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 objOut.close();

 ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
 ObjectInput objIn = new ObjectInputStream(in);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)objIn.readObject();
 objIn.close();

 Assert.assertEquals(map, decorator2.getMap());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 TShortByteMapDecorator clone = (TShortByteMapDecorator)decorator.clone();

 Assert.assertNotSame(decorator, clone);
 Assert.assertEquals(map, clone.getMap());
 }

 @Test
 public void testIterator() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key2, value2);

 TShortByteIterator it = decorator.iterator();

 Assert.assertTrue(it.hasNext());
 TShortByteMap.Entry entry1 = it.next();
 Assert.assertEquals(key1, entry1.getKey());
 Assert.assertEquals(value1, entry1.getValue());

 Assert.assertTrue(it.hasNext());
 TShortByteMap.Entry entry2 = it.next();
 Assert.assertEquals(key2, entry2.getKey());
 Assert.assertEquals(value2, entry2.getValue());

 Assert.assertFalse(it.hasNext());
 }
}