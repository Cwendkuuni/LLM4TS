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
        assertEquals(5, list._data.length);
    }

    @Test
    public void testArrayByteListWithByteCollection() {
        ByteCollection collection = new ArrayByteArray(new byte[]{1, 2, 3, 4, 5});
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, list.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListNegativeInitialCapacity() {
        new ArrayByteList(-5);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));

        try {
            list.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.get(5);
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
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());

        list.add((byte) 6);
        assertEquals(6, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(4, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertEquals((byte) 4, list.get(2));
        assertEquals((byte) 5, list.get(3));

        try {
            list.removeElementAt(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.removeElementAt(5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.set(0, (byte) 6);
        assertEquals((byte) 6, list.get(0));
        assertEquals(5, list.size());

        try {
            list.set(-1, (byte) 7);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.set(5, (byte) 7);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.add(0, (byte) 0);
        assertEquals(6, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertEquals((byte) 4, list.get(4));
        assertEquals((byte) 5, list.get(5));

        list.add(6, (byte) 6);
        assertEquals(7, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertEquals((byte) 4, list.get(4));
        assertEquals((byte) 5, list.get(5));
        assertEquals((byte) 6, list.get(6));

        try {
            list.add(-1, (byte) 7);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            list.add(7, (byte) 7);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);

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

        list.ensureCapacity(15);
        assertEquals(15, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);

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

        list.trimToSize();
        assertEquals(10, list._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(list);

        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        ArrayByteList list2 = (ArrayByteList) objIn.readObject();

        assertEquals(3, list2.size());
        assertEquals((byte) 1, list2.get(0));
        assertEquals((byte) 2, list2.get(1));
        assertEquals((byte) 3, list2.get(2));
    }

}