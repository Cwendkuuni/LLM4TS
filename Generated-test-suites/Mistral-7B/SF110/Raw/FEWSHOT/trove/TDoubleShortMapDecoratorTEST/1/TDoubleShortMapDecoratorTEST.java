package org.apache.commons.cli;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;

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
        assertEquals(null, decorator.put(1.0, (short) 1));
        assertEquals((short) 1, decorator.get(1.0));
        assertEquals(1, decorator.size());

        assertEquals(null, decorator.put(1.0, (short) 2));
        assertEquals((short) 2, decorator.get(1.0));
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.get(null));
    }

    @Test
    public void testGetWrongKeyType() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.get(new Object()));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.clear();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertEquals((short) 1, decorator.remove(1.0));
        assertEquals(1, decorator.size());
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.remove(null));
    }

    @Test
    public void testRemoveWrongKeyType() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        assertNull(decorator.remove(new Object()));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey().doubleValue());
        assertEquals((short) 1, entry.getValue().shortValue());

        entry = iterator.next();
        assertEquals(2.0, entry.getKey().doubleValue());
        assertEquals((short) 2, entry.getValue().shortValue());

        assertTrue(iterator.hasNext());
        iterator.remove();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertTrue(decorator.containsValue((short) 1));
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
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        decorator.putAll(map);
        assertEquals(2, decorator.size());

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(1.0, decorator.wrapKey(1.0));
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(1.0, decorator.unwrapKey(1.0), 0.001);
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals((short) 1, decorator.wrapValue((short) 1));
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals((short) 1, decorator.unwrapValue((short) 1));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
        in.close();

        assertEquals(2, deserializedDecorator.size());
        assertTrue(deserializedDecorator.containsKey(1.0));
        assertTrue(deserializedDecorator.containsKey(2.0));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();

        assertEquals(2, clonedDecorator.size());
        assertTrue(clonedDecorator.containsKey(1.0));
        assertTrue(clonedDecorator.containsKey(2.0));
    }
}