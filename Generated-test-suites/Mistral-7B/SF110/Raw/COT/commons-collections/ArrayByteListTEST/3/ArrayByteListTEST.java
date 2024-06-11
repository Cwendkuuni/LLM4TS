package org.junit.tests;

import static org.apache.commons.collections.primitives.ArrayByteList.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.collections.primitives.ByteCollection;
import org.apache.commons.collections.primitives.MutableByteList;

/**
 * Test cases for ArrayByteList class.
 *
 * @author John Doe
 */
public class ArrayByteListTest {

    // Test constructor with no arguments
    @Test
    public void testEmptyArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    // Test constructor with initial capacity
    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    // Test constructor from ByteCollection
    @Test
    public void testArrayByteListFromByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(byteCollection);
        assertEquals(2, list.size());
    }

    // Test get method
    @Test
    public void testGetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    // Test size method
    @Test
    public void testSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    // Test removeElementAt method
    @Test
    public void testRemoveElementAtMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals((byte) 2, list.removeElementAt(1));
        assertEquals(2, list.size());
    }

    // Test set method
    @Test
    public void testSetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 2, list.set(0, (byte) 2));
        assertEquals((byte) 2, list.get(0));
    }

    // Test add method with no arguments
    @Test
    public void testAddMethodWithNoArguments() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(3, list.size());
    }

    // Test add method with index and element
    @Test
    public void testAddMethodWithIndexAndElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add(0, (byte) 0);
        assertEquals(3, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
    }

    // Test ensureCapacity method
    @Test
    public void testEnsureCapacityMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.ensureCapacity(15);
        assertEquals(10, list.size());
    }

    // Test trimToSize method
    @Test
    public void testTrimToSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.trimToSize();
        assertEquals(5, list.size());
    }

    // Test serialization and deserialization
    @Test
    public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);

        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        assertEquals(3, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
        assertEquals((byte) 3, deserializedList.get(2));
    }

    // Test checkRange method with negative index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRange(-1);
    }

    // Test checkRange method with index greater than size
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRange(2);
    }

    // Test checkRangeIncludingEndpoint method with negative index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRangeIncludingEndpoint(-1);
    }

    // Test checkRangeIncludingEndpoint method with index greater than size
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRangeIncludingEndpoint(2);
    }

    // Test IllegalArgumentException when initialCapacity is negative
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }
}