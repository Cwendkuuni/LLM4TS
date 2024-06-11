package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testNewArrayByteList() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
        assertArrayEquals(new byte[5], list.toByteArray());
        assertNull(list.get(5));
    }

    @Test
    public void testNewArrayByteListFromByteCollection() {
        byte[] source = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(new ArrayByteList(source));
        assertEquals(5, list.size());
        assertArrayEquals(source, list.toByteArray());
    }

    @Test
    public void testGet() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], list.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetBeyondLastIndex() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        list.get(5);
    }

    @Test
    public void testSet() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        byte newElement = 10;
        for (int i = 0; i < data.length; i++) {
            list.set(i, newElement);
            assertEquals(newElement, list.get(i));
        }
        assertEquals(data, list.toByteArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        list.set(-1, (byte) 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetBeyondLastIndex() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        list.set(5, (byte) 0);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));

        list.add(1, (byte) 0);
        assertEquals(2, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte) 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddBeyondLastIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(10, (byte) 0);
    }

    @Test
    public void testRemoveElementAt() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        byte removed = list.removeElementAt(2);
        assertEquals(4, list.size());
        assertArrayEquals(new byte[]{1, 2, 4, 5}, list.toByteArray());
        assertEquals((byte) 3, removed);

        list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtBeyondLastIndex() {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        list.removeElementAt(5);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(6);
        assertEquals(6, list.size());
        assertArrayEquals(new byte[6], list.toByteArray());

        list = new ArrayByteList(new byte[10]);
        list.ensureCapacity(15);
        assertEquals(15, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(new byte[10]);
        list.trimToSize();
        assertEquals(10, list.size());
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        byte[] data = {1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();
        assertArrayEquals(data, deserializedList.toByteArray());
    }
}