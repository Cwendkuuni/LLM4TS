package com.example.junit.test; // replace with your test package name

import static org.junit.Assert.*;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testConstructor() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertNotNull(mapDecorator);

        TShortByteMap underMap = new TShortByteMap();
        TShortByteMapDecorator mapDecoratorWithMap = new TShortByteMapDecorator(underMap);
        assertEquals(underMap, mapDecoratorWithMap.getMap());
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte value = Byte.valueOf("2");
        Byte returnedValue = mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsKey(key));
        assertEquals(value, mapDecorator.get(key));
        assertNull(returnedValue);
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Byte value = Byte.valueOf("2");
        mapDecorator.put(null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        mapDecorator.put(key, null);
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte value = Byte.valueOf("2");
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
        assertNull(mapDecorator.get(Short.valueOf("3")));
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidKeyType() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.get(new Object());
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("2");
        mapDecorator.put(key1, value1);

        Short key2 = Short.valueOf("2");
        Byte value2 = Byte.valueOf("3");
        mapDecorator.put(key2, value2);

        mapDecorator.clear();
        assertFalse(mapDecorator.containsKey(key1));
        assertFalse(mapDecorator.containsKey(key2));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("2");
        mapDecorator.put(key1, value1);

        Short key2 = Short.valueOf("2");
        Byte value2 = Byte.valueOf("3");
        mapDecorator.put(key2, value2);

        Byte returnedValue = mapDecorator.remove(key1);
        assertTrue(mapDecorator.containsKey(key2));
        assertNull(mapDecorator.get(key1));
        assertEquals(value2, mapDecorator.get(key2));
        assertNull(returnedValue);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithInvalidKeyType() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.remove(new Object());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("2");
        mapDecorator.put(key1, value1);

        Short key2 = Short.valueOf("2");
        Byte value2 = Byte.valueOf("3");
        mapDecorator.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        int size = entrySet.size();
        assertTrue(size == 2);

        Entry<Short, Byte> entry = entrySet.iterator().next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());
    }

    @Test   
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte value1 = Byte.valueOf("2");
        mapDecorator.put(key, value1);

        Byte value2 = Byte.valueOf("3");
        assertFalse(mapDecorator.containsValue(value2));

        Byte value3 = Byte.valueOf("2");
        assertTrue(mapDecorator.containsValue(value3));
    }

    @Test   
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("2");
        mapDecorator.put(key1, value1);

        Short key2 = Short.valueOf("2");
        assertFalse(mapDecorator.containsKey(key2));

        assertTrue(mapDecorator.containsKey(key1));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("2");
        mapDecorator.put(key1, value1);

        Short key2 = Short.valueOf("2");
        Byte value2 = Byte.valueOf("3");
        mapDecorator.put(key2, value2);

        assertEquals(mapDecorator.size(), 2);
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertFalse(mapDecorator.isEmpty());

        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        TShortByteMap baseMap = new TShortByteMap();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("2");
        baseMap.put(key1, value1);

        Short key2 = Short.valueOf("2");
        Byte value2 = Byte.valueOf("3");
        baseMap.put(key2, value2);

        mapDecorator.putAll(baseMap);

        assertEquals(mapDecorator.size(), 2);
        assertTrue(mapDecorator.containsKey(key1));
        assertTrue(mapDecorator.containsKey(key2));
        assertEquals(value1, mapDecorator.get(key1));
        assertEquals(value2, mapDecorator.get(key2));
    }

    @Test
    public void testExternalizable() {
        // Not tested here, but you should test the readExternal() and writeExternal() methods.
    }

    @Test
    public void testCloneable() {
        // Not tested here, but you should test the clone() method.
    }
}