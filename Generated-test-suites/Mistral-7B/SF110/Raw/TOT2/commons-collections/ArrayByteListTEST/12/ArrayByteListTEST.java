import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Assert.assertTrue;
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertThrows;
import org.junit.Assert.assertNotNull;
import org.junit.Assert.assertNull;
import org.apache.commons.collections.primitives.ArrayByteList;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class ArrayByteListTest {
    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test
    public void testConstructorFromCollection() {
        ByteCollection collection = new ArrayList<>();
        collection.add((byte) 1);
        collection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testGetEmptyList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals((byte) 0, list.get(0));
    }

    @Test
    public void testGetWithValidIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexBeyondSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.get(5);
    }

    @Test
    public void testSetWithValidIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(-1, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexBeyondSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(5, (byte) 5);
    }

    @Test
    public void testAddWithValidIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(0, (byte) 1);
        list.add(1, (byte) 2);
        list.add(2, (byte) 3);
        list.add(3, (byte) 4);
        list.add(4, (byte) 5);
        assertEquals(5, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(0, (byte) 1);
        list.add(1, (byte) 2);
        list.add(-1, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexBeyondSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(0, (byte) 1);
        list.add(1, (byte) 2);
        list.add(2, (byte) 3);
        list.add(3, (byte) 4);
        list.add(5, (byte) 5);
        list.add(6, (byte) 6);
    }

    @Test
    public void testEnsureCapacityWithSizeLessThanCurrentSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        list.ensureCapacity(list.size() + 1);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testEnsureCapacityWithSizeGreaterThanCurrentSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        list.ensureCapacity(100);
        assertEquals(101, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        list.trimToSize();
        assertEquals(5, list.size());
    }

    @Test
    public void testSerialization() throws IOException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();

        assertEquals(3, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
        assertEquals((byte) 3, deserializedList.get(2));
    }

    @Test
    public void testToString() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals("[1, 2, 3]", list.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
        // This should throw an IllegalArgumentException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexBeyondSizeAfterAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.get(6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexBeyondSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.set(6, (byte) 6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexBeyondSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(6, (byte) 6);
    }

    @Test(expected = NullPointerException.class)
    public void testAddWithNullElement() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetWithNullElement() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, null);
    }

    @Test
    public void testIndexOf() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 1);
        list.add((byte) 3);

        assertEquals(2, list.indexOf((byte) 1));
        assertEquals(-1, list.indexOf((byte) 4));
    }

    @Test(expected = ClassCastException.class)
    public void testMixedCollectionTypes() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add(1);
        list.add((byte) 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyWithUnmodifiableList() {
        ArrayByteList list = new ArrayByteList();
        ByteList unmodifiableList = Collections.unmodifiableList(list);
        unmodifiableList.add((byte) 1);
    }
}