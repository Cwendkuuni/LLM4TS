package org.apache.commons.collections.primitives;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ArrayByteListTest implements Serializable {

 private byte[] data;
 private ArrayByteList list;

 @Before
 public void setUp() {
 data = new byte[]{1, 2, 3};
 list = new ArrayByteList(data);
 }

 @Test
 public void testEmptyConstructor() {
 List<Byte> emptyList = new ArrayByteList<>();
 assertEquals(0, emptyList.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 List<Byte> listWithInitialCapacity = new ArrayByteList(3);
 assertEquals(0, listWithInitialCapacity.size());
 }

 @Test
 public void testConstructorWithByteCollection() {
 List<Byte> listWithByteCollection = new ArrayByteList<>(Arrays.asList(1, 2, 3));
 assertEquals(3, listWithByteCollection.size());
 }

 @Test
 public void testGet() {
 assertEquals(1, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondEndIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
 }

 @Test
 public void testSize() {
 assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 byte oldValue = list.removeElementAt(0);
 assertEquals(2, list.size());
 assertEquals(2, oldValue);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBeyondEndIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(3));
 }

 @Test
 public void testSet() {
 list.set(0, (byte) 5);
 assertEquals(5, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 5));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondEndIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.set(4, (byte) 5));
 }

 @Test
 public void testAdd() {
 list.add(3, (byte) 4);
 assertEquals(4, list.get(3));
 assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, (byte) 4));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddBeyondEndIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, (byte) 4));
 }

 @Test
 public void testEnsureCapacity() {
 list.ensureCapacity(10);
 assertEquals(10, list._data.length);
 }

 @Test
 public void testTrimToSize() {
 list.trimToSize();
 assertEquals(3, list._data.length);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
 outputStream.writeObject(list);
 ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 ArrayByteList deserializedList = (ArrayByteList) inputStream.readObject();
 assertEquals(3, deserializedList.size());
 assertEquals(1, deserializedList.get(0));
 assertEquals(2, deserializedList.get(1));
 assertEquals(3, deserializedList.get(2));
 }

 @After
 public void tearDown() {
 list = null;
 data = null;
 }
}