package org.apache.commons.cli.test; // Assuming you want to test the TShortByteMapDecorator class in the same package as UtilTest

import static gnu.trove.map.TShortByteMaps.EMPTY;
import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = decorator.getMap();
        assertEquals(EMPTY, map);
    }

    @Test
    public void testPutGetAndClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        Map<Short, Byte> map = decorator.getMap();
        Byte gotValue = map.get(key);
        assertEquals(value, gotValue);
        decorator.clear();
        map = decorator.getMap();
        assertNull(map.get(key));
    }

    @Test
    public void testPutAndGetMultipleEntries() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        Map<Short, Byte> map = decorator.getMap();
        Byte gotValue1 = map.get(key1);
        Byte gotValue2 = map.get(key2);
        assertEquals(value1, gotValue1);
        assertEquals(value2, gotValue2);
    }

    @Test
    public void testSizeAndIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(0, decorator.size());
        decorator.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1));
        assertEquals(1, decorator.size());
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testContainsKeyAndValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        decorator.clear();
        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key2, value2);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(Map.entry(key1, value1)));
        assertTrue(entrySet.contains(Map.entry(key2, value2)));
        decorator.clear();
        entrySet = decorator.entrySet();
        assertEquals(0, entrySet.size());
    }

    @Test
    public void testContainsValueMultipleEntries() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(Short.valueOf((short) 1), value);
        decorator.put(Short.valueOf((short) 2), value);
        assertTrue(decorator.containsValue(value));
        decorator.clear();
        assertFalse(decorator.containsValue(value));
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1));
        map.put(Short.valueOf((short) 2), Byte.valueOf((byte) 2));
        decorator.putAll(map);
        Map<Short, Byte> gotMap = decorator.getMap();
        assertEquals(map, gotMap);
    }

    @Test
    public void testIteration() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        Iterator<Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());
        iterator.hasNext();
        entry = iterator.next();
        assertEquals(key2, entry.getKey());
        assertEquals(value2, entry.getValue());
        assertFalse(iterator.hasNext());
        decorator.clear();
        iterator = decorator.entrySet().iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAndWriteExternalForm() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1));
        decorator.writeExternal(out);
        out.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInputStream(inputStream);
        TShortByteMapDecorator readDecorator = new TShortByteMapDecorator();
        readDecorator.readExternal(in);
        in.close();
        Map<Short, Byte> map = readDecorator.getMap();
        Byte gotValue = map.get(Short.valueOf((short) 1));
        assertEquals(Byte.valueOf((byte) 1), gotValue);
    }
}