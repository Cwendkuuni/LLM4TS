package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertNotNull(list._data);
        assertEquals(5, list._data.length);
    }

    @Test
    public void testConstructorFromByteCollection() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(1);
        out.write(2);
        out.write(3);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        ArrayByteList list = new ArrayByteList(new ByteArrayInputStream(input));
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));

        ArrayByteList list2 = new ArrayByteList(new ByteArrayInputStream(input));
        assertSame(list, list2);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
        assertEquals(5, list.get(4));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list.add((byte) 1);
        assertEquals(1, list.size());

        list.add((byte) 2);
        assertEquals(2, list.size());

        list.add((byte) 3);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);

        byte oldVal = list.removeElementAt(2);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(4, list.get(2));
        assertEquals(5, list.get(3));
        assertNull(oldVal);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);

        list.set(0, (byte) 6);
        assertEquals(5, list.size());
        assertEquals(6, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
        assertEquals(5, list.get(4));
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
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
        assertEquals(4, list.get(4));
        assertEquals(5, list.get(5));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.ensureCapacity(10);
        assertEquals(5, list.size());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5 }, list._data, 0, 5);
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
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, list._data, 0, 5);
    }

    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList(5);

        assertDoesNotThrow(() -> list.checkRange(0));
        assertDoesNotThrow(() -> list.checkRange(1));
        assertDoesNotThrow(() -> list.checkRange(2));
        assertDoesNotThrow(() -> list.checkRange(3));
        assertDoesNotThrow(() -> list.checkRange(4));
        assertDoesNotThrow(() -> list.checkRange(5));

        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(6));
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList(5);

        assertDoesNotThrow(() -> list.checkRangeIncludingEndpoint(0));
        assertDoesNotThrow(() -> list.checkRangeIncludingEndpoint(1));
        assertDoesNotThrow(() -> list.checkRangeIncludingEndpoint(2));
        assertDoesNotThrow(() -> list.checkRangeIncludingEndpoint(3));
        assertDoesNotThrow(() -> list.checkRangeIncludingEndpoint(4));
        assertDoesNotThrow(() -> list.checkRangeIncludingEndpoint(5));

        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(6));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(list);
        os.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ArrayByteList list2 = (ArrayByteList) new ObjectInputStream(in).readObject();

        assertEquals(5, list2.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(list.get(i), list2.get(i));
        }
    }
}