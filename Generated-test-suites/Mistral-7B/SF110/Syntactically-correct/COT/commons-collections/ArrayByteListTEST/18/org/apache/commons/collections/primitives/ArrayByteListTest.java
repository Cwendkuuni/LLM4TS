package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Test
 public void testArrayByteListConstructorWithInitialCapacity() {
 list = new ArrayByteList(5);
 assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListConstructorNegativeInitialCapacity() {
 list = new ArrayByteList(-5);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testArrayByteListEmptyConstructor() {
 list = new ArrayByteList();
 assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListAddAll() {
 ByteCollection that = new ArrayByteList();
 that.add((byte) 1);
 that.add((byte) 2);
 list = new ArrayByteList();
 list.addAll(that);
 assertEquals(2, list.size());
 }

 @Test
 public void testArrayByteListGet() {
 list.add((byte) 1);
 list.add((byte) 2);
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListGetNegativeIndex() {
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.get(-1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListGetBeyondSize() {
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.get(3);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testArrayByteListSet() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.set(0, (byte) 3);
 assertEquals((byte) 3, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListSetNegativeIndex() {
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.set(-1, (byte) 3);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListSetBeyondSize() {
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.set(3, (byte) 3);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testArrayByteListAdd() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 assertEquals(3, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListAddNegativeIndex() {
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.add(-1, (byte) 3);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListAddBeyondSize() {
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.add(3, (byte) 3);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testArrayByteListEnsureCapacity() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.ensureCapacity(7);
 assertEquals(3, list.size());
 }

 @Test
 public void testArrayByteListTrimToSize() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.trimToSize();
 assertEquals(3, list.size());
 }

 @Test
 public void testArrayByteListWriteObject() throws IOException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.writeObject(objOut);
 ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 assertEquals(2, ((ArrayByteList) objIn.readObject()).size());
 }

 @Test
 public void testArrayByteListReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 objOut.writeObject(list);
 ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 assertEquals(2, ((ArrayByteList) objIn.readObject()).size());
 }

}