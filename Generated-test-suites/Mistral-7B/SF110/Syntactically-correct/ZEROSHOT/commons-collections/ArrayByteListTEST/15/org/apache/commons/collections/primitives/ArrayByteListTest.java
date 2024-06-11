package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.collections.primitives.ArrayByteList;

/**
 * Test class for ArrayByteList.
 *
 * @author John Doe
 */
public class ArrayByteListTest {

    /**
     * Test constructor with no arguments.
     */
    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
        assertArrayEquals(new byte[0], list.toByteArray());
    }

    /**
     * Test constructor with an initial capacity.
     */
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertNotNull(list);
        assertEquals(0, list.size());
        assertArrayEquals(new byte[5], list.toByteArray());
    }

    /**
     * Test constructor from a ByteCollection.
     */
    @Test
    public void testConstructorFromByteCollection() {
        byte[] sourceData = new byte[] { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList(new ByteArray(sourceData));
        assertNotNull(list);
        assertEquals(5, list.size());
        assertArrayEquals(sourceData, list.toByteArray());
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    /**
     * Test size method.
     */
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
    }

    /**
     * Test removeElementAt method.
     */
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte removed = list.removeElementAt(2);
        assertEquals((byte) 3, removed);
        assertEquals(4, list.size());
        assertArrayEquals(new byte[] { 1, 2, 4, 5 }, list.toByteArray());
    }

    /**
     * Test set method.
     */
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        list.set(0, (byte) 6);
        assertEquals((byte) 6, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    /**
     * Test add method.
     */
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(0, (byte) 0);
        assertEquals(6, list.size());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5 }, list.toByteArray());
    }

    /**
     * Test ensureCapacity method.
     */
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(4);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
    }

    /**
     * Test trimToSize method.
     */
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();
        assertEquals(3, list.size());
        assertEquals(3, list._data.length);
    }

    /**
     * Test writeObject method.
     */
    @Test
    public void testWriteObject() throws IOException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeObject(list);
        output.close();
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) input.readObject();
        input.close();
        assertEquals(5, deserializedList.size());
        assertArrayEquals(list.toByteArray(), deserializedList.toByteArray());
    }

    /**
     * Test readObject method.
     */
    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        byte[] sourceData = new byte[] { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList(5);
        list.addAll(Arrays.asList(sourceData));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeObject(list);
        output.close();
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) input.readObject();
        input.close();
        assertEquals(5, deserializedList.size());
        assertArrayEquals(sourceData, deserializedList.toByteArray());
    }

    /**
     * Test checkRange method.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(-1);
    }

    /**
     * Test checkRange method.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeEndIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRange(1);
    }

    /**
     * Test checkRange method.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeLargeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRange(100);
    }

    /**
     * Test checkRangeIncludingEndpoint method.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(-1);
    }

    /**
     * Test checkRangeIncludingEndpoint method.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointEndIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRangeIncludingEndpoint(0);
    }

    /**
     * Test checkRangeIncludingEndpoint method.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointLargeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRangeIncludingEndpoint(100);
    }

    /**
     * Test IllegalArgumentException with negative capacity.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        new ArrayByteList(-1);
    }
}