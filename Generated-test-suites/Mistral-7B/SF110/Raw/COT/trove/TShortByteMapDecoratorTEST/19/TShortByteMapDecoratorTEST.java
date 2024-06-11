package com.example.junit4; // replace with the actual package name of your JUnit test file

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;

import static gnu.trove.decorator.TShortByteMapDecorator.serialVersionUID;
import static gnu.trove.decorator.TShortByteMapDecorator.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testConstructor() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNotNull(decorator);
    }

    @Test
    public void testConstructorWithMap() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte value = Byte.valueOf("2");
        Byte putValue = decorator.put(key, value);
        assertEquals(value, putValue);
        assertEquals(key, decorator.getMap().keySet().iterator().next());
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte value = Byte.valueOf("2");
        decorator.put(key, value);
        Byte retrievedValue = decorator.get(key);
        assertEquals(value, retrievedValue);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.get(null);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("1");
        decorator.put(key1, value1);
        Short key2 = Short.valueOf("2");
        Byte value2 = Byte.valueOf("2");
        decorator.put(key2, value2);
        decorator.clear();
        assertFalse(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("1");
        decorator.put(key1, value1);
        Byte removedValue = decorator.remove(key1);
        assertEquals(value1, removedValue);
        assertFalse(decorator.containsKey(key1));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.remove(null);
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf("1");
        Byte value1 = Byte.valueOf("1");
        decorator.put(key1, value1);
        Short key2 = Short.valueOf("2");
        Byte value2 = Byte.valueOf("2");
        decorator.put(key2, value2);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte value = Byte.valueOf("1");
        decorator.put((short) 1, value);
        decorator.put((short) 2, (byte) 2);
        assertTrue(decorator.containsValue(value));
        assertFalse(decorator.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        decorator.put(key, (byte) 1);
        assertTrue(decorator.containsKey(key));
        assertFalse(decorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
        decorator.put((short) 1, (byte) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        decorator.putAll(map);
        assertEquals(map, decorator.getMap());
        assertEquals(2, decorator.size());
    }

    @Test
    public void testExternalizable() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte value = Byte.valueOf("1");
        decorator.put(key, value);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInput);
        TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
        deserializedDecorator.readExternal(in);
        assertEquals(decorator.getMap(), deserializedDecorator.getMap());
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte value = Byte.valueOf("1");
        decorator.put(key, value);
        TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();
        assertEquals(decorator.getMap(), clonedDecorator.getMap());
    }
}