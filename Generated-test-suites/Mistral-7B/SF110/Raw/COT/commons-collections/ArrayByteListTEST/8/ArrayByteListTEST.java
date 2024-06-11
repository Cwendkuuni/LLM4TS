import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(0, list.size());
        Assert.assertArrayEquals(new byte[5], list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAllFromByteCollection() {
        byte[] data = new byte[5];
        ArrayByteList sourceList = new ArrayByteList(data);
        ArrayByteList targetList = new ArrayByteList();
        targetList.addAll(sourceList);
        Assert.assertArrayEquals(data, targetList._data);
        Assert.assertEquals(data.length, targetList.size());
    }

    @Test
    public void testGetMethod() {
        byte[] data = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(data);
        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsIndex() {
        ArrayByteList list = new ArrayByteList(new byte[3]);
        list.get(3);
    }

    @Test
    public void testSizeMethod() {
        byte[] data = new byte[5];
        ArrayByteList list = new ArrayByteList(data);
        Assert.assertEquals(5, list.size());
    }

    @Test
    public void testRemoveElementAtMethod() {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ArrayByteList list = new ArrayByteList(data);
        Assert.assertEquals(1, list.removeElementAt(0));
        Assert.assertEquals(4, list.size());
        Assert.assertArrayEquals(new byte[]{2, 3, 4}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBoundsIndex() {
        byte[] data = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(data);
        list.removeElementAt(3);
    }

    @Test
    public void testSetMethod() {
        byte[] data = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(data);
        list.set(0, (byte) 4);
        Assert.assertArrayEquals(new byte[]{4, 2, 3}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsIndex() {
        byte[] data = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(data);
        list.set(3, (byte) 4);
    }

    @Test
    public void testAddMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list._data[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(3, (byte) 1);
    }

    @Test
    public void testEnsureCapacityMethod() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(5);
        Assert.assertArrayEquals(new byte[5], list._data);
    }

    @Test
    public void testTrimToSizeMethod() {
        byte[] data = new byte[10];
        ArrayByteList list = new ArrayByteList(data);
        list.trimToSize();
        Assert.assertArrayEquals(data, list._data);
        Assert.assertEquals(data.length, list.size());
    }

    @Test
    public void testWriteObjectMethod() throws IOException {
        byte[] data = new byte[]{1, 2, 3};
        ArrayByteList list = new ArrayByteList(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        Assert.assertArrayEquals(data, deserializedList._data);
        Assert.assertEquals(data.length, deserializedList.size());
    }

    @Test
    public void testReadObjectMethod() throws IOException, ClassNotFoundException {
        byte[] data = new byte[]{1, 2, 3};
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(data);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        ArrayByteList list = new ArrayByteList();
        list = (ArrayByteList) in.readObject();
        Assert.assertArrayEquals(data, list._data);
        Assert.assertEquals(data.length, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(10);
    }
}