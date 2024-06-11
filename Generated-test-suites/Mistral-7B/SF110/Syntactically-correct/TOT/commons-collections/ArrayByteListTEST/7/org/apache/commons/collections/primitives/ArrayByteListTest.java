import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        // Empty list should not throw an exception when get() is called
        Assert.assertEquals((byte) 0, list.get(0));
    }

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            list.set(i, (byte) i);
            Assert.assertEquals((byte) i, list.get(i));
        }
    }

    @Test
    public void testAddElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 1, list.get(0));

        list.add((byte) 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testSetElement() {
        ArrayByteList list = new ArrayByteList(2);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));

        list.set(0, (byte) 3);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 3, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(2);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);

        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));

        list.removeElementAt(0);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 2, list.get(0));

        list.removeElementAt(0);
        Assert.assertEquals(0, list.size());

        try {
            list.removeElementAt(-1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            list.removeElementAt(1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList(2);
        list1.set(0, (byte) 1);
        list1.set(1, (byte) 2);

        ArrayByteList list2 = new ArrayByteList(2);
        list2.set(0, (byte) 3);
        list2.set(1, (byte) 4);

        list1.addAll(1, list2);
        Assert.assertEquals(4, list1.size());
        Assert.assertEquals((byte) 1, list1.get(0));
        Assert.assertEquals((byte) 3, list1.get(1));
        Assert.assertEquals((byte) 2, list1.get(2));
        Assert.assertEquals((byte) 4, list1.get(3));
    }

    @Test
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(2);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);

        try {
            list.get(-1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            list.get(2);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
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

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        in.close();

        Assert.assertEquals(2, deserializedList.size());
        Assert.assertEquals((byte) 1, deserializedList.get(0));
        Assert.assertEquals((byte) 2, deserializedList.get(1));
    }

    @Test
    public void testNegativeInitialCapacity() {
        try {
            new ArrayByteList(-5);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}