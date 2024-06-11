package org.apache.commons.cli; // Replace with the actual package name of your test class

import gnu.trove.iterator.*;
import gnu.trove.map.*;
import java.util.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testCreateEmptyMap() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateMapWithInitialMap() {
        TDoubleShortMap underlayingMap = new TDoubleShortOpenHashMap();
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(underlayingMap);
        assertEquals(underlayingMap.size(), map.size());
        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutGetAndRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
        assertNull(map.get(3.0));

        map.remove(1.0);
        assertEquals((short) 2, map.get(2.0));
        assertNull(map.get(1.0));
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.get(null));

        map.put(null, (short) 1);
        assertNotNull(map.get(null));
        assertEquals((short) 1, map.get(null));

        map.remove(null);
        assertNull(map.get(null));
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, null);
        assertNull(map.get(1.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Collection<Entry<Double, Short>> collection = new ArrayList<>();
        collection.add(new Entry<Double, Short>(1.0, (short) 1));
        map.addAll(collection);
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
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(1.0, (short) 2);

        assertTrue(map.containsValue((short) 1));
        assertTrue(map.containsValue((short) 2));
        assertFalse(map.containsValue((short) 3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.remove(1.0);
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
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> firstEntry = iterator.next();
        assertEquals(1.0, firstEntry.getKey());
        assertEquals((short) 1, firstEntry.getValue());

        Entry<Double, Short> secondEntry = iterator.next();
        assertEquals(2.0, secondEntry.getKey());
        assertEquals((short) 2, secondEntry.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testExternalizability() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        map.writeExternal(output);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream input = new ObjectInputStream(inputStream);

        TDoubleShortMapDecorator mapFromStream = (TDoubleShortMapDecorator)input.readObject();

        assertEquals(map.size(), mapFromStream.size());
        Iterator<Entry<Double, Short>> mapIterator = map.entrySet().iterator();
        Iterator<Entry<Double, Short>> mapFromStreamIterator = mapFromStream.entrySet().iterator();

        while (mapIterator.hasNext() && mapFromStreamIterator.hasNext()) {
            Entry<Double, Short> entryFromMap = mapIterator.next();
            Entry<Double, Short> entryFromStream = mapFromStreamIterator.next();

            assertEquals(entryFromMap.getKey(), entryFromStream.getKey());
            assertEquals(entryFromMap.getValue(), entryFromStream.getValue());
        }
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        TDoubleShortMapDecorator clonedMap = (TDoubleShortMapDecorator)map.clone();

        assertEquals(map.size(), clonedMap.size());
        Iterator<Entry<Double, Short>> mapIterator = map.entrySet().iterator();
        Iterator<Entry<Double, Short>> clonedMapIterator = clonedMap.entrySet().iterator();

        while (mapIterator.hasNext() && clonedMapIterator.hasNext()) {
            Entry<Double, Short> entryFromMap = mapIterator.next();
            Entry<Double, Short> entryFromClonedMap = clonedMapIterator.next();

            assertEquals(entryFromMap.getKey(), entryFromClonedMap.getKey());
            assertEquals(entryFromMap.getValue(), entryFromClonedMap.getValue());
        }
    }
}