package org.apache.commons.cli; // Assuming you want to test TDoubleShortMapDecorator in the context of the Apache Commons CLI package

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = Short.valueOf((short) 1);
        Double key = Double.valueOf(1.0);
        decorator.put(key, value);
        assertEquals(1, decorator.size());
        assertTrue(decorator.containsKey(key));
        assertEquals(value, decorator.get(key));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(null, (Short) 1);
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.get(null));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = Short.valueOf((short) 1);
        Double key = Double.valueOf(1.0);
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, null);
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value1 = Short.valueOf((short) 1);
        Double key1 = Double.valueOf(1.0);
        decorator.put(key1, value1);

        Short value2 = Short.valueOf((short) 2);
        Double key2 = Double.valueOf(2.0);
        decorator.put(key2, value2);

        Set<Entry<Double, Short>> entries = decorator.entrySet();
        assertEquals(2, entries.size());

        Iterator<Entry<Double, Short>> entryIterator = entries.iterator();
        Entry<Double, Short> entry1 = entryIterator.next();
        assertEquals(key1, entry1.getKey());
        assertEquals(value1, entry1.getValue());

        Entry<Double, Short> entry2 = entryIterator.next();
        assertEquals(key2, entry2.getKey());
        assertEquals(value2, entry2.getValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = Short.valueOf((short) 1);
        decorator.put(1.0, value);
        assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = Short.valueOf((short) 1);
        decorator.put(1.0, value);
        decorator.clear();
        assertEquals(0, decorator.size());
        assertFalse(decorator.isEmpty()); // false, because the map is empty but not null
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) objectInput.readObject();
        objectInput.close();

        assertEquals(2, deserializedDecorator.size());
        assertTrue(deserializedDecorator.containsKey(1.0));
        assertTrue(deserializedDecorator.containsKey(2.0));
        assertEquals((short) 1, deserializedDecorator.get(1.0));
        assertEquals((short) 2, deserializedDecorator.get(2.0));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator) decorator.clone();

        assertEquals(2, clonedDecorator.size());
        assertTrue(clonedDecorator.containsKey(1.0));
        assertTrue(clonedDecorator.containsKey(2.0));
        assertEquals((short) 1, clonedDecorator.get(1.0));
        assertEquals((short) 2, clonedDecorator.get(2.0));
    }
}