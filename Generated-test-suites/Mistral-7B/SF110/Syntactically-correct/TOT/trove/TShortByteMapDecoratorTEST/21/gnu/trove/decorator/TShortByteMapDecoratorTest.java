import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyMap() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get(Short.MIN_VALUE));
     Assert.assertFalse(decorator.containsKey(Short.MIN_VALUE));
     Assert.assertFalse(decorator.containsValue((byte)0));
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutGet() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key = Short.MIN_VALUE;
     byte value = (byte)1;
     decorator.put(key, value);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals(value, decorator.get(key));
     Assert.assertTrue(decorator.containsKey(key));
     Assert.assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testPutNullKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.put((short)null, (byte)0));
     Assert.assertEquals(0, decorator.size());
     Assert.assertFalse(decorator.containsKey((short)null));
 }

 @Test
 public void testPutNullValue() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key = Short.MIN_VALUE;
     Assert.assertNull(decorator.put(key, (byte)null));
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals(null, decorator.get(key));
     Assert.assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testPutSameKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key = Short.MIN_VALUE;
     byte oldValue = (byte)0, newValue = (byte)1;
     decorator.put(key, oldValue);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals(oldValue, decorator.get(key));
     Assert.assertTrue(decorator.containsKey(key));
     Assert.assertTrue(decorator.containsValue(oldValue));

     decorator.put(key, newValue);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals(newValue, decorator.get(key));
     Assert.assertTrue(decorator.containsKey(key));
     Assert.assertFalse(decorator.containsValue(oldValue));
 }

 @Test
 public void testRemove() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key = Short.MIN_VALUE;
     byte value = (byte)1;
     decorator.put(key, value);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals(value, decorator.get(key));
     Assert.assertTrue(decorator.containsKey(key));
     Assert.assertTrue(decorator.containsValue(value));

     decorator.remove(key);
     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get(key));
     Assert.assertFalse(decorator.containsKey(key));
     Assert.assertFalse(decorator.containsValue(value));
 }

 @Test
 public void testRemoveNonExistentKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.remove(Short.MIN_VALUE));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testClear() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key1 = Short.MIN_VALUE;
     byte value1 = (byte)1;
     short key2 = 0;
     byte value2 = (byte)2;
     decorator.put(key1, value1);
     decorator.put(key2, value2);
     Assert.assertEquals(2, decorator.size());

     decorator.clear();
     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get(key1));
     Assert.assertNull(decorator.get(key2));
 }

 @Test
 public void testIteration() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key1 = Short.MIN_VALUE;
     byte value1 = (byte)1;
     short key2 = 0;
     byte value2 = (byte)2;
     decorator.put(key1, value1);
     decorator.put(key2, value2);

     TShortByteMapView view = decorator.entrySet();
     Assert.assertEquals(2, view.size());

     Entry<Short, Byte> entry;
     while ((entry = view.iterator().next()) != null) {
         Assert.assertNull(entry.getKey());
         if (entry.getValue() == value1) {
             Assert.assertEquals(key1, decorator.get(entry.getKey()));
         } else {
             Assert.assertEquals(key2, decorator.get(entry.getKey()));
         }
         Assert.assertEquals(value1, (entry.getValue() == null ? decorator.get(key1) : entry.getValue()));
         Assert.assertEquals(value2, (entry.getValue() == null ? decorator.get(key2) : entry.getValue()));
     }
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key1 = Short.MIN_VALUE;
     byte value1 = (byte)1;
     short key2 = 0;
     byte value2 = (byte)2;
     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertTrue(decorator.containsValue(value1));
     Assert.assertTrue(decorator.containsValue(value2));
     Assert.assertFalse(decorator.containsValue((byte)-1));
 }

 @Test
 public void testContainsKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     short key1 = Short.MIN_VALUE;
     byte value1 = (byte)1;
     short key2 = 0;
     byte value2 = (byte)2;
     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertTrue(decorator.containsKey(key1));
     Assert.assertTrue(decorator.containsKey(key2));
     Assert.assertFalse(decorator.containsKey((short)-1));
 }

 @Test
 public void testExternalizable() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put(Short.MIN_VALUE, (byte)1);
     decorator.put(0, (byte)2);

     ByteArrayOutputStream out = new ByteArrayOutputStream();
     ObjectOutput os = new ObjectOutputStream(out);
     decorator.writeExternal(os);
     os.close();

     ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
     ObjectInput is = new ObjectInputStream(in);
     TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)is.readObject();
     is.close();

     Assert.assertEquals(decorator.size(), decorator2.size());
     for (Entry<Short, Byte> entry : decorator.entrySet()) {
         short key = entry.getKey();
         byte value = entry.getValue();
         Assert.assertEquals(key, decorator2.get(key));
         Assert.assertEquals(value, decorator2.get(key));
     }
 }
}