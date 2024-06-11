package org.apache.commons.cli; // replaced with the package name of TDoubleShortMapDecorator for testing purposes

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(null, (short) 1);
        assertEquals(0, map.size());
        assertFalse(map.containsKey(null));
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        map.put(key, null);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertNull(map.get(key));
    }

    @Test
    public void testPutSameKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value1 = 1;
        Short value2 = 2;
        map.put(key, value1);
        map.put(key, value2);
        assertEquals(1, map.size());
        assertEquals(value2, map.get(key));
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        assertNull(map.get(null));
    }

    @Test
    public void testGetKeyNullValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, null);
        assertNull(map.get(1.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = (short) 1;
        map.put(key, value);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertEquals(value, map.get(key));
        map.remove(key);
        assertEquals(0, map.size());
        assertFalse(map.containsKey(key));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertEquals(2, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertFalse(map.containsKey(1.0));
        assertFalse(map.containsKey(2.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        Set<Entry<Double, Short>> set = map.entrySet();
        assertEquals(2, set.size());
        Iterator<Entry<Double, Short>> it = set.iterator();
        Entry<Double, Short> entry = it.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short) 1, entry.getValue());
        entry = it.next();
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
    public void testIterationOrder() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(3.0, (short) 3);
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        TDoubleShortIterator it = map._map.iterator();
        Double key1 = it.key();
        Short value1 = it.value();
        it.advance();
        Double key2 = it.key();
        Short value2 = it.value();
        it.advance();
        Double key3 = it.key();
        Short value3 = it.value();
        assertEquals(1.0, key1);
        assertEquals((short) 1, value1);
        assertEquals(2.0, key2);
        assertEquals((short) 2, value2);
        assertEquals(3.0, key3);
        assertEquals((short) 3, value3);
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        map.writeExternal(out);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        TDoubleShortMapDecorator mapFromSerialized = new TDoubleShortMapDecorator();
        mapFromSerialized.readExternal(in);
        assertEquals(map.size(), mapFromSerialized.size());
        Iterator<Entry<Double, Short>> it = mapFromSerialized.entrySet().iterator();
        Entry<Double, Short> entry;
        while (it.hasNext()) {
            entry = it.next();
            assertEquals(map.get(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        TDoubleShortMapDecorator clonedMap = (TDoubleShortMapDecorator) map.clone();
        assertEquals(map.size(), clonedMap.size());
        Iterator<Entry<Double, Short>> it = clonedMap.entrySet().iterator();
        Entry<Double, Short> entry;
        while (it.hasNext()) {
            entry = it.next();
            assertEquals(map.get(entry.getKey()), entry.getValue());
        }
    }
}