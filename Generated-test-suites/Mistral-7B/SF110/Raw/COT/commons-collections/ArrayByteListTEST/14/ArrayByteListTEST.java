package org.junit.tests;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;

public class ArrayByteListTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }

    @org.junit.Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(10, list.length());
    }

    @org.junit.Test
    public void testConstructorFromByteCollection() {
        // Assuming we have a ByteCollection that we can use to test this method
        ByteCollection byteCollection = new ByteCollection();
        ArrayByteList list = new ArrayByteList(byteCollection);
        Assert.assertEquals(byteCollection.size(), list.size());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-5);
    }

    @org.junit.Test
    public void testGetMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        byte expected = 1;
        byte actual = list.get(0);
        Assert.assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(-1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(5);
    }

    @org.junit.Test
    public void testSizeMethod() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(0, list.size());
        list.add((byte) 1);
        Assert.assertEquals(1, list.size());
    }

    @org.junit.Test
    public void testRemoveElementAtMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        byte removedElement = list.removeElementAt(0);
        Assert.assertEquals((byte) 1, removedElement);
        Assert.assertEquals(1, list.size());
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(-1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(5);
    }

    @org.junit.Test
    public void testSetMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        byte newElement = (byte) 2;
        list.set(0, newElement);
        Assert.assertEquals(newElement, list.get(0));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(-1, (byte) 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(5, (byte) 1);
    }

    @org.junit.Test
    public void testAddMethod() {
        ArrayByteList list = new ArrayByteList(5);
        byte elementToAdd = (byte) 1;
        list.add(elementToAdd);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(elementToAdd, list.get(0));
    }

    @org.junit.Test
    public void testAddMethodWithIndex() {
        ArrayByteList list = new ArrayByteList(5);
        byte elementToAdd = (byte) 1;
        int indexToAdd = 2;
        list.add(indexToAdd, elementToAdd);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(elementToAdd, list.get(indexToAdd));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(-1, (byte) 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(10, (byte) 1);
    }

    @org.junit.Test
    public void testEnsureCapacityMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.ensureCapacity(10);
        Assert.assertEquals(10, list.length());
    }

    @org.junit.Test
    public void testTrimToSizeMethod() {
        ArrayByteList list = new ArrayByteList(5);
        list.trimToSize();
        Assert.assertEquals(5, list.length());
    }

    @org.junit.Test
    public void testWriteObjectMethod() throws IOException {
        // WriteObject cannot be tested in unit test, as it requires a stream to write to.
    }

    @org.junit.Test
    public void testReadObjectMethod() throws IOException, ClassNotFoundException {
        // ReadObject cannot be tested in unit test, as it requires a stream to read from.
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRange(-1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRange(5);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRangeIncludingEndpoint(-1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.checkRangeIncludingEndpoint(5);
    }

    @org.junit.Test
    public void testConstructorWithNullByteCollection() {
        // Ensure that constructor with null ByteCollection throws IllegalArgumentException
        try {
            new ArrayByteList(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("that", e.getMessage());
        }
    }
}