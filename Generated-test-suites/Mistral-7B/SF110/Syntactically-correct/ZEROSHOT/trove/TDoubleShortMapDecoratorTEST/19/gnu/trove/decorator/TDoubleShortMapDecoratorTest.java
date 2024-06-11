import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     Assert.assertNotNull(decorator);
     Assert.assertSame(map, decorator.getMap());
 }

 @Test
 public void testPut() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value = (Short)1;
     Double key = 1.0;

     decorator.put(key, value);
     Assert.assertEquals(value, decorator.get(key));
     Assert.assertEquals(1, decorator.size());
     Assert.assertSame(map, decorator.getMap());
 }

 @Test
 public void testGet() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value = (Short)1;
     Double key = 1.0;

     decorator.put(key, value);
     Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value = (Short)1;
     Double key = 1.0;

     decorator.put(key, value);
     decorator.clear();
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value = (Short)1;
     Double key = 1.0;

     decorator.put(key, value);
     decorator.remove(key);
     Assert.assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value1 = (Short)1;
     Short value2 = (Short)2;
     Double key1 = 1.0;
     Double key2 = 2.0;

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     TShortMap values = decorator.entrySet().values();
     Assert.assertEquals(value1, values.get(key1));
     Assert.assertEquals(value2, values.get(key2));
 }

 @Test
 public void testContainsValue() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value = (Short)1;
     Double key = 1.0;

     decorator.put(key, value);
     Assert.assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value = (Short)1;
     Double key = 1.0;

     decorator.put(key, value);
     Assert.assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value = (Short)1;
     Double key = 1.0;

     decorator.put(key, value);
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Short value1 = (Short)1;
     Short value2 = (Short)2;
     Double key1 = 1.0;
     Double key2 = 2.0;

     TDoubleMap keys = new TDoubleHashMap().put(key1, value1);
     TShortMap values = new TShortHashMap().put(key1, value1).put(key2, value2);

     decorator.putAll(values);

     Assert.assertEquals(value1, decorator.get(key1));
     Assert.assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testExternalizable() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(byteStream);

     decorator.writeExternal(out);
     out.close();

     byte[] bytes = byteStream.toByteArray();
     ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
     ObjectInput in = new ObjectInputStream(byteInput);

     TDoubleShortMapDecorator decorator2 = (TDoubleShortMapDecorator)in.readObject();
     in.close();

     Assert.assertEquals(decorator, decorator2);
 }

 @Test
 public void testCloneable() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)decorator.clone();
     Assert.assertNotSame(decorator, cloned);
     Assert.assertEquals(decorator, cloned.getMap());
     Assert.assertEquals(decorator.size(), cloned.size());
 }
}