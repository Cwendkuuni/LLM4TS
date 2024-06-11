import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testGetMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortMap());
 TDoubleShortMap map = decorator.getMap();
 Assert.assertSame(map, decorator._map);
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 map = decorator.getMap();
 TDoubleShortMapDecorator expectedDecorator = new TDoubleShortMapDecorator(map);
 TDoubleShortMap expectedMap = map;

 Assert.assertEquals(expectedDecorator.size(), decorator.size());
 Assert.assertEquals(expectedMap.containsKey(1.0), decorator.containsKey(1.0));
 Assert.assertEquals(expectedMap.containsKey(2.0), decorator.containsKey(2.0));
 Assert.assertEquals(expectedMap.get(1.0), decorator.get(1.0));
 Assert.assertEquals(expectedMap.get(2.0), decorator.get(2.0));

 Assert.assertNull(decorator.get(3.0));
 Assert.assertNull(map.get(3.0));
 }

 @Test
 public void testGetNullKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Assert.assertEquals(decorator.get(null), null);
 Assert.assertEquals(map.getNoEntryKey(), decorator.get(null));
 }

 @Test
 public void testGetNullValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Assert.assertEquals(decorator.get(1.0), (short)1);
 Assert.assertEquals(map.get(1.0), (short)1);

 Assert.assertNull(decorator.get(1.0, (short)0));
 Assert.assertEquals(map.getNoEntryValue(), decorator.get(1.0, (short)0));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();

 Assert.assertEquals(decorator.size(), 0);
 Assert.assertEquals(map.size(), 0);
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.remove(1.0);

 Assert.assertEquals(decorator.size(), 1);
 Assert.assertEquals(map.size(), 1);
 Assert.assertEquals(decorator.get(2.0), (short)2);
 Assert.assertEquals(map.get(2.0), (short)2);

 Assert.assertNull(decorator.get(1.0));
 Assert.assertEquals(map.getNoEntryKey(), decorator.get(1.0));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Assert.assertTrue(decorator.containsKey(1.0));
 Assert.assertTrue(decorator.containsKey(2.0));

 Assert.assertFalse(decorator.containsKey(3.0));
 Assert.assertFalse(map.containsKey(3.0));
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(1.0, (short)2);
 decorator.put(2.0, (short)2);

 Assert.assertTrue(decorator.containsValue((short)1));
 Assert.assertTrue(decorator.containsValue((short)2));

 Assert.assertFalse(decorator.containsValue((short)3));
 Assert.assertFalse(map.containsValue((short)3));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertEquals(decorator.size(), 3);
 Assert.assertEquals(map.size(), 3);
 }

 @Test
 public void testIsEmpty() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();

 Assert.assertTrue(decorator.isEmpty());
 Assert.assertTrue(map.isEmpty());
 }

 @Test
 public void testPutAll() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 TDoubleShortMap otherMap = new TDoubleShortMap();
 otherMap.put(1.0, (short)1);
 otherMap.put(2.0, (short)2);

 decorator.putAll(otherMap);

 Assert.assertEquals(decorator.size(), 2);
 Assert.assertEquals(map.size(), 2);

 Assert.assertEquals(decorator.get(1.0), otherMap.get(1.0));
 Assert.assertEquals(decorator.get(2.0), otherMap.get(2.0));
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Set<Entry<Double, Short>> set = decorator.entrySet();

 Assert.assertEquals(set.size(), 2);

 Entry<Double, Short> entry = set.iterator().next();
 Assert.assertEquals(entry.getKey(), 1.0);
 Assert.assertEquals(entry.getValue(), (short)1);
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator loadedDecorator = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 Assert.assertEquals(decorator.size(), loadedDecorator.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();

 Assert.assertEquals(decorator.size(), clonedDecorator.size());
 }
}