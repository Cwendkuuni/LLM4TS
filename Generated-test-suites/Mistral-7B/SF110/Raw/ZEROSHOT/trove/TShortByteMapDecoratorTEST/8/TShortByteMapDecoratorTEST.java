import org.junit.Assert;
import org.junit.Test;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.map.view.TShortByteMapView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ByteArrayInputStream;
import java.util.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructors() {
 map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(map);
 Assert.assertNotNull(decorator);
 Assert.assertEquals(map, decorator.getMap());
 decorator = new TShortByteMapDecorator<>();
 map = (TShortByteMap)decorator.getMap();
 Assert.assertNotNull(map);
 Assert.assertNull(map.size());
 }

 @Test
 public void testPut() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.put((short)1, (byte)3);
 Map<Short, Byte> expected = new TShortByteMapImpl();
 expected.put((short)1, (byte)3);
 expected.put((short)2, (byte)2);
 Assert.assertEquals(expected.entrySet(), decorator.entrySet());
 }

 @Test
 public void testGet() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertEquals((byte)1, decorator.get((short)1));
 Assert.assertEquals((byte)2, decorator.get((short)2));
 Assert.assertNull(decorator.get(Short.valueOf((short)-1)));
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.clear();
 Assert.assertEquals(Collections.emptyMap(), decorator);
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.put((short)1, (byte)3);
 decorator.remove((short)1);
 Map<Short, Byte> expected = new TShortByteMapImpl();
 expected.put((short)2, (byte)2);
 Assert.assertEquals(expected.entrySet(), decorator.entrySet());
 }

 @Test
 public void testEntrySet() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 Entry<Short, Byte> expected = new Entry<Short, Byte>() {
 @Override
 public Short getKey() {
 return Short.valueOf((short)1);
 }

 @Override
 public Byte getValue() {
 return Byte.valueOf((byte)1);
 }
 };
 Assert.assertEquals(expected, iterator.next());
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertTrue(decorator.containsValue((byte)1));
 Assert.assertFalse(decorator.containsValue((byte)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertTrue(decorator.containsKey((short)1));
 Assert.assertTrue(decorator.containsKey((short)2));
 Assert.assertFalse(decorator.containsKey((short)-1));
 }

 @Test
 public void testSize() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap expected = new TShortByteMapImpl();
 expected.put((short)1, (byte)1);
 expected.put((short)2, (byte)2);
 decorator.clear();
 Map<Short, Byte> mapToPut = new TShortByteMapImpl();
 mapToPut.put((short)1, (byte)1);
 mapToPut.put((short)2, (byte)2);
 decorator.putAll(mapToPut);
 Assert.assertEquals(expected.entrySet(), decorator.entrySet());
 }

 @Test
 public void testWrapKey() {
 decorator = new TShortByteMapDecorator<>();
 Assert.assertEquals((short)0, decorator.wrapKey(0));
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testAddAllEntrySet() {
 decorator.put((short)1, (byte)1);
 Set<Entry<Short, Byte>> entries = decorator.entrySet();
 entries.add(new Entry<Short, Byte>() {
 @Override
 public Short getKey() {
 return Short.valueOf((short)2);
 }

 @Override
 public Byte getValue() {
 return Byte.valueOf((byte)3);
 }
 });
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 out.close();
 byte[] bytes = byteStream.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator newDecorator = new TShortByteMapDecorator();
 newDecorator.readExternal(in);
 in.close();
 Assert.assertEquals(decorator.entrySet(), newDecorator.entrySet());
 }
}