package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertArrayEquals(new byte[5], list._data);
    }

    @Test
    public void testArrayByteListWithByteCollection() {
        ByteCollection byteCollection = new ArrayList<Byte>();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(byteCollection);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testGetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 1, list.get(list.size() - 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexLargerThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.get(3);
    }

    @Test
    public void testSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAtMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(1, list.size());
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexLargerThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(1);
    }

    @Test
    public void testSetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(0, (byte) 3);
        assertEquals((byte) 3, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexLargerThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.set(1, (byte) 1);
    }

    @Test
    public void testAddMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexLargerThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacityMethod() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(5);
        assertArrayEquals(new byte[5], list._data);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
    }

    @Test
    public void testTrimToSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();
        assertEquals(3, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        assertEquals(2, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
    }

    @Test(expected = IOException.class)
    public void testSerializationWithNullData() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList() {
            @Override
            byte[] _data;
        };
        list.ensureCapacity(5);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
        objectOutputStream.writeObject(list);
    }
}