package org.apache.commons.cli; // Assuming you want to keep the same package for testing purposes

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collections;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutGetRemove() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        decorator.put(key1, value1);
        assertEquals(value1, decorator.get(key1));
        decorator.remove(key1);
        assertNull(decorator.get(key1));

        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        decorator.put(key2, value2);
        assertEquals(value2, decorator.get(key2));
        decorator.remove(key2);
        assertNull(decorator.get(key2));
    }

    @Test
    public void testPutGetMap() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        decorator.put(key1, value1);
        assertEquals(value1, decorator.get(key1));

        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        decorator.put(key2, value2);
        assertEquals(value2, decorator.get(key2));

        assertEquals(map.size(), decorator.size());
        assertTrue(map.containsKey(key1));
        assertTrue(map.containsKey(key2));
        assertTrue(map.containsValue(value1));
        assertTrue(map.containsValue(value2));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        decorator.put(key1, value1);
        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        decorator.put(key2, value2);

        decorator.clear();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));

        assertEquals(map.size(), decorator.size());
        assertFalse(map.containsKey(key1));
        assertFalse(map.containsKey(key2));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        decorator.put(key1, value1);
        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        decorator.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());
        iterator.hasNext();
        entry = iterator.next();
        assertEquals(key2, entry.getKey());
        assertEquals(value2, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        decorator.put(key1, value1);
        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        decorator.put(key2, value2);

        assertTrue(decorator.containsValue(value1));
        assertTrue(decorator.containsValue(value2));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        decorator.put(key1, value1);
        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        decorator.put(key2, value2);

        assertTrue(decorator.containsKey(key1));
        assertTrue(decorator.containsKey(key2));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        decorator.writeExternal(objectOutputStream);
        objectOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertTrue(decorator.isEmpty() == deserializedDecorator.isEmpty());
        Assert.assertEquals(decorator.getMap(), deserializedDecorator.getMap());
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator) decorator.clone();

        assertEquals(decorator.size(), clonedDecorator.size());
        assertTrue(decorator.isEmpty() == clonedDecorator.isEmpty());
        Assert.assertEquals(decorator.getMap(), clonedDecorator.getMap());
    }
}