package org.apache.commons.cli; // Note: Assuming the UtilTest class is in the same package as TDoubleShortMapDecorator for simplicity

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testTDoubleShortMapDecoratorEmptyConstruction() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testTDoubleShortMapDecoratorWithMapConstruction() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());

        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        assertEquals(2, decorator.size());
        assertFalse(decorator.isEmpty());

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertNull(decorator.get(3.0));
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertEquals(2, decorator.size());
        assertFalse(decorator.isEmpty());

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertNull(decorator.get(3.0));

        decorator.put(3.0, (short) 3);

        assertEquals(3, decorator.size());
        assertFalse(decorator.isEmpty());

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertEquals((short) 3, decorator.get(3.0));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(null, (short) 1);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertNull(decorator.get(3.0));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        decorator.get(null);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertEquals(2, decorator.size());
        assertFalse(decorator.isEmpty());

        decorator.clear();

        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertEquals(2, decorator.size());
        assertFalse(decorator.isEmpty());

        decorator.remove(1.0);

        assertEquals(1, decorator.size());
        assertFalse(decorator.isEmpty());

        assertNull(decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        decorator.remove(null);
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        assertEquals(2, entrySet.size());
        Iterator<Entry<Double, Short>> it = entrySet.iterator();

        Entry<Double, Short> entry1 = it.next();
        assertEquals(1.0, entry1.getKey());
        assertEquals((short) 1, entry1.getValue());

        Entry<Double, Short> entry2 = it.next();
        assertEquals(2.0, entry2.getKey());
        assertEquals((short) 2, entry2.getValue());
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
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertEquals(0, decorator.size());

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator();
        decorator1.put(1.0, (short) 1);
        decorator1.put(2.0, (short) 2);

        TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
        decorator2.put(3.0, (short) 3);
        decorator2.put(4.0, (short) 4);

        decorator1.putAll(decorator2.entrySet());

        assertEquals(4, decorator1.size());
        assertFalse(decorator1.isEmpty());

        assertEquals((short) 1, decorator1.get(1.0));
        assertEquals((short) 2, decorator1.get(2.0));
        assertEquals((short) 3, decorator1.get(3.0));
        assertEquals((short) 4, decorator1.get(4.0));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);

        decorator.writeExternal(out);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInputStream(inputStream);

        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertEquals(decorator.get(1.0), deserializedDecorator.get(1.0));
        assertEquals(decorator.get(2.0), deserializedDecorator.get(2.0));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();

        assertEquals(decorator.size(), clonedDecorator.size());
        assertEquals(decorator.get(1.0), clonedDecorator.get(1.0));
        assertEquals(decorator.get(2.0), clonedDecorator.get(2.0));
    }
}