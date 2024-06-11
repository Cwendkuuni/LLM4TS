package test;

import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testEmptyMap() {
 Assert.assertNotNull(decorator);
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertEquals(3, decorator.size());
 Assert.assertFalse(decorator.isEmpty());

 Map.Entry<Double, Short> entry = decorator.entrySet().iterator().next();
 Assert.assertEquals(1.0, entry.getKey());
 Assert.assertEquals(Short.valueOf((short)1), entry.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 decorator.put(1.0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNoEntryKey() {
 decorator.get(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNoEntryValue() {
 decorator.get(1.0);
 decorator.clear();
 decorator.get(1.0);
 }

 @Test
 public void testGet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Short value = decorator.get(1.0);
 Assert.assertEquals(Short.valueOf((short)1), value);
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();

 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Short removed = decorator.remove(2.0);
 Assert.assertEquals(Short.valueOf((short)2), removed);
 Assert.assertEquals(2, decorator.size());

 removed = decorator.remove(1.0);
 Assert.assertEquals(Short.valueOf((short)1), removed);
 Assert.assertEquals(1, decorator.size());

 removed = decorator.remove(3.0);
 Assert.assertEquals(Short.valueOf((short)3), removed);
 Assert.assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertEquals(3, decorator.entrySet().size());

 Map.Entry<Double, Short> entry = decorator.entrySet().iterator().next();
 Assert.assertEquals(1.0, entry.getKey());
 Assert.assertEquals(Short.valueOf((short)1), entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertTrue(decorator.containsValue(Short.valueOf((short)1)));
 Assert.assertTrue(decorator.containsValue(Short.valueOf((short)2)));
 Assert.assertTrue(decorator.containsValue(Short.valueOf((short)3)));
 Assert.assertFalse(decorator.containsValue((short)4));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertTrue(decorator.containsKey(1.0));
 Assert.assertTrue(decorator.containsKey(2.0));
 Assert.assertTrue(decorator.containsKey(3.0));
 Assert.assertFalse(decorator.containsKey(4.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertEquals(3, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 decorator.clear();

 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map1 = new TDoubleShortMap();
 map1.put(1.0, (short)1);
 map1.put(2.0, (short)2);
 map1.put(3.0, (short)3);

 decorator.putAll(map1);

 Assert.assertEquals(3, decorator.size());
 Assert.assertFalse(decorator.isEmpty());

 Map.Entry<Double, Short> entry = decorator.entrySet().iterator().next();
 Assert.assertEquals(1.0, entry.getKey());
 Assert.assertEquals(Short.valueOf((short)1), entry.getValue());
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(outputStream);
 decorator.writeExternal(out);
 out.close();

 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
 TDoubleShortMapDecorator decorator2 = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 Assert.assertEquals(3, decorator2.size());
 Assert.assertFalse(decorator2.isEmpty());

 Map.Entry<Double, Short> entry2 = decorator2.entrySet().iterator().next();
 Assert.assertEquals(1.0, entry2.getKey());
 Assert.assertEquals(Short.valueOf((short)1), entry2.getValue());
 }

 @After
 public void tearDown() {
 decorator = null;
 map = null;
 }
}