package org.apache.commons.cli; // replaced package name with the one from the provided code

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Double key1 = 1.0;
        Short value1 = 1;
        Short valuePut = decorator.put(key1, value1);
        assertEquals(value1, valuePut);
        assertNull(decorator.get(null));
        assertNull(decorator.get(key1));
        assertEquals(value1, decorator.get(key1));
        assertEquals(1, decorator.size());

        Double key2 = 2.0;
        Short value2 = 2;
        Short valuePut2 = decorator.put(key2, value2);
        assertEquals(value2, valuePut2);
        assertEquals(value1, decorator.get(key1));
        assertEquals(value2, decorator.get(key2));
        assertEquals(2, decorator.size());
    }

    @Test
    public void testGet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Double key = 1.0;
        Short value = 1;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
        assertEquals(null, decorator.get(null));
        assertEquals(null, decorator.get(new Object()));
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.clear();
        assertEquals(0, decorator.size());
        assertNull(decorator.get(1.0));
        assertNull(decorator.get(2.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Short removedValue = decorator.remove(1.0);
        assertEquals((short) 1, removedValue);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short) 1, entry.getValue());
        iterator.hasNext();
        entry = iterator.next();
        assertEquals(2.0, entry.getKey());
        assertEquals((short) 2, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue(Short.valueOf(3)));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsKey(Double.valueOf(1.0)));
        assertTrue(decorator.containsKey(Double.valueOf(2.0)));
        assertFalse(decorator.containsKey(Double.valueOf(3.0)));
    }

    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        assertEquals(0, decorator.size());
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        assertTrue(decorator.isEmpty());
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map1 = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator(map1);
        decorator1.put(1.0, (short) 1);
        TDoubleShortMap map2 = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator(map2);
        decorator2.put(2.0, (short) 2);
        decorator1.putAll(decorator2);
        assertEquals(2, decorator1.size());
        assertEquals((short) 1, decorator1.get(1.0));
        assertEquals((short) 2, decorator1.get(2.0));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutput);
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TDoubleShortMapDecorator decoratedMap = (TDoubleShortMapDecorator) objectInput.readObject();
        assertEquals(map, decoratedMap.getMap());
    }
}