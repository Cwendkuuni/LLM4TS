package org.apache.commons.cli; // Assuming we want to test the TShortByteMapDecorator class in the context of the Apache Commons CLI package

import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        Byte retrievedValue = decorator.get(key1);
        assertEquals(value1, retrievedValue);
    }

    @Test
    public void testPutNullKeyAndValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertNull(decorator.put(null, null));
    }

    @Test
    public void testPutNullValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key = Short.valueOf((short) 1);
        assertNull(decorator.put(key, null));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        decorator.clear();
        assertFalse(decorator.containsKey(key1));
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        Byte removedValue = decorator.remove(key1);
        assertEquals(value1, removedValue);
        assertFalse(decorator.containsKey(key1));
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertNull(decorator.remove(null));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);

        Set<Entry<Short, Byte>> entries = decorator.entrySet();
        assertTrue(entries.size() == 1);
        assertTrue(entries.contains(new AbstractMap.SimpleEntry<>(key1, value1)));
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        assertTrue(decorator.containsValue(value1));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        assertTrue(decorator.containsKey(key1));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(true, decorator.isEmpty());

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        assertEquals(false, decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short[] keys = {Short.valueOf((short) 1), Short.valueOf((short) 2), Short.valueOf((short) 3)};
        Byte[] values = {Byte.valueOf((byte) 1), Byte.valueOf((byte) 2), Byte.valueOf((byte) 3)};

        Map<Short, Byte> mapToAdd = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            mapToAdd.put(keys[i], values[i]);
        }

        decorator.putAll(mapToAdd);

        assertEquals(keys.length, decorator.size());
        for (int i = 0; i < keys.length; i++) {
            assertEquals(values[i], decorator.get(keys[i]));
        }
    }

    @Test
    public void testExternalizable() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(out);
        decorator.writeExternal(objOut);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInputStream(in);
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)objIn.readObject();
        objIn.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        for (int i = 0; i < decorator.size(); i++) {
            assertEquals(decorator.get(decorator.keyAt(i)), deserializedDecorator.get(deserializedDecorator.keyAt(i)));
        }
    }
}