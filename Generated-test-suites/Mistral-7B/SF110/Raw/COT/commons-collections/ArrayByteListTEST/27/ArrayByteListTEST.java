import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        Assert.assertNull(list._data);

        ArrayByteList listWithInitialCapacity = new ArrayByteList(5);
        Assert.assertEquals(0, listWithInitialCapacity.size());
        Assert.assertNotNull(listWithInitialCapacity._data);
        Assert.assertEquals(5, listWithInitialCapacity._data.length);
    }

    @Test
    public void testArrayByteListAddAll() {
        ArrayByteList list = new ArrayByteList();
        ByteCollection that = new ArrayByteList();
        that.add((byte) 1);
        that.add((byte) 2);
        list.addAll(that);

        Assert.assertEquals(2, list.size());
        Assert.assertArrayEquals(new byte[]{(byte) 1, (byte) 2}, list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testArrayByteListGet() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);

        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListIndexOutOfBoundsGet() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.get(-1);
    }

    @Test
    public void testArrayByteListSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        Assert.assertEquals(3, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListIndexOutOfBoundsRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListIndexOutOfBoundsSet() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.set(-1, (byte) 1);
    }

    @Test
    public void testArrayByteListAdd() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.add(4, (byte) 4);

        Assert.assertEquals(4, list.size());
        Assert.assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListIndexOutOfBoundsAdd() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.add(-1, (byte) 4);
    }

    @Test
    public void testArrayByteListEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.ensureCapacity(7);

        Assert.assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, list._data);
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testArrayByteListTrimToSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.trimToSize();

        Assert.assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, list._data);
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testArrayByteListSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        list.writeObject(oos);
        oos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();

        Assert.assertEquals(0, deserializedList.size());
        Assert.assertArrayEquals(new byte[0], deserializedList._data);

        list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);

        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        list.writeObject(oos);
        oos.close();

        bis = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bis);
        deserializedList = (ArrayByteList) ois.readObject();
        ois.close();

        Assert.assertEquals(5, deserializedList.size());
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, deserializedList._data);
    }

    @Test(expected = IOException.class)
    public void testArrayByteListSerializationFailedWriteObject() throws IOException {
        ArrayByteList list = new ArrayByteList();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(list);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testArrayByteListSerializationFailedReadObject() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(new byte[0]);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ois.readObject();
    }

}