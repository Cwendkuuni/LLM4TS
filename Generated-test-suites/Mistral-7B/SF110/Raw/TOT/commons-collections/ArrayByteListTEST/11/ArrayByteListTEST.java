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
    public void testAddElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testAddMultipleElements() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testSetElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(0, (byte) 2);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test
    public void testRemoveElementAtEnd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testRemoveElementAtBeginning() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(0);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test
    public void testRemoveElementAtMiddle() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.removeElementAt(1);
        Assert.assertEquals(2, list.size());
        Assert.assertNotEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexLargerThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.removeElementAt(10);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.ensureCapacity(7);
        Assert.assertEquals(7, list.size());
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
        Assert.assertEquals(2, deserializedList.size());
    }
}