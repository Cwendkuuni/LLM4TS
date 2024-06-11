package org.apache.commons.cli; // Assuming you want to test the TShortByteMapDecorator class in the context of the org.apache.commons.cli package

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 2);
        decorator.put(key, value);
        assertTrue(decorator.containsKey(key));
        assertEquals(value, decorator.get(key));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testPutOverwrite() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte oldValue = Byte.valueOf((byte) 1);
        Byte newValue = Byte.valueOf((byte) 2);
        decorator.put(key, oldValue);
        decorator.put(key, newValue);
        assertTrue(decorator.containsKey(key));
        assertEquals(newValue, decorator.get(key));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte value = Byte.valueOf((byte) 1);
        assertNull(decorator.put(null, value));
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        decorator.put(key, null);
        assertNull(decorator.get(key));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testGetNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.get(null));
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testGetKeyNullValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        assertNull(decorator.get(null));
        assertEquals(key, decorator.get(key));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key1, value1);
        assertTrue(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key, value1);
        assertTrue(decorator.containsValue(value1));
        assertFalse(decorator.containsValue(value2));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(0, decorator.size());
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 2);
        decorator.put(key, value);
        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 2);
        decorator.put(key, value);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        assertEquals(2, decorator.size());
        decorator.clear();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        assertEquals(2, decorator.size());
        Byte removedValue = decorator.remove(key1);
        assertTrue(removedValue != null);
        assertEquals(value1, removedValue);
        assertEquals(1, decorator.size());
        assertNull(decorator.remove(key1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.remove(null);
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        boolean containsEntry1 = entrySet.contains(new AbstractMap.SimpleEntry<>(key1, value1));
        assertTrue(containsEntry1);
        boolean containsEntry2 = entrySet.contains(new AbstractMap.SimpleEntry<>(key2, value2));
        assertTrue(containsEntry2);
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        assertEquals(key1, iterator.next().getKey());
        assertEquals(value1, iterator.next().getValue());
        assertEquals(key2, iterator.next().getKey());
        assertEquals(value2, iterator.next().getValue());
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 2);
        decorator.put(key, value);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TShortByteMapDecorator decoratedMap = (TShortByteMapDecorator) in.readObject();
        assertEquals(decorator.getMap(), decoratedMap.getMap());
        assertEquals(decorator.size(), decoratedMap.size());
        assertEquals(decorator.containsKey(key), decoratedMap.containsKey(key));
        assertEquals(decorator.get(key), decoratedMap.get(key));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 2);
        decorator.put(key, value);
        TShortByteMapDecorator clonedMap = (TShortByteMapDecorator) decorator.clone();
        assertEquals(decorator.getMap(), clonedMap.getMap());
        assertEquals(decorator.size(), clonedMap.size());
        assertEquals(decorator.containsKey(key), clonedMap.containsKey(key));
        assertEquals(decorator.get(key), clonedMap.get(key));
    }

}