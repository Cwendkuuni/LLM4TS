package org.apache.commons.cli; // Replace with the actual package name of your tests

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutGetKeyNull() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.get(null));
        assertNull(decorator.put(null, (byte) 0));
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutGetKeyShort() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        Byte putValue = decorator.put((Short) key, value);
        assertSame(value, putValue);
        Byte getValue = decorator.get(key);
        assertSame(value, getValue);
        short unwrappedKey = decorator.unwrapKey(key);
        assertTrue(unwrappedKey == key);
    }

    @Test
    public void testPutGetKeyShortMultiple() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key1 = Short.MIN_VALUE;
        byte value1 = (byte) 0x01;
        Byte putValue1 = decorator.put((Short) key1, value1);
        assertSame(value1, putValue1);

        short key2 = Short.MIN_VALUE + 1;
        byte value2 = (byte) 0x02;
        Byte putValue2 = decorator.put((Short) key2, value2);
        assertSame(value2, putValue2);

        Byte getValue1 = decorator.get(key1);
        assertSame(value1, getValue1);

        Byte getValue2 = decorator.get(key2);
        assertSame(value2, getValue2);

        Map<Short, Byte> map = new TShortByteMapImpl();
        map.put(key1, value1);
        map.put(key2, value2);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPutGetValueNull() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.get(Short.MIN_VALUE));
        assertNull(decorator.put((Short) Short.MIN_VALUE, null));
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutGetValueByte() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        Byte putValue = decorator.put((Short) key, value);
        assertSame(value, putValue);
        Byte getValue = decorator.get(key);
        assertSame(value, getValue);
    }

    @Test
    public void testPutGetValueByteMultiple() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        short key1 = Short.MIN_VALUE;
        byte value1 = (byte) 0x01;
        Byte putValue1 = decorator.put((Short) key1, value1);
        assertSame(value1, putValue1);

        short key2 = Short.MIN_VALUE + 1;
        byte value2 = (byte) 0x02;
        Byte putValue2 = decorator.put((Short) key2, value2);
        assertSame(value2, putValue2);

        Byte getValue1 = decorator.get(key1);
        assertSame(value1, getValue1);

        Byte getValue2 = decorator.get(key2);
        assertSame(value2, getValue2);

        Map<Short, Byte> map = new TShortByteMapImpl();
        map.put(key1, value1);
        map.put(key2, value2);
        assertEquals(map, decorator.getMap());

        Byte[] values = new Byte[] { value1, value2 };
        Byte[] getValues = new Byte[2];
        int i = 0;
        for (Short key : decorator.keySet()) {
            getValues[i++] = decorator.get(key);
        }
        assertArrayEquals(values, getValues);
    }

    @Test
    public void testPutGetValueByteArray() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        short[] keys = new short[] { Short.MIN_VALUE, Short.MIN_VALUE + 1, Short.MIN_VALUE + 2 };
        byte[] values = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 };

        for (int i = 0; i < keys.length; i++) {
            Byte putValue = decorator.put((Short) keys[i], (Byte) values[i]);
            assertSame(values[i], putValue);
        }

        Byte[] getValues = new Byte[keys.length];
        int j = 0;
        for (Short key : decorator.keySet()) {
            getValues[j++] = decorator.get(key);
        }
        assertArrayEquals(values, getValues);
    }

    @Test
    public void testPutGetValueByteArrayWrapper() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        short[] keys = new short[] { Short.MIN_VALUE, Short.MIN_VALUE + 1, Short.MIN_VALUE + 2 };
        Byte[] wrappedValues = new Byte[keys.length];

        for (int i = 0; i < keys.length; i++) {
            Byte putValue = decorator.put((Short) keys[i], wrappedValues[i] = (byte) (i + 1));
            assertSame(wrappedValues[i], putValue);
        }

        Byte[] getValues = new Byte[keys.length];
        int j = 0;
        for (Short key : decorator.keySet()) {
            getValues[j++] = decorator.get(key);
        }
        assertArrayEquals(wrappedValues, getValues);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        decorator.put((Short) key, value);
        assertTrue(decorator.containsKey(key));
        decorator.clear();
        assertFalse(decorator.containsKey(key));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        decorator.put((Short) key, value);
        assertTrue(decorator.containsKey(key));
        Byte removedValue = decorator.remove(key);
        assertSame(value, removedValue);
        assertFalse(decorator.containsKey(key));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        decorator.put((Short) key, value);
        assertTrue(decorator.containsValue(value));
        decorator.clear();
        assertFalse(decorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        decorator.put((Short) key, value);
        assertTrue(decorator.containsKey(key));
        decorator.clear();
        assertFalse(decorator.containsKey(key));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        decorator.put((Short) key, value);
        assertEquals(1, decorator.size());
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());

        short key = Short.MIN_VALUE;
        byte value = (byte) 0x01;
        decorator.put((Short) key, value);
        assertFalse(decorator.isEmpty());

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Map<Short, Byte> map = new TShortByteMapImpl();
        map.put((short) 0, (byte) 0x01);
        map.put((short) 1, (byte) 0x02);
        map.put((short) 2, (byte) 0x03);

        decorator.putAll(map);

        Byte[] getValues = new Byte[3];
        int j = 0;
        for (Short key : decorator.keySet()) {
            getValues[j++] = decorator.get(key);
        }
        assertArrayEquals(map.toArray(), getValues);
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Map<Short, Byte> map = new TShortByteMapImpl();
        map.put((short) 0, (byte) 0x01);
        map.put((short) 1, (byte) 0x02);
        map.put((short) 2, (byte) 0x03);

        decorator.putAll(map);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        int size = entrySet.size();
        assertTrue(size == map.size());

        Entry<Short, Byte>[] entries = new Entry[size];
        int i = 0;
        for (Entry<Short, Byte> entry : entrySet) {
            entries[i++] = entry;
        }

        for (int index = 0; index < size; index++) {
            Entry<Short, Byte> entry = map.entrySet().iterator().next();
            assertEquals(entry.getKey(), entries[index].getKey());
            assertEquals(entry.getValue(), entries[index].getValue());
        }
    }

    @Test
    public void testExternalizable() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 0, (byte) 0x01);
        decorator.put((short) 1, (byte) 0x02);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInput);

        TShortByteMapDecorator restoredDecorator = new TShortByteMapDecorator();
        restoredDecorator.readExternal(in);

        Set<Entry<Short, Byte>> entrySet = restoredDecorator.entrySet();
        int size = entrySet.size();
        assertEquals(size, decorator.size());

        Entry<Short, Byte>[] entries = new Entry[size];
        int i = 0;
        for (Entry<Short, Byte> entry : entrySet) {
            entries[i++] = entry;
        }

        for (int index = 0; index < size; index++) {
            Entry<Short, Byte> entry = decorator.entrySet().iterator().next();
            assertEquals(entry.getKey(), entries[index].getKey());
            assertEquals(entry.getValue(), entries[index].getValue());
        }

        byteInput.close();
    }

    @Test
    public void testCloneable() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 0, (byte) 0x01);
        decorator.put((short) 1, (byte) 0x02);

        TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator) decorator.clone();

        Set<Entry<Short, Byte>> entrySet = clonedDecorator.entrySet();
        int size = entrySet.size();
        assertEquals(size, decorator.size());

        Entry<Short, Byte>[] entries = new Entry[size];
        int i = 0;
        for (Entry<Short, Byte> entry : entrySet) {
            entries[i++] = entry;
        }

        for (int index = 0; index < size; index++) {
            Entry<Short, Byte> entry = decorator.entrySet().iterator().next();
            assertEquals(entry.getKey(), entries[index].getKey());
            assertEquals(entry.getValue(), entries[index].getValue());
        }
    }
}