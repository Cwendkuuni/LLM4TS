package org.apache.commons.cli;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 TDoubleShortMap _map;
 TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testPut() {
 short value = 1;
 decorator.put(1.0, value);
 Assert.assertEquals(value, decorator.get(1.0));
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short) 1);
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testPutNullValue() {
 short value = 1;
 decorator.put(1.0, null);
 Assert.assertNull(decorator.get(1.0));
 }

 @Test
 public void testGet() {
 decorator.put(1.0, (short) 1);
 Assert.assertEquals((short) 1, decorator.get(1.0));
 }

 @Test
 public void testGetNullKey() {
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 Assert.assertNull(decorator.get(1.0));
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 decorator.remove(1.0);
 Assert.assertNull(decorator.get(1.0));
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 TDoubleShortMap map = decorator.getMap();
 TDoubleShortIterator it = map.iterator();
 Assert.assertEquals(1.0, it.nextKey(), 0.001);
 Assert.assertEquals((short) 1, it.nextValue());
 it.advance();
 Assert.assertEquals(2.0, it.nextKey(), 0.001);
 Assert.assertEquals((short) 2, it.nextValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 Assert.assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 Assert.assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator.putAll(map);
 TDoubleShortMap actualMap = decorator.getMap();
 TDoubleShortIterator it = actualMap.iterator();
 Assert.assertEquals(1.0, it.nextKey(), 0.001);
 Assert.assertEquals((short) 1, it.nextValue());
 it.advance();
 Assert.assertEquals(2.0, it.nextKey(), 0.001);
 Assert.assertEquals((short) 2, it.nextValue());
 }

 @Test
 public void testWrapKey() {
 decorator.put(1.0, (short) 1);
 Assert.assertEquals(1.0, decorator.wrapKey(1.0), 0.001);
 }

 @Test
 public void testUnwrapKey() {
 decorator.put(1.0, (short) 1);
 Assert.assertEquals(1.0, decorator.unwrapKey(decorator.wrapKey(1.0)), 0.001);
 }

 @Test
 public void testWrapValue() {
 decorator.put(1.0, (short) 1);
 Assert.assertEquals((short) 1, decorator.wrapValue((short) 1));
 }

 @Test
 public void testUnwrapValue() {
 decorator.put(1.0, (short) 1);
 Assert.assertEquals((short) 1, decorator.unwrapValue(decorator.wrapValue((short) 1)));
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TDoubleShortMapDecorator dec = (TDoubleShortMapDecorator) in.readObject();
 TDoubleShortMap actualMap = dec.getMap();
 Assert.assertEquals(_map.size(), actualMap.size());
 }
}