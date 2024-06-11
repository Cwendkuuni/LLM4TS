import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list._data);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNotNull(list._data);
        assertEquals(5, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList(byteFactory.getByteArray(5));
        assertEquals(5, list.size());
        assertArrayEquals(byteFactory.getByteArray(5), list._data);
    }

    @Test
    public void testGet() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList(byteFactory.getByteArray(3));
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        try {
            list.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            list.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSize() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList(byteFactory.getByteArray(3));
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(2, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 3, list.get(1));
        try {
            list.removeElementAt(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            list.removeElementAt(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSet() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList(byteFactory.getByteArray(3));
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        list.set(0, (byte) 4);
        assertEquals((byte) 4, list.get(0));
        list.set(1, (byte) 5);
        assertEquals((byte) 5, list.get(1));
        list.set(2, (byte) 6);
        assertEquals((byte) 6, list.get(2));
        try {
            list.set(-1, (byte) 7);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            list.set(3, (byte) 7);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testAdd() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList(byteFactory.getByteArray(2));
        list.add((byte) 3);
        assertEquals(3, list.size());
        assertEquals((byte) 3, list.get(2));
        list.add(0, (byte) 1);
        assertEquals(4, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertEquals((byte) 3, list.get(2));
        list.add(4, (byte) 5);
        assertEquals(5, list.size());
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 5, list.get(4));
        ByteArrayFactory byteFactory2 = new ByteArrayFactory();
        byte[] bytes = byteFactory2.getByteArray(2);
        list.addAll(2, bytes);
        assertEquals(7, list.size());
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertEquals((byte) 4, list.get(4));
        assertEquals((byte) 5, list.get(5));
        assertEquals((byte) 5, list.get(6));
        try {
            list.add(-1, (byte) 7);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            list.add(7, (byte) 7);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testEnsureCapacity() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(5);
        assertNotNull(list._data);
        assertEquals(5, list._data.length);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
        assertEquals(5, list._data.length);
        list.add((byte) 6);
        assertEquals(6, list.size());
        assertEquals(11, list._data.length);
        ByteArrayFactory byteFactory2 = new ByteArrayFactory();
        list.ensureCapacity(byteFactory2.getByteArray(100).length);
        assertEquals(127, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
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
        assertEquals(10, list.size());
        assertEquals(10, list._data.length);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(list);
        objOut.close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(in);
        ArrayByteList list2 = (ArrayByteList) objIn.readObject();
        objIn.close();
        in.close();
        out.close();
        assertEquals(2, list2.size());
        assertArrayEquals(list._data, list2._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList();
        list.checkRange(0);
        list.checkRange(-1);
        list.checkRange(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        ByteArrayFactory byteFactory = new ByteArrayFactory();
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(0);
        list.checkRangeIncludingEndpoint(-1);
        list.checkRangeIncludingEndpoint(10);
    }

}