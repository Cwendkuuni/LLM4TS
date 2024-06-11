package org.apache.commons.cli; // Change the package name to the actual one if necessary

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(null));
        assertFalse(map.containsKey(1.0));
        assertFalse(map.containsValue((short) 1));
    }

    @Test
    public void testPutGetAndRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertEquals(3, map.size());
        assertFalse(map.isEmpty());
        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
        assertEquals((short) 3, map.get(3.0));
        assertNull(map.get(4.0));
        map.remove(1.0);
        assertEquals(2, map.size());
        assertFalse(map.containsKey(1.0));
        assertTrue(map.containsKey(2.0));
        assertTrue(map.containsKey(3.0));
        assertFalse(map.containsValue((short) 1));
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        Set<Entry<Double, Short>> entrySet = map.entrySet();
        assertEquals(3, entrySet.size());
        Iterator<Entry<Double, Short>> it = entrySet.iterator();
        Entry<Double, Short> entry;
        while (it.hasNext()) {
            entry = it.next();
            Double key = entry.getKey();
            Short value = entry.getValue();
            if (key.equals(1.0)) {
                assertEquals((short) 1, value);
            } else if (key.equals(2.0)) {
                assertEquals((short) 2, value);
            } else if (key.equals(3.0)) {
                assertEquals((short) 3, value);
            }
        }
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertTrue(map.containsValue((short) 1));
        assertTrue(map.containsValue((short) 2));
        assertTrue(map.containsValue((short) 3));
        assertFalse(map.containsValue((short) 4));
        map.clear();
        assertFalse(map.containsValue((short) 1));
        assertFalse(map.containsValue((short) 2));
        assertFalse(map.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsKey(2.0));
        assertTrue(map.containsKey(3.0));
        assertFalse(map.containsKey(4.0));
        map.clear();
        assertFalse(map.containsKey(1.0));
        assertFalse(map.containsKey(2.0));
        assertFalse(map.containsKey(3.0));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        map.writeExternal(objectOutput);
        objectOutput.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator mapFromBytes = (TDoubleShortMapDecorator) objectInput.readObject();
        objectInput.close();
        assertEquals(2, mapFromBytes.size());
        assertTrue(mapFromBytes.containsKey(1.0));
        assertTrue(mapFromBytes.containsKey(2.0));
    }
}