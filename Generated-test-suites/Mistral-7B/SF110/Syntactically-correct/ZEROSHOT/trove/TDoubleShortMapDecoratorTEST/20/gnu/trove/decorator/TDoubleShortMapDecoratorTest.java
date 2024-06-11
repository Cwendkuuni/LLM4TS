import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleShortMapView;

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
 Assert.assertNotNull(decorator);
 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 TDoubleShortMap view = decorator.entrySet().iterator().next().getValue();
 TDoubleShortMapView mapView = view.view();

 TDoubleShortIterator it = mapView.iterator();
 it.next(); // skip no entry key
 Assert.assertEquals(1.0, it.key(), 0.001);
 Assert.assertEquals(Integer.shortValue((byte)1), it.value().shortValue());
 it.next();
 Assert.assertEquals(2.0, it.key(), 0.001);
 Assert.assertEquals(Integer.shortValue((byte)2), it.value().shortValue());
 }

 @Test
 public void testGet() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Short value = decorator.get(1.0);
 Assert.assertNotNull(value);
 Assert.assertEquals(Integer.shortValue((byte)1), value.shortValue());

 value = decorator.get(2.0);
 Assert.assertNotNull(value);
 Assert.assertEquals(Integer.shortValue((byte)2), value.shortValue());

 value = decorator.get(3.0);
 Assert.assertNull(value);
 }

 @Test
 public void testClear() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();

 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.remove(1.0);

 Assert.assertEquals(1, decorator.size());
 Assert.assertNull(decorator.get(1.0));
 Assert.assertEquals(Integer.shortValue((byte)2), decorator.get(2.0).shortValue());
 }

 @Test
 public void testEntrySet() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();

 Assert.assertEquals(2, entrySet.size());
 for (Entry<Double, Short> entry : entrySet) {
 Double key = entry.getKey();
 Short value = entry.getValue();
 if (key.equals(Double.valueOf(1.0))) {
 Assert.assertEquals(Integer.shortValue((byte)1), value.shortValue());
 } else {
 Assert.assertEquals(Integer.shortValue((byte)2), value.shortValue());
 }
 }
 }

 @Test
 public void testContainsValue() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Assert.assertTrue(decorator.containsValue((short)1));
 Assert.assertTrue(decorator.containsValue((short)2));
 Assert.assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Assert.assertTrue(decorator.containsKey(Double.valueOf(1.0)));
 Assert.assertTrue(decorator.containsKey(Double.valueOf(2.0)));
 Assert.assertFalse(decorator.containsKey(Double.valueOf(3.0)));
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
 TDoubleShortMap srcMap = new TDoubleShortMap();
 srcMap.put(1.0, (short)1);
 srcMap.put(2.0, (short)2);

 decorator = new TDoubleShortMapDecorator();
 decorator.putAll(srcMap);

 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 objStream.writeObject(decorator);
 byte[] byteArray = byteStream.toByteArray();

 ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)objInputStream.readObject();

 Assert.assertEquals(decorator, deserializedDecorator);
 }
}