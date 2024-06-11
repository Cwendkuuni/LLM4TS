package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class ArrayByteListTest implements Serializable {

	@Test
	public void testConstructor() {
		ArrayByteList list = new ArrayByteList();
		assertEquals(0, list.size());
		assertEquals(8, list._data.length);

		ArrayByteList list2 = new ArrayByteList(5);
		assertEquals(0, list2.size());
		assertEquals(5, list2._data.length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNegativeCapacity() {
		new ArrayByteList(-1);
	}

	@Test
	public void testAddAll() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		ArrayByteList list2 = new ArrayByteList(3);
		list2.add((byte) 3);
		list2.add((byte) 4);
		list2.add((byte) 5);
		list.addAll(list2);
		assertEquals(8, list.size());
		for (int i = 0; i < 8; i++) {
			assertEquals(i < 5 ? (byte) (i + 1) : list2.get(i - 5), list.get(i));
		}
	}

	@Test
	public void testGet() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		assertEquals((byte) 1, list.get(0));
		assertEquals((byte) 2, list.get(1));
		assertEquals((byte) 0, list.get(2));
		assertEquals((byte) 0, list.get(3));
		assertEquals((byte) 0, list.get(4));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetOutOfBounds() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.get(5);
	}

	@Test
	public void testSize() {
		ArrayByteList list = new ArrayByteList();
		assertEquals(0, list.size());
		list.add((byte) 1);
		assertEquals(1, list.size());
		list.add((byte) 2);
		assertEquals(2, list.size());
		list.add((byte) 3);
		assertEquals(3, list.size());
	}

	@Test
	public void testRemoveElementAt() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add((byte) 4);
		list.add((byte) 5);
		assertEquals((byte) 1, list.removeElementAt(0));
		assertEquals(4, list.size());
		assertEquals((byte) 2, list.get(0));
		assertEquals((byte) 3, list.get(1));
		assertEquals((byte) 4, list.get(2));
		assertEquals((byte) 5, list.get(3));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveElementAtOutOfBounds() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.removeElementAt(5);
	}

	@Test
	public void testSet() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.set(0, (byte) 4);
		assertEquals((byte) 4, list.get(0));
		assertEquals((byte) 2, list.get(1));
		assertEquals((byte) 3, list.get(2));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSetOutOfBounds() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.set(5, (byte) 3);
	}

	@Test
	public void testAdd() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add(4);
		assertEquals(5, list.size());
		assertEquals((byte) 1, list.get(0));
		assertEquals((byte) 2, list.get(1));
		assertEquals((byte) 3, list.get(2));
		assertEquals((byte) 4, list.get(3));
		assertEquals(4, list.get(4));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testAddOutOfBounds() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add(5, (byte) 4);
	}

	@Test
	public void testEnsureCapacity() {
		ArrayByteList list = new ArrayByteList();
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add((byte) 4);
		list.add((byte) 5);
		list.add((byte) 6);
		list.add((byte) 7);
		assertEquals(8, list.size());
		list.ensureCapacity(10);
		assertEquals(12, list._data.length);
	}

	@Test
	public void testTrimToSize() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add((byte) 4);
		list.add((byte) 5);
		list.trimToSize();
		assertEquals(5, list.size());
		assertEquals(5, list._data.length);
	}

	@Test
	public void testSerialization() throws Exception {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add((byte) 4);
		list.add((byte) 5);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(list);
		oos.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		ArrayByteList list2 = (ArrayByteList) ois.readObject();
		ois.close();

		assertEquals(5, list2.size());
		for (int i = 0; i < 5; i++) {
			assertEquals(list.get(i), list2.get(i));
		}
	}
}