package org.apache.commons.collections.primitives.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * Test cases for ArrayByteList class.
 */
public class ArrayByteListTest {

    /**
     * Test constructor with no arguments.
     */
    @org.junit.Test
    public void testConstructorNoArgs() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list._data);
    }

    /**
     * Test constructor with initialCapacity.
     */
    @org.junit.Test
    public void testConstructorInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertArrayEquals(new byte[5], list._data);
    }

    /**
     * Test constructor with ByteCollection.
     */
    @org.junit.Test
    public void testConstructorByteCollection() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(10);
        output.write(20);
        output.write(30);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());

        ArrayByteList list = new ArrayByteList(input);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[]{10, 20, 30}, list._data);
    }

    /**
     * Test get method.
     */
    @org.junit.Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 10);
        list.set(1, (byte) 20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        try {
            list.get(-1);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            list.get(5);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test size method.
     */
    @org.junit.Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    /**
     * Test removeElementAt method.
     */
    @org.junit.Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 10);
        list.set(1, (byte) 20);
        list.set(2, (byte) 30);
        byte removed = list.removeElementAt(1);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[]{10, 30}, list._data);
        assertEquals(20, removed);
        try {
            list.removeElementAt(-1);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            list.removeElementAt(4);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test set method.
     */
    @org.junit.Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 10);
        list.set(1, (byte) 20);
        list.set(2, (byte) 30);
        byte oldVal = list.set(1, (byte) 21);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[]{10, 21, 30}, list._data);
        assertEquals(20, oldVal);
        try {
            list.set(-1, (byte) 1);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            list.set(5, (byte) 1);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test add method.
     */
    @org.junit.Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add(0, (byte) 0);
        list.add(4, (byte) 4);
        assertEquals(5, list.size());
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, list._data);
        try {
            list.add(-1, (byte) 1);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            list.add(6, (byte) 1);
            fail("IndexOutOfBoundsException should be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test ensureCapacity method.
     */
    @org.junit.Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
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
        list.add((byte) 10);
        assertEquals(11, list.size());
        list.ensureCapacity(20);
        assertEquals(21, list._data.length);
    }

    /**
     * Test trimToSize method.
     */
    @org.junit.Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        for (int i = 0; i < 5; i++) {
            list.add((byte) i);
        }
        list.trimToSize();
        assertEquals(5, list.size());
        assertEquals(5, list._data.length);
    }

    /**
     * Test serialization and deserialization.
     */
    @org.junit.Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(output);
        out.writeObject(list);

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();

        assertEquals(3, deserializedList.size());
        assertArrayEquals(new byte[]{1, 2, 3}, deserializedList._data);
    }

    /**
     * Test constructor with negative initialCapacity.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    /**
     * Test index out of bounds exception with negative index.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.get(-1);
    }

    /**
     * Test index out of bounds exception with index greater than size.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.get(5);
    }

    /**
     * Test index out of bounds exception with negative index in set method.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(-1, (byte) 1);
    }

    /**
     * Test index out of bounds exception with index greater than size in set method.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(6, (byte) 1);
    }

    /**
     * Test index out of bounds exception with negative index in add method.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(-1, (byte) 1);
    }

    /**
     * Test index out of bounds exception with index greater than size in add method.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(6, (byte) 1);
    }

    /**
     * Test index out of bounds exception with negative index in removeElementAt method.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(-1);
    }

    /**
     * Test index out of bounds exception with index greater than size in removeElementAt method.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(6);
    }
}