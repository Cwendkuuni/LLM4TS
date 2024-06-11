import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.entry.TDoubleShortEntry;
import gnu.trove.map.view.TDoubleMapView;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNotNull(decorator);
     Assert.assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
     _map = new TDoubleShortOpenHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNotNull(decorator);
     Assert.assertSame(_map, decorator.getMap());
 }

 @Test
 public void testPutNullKey() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put(null, (short) 0));
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutWithValidKeyAndValue() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put(1.0, (short) 1));
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put(1.0, null));
 }

 @Test
 public void testGetNullKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetValidKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     Assert.assertEquals((short) 1, decorator.get(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKeyObject() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     Assert.assertNull(decorator.get(new Object(){ }));
 }

 @Test
 public void testClear() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     decorator.clear();
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemoveNullKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     Assert.assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveValidKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     Assert.assertEquals((short) 1, decorator.remove(1.0));
     Assert.assertFalse(decorator.containsKey(1.0));
 }

 @Test
 public void testEntrySet() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     decorator.put(2.0, (short) 2);
     TDoubleMapView<Double, Short> view = decorator.entrySet().asMap();
     Assert.assertEquals(2, view.size());
     Assert.assertTrue(view.containsKey(1.0));
     Assert.assertTrue(view.containsKey(2.0));
 }

 @Test
 public void testContainsValue() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     decorator.put(2.0, (short) 2);
     Assert.assertTrue(decorator.containsValue((short) 1));
     Assert.assertTrue(decorator.containsValue((short) 2));
 }

 @Test
 public void testContainsKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     decorator.put(2.0, (short) 2);
     Assert.assertTrue(decorator.containsKey(1.0));
     Assert.assertTrue(decorator.containsKey(2.0));
 }

 @Test
 public void testSize() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     decorator.put(2.0, (short) 2);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertTrue(decorator.isEmpty());
     decorator.put(1.0, (short) 1);
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     TDoubleShortMap map = new TDoubleShortOpenHashMap();
     map.put(1.0, (short) 1);
     map.put(2.0, (short) 2);
     decorator = new TDoubleShortMapDecorator();
     decorator.putAll(map);
     Assert.assertEquals(2, decorator.size());
     Assert.assertTrue(decorator.containsKey(1.0));
     Assert.assertTrue(decorator.containsKey(2.0));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
     Assert.assertNotNull(deserializedDecorator);
     Assert.assertSame(_map, deserializedDecorator.getMap());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map = new TDoubleShortHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();
     Assert.assertNotNull(clonedDecorator);
     Assert.assertSame(_map, clonedDecorator.getMap());
 }

 @Test
 public void testIterator() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put(1.0, (short) 1);
     decorator.put(2.0, (short) 2);
     TDoubleShortIterator iterator = decorator.iterator();
     TDoubleShortEntry entry = null;
     while (iterator.hasNext()) {
         entry = iterator.next();
         Assert.assertNotNull(entry);
         Assert.assertEquals(entry.getKey(), entry.getValue().doubleValue());
     }
 }

 @Test
 public void testExternalizationWithEmptyMap() throws IOException, ClassNotFoundException {
     decorator = new TDoubleShortMapDecorator();
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
     Assert.assertTrue(deserializedDecorator.isEmpty());
 }

 @Test
 public void testExternalizationWithMapContainingValues() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short) 1);
     decorator.put(2.0, (short) 2);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
     Assert.assertEquals(2, deserializedDecorator.size());
     Assert.assertTrue(deserializedDecorator.containsKey(1.0));
     Assert.assertTrue(deserializedDecorator.containsKey(2.0));
     Assert.assertEquals((short) 1, deserializedDecorator.get(1.0).shortValue());
     Assert.assertEquals((short) 2, deserializedDecorator.get(2.0).shortValue());
 }

}