package org.apache.commons.cli; // Assuming the test class is in the same package as the tested class for simplicity

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void test初始化() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.getMap());

        TShortByteMap baseMap = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(baseMap);
        assertEquals(baseMap, decorator.getMap());
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.put((short) 1, (byte) 1));
        assertEquals((byte) 1, map.put((short) 1, (byte) 2));
        assertEquals((byte) 2, map.get((short) 1));

        short key = Short.MIN_VALUE;
        Byte value = null;
        map.put(key, value);
        assertNull(map.get(key));
        assertEquals(0, map.size());

        map.put(key, (byte) 3);
        assertEquals((byte) 3, map.get(key));
        assertEquals(1, map.size());

        map.put((short) 2, (byte) 4);
        assertEquals((byte) 4, map.get((short) 2));
        assertEquals(2, map.size());
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.get(null));
        assertNull(map.get((short) Short.MIN_VALUE));
        assertNull(map.get((short) 1));

        map.put((short) 1, (byte) 1);
        assertEquals((byte) 1, map.get((short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.clear();
        assertNull(map.get((short) 1));
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        assertNull(map.remove(null));
        assertEquals((byte) 1, map.remove((short) 1));
        assertNull(map.get((short) 1));
        assertEquals(0, map.size());

        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        assertNull(map.remove((short) 3));
        assertEquals((byte) 1, map.remove((short) 1));
        assertEquals((byte) 2, map.get((short) 2));
        assertEquals(1, map.size());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertTrue(entrySet.isEmpty());

        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals((short) 1, entry.getKey());
        assertEquals((byte) 1, entry.getValue());
        iterator.hasNext();
        entry = iterator.next();
        assertEquals((short) 2, entry.getKey());
        assertEquals((byte) 2, entry.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertFalse(map.containsValue((byte) 1));

        map.put((short) 1, (byte) 1);
        assertTrue(map.containsValue((byte) 1));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertFalse(map.containsKey((short) 1));

        map.put((short) 1, (byte) 1);
        assertTrue(map.containsKey((short) 1));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(0, map.size());

        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());

        map.put((short) 1, (byte) 1);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator target = new TShortByteMapDecorator();
        TShortByteMap source = new TShortByteMap();
        source.put((short) 1, (byte) 1);
        source.put((short) 2, (byte) 2);

        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 3, (byte) 3);
        decorator.put((short) 4, (byte) 4);

        target.putAll(source);
        assertEquals(2, target.size());
        assertEquals((byte) 1, target.get((short) 1));
        assertEquals((byte) 2, target.get((short) 2));

        target.putAll(decorator);
        assertEquals(4, target.size());
        assertEquals((byte) 1, target.get((short) 1));
        assertEquals((byte) 2, target.get((short) 2));
        assertEquals((byte) 3, target.get((short) 3));
        assertEquals((byte) 4, target.get((short) 4));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);
        map.writeExternal(out);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
        TShortByteMapDecorator map2 = new TShortByteMapDecorator();
        map2.readExternal(in);
        assertEquals(map.get((short) 1), map2.get((short) 1));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TShortByteMapDecorator original = new TShortByteMapDecorator();
        original.put((short) 1, (byte) 1);
        TShortByteMapDecorator clone = (TShortByteMapDecorator)original.clone();
        assertEquals(original.get((short) 1), clone.get((short) 1));
    }
}