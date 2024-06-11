package org.apache.commons.cli; // Assuming you want to keep the test package name the same for consistency with the example

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testTShortByteMapDecoratorEmptyConstruction() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(0, map.size());
        assertNull(map.getNoEntryKey());
        assertNull(map.getNoEntryValue());
    }

    @Test
    public void testTShortByteMapDecoratorMapConstruction() {
        TShortByteMap underlinedMap = new TShortByteHashMap();
        TShortByteMapDecorator decoratorMap = new TShortByteMapDecorator(underlinedMap);
        assertEquals(underlinedMap.size(), decoratorMap.size());
        assertEquals(underlinedMap.getNoEntryKey(), decoratorMap.getNoEntryKey());
        assertEquals(underlinedMap.getNoEntryValue(), decoratorMap.getNoEntryValue());
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);
        assertEquals(1, map.size());
        assertEquals(value1, map.get(key1));

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);
        assertEquals(2, map.size());
        assertEquals(value2, map.get(key2));
        assertEquals(value1, map.get(key1));
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);

        assertNull(map.get(null));
        assertEquals(value1, map.get(key1));
        assertEquals(value2, map.get(key2));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);

        map.clear();
        assertEquals(0, map.size());
        assertNull(map.getNoEntryKey());
        assertNull(map.getNoEntryValue());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);

        Byte removedValue = map.remove(key1);
        assertEquals(1, map.size());
        assertNull(removedValue);
        assertNull(map.get(key1));
        assertEquals(value2, map.get(key2));

        removedValue = map.remove(key2);
        assertEquals(0, map.size());
        assertNull(removedValue);
        assertNull(map.get(key1));
        assertNull(map.get(key2));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());

        assertTrue(entrySet.contains(Map.entry(key1, value1)));
        assertTrue(entrySet.contains(Map.entry(key2, value2)));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);

        assertTrue(map.containsValue(value1));
        assertTrue(map.containsValue(value2));
        assertFalse(map.containsValue(new Byte((byte)3)));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);

        assertTrue(map.containsKey(key1));
        assertTrue(map.containsKey(key2));
        assertFalse(map.containsKey((Object)null));
        assertFalse(map.containsKey((short)-1));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        map.put(key2, value2);

        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        assertFalse(map.isEmpty());

        map.clear();

        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        Map<Short, Byte> underlinedMap = new TShortByteHashMap();
        underlinedMap.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        underlinedMap.put(key2, value2);

        map.putAll(underlinedMap);

        assertEquals(2, map.size());
        assertEquals(value1, map.get(key1));
        assertEquals(value2, map.get(key2));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        map.put(key1, value1);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        map.writeExternal(objStream);
        objStream.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInput = new ObjectInputStream(byteInput);
        TShortByteMapDecorator decoredMap = (TShortByteMapDecorator)objInput.readObject();
        objInput.close();

        assertEquals(map.size(), decoredMap.size());
        assertEquals(map.getNoEntryKey(), decoredMap.getNoEntryKey());
        assertEquals(map.getNoEntryValue(), decoredMap.getNoEntryValue());
        assertEquals(map.get(key1), decoredMap.get(key1));
    }
}