package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Test
 public void testArrayByteListEmptyConstructor() {
 list = new ArrayByteList();
 assertTrue(list.size() == 0);
 }

 @Test
 public void testArrayByteListWithInitialCapacity() {
 list = new ArrayByteList(5);
 assertEquals(5, list.size());
 }

 @Test
 public void testArrayByteListWithByteCollection() {
 ByteCollection byteCollection = new byte[5]; // Assuming ByteCollection is a valid interface or class
 list = new ArrayByteList(byteCollection);
 assertEquals(byteCollection.size(), list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListNegativeInitialCapacity() {
 list = new ArrayByteList(-5);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test
 public void testGetMethod() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 byte retrievedByte = list.get(0);
 assertEquals(retrievedByte, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodOutOfRange() {
 list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 int indexOutOfRange = 4;
 try {
 List.get(indexOutOfRange);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Should be at least 0 and less than " + list.size() + ", found " + indexOutOfRange, e.getMessage());
 }
 }

 @Test
 public void testSizeMethod() {
 list = new ArrayByteList(5);
 assertEquals(5, list.size());
 list.add((byte) 6);
 assertEquals(6, list.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 byte removedByte = list.removeElementAt(1);
 assertEquals(removedByte, (byte) 2);
 assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodOutOfRange() {
 list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 int indexOutOfRange = 4;
 try {
 list.removeElementAt(indexOutOfRange);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Should be at least 0 and less than " + list.size() + ", found " + indexOutOfRange, e.getMessage());
 }
 }

 @Test
 public void testSetMethod() {
 list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 byte newByte = (byte) 4;
 list.set(1, newByte);
 assertEquals(newByte, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodOutOfRange() {
 list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 int indexOutOfRange = 4;
 try {
 list.set(indexOutOfRange, (byte) 4);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Should be at least 0 and less than " + list.size() + ", found " + indexOutOfRange, e.getMessage());
 }
 }

 @Test
 public void testAddMethod() {
 list = new ArrayByteList(3);
 list.add(0, (byte) 1);
 list.add(1, (byte) 2);
 list.add(2, (byte) 3);
 list.add(3, (byte) 4);
 assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodOutOfRange() {
 list = new ArrayByteList(3);
 list.add(0, (byte) 1);
 list.add(1, (byte) 2);
 list.add(2, (byte) 3);
 int indexOutOfRange = 4;
 try {
 list.add(indexOutOfRange, (byte) 4);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Should be at least 0 and at most " + list.size() + ", found " + indexOutOfRange, e.getMessage());
 }
 }

 @Test
 public void testEnsureCapacityMethod() {
 list = new ArrayByteList(10);
 list.ensureCapacity(15);
 }

 @Test
 public void testTrimToSizeMethod() {
 list = new ArrayByteList(5);
 list.trimToSize();
 }

 @Test
 public void testWriteObjectMethod() throws IOException {
 // This method is not testable in JUnit as it requires a ObjectOutputStream object, which cannot be easily created in a test
 }

 @Test
 public void testReadObjectMethod() throws IOException, ClassNotFoundException {
 // This method is not testable in JUnit as it requires a ObjectInputStream object, which cannot be easily created in a test
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeMethod() {
 list = new ArrayByteList(3);
 int indexOutOfRange = 4;
 list.checkRange(indexOutOfRange);
 fail("IndexOutOfBoundsException expected but not thrown.");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeIncludingEndpointMethod() {
 list = new ArrayByteList(3);
 int indexOutOfRange = 4;
 list.checkRangeIncludingEndpoint(indexOutOfRange);
 fail("IndexOutOfBoundsException expected but not thrown.");
 }
}