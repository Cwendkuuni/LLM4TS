package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.get(3);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals((byte) 2, list.get(0));
        assertEquals(1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(3);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(0, (byte) 3);
        assertEquals((byte) 3, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(-1, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(3, (byte) 3);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add(0, (byte) 0);
        assertEquals(3, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add(0, (byte) 0);
        list.add(-1, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add(0, (byte) 0);
        list.add(5, (byte) 3);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        for (int i = 0; i < 10; i++) {
            list.add((byte) i);
        }
        ArrayByteList copy = new ArrayByteList(list);
        assertEquals(10, list.size());
        assertEquals(10, copy.size());
        for (int i = 0; i < 10; i++) {
            assertEquals((byte) i, list.get(i));
            assertEquals((byte) i, copy.get(i));
        }
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        for (int i = 0; i < 10; i++) {
            list.add((byte) i);
        }
        ArrayByteList copy = new ArrayByteList(list);
        assertEquals(10, list.size());
        assertEquals(10, copy.size());
        list.trimToSize();
        assertEquals(10, list.size());
        assertEquals(10, copy.size());
        for (int i = 0; i < 10; i++) {
            assertEquals((byte) i, list.get(i));
            assertEquals((byte) i, copy.get(i));
        }
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(list);
        objStream.close();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInput = new ObjectInputStream(byteInput);
        ArrayByteList deserializedList = (ArrayByteList) objInput.readObject();
        ArrayList<Byte> deserializedArrayList = new ArrayList<>();
        for (int i = 0; i < deserializedList.size(); i++) {
            deserializedArrayList.add(deserializedList.get(i));
        }
        assertEquals(deserializedArrayList.toArray(), new byte[]{(byte) 1, (byte) 2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-5);
    }
}