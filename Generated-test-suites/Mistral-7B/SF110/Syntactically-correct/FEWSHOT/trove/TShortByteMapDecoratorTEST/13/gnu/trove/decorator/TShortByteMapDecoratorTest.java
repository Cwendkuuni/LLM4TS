package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get(null));
        assertNull(map.get(new Short((short) 1)));
        assertFalse(map.containsKey(null));
        assertFalse(map.containsKey(new Short((short) 1)));
        assertNull(map.remove(null));
        assertNull(map.remove(new Short((short) 1)));
    }

    @Test
    public void testPutGetAndRemoveSingleKeyValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = new Byte((byte) 1);
        Short key = new Short((short) 1);

        Byte putValue = map.put(key, value);
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
        assertEquals(value, map.get(key));
        assertNull(map.get(null));
        assertNull(map.get(new Short((short) 2)));

        Byte getValue = map.get(key);
        assertEquals(value, getValue);

        Byte removedValue = map.remove(key);
        assertEquals(value, removedValue);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get(key));
    }

    @Test
    public void testPutAndGetMultipleKeyValuePairs() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = new Byte((byte) 1);
        Byte value2 = new Byte((byte) 2);
        Short key1 = new Short((short) 1);
        Short key2 = new Short((short) 2);

        Byte putValue1 = map.put(key1, value1);
        Byte putValue2 = map.put(key2, value2);

        assertEquals(2, map.size());
        assertFalse(map.isEmpty());
        assertEquals(value1, map.get(key1));
        assertEquals(value2, map.get(key2));
        assertNull(map.get(null));
        assertNull(map.get(new Short((short) 3)));

        Map<Short, Byte> mapView = map.entrySet().iterator().next().getValue();
        TShortByteMapView view = (TShortByteMapView) mapView;
        assertEquals(view.size(), map.size());
        assertEquals(value1, view.get((short) 1));
        assertEquals(value2, view.get((short) 2));

        Byte getValue1 = map.get(key1);
        assertEquals(value1, getValue1);
        Byte getValue2 = map.get(key2);
        assertEquals(value2, getValue2);

        Byte removedValue1 = map.remove(key1);
        Byte removedValue2 = map.remove(key2);

        assertEquals(value1, removedValue1);
        assertEquals(value2, removedValue2);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get(key1));
        assertNull(map.get(key2));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = new Byte((byte) 1);
        Byte value2 = new Byte((byte) 2);
        Short key1 = new Short((short) 1);
        Short key2 = new Short((short) 2);

        Byte putValue1 = map.put(key1, value1);
        Byte putValue2 = map.put(key2, value2);

        assertEquals(2, map.size());
        assertFalse(map.isEmpty());
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get(key1));
        assertNull(map.get(key2));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = new Byte((byte) 1);
        Byte value2 = new Byte((byte) 2);
        Short key1 = new Short((short) 1);
        Short key2 = new Short((short) 2);

        Byte putValue1 = map.put(key1, value1);
        Byte putValue2 = map.put(key2, value2);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        map.writeExternal(objectStream);
        byte[] mapData = byteStream.toByteArray();
        TShortByteMapDecorator mapFromData = new TShortByteMapDecorator();
        mapFromData.readExternal(new ObjectInputStream(new ByteArrayInputStream(mapData)));

        assertEquals(map.size(), mapFromData.size());
        assertTrue(map.equals(mapFromData));
    }
}