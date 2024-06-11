import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap.MapEntry;
import gnu.trove.map.view.TShortByteView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 map = TShortByteMap.defaultMap();
 decorator = new TShortByteMapDecorator(map);
 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 MapEntry entry = decorator.entrySet().iterator().next();
 Assert.assertEquals((short) 1, entry.getKey());
 Assert.assertEquals((byte) 1, entry.getValue());
 }

 @Test
 public void testGet() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 Assert.assertEquals((byte) 1, decorator.get((short) 1));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.clear();
 Assert.assertFalse(decorator.isEmpty());
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.remove((short) 1);
 Assert.assertEquals((byte) 2, decorator.get((short) 2));
 Assert.assertNull(decorator.get((short) 1));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 TShortByteView view = decorator.entrySet().iterator();
 Assert.assertEquals(decorator.size(), view.size());
 while (view.hasNext()) {
 MapEntry entry = (MapEntry)view.next();
 Assert.assertEquals(entry.getKey(), decorator.get(entry.getKey()));
 Assert.assertEquals(entry.getValue(), decorator.get(entry.getKey()));
 }
 }

 @Test
 public void testContainsValue() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 Assert.assertTrue(decorator.containsValue((byte) 1));
 Assert.assertTrue(decorator.containsValue((byte) 2));
 Assert.assertFalse(decorator.containsValue((byte) 3));
 }

 @Test
 public void testContainsKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 Assert.assertTrue(decorator.containsKey((short) 1));
 Assert.assertTrue(decorator.containsKey((short) 2));
 Assert.assertFalse(decorator.containsKey((short) 3));
 }

 @Test
 public void testSize() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 Assert.assertFalse(decorator.isEmpty());
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map2 = TShortByteMap.defaultMap();
 map2.put((short) 3, (byte) 3);
 map2.put((short) 4, (byte) 4);
 decorator = new TShortByteMapDecorator();
 decorator.putAll(map2);
 Assert.assertEquals(decorator.size(), map2.size());
 }

 @Test
 public void testExternalizable() {
 try {
 decorator = new TShortByteMapDecorator();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)ois.readObject();
 ois.close();
 Assert.assertNotNull(decorator2);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testCloneable() {
 try {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)decorator.clone();
 Assert.assertEquals(decorator.size(), decorator2.size());
 } catch (CloneNotSupportedException e) {
 e.printStackTrace();
 }
 }
}