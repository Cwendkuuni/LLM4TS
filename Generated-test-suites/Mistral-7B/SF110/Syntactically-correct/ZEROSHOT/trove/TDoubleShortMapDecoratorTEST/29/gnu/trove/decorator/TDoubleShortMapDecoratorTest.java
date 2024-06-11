package test;

import static org.junit.Assert.*;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(Collections.emptyMap(), decorator);
 assertTrue(decorator.isEmpty());
 assertFalse(decorator.containsKey(Double.MIN_VALUE));
 assertFalse(decorator.containsValue((short)1));
 assertFalse(decorator.containsValue((short)-1));
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testPutGet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertEquals((Short)1, decorator.get(1.0));
 assertEquals((Short)2, decorator.get(2.0));
 assertEquals(null, decorator.get(3.0));
 }

 @Test
 public void testPutOverwriteValue() {
 decorator.put(1.0, (short)1);
 decorator.put(1.0, (short)2);

 assertEquals((Short)2, decorator.get(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();

 assertTrue(decorator.isEmpty());
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(2.0));
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.remove(1.0);

 assertFalse(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 assertEquals(0, entries.size());

 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 entries = decorator.entrySet();
 Entry<Double, Short> entry = entries.iterator().next();
 assertEquals(1.0, entry.getKey().doubleValue(), 0.0);
 assertEquals((Short)1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short)1);
 map.put(2.0, (short)2);

 decorator.putAll(map);

 assertEquals(map, decorator);
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, decorator.unwrapKey(decorator.wrapKey(1.0)));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals(1.0, decorator.wrapKey(decorator.unwrapKey(1.0)));
 }

 @Test
 public void testWrapValue() {
 assertEquals((short)1, decorator.unwrapValue(decorator.wrapValue((short)1)));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((short)1, decorator.wrapValue(decorator.unwrapValue((short)1)));
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objStream);
 byteStream.flush();

 ObjectInput in = new ObjectInput(new ByteArrayInputStream(byteStream.toByteArray()));
 TDoubleShortMapDecorator decorator2 = (TDoubleShortMapDecorator)in.readObject();

 assertEquals(decorator, decorator2);
 }
}