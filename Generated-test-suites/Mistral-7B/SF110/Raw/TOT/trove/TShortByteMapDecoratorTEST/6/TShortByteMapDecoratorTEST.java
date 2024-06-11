import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.map.view.TShortByteView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyDecorator() {
 decorator = new TShortByteMapDecorator();
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testDecoratorWithInitialMap() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 _map.put((short)1, (byte)1);
 _map.put((short)2, (byte)2);

 Assert.assertEquals(2, decorator.size());
 Assert.assertFalse(decorator.isEmpty());

 Byte value = decorator.get((short)1);
 Assert.assertEquals((byte)1, value);

 value = decorator.get((short)2);
 Assert.assertEquals((byte)2, value);

 value = decorator.get((short)-1);
 Assert.assertNull(value);

 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testDecoratorWithNullKey() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 Assert.assertNull(decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testDecoratorWithNullValue() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 decorator.put((short)1, null);
 }

 @Test
 public void testDecoratorWithEntrySet() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 _map.put((short)1, (byte)1);
 _map.put((short)2, (byte)2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();

 Assert.assertEquals(2, entrySet.size());

 Entry<Short, Byte> entry = iterator.next();
 Assert.assertEquals((short)1, entry.getKey());
 Assert.assertEquals((byte)1, entry.getValue());

 entry = iterator.next();
 Assert.assertEquals((short)2, entry.getKey());
 Assert.assertEquals((byte)2, entry.getValue());

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testDecoratorWithRemove() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 _map.put((short)1, (byte)1);
 _map.put((short)2, (byte)2);

 Byte value = decorator.remove((short)1);
 Assert.assertEquals((byte)1, value);
 Assert.assertEquals(1, decorator.size());

 value = decorator.remove((short)2);
 Assert.assertNull(value);
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testDecoratorWithPutAll() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 TShortByteMap mapToAdd = new TShortByteMapImpl();
 mapToAdd.put((short)1, (byte)1);
 mapToAdd.put((short)2, (byte)2);
 mapToAdd.put((short)3, (byte)3);

 decorator.putAll(mapToAdd);

 Assert.assertEquals(3, decorator.size());

 Byte value = decorator.get((short)1);
 Assert.assertEquals((byte)1, value);

 value = decorator.get((short)2);
 Assert.assertEquals((byte)2, value);

 value = decorator.get((short)3);
 Assert.assertEquals((byte)3, value);
 }

 @Test
 public void testDecoratorWithExternalizable() throws IOException, ClassNotFoundException {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 _map.put((short)1, (byte)1);
 _map.put((short)2, (byte)2);

 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
 ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());

 decorator.writeExternal(byteOutputStream);
 decorator = new TShortByteMapDecorator(_map);decorator.readExternal(byteInputStream);

 Assert.assertEquals(2, decorator.size());
 Byte value = decorator.get((short)1);
 Assert.assertEquals((byte)1, value);
 value = decorator.get((short)2);
 Assert.assertEquals((byte)2, value);
 }

 @Test
 public void testDecoratorWithCloneable() throws CloneNotSupportedException {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 _map.put((short)1, (byte)1);
 _map.put((short)2, (byte)2);
 TShortByteMapDecorator decoratorClone = (TShortByteMapDecorator)decorator.clone();

 Assert.assertEquals(2, decoratorClone.size());
 Byte value = decoratorClone.get((short)1);
 Assert.assertEquals((byte)1, value);
 value = decoratorClone.get((short)2);
 Assert.assertEquals((byte)2, value);
 }
}