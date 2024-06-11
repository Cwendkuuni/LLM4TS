package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        assertEquals(1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(5);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals((byte) 3, list.removeElementAt(2));
        assertEquals(2, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.removeElementAt(3);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals((byte) 3, list.set(2, (byte) 4));
        assertEquals((byte) 4, list.get(2));
        assertEquals(3, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(3, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add(0, (byte) 0);
        assertEquals(4, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
    }

    @Test
    public void testAddWithNullElement() {
        ArrayByteList list = new ArrayByteList();
        try {
            list.add(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(5);
        assertEquals(5, list.size());
        list.ensureCapacity(2);
        assertEquals(5, list.size());
        list.add((byte) 1);
        list.ensureCapacity(1);
        assertEquals(6, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.trimToSize();
        assertEquals(5, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(list);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        assertEquals(2, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
    }
}