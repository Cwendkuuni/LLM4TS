package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testEmptyList() {
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testInitialCapacity() {
        list = new ArrayByteList(4);
        assertEquals(0, list.size());
        assertArrayEquals(new byte[4], list._data);
    }

    @Test
    public void testAddByte() {
        list.add((byte) 1);
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testAddAllByteCollection() {
        list.add((byte) 1);
        list.addAll(new ArrayByteList(new byte[]{2, 3, 4}));
        assertEquals(4, list.size());
        assertArrayEquals(new byte[]{1, 2, 3, 4}, list._data);
    }

    @Test
    public void testGetByte() {
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        list.add((byte) 1);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetBeyondEndIndex() {
        list.add((byte) 1);
        list.get(list.size());
    }

    @Test
    public void testSetByte() {
        list.add((byte) 1);
        list.set(0, (byte) 2);
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetBeyondEndIndex() {
        list.set(list.size(), (byte) 1);
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        byte removed = list.removeElementAt(0);
        assertEquals((byte) 1, removed);
        assertEquals(1, list.size());
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveNegativeIndex() {
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveBeyondEndIndex() {
        list.removeElementAt(list.size());
    }

    @Test
    public void testAddAndGet() {
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testAddAndRemove() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.remove((byte) 1);
        assertEquals(1, list.size());
        assertEquals((byte) 2, list.get(0));
    }

    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.trimToSize();
        assertEquals(2, list.size());
    }

    @Test
    public void testSerialization() {
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();

        assertEquals(2, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialCapacityNegative() {
        list = new ArrayByteList(-1);
    }

    @Test
    public void testCheckRange() {
        list.add((byte) 1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(list.size()));
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        list.add((byte) 1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(list.size()));
        list.add((byte) 2);
        assertSame(list, list.checkRangeIncludingEndpoint(0));
        assertSame(list, list.checkRangeIncludingEndpoint(1));
        assertSame(list, list.checkRangeIncludingEndpoint(2));
    }

    @Test
    public void testIncrModCount() {
        list.add((byte) 1);
        list.incrModCount();
        list.add((byte) 2);
        list.incrModCount();
        assertEquals(2, list.modCount);
    }

    @Test
    public void testEnsureCapacity() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.add((byte) 10);
        list.ensureCapacity(11);
        assertTrue(list._data.length >= 11);
    }
}