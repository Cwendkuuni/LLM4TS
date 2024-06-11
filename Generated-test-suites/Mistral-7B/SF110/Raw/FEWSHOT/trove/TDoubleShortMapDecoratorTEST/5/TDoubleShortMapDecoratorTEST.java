package org.apache.commons.cli; // assuming you want to test the TDoubleShortMapDecorator class in the same package for test case simplicity

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
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
        assertNotNull(decorator.get(1.0));
        assertEquals((short) 1, decorator.get(1.0));
        assertNotNull(decorator.get(2.0));
        assertEquals((short) 2, decorator.get(2.0));
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.get(null));
        decorator.put(null, (short) 0);
        assertTrue(decorator.containsKey(null));
        assertEquals((short) 0, decorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, null);
        assertNull(decorator.get(1.0));
        decorator.put(1.0, (short) 1);
        assertNotNull(decorator.get(1.0));
        assertEquals((short) 1, decorator.get(1.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.clear();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.remove(1.0);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        Iterator<Entry<Double, Short>> entryIterator = entrySet.iterator();
        Entry<Double, Short> entry = entryIterator.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short) 1, entry.getValue());
        entry = entryIterator.next();
        assertEquals(2.0, entry.getKey());
        assertEquals((short) 2, entry.getValue());
    }

    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutputStream);
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)objectInput.readObject();
        assertEquals(decorator.size(), deserializedDecorator.size());
        Iterator<Map.Entry<Double, Short>> deserializedIterator = deserializedDecorator.entrySet().iterator();
        Map.Entry<Double, Short> deserializedEntry = deserializedIterator.next();
        assertEquals(decorator.get(1.0), deserializedEntry.getValue());
        assertEquals(decorator.get(2.0), deserializedEntry.getValue());
    }
}