package com.example.junit.test; // replace with your testing package name

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testConstructor() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertNotNull(mapDecorator);
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        assertNotNull(decorator);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertNull(mapDecorator.put(null, (short) 1));
        mapDecorator.put(1.0, (short) 1);
        assertNotNull(mapDecorator.get(1.0));
        assertEquals((short) 1, mapDecorator.get(1.0));
        mapDecorator.put(1.0, (short) 2);
        assertEquals((short) 2, mapDecorator.get(1.0));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertNull(mapDecorator.get(null));
        mapDecorator.put(1.0, (short) 1);
        assertNotNull(mapDecorator.get(1.0));
        assertEquals((short) 1, mapDecorator.get(1.0));
        assertNull(mapDecorator.get(2.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        assertNotNull(mapDecorator.remove(1.0));
        assertEquals(0, mapDecorator.size());
        assertNull(mapDecorator.remove(1.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        assertEquals(2, entrySet.size());
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey(), 0.001);
        assertEquals((short) 1, entry.getValue());
        entry = iterator.next();
        assertEquals(2.0, entry.getKey(), 0.001);
        assertEquals((short) 2, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        assertTrue(mapDecorator.containsValue((short) 1));
        assertTrue(mapDecorator.containsValue((short) 2));
        assertFalse(mapDecorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(2.0));
        assertFalse(mapDecorator.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals(0, mapDecorator.size());
        mapDecorator.put(1.0, (short) 1);
        assertEquals(1, mapDecorator.size());
        mapDecorator.put(2.0, (short) 2);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put(1.0, (short) 1);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        mapDecorator.putAll(map);
        assertEquals(2, mapDecorator.size());
        assertEquals(Collections.singletonMap(1.0, (short) 1), mapDecorator);
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals(1.0, mapDecorator.wrapKey(1.0));
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEqual(1.0, mapDecorator.unwrapKey(1.0), 0.001);
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals((short) 1, mapDecorator.wrapValue((short) 1));
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals((short) 1, mapDecorator.unwrapValue((short) 1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveEntry() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        mapDecorator.entrySet().remove(new Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return 1.0;
            }

            @Override
            public Short getValue() {
                return (short) 1;
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEntry() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.addEntry(new Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return 1.0;
            }

            @Override
            public Short getValue() {
                return (short) 1;
            }
        });
    }

    @Test
    public void testIteration() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.putAll(Arrays.asList(
                new Entry<Double, Short>(1.0, (short) 1),
                new Entry<Double, Short>(2.0, (short) 2),
                new Entry<Double, Short>(3.0, (short) 3)
        ));

        TDoubleShortIterator it = mapDecorator.entrySet().iterator();
        Entry<Double, Short> entry = it.next();
        assertEquals(1.0, entry.getKey(), 0.001);
        assertEquals((short) 1, entry.getValue());
        entry = it.next();
        assertEquals(2.0, entry.getKey(), 0.001);
        assertEquals((short) 2, entry.getValue());
        entry = it.next();
        assertEquals(3.0, entry.getKey(), 0.001);
        assertEquals((short) 3, entry.getValue());
        assertFalse(it.hasNext());
    }

    @Test
    public void testExternalizable() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        mapDecorator.writeExternal(objectOutputStream);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator mapDecoratorCloned = (TDoubleShortMapDecorator) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(mapDecorator, mapDecoratorCloned);
    }
}