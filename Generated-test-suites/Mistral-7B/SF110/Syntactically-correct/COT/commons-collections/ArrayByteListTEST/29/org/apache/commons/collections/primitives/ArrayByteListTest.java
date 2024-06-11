package org.apache.commons.collections.primitives.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class ArrayByteListTest {

 private static final int INITIAL_CAPACITY = 10;
 private static final int TEST_SIZE = 5;
 private static final byte TEST_ELEMENT = 0x01;

 @Test
 public void testConstructorWithNoArguments() {
     ArrayByteList arrayByteList = new ArrayByteList();
     assertEquals(0, arrayByteList.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
     ArrayByteList arrayByteList = new ArrayByteList(INITIAL_CAPACITY);
     assertEquals(0, arrayByteList.size());
     assertEquals(INITIAL_CAPACITY, arrayByteList.capacity());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
     ArrayByteList arrayByteList = new ArrayByteList(-1);
 }

 @Test
 public void testAddAllFromCollection() {
     ArrayByteList arrayByteList = new ArrayByteList(3);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     assertEquals(3, arrayByteList.size());
     for (int i = 0; i < 3; ++i) {
         assertEquals(TEST_ELEMENT, arrayByteList.get(i));
     }
 }

 @Test
 public void testGet() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.add(TEST_ELEMENT);
     for (int i = 0; i < TEST_SIZE; ++i) {
         assertEquals(TEST_ELEMENT, arrayByteList.get(i));
     }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeNegativeIndex() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     arrayByteList.checkRange(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeBiggerSize() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     arrayByteList.checkRange(TEST_SIZE + 1);
 }

 @Test
 public void testSet() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     arrayByteList.set(0, (byte) 0x02);
     assertEquals(TEST_ELEMENT, arrayByteList.get(1));
     assertEquals((byte) 0x02, arrayByteList.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     arrayByteList.set(-1, (byte) 0x03);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBiggerSize() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     arrayByteList.set(TEST_SIZE + 1, (byte) 0x04);
 }

 @Test
 public void testAdd() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.add((byte) 0x02);
     assertEquals(TEST_SIZE + 1, arrayByteList.size());
     assertEquals(TEST_ELEMENT, arrayByteList.get(TEST_SIZE));
     assertEquals((byte) 0x02, arrayByteList.get(TEST_SIZE + 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.add(-1, (byte) 0x03);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddBiggerSize() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE);
     arrayByteList.add(TEST_SIZE + 1, (byte) 0x04);
 }

 @Test
 public void testRemoveElementAt() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE + 1);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     assertEquals(TEST_ELEMENT, arrayByteList.removeElementAt(0));
     assertEquals(TEST_ELEMENT, arrayByteList.get(0));
     assertEquals(TEST_SIZE, arrayByteList.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE + 1);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     arrayByteList.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBiggerSize() {
     ArrayByteList arrayByteList = new ArrayByteList(TEST_SIZE + 1);
     arrayByteList.addAll(new ArrayByteList(new byte[] { TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT, TEST_ELEMENT }));
     arrayByteList.removeElementAt(TEST_SIZE + 1);
 }

 @Test
 public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
     ArrayByteList arrayByteList = new ArrayByteList(INITIAL_CAPACITY);
     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
     objectOutputStream.writeObject(arrayByteList);
     ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
     ArrayByteList deserializedArrayByteList = (ArrayByteList) objectInputStream.readObject();
     assertEquals(arrayByteList.size(), deserializedArrayByteList.size());
 }

}