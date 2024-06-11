import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        Assert.assertNull(list.get(0));
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(0));
        Assert.assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-1));
    }

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            list.add((byte) i);
            Assert.assertEquals(i, list.get(i));
        }
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList(2);
        list1.add((byte) 1);
        list1.add((byte) 2);
        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 3);
        list2.add((byte) 4);
        list2.add((byte) 5);
        list1.addAll(list2);
        Assert.assertEquals(8, list1.size());
        Assert.assertEquals(list2.toString(), list1.toString());
    }

    @Test
    public void testGetSetRemove() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        Assert.assertEquals(1, list.get(0));
        list.set(0, (byte) 4);
        Assert.assertEquals(4, list.get(0));
        Assert.assertEquals(2, list.removeElementAt(1));
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.ensureCapacity(15);
        Assert.assertEquals(11, list.size());
        list.trimToSize();
        Assert.assertEquals(6, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        Assert.assertEquals(list.toString(), deserializedList.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsException() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.get(-1);
    }

    @Test
    public void testModCount() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.set(0, (byte) 4);
        list.removeElementAt(0);
        list.set(2, (byte) 5);
        list.add(0, (byte) 6);
        list.add(3, (byte) 7);
        list.trimToSize();
        Assert.assertEquals(7, list.size());
        Assert.assertEquals(8, list.modCount);
    }
}