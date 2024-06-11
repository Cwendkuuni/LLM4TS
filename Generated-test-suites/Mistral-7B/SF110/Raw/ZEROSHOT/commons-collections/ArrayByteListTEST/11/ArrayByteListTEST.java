package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;

/**
 * Tests for ArrayByteList class
 *
 * @author John Doe
 */
public class ArrayByteListTest implements Serializable {

    private static final byte[] TEST_DATA = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    @Test
    public void testEmptyListConstruction() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list._data);
    }

    @Test
    public void testInitialCapacityConstruction() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertEquals(5, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityConstruction() {
        new ArrayByteList(-1);
    }

    @Test
    public void testCollectionConstruction() {
        ArrayByteList list = new ArrayByteList(Arrays.asList(TEST_DATA));
        assertEquals(TEST_DATA.length, list.size());
        assertArrayEquals(TEST_DATA, list.toByteArray());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        for (int i = 0; i < TEST_DATA.length; ++i) {
            assertEquals(TEST_DATA[i], list.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        list.get(-1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        byte newValue = 10;
        list.set(0, newValue);
        assertNotSame(TEST_DATA[0], list.get(0));
        assertEquals(newValue, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        list.set(-1, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        byte newValue = 10;
        list.add((short) 0, newValue);
        assertEquals(1, list.size());
        assertEquals(newValue, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        list.add(-1, (byte) 1);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        byte[] newData = { 11, 12, 13, 14 };
        list.addAll(Arrays.asList(newData));
        assertEquals(TEST_DATA.length + newData.length, list.size());
        assertArrayEquals(Arrays.concat(TEST_DATA, newData), list.toByteArray());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        byte removedElement = list.removeElementAt(0);
        assertEquals(TEST_DATA.length - 1, list.size());
        assertNotSame(TEST_DATA[0], removedElement);
        assertEquals(TEST_DATA[0], list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        list.removeElementAt(-1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.addAll(Arrays.asList(TEST_DATA));
        list.ensureCapacity(TEST_DATA.length * 2);
        assertEquals(TEST_DATA.length * 2, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.addAll(Arrays.asList(TEST_DATA));
        list.trimToSize();
        assertEquals(TEST_DATA.length, list._data.length);
    }

    @Test
    public void testSerialization() throws Exception {
        ArrayByteList list = new ArrayByteList(TEST_DATA);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        assertArrayEquals(TEST_DATA, deserializedList.toByteArray());
    }
}