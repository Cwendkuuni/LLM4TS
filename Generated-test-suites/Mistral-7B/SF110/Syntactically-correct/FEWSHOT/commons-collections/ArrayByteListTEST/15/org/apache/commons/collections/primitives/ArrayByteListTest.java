package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list._data.length);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(1);
        out.write(2);
        out.write(3);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ArrayByteList list = new ArrayByteList(new ByteArrayInputStream(in.readAllBytes()));
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        byte removed = list.removeElementAt(1);
        assertEquals((byte) 2, removed);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.set(0, (byte) 4);
        assertEquals((byte) 4, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add(4, (byte) 4);
        assertEquals(4, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
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
        list.ensureCapacity(11);
        assertEquals(7, list.size());
        assertArrayEquals(new byte[] {1, 2, 3, 4, 5, 6, 7}, list.toByteArray());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.trimToSize();
        assertEquals(5, list.size());
    }

    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList(5);
        assertFalse(list.checkRange(-1));
        assertFalse(list.checkRange(5));
        assertTrue(list.checkRange(0));
        assertTrue(list.checkRange(4));
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList(5);
        assertFalse(list.checkRangeIncludingEndpoint(-1));
        assertFalse(list.checkRangeIncludingEndpoint(5));
        assertTrue(list.checkRangeIncludingEndpoint(0));
        assertTrue(list.checkRangeIncludingEndpoint(4));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(new byte[] {1, 2, 3, 4, 5});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(list);
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) is.readObject();
        assertEquals(5, deserializedList.size());
        assertArrayEquals(new byte[] {1, 2, 3, 4, 5}, deserializedList.toByteArray());
    }
}