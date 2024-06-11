package org.apache.commons.cli; // Replace with the actual package name of the test class

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        assertFalse(map.isEmpty());
        assertNull(map.get(null));
        assertNull(map.get(new Double(0.0)));
        assertFalse(map.containsKey(null));
        assertFalse(map.containsKey(new Double(0.0)));
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        assertEquals(2, map.size());
        assertTrue(map.containsKey(new Double(1.0)));
        assertTrue(map.containsKey(new Double(2.0)));
        assertEquals((short)1, map.get(new Double(1.0)));
        assertEquals((short)2, map.get(new Double(2.0)));
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        assertNull(map.get(null));
    }

    @Test
    public void testGetWrongKeyType() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        assertNull(map.get(new Integer(1)));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        map.clear();
        assertEquals(0, map.size());
        assertFalse(map.isEmpty());
        assertNull(map.get(new Double(1.0)));
        assertNull(map.get(new Double(2.0)));
        assertFalse(map.containsKey(new Double(1.0)));
        assertFalse(map.containsKey(new Double(2.0)));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        Short removedValue = map.remove(new Double(1.0));
        assertEquals(1, map.size());
        assertFalse(map.containsKey(new Double(1.0)));
        assertTrue(map.containsKey(new Double(2.0)));
        assertEquals((short)2, map.get(new Double(2.0)));
        assertNull(removedValue);
    }

    @Test
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        assertNull(map.remove(null));
        assertEquals(2, map.size());
        assertTrue(map.containsKey(new Double(1.0)));
        assertTrue(map.containsKey(new Double(2.0)));
    }

    @Test
    public void testRemoveWrongKeyType() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        assertNull(map.remove(new Integer(1)));
        assertEquals(2, map.size());
        assertTrue(map.containsKey(new Double(1.0)));
        assertTrue(map.containsKey(new Double(2.0)));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        Set<Entry<Double, Short>> entries = map.entrySet();
        assertEquals(2, entries.size());
        assertTrue(entries.contains(new org.junit.platform.commons.util.Pair<>(new Double(1.0), new Short((short)1))));
        assertTrue(entries.contains(new org.junit.platform.commons.util.Pair<>(new Double(2.0), new Short((short)2))));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        assertTrue(map.containsValue((short)1));
        assertTrue(map.containsValue((short)2));
        assertFalse(map.containsValue((short)3));
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map1 = new TDoubleShortMapDecorator();
        map1.put(new Double(1.0), (short)1);
        TDoubleShortMapDecorator map2 = new TDoubleShortMapDecorator();
        map2.put(new Double(2.0), (short)2);
        map1.putAll(map2);
        assertEquals(2, map1.size());
        assertTrue(map1.containsKey(new Double(1.0)));
        assertTrue(map1.containsKey(new Double(2.0)));
        assertEquals((short)1, map1.get(new Double(1.0)));
        assertEquals((short)2, map1.get(new Double(2.0)));
    }
}