package org.apache.commons.cli; // This package is different from the actual package of the class, please change it accordingly.

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testTShortByteMapDecorator() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        TShortByteMap underMap = new TShortByteHashMap();
        TShortByteMapDecorator decoratedMap = new TShortByteMapDecorator(underMap);
        assertEquals(underMap, decoratedMap.getMap());

        assertNull(decoratedMap.put((short) 1, (byte) 1));
        assertEquals(1, decoratedMap.size());
        assertFalse(decoratedMap.isEmpty());

        assertNull(decoratedMap.get(null));
        assertNull(decoratedMap.get((Short) 2));

        byte value = (byte) 2;
        decoratedMap.put((Short) 2, value);
        assertSame(value, decoratedMap.get((Short) 2));

        Set<Entry<Short, Byte>> entrySet = decoratedMap.entrySet();
        assertNotNull(entrySet);
        assertEquals(1, entrySet.size());
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return new Short((short) 2);
            }

            @Override
            public Byte getValue() {
                return new Byte((byte) 2);
            }
        }));
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        assertNull(map.put((short) 1, (byte) 1));
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());

        assertNull(map.put((short) 1, (byte) 2));
        assertEquals(1, map.size());

        assertNull(map.put((short) 1, (byte) null));
        assertEquals(1, map.size());

        short key = Short.MIN_VALUE;
        Byte value = Byte.MIN_VALUE;
        assertNull(map.put(key, value));
        assertEquals(1, map.size());

        assertNull(map.put(null, (byte) 1));
        assertEquals(1, map.size());

        assertNull(map.put((short) 1, null));
        assertEquals(1, map.size());

        key = Short.MAX_VALUE;
        value = Byte.MAX_VALUE;
        assertNull(map.put(key, value));
        assertEquals(1, map.size());
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        assertNull(map.get(null));
        assertNull(map.get((Short) 1));

        byte value = (byte) 2;
        map.put((Short) 1, value);
        assertSame(value, map.get((Short) 1));

        value = (byte) 3;
        map.put((Short) 1, value);
        assertSame(value, map.get((Short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        assertSame((byte) 1, map.remove((Short) 1));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        assertNull(map.remove(null));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        assertNull(map.remove((Short) 1));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertEquals(1, entrySet.size());
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return new Short((short) 1);
            }

            @Override
            public Byte getValue() {
                return new Byte((byte) 1);
            }
        }));

        // Test the remove method of the AbstractSet implementation
        map.remove((Short) 1);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(entrySet.contains(new Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return new Short((short) 1);
            }

            @Override
            public Byte getValue() {
                return new Byte((byte) 1);
            }
        }));
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
        assertEquals(1, map.size());

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
        TShortByteMapDecorator map = new TShortByteMapDecorator();

        Map<Short, Byte> underMap = new HashMap<>();
        underMap.put((short) 1, (byte) 1);
        underMap.put((short) 2, (byte) 2);

        map.putAll(underMap);
        assertEquals(2, map.size());

        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.containsAll(underMap.entrySet()));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(out);
        map.writeExternal(objOut);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInputStream(in);
        TShortByteMapDecorator map2 = (TShortByteMapDecorator) objIn.readObject();
        objIn.close();

        assertEquals(map, map2);
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        TShortByteMapDecorator clonedMap = (TShortByteMapDecorator) map.clone();
        assertEquals(map, clonedMap);
    }
}