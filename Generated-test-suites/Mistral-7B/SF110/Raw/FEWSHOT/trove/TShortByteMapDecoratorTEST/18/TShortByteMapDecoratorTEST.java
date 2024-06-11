package org.apache.commons.cli; // Replace with the actual package name of TShortByteMapDecorator

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import gnu.trove.decorator.TShortByteMap;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMap.Entry;
import gnu.trove.map.TShortByteMap.MapEntry;
import gnu.trove.map.TShortByteMap.NoEntryKey;
import gnu.trove.map.TShortByteMap.NoEntryValue;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testCreationWithMap() {
        TShortByteMap map = mock(TShortByteMap.class);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyInPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) null, (Byte) 'V');
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueInPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) 1, (Byte) null);
    }

    @Test
    public void testPut() {
        Short expectedKey = Short.valueOf("1");
        Byte expectedValue = Byte.valueOf("2");

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Byte actualValue = mapDecorator.put(expectedKey, expectedValue);
        assertEquals(expectedValue, actualValue);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testGet() {
        Short expectedKey = Short.valueOf("1");
        Byte expectedValue = Byte.valueOf("2");

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put(expectedKey, expectedValue);

        Byte actualValue = mapDecorator.get(expectedKey);
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyInGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.get(null);
    }

    @Test
    public void testGetNullValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) 1, (Byte) null);

        Byte actualValue = mapDecorator.get((Short) 1);
        assertNull(actualValue);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) 1, (Byte) 'A');
        mapDecorator.clear();

        assertFalse(mapDecorator.containsKey((Short) 1));
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testRemove() {
        Short keyToRemove = Short.valueOf("1");
        Byte valueToRemove = Byte.valueOf("A");
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put(keyToRemove, valueToRemove);

        Byte removedValue = mapDecorator.remove(keyToRemove);
        assertEquals(valueToRemove, removedValue);
        assertFalse(mapDecorator.containsKey(keyToRemove));
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.remove(null);

        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testRemoveNonExistingKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.remove((Short) 1);

        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) 1, (Byte) 'A');

        assertTrue(mapDecorator.containsValue(Byte.valueOf("A")));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) 1, (Byte) 'A');

        assertTrue(mapDecorator.containsKey((Short) 1));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) 1, (Byte) 'A');
        mapDecorator.put((Short) 2, (Byte) 'B');

        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        assertTrue(mapDecorator.isEmpty());

        mapDecorator.put((Short) 1, (Byte) 'A');

        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Short[] keys = {Short.valueOf("1"), Short.valueOf("2")};
        Byte[] values = {Byte.valueOf("A"), Byte.valueOf("B")};

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        Map<Short, Byte> mapToAdd = new HashMap<>();
        mapToAdd.putAll(Arrays.asList(pairs(keys, values)));

        mapDecorator.putAll(mapToAdd);

        assertEquals(2, mapDecorator.size());
        assertTrue(mapDecorator.containsKey(keys[0]));
        assertTrue(mapDecorator.containsKey(keys[1]));
        assertTrue(mapDecorator.containsValue(values[0]));
        assertTrue(mapDecorator.containsValue(values[1]));
    }

    @Test
    public void testEntrySet() {
        Short[] keys = {Short.valueOf("1"), Short.valueOf("2")};
        Byte[] values = {Byte.valueOf("A"), Byte.valueOf("B")};

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        Map<Short, Byte> mapToAdd = new HashMap<>();
        mapToAdd.putAll(Arrays.asList(pairs(keys, values)));

        mapDecorator.putAll(mapToAdd);

        Set<Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Short, Byte>> it = entrySet.iterator();
        Entry<Short, Byte> entry1 = it.next();
        Entry<Short, Byte> entry2 = it.next();

        assertEquals(keys[0], entry1.getKey());
        assertEquals(values[0], entry1.getValue());
        assertEquals(keys[1], entry2.getKey());
        assertEquals(values[1], entry2.getValue());
    }

    @Test
    public void testContains() {
        Short[] keys = {Short.valueOf("1"), Short.valueOf("2")};
        Byte[] values = {Byte.valueOf("A"), Byte.valueOf("B")};

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        Map<Short, Byte> mapToAdd = new HashMap<>();
        mapToAdd.putAll(Arrays.asList(pairs(keys, values)));

        mapDecorator.putAll(mapToAdd);

        assertTrue(mapDecorator.contains(new Entry<Short, Byte>(keys[0], values[0])));
        assertTrue(mapDecorator.contains(new Entry<Short, Byte>(keys[1], values[1])));
        assertFalse(mapDecorator.contains(new Entry<Short, Byte>(Short.valueOf("3"), Byte.valueOf("C"))));
    }

    @Test
    public void testRemoveEntry() {
        Short keyToRemove = Short.valueOf("1");
        Byte valueToRemove = Byte.valueOf("A");

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put(keyToRemove, valueToRemove);

        Entry<Short, Byte> entryToRemove = new Entry<Short, Byte>(keyToRemove, valueToRemove);
        mapDecorator.remove(entryToRemove);

        assertFalse(mapDecorator.containsKey(keyToRemove));
        assertEquals(0, mapDecorator.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorEmptyMap() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        Iterator<Entry<Short, Byte>> iterator = mapDecorator.entrySet().iterator();
        iterator.next();

        iterator.next(); // This should throw NoSuchElementException
    }

    @Test
    public void testIteratorNonEmptyMap() {
        Short[] keys = {Short.valueOf("1"), Short.valueOf("2")};
        Byte[] values = {Byte.valueOf("A"), Byte.valueOf("B")};

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        Map<Short, Byte> mapToAdd = new HashMap<>();
        mapToAdd.putAll(Arrays.asList(pairs(keys, values)));

        mapDecorator.putAll(mapToAdd);

        Iterator<Entry<Short, Byte>> iterator = mapDecorator.entrySet().iterator();

        Entry<Short, Byte> entry1 = iterator.next();
        assertEquals(keys[0], entry1.getKey());
        assertEquals(values[0], entry1.getValue());

        Entry<Short, Byte> entry2 = iterator.next();
        assertEquals(keys[1], entry2.getKey());
        assertEquals(values[1], entry2.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((Short) 1, (Byte) 'A');

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        mapDecorator.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TShortByteMapDecorator mapDecoratorFromStream = (TShortByteMapDecorator) objectInput.readObject();
        objectInput.close();

        Byte actualValue = mapDecoratorFromStream.get((Short) 1);
        assertEquals(Byte.valueOf("A"), actualValue);
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TShortByteMapDecorator originalMap = new TShortByteMapDecorator();
        originalMap.put((Short) 1, (Byte) 'A');

        TShortByteMapDecorator clone = (TShortByteMapDecorator) originalMap.clone();

        Byte actualValue = clone.get((Short) 1);
        assertEquals(Byte.valueOf("A"), actualValue);
    }

    private static <K, V> Map.Entry<K, V>[] pairs(K[] keys, V[] values) {
        Map.Entry<K, V>[] entries = new Map.Entry[keys.length];
        for (int i = 0; i < keys.length; i++) {
            entries[i] = new AbstractMap.SimpleEntry<>(keys[i], values[i]);
        }
        return entries;
    }
}