package org.apache.commons.cli; // Replace with the actual package name of your test class

import static org.junit.Assert.*;
import gnu.trove.decorator.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testCreateEmptyMap() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateDecoratorWithMap() {
        TDoubleShortMap underlyingMap = TDoubleShortMap.decorate(new THashMap<Double, Short>(10));
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(underlyingMap);
        assertEquals(underlyingMap, decorator.getMap());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertEquals(2, map.size());
        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(null, (short) 1);
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, null);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
        assertNull(map.get(3.0));
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.get(null);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.remove(1.0);
        map.remove(2.0);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.remove(null);
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        Set<Entry<Double, Short>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> firstEntry = iterator.next();
        Entry<Double, Short> secondEntry = iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(1.0, firstEntry.getKey().doubleValue());
        assertEquals(1, firstEntry.getValue().shortValue());
        assertEquals(2.0, secondEntry.getKey().doubleValue());
        assertEquals(2, secondEntry.getValue().shortValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 1);
        assertTrue(map.containsValue((short) 1));
        assertFalse(map.containsValue((short) 2));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 1);
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsKey(2.0));
        assertFalse(map.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 1);
        map.put(3.0, (short) 1);
        assertEquals(3, map.size());
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
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        TDoubleMap<Double> underlyingMap = TDoubleMap.decorate(new THashMap<Double, Short>(5));
        TShortMap<Short> valuesMap = TShortMap.decorate(new THashMap<Short, Object>(5));
        for (Double key : underlyingMap.keySet()) {
            map.put(key, underlyingMap.get(key));
            valuesMap.put(underlyingMap.get(key), key);
        }
        Map<Double, Short> anotherMap = new TDoubleShortMapDecorator(underlyingMap);
        map.putAll(anotherMap);
        assertEquals(underlyingMap.size(), map.size());
        assertTrue(map.containsAll(anotherMap.entrySet()));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        map.writeExternal(objectOutputStream);
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TDoubleShortMapDecorator deserializedMap = (TDoubleShortMapDecorator) objectInput.readObject();
        assertEquals(2, deserializedMap.size());
        assertEquals((short) 1, deserializedMap.get(1.0));
        assertEquals((short) 2, deserializedMap.get(2.0));
    }
}