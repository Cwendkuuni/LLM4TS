package org.example.junit.test.gnu.trove.decorator;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import org.apache.commons.cli.util.TestUtils;
import org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator _decorator;

 @Test
 public void testInitialization() {
     _decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(_decorator.getMap());

     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals(_map, _decorator.getMap());
 }

 @Test
 public void testPut() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator();

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     _map.put(1.0, (short)1);
     _map.put(2.0, (short)2);

     Assert.assertEquals(_map.size(), _decorator.size());
     Assert.assertEquals(_map.get(1.0), _decorator.get(1.0));
 }

 @Test
 public void testPut_NullKey() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     Short value = _decorator.put((Double) null, (short) 1);
     Assert.assertNull(value);
     Assert.assertFalse(_map.containsKey(Double.MIN_VALUE));
 }

 @Test
 public void testPut_NullValue() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     Double key = 1.0;
     Short value = null;
     _decorator.put(key, value);

     Assert.assertEquals(_map.get(key), null);
 }

 @Test
 public void testGet() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     Assert.assertEquals((Short)1, _decorator.get(1.0));
     Assert.assertEquals((Short)2, _decorator.get(2.0));
     Assert.assertNull(_decorator.get(3.0));
 }

 @Test
 public void testGet_NullKey() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertNull(_decorator.get(null));
 }

 @Test
 public void testClear() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     _decorator.clear();
     Assert.assertEquals(0, _decorator.size());
     Assert.assertEquals(0, _map.size());
 }

 @Test
 public void testRemove() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     Short removedValue = _decorator.remove(1.0);
     Assert.assertEquals((Short)1, removedValue);
     Assert.assertEquals(1, _map.size());
 }

 @Test
 public void testRemove_NullKey() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     Short removedValue = _decorator.remove(null);
     Assert.assertNull(removedValue);
 }

 @Test
 public void testContainsValue() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     Assert.assertTrue(_decorator.containsValue((short)1));
     Assert.assertTrue(_decorator.containsValue((short)2));
 }

 @Test
 public void testContainsKey() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     Assert.assertTrue(_decorator.containsKey(1.0));
     Assert.assertTrue(_decorator.containsKey(2.0));
 }

 @Test
 public void testSize() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     Assert.assertEquals(2, _decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertTrue(_decorator.isEmpty());

     _decorator.put(1.0, (short)1);
     Assert.assertFalse(_decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     Map<Double, Short> mapToAdd = new HashMap<>();
     mapToAdd.put(1.0, (short)1);
     mapToAdd.put(2.0, (short)2);

     _decorator.putAll(mapToAdd);

     Assert.assertEquals(_map.size(), _decorator.size());
     Assert.assertEquals(_map.get(1.0), (short)1);
     Assert.assertEquals(_map.get(2.0), (short)2);
 }

 @Test
 public void testEntrySet() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     Set<Entry<Double, Short>> entrySet = _decorator.entrySet();

     Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
     Entry<Double, Short> entry = iterator.next();

     Assert.assertEquals(1.0, entry.getKey());
     Assert.assertEquals((Short)1, entry.getValue());
 }

 @Test
 public void testExternalizable() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);

     out.writeObject(_decorator);
     out.close();

     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
     ObjectInput in = new ObjectInputStream(bais);

     TDoubleShortMapDecorator dec2 = (TDoubleShortMapDecorator)in.readObject();
     in.close();

     Assert.assertEquals(_map, dec2.getMap());
 }

 @Test
 public void testCloneable() {
     _map = new TDoubleShortOpenHashMap();
     _decorator = new TDoubleShortMapDecorator(_map);

     TDoubleShortMapDecorator dec2 = null;
     try {
         dec2 = (TDoubleShortMapDecorator) _decorator.clone();
     } catch (CloneNotSupportedException e) {
         fail("Should not throw CloneNotSupportedException");
     }

     Assert.assertNotSame(_decorator, dec2);
     Assert.assertEquals(_map, dec2.getMap());
 }
}