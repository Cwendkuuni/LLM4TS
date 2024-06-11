package com.example.junit4; // replace with your testing package name if needed

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.view.TShortByteMapBackingMap;
import gnu.trove.map.view.TShortByteOpenHashMap;
import gnu.trove.map.view.TShortByteView;

public class TShortByteMapDecoratorTest {

    // Test methods for constructor and getters

    @Test
    public void testEmptyConstructor() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(Collections.emptyMap(), map);
    }

    @Test
    public void testMapConstructor() {
        TShortByteMap map = new TShortByteOpenHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }

    // Test methods for put()

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.put((Short)null, (Byte)null));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.put((Short)1, null));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutExistingKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte existingValue = (Byte)1;
        map.put((Short)1, existingValue);
        Byte newValue = (Byte)2;
        assertEquals(existingValue, map.put((Short)1, newValue));
        assertEquals(newValue, map.get((Short)1));
    }

    @Test
    public void testPutNewKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte newValue = (Byte)2;
        assertEquals(null, map.put((Short)1, (Byte)1));
        assertEquals((Byte)1, map.get((Short)1));
        assertEquals(newValue, map.put((Short)2, newValue));
        assertEquals(newValue, map.get((Short)2));
    }

    // Test methods for get()

    @Test
    public void testGetNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.get(null));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testGetNonExistentKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((Short)1, (Byte)1);
        assertNull(map.get((Short)2));
    }

    @Test
    public void testGetExistentKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = (Byte)1;
        map.put((Short)1, value);
        assertEquals(value, map.get((Short)1));
    }

    // Test methods for clear()

    @Test
    public void testClearEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testClearMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((Short)1, (Byte)1);
        map.put((Short)2, (Byte)2);
        assertEquals(2, map.size());
        map.clear();
        assertTrue(map.isEmpty());
    }

    // Test methods for remove()

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((Short)1, (Byte)1);
        assertNull(map.remove(null));
        assertEquals(1, map.size());
    }

    @Test
    public void testRemoveNonExistentKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((Short)1, (Byte)1);
        assertNull(map.remove((Short)2));
        assertEquals(1, map.size());
    }

    @Test
    public void testRemoveExistentKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = (Byte)1;
        map.put((Short)1, value);
        Byte removedValue = map.remove((Short)1);
        assertEquals(value, removedValue);
        assertTrue(map.isEmpty());
    }

    // Test methods for entrySet()

    @Test
    public void testEmptyEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testNonEmptyEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((Short)1, (Byte)1);
        map.put((Short)2, (Byte)2);
        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals((Short)1, entry.getKey());
        assertEquals((Byte)1, entry.getValue());
        entry = iterator.next();
        assertEquals((Short)2, entry.getKey());
        assertEquals((Byte)2, entry.getValue());
    }

    // Test methods for containsValue()

    @Test
    public void testContainsValueNull() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertFalse(map.containsValue(null));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsValueTrue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = (Byte)1;
        map.put((Short)1, value);
        assertTrue(map.containsValue(value));
    }

    @Test
    public void testContainsValueFalse() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = (Byte)1;
        map.put((Short)1, value1);
        Byte value2 = (Byte)2;
        assertFalse(map.containsValue(value2));
    }

    // Test methods for containsKey()

    @Test
    public void testContainsKeyNull() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertFalse(map.containsKey(null));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsKeyTrue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = (Byte)1;
        map.put((Short)1, value);
        assertTrue(map.containsKey((Short)1));
    }

    @Test
    public void testContainsKeyFalse() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = (Byte)1;
        map.put((Short)1, value1);
        Byte value2 = (Byte)2;
        assertFalse(map.containsKey((Short)2));
    }

    // Test methods for size()

    @Test
    public void testSizeEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testSizeNonEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = (Byte)1;
        map.put((Short)1, value);
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }

    // Test methods for isEmpty()

    @Test
    public void testIsEmptyTrue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testIsEmptyFalse() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = (Byte)1;
        map.put((Short)1, value);
        assertFalse(map.isEmpty());
    }

    // Test methods for putAll()

    @Test
    public void testPutAllEmptyMap() {
        TShortByteMapDecorator target = new TShortByteMapDecorator();
        TShortByteMap source = new TShortByteOpenHashMap();
        target.putAll(source);
        assertTrue(target.isEmpty());
    }

    @Test
    public void testPutAllNonEmptyMap() {
        TShortByteMapDecorator target = new TShortByteMapDecorator();
        TShortByteMap source = new TShortByteOpenHashMap();
        source.put((Short)1, (Byte)1);
        target.putAll(source);
        assertEquals(1, target.size());
        assertTrue(target.containsKey((Short)1));
        assertEquals((Byte)1, target.get((Short)1));
    }

    // Test methods for externalization

    @Test
    public void testReadExternalEmptyMap() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        objectOutput.writeObject(map);
        objectOutput.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TShortByteMapDecorator readMap = (TShortByteMapDecorator)objectInput.readObject();
        objectInput.close();
        assertEquals(Collections.emptyMap(), readMap);
    }

    @Test
    public void testReadExternalMap() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        TShortByteMap map = new TShortByteOpenHashMap();
        map.put((Short)1, (Byte)1);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        objectOutput.writeByte(0);
        objectOutput.writeObject(decorator.getMap());
        objectOutput.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TShortByteMapDecorator readDecorator = (TShortByteMapDecorator)objectInput.readObject();
        objectInput.close();
        assertEquals(map, readDecorator.getMap());
    }

    @Test
    public void testWriteExternalMap() {
        TShortByteMap map = new TShortByteOpenHashMap();
        map.put((Short)1, (Byte)1);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        decorator.writeExternal(objectOutput);
        objectOutput.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TShortByteMapDecorator readDecorator = (TShortByteMapDecorator)objectInput.readObject();
        objectInput.close();
        assertEquals(map, readDecorator.getMap());
    }

    // Test methods for custom iterator

    @Test
    public void testIteratorEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        TShortByteIterator iterator = map.entrySet().iterator();
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testIteratorNonEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = (Byte)1;
        map.put((Short)1, value);
        TShortByteIterator iterator = map.entrySet().iterator();
        assertTrue(iterator.hasNext());
        Entry<Short, Byte> entry = iterator.next();
        assertEquals((Short)1, entry.getKey());
        assertEquals(value, entry.getValue());
        assertTrue(iterator.hasNext());
        entry = iterator.next();
        // There is no next entry
        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException e) {
            // expected
        }
    }
}