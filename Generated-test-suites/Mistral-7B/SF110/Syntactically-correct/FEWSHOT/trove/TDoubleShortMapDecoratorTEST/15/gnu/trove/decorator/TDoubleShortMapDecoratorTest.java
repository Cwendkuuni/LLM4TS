package org.apache.commons.cli; // Replace with the actual package name of your test class

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testInitialization() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNotNull(map);
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(new TDoubleShortMap());
        assertNotNull(decoratedMap);
        assertEquals(0, decoratedMap.size());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(null, map.put(1.0, (short) 1));
        assertEquals((short) 1, map.get(1.0));
        assertEquals(1, map.size());

        Double key = 2.0;
        Short value = (short) 2;
        assertEquals(null, map.put(key, value));
        assertEquals(value, map.get(key));
        assertEquals(2, map.size());

        Double existingKey = 1.0;
        Short existingValue = (short) 1;
        assertEquals(existingValue, map.put(existingKey, existingValue));
        assertEquals(existingValue, map.get(existingKey));
        assertEquals(2, map.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(null, (short) 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutWithNullValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, null);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        assertEquals((short) 1, map.get(1.0));
        assertEquals(null, map.get(2.0));
    }

    @Test
    public void testGetWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(null, map.get(null));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertEquals((short) 1, map.remove(1.0));
        assertNull(map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
        assertEquals(1, map.size());
    }

    @Test
    public void testRemoveWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertNull(map.remove(null));
        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
        assertEquals(2, map.size());
    }

    @Test
    public void testRemoveWithNonExistentKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertNull(map.remove(3.0));
        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
        assertEquals(2, map.size());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short) 1, entry.getValue());

        entry = iterator.next();
        assertEquals(2.0, entry.getKey());
        assertEquals((short) 2, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertTrue(map.containsValue((short) 1));
        assertTrue(map.containsValue((short) 2));
        assertFalse(map.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsKey(2.0));
        assertFalse(map.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());

        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());

        map.put(1.0, (short) 1);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map1 = new TDoubleShortMapDecorator();
        map1.put(1.0, (short) 1);
        map1.put(2.0, (short) 2);

        TDoubleShortMapDecorator map2 = new TDoubleShortMapDecorator();
        map2.put(3.0, (short) 3);
        map2.put(4.0, (short) 4);

        map1.putAll(map2);
        assertEquals(4, map1.size());

        Iterator<Entry<Double, Short>> iterator = map1.entrySet().iterator();
        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short) 1, entry.getValue());

        entry = iterator.next();
        assertEquals(2.0, entry.getKey());
        assertEquals((short) 2, entry.getValue());

        entry = iterator.next();
        assertEquals(3.0, entry.getKey());
        assertEquals((short) 3, entry.getValue());

        entry = iterator.next();
        assertEquals(4.0, entry.getKey());
        assertEquals((short) 4, entry.getValue());
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator() {
            @Override
            protected Double wrapKey(double k) {
                return new Double(k);
            }
        };
        map.put(1, (short) 1);
        assertEquals(new Double(1), map.keySet().iterator().next());
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator() {
            @Override
            protected Short wrapValue(short v) {
                return new Short((short) (v + 1));
            }
        };
        map.put(1, (short) 1);
        assertEquals(new Short((short) 2), map.get(1));
    }

    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);

        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        objStream.writeObject(map);
        objStream.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInput = new ObjectInputStream(byteInput);

        TDoubleShortMapDecorator deserializedMap = (TDoubleShortMapDecorator) objInput.readObject();
        objInput.close();

        assertEquals(map.size(), deserializedMap.size());
        Iterator<Entry<Double, Short>> mapIterator = map.entrySet().iterator();
        Iterator<Entry<Double, Short>> deserializedMapIterator = deserializedMap.entrySet().iterator();

        while (mapIterator.hasNext() && deserializedMapIterator.hasNext()) {
            Entry<Double, Short> mapEntry = mapIterator.next();
            Entry<Double, Short> deserializedMapEntry = deserializedMapIterator.next();
            assertEquals(mapEntry.getKey(), deserializedMapEntry.getKey());
            assertEquals(mapEntry.getValue(), deserializedMapEntry.getValue());
        }
    }
}