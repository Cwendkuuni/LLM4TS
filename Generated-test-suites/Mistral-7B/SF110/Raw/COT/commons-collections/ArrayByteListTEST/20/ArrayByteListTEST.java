import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(5, list.size());
        Assert.assertNull(list.get(0));
    }

    @Test
    public void testArrayByteListConstructorWithEmptyByteCollection() {
        ByteCollection byteCollection = new byte[0]; // Use an empty byte array as a ByteCollection
        ArrayByteList list = new ArrayByteList(byteCollection);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListGetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        byte byteValue = list.get(1);
        Assert.assertEquals((byte) 2, byteValue);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListGetMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListGetMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.get(2);
    }

    @Test
    public void testArrayByteListSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        list.add((byte) 1);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testArrayByteListRemoveElementAtMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        byte removedByte = list.removeElementAt(0);
        Assert.assertEquals((byte) 1, removedByte);
        Assert.assertEquals(1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListRemoveElementAtMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListRemoveElementAtMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(1);
    }

    @Test
    public void testArrayByteListSetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        byte newByteValue = (byte) 3;
        list.set(0, newByteValue);
        Assert.assertEquals(newByteValue, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListSetMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListSetMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.set(1, (byte) 1);
    }

    @Test
    public void testArrayByteListAddMethodWithSingleElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testArrayByteListAddMethodWithMultipleElements() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        Assert.assertEquals(3, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListAddMethodWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListAddMethodWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(10, (byte) 1);
    }

    @Test
    public void testArrayByteListEnsureCapacityMethod() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(3);
        Assert.assertEquals(3, list._data.length);
    }

    @Test
    public void testArrayByteListTrimToSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(3, list._data.length);
    }

    @Test
    public void testArrayByteListSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInput = new ObjectInputStream(inputStream);
        ArrayByteList deserializedList = (ArrayByteList)objectInput.readObject();
        objectInput.close();

        Assert.assertEquals(2, deserializedList.size());
        Assert.assertEquals((byte)1, deserializedList.get(0));
        Assert.assertEquals((byte)2, deserializedList.get(1));
    }
}