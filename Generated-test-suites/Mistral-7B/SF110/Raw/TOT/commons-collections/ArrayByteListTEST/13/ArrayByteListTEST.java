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
        ArrayByteList list = new ArrayByteList(-1);
    }

    @Test
    public void testAddAll() {
        byte[] input = {1, 2, 3};
        ArrayByteList list = new ArrayByteList();
        list.addAll(input, 0);
        Assert.assertArrayEquals(input, list.toByteArray());
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.get(2);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals((byte) 1, list.removeElementAt(0));
        Assert.assertEquals((byte) 2, list.get(0));
        Assert.assertEquals(1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.removeElementAt(1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(0, (byte) 3);
        Assert.assertEquals((byte) 3, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals(2, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(1, (byte) 2);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, list.toByteArray());
        Assert.assertEquals(3, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add(1, (byte) 2);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
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
        list.trimToSize();
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, list.toByteArray());
        Assert.assertEquals(10, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        Assert.assertArrayEquals(new byte[]{1, 2}, deserializedList.toByteArray());
        Assert.assertEquals(2, deserializedList.size());
    }
}