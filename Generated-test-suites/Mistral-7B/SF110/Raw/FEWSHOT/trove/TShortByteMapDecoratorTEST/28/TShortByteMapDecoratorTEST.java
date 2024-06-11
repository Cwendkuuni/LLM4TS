package org.apache.commons.cli; // Replace with the actual package name of your tests, if needed

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        assertEquals(0, decorator.size());
        assertNull(decorator.get(-1));
        assertNull(decorator.get((short) 0));

        assertTrue(decorator.isEmpty());
        assertFalse(decorator.containsKey(-1));
        assertFalse(decorator.containsKey((short) 0));
    }

    @Test
    public void testPutGetRemove() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 1);
        assertEquals((byte) 1, decorator.get((short) 1));

        decorator.put((short) 2, (byte) 2);
        assertEquals((byte) 2, decorator.get((short) 2));

        decorator.remove((short) 1);
        assertNull(decorator.get((short) 1));

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutGetRemoveAll() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);
        decorator.put((short) 3, (byte) 3);

        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
        Map.Entry<Short, Byte> entry = iterator.next();

        assertEquals((short) 1, entry.getKey());
        assertEquals((byte) 1, entry.getValue());
        iterator.remove();

        entry = iterator.next();
        assertEquals((short) 2, entry.getKey());
        assertEquals((byte) 2, entry.getValue());
        iterator.remove();

        entry = iterator.next();
        assertEquals((short) 3, entry.getKey());
        assertEquals((byte) 3, entry.getValue());

        iterator.hasNext(); // should return false

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertTrue(decorator.containsValue((byte) 1));
        assertTrue(decorator.containsValue((byte) 2));
        assertFalse(decorator.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 2));
        assertFalse(decorator.containsKey((short) 3));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);
        decorator.put((short) 3, (byte) 3);

        assertEquals(3, decorator.size());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map1 = new TShortByteMapImpl();
        TShortByteMap map2 = new TShortByteMapImpl();
        TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(map1);
        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator(map2);

        decorator1.put((short) 1, (byte) 1);
        decorator1.put((short) 2, (byte) 2);

        decorator2.putAll(decorator1);

        assertEquals(2, decorator2.size());
        assertTrue(decorator2.containsKey((short) 1));
        assertTrue(decorator2.containsKey((short) 2));
    }

    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        decorator.writeExternal(out);

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) in.readObject();

        assertEquals(2, deserializedDecorator.size());
        assertTrue(deserializedDecorator.containsKey((short) 1));
        assertTrue(deserializedDecorator.containsKey((short) 2));
    }
}