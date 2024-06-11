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

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListWithNegativeInitialCapacity() {
        new ArrayByteList(-5);
    }

    @Test
    public void testArrayByteListWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        try {
            list.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list.add((byte) 1);
        assertEquals(1, list.size());

        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(2, list.size());
        assertEquals((byte) 2, list.get(0));

        assertEquals((byte) 2, list.removeElementAt(1));
        assertEquals(1, list.size());
        assertNull(list.get(0));

        try {
            list.removeElementAt(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.removeElementAt(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 1, list.set(0, (byte) 4));
        assertEquals(4, list.size());
        assertEquals((byte) 4, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));

        try {
            list.set(-1, (byte) 5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.set(3, (byte) 5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();

        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add(0, (byte) 0);

        assertEquals(4, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));

        try {
            list.add(-1, (byte) 5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.add(5, (byte) 5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.ensureCapacity(10);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[10], list._data);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.trimToSize();
        assertEquals(3, list.size());
        assertArrayEquals(new byte[3], list._data);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));

        list.add((byte) 4);
        list.trimToSize();
        assertEquals(4, list.size());
        assertArrayEquals(new byte[4], list._data);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        in.close();

        byteInput.close();
        byteStream.close();

        assertEquals(2, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
    }

    @Test
    public void testIndexOutOfBoundsExceptionMessage() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        try {
            list.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 3, found -1", e.getMessage());
        }

        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 3, found 3", e.getMessage());
        }
    }
}