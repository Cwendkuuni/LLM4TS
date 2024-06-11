package org.apache.commons.cli; // Replace with the actual package name of your test class

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.iterator.TShortIterator;
import org.junit.Before;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMap map;
    private TDoubleShortMapDecorator decorator;

    @Before
    public void setUp() {
        map = TMap.decorate(THashMap.create(), TDoubleShortMap.class);
        decorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testGetMap() {
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPut() {
        final double key = 1.0;
        final short value = 1;

        final Short putValue = decorator.put(key, value);
        assertNull(putValue);
        assertEquals(value, decorator.get(key));

        final Short existingValue = (Short)1;
        decorator.put(key, existingValue);
        assertEquals(existingValue, decorator.get(key));
        assertNull(decorator.put(key, value));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        decorator.put(null, (Short)1);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        decorator.put((Double)1.0, null);
    }

    @Test
    public void testGet() {
        decorator.put(1.0, (short)1);
        assertEquals((short)1, decorator.get(1.0));

        decorator.put(2.0, (short)2);
        assertEquals((short)2, decorator.get(2.0));
        assertEquals(null, decorator.get(3.0));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        decorator.get(null);
    }

    @Test
    public void testClear() {
        decorator.put(1.0, (short)1);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        decorator.put(1.0, (short)1);
        final Short removedValue = decorator.remove(1.0);
        assertEquals(removedValue, (Short)1);
        assertFalse(decorator.containsKey(1.0));

        decorator.put(2.0, (short)2);
        final Short noRemovedValue = decorator.remove(1.0);
        assertNull(noRemovedValue);
        assertTrue(decorator.containsKey(2.0));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        decorator.remove(null);
    }

    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        final Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        final Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short)1, entry.getValue());

        entry = iterator.next();
        assertEquals(2.0, entry.getKey());
        assertEquals((short)2, entry.getValue());

        iterator.remove();
        assertEquals(1, entrySet.size());
    }

    @Test
    public void testContainsValue() {
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        assertTrue(decorator.containsValue((short)1));
        assertTrue(decorator.containsValue((short)2));
        assertFalse(decorator.containsValue((short)3));
    }

    @Test
    public void testContainsKey() {
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test   
    public void testSize() {
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        assertEquals(2, decorator.size());
    }

    @Test   
    public void testIsEmpty() {
        decorator.clear();
        assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short)1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap sourceMap = TMap.decorate(THashMap.create(), TDoubleShortMap.class);
        sourceMap.put(1.0, (short)1);
        sourceMap.put(2.0, (short)2);

        decorator.putAll(sourceMap);
        assertEquals(2, decorator.size());

        final Iterator<Entry<Double, Short>> iterator = decorator.entrySet().iterator();
        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short)1, entry.getValue());

        entry = iterator.next();
        assertEquals(2.0, entry.getKey());
        assertEquals((short)2, entry.getValue());
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(out);
        decorator.writeExternal(objectOutput);

        ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
        deserializedDecorator.readExternal(input);

        assertEquals(decorator.size(), deserializedDecorator.size());

        final Iterator<Entry<Double, Short>> deserializedIterator = deserializedDecorator.entrySet().iterator();
        Entry<Double, Short> deserializedEntry = deserializedIterator.next();
        assertEquals(1.0, deserializedEntry.getKey());
        assertEquals((short)1, deserializedEntry.getValue());

        deserializedEntry = deserializedIterator.next();
        assertEquals(2.0, deserializedEntry.getKey());
        assertEquals((short)2, deserializedEntry.getValue());
    }
}