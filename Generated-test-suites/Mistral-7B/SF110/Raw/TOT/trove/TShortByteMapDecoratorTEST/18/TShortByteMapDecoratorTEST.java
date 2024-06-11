import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.entry.MapEntry;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyDecorator() {
 decorator = new TShortByteMapDecorator();
 Assert.assertEquals(0, decorator.size());
 Assert.assertNull(decorator.get(Short.MIN_VALUE));
 }

 @Test
 public void testDecoratorWithMap() {
 map = TShortByteMaps.create();
 decorator = new TShortByteMapDecorator(map);
 map.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 map.put(Short.valueOf((short)2), Byte.valueOf((byte)2));

 Assert.assertEquals(map.size(), decorator.size());
 Assert.assertEquals(map.get(Short.valueOf((short)1)), decorator.get(Short.valueOf((short)1)));
 Assert.assertEquals(map.get(Short.valueOf((short)2)), decorator.get(Short.valueOf((short)2)));

 Short key = Short.valueOf((short)3);
 Byte value = Byte.valueOf((byte)3);
 decorator.put(key, value);
 map.put(key, value.byteValue());

 Assert.assertEquals(map.size() + 1, decorator.size());
 Assert.assertEquals(decorator.get(key), value);
 Assert.assertEquals(map.get(key), value.byteValue());
 }

 @Test
 public void testDecoratorWithNullKey() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNull(decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testDecoratorWithNullValue() {
 decorator = new TShortByteMapDecorator();
 decorator.put(Short.valueOf((short)1), null);
 }

 @Test
 public void testDecoratorEntrySet() {
 decorator = new TShortByteMapDecorator(map);

 Set<Map.Entry<Short, Byte>> decoratorEntrySet = decorator.entrySet();
 Iterator<Map.Entry<Short, Byte>> iterator = decoratorEntrySet.iterator();
 Map.Entry<Short, Byte> entry = null;

 // Check size
 Assert.assertEquals(map.size(), decoratorEntrySet.size());

 // Check iteration
 while (iterator.hasNext()) {
 entry = iterator.next();
 Assert.assertEquals(map.containsKey(entry.getKey()), decorator.containsKey(entry.getKey()));
 Assert.assertEquals(map.get(entry.getKey()), decorator.get(entry.getKey()));
 }
 }

 @Test
 public void testDecoratorClear() {
 decorator = new TShortByteMapDecorator(map);
 map.clear();
 decorator.clear();

 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testDecoratorRemove() {
 decorator = new TShortByteMapDecorator(map);
 map.remove(Short.valueOf((short)1));
 decorator.remove(Short.valueOf((short)1));

 Assert.assertEquals(map.size() - 1, decorator.size());
 }

 @Test
 public void testDecoratorExternalizable() {
 try {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 oos.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 TShortByteMapDecorator deserializeDecorator = (TShortByteMapDecorator)ois.readObject();
 ois.close();

 // Check that deserialized decorator is equal to original one
 Assert.assertEquals(decorator, deserializeDecorator);
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
}

 @Test
 public void testDecoratorCloneable() {
 try {
 TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();
 // Check that cloned decorator is equal to original one
 Assert.assertEquals(decorator, clonedDecorator);
 // Check that cloned decorator's map is equal to original one but not the same instance
 TShortByteMap clonedMap = clonedDecorator.getMap();
 Assert.assertEquals(map, clonedMap);
 Assert.assertNotSame(map, clonedMap);
 } catch (CloneNotSupportedException e) {
 e.printStackTrace();
 }
}
}