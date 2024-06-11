package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testInitialCapacityArrayByteList() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityArrayByteList() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAllArrayByteList() {
        ArrayByteList list1 = new ArrayByteList(3);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);

        ArrayByteList list2 = new ArrayByteList(2);
        list2.add((byte) 4);
        list2.add((byte) 5);

        list1.addAll(list2);

        assertEquals(5, list1.size());
        assertEquals((byte) 1, list1.get(0));
        assertEquals((byte) 2, list1.get(1));
        assertEquals((byte) 3, list1.get(2));
        assertEquals((byte) 4, list1.get(3));
        assertEquals((byte) 5, list1.get(4));
    }

    @Test
    public void testGetByteArrayByteList() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte[] expected = {1, 2, 3};
        assertArrayEquals(expected, list.toByteArray());
    }

    @Test
    public void testAddByteArrayByteList() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte[] data = {4, 5, 6};
        list.add(data);

        assertEquals(6, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
        assertEquals((byte) 6, list.get(5));
    }

    @Test
    public void testSetByteArrayByteList() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        byte[] data = {6, 7, 8};
        list.set(0, data[0]);
        list.set(1, data[1]);
        list.set(2, data[2]);

        assertEquals((byte) 6, list.get(0));
        assertEquals((byte) 7, list.get(1));
        assertEquals((byte) 8, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    @Test
    public void testAddByteByteList() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte element = (byte) 4;
        list.add(2, element);

        assertEquals(4, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 4, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBoundsByteByteList() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte element = (byte) 4;
        list.add(-1, element);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBoundsByteByteList2() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte element = (byte) 4;
        list.add(5, element);
    }

    @Test
    public void testRemoveElementAtByteByteList() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        byte removed = list.removeElementAt(2);

        assertEquals(4, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 4, list.get(2));
        assertEquals((byte) 5, list.get(3));
        assertNull(removed);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBoundsByteByteList() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.removeElementAt(-1);
    }

    @Test
    public void testTrimToSizeByteByteList() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.trimToSize();

        assertEquals(5, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    @Test
    public void testTrimToSizeByteByteList2() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.trimToSize();
        list.add((byte) 6);

        assertEquals(6, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
        assertEquals((byte) 6, list.get(5));
    }

    @Test
    public void testEnsureCapacityByteByteList() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.ensureCapacity(6);

        assertEquals(6, list._data.length);
    }

    @Test
    public void testSerializationByteByteList() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(list);
        objStream.close();

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInputStream = new ObjectInputStream(byteInputStream);
        ArrayByteList deserializedList = (ArrayByteList) objInputStream.readObject();
        objInputStream.close();

        assertEquals(5, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
        assertEquals((byte) 3, deserializedList.get(2));
        assertEquals((byte) 4, deserializedList.get(3));
        assertEquals((byte) 5, deserializedList.get(4));
    }
}