package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void setUp() {
 list = new ArrayByteList();
 }

 @Test
 public void testEmptyList() {
 assertEquals(0, list.size());
 assertNull(list.get(0));
 }

 @Test
 public void testCreateListWithInitialCapacity() {
 list = new ArrayByteList(5);
 assertEquals(0, list.size());
 assertNull(list.get(0));
 assertNotNull(list._data);
 assertEquals(5, list._data.length);
 }

 @Test
 public void testCreateListWithByteCollection() {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 output.write(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 ArrayByteList listFromCollection = new ArrayByteList(input);
 assertEquals(9, listFromCollection.size());
 for (int i = 0; i < 9; i++) {
 assertEquals((byte) (i + 1), listFromCollection.get(i));
 }
 }

 @Test
 public void testGet() {
 list.add(1);
 list.add(2);
 assertEquals(1, list.get(0));
 assertEquals(2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 list.add(1);
 list.add(2);
 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetIndexOutOfBounds() {
 list.add(1);
 list.add(2);
 list.get(2);
 }

 @Test
 public void testSize() {
 list.add(1);
 list.add(2);
 list.add(3);
 assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 list.add(1);
 list.add(2);
 list.add(3);
 byte removed = list.removeElementAt(1);
 assertEquals(2, removed);
 assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 list.add(1);
 list.add(2);
 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtIndexOutOfBounds() {
 list.add(1);
 list.add(2);
 list.removeElementAt(2);
 }

 @Test
 public void testSet() {
 list.add(1);
 list.add(2);
 list.set(0, (byte) 3);
 assertEquals(3, list.get(0));
 assertEquals(2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 list.add(1);
 list.add(2);
 list.set(-1, (byte) 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetIndexOutOfBounds() {
 list.add(1);
 list.add(2);
 list.set(2, (byte) 3);
 }

 @Test
 public void testAdd() {
 list.add(1);
 list.add(2);
 list.add(3);
 list.add(4);
 assertEquals(4, list.size());
 assertEquals(1, list.get(0));
 assertEquals(2, list.get(1));
 assertEquals(3, list.get(2));
 assertEquals(4, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
 list.add(1);
 list.add(2);
 list.add(-1, (byte) 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddIndexOutOfBounds() {
 list.add(1);
 list.add(2);
 list.add(3, (byte) 4);
 }

 @Test
 public void testAddAll() {
ByteArrayOutputStream output = new ByteArrayOutputStream();
 output.write(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 list.addAll(input);
 assertEquals(9, list.size());
 for (int i = 0; i < 9; i++) {
 assertEquals((byte) (i + 1), list.get(i));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testAddAllNullCollection() {
 ByteList nullList = null;
 list.addAll(nullList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNegativeCapacity() {
 ByteList emptyList = new ArrayByteList();
 list.addAll(emptyList, -1);
 }

 @Test
 public void testEnsureCapacity() {
 list.add(1);
 list.add(2);
 list.add(3);
 list.add(4);
 list.add(5);
 list.add(6);
 list.add(7);
 list.add(8);
 list.add(9);
 list.ensureCapacity(16);
 assertSame(list._data, list._data);
 assertEquals(10, list._data.length);
 }

 @Test
 public void testTrimToSize() {
 list.add(1);
 list.add(2);
 list.add(3);
 list.add(4);
 list.add(5);
 list.add(6);
 list.add(7);
 list.add(8);
 list.add(9);
 list.trimToSize();
 assertSame(list._data, list._data);
 assertEquals(9, list._data.length);
 }

 @Test
 public void testToString() {
 list.add(1);
 list.add(2);
 list.add(3);
 list.toString();
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ByteArrayOutputStream output2 = new ByteArrayOutputStream();
 list.add(1);
 list.add(2);
 list.add(3);
 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 ObjectOutputStream out = new ObjectOutputStream(output2);
 out.writeObject(list);
 List listFromStream = (List) new ObjectInputStream(new ByteArrayInputStream(output2.toByteArray())).readObject();
 assertEquals(list, listFromStream);
 }
}