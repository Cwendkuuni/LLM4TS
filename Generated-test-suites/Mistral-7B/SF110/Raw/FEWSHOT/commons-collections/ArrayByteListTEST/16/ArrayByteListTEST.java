package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyArrayByteList() {
        final ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        final ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertArrayEquals(new byte[5], list._data);
    }

    @Test
    public void testArrayByteListWithExistingCapacity() {
        final ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        assertEquals(2, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertArrayEquals(new byte[]{(byte) 0, (byte) 1}, list._data);
    }

    @Test
    public void testArrayByteListWithExistingElements() {
        final ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(4, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertArrayEquals(new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3}, list._data);

        list.set(1, (byte) 4);
        assertEquals((byte) 4, list.get(1));
        assertArrayEquals(new byte[]{(byte) 0, (byte) 4, (byte) 2, (byte) 3}, list._data);

        list.add(0, (byte) 5);
        assertEquals(5, list.size());
        assertEquals((byte) 5, list.get(0));
        assertEquals((byte) 4, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertArrayEquals(new byte[]{(byte) 5, (byte) 4, (byte) 2, (byte) 3}, list._data);

        list.removeElementAt(0);
        assertEquals(4, list.size());
        assertEquals((byte) 4, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertArrayEquals(new byte[]{(byte) 4, (byte) 2, (byte) 3}, list._data);

        list.removeElementAt(1);
        assertEquals(3, list.size());
        assertEquals((byte) 4, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertArrayEquals(new byte[]{(byte) 4, (byte) 3}, list._data);
    }

    @Test
    public void testArrayByteListWithNegativeInitialCapacity() {
        try {
            new ArrayByteList(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testArrayByteListWithNullCollection() {
        try {
            new ArrayByteList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testArrayByteListWithExistingCapacityAndAddingElements() {
        final ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.ensureCapacity(10);
        assertEquals(4, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertArrayEquals(new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3}, list._data);

        list.add(4, (byte) 4);
        assertEquals(5, list.size());
        assertEquals((byte) 4, list.get(4));
        assertArrayEquals(new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4}, list._data);

        list.add(0, (byte) 5);
        assertEquals(6, list.size());
        assertEquals((byte) 5, list.get(0));
        assertEquals((byte) 4, list.get(1));
        assertArrayEquals(new byte[]{(byte) 5, (byte) 4, (byte) 2, (byte) 3, (byte) 4}, list._data);

        list.trimToSize();
        assertEquals(6, list.size());
        assertEquals((byte) 5, list.get(0));
        assertEquals((byte) 4, list.get(1));
        assertArrayEquals(new byte[]{(byte) 5, (byte) 4, (byte) 2, (byte) 3, (byte) 4}, list._data);
    }

    @Test
    public void testArrayByteListWithExistingCapacityAndRemovingElements() {
        final ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        final byte[] data = list._data.clone();
        list.removeElementAt(2);
        assertEquals(3, list.size());
        assertFalse(Arrays.equals(data, list._data));
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertArrayEquals(new byte[]{(byte) 0, (byte) 1}, list._data);

        list.removeElementAt(0);
        assertEquals(2, list.size());
        assertFalse(Arrays.equals(data, list._data));
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertArrayEquals(new byte[]{(byte) 1, (byte) 3}, list._data);

        list.removeElementAt(1);
        assertEquals(1, list.size());
        assertFalse(Arrays.equals(data, list._data));
        assertEquals((byte) 3, list.get(0));
        assertArrayEquals(new byte[]{(byte) 3}, list._data);

        list.trimToSize();
        assertEquals(1, list.size());
        assertSame(list._data, data);
    }

    @Test
    public void testSerializableArrayByteList() throws IOException, ClassNotFoundException {
        final ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(list);
        objStream.close();

        final ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        final ArrayByteList deserializedList = (ArrayByteList) objInput.readObject();
        objInput.close();

        assertEquals(4, deserializedList.size());
        assertEquals((byte) 0, deserializedList.get(0));
        assertEquals((byte) 1, deserializedList.get(1));
        assertEquals((byte) 2, deserializedList.get(2));
        assertEquals((byte) 3, deserializedList.get(3));
        assertArrayEquals(new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3}, deserializedList._data);
    }
}