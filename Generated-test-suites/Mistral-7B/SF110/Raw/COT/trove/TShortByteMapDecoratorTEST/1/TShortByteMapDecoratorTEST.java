package test;

import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMap _map;
    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        _map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(_map);
    }

    // Testing constructor

    @Test
    public void testTShortByteMapDecoratorEmptyConstructor() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testTShortByteMapDecoratorWithMapConstructor() {
        assertFalse(decorator.isEmpty());
    }

    // Testing methods with basic functionality

    @Test
    public void testPut() {
        short key = 1;
        byte value = 1;
        byte putValue = decorator.put(key, value);
        assertEquals(value, putValue);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(null));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testGet() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
        assertNull(decorator.get(null));
    }

    @Test
    public void testClear() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        byte removedValue = decorator.remove(key);
        assertEquals(value, removedValue);
        assertEquals(0, decorator.size());
    }

    @Test
    public void testEntrySet() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertTrue(entrySet.isEmpty());
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.remove();
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        assertTrue(decorator.containsKey((Short)key));
        assertFalse(decorator.containsKey(new Object()));
    }

    @Test
    public void testSize() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        short key = 1;
        byte value = 1;
        decorator.put(key, value);
        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        map.put(1, (byte)1);
        map.put(2, (byte)2);
        decorator.putAll(map);
        assertEquals(2, decorator.size());
        assertNull(decorator.get(null));
        assertEquals((byte)1, decorator.get((short)1));
        assertEquals((byte)2, decorator.get((short)2));
    }

    // Testing edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        decorator.put((Short)null, (Byte)1);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        decorator.put((Short)1, (Byte)null);
    }

    @Test(expected = ClassCastException.class)
    public void testGetClassCastException() {
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)1, (Byte)2);
        assertEquals((Byte)1, decorator.get((Object)1));
        assertNotEquals((Byte)2, decorator.get((Object)1));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        decorator.put((Short)1, (Byte)1);
        assertNull(decorator.get(null));
    }

    @Test(expected = KeyNotFoundException.class)
    public void testGetKeyNotFoundException() {
        decorator.put((Short)1, (Byte)1);
        assertNull(decorator.get((short)2));
    }

    @Test
    public void testIteratorHasNext() {
        short key1 = 1;
        byte value1 = 1;
        decorator.put(key1, value1);

        short key2 = 2;
        byte value2 = 2;
        decorator.put(key2, value2);

        Iterator<Entry<Short, Byte>> iterator = decorator.iterator();
        assertTrue(iterator.hasNext());
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());
        assertTrue(iterator.hasNext());
        entry = iterator.next();
        assertEquals(key2, entry.getKey());
        assertEquals(value2, entry.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testIteratorConcurrentModificationException() {
        short key1 = 1;
        byte value1 = 1;
        decorator.put(key1, value1);

        short key2 = 2;
        byte value2 = 2;
        decorator.put(key2, value2);

        Iterator<Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());

        decorator.put((Short)1, (Byte)3);
        iterator.next();

        iterator.remove();
    }

    @Test(expected = ClassCastException.class)
    public void testSerializerDeserializerClassCastException() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(decorator);
        objectStream.close();
        byte[] byteArray = byteStream.toByteArray();
        ByteArrayInputStream byteStreamInput = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectStreamInput = new ObjectInputStream(byteStreamInput);
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)objectStreamInput.readObject();
        byte wrongValue = (byte)128;
        assertEquals(deserializedDecorator.get((Short)1), (Byte)1);
        assertEquals(deserializedDecorator.get((Short)1), (Byte)1);
        deserializedDecorator.put((Short)1, wrongValue);
        assertNotEquals((Byte)wrongValue, deserializedDecorator.get((Short)1));
    }
}