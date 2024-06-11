package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;

/**
 * Test class for {@link ArrayByteList}.
 *
 * @author John Doe
 */
public class ArrayByteListTest {

    /**
     * Test constructor with no arguments.
     */
    @Test
    public void testEmptyConstuctor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    /**
     * Test constructor with initial capacity.
     */
    @Test
    public void testInitialCapacityConstuctor() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertEquals(5, list.capacity());
    }

    /**
     * Test constructor from {@link ByteCollection}.
     */
    @Test
    public void testByteCollectionConstructor() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
        byte[] expectedData = new byte[]{(byte) 1, (byte) 2};
        assertArrayEquals(expectedData, list.toByteArray());
    }

    /**
     * Test getting an element.
     */
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        byte expected = (byte) 1;
        byte actual = list.get(0);
        assertEquals(expected, actual);
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        int expected = 3;
        int actual = list.size();
        assertEquals(expected, actual);
    }

    /**
     * Test removing an element.
     */
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte expected = (byte) 2;
        byte actual = list.removeElementAt(1);
        assertEquals(expected, actual);
        int size = list.size();
        assertEquals(2, size);
    }

    /**
     * Test setting an element.
     */
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        byte newValue = (byte) 3;
        list.set(0, newValue);

        byte expectedValue = newValue;
        byte actualValue = list.get(0);
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test adding an element.
     */
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();

        byte newElement = (byte) 1;
        list.add(newElement);
        int size = list.size();
        assertEquals(1, size);

        byte[] expectedData = new byte[]{(byte) 1};
        assertArrayEquals(expectedData, list.toByteArray());
    }

    /**
     * Test adding multiple elements.
     */
    @Test
    public void testAddMultiple() {
        ArrayByteList list = new ArrayByteList();

        byte[] newElements = new byte[]{(byte) 1, (byte) 2, (byte) 3};
        for (byte newElement : newElements) {
            list.add(newElement);
        }
        int size = list.size();
        assertEquals(3, size);

        byte[] expectedData = new byte[]{
            (byte) 1,
            (byte) 2,
            (byte) 3
        };
        assertArrayEquals(expectedData, list.toByteArray());
    }

    /**
     * Test ensuring capacity.
     */
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();

        for (int i = 0; i < 100; i++) {
            list.add((byte) i);
        }

        list.ensureCapacity(150);
        int capacity = list.capacity();
        assertTrue(capacity >= 150);
    }

    /**
     * Test trimming to size.
     */
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.trimToSize();
        int size = list.size();
        assertEquals(3, size);

        byte[] expectedData = new byte[]{(byte) 1, (byte) 2, (byte) 3};
        assertArrayEquals(expectedData, list.toByteArray());
    }

    /**
     * Test serialization.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        objectInputStream.close();

        int deserializedSize = deserializedList.size();
        assertEquals(2, deserializedSize);

        byte[] expectedData = new byte[]{(byte) 1, (byte) 2};
        assertArrayEquals(expectedData, deserializedList.toByteArray());
    }

    /**
     * Test checking range.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(-1);
    }

    /**
     * Test checking range including endpoint.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(-1);
    }

    /**
     * Test checking range.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeBiggerIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRange(2);
    }

    /**
     * Test checking range including endpoint.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointBiggerIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRangeIncludingEndpoint(2);
    }
}