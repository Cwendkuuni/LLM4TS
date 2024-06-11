package org.apache.commons.cli; // Assuming you want to keep the test package the same

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertIterableEquals;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertFalse(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get(Short.MIN_VALUE));
        assertNull(map.remove(Short.MIN_VALUE));
    }

    @Test
    public void testBasicPutGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        assertEquals(2, map.size());
        assertEquals(value1, map.get((short)1));
        assertEquals(value2, map.get((short)2));
    }

    @Test
    public void testPutOverwrite() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)1, value2);
        assertEquals(1, map.size());
        assertEquals(value2, map.get((short)1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put(null, (byte)1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValuePut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, null);
    }

    @Test
    public void testGetNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, Byte.valueOf((byte)1));
        assertNull(map.get(null));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        assertEquals(2, map.size());
        assertEquals(value1, map.get((short)1));
        assertEquals(value2, map.get((short)2));
        map.remove((short)1);
        assertEquals(1, map.size());
        assertNull(map.get((short)1));
        assertEquals(value2, map.get((short)2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.remove(null);
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        assertTrue(map.containsValue(value1));
        assertTrue(map.containsValue(value2));
        assertFalse(map.containsValue(Byte.valueOf((byte)3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.containsValue(null);
    }

    @Test    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        assertTrue(map.containsKey((short)1));
        assertTrue(map.containsKey((short)2));
        assertFalse(map.containsKey((short)3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.containsKey(null);
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        Map<Short, Byte> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new Entry<Short, Byte>((short)1, value1)));
        assertTrue(entrySet.contains(new Entry<Short, Byte>((short)2, value2)));
        assertFalse(entrySet.contains(new Entry<Short, Byte>((short)3, null)));
    }

    @Test
    public void testEntrySetIteration() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        Map.Entry<Short, Byte> entry;
        Iterator<Map.Entry<Short, Byte>> iterator = map.entrySet().iterator();
        entry = iterator.next();
        assertEquals((short)1, entry.getKey());
        assertEquals(value1, entry.getValue());
        entry = iterator.next();
        assertEquals((short)2, entry.getKey());
        assertEquals(value2, entry.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEntrySetEmptyIteration() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Map.Entry<Short, Byte> entry = map.entrySet().iterator().next();
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator map1 = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        TShortByteMapDecorator map2 = new TShortByteMapDecorator();
        map2.put((short)1, value1);
        map2.put((short)2, value2);
        map1.putAll(map2);
        assertEquals(2, map1.size());
        assertEquals(value1, map1.get((short)1));
        assertEquals(value2, map1.get((short)2));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.get(Short.MIN_VALUE));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(map);
        objStream.close();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInput = new ObjectInputStream(byteInput);
        TShortByteMapDecorator mapFromStream = (TShortByteMapDecorator)objInput.readObject();
        objInput.close();
        assertEquals(map, mapFromStream);
        assertEquals(2, mapFromStream.size());
        assertEquals(value1, mapFromStream.get((short)1));
        assertEquals(value2, mapFromStream.get((short)2));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        map.put((short)1, value1);
        map.put((short)2, value2);
        TShortByteMapDecorator clone = (TShortByteMapDecorator)map.clone();
        assertEquals(map, clone);
        assertEquals(2, clone.size());
        assertEquals(value1, clone.get((short)1));
        assertEquals(value2, clone.get((short)2));
    }
}