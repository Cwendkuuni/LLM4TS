package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;

public class ArrayByteListTest {

	@Test
	public void testEmptyList() {
		ArrayByteList list = new ArrayByteList();
		assertEquals(0, list.size());
		assertTrue(list.isEmpty());
	}

	@Test
	public void testInitialCapacity() {
		ArrayByteList list = new ArrayByteList(5);
		assertEquals(5, list.size());
		assertFalse(list.isEmpty());
		assertArrayEquals(new byte[5], list.toByteArray());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeInitialCapacity() {
		new ArrayByteList(-1);
	}

	@Test
	public void testAddAll() {
		ArrayByteList list = new ArrayByteList(5);
		list.add((byte) 1);
		list.add((byte) 2);
		ArrayByteList otherList = new ArrayByteList();
		otherList.add((byte) 3);
		otherList.add((byte) 4);
		list.addAll(otherList);
		assertEquals(7, list.size());
		assertArrayEquals(new byte[] {1, 2, 3, 4, 3, 4, 0}, list.toByteArray());
	}

	@Test
	public void testGet() {
		ArrayByteList list = new ArrayByteList();
		list.add((byte) 1);
		list.add((byte) 2);
		assertEquals((byte) 1, list.get(0));
		assertEquals((byte) 2, list.get(1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
	}

	@Test
	public void testSize() {
		ArrayByteList list = new ArrayByteList();
		assertEquals(0, list.size());
		list.add((byte) 1);
		list.add((byte) 2);
		assertEquals(2, list.size());
	}

	@Test
	public void testRemoveElementAt() {
		ArrayByteList list = new ArrayByteList();
		list.add((byte) 1);
		list.add((byte) 2);
		assertEquals((byte) 1, list.removeElementAt(0));
		assertEquals((byte) 2, list.get(0));
		assertEquals(1, list.size());
		assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(1));
	}

	@Test
	public void testSet() {
		ArrayByteList list = new ArrayByteList();
		list.add((byte) 1);
		list.add((byte) 2);
		assertEquals((byte) 1, list.set(0, (byte) 3));
		assertEquals((byte) 3, list.get(0));
		assertEquals((byte) 2, list.get(1));
		assertEquals(2, list.size());
	}

	@Test
	public void testAdd() {
		ArrayByteList list = new ArrayByteList();
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		assertEquals(3, list.size());
		assertEquals((byte) 1, list.get(0));
		assertEquals((byte) 2, list.get(1));
		assertEquals((byte) 3, list.get(2));
	}

	@Test
	public void testEnsureCapacity() {
		ArrayByteList list = new ArrayByteList();
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add((byte) 4);
		list.add((byte) 5);
		assertEquals(5, list.size());
		list.ensureCapacity(10);
		assertEquals(11, list.size());
		assertArrayEquals(new byte[11], list.toByteArray());
	}

	@Test
	public void testTrimToSize() {
		ArrayByteList list = new ArrayByteList(10);
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);
		list.add((byte) 4);
		list.add((byte) 5);
		list.trimToSize();
		assertEquals(5, list.size());
		assertEquals(5, list.capacity());
	}

	@Test
	public void testSerializablity() {
		ArrayByteList list = new ArrayByteList();
		list.add((byte) 1);
		list.add((byte) 2);
		list.add((byte) 3);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
		objectStream.writeObject(list);
		objectStream.close();
		byte[] serializedList = byteStream.toByteArray();
		byteStream.close();

		ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedList);
		ObjectInputStream objectInput = new ObjectInputStream(byteInput);
		ArrayByteList deserializedList = (ArrayByteList) objectInput.readObject();
		objectInput.close();
		byteInput.close();

		assertEquals(3, deserializedList.size());
		assertArrayEquals(new byte[] {1, 2, 3}, deserializedList.toByteArray());
	}

	private byte[] toByteArray(ArrayByteList list) {
		byte[] result = new byte[list.size()];
		System.arraycopy(list.toByteArray(), 0, result, 0, result.length);
		return result;
	}

	private void assertArrayEquals(byte[] expected, byte[] actual) {
		assertArrayEquals(expected, actual, "Byte arrays are not equal");
	}

	private void assertArrayEquals(byte[] expected, byte[] actual, String message) {
		assertArrayEquals(expected, actual, message, true);
	}
}