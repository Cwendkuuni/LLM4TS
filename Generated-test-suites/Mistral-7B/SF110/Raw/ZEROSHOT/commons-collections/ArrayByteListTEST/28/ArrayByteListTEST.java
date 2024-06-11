package org.apache.commons.collections.primitives;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ArrayByteListTest implements Serializable {

    private ArrayByteList arrayByteList;

    @Before
    public void setup() {
        arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
    }

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list._data);

        ArrayByteList listWithInitialCapacity = new ArrayByteList(5);
        assertEquals(0, listWithInitialCapacity.size());
        assertNotNull(listWithInitialCapacity._data);
        assertArrayEquals(new byte[5], listWithInitialCapacity._data);
    }

    @Test
    public void testConstructorWithCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(byteCollection);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[]{1, 2, 3}, list._data);
    }

    @Test
    public void testGet() {
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        arrayByteList.get(3);
    }

    @Test
    public void testSize() {
        assertEquals(3, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        byte removedElement = arrayByteList.removeElementAt(1);
        assertEquals((byte) 2, removedElement);
        assertEquals(2, arrayByteList.size());
        assertArrayEquals(new byte[]{1, 3}, arrayByteList._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        arrayByteList.removeElementAt(3);
    }

    @Test
    public void testSet() {
        byte newElement = (byte) 4;
        arrayByteList.set(0, newElement);
        assertEquals(newElement, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        arrayByteList.set(3, (byte) 5);
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 0);
        assertEquals(4, arrayByteList.size());
        assertArrayEquals(new byte[]{0, 1, 3}, arrayByteList._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        arrayByteList.add(4, (byte) 5);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.trimToSize();
        assertEquals(0, list._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(arrayByteList);

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();

        assertEquals(3, deserializedList.size());
        assertArrayEquals(arrayByteList._data, deserializedList._data);
    }
}