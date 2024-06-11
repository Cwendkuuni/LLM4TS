import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        // Empty list should not throw an exception when getting an element
        Assert.assertNull(list.get(0));
    }

    @Test
    public void testAddAndGetElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testAddAndGetElementAtEnd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(0, (byte) 3);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 3, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 1));
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, (byte) 1));
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 1);
        list1.add((byte) 2);

        ArrayByteList list2 = new ArrayByteList();
        list2.add((byte) 3);
        list2.add((byte) 4);

        list1.addAll(1, list2);
        Assert.assertEquals(5, list1.size());
        Assert.assertEquals((byte) 3, list1.get(1));
        Assert.assertEquals((byte) 4, list1.get(2));
        Assert.assertEquals((byte) 1, list1.get(0));
        Assert.assertEquals((byte) 2, list1.get(3));
        Assert.assertEquals((byte) 1, list1.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.addAll(-1, new ArrayByteList()));
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.addAll(5, new ArrayByteList()));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(0);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(2));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.close();
        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteStream1 = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(byteStream1);
        ArrayByteList listDeserialized = (ArrayByteList) in.readObject();
        Assert.assertEquals(2, listDeserialized.size());
        Assert.assertEquals((byte) 1, listDeserialized.get(0));
        Assert.assertEquals((byte) 2, listDeserialized.get(1));
    }

    @Test
    public void testCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(8, list._data.length);
        list.ensureCapacity(10);
        Assert.assertEquals(8, list._data.length);
        list.add((byte) 5);
        Assert.assertEquals(5, list.size());
        Assert.assertEquals(16, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialCapacityBelowZero() {
        new ArrayByteList(-1);
    }
}