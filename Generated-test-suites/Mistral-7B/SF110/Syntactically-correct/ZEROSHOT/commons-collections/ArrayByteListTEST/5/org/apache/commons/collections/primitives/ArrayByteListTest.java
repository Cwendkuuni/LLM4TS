package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void setUp() {
 list = new ArrayByteList();
 }

 @Test
 public void testEmptyList() {
 assertFalse(list.isEmpty());
 assertEquals(0, list.size());
 }

 @Test
 public void testInitialCapacity() {
 list = new ArrayByteList(5);
 assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 list = new ArrayByteList(-1);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAdd() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 assertEquals(3, list.size());
 }

 @Test
 public void testAddAt() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add(0, (byte) 0);
 list.add(2, (byte) 4);
 assertEquals(4, list.size());
 }

 @Test
 public void testGet() {
 list.add((byte) 1);
 list.add((byte) 2);
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.get(2); // no exception thrown, but this is incorrect
 fail("IndexOutOfBoundsException expected but not thrown");
 }

 @Test
 public void testSet() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.set(0, (byte) 0);
 list.set(1, (byte) 3);
 assertEquals((byte) 0, list.get(0));
 assertEquals((byte) 3, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetOutOfBounds() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.set(2, (byte) 3); // no exception thrown, but this is incorrect
 fail("IndexOutOfBoundsException expected but not thrown");
 }

 @Test
 public void testRemoveElementAt() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.removeElementAt(0);
 assertEquals((byte) 2, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtOutOfBounds() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.removeElementAt(2); // no exception thrown, but this is incorrect
 fail("IndexOutOfBoundsException expected but not thrown");
 }

 @Test
 public void testTrimToSize() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.trimToSize();
 assertEquals(2, list.size());
 }

 @Test
 public void testSerializable() throws Exception {
 ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{(byte) 3, (byte) 4, (byte) 5});
 list = new ArrayByteList(is);
 assertEquals(3, list.size());
 }

 @After
 public void tearDown() {
 list = null;
 }
}