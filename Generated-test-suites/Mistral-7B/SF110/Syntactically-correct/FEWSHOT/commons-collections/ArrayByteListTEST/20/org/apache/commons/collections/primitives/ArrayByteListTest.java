package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddToEmptyList() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testAddToList() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(4, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertEquals((byte) 4, list.get(2));
        assertEquals((byte) 5, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals((byte) 1, list.set(0, (byte) 6));
        assertEquals((byte) 6, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.set(-1, (byte) 6);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList(5);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);
        list1.add((byte) 4);
        list1.add((byte) 5);

        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 6);
        list2.add((byte) 7);
        list2.add((byte) 8);

        list1.addAll(1, list2);
        assertEquals(8, list1.size());
        assertEquals((byte) 1, list1.get(0));
        assertEquals((byte) 6, list1.get(1));
        assertEquals((byte) 7, list1.get(2));
        assertEquals((byte) 8, list1.get(3));
        assertEquals((byte) 2, list1.get(4));
        assertEquals((byte) 3, list1.get(5));
        assertEquals((byte) 4, list1.get(6));
        assertEquals((byte) 5, list1.get(7));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllOutOfBounds() {
        ArrayByteList list1 = new ArrayByteList(5);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);
        list1.add((byte) 4);
        list1.add((byte) 5);

        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 6);
        list2.add((byte) 7);
        list2.add((byte) 8);

        list1.addAll(-1, list2);
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
    }

    @Test
    public void testTrimToSizeWithEmptyList() {
        ArrayByteList list = new ArrayByteList();
        list.trimToSize();
        assertEquals(0, list.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testEnsureCapacityWithFullList() {
        ArrayByteList list = new ArrayByteList(10);
        for (int i = 0; i < 10; i++) {
            list.add((byte) i);
        }
        list.ensureCapacity(5);
        assertEquals(15, list._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);

        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(byteInput);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();

        assertEquals(3, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
        assertEquals((byte) 3, deserializedList.get(2));
    }
}