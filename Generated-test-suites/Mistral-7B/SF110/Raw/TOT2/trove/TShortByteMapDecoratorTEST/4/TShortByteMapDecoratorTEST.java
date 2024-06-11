import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map = new TShortByteMap();
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 decorator = new TShortByteMapDecorator(map);
 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 decorator = new TShortByteMapDecorator();
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 decorator.put(key, value);
 Assert.assertEquals(value, decorator.get(key));
 }

 @Test
 public void testPutWithNullKey() {
 decorator = new TShortByteMapDecorator();
 Byte value = Byte.valueOf((byte) 1);
 Assert.assertNull(decorator.put(null, value));
 Assert.assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testPutWithNullValue() {
 decorator = new TShortByteMapDecorator();
 Short key = Short.valueOf((short) 1);
 Assert.assertNull(decorator.put(key, null));
 Assert.assertFalse(decorator.containsValue(null));
 }

 @Test
 public void testGetWithNullKey() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetWithNonExistentKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1));
 Assert.assertNull(decorator.get(Short.valueOf((short) 2)));
 }

 @Test
 public void testClear() {
 decorator = new TShortByteMapDecorator(map);
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator = new TShortByteMapDecorator(map);
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 decorator.put(key, value);
 Byte removedValue = decorator.remove(key);
 Assert.assertEquals(value, removedValue);
 Assert.assertFalse(decorator.containsKey(key));
 }

 @Test
 public void testRemoveWithNullKey() {
 decorator = new TShortByteMapDecorator(map);
 Assert.assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveWithNonExistentKey() {
 decorator = new TShortByteMapDecorator(map);
 Assert.assertNull(decorator.remove(Short.valueOf((short) 2)));
 }

 @Test
 public void testEntrySet() {
 decorator = new TShortByteMapDecorator(map);
 Byte value = Byte.valueOf((byte) 1);
 decorator.put((short) 1, value);
 Entry<Short, Byte> entry = new Entry<Short, Byte>() {
 @Override
 public Short getKey() {
 return Short.valueOf((short) 1);
 }

 @Override
 public Byte getValue() {
 return value;
 }

 @Override
 public boolean equals(Object o) {
 if (o instanceof Entry<?, ?>) {
 Entry<?, ?> entry1 = (Entry<?, ?>) o;
 return entry1.getKey().equals(this.getKey()) && entry1.getValue().equals(this.getValue());
 }
 return false;
 }

 @Override
 public int hashCode() {
 return this.getKey().hashCode() + this.getValue().hashCode();
 }
 };
 Assert.assertTrue(decorator.entrySet().contains(entry));
 }

 @Test
 public void testContainsValue() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 Assert.assertTrue(decorator.containsValue((byte) 1));
 }

 @Test
 public void testContainsKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 Assert.assertTrue(decorator.containsKey((short) 1));
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testAddAll() {
 decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.addAll(Arrays.asList(new Entry<Short, Byte>() {
 @Override
 public Short getKey() {
 return Short.valueOf((short) 2);
 }

 @Override
 public Byte getValue() {
 return Byte.valueOf((byte) 2);
 }
 }));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 TShortByteMapDecorator decoratorToWrite = new TShortByteMapDecorator(map);
 oos.writeObject(decoratorToWrite);
 oos.writeByte(0);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 TShortByteMapDecorator readDecorator = (TShortByteMapDecorator)ois.readObject();
 ois.close();
 Assert.assertEquals(decoratorToWrite.getMap(), readDecorator.getMap());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 TShortByteMapDecorator readDecorator = (TShortByteMapDecorator)ois.readObject();
 ois.close();
 Assert.assertEquals(decorator, readDecorator);
 }
}