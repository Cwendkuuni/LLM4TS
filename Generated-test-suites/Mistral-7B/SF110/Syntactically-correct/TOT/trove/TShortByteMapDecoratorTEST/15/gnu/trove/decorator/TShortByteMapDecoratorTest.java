import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteView;
import gnu.trove.map.TShortByteMap.EmptyShortByteMap;
import gnu.trove.map.TShortByteMap.EmptyShortByteMapEntry;
import gnu.trove.map.TShortByteMap.ShortByteMappedByteArray;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyDecorator() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 Assert.assertNull(decorator.get(Short.MIN_VALUE));
 Assert.assertNull(decorator.get(Short.MAX_VALUE));
 Assert.assertNull(decorator.get(null));
 Assert.assertFalse(decorator.containsKey(Short.MIN_VALUE));
 Assert.assertFalse(decorator.containsKey(Short.MAX_VALUE));
 Assert.assertFalse(decorator.containsKey(null));
 Assert.assertNull(decorator.remove(Short.MIN_VALUE));
 Assert.assertNull(decorator.remove(Short.MAX_VALUE));
 Assert.assertNull(decorator.remove(null));
 Assert.assertFalse(decorator.containsValue((byte)0));
 Assert.assertFalse(decorator.containsValue((byte)1));
 }

 @Test
 public void testDecoratorWithMap() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);

 Assert.assertEquals(_map.size(), decorator.size());

 // Put
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 _map.put(key.shortValue(), value.byteValue());
 Assert.assertEquals(value, decorator.get(key));
 Assert.assertEquals(value.byteValue(), _map.get(key.shortValue()));

 // Get
 key = Short.valueOf((short)2);
 value = Byte.valueOf((byte)2);
 decorator.put(key, value);
 _map.put(key.shortValue(), value.byteValue());
 Assert.assertEquals(value, decorator.get(key));
 Assert.assertEquals(value.byteValue(), _map.get(key.shortValue()));
 Assert.assertEquals(null, decorator.get(null));
 Assert.assertEquals(null, decorator.get(Short.MIN_VALUE));
 Assert.assertEquals(null, decorator.get(Short.MAX_VALUE));

 // Clear
 decorator.clear();
 _map.clear();
 Assert.assertEquals(0, decorator.size());
 Assert.assertEquals(0, _map.size());

 // Contains
 Assert.assertTrue(decorator.containsKey(Short.valueOf((short)1)));
 Assert.assertTrue(decorator.containsKey(Short.valueOf((short)2)));
 Assert.assertFalse(decorator.containsKey(Short.valueOf((short)3)));
 Assert.assertTrue(_map.containsKey((short)1));
 Assert.assertTrue(_map.containsKey((short)2));
 Assert.assertFalse(_map.containsKey((short)3));

 // Remove
 decorator.remove(Short.valueOf((short)1));
 _map.removeKey((short)1);
 Assert.assertNull(decorator.get(Short.valueOf((short)1)));
 Assert.assertEquals(null, _map.get((short)1));

 // Entry Set
 TShortByteView view = _map.view();
 Assert.assertEquals(view.size(), decorator.size());
 for (TShortByteIterator it = view.iterator(); it.hasNext(); ) {
 Entry<Short, Byte> entry = it.next();
 Assert.assertEquals(entry.getKey(), decorator.get(entry.getKey()));
 Assert.assertEquals(entry.getValue(), decorator.get(entry.getKey()));
 }
 }

 @Test
 public void testDecoratorWithNull() {
 decorator = new TShortByteMapDecorator();
 // Put
 decorator.put((short)0, (byte)0);
 _map = decorator.getMap();
 _map.put((short)0, (byte)0);
 Assert.assertEquals(Byte.valueOf((byte)0), decorator.get((short)0));
 Assert.assertEquals(Byte.valueOf((byte)0), decorator.get((Short)Short.valueOf((short)0)));
 Assert.assertEquals(Byte.valueOf((byte)0), _map.get((short)0));
 Assert.assertEquals(Byte.valueOf((byte)0), _map.get(Short.valueOf((short)0)));

 // Get
 decorator.put((short)1, null);
 _map.put((short)1, (byte)1);
 Assert.assertEquals(null, decorator.get((short)1));
 Assert.assertNull(decorator.get((Short)Short.valueOf((short)1)));
 Assert.assertEquals(Byte.valueOf((byte)1), _map.get((short)1));
 Assert.assertEquals(Byte.valueOf((byte)1), _map.get(Short.valueOf((short)1)));

 // ContainsValue
 decorator.put((short)2, (byte)2);
 _map.put((short)2, (byte)2);
 Assert.assertTrue(decorator.containsValue((byte)2));
 Assert.assertTrue(_map.containsValue((byte)2));

 // ContainsKey
 decorator.put((short)3, (byte)3);
 _map.put((short)3, (byte)3);
 Assert.assertTrue(decorator.containsKey(Short.valueOf((short)3)));
 Assert.assertTrue(_map.containsKey((short)3));

 // ContainsNullKey
 decorator.put(null, null);
 _map.put(TShortByteMap.NO_ENTRY_KEY, TShortByteMap.NO_ENTRY_VALUE);
 Assert.assertTrue(decorator.containsKey(null));
 Assert.assertTrue(_map.containsKey(TShortByteMap.NO_ENTRY_KEY));

 // ContainsNullValue
 decorator.put((short)4, null);
 _map.put((short)4, TShortByteMap.NO_ENTRY_VALUE);
 Assert.assertTrue(decorator.containsValue(null));
 Assert.assertTrue(_map.containsValue(TShortByteMap.NO_ENTRY_VALUE));

 // Clear
 decorator.clear();
 _map.clear();
 Assert.assertEquals(0, decorator.size());
 Assert.assertEquals(0, _map.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 byte[] bytes = new ShortByteMappedByteArray(10).toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator decorator = (TShortByteMapDecorator)in.readObject();
 Assert.assertNotNull(decorator);
 Assert.assertEquals(5, decorator.size());

 ObjectOutput out = new ObjectOutputStream(new ByteArrayOutputStream());
 decorator.writeExternal(out);
 out.flush();
 byte[] bytesOut = out.toByteArray();
 ObjectInput inOut = new ObjectInputStream(new ByteArrayInputStream(bytesOut));
 TShortByteMapDecorator decoratorOut = (TShortByteMapDecorator)inOut.readObject();
 Assert.assertNotNull(decoratorOut);
 Assert.assertEquals(decorator, decoratorOut);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator original = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator clone = (TShortByteMapDecorator)original.clone();
 Assert.assertNotSame(original, clone);
 Assert.assertEquals(original.size(), clone.size());
 // Other assertions as in previous tests
 }
}