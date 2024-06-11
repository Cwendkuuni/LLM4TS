package org.apache.commons.collections.primitives;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ArrayByteListTest {

 @Test
 public void testArrayByteListEmptyConstruction() {
 ArrayByteList list = new ArrayByteList();
 assertEquals(0, list.size());
 assertNull(list.getData());
 }

 @Test
 public void testArrayByteListInitialCapacityConstruction() {
 ArrayByteList list = new ArrayByteList(5);
 assertEquals(0, list.size());
 assertNotNull(list.getData());
 assertEquals(5, list.getData().length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListNegativeInitialCapacityConstruction() {
 ArrayByteList list = new ArrayByteList(-1);
 // should throw IllegalArgumentException
 }

 @Test
 public void testArrayByteListAddAll() {
 ByteCollection byteCollection = new ArrayByteList(4);
 byteCollection.add((byte) 0);
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 byteCollection.add((byte) 3);

 ArrayByteList list = new ArrayByteList();
 list.addAll(byteCollection);

 assertEquals(4, list.size());
 assertArrayEquals(new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3}, list.getData());
 }

 @Test
 public void testGetMethod() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 assertEquals((byte) 0, list.get(0));
 assertEquals((byte) 1, list.get(1));
 assertEquals((byte) 2, list.get(2));
 assertEquals((byte) 3, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodOutOfBounds() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.get(4); // should throw IndexOutOfBoundsException
 }

 @Test
 public void testSizeMethod() {
 ArrayByteList list = new ArrayByteList();
 assertEquals(0, list.size());

 ArrayByteList list2 = new ArrayByteList(5);
 assertEquals(5, list2.size());

 ArrayByteList list3 = new ArrayByteList();
 list3.add((byte) 0);
 list3.add((byte) 1);
 list3.add((byte) 2);
 list3.add((byte) 3);
 assertEquals(4, list3.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 assertEquals((byte) 0, list.removeElementAt(0));
 assertEquals((byte) 1, list.removeElementAt(1));
 assertEquals((byte) 2, list.removeElementAt(2));
 assertEquals((byte) 3, list.removeElementAt(3));
 assertEquals(0, list.size());
 assertNull(list.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodOutOfBounds() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.removeElementAt(4); // should throw IndexOutOfBoundsException
 }

 @Test
 public void testSetMethod() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(0, (byte) 4);
 assertArrayEquals(new byte[]{(byte) 4, (byte) 1, (byte) 2, (byte) 3}, list.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodOutOfBounds() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(4, (byte) 5); // should throw IndexOutOfBoundsException
 }

 @Test
 public void testAddMethod() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add(0, (byte) 4);
 assertArrayEquals(new byte[]{(byte) 4, (byte) 0, (byte) 1, (byte) 2, (byte) 3}, list.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodOutOfBounds() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add(-1, (byte) 4); // should throw IndexOutOfBoundsException
 list.add(5, (byte) 4); // should throw IndexOutOfBoundsException
 }

 @Test
 public void testEnsureCapacityMethod() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.add((byte) 9);

 list.ensureCapacity(10);
 assertEquals(10, list.getData().length);
 }

 @Test
 public void testTrimToSizeMethod() {
 ArrayByteList list = new ArrayByteList(10);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.add((byte) 9);
 list.trimToSize();
 assertEquals(10, list.getData().length);
 }

 @Test
 public void testWriteObjectMethod() throws IOException {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(baos);
 out.writeObject(list);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream in = new ObjectInputStream(bais);
 ArrayByteList listFromStream = (ArrayByteList) in.readObject();
 in.close();

 assertEquals(4, listFromStream.size());
 assertArrayEquals(list.getData(), listFromStream.getData());
 }

 @Test
 public void testReadObjectMethod() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 baos.writeInt(4);
 for (int i = 0; i < 4; ++i) {
 baos.writeByte((byte) i);
 }
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 ArrayByteList list = (ArrayByteList) in.readObject();
 in.close();

 assertEquals(4, list.size());
 assertArrayEquals(new byte[]{0, 1, 2, 3}, list.getData());
 }

 @Test
 public void testCheckRangeMethod() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.checkRange(0);
 list.checkRange(1);
 list.checkRange(2);
 list.checkRange(3);
 list.checkRange(4); // should not throw an exception
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeMethodOutOfBounds() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.checkRange(5); // should throw IndexOutOfBoundsException
 }

 @Test
 public void testCheckRangeIncludingEndpointMethod() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.checkRangeIncludingEndpoint(0);
 list.checkRangeIncludingEndpoint(1);
 list.checkRangeIncludingEndpoint(2);
 list.checkRangeIncludingEndpoint(3);
 list.checkRangeIncludingEndpoint(4);
 list.checkRangeIncludingEndpoint(5); // should not throw an exception
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeIncludingEndpointMethodOutOfBounds() {
 ArrayByteList list = new ArrayByteList(4);
 list.add((byte) 0);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.checkRangeIncludingEndpoint(-1); // should throw IndexOutOfBoundsException
 list.checkRangeIncludingEndpoint(5); // should throw IndexOutOfBoundsException
 }
}