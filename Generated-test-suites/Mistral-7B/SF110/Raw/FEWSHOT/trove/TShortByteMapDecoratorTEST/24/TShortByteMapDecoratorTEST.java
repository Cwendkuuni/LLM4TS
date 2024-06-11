package org.apache.commons.cli; // Replace with the actual package name of your test class

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testTShortByteMapDecorator() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertNotNull(decorator);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte retval1 = decorator.put(key1, value1);
        assertNull(retval1);
        assertEquals(value1, decorator.get(key1));

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        Byte retval2 = decorator.put(key2, value2);
        assertNull(retval2);
        assertEquals(value2, decorator.get(key2));

        Short key3 = Short.valueOf((short) 3);
        Byte value3 = Byte.valueOf((byte) 3);
        Byte retval3 = decorator.put(key3, value3);
        assertEquals(value1, retval3); // Key 3 replaces key 1
        assertEquals(value3, decorator.get(key3));
        assertNull(decorator.get(key1));
    }

    @Test
    public void testGet() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte retval1 = decorator.put(key1, value1);
        assertNull(retval1);
        assertEquals(value1, decorator.get(key1));

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        Byte retval2 = decorator.put(key2, value2);
        assertNull(retval2);
        assertEquals(value2, decorator.get(key2));

        Short key3 = Short.valueOf((short) 3);
        Byte value3 = Byte.valueOf((byte) 3);
        Byte retval3 = decorator.put(key3, value3);
        assertEquals(value1, retval3); // Key 3 replaces key 1
        assertEquals(value3, decorator.get(key3));
        assertNull(decorator.get(key1));

        assertEquals(value1, decorator.get(null)); // Get value for null key
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte retval1 = decorator.put(key1, value1);
        assertNull(retval1);

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        Byte retval2 = decorator.put(key2, value2);
        assertNull(retval2);

        decorator.clear();

        assertFalse(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte retval1 = decorator.put(key1, value1);
        assertNull(retval1);

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        Byte retval2 = decorator.put(key2, value2);
        assertNull(retval2);

        Byte retval = decorator.remove(key1);
        assertNull(retval);
        assertFalse(decorator.containsKey(key1));
        assertEquals(value2, decorator.get(key2));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

        int size = entrySet.size();
        assertEquals(2, size);

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());

        entry = iterator.next();
        assertEquals(key2, entry.getKey());
        assertEquals(value2, entry.getValue());

        iterator.remove();
        size = entrySet.size();
        assertEquals(1, size);

        assertFalse(entrySet.contains(entry));
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);

        decorator.put(key1, value1);

        assertTrue(decorator.containsValue(value1));
        assertTrue(decorator.containsValue(value2));

        decorator.put(key1, value2);

        assertFalse(decorator.containsValue(value1));
        assertTrue(decorator.containsValue(value2));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);

        assertFalse(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));

        decorator.put(key1, Byte.valueOf((byte) 1));

        assertTrue(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));

        decorator.put(key2, Byte.valueOf((byte) 2));

        assertTrue(decorator.containsKey(key1));
        assertTrue(decorator.containsKey(key2));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        int size = decorator.size();
        assertEquals(0, size);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte retval1 = decorator.put(key1, value1);
        assertNull(retval1);

        size = decorator.size();
        assertEquals(1, size);

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        Byte retval2 = decorator.put(key2, value2);
        assertNull(retval2);

        size = decorator.size();
        assertEquals(2, size);
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        assertTrue(decorator.isEmpty());

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte retval1 = decorator.put(key1, value1);
        assertNull(retval1);

        assertFalse(decorator.isEmpty());

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        Byte retval2 = decorator.put(key2, value2);
        assertNull(retval2);

        assertFalse(decorator.isEmpty());

        decorator.clear();

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map1 = new TShortByteHashMap();
        TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(map1);

        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte retval1 = decorator1.put(key1, value1);
        assertNull(retval1);

        TShortByteMap map2 = new TShortByteHashMap();
        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator(map2);

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        Byte retval2 = decorator2.put(key2, value2);
        assertNull(retval2);

        Map<Short, Byte> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);

        decorator1.putAll(map);

        assertEquals(2, decorator1.size());
        assertTrue(decorator1.containsKey(key1));
        assertTrue(decorator1.containsKey(key2));
    }
}