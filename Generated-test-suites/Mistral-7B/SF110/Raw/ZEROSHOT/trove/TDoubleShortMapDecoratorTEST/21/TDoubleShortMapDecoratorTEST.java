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
 decorator = new TDoubleShortMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 Assert.assertSame(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 TDoubleShortMap expectedMap = new TDoubleShortMap();
 expectedMap.put(1.0, (short)1);
 expectedMap.put(2.0, (short)2);
 Assert.assertEquals(expectedMap.size(), decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 Assert.assertEquals((short)1, decorator.get(1.0));
 }

 @Test
 public void testGetWithNullKey() {
 decorator = new TDoubleShortMapDecorator();
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetWithNonExistentKey() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 Assert.assertNull(decorator.get(2.0));
 }

 @Test
 public void testClear() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 Assert.assertEquals((short)1, decorator.remove(1.0));
 Assert.assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator = new TDoubleShortMapDecorator();
 decorator.remove(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNonExistentKey() {
 decorator = new TDoubleShortMapDecorator();
 decorator.remove(1.0);
 }

 @Test
 public void testEntrySet() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Assert.assertEquals(2, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Assert.assertTrue(decorator.containsValue((short)1));
 Assert.assertTrue(decorator.containsValue((short)2));
 }

 @Test
 public void testContainsKey() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Assert.assertTrue(decorator.containsKey(1.0));
 Assert.assertTrue(decorator.containsKey(2.0));
 Assert.assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator = new TDoubleShortMapDecorator();
 Assert.assertTrue(decorator.isEmpty());
 decorator.put(1.0, (short)1);
 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 decorator = new TDoubleShortMapDecorator();
 TDoubleShortMap mapToAdd = new TDoubleShortMap();
 mapToAdd.put(1.0, (short)1);
 mapToAdd.put(2.0, (short)2);
 decorator.putAll(mapToAdd);
 TDoubleShortMap expectedMap = new TDoubleShortMap();
 expectedMap.put(1.0, (short)1);
 expectedMap.put(2.0, (short)2);
 Assert.assertEquals(expectedMap.size(), decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 decorator = new TDoubleShortMapDecorator(map);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
 Assert.assertEquals(decorator.getMap(), deserializedDecorator.getMap());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 decorator = new TDoubleShortMapDecorator(map);
 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();
 Assert.assertEquals(decorator.getMap(), clonedDecorator.getMap());
 }

}