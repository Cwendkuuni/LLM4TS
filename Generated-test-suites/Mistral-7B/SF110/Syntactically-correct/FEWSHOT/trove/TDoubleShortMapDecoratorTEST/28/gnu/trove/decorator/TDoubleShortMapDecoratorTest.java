package org.apache.commons.cli; // This is just to keep the test class in the same package as the tested class

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testConstructor() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortShortMapDecorator(map);
        assertEquals(map, decoratedMap.getMap());
    }

    @Test
    public void testPut() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        Double key = 1.0;
        Short oldValue = decoratedMap.put(key, value);
        assertNull(oldValue);
        assertEquals(value, decoratedMap.get(key));

        key = 2.0;
        value = (Short) 2;
        oldValue = decoratedMap.put(key, value);
        assertNull(oldValue);
        assertEquals(value, decoratedMap.get(key));

        key = 1.0;
        oldValue = decoratedMap.put(key, (Short) 3);
        assertEquals(value, decoratedMap.get(key));
        assertEquals(value, decoratedMap.put(key, (Short) 4));
        assertEquals((Short) 4, decoratedMap.get(key));
        assertEquals(value, oldValue);
    }

    @Test
    public void testGet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        Double key = 1.0;
        assertNull(decoratedMap.get(key));

        decoratedMap.put(key, value);
        assertEquals(value, decoratedMap.get(key));

        key = 2.0;
        decoratedMap.put(key, value);

        assertNull(decoratedMap.get(null));
        assertNull(decoratedMap.get(-1.0));
        assertNull(decoratedMap.get(Double.MAX_VALUE));
        assertNull(decoratedMap.get(Double.MIN_VALUE));

        assertEquals(value, decoratedMap.get(key));
        assertEquals(value, decoratedMap.get(key)); // Testing for idempotency
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        Double key1 = 1.0;
        Double key2 = 2.0;

        decoratedMap.put(key1, value);
        decoratedMap.put(key2, value);

        decoratedMap.clear();

        assertEmpty(decoratedMap);
    }

    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        Double key1 = 1.0;
        Double key2 = 2.0;

        decoratedMap.put(key1, value);
        decoratedMap.put(key2, value);

        Short removedValue = decoratedMap.remove(key1);
        assertEquals(value, removedValue);
        assertNull(decoratedMap.get(key1));

        removedValue = decoratedMap.remove(key2);
        assertNull(removedValue);
        assertEquals(value, decoratedMap.get(key2));

        removedValue = decoratedMap.remove(null);
        assertNull(removedValue);
        assertEquals(value, decoratedMap.get(null));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        Double key1 = 1.0;
        Double key2 = 2.0;

        decoratedMap.put(key1, value);
        decoratedMap.put(key2, value);

        Set<Entry<Double, Short>> entrySet = decoratedMap.entrySet();

        int size = entrySet.size();
        assertTrue(size == 2);

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> entry;

        entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value, entry.getValue());

        entry = iterator.next();
        assertEquals(key2, entry.getKey());
        assertEquals(value, entry.getValue());

        iterator.remove();
        size = entrySet.size();
        assertTrue(size == 1);

        iterator.next();
        iterator.remove();
        size = entrySet.size();
        assertTrue(size == 0);
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value1 = (Short) 1;
        Short value2 = (Short) 2;

        decoratedMap.put(1.0, value1);
        decoratedMap.put(2.0, value2);

        assertTrue(decoratedMap.containsValue(value1));
        assertTrue(decoratedMap.containsValue(value2));

        assertFalse(decoratedMap.containsValue(null));
        assertFalse(decoratedMap.containsValue((Object)1.1));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Double key1 = 1.0;
        Double key2 = 2.0;

        decoratedMap.put(key1, (Short)1);
        decoratedMap.put(key2, (Short)2);

        assertTrue(decoratedMap.containsKey(key1));
        assertTrue(decoratedMap.containsKey(key2));

        assertFalse(decoratedMap.containsKey(null));
        assertFalse(decoratedMap.containsKey(Double.MAX_VALUE));
        assertFalse(decoratedMap.containsKey(Double.MIN_VALUE));
        assertFalse(decoratedMap.containsKey(1.1));
    }

    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        decoratedMap.put(1.0, value);
        decoratedMap.put(2.0, value);
        decoratedMap.put(3.0, value);

        assertEquals(3, decoratedMap.size());

        decoratedMap.clear();
        assertEquals(0, decoratedMap.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        assertTrue(decoratedMap.isEmpty());

        decoratedMap.put(1.0, value);

        assertFalse(decoratedMap.isEmpty());

        decoratedMap.clear();

        assertTrue(decoratedMap.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map1 = new TDoubleShortMap();
        TDoubleShortMap map2 = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap1 = new TDoubleShortMapDecorator(map1);
        TDoubleShortMapDecorator decoratedMap2 = new TDoubleShortMapDecorator(map2);

        Short value = (Short) 1;

        decoratedMap1.put(1.0, value);
        decoratedMap1.put(2.0, value);

        decoratedMap2.put(2.0, value);
        decoratedMap2.put(3.0, value);

        decoratedMap1.putAll(decoratedMap2);

        assertEquals(3, decoratedMap1.size());

        assertTrue(decoratedMap1.containsKey(1.0));
        assertTrue(decoratedMap1.containsKey(2.0));
        assertTrue(decoratedMap1.containsKey(3.0));

        assertFalse(decoratedMap1.containsKey(null));
        assertFalse(decoratedMap1.containsKey(Double.MAX_VALUE));
        assertFalse(decoratedMap1.containsKey(Double.MIN_VALUE));
        assertFalse(decoratedMap1.containsKey(1.1));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        decoratedMap.put(1.0, value);
        decoratedMap.put(2.0, value);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        decoratedMap.writeExternal(out);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TDoubleShortMapDecorator decoratedMap2 = (TDoubleShortMapDecorator) in.readObject();
        in.close();

        assertEquals(decoratedMap.size(), decoratedMap2.size());

        assertTrue(decoratedMap.containsKey(1.0));
        assertTrue(decoratedMap.containsKey(2.0));
        assertTrue(decoratedMap2.containsKey(1.0));
        assertTrue(decoratedMap2.containsKey(2.0));

        assertFalse(decoratedMap.containsKey(null));
        assertFalse(decoratedMap.containsKey(Double.MAX_VALUE));
        assertFalse(decoratedMap.containsKey(Double.MIN_VALUE));
        assertFalse(decoratedMap.containsKey(1.1));

        assertFalse(decoratedMap.equals(decoratedMap2)); // Testing for deep equality
        assertEquals(decoratedMap.hashCode(), decoratedMap2.hashCode()); // Testing for hashCode consistency
    }

    @Test
    public void testCloneable() throws CloneNotSupportException {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);

        Short value = (Short) 1;

        decoratedMap.put(1.0, value);
        decoratedMap.put(2.0, value);

        TDoubleShortMapDecorator clonedMap = (TDoubleShortMapDecorator) decoratedMap.clone();

        assertEquals(decoratedMap.size(), clonedMap.size());

        assertTrue(decoratedMap.containsKey(1.0));
        assertTrue(decoratedMap.containsKey(2.0));
        assertTrue(clonedMap.containsKey(1.0));
        assertTrue(clonedMap.containsKey(2.0));

        assertFalse(decoratedMap.equals(clonedMap)); // Testing for deep equality
        assertEquals(decoratedMap.hashCode(), clonedMap.hashCode()); // Testing for hashCode consistency
    }

    private void assertEmpty(Map<Double, Short> map) {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertFalse(map.containsKey(null));
        assertFalse(map.containsKey(Double.MAX_VALUE));
        assertFalse(map.containsKey(Double.MIN_VALUE));
        assertFalse(map.containsValue(null));
        assertFalse(map.containsValue((Object) 1.1));
    }
}