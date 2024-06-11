package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyConstructor() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertFalse(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testMapConstructor() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);
        assertSame(baseMap, map.getMap());
        assertFalse(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testPut() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        assertEquals(1, map.size());
        assertSame(value1, map.get(key1));
        assertEquals(value1, map.put(key1, value2));
        assertNull(map.get(key1));
        assertEquals(1, map.size());

        map.put(key2, (byte)3);
        assertEquals(2, map.size());
        assertSame(Byte.valueOf((byte)3), map.get(key2));
    }

    @Test
    public void testGet() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);

        map.put(key1, value1);
        assertSame(value1, map.get(key1));
        assertNull(map.get(Short.valueOf((short)2)));
    }

    @Test
    public void testClear() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        map.put(key2, value2);
        assertEquals(2, map.size());

        map.clear();
        assertFalse(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        map.put(key2, value2);
        assertEquals(2, map.size());

        Byte removedValue = map.remove(key1);
        assertEquals(1, map.size());
        assertSame(value1, removedValue);
        assertNull(map.get(key1));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        map.put(key2, value2);
        assertEquals(2, map.size());

        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry1 = iterator.next();
        Entry<Short, Byte> entry2 = iterator.next();

        assertSame(key1, entry1.getKey());
        assertSame(value1, entry1.getValue());
        assertSame(key2, entry2.getKey());
        assertSame(value2, entry2.getValue());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        map.put(key2, value2);

        assertTrue(map.containsValue(value1));
        assertTrue(map.containsValue(value2));
        assertFalse(map.containsValue(Byte.valueOf((byte)3)));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        map.put(key2, value2);

        assertTrue(map.containsKey(key1));
        assertTrue(map.containsKey(key2));
        assertFalse(map.containsKey(Short.valueOf((short)3)));
    }

    @Test
    public void testSize() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        map.put(key2, value2);

        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        map.put(key1, value1);
        map.put(key2, value2);

        assertFalse(map.isEmpty());

        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap baseMap1 = new TShortByteHashMap(10);
        TShortByteMap baseMap2 = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap1);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);
        Short key2 = Short.valueOf((short)2);

        Map<Short, Byte> mapToAdd = new TShortByteHashMap();
        mapToAdd.put(key1, value1);
        mapToAdd.put(key2, value2);

        map.putAll(mapToAdd);

        assertEquals(2, map.size());
        assertSame(value1, map.get(key1));
        assertSame(value2, map.get(key2));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMap baseMap = new TShortByteHashMap(10);
        TShortByteMapDecorator map = new TShortByteMapDecorator(baseMap);

        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        Short key1 = Short.valueOf((short)1);

        map.put(key1, value1);
        map.put(key1, value2);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        map.writeExternal(objStream);
        objStream.close();

        byte[] bytes = byteStream.toByteArray();
        ObjectInput in = new ObjectInput(new ByteArrayInputStream(bytes));

        TShortByteMapDecorator mapFromStream = new TShortByteMapDecorator();
        mapFromStream.readExternal(in);
        in.close();

        assertEquals(1, mapFromStream.size());
        assertNull(mapFromStream.get(key1));
    }
}