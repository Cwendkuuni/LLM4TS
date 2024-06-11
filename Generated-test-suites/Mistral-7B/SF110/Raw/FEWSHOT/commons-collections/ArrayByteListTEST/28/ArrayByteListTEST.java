package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    private ArrayByteList list;

    @Before
    public void setup() {
        list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
    }

    @Test
    public void testEmptyConstructor() {
        ArrayByteList emptyList = new ArrayByteList();
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList initialCapacityList = new ArrayByteList(5);
        assertEquals(0, initialCapacityList.size());
        assertNotNull(initialCapacityList._data);
        assertEquals(5, initialCapacityList._data.length);
    }

    @Test
    public void testConstructorFromByteCollection() {
        ArrayByteList thatList = new ArrayByteList();
        thatList.add((byte) 1);
        thatList.add((byte) 2);
        thatList.add((byte) 3);

        ArrayByteList listFromCollection = new ArrayByteList(thatList);
        assertEquals(3, listFromCollection.size());
        assertArrayEquals(new byte[]{1, 2, 3}, listFromCollection._data);
    }

    @Test
    public void testGet() {
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        list.get(-1);
    }

    @Test
    public void testSize() {
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        byte removed = list.removeElementAt(1);
        assertEquals((byte) 2, removed);
        assertEquals(2, list.size());
        assertArrayEquals(new byte[]{1, 3}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        list.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        list.set(0, (byte) 4);
        assertEquals((byte) 4, list.get(0));
        assertEquals(3, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        list.set(-1, (byte) 1);
    }

    @Test
    public void testAdd() {
        list.add(0, (byte) 0);
        assertEquals(4, list.size());
        assertEquals((byte) 0, list.get(0));
        assertArrayEquals(new byte[]{0, 1, 3}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        list.add(-1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(5);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[]{1, 2, 3}, list._data);
        assertNotNull(list._data);
        assertEquals(8, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        list.trimToSize();
        assertEquals(3, list.size());
        assertNotNull(list._data);
        assertEquals(3, list._data.length);
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(list);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);

        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        assertEquals(3, deserializedList.size());
        assertArrayEquals(new byte[]{1, 2, 3}, deserializedList._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        list.checkRange(4);
    }
}