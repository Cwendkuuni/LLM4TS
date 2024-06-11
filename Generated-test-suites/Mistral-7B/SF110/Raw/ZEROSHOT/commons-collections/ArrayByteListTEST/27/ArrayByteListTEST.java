package org.apache.commons.collections.primitives;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ArrayByteListTest {

    @Test
    public void testConstructor_NoArguments() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        Assert.assertNull(list._data);
    }

    @Test
    public void testConstructor_InitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        Assert.assertEquals(0, list.size());
        Assert.assertArrayEquals(new byte[10], list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAll_ByteCollection() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        Assert.assertArrayEquals(initialData, list._data);
        Assert.assertEquals(initialData.length, list.size());
    }

    @Test
    public void testGet_ValidIndex() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        byte expected = 2;
        byte actual = list.get(1);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        list.get(-1);
    }

    @Test
    public void testRemoveElementAt_ValidIndex() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        byte expected = 2;
        byte actual = list.removeElementAt(1);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(initialData.length - 1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_InvalidIndex() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        list.removeElementAt(-1);
    }

    @Test
    public void testSet_ValidIndex() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        byte newValue = 5;
        list.set(1, newValue);
        byte expected = newValue;
        byte actual = list.get(1);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_InvalidIndex() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        list.set(-1, (byte) 0);
    }

    @Test
    public void testAdd_ValidIndex() {
        ArrayByteList list = new ArrayByteList();
        byte newValue = 5;
        list.add(0, newValue);
        byte expected = newValue;
        byte actual = list.get(0);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        byte newValue = 5;
        list.add(-1, newValue);
    }

    @Test
    public void testEnsureCapacity_MinCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(1);
        Assert.assertEquals(16, list._data.length);
    }

    @Test
    public void testEnsureCapacity_CapacityLargerThanMinCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        Assert.assertEquals(16, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));
        list.trimToSize();
        Assert.assertEquals(initialData.length, list._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();

        Assert.assertArrayEquals(initialData, deserializedList._data);
        Assert.assertEquals(initialData.length, deserializedList.size());
    }

    @Test
    public void testSerializationRoundTrip() throws IOException, ClassNotFoundException {
        byte[] initialData = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(initialData.length);
        list.addAll(new ArrayByteList(initialData));

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();

        Assert.assertArrayEquals(initialData, deserializedList._data);
        Assert.assertEquals(initialData.length, deserializedList.size());

        Assert.assertEquals(initialData.length, deserializedList._data.length);

        deserializedList.add(1, (byte) 4);
        Assert.assertArrayEquals(new byte[]{1, 4, 2, 3}, deserializedList._data);
        Assert.assertEquals(initialData.length + 1, deserializedList.size());

        byteStream = new ByteArrayOutputStream();
        out = new ObjectOutputStream(byteStream);
        out.writeObject(deserializedList);
        out.close();

        byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        in = new ObjectInputStream(byteInput);
        list = (ArrayByteList) in.readObject();

        Assert.assertArrayEquals(new byte[]{1, 4, 2, 3}, list._data);
        Assert.assertEquals(initialData.length + 1, list.size());
    }

    @Test
    public void testSerializationEmptyList() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();

        Assert.assertArrayEquals(new byte[0], deserializedList._data);
        Assert.assertEquals(0, deserializedList.size());
    }

    @Test
    public void testSerializationNullData() {
        ArrayByteList list = new ArrayByteList();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.writeObject(null);
        out.writeInt(-1);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);
        try {
            ArrayByteList deserializedList = (ArrayByteList) in.readObject();
            Assert.fail("Expected IllegalArgumentException");
        } catch (ClassCastException e) {
            // Expected
        } catch (EOFException e) {
            // Expected
        } catch (IOException e) {
            Assert.fail("Unexpected IOException");
        }
    }
}