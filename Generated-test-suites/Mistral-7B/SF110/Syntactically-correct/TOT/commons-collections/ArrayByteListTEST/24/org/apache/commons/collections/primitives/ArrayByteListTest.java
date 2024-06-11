import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(5, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        ArrayByteList list = new ArrayByteList(-5);
    }

    @Test
    public void testAddAll() {
        byte[] data = new byte[] {1, 2, 3};
        ArrayByteList sourceList = new ArrayByteList(data);
        ArrayByteList targetList = new ArrayByteList(2);
        targetList.addAll(sourceList);
        Assert.assertArrayEquals(data, targetList._data);
        Assert.assertEquals(3, targetList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.get(-1);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
        Assert.assertEquals((byte) 4, list.get(3));
        Assert.assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.set(-1, (byte) 1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.set(0, (byte) 10);
        Assert.assertArrayEquals(new byte[] {10, 2, 3, 4, 5}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.removeElementAt(-1);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        Assert.assertEquals((byte) 1, list.removeElementAt(0));
        Assert.assertEquals(4, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(6, (byte) -1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(1, (byte) 10);
        Assert.assertArrayEquals(new byte[] {10, 1, 2, 3, 4, 5}, list._data);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.add((byte) 10);
        list.add((byte) 11);
        list.add((byte) 12);
        list.ensureCapacity(20);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.add((byte) 10);
        list.add((byte) 11);
        list.add((byte) 12);
        list.trimToSize();
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        Assert.assertEquals(list.size(), deserializedList.size());
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), deserializedList.get(i));
        }
    }
}