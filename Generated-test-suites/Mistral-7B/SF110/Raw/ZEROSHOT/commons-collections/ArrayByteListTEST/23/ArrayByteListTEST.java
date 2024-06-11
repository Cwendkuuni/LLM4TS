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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;

/**
 * Tests for ArrayByteList class
 *
 * @author John Doe
 */
public class ArrayByteListTest {

    private byte[] testData = new byte[] { 1, 2, 3, 4, 5 };

    @Test
    public void testConstructorWithNoArguments() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertNotNull(list);
        assertEquals(0, list.size());
        assertArrayEquals(new byte[5], list._data);
    }

    @Test
    public void testConstructorWithByteCollectionThat() {
        ByteCollection byteCollection = Collections.singletonList((byte) 1);
        ArrayByteList list = new ArrayByteList(byteCollection);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(testData);
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], list.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(testData);
        list.get(-1);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(testData);
        assertEquals(testData.length, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(testData);
        byte removedElement = list.removeElementAt(0);
        assertEquals(testData[1], removedElement);
        assertEquals(testData.length - 1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(testData);
        list.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(testData);
        byte newElement = (byte) 6;
        list.set(0, newElement);
        byte oldElement = testData[0];
        assertEquals(newElement, list.get(0));
        assertArrayEquals(new byte[] { oldElement, testData[1], testData[2], testData[3], testData[4] }, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(testData);
        list.set(-1, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        byte newElement = (byte) 6;
        list.add(newElement);
        assertEquals(1, list.size());
        assertEquals(newElement, list.get(0));
    }

    @Test
    public void testAddWithValidIndex() {
        ArrayByteList list = new ArrayByteList(testData);
        byte newElement = (byte) 6;
        list.add(2, newElement);
        assertEquals(testData.length + 1, list.size());
        assertArrayEquals(Arrays.copyOfRange(testData, 0, 2), Arrays.copyOfRange(list._data, 0, 2));
        assertEquals(newElement, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(testData);
        list.add(-1, (byte) 1);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list = new ArrayByteList();
        byte[] newData = new byte[] { 7, 8, 9 };
        list.addAll(Arrays.asList(newData));
        assertEquals(testData.length + newData.length, list.size());
        assertArrayEquals(Arrays.copyOfRange(testData, 0, testData.length), Arrays.copyOfRange(list._data, 0, testData.length));
        assertArrayEquals(newData, Arrays.copyOfRange(list._data, testData.length, testData.length + newData.length));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(testData);
        list.ensureCapacity(testData.length * 2);
        assertArrayEquals(testData, list._data);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.addAll(Arrays.asList(testData));
        list.trimToSize();
        assertArrayEquals(testData, list._data);
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ArrayByteList list = new ArrayByteList(testData);
        objectOutputStream.writeObject(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        assertArrayEquals(testData, deserializedList._data);
        assertEquals(testData.length, deserializedList.size());
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ArrayByteList list = new ArrayByteList(testData);
        objectOutputStream.writeObject(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        assertArrayEquals(testData, deserializedList._data);
        assertEquals(testData.length, deserializedList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList(testData);
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(testData);
        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(testData);
        list.checkRangeIncludingEndpoint(testData.length + 1);
    }
}