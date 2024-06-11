package gnu.trove.decorator.test;

import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPutNullKey() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNull(decorator.put(null, (short) 1));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullValue() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNull(decorator.put((double) 1.0, null));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPut() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals((short) 2, decorator.put((double) 1.0, (short) 1));
     Assert.assertEquals((short) 1, decorator.get((double) 1.0));
 }

 @Test
 public void testGetNullKey() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetWrongTypeKey() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNull(decorator.get((Object) 1));
 }

 @Test
 public void testGetWrongTypeValue() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNull(decorator.get((double) 1.0, (Object) 1));
 }

 @Test
 public void testClear() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     decorator.clear();
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemoveNullKey() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put((double) 1.0, (short) 1);
     Assert.assertNull(decorator.remove(null));
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testRemoveNullValue() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put((double) 1.0, (short) 1);
     Assert.assertNull(decorator.remove((double) 1.0, null));
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testRemoveExistingKey() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     Assert.assertEquals((short) 1, decorator.remove((double) 1.0));
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testRemoveNonExistingKey() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNull(decorator.remove((double) 1.0));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testSize() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals(0, decorator.size());
     decorator.put((double) 1.0, (short) 1);
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertTrue(decorator.isEmpty());
     decorator.put((double) 1.0, (short) 1);
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Map<Double, Short> map = new HashMap<>();
     map.put((double) 1.0, (short) 1);
     map.put((double) 2.0, (short) 2);
     decorator.putAll(map);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testEntrySet() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Set<Entry<Double, Short>> entrySet = decorator.entrySet();
     Assert.assertFalse(entrySet.isEmpty());
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     Assert.assertEquals(2, entrySet.size());
 }

 @Test
 public void testContainsValue() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertFalse(decorator.containsValue((short) 3));
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 1);
     Assert.assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertFalse(decorator.containsKey((Object) 1));
     decorator.put((double) 1.0, (short) 1);
     Assert.assertTrue(decorator.containsKey((double) 1.0));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
     Assert.assertEquals(_map, deserializedDecorator.getMap());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map = new TDoubleDoubleHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();
     Assert.assertEquals(_map, clonedDecorator.getMap());
 }
}