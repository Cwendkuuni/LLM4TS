import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testEmptyConstructor() {
 decorator = new TDoubleShortMapDecorator();
 Assert.assertEquals(Collections.emptyMap(), decorator);
 }

 @Test
 public void testDecoratorConstructor() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Map<Double, Short> expected = new TDoubleShortMap();
 expected.put(1.0, (short)1);
 expected.put(2.0, (short)2);
 Assert.assertEquals(expected, decorator);
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyPut() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValuePut() {
 decorator = new TDoubleShortMapDecorator();
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Assert.assertEquals((short)1, decorator.get(1.0));
 Assert.assertEquals((short)2, decorator.get(2.0));
 Assert.assertEquals(null, decorator.get(3.0));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyGet() {
 decorator = new TDoubleShortMapDecorator();
 decorator.get(null);
 }

 @Test
 public void testClear() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();
 Assert.assertEquals(Collections.emptyMap(), decorator);
 }

 @Test
 public void testRemove() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.remove(1.0);
 Map<Double, Short> expected = new TDoubleShortMap();
 expected.put(2.0, (short)2);
 Assert.assertEquals(expected, decorator);
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyRemove() {
 decorator = new TDoubleShortMapDecorator();
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Assert.assertEquals(2, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Assert.assertTrue(decorator.containsValue((short)1));
 Assert.assertTrue(decorator.containsValue((short)2));
 Assert.assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Assert.assertTrue(decorator.containsKey(1.0));
 Assert.assertTrue(decorator.containsKey(2.0));
 Assert.assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 Assert.assertTrue(decorator.isEmpty());
 decorator.put(1.0, (short)1);
 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 Map<Double, Short> mapToAdd = new TDoubleShortMap();
 mapToAdd.put(1.0, (short)1);
 mapToAdd.put(2.0, (short)2);
 decorator.putAll(mapToAdd);
 Assert.assertEquals(mapToAdd, decorator);
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator decoratorFromSerialized = (TDoubleShortMapDecorator)in.readObject();
 Map<Double, Short> expected = new TDoubleShortMap();
 expected.put(1.0, (short)1);
 expected.put(2.0, (short)2);
 Assert.assertEquals(expected, decoratorFromSerialized);
 }

}