import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        Assert.assertNull(list.get(0));
    }

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testInitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
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

        list1.addAll(list2);

        Assert.assertEquals(5, list1.size());
        Assert.assertEquals((byte) 1, list1.get(0));
        Assert.assertEquals((byte) 2, list1.get(1));
        Assert.assertEquals((byte) 3, list1.get(2));
        Assert.assertEquals((byte) 4, list1.get(3));
        Assert.assertEquals((byte) 5, list1.get(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testGetIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        try {
            list.get(-1);
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Should be at least 0 and less than 3, found -1", e.getMessage());
            throw e;
        }

        try {
            list.get(3);
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Should be at least 0 and less than 3, found 3", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.set(0, (byte) 4);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals((byte) 4, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        Assert.assertEquals(3, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));

        list.removeElementAt(1);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 3, list.get(1));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.add(0, (byte) 0);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals((byte) 0, list.get(0));
        Assert.assertEquals((byte) 1, list.get(1));
        Assert.assertEquals((byte) 2, list.get(2));
        Assert.assertEquals((byte) 3, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.add(4, (byte) 4);
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

        list.trimToSize();
        Assert.assertEquals(5, list.size());
    }

    @Test(expected = IOException.class)
    public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
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

    @Test
    public void testSerializationAndDeserializationWithNullData() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();

        Assert.assertEquals(0, deserializedList.size());
        Assert.assertNull(deserializedList._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializationWithNegativeCapacity() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(-1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();
    }
}