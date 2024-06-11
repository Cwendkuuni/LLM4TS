package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;

public class ArrayByteListTest {

 private byte[] data;
 private ArrayByteList list;

 @Before
 public void setup() {
 data = new byte[] { 1, 2, 3, 4, 5 };
 list = new ArrayByteList(data);
 }

 @Test
 public void testEmptyConstructor() {
 ArrayByteList emptyList = new ArrayByteList();
 assertEquals(0, emptyList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentConstructorNegativeCapacity() {
 ArrayByteList list = new ArrayByteList(-1);
 fail("Should throw IllegalArgumentException with message 'capacity " + (-1) + "'");
 }

 @Test
 public void testConstructWithCollection() {
 ByteCollection byteCollection = new ArrayByteList(data);
 ArrayByteList list = new ArrayByteList(byteCollection);
 assertEquals(data.length, list.size());
 }

 @Test
 public void testGet() {
 byte byteValue = list.get(2);
 assertEquals(byteValue, data[2]);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 try {
 list.get(-1);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }

 try {
 list.get(5);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }
 }

 @Test
 public void testSize() {
 assertEquals(data.length, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 byte removedByte = list.removeElementAt(2);
 assertEquals(removedByte, data[2]);
 assertEquals(data.length - 1, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtOutOfBounds() {
 try {
 list.removeElementAt(-1);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }

 try {
 list.removeElementAt(5);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }
 }

 @Test
 public void testSet() {
 byte newByte = 6;
 byte oldByte = list.set(2, newByte);
 assertEquals(newByte, list.get(2));
 assertEquals(data[2], oldByte);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetOutOfBounds() {
 try {
 list.set(-1, (byte) 0);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }

 try {
 list.set(5, (byte) 0);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }
 }

 @Test
 public void testAdd() {
 byte newByte = 6;
 list.add(3, newByte);
 assertEquals(newByte, list.get(3));
 assertEquals(data.length + 1, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddOutOfBounds() {
 try {
 list.add(-1, (byte) 0);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }

 try {
 list.add(5, (byte) 0);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 //expected
 }
 }

 @Test
 public void testEnsurCapacity() {
 list.ensureCapacity(list.size() + 10);
 assertEquals(list.size(), data.length);
 }

 @Test
 public void testTrimToSize() {
 list.trimToSize();
 assertEquals(list.size(), data.length);
 }

 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 objectStream.writeObject(list);
 objectStream.close();
 byte[] bytes = byteStream.toByteArray();
 ArrayByteList deserializedList = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 assertEquals(list.size(), deserializedList.size());
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 objectStream.writeObject(list);
 objectStream.close();
 byte[] bytes = byteStream.toByteArray();
 ArrayByteList deserializedList = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 assertEquals(list.toString(), deserializedList.toString());
 }
}