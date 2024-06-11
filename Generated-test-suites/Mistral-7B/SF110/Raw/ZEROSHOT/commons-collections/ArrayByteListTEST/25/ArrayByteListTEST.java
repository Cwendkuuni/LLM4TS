package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;

public class ArrayByteListTest {

 private byte[] testData = new byte[] { 1, 2, 3, 4, 5 };

 @Test
 public void testConstructorWithInitialCapacity() {
 ArrayByteList arrayByteList = new ArrayByteList(5);
 assertEquals(5, arrayByteList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
 ArrayByteList arrayByteList = new ArrayByteList(-5);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testAddAll() {
 ArrayByteList arrayByteList = new ArrayByteList();
 arrayByteList.addAll(asByteList(testData));
 assertEquals(5, arrayByteList.size());
 }

 @Test
 public void testGet() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 for (int i = 0; i < testData.length; i++) {
 assertEquals(testData[i], arrayByteList.get(i));
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 arrayByteList.get(-1);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondSize() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 arrayByteList.get(5);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test
 public void testSet() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 byte newValue = 6;
 for (int i = 0; i < testData.length; i++) {
 arrayByteList.set(i, newValue);
 assertEquals(newValue, arrayByteList.get(i));
 }
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 byte removedElement = arrayByteList.removeElementAt(1);
 assertEquals(testData[1], removedElement);
 assertEquals(4, arrayByteList.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtOutOfBounds() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 arrayByteList.removeElementAt(-1);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBeyondSize() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 arrayByteList.removeElementAt(5);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList arrayByteList = new ArrayByteList();
 arrayByteList.ensureCapacity(10);
 assertEquals(10, arrayByteList.size());
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 arrayByteList.trimToSize();
 assertEquals(5, arrayByteList.size());
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 ArrayByteList arrayByteList = new ArrayByteList(testData);
 oos.writeObject(arrayByteList);
 byte[] byteArray = baos.toByteArray();
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
 assertEquals(testData.length, deserializedList.size());
 for (int i = 0; i < testData.length; i++) {
 assertEquals(testData[i], deserializedList.get(i));
 }
 }

 private ByteList asByteList(byte[] testData) {
 byte[] array = new byte[testData.length];
 System.arraycopy(testData, 0, array, 0, testData.length);
 return new ArrayByteList(array);
 }

}