import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
     _map = new TDoubleFloatMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNotNull(decorator);
     Assert.assertSame(_map, decorator.getMap());
 }

 @Test
 public void testPutNullKey() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put(null, (short) 0));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullValue() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put((double) 1.0, null));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPut() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put((double) 1.0, (short) 1));
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals((short) 1, decorator.get((double) 1.0));
 }

 @Test
 public void testGetNullKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetWrongKeyType() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     Assert.assertNull(decorator.get(new Integer(1)));
 }

 @Test
 public void testGet() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     Assert.assertEquals((short) 1, decorator.get((double) 1.0));
 }

 @Test
 public void testClear() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     decorator.clear();
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemoveNullKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     Assert.assertNull(decorator.remove(null));
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testRemoveWrongKeyType() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     Assert.assertNull(decorator.remove(new Integer(1)));
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testRemove() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     Assert.assertEquals((short) 1, decorator.remove((double) 1.0));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testEntrySet() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     Set<Entry<Double, Short>> entrySet = decorator.entrySet();
     final Iterator<Entry<Double, Short>> it = entrySet.iterator();
     Assert.assertEquals(2, entrySet.size());
     Assert.assertTrue(it.hasNext());
     Entry<Double, Short> entry = it.next();
     Assert.assertEquals((double) 1.0, entry.getKey());
     Assert.assertEquals((short) 1, entry.getValue());
     Assert.assertTrue(it.hasNext());
     entry = it.next();
     Assert.assertEquals((double) 2.0, entry.getKey());
     Assert.assertEquals((short) 2, entry.getValue());
 }

 @Test
 public void testContainsValue() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 1);
     Assert.assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     Assert.assertTrue(decorator.containsKey((double) 1.0));
 }

 @Test
 public void testSize() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertTrue(decorator.isEmpty());
     decorator.put((double) 1.0, (short) 1);
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TDoubleFloatMap();
     decorator = new TDoubleShortMapDecorator(_map);
     TDoubleShortMap mapToAdd = new TDoubleShortMap();
     mapToAdd.put((double) 1.0, (short) 1);
     mapToAdd.put((double) 2.0, (short) 2);
     decorator.putAll(mapToAdd);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() {
     _map = new TDoubleFloatMap();
     decorator = new TDoubleShortMapDecorator(_map);
     ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(byteArrayOS);
     decorator.writeExternal(out);
     byte[] bytes = byteArrayOS.toByteArray();
     ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(bytes);
     ObjectInput in = new ObjectInputStream(byteArrayIS);
     TDoubleShortMapDecorator decFromStream = (TDoubleShortMapDecorator)in.readObject();
     Assert.assertEquals(decorator.size(), decFromStream.size());
 }
}