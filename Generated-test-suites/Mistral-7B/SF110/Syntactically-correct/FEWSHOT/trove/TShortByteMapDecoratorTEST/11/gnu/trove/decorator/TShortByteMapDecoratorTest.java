package org.apache.commons.cli; // Assuming you want to test TShortByteMapDecorator in the context of the package org.apache.commons.cli

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 2);
        map.put(key, value);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testPutOverwrite() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map.put(key, value1);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key, value2);
        assertEquals(1, map.size());
        assertEquals(value2, map.get(key));
    }

    @Test
    public void testGetNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.get(null));
    }

    @Test
    public void testGetWrongKeyType() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Integer key = Integer.valueOf(1);
        assertNull(map.get(key));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = Byte.valueOf((byte) 1);
        map.put((short) 1, value);
        assertTrue(map.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        map.put(key, (byte) 1);
        assertTrue(map.containsKey(key));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map.put(key1, value1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Short, Byte>> it = entrySet.iterator();
        Entry<Short, Byte> entry = it.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());

        entry = it.next();
        assertEquals(key2, entry.getKey());
        assertEquals(value2, entry.getValue());
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map.put(key1, value1);
        assertEquals(1, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map.put(key1, value1);
        assertEquals(1, map.size());
        Byte removed = map.remove(key1);
        assertEquals(value1, removed);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator map1 = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map1.put(key1, value1);

        TShortByteMapDecorator map2 = new TShortByteMapDecorator();
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        map2.put(key2, value2);

        map1.putAll(map2);
        assertEquals(2, map1.size());
        assertTrue(map1.containsKey(key1));
        assertTrue(map1.containsKey(key2));
        assertEquals(value1, map1.get(key1));
        assertEquals(value2, map1.get(key2));
    }

    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);

        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        map.writeExternal(os);
        os.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream is = new ObjectInputStream(in);

        TShortByteMapDecorator mapLoaded = (TShortByteMapDecorator)is.readObject();
        is.close();

        assertEquals(1, mapLoaded.size());
        assertTrue(mapLoaded.containsKey((short) 1));
        assertEquals((byte) 1, mapLoaded.get((short) 1));
    }
}