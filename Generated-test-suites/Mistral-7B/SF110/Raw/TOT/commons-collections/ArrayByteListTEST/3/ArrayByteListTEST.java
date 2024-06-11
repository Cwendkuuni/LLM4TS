package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

 // Test constructor with no arguments
 @Test
 public void testEmptyConstructor() {
 ArrayByteList list = new ArrayByteList();
 assertEquals(0, list.size());
 }

 // Test constructor with initial capacity
 @Test
 public void testInitialCapacityConstructor() {
 ArrayByteList list = new ArrayByteList(10);
 assertEquals(0, list.size());
 assertArrayEquals(new byte[10], list._data);
 }

 // Test constructor from ByteCollection
 @Test
 public void testByteCollectionConstructor() {
 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
 byteOutputStream.write("Test".getBytes());
 ArrayByteList list = new ArrayByteList((ByteCollection) byteOutputStream);
 assertEquals(5, list.size());
 for (int i = 0; i < 5; i++) {
 assertEquals("Test".charAt(i), list.get(i));
 }
 }

 // Test get method
 @Test
 public void testGetMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 'A');
 list.set(1, (byte) 'B');
 list.set(2, (byte) 'C');
 list.set(3, (byte) 'D');
 list.set(4, (byte) 'E');
 for (int i = 0; i < 5; i++) {
 assertEquals(i + (char) 'A', list.get(i));
 }
 }

 // Test size method
 @Test
 public void testSizeMethod() {
 ArrayByteList list = new ArrayByteList();
 assertEquals(0, list.size());
 list.add((byte) 'A');
 assertEquals(1, list.size());
 list.add((byte) 'B');
 list.add((byte) 'C');
 list.add((byte) 'D');
 list.add((byte) 'E');
 assertEquals(5, list.size());
 }

 // Test removeElementAt method
 @Test
 public void testRemoveElementAtMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 'A');
 list.set(1, (byte) 'B');
 list.set(2, (byte) 'C');
 list.set(3, (byte) 'D');
 list.set(4, (byte) 'E');
 assertEquals(5, list.size());
 byte removedByte = list.removeElementAt(3);
 assertEquals('D', removedByte);
 assertEquals(4, list.size());
 }

 // Test set method
 @Test
 public void testSetMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 'A');
 list.set(1, (byte) 'B');
 list.set(2, (byte) 'C');
 list.set(3, (byte) 'D');
 list.set(4, (byte) 'E');
 for (int i = 0; i < 5; i++) {
 list.set(i, (byte) ('A' + i));
 }
 for (int i = 0; i < 5; i++) {
 assertEquals(i + (char) 'A', list.get(i));
 }
 }

 // Test add method
 @Test
 public void testAddMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 'A');
 list.set(1, (byte) 'B');
 list.set(2, (byte) 'C');
 list.set(3, (byte) 'D');
 list.set(4, (byte) 'E');
 list.add((byte) 'F');
 assertEquals(6, list.size());
 list.add(0, (byte) 'G');
 assertEquals(7, list.size());
 }

 // Test ensureCapacity method
 @Test
 public void testEnsureCapacityMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 'A');
 list.set(1, (byte) 'B');
 list.set(2, (byte) 'C');
 list.set(3, (byte) 'D');
 list.set(4, (byte) 'E');
 list.add(new byte[100]);
 assertEquals(106, list.size());
 }

 // Test trimToSize method
 @Test
 public void testTrimToSizeMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 'A');
 list.set(1, (byte) 'B');
 list.set(2, (byte) 'C');
 list.set(3, (byte) 'D');
 list.set(4, (byte) 'E');
 list.add(new byte[100]);
 list.trimToSize();
 assertEquals(5, list.size());
 }

 // Test Serialization
 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 // Test writing to ObjectOutputStream
 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteOutputStream);
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 'A');
 list.set(1, (byte) 'B');
 list.set(2, (byte) 'C');
 list.set(3, (byte) 'D');
 list.set(4, (byte) 'E');
 out.writeObject(list);
 out.close();
 byte[] writtenListBytes = byteOutputStream.toByteArray();

 // Test reading from ObjectInputStream
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(writtenListBytes));
 ArrayByteList deserializedList = (ArrayByteList) in.readObject();
 assertEquals(5, deserializedList.size());
 for (int i = 0; i < 5; i++) {
 assertEquals(i + (char) 'A', deserializedList.get(i));
 }
 }

 // Test checkRange method
 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.checkRange(-1);
 }

 // Test checkRange method with index greater than size
 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeGreaterSizeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.checkRange(5);
 }

 // Test checkRangeIncludingEndpoint method with index less than 0
 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeIncludingEndpointNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.checkRangeIncludingEndpoint(-1);
 }

 // Test checkRangeIncludingEndpoint method with index greater than size
 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeIncludingEndpointGreaterSizeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.checkRangeIncludingEndpoint(5);
 }
}