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
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAll() {
        byte[] data = new byte[10];
        ArrayByteList list = new ArrayByteList(data);
        ArrayByteList otherList = new ArrayByteList();
        otherList.add((byte) 1);
        otherList.add((byte) 2);
        otherList.add((byte) 3);

        list.addAll(otherList);
        assertEquals(3, list.size());
        assertArrayEquals(data, list._data);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);

        list.get(1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.set(0, (byte) 3);
        assertEquals((byte) 3, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.set(-1, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);

        list.set(1, (byte) 3);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.add((byte) 3);
        assertEquals(3, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.add(-1, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.add(3, (byte) 3);
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
        assertEquals(5, list.size());
        assertArrayEquals(new byte[10], list._data);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.trimToSize();
        assertEquals(5, list.size());
        assertArrayEquals(new byte[5], list._data);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(list);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        in.close();

        assertEquals(3, deserializedList.size());
        assertArrayEquals(list._data, deserializedList._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);

        list.checkRange(1);
    }

    @Test
    public void testCheckRangeIncludingEndPoint() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.checkRangeIncludingEndpoint(0);
        list.checkRangeIncludingEndpoint(1);
        list.checkRangeIncludingEndpoint(2);

        assertEquals(3, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingNegativeEndPoint() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);

        list.checkRangeIncludingEndpoint(1);
    }

}