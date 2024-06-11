package org.apache.commons.cli; // Replace with the actual package name of your test class

import gnu.trove.iterator.*;
import gnu.trove.map.*;
import java.util.*;
import org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        Assert.assertEquals(0, underlyingMap.size());
        Assert.assertNull(decorator.get(null));
        Assert.assertNull(decorator.get(1.0));

        decorator.clear();
        Assert.assertEquals(0, underlyingMap.size());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        Assert.assertEquals((short) 1, decorator.get(1.0));

        decorator.put(2.0, (short) 2);
        underlyingMap.put(2.0, (short) 2);

        Assert.assertEquals((short) 1, decorator.get(1.0));
        Assert.assertEquals((short) 2, decorator.get(2.0));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        Assert.assertEquals((short) 1, decorator.get(1.0));
        Assert.assertNull(decorator.get(2.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        decorator.clear();
        Assert.assertEquals(0, underlyingMap.size());
        Assert.assertNull(decorator.get(1.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        decorator.remove(1.0);
        Assert.assertEquals(0, underlyingMap.size());
        Assert.assertNull(decorator.get(1.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        Assert.assertNotNull(entrySet);
        Assert.assertEquals(1, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        Assert.assertEquals(1.0, entry.getKey());
        Assert.assertEquals((short) 1, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        Assert.assertTrue(decorator.containsValue((short) 1));
        Assert.assertFalse(decorator.containsValue((short) 2));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        Assert.assertTrue(decorator.containsKey(1.0));
        Assert.assertFalse(decorator.containsKey(2.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        decorator.put(1.0, (short) 1);
        underlyingMap.put(1.0, (short) 1);

        Assert.assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        Assert.assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short) 1);

        Assert.assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap underlyingMap = decorator.getMap();

        Map<Double, Short> mapToAdd = new HashMap<>();
        mapToAdd.put(1.0, (short) 1);
        mapToAdd.put(2.0, (short) 2);

        decorator.putAll(mapToAdd);

        Assert.assertEquals(2, decorator.size());
        Assert.assertEquals((short) 1, decorator.get(1.0));
        Assert.assertEquals((short) 2, decorator.get(2.0));
    }
}