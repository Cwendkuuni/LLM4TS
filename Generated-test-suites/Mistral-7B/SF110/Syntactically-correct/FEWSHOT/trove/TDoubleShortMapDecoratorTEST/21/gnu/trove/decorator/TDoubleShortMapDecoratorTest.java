package org.apache.commons.cli; // Please note that the package name in the tests should match the package name of the class under test

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        Short returnedValue = decorator.put(putKey, putValue);
        assertEquals(putValue, returnedValue);
        assertTrue(decorator.containsKey(putKey));
        assertEquals(1, decorator.size());

        Short putValue2 = Short.valueOf((short) 2);
        Double putKey2 = Double.valueOf(2.0);
        Short returnedValue2 = decorator.put(putKey2, putValue2);
        assertEquals(putValue2, returnedValue2);
        assertTrue(decorator.containsKey(putKey2));
        assertEquals(2, decorator.size());
        assertNull(decorator.put(putKey, (Short)null));
        assertNull(decorator.get(putKey));
        assertEquals(2, decorator.size());
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        Short returnedValue = decorator.put(putKey, putValue);
        assertEquals(putValue, decorator.get(putKey));

        Short putValue2 = Short.valueOf((short) 2);
        Double putKey2 = Double.valueOf(2.0);
        Short returnedValue2 = decorator.put(putKey2, putValue2);
        assertEquals(putValue2, decorator.get(putKey2));

        assertNull(decorator.get(null));
        assertNull(decorator.get(Double.valueOf(3.0)));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        decorator.put(putKey, putValue);
        assertEquals(1, decorator.size());
        assertTrue(decorator.containsKey(putKey));
        decorator.clear();
        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(putKey));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        decorator.put(putKey, putValue);
        assertEquals(1, decorator.size());
        assertTrue(decorator.containsKey(putKey));

        Short removedValue = decorator.remove(putKey);
        assertEquals(putValue, removedValue);
        assertNull(decorator.get(putKey));
        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(putKey));

        assertNull(decorator.remove(null));
        assertNull(decorator.remove(Double.valueOf(2.0)));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        decorator.put(putKey, putValue);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();

        assertEquals(putKey, entry.getKey());
        assertEquals(putValue, entry.getValue());

        iterator.hasNext();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short) 1);
        decorator.put(Double.valueOf(1.0), putValue);
        assertTrue(decorator.containsValue(putValue));
        assertFalse(decorator.containsValue((Short)null));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        decorator.put(putKey, putValue);
        assertTrue(decorator.containsKey(putKey));
        assertFalse(decorator.containsKey(null));
        assertFalse(decorator.containsKey(Double.valueOf(2.0)));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());

        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        decorator.put(putKey, putValue);
        assertEquals(1, decorator.size());

        Short putValue2 = Short.valueOf((short) 2);
        Double putKey2 = Double.valueOf(2.0);
        decorator.put(putKey2, putValue2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        Map<Double, Short> mapToPut = new TDoubleShortOpenHashMap();
        Short putValue = Short.valueOf((short) 1);
        Double putKey = Double.valueOf(1.0);
        mapToPut.put(putKey, putValue);
        Short putValue2 = Short.valueOf((short) 2);
        Double putKey2 = Double.valueOf(2.0);
        mapToPut.put(putKey2, putValue2);

        decorator.putAll(mapToPut);
        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(putKey));
        assertTrue(decorator.containsKey(putKey2));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(Double.valueOf(1.0), (short)1);
        decorator.put(Double.valueOf(2.0), (short)2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutput);
        objectOutput.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator deserializedMap = (TDoubleShortMapDecorator)objectInput.readObject();
        objectInput.close();

        assertEquals(decorator.size(), deserializedMap.size());
        assertArrayEquals(decorator.getMap().toArray(), deserializedMap.getMap().toArray());
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator originalMap = new TDoubleShortMapDecorator();
        originalMap.put(Double.valueOf(1.0), (short)1);
        originalMap.put(Double.valueOf(2.0), (short)2);

        TDoubleShortMapDecorator clonedMap = (TDoubleShortMapDecorator)originalMap.clone();

        assertEquals(originalMap.size(), clonedMap.size());
        assertArrayEquals(originalMap.getMap().toArray(), clonedMap.getMap().toArray());
    }
}