package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyArrayByteList() {
        ArrayByteList byteList = new ArrayByteList();
        assertEquals(0, byteList.size());
        assertNull(byteList.get(0));
    }

    @Test
    public void testInitializeArrayByteList() {
        ArrayByteList byteList = new ArrayByteList(5);
        assertEquals(5, byteList.size());
        assertNotNull(byteList._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializeNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testCopyConstructor() {
        byte[] data = new byte[5];
        ArrayByteList source = new ArrayByteList(data);
        ArrayByteList target = new ArrayByteList(source);
        assertArrayEquals(data, target._data);
        assertEquals(5, target.size());
    }

    @Test
    public void testGet() {
        byte[] data = new byte[5];
        ArrayByteList byteList = new ArrayByteList(data);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], byteList.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList byteList = new ArrayByteList(5);
        byteList.get(5);
    }

    @Test
    public void testSet() {
        byte[] data = new byte[5];
        ArrayByteList byteList = new ArrayByteList(data);
        byte newByte = 12;
        byteList.set(0, newByte);
        assertEquals(newByte, byteList.get(0));
        assertEquals(data[1], byteList.get(1));
        for (int i = 2; i < data.length; i++) {
            assertEquals(data[i], byteList.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList byteList = new ArrayByteList(5);
        byteList.set(5, (byte) 12);
    }

    @Test
    public void testAdd() {
        byte[] data = new byte[5];
        ArrayByteList byteList = new ArrayByteList(data);
        byte newByte = 12;
        byteList.add(0, newByte);
        assertArrayEquals(new byte[]{newByte, data[0], data[1], data[2], data[3]}, byteList._data);
        assertEquals(6, byteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList byteList = new ArrayByteList(5);
        byteList.add(5, (byte) 12);
    }

    @Test
    public void testRemoveElementAt() {
        byte[] data = new byte[5];
        ArrayByteList byteList = new ArrayByteList(data);
        byte removedByte = byteList.removeElementAt(1);
        assertEquals(data[1], removedByte);
        assertArrayEquals(new byte[]{data[0], data[2], data[3], data[4]}, byteList._data);
        assertEquals(4, byteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList byteList = new ArrayByteList(5);
        byte removedByte = byteList.removeElementAt(5);
    }

    @Test
    public void testAddAll() {
        byte[] data1 = new byte[2];
        byte[] data2 = new byte[3];
        ArrayByteList byteList1 = new ArrayByteList(data1);
        ArrayByteList byteList2 = new ArrayByteList(data2);
        byteList1.addAll(byteList2);
        assertArrayEquals(new byte[]{data1[0], data1[1], data2[0], data2[1], data2[2]}, byteList1._data);
        assertEquals(5, byteList1.size());
    }

    @Test
    public void testTrimToSize() {
        byte[] data = new byte[5];
        ArrayByteList byteList = new ArrayByteList(data);
        byteList.trimToSize();
        assertArrayEquals(data, byteList._data);
        assertEquals(5, byteList.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList byteList = new ArrayByteList(5);
        byteList.ensureCapacity(10);
        assertEquals(10, byteList._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        byte[] data = new byte[5];
        ArrayByteList byteList = new ArrayByteList(data);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(byteList);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ArrayByteList deserializedByteList = (ArrayByteList) objectInputStream.readObject();
        assertArrayEquals(data, deserializedByteList._data);
        assertEquals(5, deserializedByteList.size());
    }
}