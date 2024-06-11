package org.apache.commons.cli; // Assuming you want to test TShortByteMapDecorator in the same package as Util for the sake of simplicity.

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testConstructor() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        Byte putValue = decorator.put(key, value);
        assertEquals(value, putValue);
        assertEquals(value, decorator.get(key));
        assertEquals(1, decorator.size());

        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        Byte putValue2 = decorator.put(key2, value2);
        assertEquals(value2, putValue2);
        assertEquals(value2, decorator.get(key2));
        assertEquals(2, decorator.size());

        assertNull(decorator.get(Short.valueOf((short)0)));
    }

    @Test
    public void testGet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);

        assertEquals(value, decorator.get(key));
        assertNull(decorator.get(Short.valueOf((short)0)));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);

        decorator.clear();
        assertTrue(decorator.isEmpty());

        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key2, value2);

        assertEquals(value2, decorator.get(key2));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        Byte removedValue = decorator.remove(key);
        assertEquals(value, removedValue);
        assertNull(decorator.get(key));
        assertTrue(decorator.isEmpty());

        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key2, value2);
        assertEquals(value2, decorator.get(key2));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(1, entrySet.size());
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);

        assertTrue(decorator.containsValue(value));
        assertFalse(decorator.containsValue(Byte.valueOf((byte)2)));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertFalse(decorator.containsKey(Short.valueOf((short)0)));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key2, value2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key2, value2);

        assertFalse(decorator.isEmpty());

        decorator.clear();

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Map<Short, Byte> map2 = new HashMap<>();
        map2.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
        map2.put(Short.valueOf((short)2), Byte.valueOf((byte)2));

        decorator.putAll(map2);

        assertEquals(2, decorator.size());
        assertEquals(Byte.valueOf((byte)1), decorator.get(Short.valueOf((short)1)));
        assertEquals(Byte.valueOf((byte)2), decorator.get(Short.valueOf((short)2)));
    }

    @Test
    public void testWriteExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);

        decorator.writeExternal(objectOutput);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)objectInput.readObject();

        assertEquals(decorator.getMap(), deserializedDecorator.getMap());
        assertEquals(decorator.size(), deserializedDecorator.size());
        assertEquals(decorator.get(key), deserializedDecorator.get(key));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);

        decorator.writeExternal(objectOutput);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));

        TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
        deserializedDecorator.readExternal(objectInput);

        assertEquals(decorator.getMap(), deserializedDecorator.getMap());
        assertEquals(decorator.size(), deserializedDecorator.size());
        assertEquals(decorator.get(key), deserializedDecorator.get(key));
    }
}