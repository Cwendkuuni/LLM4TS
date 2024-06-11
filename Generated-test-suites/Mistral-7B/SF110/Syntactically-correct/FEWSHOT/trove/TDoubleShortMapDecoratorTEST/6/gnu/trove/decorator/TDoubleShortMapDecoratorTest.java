package org.apache.commons.cli; // Note: This package should be changed to the actual package name of TDoubleShortMapDecorator

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(null));
        assertNull(decorator.get(1.0));
        assertNull(decorator.remove(1.0));
    }

    @Test
    public void testPut() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertEquals(3, decorator.size());
        assertFalse(decorator.isEmpty());

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertEquals((short) 3, decorator.get(3.0));

        assertNull(decorator.get(4.0));

        assertEquals((short) 1, decorator.remove(1.0));
        assertEquals((short) 2, decorator.remove(2.0));
        assertEquals((short) 3, decorator.remove(3.0));

        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        TDoubleShortMap anotherMap = new TDoubleShortMap();
        anotherMap.put(1.0, (short) 1);
        anotherMap.put(2.0, (short) 2);
        anotherMap.put(3.0, (short) 3);

        decorator.putAll(anotherMap);

        assertEquals(3, decorator.size());
        assertFalse(decorator.isEmpty());

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertEquals((short) 3, decorator.get(3.0));

        Set<Entry<Double, Short>> entries = decorator.entrySet();
        TDoubleShortIterator it = map.iterator();

        while (it.hasNext()) {
            Map.Entry<Double, Short> entry = it.next();
            assertEquals(entry.getKey(), decorator.get(entry.getKey()));
            assertEquals(entry.getValue(), decorator.get(entry.getKey()));
        }
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        Set<Entry<Double, Short>> entries = decorator.entrySet();

        assertEquals(3, entries.size());

        Iterator<Entry<Double, Short>> entryIterator = entries.iterator();
        Entry<Double, Short> entry;

        entry = entryIterator.next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short) 1, entry.getValue());

        entry = entryIterator.next();
        assertEquals(2.0, entry.getKey());
        assertEquals((short) 2, entry.getValue());

        entry = entryIterator.next();
        assertEquals(3.0, entry.getKey());
        assertEquals((short) 3, entry.getValue());

        assertFalse(entryIterator.hasNext());
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsKey(3.0));

        assertFalse(decorator.containsKey(4.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertTrue(decorator.containsValue((short) 3));

        assertFalse(decorator.containsValue((short) 0));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        oos.writeObject(decorator);

        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();

        assertEquals(3, deserializedDecorator.size());
        assertEquals((short) 1, deserializedDecorator.get(1.0));
        assertEquals((short) 2, deserializedDecorator.get(2.0));
        assertEquals((short) 3, deserializedDecorator.get(3.0));
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        oos.writeObject(decorator);
        oos.flush();

        byte[] serializedData = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        ObjectInputStream ois = new ObjectInputStream(bais);

        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) ois.readObject();

        assertEquals(3, deserializedDecorator.size());
        assertEquals((short) 1, deserializedDecorator.get(1.0));
        assertEquals((short) 2, deserializedDecorator.get(2.0));
        assertEquals((short) 3, deserializedDecorator.get(3.0));
    }
}