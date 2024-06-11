package test;

import static org.junit.Assert.*;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortHashMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
 TDoubleShortMap map = decorator.getMap();
 assertSame(map, _map);
 }

 @Test
 public void testPut() {
 Short value = (Short)1;
 Short oldValue = decorator.put(2.0, value);
 assertEquals(value, decorator.get(2.0));
 assertNull(oldValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (Short)1);
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(2.0, null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testGet() {
 Short value = (Short)1;
 decorator.put(2.0, value);
 assertEquals(value, decorator.get(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 decorator.get(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 Short value = (Short)1;
 decorator.put(2.0, value);
 Short removedValue = decorator.remove(2.0);
 assertEquals(value, removedValue);
 assertNull(decorator.get(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (Short)1);
 decorator.put(2.0, (Short)2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put(1.0, (Short)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortHashMap map = new TDoubleShortHashMap();
 map.put(1.0, (Short)1);
 map.put(2.0, (Short)2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (Short)1);
 decorator.put(2.0, (Short)2);
 assertTrue(decorator.containsValue((Short)1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (Short)1);
 decorator.put(2.0, (Short)2);
 assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertFalse(entrySet.isEmpty());
 }

 @Test
 public void testExternalizable() {
 // Add tests for readExternal and writeExternal methods here
 }

 @Test
 public void testCloneable() {
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)decorator.clone();
 assertEquals(decorator, cloned);
 }
}