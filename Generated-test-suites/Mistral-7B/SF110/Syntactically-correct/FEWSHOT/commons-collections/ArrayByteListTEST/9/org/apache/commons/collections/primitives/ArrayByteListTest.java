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

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructorFromByteCollection() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Hello World".getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteArrayInputStream inputStreamBackup = new ByteArrayInputStream(outputStream.toByteArray());
        ByteArrayInputStream inputStreamWithWrongSize = new ByteArrayInputStream("Hello World".getBytes());

        ArrayByteList list = new ArrayByteList(inputStream);
        assertEquals(13, list.size());
        for (int i = 0; i < 13; i++) {
            assertEquals(i, list.get(i));
        }

        ArrayByteList listFromBackup = new ArrayByteList(inputStreamBackup);
        assertEquals(13, listFromBackup.size());
        for (int i = 0; i < 13; i++) {
            assertEquals(i, listFromBackup.get(i));
        }

        try {
            new ArrayByteList(inputStreamWithWrongSize);
            fail("Should throw an IllegalArgumentException because the size of the input stream is not correct.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
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
            list.get(5);
            fail("Should throw an IndexOutOfBoundsException because the index is out of bounds.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
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
            fail("Should throw an IndexOutOfBoundsException because the index is negative.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            list.removeElementAt(5);
            fail("Should throw an IndexOutOfBoundsException because the index is out of bounds.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
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
            fail("Should throw an IndexOutOfBoundsException because the index is negative.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            list.set(5, (byte) 7);
            fail("Should throw an IndexOutOfBoundsException because the index is out of bounds.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
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
            fail("Should throw an IndexOutOfBoundsException because the index is negative.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            list.add(7, (byte) 7);
            fail("Should throw an IndexOutOfBoundsException because the index is out of bounds.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
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

        assertEquals(10, list.size());
        list.ensureCapacity(11);
        assertEquals(12, list._data.length);
        for (int i = 0; i < 12; i++) {
            assertNull(list.get(i));
        }

        list.add(11, (byte) 11);
        assertEquals(12, list.size());
        assertEquals((byte) 11, list.get(11));
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

        assertEquals(10, list.size());
        list.trimToSize();
        assertEquals(10, list.size());

        for (int i = 0; i < 10; i++) {
            assertEquals(i, list.get(i));
        }

        list.trimToSize();
        assertEquals(10, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArrayByteList deserializedList = new ArrayByteList(inputStream);

        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, deserializedList.get(i));
        }
        assertEquals(5, deserializedList.size());
    }

    @Test
    public void testSerializationWithWrongSize() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeInt(11);
        for (int i = 0; i < 5; i++) {
            objectOutputStream.writeByte(i + 1);
        }
        objectOutputStream.writeBytes("Hello World".getBytes());
        objectOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        try {
            new ArrayByteList(inputStream);
            fail("Should throw an IllegalArgumentException because the size of the input stream is not correct.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}