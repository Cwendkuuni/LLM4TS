package org.apache.commons.cli;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator _decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortOpenHashMap();
 _decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testPut() {
 _decorator.put(1.0, (short) 1);
 _decorator.put(2.0, (short) 2);
 _decorator.put(3.0, (short) 3);

 assertEquals(3, _decorator.size());

 Double key = 1.0;
 Short value = (Short) _decorator.get(key);
 assertEquals((short) 1, value);
 }

 @Test
 public void testGetNullKey() {
 assertNull(_decorator.get(null));
 }

 @Test
 public void testGetWrongKeyType() {
 assertNull(_decorator.get("not a double"));
 }

 @Test
 public void testClear() {
 _decorator.clear();
 assertEquals(0, _decorator.size());
 }

 @Test
 public void testRemove() {
 _decorator.put(1.0, (short) 1);
 _decorator.put(2.0, (short) 2);
 _decorator.put(3.0, (short) 3);

 Short removedValue = _decorator.remove(2.0);
 assertEquals((short) 2, removedValue);

 assertEquals(2, _decorator.size());
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(_decorator.remove(null));
 }

 @Test
 public void testRemoveWrongKeyType() {
 assertNull(_decorator.remove("not a double"));
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = _decorator.entrySet();

 assertEquals(3, entrySet.size());

 Entry<Double, Short> entry = entrySet.iterator().next();
 assertEquals(1.0, entry.getKey());
 assertEquals((short) 1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 _decorator.put(1.0, (short) 1);
 _decorator.put(2.0, (short) 2);
 _decorator.put(3.0, (short) 3);

 assertTrue(_decorator.containsValue((short) 1));
 assertTrue(_decorator.containsValue((short) 2));
 assertTrue(_decorator.containsValue((short) 3));
 }

 @Test
 public void testContainsKey() {
 _decorator.put(1.0, (short) 1);
 _decorator.put(2.0, (short) 2);
 _decorator.put(3.0, (short) 3);

 assertTrue(_decorator.containsKey(1.0));
 assertTrue(_decorator.containsKey(2.0));
 assertTrue(_decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 _decorator.put(1.0, (short) 1);
 _decorator.put(2.0, (short) 2);
 _decorator.put(3.0, (short) 3);

 assertEquals(3, _decorator.size());
 }

 @Test
 public void testIsEmpty() {
 _decorator.clear();
 assertTrue(_decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortOpenHashMap();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 map.put(3.0, (short) 3);

 _decorator.putAll(map);

 assertEquals(3, _decorator.size());
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, _decorator.wrapKey(1.0));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals(1.0, _decorator.unwrapKey((Double) 1.0), 0.0);
 }

 @Test
 public void testWrapValue() {
 assertEquals((short) 1, _decorator.wrapValue((short) 1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((short) 1, _decorator.unwrapValue((Short) 1), (short) 0);
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);

 _decorator.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();

 TDoubleShortMapDecorator dec = new TDoubleShortMapDecorator();
 dec.readExternal(new ObjectInput(new ByteArrayInputStream(bytes)));

 assertEquals(3, dec.size());
 }

 @Test
 public void testCloneable() {
 try {
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)_decorator.clone();
 assertEquals(_decorator.size(), cloned.size());
 } catch (CloneNotSupportedException e) {
 fail("Should not throw CloneNotSupportedException");
 }
 }
}