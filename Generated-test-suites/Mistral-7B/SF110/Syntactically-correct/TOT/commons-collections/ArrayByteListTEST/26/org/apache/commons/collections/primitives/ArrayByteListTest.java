package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        // Empty list should not throw an exception when getting an element
        Assert.assertNull(list.get(0));
    }

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(5, list.size());
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        Assert.assertEquals(5, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
        Assert.assertEquals((byte) 4, list.get(3));
        Assert.assertEquals((byte) 5, list.get(4));
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList(3);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);

        ArrayByteList list2 = new ArrayByteList(2);
        list2.add((byte) 4);
        list2.add((byte) 5);

        list1.addAll(1, list2);

        Assert.assertEquals(6, list1.size());
        Assert.assertEquals((byte) 1, list1.get(0));
        Assert.assertEquals((byte) 2, list1.get(1));
        Assert.assertEquals((byte) 3, list1.get(2));
        Assert.assertEquals((byte) 4, list1.get(3));
        Assert.assertEquals((byte) 5, list1.get(4));
        Assert.assertEquals((byte) 1, list1.get(5));
    }

    @Test
    public void testGetAtEnd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        Assert.assertEquals((byte) 5, list.get(list.size() - 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.get(-1);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        Assert.assertEquals(5, list.size());
        Assert.assertEquals((byte) 1, list.get(0));

        list.removeElementAt(0);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.removeElementAt(6);
    }

    @Test
    public void testSetElement() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        Assert.assertEquals(5, list.size());
        Assert.assertEquals((byte) 1, list.get(0));

        list.set(0, (byte) 10);
        Assert.assertEquals((byte) 10, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.set(-1, (byte) 10);
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

        Assert.assertEquals(5, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();

        Assert.assertEquals(3, deserializedList.size());
        Assert.assertEquals((byte) 1, deserializedList.get(0));
        Assert.assertEquals((byte) 2, deserializedList.get(1));
        Assert.assertEquals((byte) 3, deserializedList.get(2));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializationWithInvalidClass() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject("InvalidClass");
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
    }
}