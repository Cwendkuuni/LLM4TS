package com.example.tests;

import static org.junit.Assert.*;

import gnu.trove.decorator.TShortByteMap;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TShortByteMapDecoratorTest {

    @Test
    public void testConstructor() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNotNull(decorator);
        
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decoratorWithMap = new TShortByteMapDecorator(map);
        assertEquals(map, decoratorWithMap.getMap());
    }

    @Test
    public void testGetMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.getMap());
        
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decoratorWithMap = new TShortByteMapDecorator(map);
        assertSame(map, decoratorWithMap.getMap());
    }

    @Test
    public void testNullKeyAndValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        assertNull(decorator.put((Short)null, (Byte)null));
        assertFalse(decorator.containsKey((Short)null));
        assertFalse(decorator.containsKey((Object)null));
        assertNull(decorator.get((Short)null));
        assertNull(decorator.get((Object)null));
        assertEquals(0, decorator.size());
        decorator.clear();

        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decoratorWithMap = new TShortByteMapDecorator(map);

        assertNull(decoratorWithMap.put((Short)null, (Byte)null));
        assertFalse(decoratorWithMap.containsKey((Short)null));
        assertFalse(decoratorWithMap.containsKey((Object)null));
        assertNull(decoratorWithMap.get((Short)null));
        assertNull(decoratorWithMap.get((Object)null));
        assertEquals(0, decoratorWithMap.size());
        decoratorWithMap.clear();
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte valueBefore = decorator.put((Short)1, (Byte)1);
        assertNull(valueBefore);
        Byte valueAfter = decorator.put((Short)1, (Byte)2);
        assertSame((Byte)1, valueBefore);
        assertSame((Byte)2, valueAfter);
        assertTrue(decorator.containsKey((Short)1));
        assertEquals((Byte)2, decorator.get((Short)1));

        decorator.clear();
        Byte valueBefore2 = decorator.put((Short)2, (Byte)3);
        assertNull(valueBefore2);
        Byte valueAfter2 = decorator.put((Short)2, (Byte)4);
        assertSame((Byte)3, valueBefore2);
        assertSame((Byte)4, valueAfter2);
        assertTrue(decorator.containsKey((Short)2));
        assertEquals((Byte)4, decorator.get((Short)2));
        
        Byte valueBefore3 = decorator.put((Short)1, (Byte)5);
        assertSame((Byte)4, valueBefore3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put(null, null);
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        assertNull(decorator.get((Object)null));
        assertNull(decorator.get((Short)3));
        assertSame((Byte)1, decorator.get((Short)1));
        assertSame((Byte)2, decorator.get((Short)2));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);
        decorator.clear();
        assertFalse(decorator.containsKey((Short)1));
        assertFalse(decorator.containsKey((Short)2));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        Byte valueBefore = decorator.remove((Short)1);
        assertSame((Byte)1, valueBefore);
        assertFalse(decorator.containsKey((Short)1));
        assertEquals(1, decorator.size());
        
        Byte valueBefore2 = decorator.remove((Short)2);
        assertSame((Byte)2, valueBefore2);
        assertFalse(decorator.containsKey((Short)2));
        assertEquals(0, decorator.size());
        
        Byte valueBefore3 = decorator.remove((Object)null);
        assertNull(valueBefore3);
        assertFalse(decorator.containsKey((Short)1));
        assertFalse(decorator.containsKey((Short)2));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        Set<Entry<Short, Byte>> set = decorator.entrySet();
        assertFalse(set.isEmpty());
        assertEquals(2, set.size());
        
        Iterator<Entry<Short, Byte>> iterator = set.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals((Short)1, entry.getKey());
        assertEquals((Byte)1, entry.getValue());
        
        entry = iterator.next();
        assertEquals((Short)2, entry.getKey());
        assertEquals((Byte)2, entry.getValue());
        
        iterator.remove();
        assertEquals(1, set.size());
        
        Set<Entry<Short, Byte>> set2 = new AbstractSet<Entry<Short, Byte>>() {};
        decorator.clear();
        set2.addAll(decorator.entrySet());
        assertTrue(set2.isEmpty());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        assertTrue(decorator.containsValue((Byte)1));
        assertTrue(decorator.containsValue((Byte)2));
        assertFalse(decorator.containsValue(new Byte((byte)3)));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        assertTrue(decorator.containsKey((Short)1));
        assertTrue(decorator.containsKey((Short)2));
        assertFalse(decorator.containsKey((Object)null));
        assertFalse(decorator.containsKey((Short)3));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        assertEquals(2, decorator.size());
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        TShortByteMap map = new TShortByteMap();
        map.put((Short)1, (Byte)1);
        map.put((Short)2, (Byte)2);

        decorator.putAll(map);
        assertEquals(map.size(), decorator.size());
        assertEquals(map.containsKey((Short)1), decorator.containsKey((Short)1));
        assertEquals(map.containsKey((Short)2), decorator.containsKey((Short)2));
        assertEquals(map.get((Short)1), decorator.get((Short)1));
        assertEquals(map.get((Short)2), decorator.get((Short)2));

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short)1, (Byte)1);
        decorator.put((Short)2, (Byte)2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutput);
        objectOutput.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)objectInput.readObject();
        objectInput.close();

        assertEquals(decorator.size(), decorator2.size());
        assertEquals(decorator.containsKey((Short)1), decorator2.containsKey((Short)1));
        assertEquals(decorator.containsKey((Short)2), decorator2.containsKey((Short)2));
        assertEquals(decorator.get((Short)1), decorator2.get((Short)1));
        assertEquals(decorator.get((Short)2), decorator2.get((Short)2));

        decorator.clear();
        decorator2.clear();
        assertTrue(decorator.isEmpty());
        assertTrue(decorator2.isEmpty());
    }
}