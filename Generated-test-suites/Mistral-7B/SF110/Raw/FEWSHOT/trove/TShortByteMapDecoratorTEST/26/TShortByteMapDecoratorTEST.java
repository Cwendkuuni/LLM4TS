package org.apache.commons.cli;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testPutGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        map.put(key, value);
        Assert.assertEquals(1, map.size());
        Assert.assertFalse(map.isEmpty());
        Assert.assertEquals(value, map.get(key));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.put(null, (byte) 1));
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        map.put(key, null);
        Assert.assertEquals(1, map.size());
        Assert.assertFalse(map.isEmpty());
        Assert.assertNull(map.get(key));
    }

    @Test
    public void testPutSameKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key, value1);
        map.put(key, value2);
        Assert.assertEquals(1, map.size());
        Assert.assertFalse(map.isEmpty());
        Assert.assertEquals(value2, map.get(key));
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        TShortByteMap mapToAdd = new TShortByteMap(); // Implement this map and populate it with some key-value pairs
        map.putAll(mapToAdd);
        Assert.assertEquals(mapToAdd.size(), map.size());
        Assert.assertFalse(map.isEmpty());
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key1, value1);
        map.put(key2, value2);
        map.clear();
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map.put(key1, value1);
        Assert.assertEquals(1, map.size());
        Assert.assertFalse(map.isEmpty());
        Byte removedValue = map.remove(key1);
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
        Assert.assertEquals(value1, removedValue);
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.remove(null));
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        map.put(key, (byte) 1);
        assertTrue(map.containsKey(key));
        assertFalse(map.containsKey(Short.valueOf((short) 2)));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        map.put(key, value);
        assertTrue(map.containsValue(value));
        assertFalse(map.containsValue(Byte.valueOf((byte) 2)));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map.put(key1, value1);
        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());
    }
}