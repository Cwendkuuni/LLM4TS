package org.apache.commons.collections.primitives.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest implements Serializable {

    private static final int DEFAULT_CAPACITY = 8;
    private static final int INITIAL_CAPACITY = 16;
    private static final int INDEX_OUT_OF_BOUNDS = -1;
    private static final int NEGATIVE_INDEX = -1;
    private static final int INDEX_BEYOND_BOUNDS = 10;
    private static final byte[] DATA = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    private static final byte ELEMENT = 11;
    private static final int SIZE = 11;
    private static final int MIN_CAPACITY = 20;

    private ArrayByteList arrayByteList;
    private ArrayByteList copyOfArrayByteList;
    private ByteArrayInputStream byteArrayInputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
        copyOfArrayByteList = new ArrayByteList(INITIAL_CAPACITY);

        byteArrayInputStream = new ByteArrayInputStream(DATA);
        byteArrayOutputStream = new ByteArrayOutputStream();
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Arrays.copyOfRange(DATA, 0, SIZE)));
    }

    @After
    public void tearDown() throws IOException {
        byteArrayInputStream.close();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        objectInputStream.close();
    }

    @Test
    public void testArrayByteListConstructor() {
        assertNotNull(arrayByteList);
        assertNull(arrayByteList._data);
        assertEquals(0, arrayByteList._size);
    }

    @Test
    public void testArrayByteListInitialCapacityConstructor() {
        assertNotNull(copyOfArrayByteList);
        assertNull(copyOfArrayByteList._data);
        assertEquals(0, copyOfArrayByteList._size);
        assertEquals(INITIAL_CAPACITY, copyOfArrayByteList._data.length);
    }

    @Test
    public void testArrayByteListByteCollectionConstructor() {
        final ArrayByteList arrayByteList = new ArrayByteList((ByteCollection) () -> DATA);
        assertNotNull(arrayByteList);
        assertNotNull(arrayByteList._data);
        assertEquals(SIZE, arrayByteList._size);
        assertArrayEquals(DATA, arrayByteList._data);
    }

    @Test
    public void testGet() {
        arrayByteList.addAll(Arrays.asList(ELEMENT));
        assertEquals(ELEMENT, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBoundsException() {
        arrayByteList.checkRange(INDEX_OUT_OF_BOUNDS);
    }

    @Test
    public void testSize() {
        arrayByteList.addAll(Arrays.asList(ELEMENT));
        assertEquals(1, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.addAll(Arrays.asList(ELEMENT, ELEMENT));
        assertEquals(ELEMENT, arrayByteList.removeElementAt(0));
        assertEquals(1, arrayByteList.size());
        assertEquals(ELEMENT, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBoundsException() {
        arrayByteList.checkRange(INDEX_BEYOND_BOUNDS);
    }

    @Test
    public void testSet() {
        arrayByteList.addAll(Arrays.asList(ELEMENT, ELEMENT));
        arrayByteList.set(0, (byte) 12);
        assertEquals(12, arrayByteList.get(0));
        assertEquals(1, arrayByteList.size());
        assertEquals(ELEMENT, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexOutOfBoundsException() {
        arrayByteList.checkRange(INDEX_BEYOND_BOUNDS);
    }

    @Test
    public void testAdd() {
        arrayByteList.add(ELEMENT);
        assertEquals(1, arrayByteList.size());
        assertEquals(ELEMENT, arrayByteList.get(0));

        arrayByteList.add(2, ELEMENT);
        assertEquals(3, arrayByteList.size());
        assertEquals(ELEMENT, arrayByteList.get(1));
        assertEquals(ELEMENT, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBoundsException() {
        arrayByteList.checkRangeIncludingEndpoint(INDEX_BEYOND_BOUNDS);
    }

    @Test
    public void testAddAll() {
        arrayByteList.addAll(Arrays.asList(ELEMENT));
        assertEquals(2, arrayByteList.size());
        assertEquals(ELEMENT, arrayByteList.get(1));

        copyOfArrayByteList.addAll(Arrays.asList(ELEMENT, ELEMENT));
        arrayByteList.addAll(copyOfArrayByteList);
        assertEquals(6, arrayByteList.size());
        assertArrayEquals(DATA, arrayByteList._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialCapacityNegativeException() {
        arrayByteList = new ArrayByteList(-1);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.addAll(Arrays.asList(ELEMENT, ELEMENT, ELEMENT, ELEMENT));
        arrayByteList.ensureCapacity(MIN_CAPACITY);
        assertTrue(MIN_CAPACITY <= arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.addAll(Arrays.asList(ELEMENT, ELEMENT, ELEMENT, ELEMENT));
        arrayByteList.trimToSize();
        assertEquals(4, arrayByteList.size());
    }

    @Test
    public void testWriteObject() throws IOException {
        arrayByteList.addAll(Arrays.asList(ELEMENT));
        objectOutputStream.writeObject(arrayByteList);

        final ArrayByteList readArrayByteList = (ArrayByteList) objectInputStream.readObject();
        assertEquals(1, readArrayByteList.size());
        assertEquals(ELEMENT, readArrayByteList.get(0));
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        final ArrayByteList readArrayByteList = (ArrayByteList) objectInputStream.readObject();
        assertEquals(SIZE, readArrayByteList.size());
        assertArrayEquals(DATA, readArrayByteList._data);
    }
}