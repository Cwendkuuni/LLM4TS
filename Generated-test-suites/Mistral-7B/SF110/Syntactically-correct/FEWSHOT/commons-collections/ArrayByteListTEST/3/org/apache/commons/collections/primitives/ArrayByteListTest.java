package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test
    public void testConstructorWithNegativeInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-1));
    }

    @Test
    public void testConstructorFromByteCollection() {
        ArrayByteList source = new ArrayByteList();
        source.add((byte) 1);
        source.add((byte) 2);
        source.add((byte) 3);
        ArrayByteList target = new ArrayByteList(source);
        assertEquals(source.size(), target.size());
        assertArrayEquals(source.toByteArray(), target.toByteArray());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals((byte) 2, list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(0, (byte) 2);
        assertEquals((byte) 2, list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add(0, (byte) 0);
        assertEquals(3, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.ensureCapacity(10);
        assertEquals(6, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.trimToSize();
        assertEquals(2, list.size());
    }

    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(3));
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(3));
        assertTrue(list.checkRangeIncludingEndpoint(0));
        assertTrue(list.checkRangeIncludingEndpoint(1));
        assertTrue(list.checkRangeIncludingEndpoint(2));
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
        ArrayByteList deserializedList = (ArrayByteList) objIn.readObject();
        assertEquals(list.size(), deserializedList.size());
        assertArrayEquals(list.toByteArray(), deserializedList.toByteArray());
    }
}