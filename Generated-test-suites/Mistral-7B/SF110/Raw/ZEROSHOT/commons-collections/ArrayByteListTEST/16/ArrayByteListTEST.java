package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {

    @org.junit.Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @org.junit.Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
        assertNotNull(list.get(0));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-5);
    }

    @org.junit.Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList(5);
        list1.add((byte) 1);
        list1.add((byte) 2);

        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 3);
        list2.add((byte) 4);
        list2.add((byte) 5);

        list1.addAll(list2);
        assertEquals(8, list1.size());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 3, (byte) 4, (byte) 5}, list1.toByteArray());
    }

    @org.junit.Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.get(2);
    }

    @org.junit.Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.set(0, (byte) 3);
        assertEquals((byte) 3, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(2, (byte) 3);
    }

    @org.junit.Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.add((byte) 3);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, list.toByteArray());
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add(6, (byte) 3);
    }

    @org.junit.Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertEquals(2, list.size());
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(2);
    }

    @org.junit.Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();

        assertEquals(3, deserializedList.size());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, deserializedList.toByteArray());
    }

    @org.junit.Test
    public void testRandomAccess() {
        final int size = 1000;
        ArrayByteList list = new ArrayByteList(size);
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            list.add((byte) random.nextByte());
        }

        byte value = (byte) 0x5a;
        int index = random.nextInt(size);

        list.set(index, value);
        assertEquals(value, list.get(index));

        assertFalse(Arrays.equals(value, list.toByteArray()));
        assertTrue(Arrays.binarySearch(list.toByteArray(), value) >= 0);

        list.removeElement((byte) value);
        assertFalse(Arrays.binarySearch(list.toByteArray(), value) >= 0);
    }
}