package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityConstructor() {
        new ArrayByteList(-1);
    }

    @Test
    public void testInitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list.capacity());
    }

    @Test
    public void testByteCollectionConstructor() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("hello".getBytes());
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ByteArrayInputStream input = new ByteArrayInputStream("size=5[B".getBytes());
        ByteList expected = new ArrayByteList(new ByteArrayInputStream(input.readAllBytes()));
        ArrayByteList list = new ArrayByteList(new ByteArrayInputStream(in.readAllBytes()));
        assertEquals(expected, list);
        assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            assertEquals((byte) "hello".charAt(i), list.get(i));
        }
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 'h');
        list.set(1, (byte) 'e');
        list.set(2, (byte) 'l');
        list.set(3, (byte) 'l');
        list.set(4, (byte) 'o');
        assertEquals((byte) 'h', list.get(0));
        assertEquals((byte) 'e', list.get(1));
        assertEquals((byte) 'l', list.get(2));
        assertEquals((byte) 'l', list.get(3));
        assertEquals((byte) 'o', list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 'h');
        list.set(1, (byte) 'e');
        list.set(2, (byte) 'l');
        list.set(3, (byte) 'l');
        list.set(4, (byte) 'o');
        list.get(-1);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 0);
        assertEquals(1, list.size());
        list.add((byte) 1);
        assertEquals(2, list.size());
        list.add((byte) 2);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 'h');
        list.set(1, (byte) 'e');
        list.set(2, (byte) 'l');
        list.set(3, (byte) 'l');
        list.set(4, (byte) 'o');
        assertEquals((byte) 'h', list.removeElementAt(0));
        assertEquals((byte) 'e', list.get(0));
        assertEquals((byte) 'l', list.get(1));
        assertEquals((byte) 'l', list.get(2));
        assertEquals((byte) 'o', list.get(3));
        assertEquals(4, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 'h');
        list.set(1, (byte) 'e');
        list.set(2, (byte) 'l');
        list.set(3, (byte) 'l');
        list.set(4, (byte) 'o');
        list.removeElementAt(5);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 'h');
        list.set(1, (byte) 'e');
        list.set(2, (byte) 'l');
        list.set(3, (byte) 'l');
        list.set(4, (byte) 'o');
        list.set(0, (byte) 'H');
        assertEquals((byte) 'H', list.get(0));
        assertEquals((byte) 'e', list.get(1));
        assertEquals((byte) 'l', list.get(2));
        assertEquals((byte) 'l', list.get(3));
        assertEquals((byte) 'o', list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 'h');
        list.set(1, (byte) 'e');
        list.set(2, (byte) 'l');
        list.set(3, (byte) 'l');
        list.set(4, (byte) 'o');
        list.set(5, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add(5, (byte) 5);
        list.add(0, (byte) 6);
        assertEquals(6, list.get(0));
        assertEquals(5, list.get(5));
        assertEquals(6, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add(6, (byte) 5);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.ensureCapacity(10);
        assertEquals(10, list.capacity());
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.ensureCapacity(10);
        assertEquals(11, list.capacity());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.ensureCapacity(10);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.trimToSize();
        assertEquals(10, list.capacity());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(list);

        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objectIn = new ObjectInputStream(input);
        ArrayByteList deserializedList = (ArrayByteList) objectIn.readObject();

        assertEquals(list, deserializedList);
        for (int i = 0; i < 5; i++) {
            assertEquals(list.get(i), deserializedList.get(i));
        }
    }
}