package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertArrayEquals(new byte[5], list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListNegativeInitialCapacity() {
        new ArrayByteList(-5);
    }

    @Test
    public void testArrayByteListFromByteCollection() {
        ByteArray exArray = new ByteArray(new byte[] { 1, 2, 3, 4, 5 });
        ArrayByteList list = new ArrayByteList(exArray);
        assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(exArray.get(i), list.get(i));
        }
    }

    @Test
    public void testGetMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, list.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(5);
    }

    @Test
    public void testSizeMethod() {
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
        assertEquals(5, list.size());
        byte removedElement = list.removeElementAt(2);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 4, list.get(2));
        assertEquals((byte) 5, list.get(3));
        assertEquals(removedElement, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(5);
    }

    @Test
    public void testSetMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.set(0, (byte) 2);
        assertEquals(2, list.get(0));
        assertEquals(1, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(-1, (byte) 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(5, (byte) 2);
    }

    @Test
    public void testAddMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
        list.add(0, (byte) 0);
        assertEquals(6, list.size());
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
        assertEquals(4, list.get(4));
        assertEquals(5, list.get(5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(-1, (byte) 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(6, (byte) 0);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList(5);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);
        list1.add((byte) 4);
        list1.add((byte) 5);
        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 6);
        list2.add((byte) 7);
        list2.add((byte) 8);
        list1.addAll(1, list2);
        assertEquals(8, list1.size());
        assertEquals(6, list1.get(1));
        assertEquals(7, list1.get(2));
        assertEquals(8, list1.get(3));
        assertEquals(1, list1.get(4));
        assertEquals(2, list1.get(5));
        assertEquals(3, list1.get(6));
        assertEquals(4, list1.get(7));
        assertEquals(5, list1.get(4));
        assertEquals(5, list1.get(5));
        assertEquals(0, list1.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllNegativeIndex() {
        ArrayByteList list1 = new ArrayByteList(5);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);
        list1.add((byte) 4);
        list1.add((byte) 5);
        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 6);
        list2.add((byte) 7);
        list2.add((byte) 8);
        list1.addAll(-1, list2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllIndexGreaterThanSize() {
        ArrayByteList list1 = new ArrayByteList(5);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);
        list1.add((byte) 4);
        list1.add((byte) 5);
        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 6);
        list2.add((byte) 7);
        list2.add((byte) 8);
        list1.addAll(10, list2);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.ensureCapacity(10);
        assertEquals(11, list._data.length);
    }

    @Test
    public void testEnsureCapacityLessThanMinCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.ensureCapacity(4);
        assertEquals(6, list._data.length);
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
        assertEquals(5, list._data.length);
    }

    @Test
    public void testTrimToSizeWhenSizeLessThanDataLength() {
        ArrayByteList list = new ArrayByteList(5);
        list._data = new byte[10];
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.trimToSize();
        assertEquals(5, list._data.length);
    }

    @Test
    public void testWriteObject() throws IOException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(list);
        ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectIn.readObject();
        assertEquals(5, deserializedList.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, deserializedList.get(i));
        }
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ByteArray byteArray = new ByteArray(new byte[] { 1, 2, 3, 4, 5 });
        ObjectOutputStream objectOut = new ObjectOutputStream(new ByteArrayOutputStream());
        objectOut.writeObject(byteArray);
        ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(objectOut.toByteArray()));
        ArrayByteList list = (ArrayByteList) objectIn.readObject();
        assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(byteArray.get(i), list.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRange(6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRangeIncludingEndpoint(6);
    }
}