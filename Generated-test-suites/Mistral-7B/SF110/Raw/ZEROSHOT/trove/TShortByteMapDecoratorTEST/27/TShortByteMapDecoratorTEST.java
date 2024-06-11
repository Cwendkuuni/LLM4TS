import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapBackingMap;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 @Test
 public void testEmptyDecorator() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 TShortByteMap emptyMap = new TShortByteMap();
 Assert.assertEquals(emptyMap, decorator.getMap());
 Assert.assertFalse(decorator.isEmpty());
 Assert.assertFalse(decorator.containsKey((Short) null));
 Assert.assertFalse(decorator.containsKey((Short) 1));
 Assert.assertNull(decorator.get((Short) 1));
 Assert.assertNull(decorator.get((Short) null));
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testDecoratorWithInitialMap() {
 TShortByteMap initialMap = new TShortByteMap();
 initialMap.put((short) 1, (byte) 1);
 initialMap.put((short) 2, (byte) 2);
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(initialMap);

 TShortByteMap underlyingMap = decorator.getMap();
 TShortByteMap expectedMap = new TShortByteMap();
 expectedMap.put((short) 1, (byte) 1);
 expectedMap.put((short) 2, (byte) 2);

 Assert.assertEquals(expectedMap, underlyingMap);

 Assert.assertFalse(decorator.isEmpty());
 Assert.assertEquals(2, decorator.size());

 Assert.assertNull(decorator.get((Short) null));
 Assert.assertEquals((Byte) Byte.valueOf("1"), decorator.get((Short) new Short((short) 1)));
 Assert.assertEquals((Byte) Byte.valueOf("2"), decorator.get((Short) new Short((short) 2)));

 Assert.assertFalse(decorator.containsKey((Short) null));
 Assert.assertTrue(decorator.containsKey((Short) new Short((short) 1)));
 Assert.assertTrue(decorator.containsKey((Short) new Short((short) 2)));

 Assert.assertNull(decorator.remove((Short) null));
 Assert.assertEquals((Byte) Byte.valueOf("1"), decorator.remove((Short) new Short((short) 1)));
 Assert.assertEquals((Byte) Byte.valueOf("2"), decorator.get((Short) new Short((short) 2)));
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testDecoratorWithNullKey() {
 TShortByteMap initialMap = new TShortByteMap();
 initialMap.put((short) 1, (byte) 1);
 initialMap.put((short) 2, (byte) 2);
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(initialMap);

 Assert.assertNull(decorator.get((Short) null));
 Assert.assertEquals((Byte) Byte.valueOf("1"), decorator.get((Short) new Short((short) 1)));
 Assert.assertEquals((Byte) Byte.valueOf("2"), decorator.get((Short) new Short((short) 2)));

 Assert.assertFalse(decorator.containsKey((Short) null));
 Assert.assertTrue(decorator.containsKey((Short) new Short((short) 1)));
 Assert.assertTrue(decorator.containsKey((Short) new Short((short) 2)));

 Assert.assertNull(decorator.remove((Short) null));
 Assert.assertEquals((Byte) Byte.valueOf("1"), decorator.remove((Short) new Short((short) 1)));
 Assert.assertEquals((Byte) Byte.valueOf("2"), decorator.get((Short) new Short((short) 2)));
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testDecoratorWithNullValue() {
 TShortByteMap initialMap = new TShortByteMap();
 initialMap.put((short) 1, (byte) 1);
 initialMap.put((short) 2, (byte) 2);
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(initialMap);

 Assert.assertEquals((Byte) null, decorator.get((Short) null));
 Assert.assertEquals((Byte) Byte.valueOf("1"), decorator.get((Short) new Short((short) 1)));
 Assert.assertEquals((Byte) Byte.valueOf("2"), decorator.get((Short) new Short((short) 2)));

 Assert.assertFalse(decorator.containsKey((Short) null));
 Assert.assertTrue(decorator.containsKey((Short) new Short((short) 1)));
 Assert.assertTrue(decorator.containsKey((Short) new Short((short) 2)));

 Assert.assertNull(decorator.remove((Short) null));
 Assert.assertEquals((Byte) Byte.valueOf("1"), decorator.remove((Short) new Short((short) 1)));
 Assert.assertEquals((Byte) Byte.valueOf("2"), decorator.get((Short) new Short((short) 2)));
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testIteration() throws Exception {
 TShortByteMap initialMap = new TShortByteMap();
 initialMap.put((short) 1, (byte) 1);
 initialMap.put((short) 2, (byte) 2);
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(initialMap);

 Byte[] expectedValues = {Byte.valueOf("1"), Byte.valueOf("2")};
 Short[] expectedKeys = {new Short((short) 1), new Short((short) 2)};

 TShortByteMapView view = decorator.entrySet().asView();
 TShortByteMapBackingMap backingMap = view.getBackingMap();
 TShortByteIterator iterator = backingMap.iterator();

 int i = 0;
 while (iterator.hasNext()) {
 Entry<Short, Byte> entry = iterator.next();
 Assert.assertEquals(expectedKeys[i], entry.getKey());
 Assert.assertEquals(expectedValues[i], entry.getValue());
 i++;
 }
 }

 @Test
 public void testSerialization() throws Exception {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 objStream.writeObject(decorator);
 objStream.close();

 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) inputStream.readObject();
 inputStream.close();

 Assert.assertEquals(2, deserializedDecorator.size());
 Assert.assertEquals((Byte) Byte.valueOf("1"), deserializedDecorator.get((Short) new Short((short) 1)));
 Assert.assertEquals((Byte) Byte.valueOf("2"), deserializedDecorator.get((Short) new Short((short) 2)));
 }
}