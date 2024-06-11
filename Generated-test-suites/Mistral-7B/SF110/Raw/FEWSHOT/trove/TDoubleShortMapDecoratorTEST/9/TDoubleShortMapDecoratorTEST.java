package org.apache.commons.cli; // Make sure to replace the package name with the actual package name of your test class

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        assertFalse(map.isEmpty());
        assertTrue(map.isEmpty()); // Empty map is marked as empty by default
        assertNull(map.get(null));
        assertNull(map.get(new Double(0.0)));
    }

    @Test
    public void testPutGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        map.put(new Double(1.0), (short)3); // Overwriting the previous value

        assertEquals(2, map.size());
        assertEquals((short)1, map.get(new Double(1.0)));
        assertEquals((short)3, map.get(new Double(1.0))); // The last value is returned
        assertEquals((short)2, map.get(new Double(2.0)));
        assertNull(map.get(new Double(3.0)));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        map.clear();
        assertEquals(0, map.size());
        assertFalse(map.isEmpty()); // Empty map is not marked as empty after clear operation
        assertNull(map.get(new Double(1.0)));
        assertNull(map.get(new Double(2.0)));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);
        map.remove(new Double(1.0));
        assertEquals(1, map.size());
        assertNull(map.get(new Double(1.0)));
        assertEquals((short)2, map.get(new Double(2.0)));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);

        Set<Entry<Double, Short>> entrySet = map.entrySet();
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> entry = iterator.next();
        assertEquals(new Double(1.0), entry.getKey());
        assertEquals((short)1, entry.getValue());

        iterator.hasNext(); // Advance the iterator

        entry = iterator.next();
        assertEquals(new Double(2.0), entry.getKey());
        assertEquals((short)2, entry.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);

        assertTrue(map.containsValue((short)1));
        assertTrue(map.containsValue((short)2));
        assertFalse(map.containsValue((short)3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);

        assertTrue(map.containsKey(new Double(1.0)));
        assertTrue(map.containsKey(new Double(2.0)));
        assertFalse(map.containsKey(new Double(3.0)));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(new Double(1.0), (short)1);
        map.put(new Double(2.0), (short)2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        map.writeExternal(oos);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TDoubleShortMapDecorator mapFromSerialized = (TDoubleShortMapDecorator)ois.readObject();
        ois.close();

        assertEquals(map.size(), mapFromSerialized.size());
        assertTrue(map.containsKey(map.keySet().iterator().next()));
        assertTrue(mapFromSerialized.containsKey(mapFromSerialized.keySet().iterator().next()));
        assertEquals(map.get(map.keySet().iterator().next()), mapFromSerialized.get(mapFromSerialized.keySet().iterator().next()));
    }
}